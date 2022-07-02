# -*- coding: utf-8 -*-
import configparser
import json
from math import e
import re
import os
from pymysql import NULL

import requests
import Encrypt
import datetime
class getLoginInfo:
    def __init__(self):
        # 获取路径
        configPath = os.path.join(os.path.dirname(os.path.abspath(__file__)), 'config.ini')
        config = configparser.ConfigParser()
        # 读取配置文件
        config.read(configPath, encoding="utf-8")
        self.config=config
        self.head = {}
        self.studentId=""
        self.password=""
        self.HWWAFSESID=""
        self.HWWAFSESTIME=""
        # MOD_AUTH_CAS = ticket
        self.ticket=""
        self.session=requests.Session()
    
    def setIdAndPsw(self,id,psw):
        self.studentId=id
        self.password=psw

    def login(self):
        try:
            print(datetime.datetime.now(),"开始获取登录页面...")
            # 清空字典，避免出现无效cookie
            self.head.clear()
            self.head = {
                'User-Agent': 'Mozilla/5.0 (Linux; Android 7.1.1; MI 6 Build/NMF26X; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/61.0.3163.98 Mobile Safari/537.36',
                }
            req = self.session.get(self.config.get('url', "login_Url"), headers=self.head)
            set_cookie = req.headers["SET-COOKIE"]
            route = re.findall("route=(.*?),", set_cookie)[0]
            JSESSIONID = re.findall("JSESSIONID=(.*?);", set_cookie)[0]
            html = req.text
            LT = re.findall('name="lt" value="(.*)"', html)[0]
            key = re.findall('var pwdDefaultEncryptSalt = "(.*?)"', html)[0]
            dllt = re.findall('name="dllt" value="(.*)"', html)[0]
            execution = re.findall('name="execution" value="(.*?)"', html)[0]
            rmShown = re.findall('name="rmShown" value="(.*?)"', html)[0]
            print(datetime.datetime.now(),self.studentId+'获取登录页面信息成功,正在进行登录前的准备...')
            body = {
                'username': self.studentId,
                'password': Encrypt.AESEncrypt_psw(self.password, key),
                'captchaResponse': '',
                'lt': LT,
                'dllt': dllt,
                'execution': execution,
                '_eventId': 'submit',
                'rmShown': rmShown
            }
            req = self.session.post(self.config.get('url', "login_Url"),data=body, headers=self.head)
            set_cookie = req.history[0].headers["SET-COOKIE"]
            CASTGC = re.findall("CASTGC=(.*?);", set_cookie)[0]
            CASPRIVACY = re.findall("CASPRIVACY=(.*?);", set_cookie)[0]
            iPlanetDirectoryPro = re.findall(
                "iPlanetDirectoryPro=(.*?);", set_cookie)
            print(datetime.datetime.now(),self.studentId+"登录成功,成功获取CASTGC,下一步获取HWWAFSESID和HWWAFSESTIME....")
            req = self.session.get(self.config.get('url', "getHWW_url"), headers=self.head)
            set_cookie = req.history[0].headers["SET-COOKIE"]
            HWWAFSESID = re.findall("HWWAFSESID=(.*?);", set_cookie)[0]
            self.HWWAFSESID=HWWAFSESID
            HWWAFSESTIME = re.findall("HWWAFSESTIME=(.*?);", set_cookie)[0]
            self.HWWAFSESTIME=HWWAFSESTIME
            print(datetime.datetime.now(),self.studentId+'获取HWWAFSESID和HWWAFSESTIME成功，正在进行获取MOD_AUTH_CAS前的准备...')
            self.head['Cookie'] = 'HWWAFSESID=' + HWWAFSESID+'; HWWAFSESTIME='+HWWAFSESTIME
            self.head['Host'] = 'huat.campusphere.net'
            req = self.session.get(self.config.get('url', "cas_url"), headers=self.head)
            # print(req)
            # print(req.text)
            # print(req.headers)
            # print(req.history[0].headers)
            # print(req.history[1].headers)
            # print(req.history[2].headers)


            # req = self.session.post(self.config.get('url', "cas_url"), headers=self.head)
            # print(datetime.datetime.now(),'第一步完成...')
            # self.head['Host'] = 'cas.huat.edu.cn'
            # self.head['Cookie'] = 'CASTGC='+CASTGC+'; route='+route+'; JSESSIONID='+JSESSIONID + \
            #     '; org.springframework.web.servlet.i18n.CookieLocaleResolver.LOCALE=zh_CN'
            # print(datetime.datetime.now(),'第二步完成...')
            # req = self.session.post(req.history[0].headers["Location"], headers=self.head)
            # self.head['Host'] = 'huat.campusphere.net'
            # print(datetime.datetime.now(),'第三步完成...')
            # req = self.session.post(req.history[0].headers["Location"], headers=self.head)
            ticket = re.findall(
                "ticket=(.*)", req.history[1].headers['Location'])[0]
            print(datetime.datetime.now(),self.studentId+'成功获取MOD_AUTH_CAS,保存数据后结束该函数！')
            self.ticket=ticket
            print(self.studentId+"的ticket是："+ticket)
            return True
        except:
            return False
        # if(self.config.has_section('Cookie')!=True):
        #     self.config.add_section("Cookie")
        # self.config.set('Cookie', 'HWWAFSESID', HWWAFSESID)
        # self.config.set('Cookie', 'HWWAFSESTIME', HWWAFSESTIME)
        # self.config.set('Cookie', 'ticket', ticket)
        # self.config.write(open(self.configPath, 'w',encoding="UTF-8"))

class submitTasks(getLoginInfo):
    def __init__(self,stu):
        getLoginInfo.__init__(self)
        self.studentId=stu.getStudentID()
        self.password=stu.getPsw()
        self.address_province = stu.getAddress_province()
        self.address_city = stu.getAddress_city()
        self.address_region = stu.getAddress_region()
        self.address = stu.getAddress()
        self.lon = stu.getLon()
        self.lat = stu.getLat()
        self.phoneModel = "iPhone X"
        self.pushChannel = stu.getPushChannel()
        self.ServerChanKey = stu.getServerChanKey()
        self.pushDeerKey = stu.getPushDeerKey()
        self.errorCount = int(stu.getErrorCount())
        # 今日校园打卡任务
        self.punchTask={}
        self.signTaskName="科院2021-2022学年第二学期学生安全日报"
        #今日校园信息收集任务
        self.collectorTask={}
        self.collectorTaskName="科技学院2022年寒假学生健康安全日报"
        print(self.studentId)

    def pushMessage(self,title,desp):
        if(self.pushChannel=="ServerChan"):
            url = "https://sctapi.ftqq.com/"+self.ServerChanKey+".send?title="+title+"&desp="+desp
            req = requests.get(url=url).text
        elif(self.pushChannel=="PushDeer"):
            url=self.config.get('url', "pushDeer_url")+"?pushkey="+self.pushDeerKey+"&text="+title+":"+desp
            req = json.loads(requests.get(url=url).text)
            if(len(req.get("content").get("result"))>0):
                print(self.studentId+"推送成功")

    def getTasks(self):
        try:
            print(datetime.datetime.now(),self.studentId+'正在获取打卡任务...')
            self.head.clear()
            self.head = {
                'User-Agent': 'Mozilla/5.0 (Linux; Android 7.1.1; MI 6 Build/NMF26X; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/61.0.3163.98 Mobile Safari/537.36',
                }
            params = {
                'pageSize': 10,
                'pageNumber': 10}
            self.head['Host'] = 'huat.campusphere.net'
            self.head['Content-Type'] = 'application/json;charset=UTF-8'
            self.head['Cookie'] = 'HWWAFSESID='+self.HWWAFSESID+'; HWWAFSESTIME='+self.HWWAFSESTIME+'; MOD_AUTH_CAS='+self.ticket
            req = requests.post(self.config.get('url', "tasks_url"),
                                headers=self.head, data=json.dumps(params))
            tasks = json.loads(req.text)
            print(self.studentId+"获取打卡任务成功")
            for task in tasks['datas']['unSignedTasks']:
                if(task['taskName']==self.signTaskName):
                    self.punchTask['signInstanceWid']=task['signInstanceWid']
                    self.punchTask['signWid']=task['signWid']
                    # 获取打卡的详细数据
                    req = requests.post(self.config.get('url', "sign_detail_url"),
                                headers=self.head, data=json.dumps(self.punchTask))
                    print(req.text)
                    print(datetime.datetime.now(),self.studentId,'存在需要打卡的任务')
                    return True
                else:
                    continue
            print(datetime.datetime.now(),self.studentId,'没有需要打卡的任务！')
            return False
        except:
            print(datetime.datetime.now(),self.studentId,'getTasks失败！')
            return False

    def submitTask(self):
        try:
            print(datetime.datetime.now(),self.studentId,'开始打卡...')
            extension = {
                "systemName": "android",
                "systemVersion": "11",
                "model": self.phoneModel,
                "deviceId": Encrypt.GenDeviceID(self.studentId, self.phoneModel),
                "appVersion": self.config.get('key', "appversion"),
                "lon": self.lon,
                "lat": self.lat,
                "userId": self.studentId}
            self.head.clear()
            self.head = {
                'User-Agent': 'Mozilla/5.0 (Linux; Android 7.1.1; MI 6 Build/NMF26X; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/61.0.3163.98 Mobile Safari/537.36',
                'CpdailyStandAlone': '0',
                'extension': '1',
                'Connection': 'Keep-Alive',
                'Accept-Encoding': 'gzip'}
            self.head['Host'] = 'huat.campusphere.net'
            self.head['Cpdaily-Extension']=Encrypt.DESEncrypt(json.dumps(extension))
            self.head['Content-Type'] = 'application/json;charset=UTF-8'
            self.head['Cookie'] = 'HWWAFSESID='+self.HWWAFSESID+'; HWWAFSESTIME='+self.HWWAFSESTIME+'; MOD_AUTH_CAS='+self.ticket
            form = {
                "longitude": self.lon,
                "latitude": self.lat,
                "isMalposition": '0',
                "abnormalReason": "",
                "signPhotoUrl": "",
                "isNeedExtra": '1',
                "position":  self.address,
                "uaIsCpadaily": 'true',
                "signInstanceWid": self.punchTask['signInstanceWid'],
                "signVersion": "1.0.0",
                "extraFieldItems": [
                    {"extraFieldItemValue": "否（暂未返校）", "extraFieldItemWid": '1853524'}, 
                    {"extraFieldItemValue": "36.3℃", "extraFieldItemWid": '1853528'}, 
                    {"extraFieldItemValue": "否", "extraFieldItemWid": '1853541'}, 
                    {"extraFieldItemValue": "否", "extraFieldItemWid": '1853542'}, 
                    {"extraFieldItemValue": "健康码、行程码均无异常", "extraFieldItemWid": '1853545'}]}
            body = {
                    "appVersion": self.config.get('key', "appversion"),
                    "systemName": "android",
                    "bodyString": Encrypt.GenBodyString(form),
                    "model": self.phoneModel,
                    "lon": self.lon,
                    "calVersion": "firstv",
                    "systemVersion": "11",
                    "deviceId": Encrypt.GenDeviceID(self.studentId, self.phoneModel),
                    "userId": self.studentId,
                    "version": "first_v2",
                    "lat": self.lat}
            body['sign'] = Encrypt.SignForm(body)
            r = json.loads(requests.post(self.config.get('url', "sign_url"), data=json.dumps(body), headers=self.head).text)
            print(datetime.datetime.now(),"任务打卡数据为：",r)
            self.pushMessage("今日校园打卡",r['message'])
            return True
        except:
            return False

    def getCollector(self):
        try:
            print(datetime.datetime.now(),'开始获取学校收集信息任务')
            self.head.clear()
            self.head = {
                'User-Agent': 'Mozilla/5.0 (Linux; Android 7.1.1; MI 6 Build/NMF26X; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/61.0.3163.98 Mobile Safari/537.36',}
            params = {
                'pageSize': 20,
                'pageNumber': 1}
            self.head['Host'] = 'huat.campusphere.net'
            self.head['Content-Type'] = 'application/json;charset=UTF-8'
            self.head['Cookie'] = 'HWWAFSESID='+self.HWWAFSESID+'; HWWAFSESTIME='+self.HWWAFSESTIME+'; MOD_AUTH_CAS='+self.ticket
            req = requests.post(self.config.get('url', "collector_url"),
                                headers=self.head, data=json.dumps(params))
            tasks = json.loads(req.text)
            for task in tasks['datas']['rows']:
                # 判断是不是需要填写
                if(task['subject'] == self.collectorTaskName):
                    print(datetime.datetime.now(),'存在需要打卡的任务！')
                    data={
                        "collectorWid":task['wid'],
                        "instanceWid":task['instanceWid']
                    }
                    req=requests.post(self.config.get('url', "collector_detail_url"),
                                headers=self.head, data=json.dumps(data))
                    tasks = json.loads(req.text)
                    print(datetime.datetime.now(),'开始保存数据到配置文件 ，然后结束该函数')
                    self.collectorTask['collectWid']=task['wid']
                    self.collectorTask['instanceWid']=task['instanceWid']
                    self.collectorTask['formWid']=tasks['datas']['collector']['formWid']
                    self.collectorTask['schoolTaskWid']=tasks['datas']['collector']['schoolTaskWid']
                    # data['formWid']= tasks['datas']['collector']['formWid']
                    # data['pageSize']=9999
                    # data['pageNumber']=1
                    # req = requests.post(self.config.get('url', "collector_form_url"),
                    #             headers=self.head, data=json.dumps(data))
                    # print(req.text)
                else:
                    continue
            print(self.collectorTask)
            return True
        except:
            return False
    
    def submitCollector(self):
        try:
            print(datetime.datetime.now(),'开始读取数据并填充')
            form = {
                    "longitude": self.lon,
                    "latitude": self.lat,
                    "address": self.address,
                    "uaIsCpadaily": True,
                    "formWid": self.collectorTask['formWid'],
                    "collectWid": self.collectorTask['collectWid'],
                    # "schoolTaskWid": "",
                    "schoolTaskWid": self.collectorTask['schoolTaskWid'],
                    "instanceWid": self.collectorTask['instanceWid'],
                    "form":[{'wid':'33394','formWid':'3088','fieldType':'7','title':'今天你的所在地是？','sort':1,'value': self.address_province+'/'+self.address_city+'/'+self.address_region,'show':True,'formType':'0','sortNum':'1'},
                    {'wid':'33395','formWid':'3088','fieldType':'2','title':'你是否已经安全抵达目的地？','sort':2,'fieldItems':[{'itemWid':'78251','content':'是，已抵达','isOtherItems':False,}],'value':'78251','show':True,'formType':'0','sortNum':'2'},
                    {'wid':'33396','formWid':'3088','fieldType':'2','title':'今天你的体温是多少？','sort':3,'fieldItems':[{'itemWid':'78254','content':'37.2℃及以下','isOtherItems':False,}],'value':'78254','show':True,'formType':'0','sortNum':'3'},
                    {'wid':'33397','formWid':'3088','fieldType':'2','title':'今天你的身体状况是？','sort':4,'fieldItems':[{'itemWid':'78256','content':'健康','isOtherItems':False,}],'value':'78256','show':True,'formType':'0','sortNum':'4'},
                    {'wid':'33398','formWid':'3088','fieldType':'2','title':'截至打卡当日，你所在地区是否为中、高风险地区？','sort':5,'fieldItems':[{'itemWid':'78262','content':'否','isOtherItems':False,}],'value':'78262','show':True,'formType':'0','sortNum':'5'},
                    {'wid':'33399','formWid':'3088','fieldType':'2','title':'近14天你或你的共同居住人是否有疫情中、高风险区域人员接触史？','sort':6,'fieldItems':[{'itemWid':'78264','content':'否','isOtherItems':False,}],'value':'78264','show':True,'formType':'0','sortNum':'6'},
                    {'wid':'33400','formWid':'3088','fieldType':'2','title':'近14天你或你的共 同居住人是否和确诊、疑似病人接触过？','sort':7,'fieldItems':[{'itemWid':'78266','content':'否','isOtherItems':False,}],'value':'78266','show':True,'formType':'0','sortNum':'7'},
                    {'wid':'33401','formWid':'3088','fieldType':'2','title':'近14天你或你的共同居住人是否是确诊、疑似病例 ？','sort':8,'fieldItems':[{'itemWid':'78268','content':'否','isOtherItems':False,}],'value':'78268','show':True,'formType':'0','sortNum':'8'},
                    {'wid':'33402','formWid':'3088','fieldType':'2','title':'你或你的共同居住人目前是否被医学隔离？','sort':9,'fieldItems':[{'itemWid':'78270','content':'否','imageUrl':'','isOtherItems':False,}],'value':'78270','show':True,'formType':'0','sortNum':'9'},
                    {'wid':'33403','formWid':'3088','fieldType':'2','title':'今天你当地的健康码颜色是？','sort':10,'fieldItems':[{'itemWid':'78271','content':'绿色','isOtherItems':False,}],'value':'78271','show':True,'formType':'0','sortNum':'10'},
                     {'wid':'33404','formWid':'3088','fieldType':'2','title':'本人是否承诺以上所填报的全部内容均属实、准确，不存在任何隐瞒与不实的情况，更无遗漏之处','sort':11,'fieldItems':[{'itemWid':'78274','content':'是','isOtherItems':False,}],'value':'78274','show':True,'formType':'0','sortNum':'11'}]
                    }
            body = {
                    "appVersion": self.config.get('key', "appversion"),
                    "systemName": "android",
                    "bodyString": Encrypt.GenBodyString(form),
                    "model": self.phoneModel,
                    "lon": self.lon,
                    "calVersion": "firstv",
                    "systemVersion": "11",
                    "deviceId": Encrypt.GenDeviceID(self.studentId, self.phoneModel),
                    "userId": self.studentId,
                    "version": "first_v2",
                    "lat": self.lat}
            body['sign'] = Encrypt.SignForm(body)
            extension = {
                "lon": self.lon,
                "lat": self.lat,
                "model": self.phoneModel,
                "appVersion":self.config.get('key', "appversion"),
                "systemVersion": "11",
                "userId": self.studentId,
                "systemName": "android",
                "deviceId": Encrypt.GenDeviceID(self.studentId, self.phoneModel),
            }
            self.head.clear()
            self.head = {
                'User-Agent': 'Mozilla/5.0 (Linux; Android 7.1.1; MI 6 Build/NMF26X; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/61.0.3163.98 Mobile Safari/537.36',
                'CpdailyStandAlone': '0',
                'extension': '1',
                'Connection': 'Keep-Alive',
                'Accept-Encoding': 'gzip'}
            self.head['Host'] = 'huat.campusphere.net'
            self.head['Cpdaily-Extension']=Encrypt.DESEncrypt(json.dumps(extension))
            self.head['Content-Type'] = 'application/json;charset=UTF-8'
            self.head['Cookie'] = 'HWWAFSESID='+self.HWWAFSESID+'; HWWAFSESTIME='+self.HWWAFSESTIME+'; MOD_AUTH_CAS='+self.ticket
            r = json.loads(requests.post(self.config.get('url', "collector_submitForm_url"), data=json.dumps(body), headers=self.head).text)
            print(datetime.datetime.now(),"任务打卡数据为：",r)
            self.pushMessage("今日校园打卡",r['message'])
            return True
        except:
            return False
        