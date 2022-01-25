import tornado.ioloop
import tornado.web
import builtins
import unicodedata
import uuid
import os
import re
import time

def filter(data):
    return False

class IndexHandler(tornado.web.RequestHandler):
    def get(self):
        self.render("templates/index.html",)
    def post(self):
        data = self.get_argument("data")
        id = 'navy'
        f = open(f"uploads/{id}.html",'w')
        f.write(data)
        f.close()
        try:
            self.render(f"uploads/{id}.html",)
        except Exception as e:
            print(e)
            self.finish("error")
        os.unlink(f"uploads/{id}.html")

def make_app():
    return tornado.web.Application([
        (r"/", IndexHandler),
    ],compiled_template_cache=False)

if __name__ == "__main__":
    app = make_app()
    app.listen(8888)
    tornado.ioloop.IOLoop.current().start()