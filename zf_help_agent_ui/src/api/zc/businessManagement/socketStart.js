//浏览器WebSocket对象
let wsImpl = window.WebSocket || window.MozWebSocket;
export function SocketStart(options) {
    this.url = 'ws://localhost:8181';//Socket地址
    this.ws = new wsImpl(this.url);//初始化Socket对象
    this.onmessage = options.onmessage;//接受消息回调函数
    this.onopen = options.onopen;//连接成功回调函数
    this.onclose = options.onclose;//连接断开回调函数
    this.dataType = 'json';//默认json
    var _self = this;
    SocketInit(_self)
}

function SocketInit(_self) {
    //setTimeout
    var loopTime;

    /**
     * 发送消息方法
     * msg 消息内容，字符串或json都可以
     */
    _self.send = function (msg) {
        // 发送消息
        try {
            // console.log("发送信息为:" + msg)
            _self.ws.send(msg);
        } catch (error) {
            console.log("error："+error)
        }
    }

    /**
     * 接收消息事件
     */
    _self.ws.onmessage = function (evt) {
        //console.log("返回的内容:" + JSON.parse(evt.data))
        // 判断消息是否为空
        if (!evt.data) {
            return;
        }
        //判断消息类型
        var get_data = JSON.parse(evt.data);

        //判断消息是否为心跳
        if (get_data.msg == 'tick') {
            //console.log("心跳检测！");
            return;
        }
        // 连接成功
        if (get_data.msg == "success") {
            //console.log("连接成功！");
            return;
        }

        //调用回调函数
        if (_self.onmessage) {
            //console.log("收到回复，回调之前:" + get_data[content]);
            _self.onmessage(get_data);
        }

    };
    //连接成功事件
    _self.ws.onopen = function () {
        // 调用心跳函数 第一次发送内容为  open, 开启连接
        loop("open");

        //调用回调函数
        if (_self.onopen) {
            _self.onopen();
        }
    };

    //断开连接事件
    _self.ws.onclose = function () {
        clearTimeout(loopTime);
        console.error("连接断开！");
        console.log("重新连接！");


        //调用回调函数
        if (_self.onclose) {
            _self.onclose();
        }

        //15秒后重连
        setTimeout(function () {
            _self.ws = new wsImpl(_self.url);
            SocketInit(_self)
            //或者主动关闭
            //_self.ws.close();
        }, 1000);
    }

    //心跳函数 递归执行 20秒一次，接收方为自己
    function loop(i) {
        _self.send((i || "tick"));
        //递归检测心跳
        loopTime = setTimeout(function () {
            loop();
        }, 2000000);
    }
}
