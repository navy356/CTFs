from flask import *

app = Flask(__name__)

@app.route("/")
def index():
    data="user=test&pass=test"
    return redirect(url_for('http://localhost:3999/api/login'), code=307)


if __name__ == "__main__":
    app.run()
    