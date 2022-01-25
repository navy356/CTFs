#!/usr/bin/env python3

import asyncio
import simplejson as json
import time
import websockets

async def hello():
    async with websockets.connect('ws://web4.affinityctf.com/socket.io/?EIO=4&transport=websocket&sid=5MZ9cY5JanT9tzMEABxV') as websocket:

        # The initial connect message
        await websocket.send('{"type":"WS_CONNECTED","session":null,"protocol":"43ae08fd-9cf2-4f54-a6a6-8454aef59581"}')

        # Wait for OHHIMARK
        while True:
            response = json.loads(await websocket.recv())
            if response['type'] == 'WS_OHHIMARK':
                break

        # Send the login info
        await websocket.send('{"type":"WS_LOGIN","usernameOrEmail":"VG","password":"3j6QMM3grTYYp7jWkie&f"}')

        # Print out everything we get back
        while True:
            response = json.loads(await websocket.recv())
            print(response)

asyncio.get_event_loop().run_until_complete(hello())
