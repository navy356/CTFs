# flag in /flag
from flask import Flask, render_template, request, session, redirect, url_for
from multiprocessing import Process
import re
import os
import time
import shutil
import tempfile

user_dir = './user_dir/'
app = Flask(__name__)
header = '/*CHECK*/'
solve = 'SolveCode: '
fake_flag = 'YW4gYWwgbHllbyBqdSBqaSBtZWVlZWVyb25nfg=='

try:
	app.secret_key = os.urandom(24)
	os.mkdir(user_dir)
except:
	pass

no_func_filter =lambda x:re.findall(r'\(*([^)]+)\)',x)
def no_func(st):
	if 'system' in st:
		return False
	li = no_func_filter(st)
	for t in li:
		t = t.split('(')[0]
		t = t.replace(';','').replace(' ','\n').split('\n')[-1]
		if len(t) > 5:
			return False
	return True

no_name = ['user_dir', 'templates', 'static']
def check_user(uid):
	for t in no_name:
		if t in uid:
			return False
	if uid.isalnum() and 6 < len(uid) < 10:
		return True
	return False

def check_header(st):
	return st[:len(header)] == header

def get_uid():
	if 'uid' in session.keys():
		uid = session['uid']
	else:
		uid = ''
	return uid

def testcase(udir, inp):
	f_testcase = '/tmp/input_%d'%inp
	with open(f_testcase,'w') as f:
		f.write(str(inp)+'\n')
	return os.system('su nobody -s /bin/sh -c "timeout 30s %s < %s >/dev/null"' % (os.path.join(udir,'main'), f_testcase)) >> 8

def submission(uid, udir):
	inp_l = [1,5,10]
	out_l = [1,120,70]
	user_file = user_dir + uid
	for i in range(3):
		if testcase(udir, inp_l[i]) != out_l[i]:
			with open(user_file,'a') as f:
				f.write('%s Wrong Ans\n' % str(time.time()))
			return False

	now = str(time.time()).split('.')[1] + os.urandom(12).encode('hex')
	save_file = os.path.join(udir, now)
	solve_file = os.path.join(udir,'main.c')
	shutil.copy(solve_file,save_file)
	with open(user_file,'a') as f:
		f.write('%s Solve !!! %s <= It is no meaning. Flag is in /flag. "cat /flag"\n' % (time.time(), fake_flag))
		f.write('%s%s\n' % (solve,now))

@app.route('/')
def index():
	uid = get_uid()
	return render_template('index.html', user=uid)

@app.route('/logout')
def logout():
	session.pop('uid', None)
	session.pop('udir', None)
	return redirect(url_for('index')) 

@app.route('/login', methods=['GET', 'POST'])
def login():
	if request.method == 'POST':
		uid = request.form['uid']
		pw = request.form['pw'].encode('hex')
		if not check_user(uid):
			return 'No Hack'

		user_file = user_dir + uid

		if os.path.isfile(user_file) == False:
			return 'Login Fail'

		with open(user_file,'r') as f:
			user_info = f.read().split('\n')[0]

		if pw == user_info.split('|')[0]:
			session['uid'] = uid
			session['udir'] = user_info.split('|')[1]
			return redirect(url_for('index'))
		return 'Login Fail'
	else:
		return render_template('login.html')

@app.route('/reg', methods=['GET', 'POST'])
def reg():
	if request.method == 'POST':
		uid = request.form['uid']
		pw = request.form['pw'].encode('hex')
		if not check_user(uid):
			return 'No Hack'

		user_file = user_dir + uid

		if os.path.isfile(user_file) == False:
			udir = tempfile.mkdtemp()
			os.chmod(udir,0o777)
			main_file = os.path.join(udir,'main.c')
			with open(main_file,'w') as f:
				f.write(header+'\n#include <stdio.h>\nint main(){\nputs("Hello World");\nreturn 0;\n}')
			with open(user_file,'w') as f:
				f.write(pw + '|' + udir + '\n')
			return '<script>alert("Reg Sucess");location.href="/";</script>'
		else:
			return 'Already Reg'
	else:
		return render_template('reg.html')


@app.route('/prob', methods=['GET', 'POST'])
def prob():
	if get_uid() == '':
		return '<script>alert("No Login");location.href="/";</script>'

	main_file = os.path.join(session['udir'],'main.c')
	with open(main_file,'r') as f:
		main_data = f.read()

	if not check_header(main_data):
		return 'No Hack'

	if request.method == 'POST':
		code = request.form['code']
		if no_func(code):
			if check_header(code):
				with open(main_file,'w') as f:
					f.write(code)
				os.system('su nobody -s /bin/sh -c "cd %s;gcc main.c -o main >/dev/null 2>/dev/null;"' % session['udir'])
				proc = Process(target=submission, args=(session['uid'],session['udir']))
				proc.start()
				time.sleep(1)
				return redirect(url_for('mypage'))
			else:
				return 'Check File Type'
		else:
			return 'No Hack'
	else:
		return render_template('prob.html', main_data=main_data)

@app.route('/mypage')
def mypage():
	if get_uid() == '':
		return '<script>alert("No Login");location.href="/";</script>'

	uid = session['uid']
	udir = session['udir']
	user_file = user_dir + uid
	solve_file = []

	with open(user_file,'r') as f:
		user_log = f.read().split('\n')[1:]

	for t in user_log:
		if t[:len(solve)] == solve:
			solve_file.append(t[len(solve):])

	view = request.args.get('view')
	if view == None:
		return render_template('mypage.html', log=user_log, solves=solve_file)
	else:
		if view in solve_file:
			fname = os.path.join(udir,view)
			with open(fname,'r') as f:
				data = f.read()
			return data
		else:
			return 'No Hack'

app.run(host='0.0.0.0', port=2222, threaded=True)#, debug=True)