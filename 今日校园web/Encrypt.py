# -*- coding: utf-8 -*-
import configparser
import hashlib
import os
from Crypto.Cipher import AES
from pyDes import des, CBC, PAD_PKCS5
import pyaes
import json
import math
import random
import base64
def getRandomString(length):
    chs = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678'
    result = ''
    for i in range(0, length):
        result += chs[(math.floor(random.random() * len(chs)))]
    return result
def EncryptAES(s, key, iv='1' * 16, charset='utf-8'):
    key = key.encode(charset)
    iv = iv.encode(charset)
    BLOCK_SIZE = 16
    def pad(s): return (s + (BLOCK_SIZE - len(s) % BLOCK_SIZE)
                        * chr(BLOCK_SIZE - len(s) % BLOCK_SIZE))
    raw = pad(s)
    cipher = AES.new(key, AES.MODE_CBC, iv)
    encrypted = cipher.encrypt(bytes(raw, encoding=charset))
    return str(base64.b64encode(encrypted), charset)

# 金智的登录密码，AES加密
def AESEncrypt_psw(data, key):
    return EncryptAES(getRandomString(64) + data, key, key)

configPath = os.path.join(os.path.dirname(os.path.abspath(__file__)), 'config.ini')
config = configparser.ConfigParser()
# 读取配置文件
config.read(configPath, encoding="utf-8")
DESKEY = config.get('key','deskey')
AESKEY = config.get('key','aeskey')
APPVERSION = config.get('key','appversion')

def DESEncrypt(s, Key=DESKEY):
        iv = b"\x01\x02\x03\x04\x05\x06\x07\x08"
        k = des(Key, CBC, iv, pad=None, padmode=PAD_PKCS5)
        encrypt_str = k.encrypt(s)
        return base64.b64encode(encrypt_str).decode()

def AESEncrypt(s,key,iv=b'\x01\x02\x03\x04\x05\x06\x07\x08\t\x01\x02\x03\x04\x05\x06\x07'):
        Encrypter=pyaes.Encrypter(pyaes.AESModeOfOperationCBC(key.encode('utf-8'),iv))
        Encrypted=Encrypter.feed(s)
        Encrypted+=Encrypter.feed()
        return base64.b64encode(Encrypted).decode()
        
# 生成设备id，根据用户账号生成,保证同一学号每次执行时deviceID不变，可以避免辅导员看到用新设备签到
def GenDeviceID(username,phoneModel):
        deviceId = ''
        random.seed(int(username))
        for i in range(8):
            num = random.randint(97, 122)
            if (num*i+random.randint(1, 8)) % 3 == 0:
                deviceId = deviceId+str(num % 9)
            else:
                deviceId = deviceId+chr(num)
        deviceId = deviceId+phoneModel
        return deviceId
# BodyString
def GenBodyString(form):
        return AESEncrypt(json.dumps(form),AESKEY)
# 生成sign验证
def SignForm(body):
        tosign={
            "appVersion":APPVERSION,
            "bodyString":body['bodyString'],
            "deviceId":body["deviceId"],
            "lat":body["lat"],
            "lon":body["lon"],
            "model":body["model"],
            "systemName":body["systemName"],
            "systemVersion":body["systemVersion"],
            "userId":body["userId"],   
        }
        signStr=""
        for i in tosign:
            if signStr:
                signStr+="&"
            signStr+="{}={}".format(i,tosign[i])
        signStr+="&{}".format(AESKEY)
        return hashlib.md5(signStr.encode()).hexdigest()