<%--
  Created by IntelliJ IDEA.
  User: 安宸
  Date: 2022/2/5
  Time: 11:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/vant@2.12/lib/index.css" />
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css"
          integrity="sha384-HSMxcRTRxnN+Bdg0JdbxYKrThecOKuH5zCYotlSAcp1+c8xmyTe9GYg1l9a69psu" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
    <link rel="stylesheet" href="./index.css" />
    <title>今日校园自动打卡注册</title>
</head>

<body>
<div id="app">
    <van-steps  v-if="active >= 0" :active="active" class="steps">
        <van-step>验证账号</van-step>
        <van-step>填写信息</van-step>
        <van-step>推送验证</van-step>
        <van-step>完成</van-step>
    </van-steps>

    <div class="guide" v-if="active == -1">
        <van-notice-bar mode="closeable">注册前先获取推送渠道的密钥，以便接收打卡结果</van-notice-bar>
        <van-notice-bar mode="closeable">小米和苹果建议选择PushDeer，可以实现无后台推送；其他设备建议选择Server酱</van-notice-bar>
        <van-tabs v-model:active="guide" swipeable animated lazy-render>
            <van-tab title="Server酱" style="overflow:scroll; height: 500px;">
                <h3>获取key</h3>
                <p>1.点击下方跳转到Server酱登录页面，扫码登录</p>
                <p>2.选择SendKey下方的复制按钮（我们需要的就是这个SendKey）</p>
                <van-image show-loading width="90%" height="20rem" fit="contain" style="margin-left:5%;" src="./Server.png"></van-image>
                <h3>安装</h3>
                <van-cell title="点击此处获取Server酱sendKey" is-link url="https://sct.ftqq.com/login" ></van-cell>
            </van-tab>
            <van-tab title="PushDeer"  style="overflow:scroll; height: 500px;">
                <h3>获取key</h3>
                <p>1.通过apple账号（或微信账号·仅Android版支持）登录</p>
                <p>2.切换到「设备」标签页，点击右上角的加号，注册当前设备</p>
                <p>3.切换到「Key」标签页，点击右上角的加号，创建一个Key（我们需要的就是这个密钥）</p>
                <h3>安装</h3>
                <p>安卓暂无快应用，需要下载apk安装</p>
                <van-cell title="点击此处下载APK" is-link url="https://gitee.com/easychen/pushdeer/attach_files/958282/download/pushdeer-alpha-4.apk" ></van-cell>
                <p>苹果手机（iOS 14+）用系统摄像头扫码即可拉起轻应用。亦可在苹果商店搜索「PushDeer」安装。</p>
                <van-image show-loading width="90%" height="20rem" fit="contain" style="margin-left:5%;" src="./applePushDeer.png"></van-image>
            </van-tab>
        </van-tabs>
    </div>
    <div class="stepDiv" v-if="active == 0">
        <van-cell-group>
            <van-field clearable type="digit" v-model="studentId" label="学号" placeholder="请输入学号" />
        </van-cell-group>
        <van-field clearable type="password" v-model="password" label="密码" placeholder="请输入密码" />
    </div>
    <div class="stepDiv" v-if="active == 1">
        <van-cell is-link title="地区选择" v-model="value" @click="showPopup"></van-cell>
        <van-popup v-model="Popupshow" position="bottom" :style="{ height: '40%' }">
            <van-area title="省市区选择" :area-list="areaList" title="title" v-on:confirm="onConfirm" />
        </van-popup>
        <van-cell-group>
            <van-field clearable type="number" v-model="longitude" label="经度" placeholder="等待获取经度" />
        </van-cell-group>
        <van-cell-group>
            <van-field clearable type="number" v-model="latitude" label="纬度" placeholder="等待获取纬度" />
        </van-cell-group>
    </div>
    <div class="stepDiv" v-if="active == 2">
        <van-radio-group  :disabled="isSend"  v-model="radio" direction="horizontal" class="radio">
            <van-radio name="1">pushDeer</van-radio>
            <van-radio name="2">Server酱</van-radio>
        </van-radio-group>
        <van-cell-group>
            <van-field clearable type="text" v-model="pushDeerKey" label="推送密钥" placeholder="请输入密钥" />
            <template #button>
                <van-button :disabled="isSend" size="small" type="primary" @click="requestCaptcha">{{CaptchaText}}</van-button>
            </template>
        </van-cell-group>
        <van-cell-group>
            <van-field clearable type="number" v-model="code" label="验证码" placeholder="请输入验证码" />
        </van-cell-group>
    </div>
    <div class="stepDiv" v-if="active == 3">
    </div>
    <van-button v-if="active < 3" class="nextButton" type="primary" @click="next">下一步</van-button>
    <van-overlay :show="maskLayer">
        <div class="wrapper" @click.stop>
            <van-loading size="48px" color="#1989fa" vertical>加载中...</van-loading>
        </div>
    </van-overlay>

    <div id="myModal" class="modal fade" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">{{ModalTitle}}</h4>
                </div>
                <div class="modal-body">
                    <p>{{ModelMsg}}</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
</div>

<script src="https://cdn.jsdelivr.net/npm/vue@2.6/dist/vue.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"
        integrity="sha384-aJ21OjlMXNL5UyIl/XNwTMqvzeRMZH2w8c5cRVpzpU8Y5bApTppSuUkhZXN0VxHd"
        crossorigin="anonymous"></script>
<!-- 这个链接访问着太慢了 -->
<!-- <script src="https://unpkg.com/axios/dist/axios.min.js"></script> -->
<script src="./axios.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/vant@2.12/lib/vant.min.js"></script>
<script src="./index.js"></script>
</body>

</html>