import logging
import json
import base64
import requests
from dissononce.extras.meta.protocol.factory import NoiseProtocolFactory
from dissononce.processing.handshakepatterns.interactive.XX import XXHandshakePattern
from dissononce.dh.x25519.x25519 import X25519DH
from aiohttp import web
import aiohttp
from urllib.parse import urlparse
from . import measure

logger = logging.getLogger(__name__)

keypair = X25519DH().generate_keypair()
protocol = NoiseProtocolFactory().get_noise_protocol('Noise_XX_25519_AESGCM_SHA256')
session = requests.Session()

async def websocket_handler(request):
    ws = web.WebSocketResponse()
    await ws.prepare(request)
    await noisy_socket(ws)
    return ws

async def noisy_socket(ws):
    with measure('handler'):
        logger.debug('Incoming connection on /noise, init handshake')
        handshakestate = protocol.create_handshakestate()
        handshakestate.initialize(XXHandshakePattern(), False, b'', s=keypair)

        logger.debug('Commencing handshake')
        try:
            incoming = await ws.receive_bytes()
        except:
            logger.debug('Handhsake not complete')
            return

        assert incoming, 'Received empty data during handshake'
        handshakestate.read_message(bytes(incoming), bytearray())

        message_buffer = bytearray()
        handshakestate.write_message(b'', message_buffer)
        await ws.send_bytes(message_buffer)

        try:
            incoming = await ws.receive_bytes()
        except:
            logger.debug('Handhsake not complete')
            return
        cipherstates = handshakestate.read_message(bytes(incoming), bytearray())

        remote_pubkey = handshakestate.rs
        assert remote_pubkey, 'Could not obtain remote public key'

        logger.debug('Handshake complete, starting read loop')

        b64pubkey = base64.b64encode(remote_pubkey.data)
        while True:
            try:
                incoming = await ws.receive_bytes()
            except:
                logger.debug('Connection closed')
                return
            with measure('request'):
                plaintext = cipherstates[0].decrypt_with_ad(b'', bytes(incoming))
                parsed = json.loads(plaintext)
                assert 'id' in parsed, 'Malformed request'

                logger.debug('Processing request with id %s', parsed['id'])

                result = {
                    'id': parsed['id']
                }
                requestdata = {
                    'url': '',
                    'headers': {},
                    'data': '',
                    'method': ''
                }
                requested_path = '/'
                try:
                    requestdata.update(json.loads(parsed['data']))

                    requestdata['headers']['X-PubKey'] = b64pubkey
                    if requestdata['url'].startswith('/'):
                        requestdata['url'] = f"http://backend_internal{requestdata['url']}"

                    parsed_url = urlparse(requestdata['url'])
                    assert parsed_url.netloc == 'backend_internal', 'Host not allowed'
                    assert parsed_url.scheme == 'http', 'Scheme not allowed'
                    requested_path = parsed_url.path

                    resp = session.request(**requestdata)
                    response_data = {
                        'status': resp.status_code,
                        'data': resp.text,
                        'text': resp.text,
                        'headers': dict(resp.headers)
                    }
                except Exception as e:
                    logger.error(e)
                    response_data = {
                        'status': 400 if type(e) is AssertionError else 500,
                        'data': str(e),
                        'headers' : {}
                    }
                logger.info(f'id:{parsed["id"]} {requestdata["method"]} {requested_path} => {response_data["status"]}')

                result['data'] = json.dumps(response_data)
                ciphertext_send = cipherstates[1].encrypt_with_ad(b'', json.dumps(result).encode())
                await ws.send_bytes(ciphertext_send)

                logger.debug('Finished processing request with id %s', parsed['id'])

