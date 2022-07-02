from dbutils.pooled_db import PooledDB
import pymysql
import time
class Operation_mysql():
    def __init__(self):
        self.mysql_pool_list=PooledDB(
                               creator=pymysql,
                               mincached=1,# 初始化时，链接池中至少创建的空闲的链接，0表示不创建
                               maxcached=3,# 链接池中最多闲置的链接，0和None不限制
                               maxconnections=10,# 连接池允许的最大连接数，0和None表示不限制连接数
                               host='39.101.141.70',
                               user='jrxy',
                               password='dYeSdMwhiBiSnRWG',
                               db='jrxy',
                               port=3306,
                               charset='utf8',
                               blocking=True,
                               maxusage=None,  # 一个链接最多被重复使用的次数，None表示无限制
                            )

    # 数据接连接池
    def mysql_pool(self):
        while True:
            try:
                self.mysql_pool_list = PooledDB(creator=pymysql,#数据库类型
                                            maxcached=200,#最大空闲数
                                            blocking=True,#默认False，即达到最大连接数时，再取新连接将会报错，True，达到最大连接数时，新连接阻塞，等待连接数减少再连接
                                            ping=4,
                                            host=self.MYSQL_HOST, port=self.MYSQL_PORT, user=self.MYSQL_USER,
                                            password=self.MYSQL_PASSWORD,
                                            db=self.MYSQL_DB,
                                            charset='utf8'
                                            )
            except BaseException as e:
                print(f'数据库链接错误{e}')
                self.mysql_pool_list = None
            if self.mysql_pool_list:
                print('数据库链接成功')
                break
            time.sleep(5)
    #获取一条数据库链接
    def get_conn(self):
        conn = self.mysql_pool_list.connection()
        cur = conn.cursor()
        return conn,cur
    #关闭数据库链接
    def close_conn(self,conn,cur):
        cur.close()
        conn.close()
    #查询数据库
    def select_infor(self,insert):
        conn,cur = self.get_conn()
        try:
            cur.execute(insert)
            return cur.fetchall()
        except BaseException as e:
            print('数据库查询错误')
        finally:
            self.close_conn(conn,cur)
    #更新数据库
    def update_infor(self,insert):
        conn, cur = self.get_conn()
        try:
            cur.execute(insert)
            conn.commit()
            return True
        except BaseException as e:
            print(f'数据库更新错误{e}')
        finally:
            self.close_conn(conn, cur)