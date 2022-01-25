from aiohttp import web
from app import routes


app = web.Application()
app.add_routes([web.get('/noise', routes.websocket_handler)])

if __name__ == '__main__':
    web.run_app(app, port=5000)
