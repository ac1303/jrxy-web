# 自言自语
* 本来是就自己使用的一个python，然后放假在家无聊想着让同学们自己填写账号密码，然后我帮他们定时打卡，独乐乐不如众乐乐嘛，所以才做了这个四不像的项目
* 其实在写代码的过程中还是遇到了蛮多问题的。
    1. 首先就是如何实现自动打卡，我首先想到的是，反正也有思路嘛，干脆用java重写一遍自动打卡，但是很快很快就放弃了，每个语言都有自己的优点，python简单几行就能实现的功能，在java需要更多甚至十几行。
    2. 后来想用python，但是python写web我又不太会，所以我想用java写web，用python来执行自动打卡。
    3. 但是python和java之间如何通讯呢？我的想法是python写两个简单的接口，一个用来验证账号密码是否正确，一个用来触发打卡任务，再仔细想想，Linux 本身就有定时任务，所以就只需要一个验证账号密码的接口就行。
    4. 以前没有接触过数据库链接池，在python打卡任务中使用了3个线程同时执行，结果在修改数据库数据的时候出现了写入失败，后来查资料才知道应该使用链接池，java代码测试的时候也出现了这种问题，所以也用了同样的办法解决
    5. 在写验证码功能的过程中也遇到了问题，解决思路是利用多线程定时任务，在成功发送验证码的时候记录key和下发的验证码，然后添加定时删除的任务，并记录句柄，用户提交正确的验证码后通过句柄关闭定时任务，然后删除对应的数据
    6. 还有一大堆的其他小问题，基本上就是面向百度编程，
* 收获也是蛮多的，以前用来测试的代码直接用main方法写在类里面，这次发现maven里面那个测试根目录真香，还有日志框架，虽然以前用过log4j，但是并没有在意，因为直接打印关键数据到控制台也能找到问题，这次在部署到服务器的时候出现了问题，又没使用log4j，找bug找的真的是头大，一份实用的日志真的很重要！

# 部署
* 代码中填写必要的信息
* 今日校园web文件移出到自己的某个目录下
* 进入今日校园web目录，安装必要的依赖
* 持续后台运行  nohup python run.py >/dev/null 2>&1 &
* Linux设置定时任务，定时启动今日校园web目录下的index.py文件，实现定时打卡
>因为我第一次接触Flask，在网上找了半天教程，都是需要Nginx，但是我用的Tomcat，觉得麻烦，所以只能这样部署了
* java maven项目打包，发布到Tomcat，Tomcat配置中改成UTF-8编码


# 说明
* 只适用于湖北汽车工业学院
* 没有配置文件，代码中我删掉了一些关于我自己的信息
>service模块下resources文件夹中druid.properties文件是配置数据库连接池的，需要填写数据库链接信息

>service模块下com.anchen.function.GetLonAndLat的getLonAndLat函数中需要填写百度地图的密钥

>service模块下com.anchen.function.VerifyStudentIDAndPassword的getIDAndPassword函数中，第20行需要填写python flask项目的地址和验证密钥(自己随机生成，主要是为了防止其他人调用)；第26行需要填写生成sign的key和iv(iv对加密解密结果无影响，可以随机,只要是16位就行)，第48行同上

>python 文件夹下run.py文件第八行，填写密钥，需要和maven项目getIDAndPassword函数中，第20行填写的一致

>python 文件夹下ConnectMySQLdb.py中需要填写数据库链接信息