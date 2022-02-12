
from concurrent.futures import ThreadPoolExecutor

import ConnectMySQLdb
import JinRiXiaoYuan
import student

executor = ThreadPoolExecutor(max_workers=3)
pool=ConnectMySQLdb.Operation_mysql()

def submit(stu):
    getLogin=JinRiXiaoYuan.submitTasks(stu)
    if(getLogin.login()):
        if(getLogin.getCollector()):
            if(getLogin.submitCollector()):
                print(stu.getStudentID(),"提交成功")
            else:
                print(stu.getStudentID(),"提交失败")
        else:
            print(stu.getStudentID(),"获取收集任务失败")
    else:
        countErrors(stu)
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
    # print(stu.getStudentID())
# conn.close()