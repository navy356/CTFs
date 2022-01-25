from flask import *
from werkzeug.utils import secure_filename
import os
import binascii
from bot import *
from PIL import Image
import base64

app = Flask(__name__)
#app.config['UPLOAD_FOLDER'] = '/app/uploads'
app.config['UPLOAD_FOLDER'] = '/home/navy356/CTFs/MyChalls/Chall1/uploads'
#RESPONSE_FILE='/app/responses/response.png'
RESPONSE_FILE='/home/navy356/CTFs/MyChalls/Chall1/responses/response.png'
lastViewed=list()

def removeFlag(flagFile):
    if os.path.exists(flagFile):
        os.remove(flagFile)

@app.route('/',methods=['GET','POST'])
def index():
    if request.method == 'POST':
        if 'ImageFile' not in request.files:
            flash('No file part')
            return redirect(request.url)
        file = request.files['ImageFile']
        if file.filename == '':
            flash('No selected file')
            return redirect(request.url)
        if file:
            filename = binascii.b2a_hex(os.urandom(5)).decode("utf-8") +'.svg'
            file.save(os.path.join(app.config['UPLOAD_FOLDER'], filename))

            lastViewed=visit('file://'+app.config['UPLOAD_FOLDER']+'/'+filename,list())

            removeFlag(lastViewed[-1])

            if os.path.exists(os.path.join(app.config['UPLOAD_FOLDER'], filename)):
                os.remove(os.path.join(app.config['UPLOAD_FOLDER'], filename))
            return 'OK'
    else:
        return render_template('index.html')

@app.route('/response', methods=['GET'])
def response():
    if os.path.exists(RESPONSE_FILE):
        return send_file(RESPONSE_FILE,mimetype='image/png')
    else:
        return "No response"

@app.route('/admin', methods=['GET'])
def admin():
    if request.remote_addr!='127.0.0.1':
        return "You are not admin"
    else:
        return render_template('admin.html',lastViewed=lastViewed)

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=1337, debug=True)