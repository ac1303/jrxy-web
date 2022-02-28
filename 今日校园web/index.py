
from concurrent.futures import ThreadPoolExecutor


import ConnectMySQLdb
import JinRiXiaoYuan
import student
import requests

executor = ThreadPoolExecutor(max_workers=3)
pool=ConnectMySQLdb.Operation_mysql()

def sendMessage(stu,title,desp):
    print(stu.getStudentID())
    if(stu.getPushChannel()=="ServerChan"):
        url = "https://sctapi.ftqq.com/"+stu.getServerChanKey()+".send?title="+title+"&desp="+desp
        req = requests.get(url=url)
        print(req)
    elif(stu.getPushChannel()=="PushDeer"):
        url="https://api2.pushdeer.com/message/push?pushkey="+stu.getPushDeerKey()+"&text="+title+":"+desp
        req = requests.get(url=url)
        print(req)
    else:
        print("未知推送方式")

# 信息收集
# def submit(stu):
#     getLogin=JinRiXiaoYuan.submitTasks(stu)
#     if(getLogin.login()):
#         if(getLogin.getCollector()):
#             if(getLogin.submitCollector()):
#                 print(stu.getStudentID(),"提交成功")
#             else:
#                 print(stu.getStudentID(),"提交失败")
#         else:
#             print(stu.getStudentID(),"获取收集任务失败")
#     else:
#         countErrors(stu)
#         print(stu.getStudentID(),"登录失败")


# 打卡
def submit(stu):
    getLogin=JinRiXiaoYuan.submitTasks(stu)
    if(getLogin.login()):
        if(getLogin.getTasks()):
            if(getLogin.submitTask()):
                print(stu.getStudentID(),"提交成功")
            else:
                sendMessage(stu,"今日校园打卡：","提交数据失败，请联系QQ878375551重新适配")
                print(stu.getStudentID(),"提交失败")
        else:
            sendMessage(stu,"今日校园打卡：","获取打卡任务失败")
            print(stu.getStudentID(),"获取打卡任务失败")
    else:
        countErrors(stu)
        sendMessage(stu,"今日校园打卡：","登录失败，请检查账号密码是否正确，连续错误三次将删除自动打卡任务")
        print(stu.getStudentID(),"登录失败")

def countErrors(stu):
    stu.setErrorCount(stu.getErrorCount()+1)
    if(stu.getErrorCount()>=3):
        # 删除这条记录
        pool.update_infor("delete from jrxy where studentID='{}'".format(stu.getStudentID()))
        print(stu.getStudentID(),"删除数据完成")
        return
    pool.update_infor("update jrxy set errorCount="+str(stu.getErrorCount())+" where studentID="+str(stu.getStudentID()))
    print(stu.getStudentID(),"更新数据完成")


# 获取查询结果中的psw字段
for item in pool.select_infor("select * from jrxy"):
    stu=student.student(item[0],item[1],item[2],item[3],item[4],item[5],item[6],item[7],item[8],item[9],item[10],item[11])
    executor.submit(submit, (stu))
    # if(stu.getStudentID()==2019901344):
    #     executor.submit(submit, (stu))
    #     break
    # continue
    # executor.submit(sendMessage, stu,"今日校园自动打卡维护通知","学校修改了打卡方式，我正在重新适配，这两天需要手动打卡，请注意")
    # print(stu.getStudentID())
# conn.close()