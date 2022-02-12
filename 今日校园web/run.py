# -*- coding: utf-8 -*-
import JinRiXiaoYuan
from flask import Flask
app = Flask(__name__)
# 判断账号密码是否正确
@app.route('/login/<id>&<psw>&<sign>')
def hello_world(id,psw,sign):
    if(sign!="QyBkSG8"):
        return "{\"code\":500}"
    getLogin=JinRiXiaoYuan.getLoginInfo()
    getLogin.setIdAndPsw(id,psw)
    if(getLogin.login()):
        return "{\"code\":200}"
    else:
        return "{\"code\":400,\"msg\":\"在确保账号密码正确的情况下，先登录这个网址(http://cas.huat.edu.cn/authserver/login)后再来尝试\"}"

if __name__ == '__main__':
    app.run(host='0.0.0.0',port= 5000,debug=True)

