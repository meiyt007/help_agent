/*
* 插件: jSign
* 描述: JavaScript 签名控制插件
* 版本: 1.0.0
* 版本: 1.0.1
*       (1) 增加按键事件响应接口 
* */

/**
 * 签名脚本对象
 * 所属 [用户]
 * @returns {*}
 */
function jSign () {
    if (!!window.WebSocket && window.WebSocket.prototype.send) {
        this.net = createNet();
        return this;
    }
    else {
        return null;
    }
}

/**
 * 初始化签名对象
 * 所属 [用户]
 * @param callback
 * @returns {*}
 */
jSign.prototype.Init = function (callback, url = "127.0.0.1:12267") {
    if (this.net == null) {
        callback(0);
        return null;
    }
    this.funcs = [];
    this.rets = [];
    this.rcb = callback;
    this.cid = 1;

    this.net.setStatusCallback(this, this.NetStatusCallback);
    this.net.setDataCallback(this, this.NetDataCallback);
    this.net.connect(url);

    return this;
};

/**
 * 对象销毁
 * 所属 [用户]
 * @param callback
 */
jSign.prototype.Destroy = function (callback) {
    this.funcs = [];
    this.net.disconnect();
    this.net.setStatusCallback(null, null);
    this.net.setDataCallback(null, null);
    if (typeof callback === "function") {
        callback(true);
    }
};


/**
 * 消息类型
 * @type {{REQUEST: number, RESPONSE: number, EVENT: number, NOTIFY:number}}
 */
var SIGN_MSG_TYPE = {
    REQUEST: 2001,
    RESPONSE: 2002,
    EVENT: 2003,
    NOTIFY: 2004
};
/**
 * 接口请求类型ID
 * @type {{ClearSign: number}}
 */
var SIGN_IID = {
    ClearSign: 1,
    SetPenColor: 2,
    SetBorderColor: 3,
    SetBKColor: 4,
    SetPenSize: 5,
    SaveSignToFile: 6,
    GetSignBase64: 7,
    DeviceStatus: 8,
    ActivateMouse: 9,
    SetScreenRotation: 10,
    GetScreenRect: 11
};

/**
 * 接口ID扩展
 * @type {{BeginSign: number, EndSign: number, MoveSignWindow: number, ShowURL: number, CloseURL: number}}
 */
var SIGN_IID_EX = {
    BeginSign: 101,
    EndSign: 102,
    MoveSignWindow: 103,
    ShowURL: 104,
    CloseURL: 105
};

/**
 * 用户事件类型
 * @type {{}}
 */
var SIGN_EVENT_TYPE = {
    Click: 201,
    Key: 202,
    Custom: 203
};

/**
 * 自定义事件子ID
 * @type {{Cancel: number, Clear: number, Confirm: number}}
 */
var SIGN_EID_CUSTOM = {
    Cancel: 301,
    Clear: 302,
    Confirm: 303
};

/**
 * 通知类型
 * @type {{}}
 */
var SIGN_NOTIFY_TYPE = {
    Status: 401
};


/**
 * =====================================================================================
 * 通用的公共接口
 */

/**
 * 清除笔迹
 * 所属 [用户]
 * @param callback
 */
jSign.prototype.ClearSign = function (callback) {
    let pack = [];
    let iid = SIGN_IID.ClearSign.toString();
    pack.push(iid);
    pack.push("ClearSign");
    this.Emit(pack, callback);
};

/**
 * 设置画笔颜色
 * 所属 [用户]
 * @param r
 * @param g
 * @param b
 * @param callback
 */
jSign.prototype.SetPenColor = function (r, g, b, callback) {
    let pack = [];
    let iid = SIGN_IID.SetPenColor.toString();
    pack.push(iid);
    pack.push("SetPenColor");
    pack.push.apply(pack, [r.toString(), g.toString(), b.toString()]);
    this.Emit(pack, callback);
};

/**
 * 设置签字区边框颜色
 * 所属 [用户]
 * @param r
 * @param g
 * @param b
 * @param callback
 */
jSign.prototype.SetBorderColor = function (r, g, b, callback) {
    let pack = [];
    let iid = SIGN_IID.SetBorderColor.toString();
    pack.push(iid);
    pack.push("SetBorderColor");
    pack.push.apply(pack, [r.toString(), g.toString(), b.toString()]);
    this.Emit(pack, callback);
};

/**
 * 设置签字区域背景颜色，默认白色
 * 所属 [用户]
 * @param r
 * @param g
 * @param b
 * @param callback
 */
jSign.prototype.SetBKColor = function (r, g, b, callback) {
    let pack = [];
    let iid = SIGN_IID.SetBKColor.toString();
    pack.push(iid);
    pack.push("SetBKColor");
    pack.push.apply(pack, [r.toString(), g.toString(), b.toString()]);
    this.Emit(pack, callback);
};

/**
 * 设置画笔线条宽度范围
 * 所属 [用户]
 * @param min
 * @param max
 * @param callback
 */
jSign.prototype.SetPenSize = function (min, max, callback) {
    let pack = [];
    let iid = SIGN_IID.SetPenSize.toString();
    pack.push(iid);
    pack.push("SetPenSize");
    pack.push.apply(pack, [min.toString(), max.toString()]);
    this.Emit(pack, callback);
};

/**
 * 保存笔迹图像到本地磁盘
 * 所属 [用户]
 * @param path
 * @param w
 * @param h
 * @param transparent
 * @param callback
 */
jSign.prototype.SaveSignToFile = function (path, w, h, transparent, callback) {
    let pack = [];
    let iid = SIGN_IID.SaveSignToFile.toString();
    pack.push(iid);
    pack.push("SaveSignToFile");

    pack.push.apply(pack, [path, w.toString(), h.toString(), transparent === true ? "1" : "0"]);
    this.Emit(pack, callback);
};

/**
 * 获取笔迹图像base64字符串
 * 所属 [用户]
 * @param w
 * @param h
 * @param transparent
 * @param callback
 */
jSign.prototype.GetSignBase64 = function (w, h, transparent, callback) {
    let pack = [];
    let iid = SIGN_IID.GetSignBase64.toString();
    pack.push(iid);
    pack.push("GetSignBase64");

    pack.push.apply(pack, [w.toString(), h.toString(), transparent === true ? "1" : "0"]);
    this.Emit(pack, callback);
};

/**
 * 激活鼠标控制
 * 所属 [用户]
 * @param active
 * @param callback
 * @constructor
 */
jSign.prototype.ActivateMouse = function (active, callback) {
    let pack = [];
    let iid = SIGN_IID.ActivateMouse.toString();
    pack.push(iid);
    pack.push("ActivateMouse");

    pack.push(active === true ? "1" : "0");
    this.Emit(pack, callback);
};

/**
 * 检查设备连接状态
 * 所属 [用户]
 * @param callback
 */
jSign.prototype.DeviceStatus = function (callback) {
    let pack = [];
    let iid = SIGN_IID.DeviceStatus.toString();
    pack.push(iid);
    pack.push("DeviceStatus");
    this.Emit(pack, callback);
};
/**
 * =====================================================================================
 * 公共扩展接口
 */
/**
 * 弹出签名对话框
 * 所属 [用户]
 * @param callback
 */
jSign.prototype.BeginSign = function (callback) {
    let pack = [];
    let iid = SIGN_IID_EX.BeginSign.toString();
    pack.push(iid);
    pack.push("BeginSign");
    this.Emit(pack, callback);
};

/**
 * 关闭签名对话框
 * 所属 [用户]
 * @param callback
 */
jSign.prototype.EndSign = function (callback) {
    let pack = [];
    let iid = SIGN_IID_EX.EndSign.toString();
    pack.push(iid);
    pack.push("EndSign");
    this.Emit(pack, callback);
};

/**
 * 移动签名窗口到屏幕指定区域
 * 所属 [用户]
 * @param x
 * @param y
 * @param w
 * @param h
 * @param callback
 */
jSign.prototype.MoveSignWindow = function (x, y, w, h, callback) {
    let pack = [];
    let iid = SIGN_IID_EX.MoveSignWindow.toString();
    pack.push(iid);
    pack.push("MoveSignWindow");

    pack.push.apply(pack, [x.toString(), y.toString(), w.toString(), h.toString()]);
    this.Emit(pack, callback);
};


jSign.prototype.ShowURL = function (url, callback) {
    let pack = [];
    let iid = SIGN_IID_EX.ShowURL.toString();
    pack.push(iid);
    pack.push("ShowURL");

    url = url.replace(/\\/ig, "/");
    if (url.search("\%") === -1)
        url = encodeURI(url);

    pack.push(url);
    this.Emit(pack, callback);
};

jSign.prototype.CloseURL = function (callback) {
    let pack = [];
    let iid = SIGN_IID_EX.CloseURL.toString();
    pack.push(iid);
    pack.push("CloseURL");
    this.Emit(pack, callback);
};



/**
 * =====================================================================================
 * 由用户实现的事件接口，可选
 */

/**
 * 网络状态通知函数，格式 function(status)
 * 所属 [用户]
 * @type {null}
 */
jSign.prototype.onStatusChanged = null;

/**
 * 取消按钮事件
 * 所属 [用户]
 * @type {null}
 */
jSign.prototype.onCancel = null;

/**
 * 清除按钮事件
 * 所属 [用户]
 * @type {null}
 */
jSign.prototype.onClear = null;

/**
 * 确定按钮事件
 * 所属 [用户]
 * @type {null}
 */
jSign.prototype.onConfirm = null;

/**
 * 物理设备状态改变事件(接入/移除)，格式 function(id)
 * 所属 [用户]
 * @type {null}
 */
jSign.prototype.onNotify = null;


/**
 * 用户按键事件
 * 所属 [用户]
 * @type {null}
 */
jSign.prototype.onKey = null;

/**
 * =====================================================================================
 * 以下为私有函数
 */

/**
 * 网络状态改变响应函数
 * 所属 [私有]
 * @param mgr
 * @param status
 */
jSign.prototype.NetStatusCallback = function (mgr, status) {
    if (!!mgr.onStatusChanged) {
        mgr.onStatusChanged(status);
    }
    if (!!mgr.rcb) {
        mgr.rcb(status);
        mgr.rcb = null;
    }
};

/**
 * 网络数据响应回调函数
 * 所属 [私有]
 * @param mgr
 * @param data
 */
jSign.prototype.NetDataCallback = function (mgr, data) {
    let nodes = data.split(',');
    let smt = parseInt(nodes[0]);
    switch (smt) {
        case SIGN_MSG_TYPE.RESPONSE: {
            let cid = nodes[1];
            let iid = nodes[2];
            let name = nodes[3];
            let status = nodes[4] === '1';
            let params = [];
            let i = 0;
            for (let node in nodes) {
                if (i > 4) {
                    params.push(nodes[node]);
                }
                i++;
            }
            if (mgr.funcs.hasOwnProperty(iid)) {
                mgr.funcs[iid](status, params);
            }
            mgr.funcs[iid] = null;
            if (mgr.rets.hasOwnProperty(cid)) {
                clearTimeout(mgr.rets[cid].timer);
                mgr.rets[cid] = null;
            }
        }
            break;
        case SIGN_MSG_TYPE.EVENT: {
            let etype = parseInt(nodes[1]);
            switch (etype) {
                case SIGN_EVENT_TYPE.Click:
                    break;
                case SIGN_EVENT_TYPE.Key:
                    {
                        let id = parseInt(nodes[2]);
                        if (!!mgr.onKey && (typeof mgr.onKey) === "function") {
                            mgr.onKey(id);
                        }
                    }
                    break;
                case SIGN_EVENT_TYPE.Custom:
                    {
                        let eid = parseInt(nodes[2]);
                        switch (eid) {
                            case SIGN_EID_CUSTOM.Cancel: {
                                if (!!mgr.onCancel && (typeof mgr.onCancel) === "function") {
                                    mgr.onCancel();
                                }
                            }
                                break;

                            case SIGN_EID_CUSTOM.Clear: {
                                if (!!mgr.onClear && (typeof mgr.onClear) === "function") {
                                    mgr.onClear();
                                }
                            }
                                break;
                            case SIGN_EID_CUSTOM.Confirm: {
                                if (!!mgr.onConfirm && (typeof mgr.onConfirm) === "function") {
                                    mgr.onConfirm();
                                }
                            }
                                break;
                        }
                    }
                    break;
            }
        }
            break;
        case SIGN_MSG_TYPE.NOTIFY: {
            let id = parseInt(nodes[1]);
            switch (id) {
                case SIGN_NOTIFY_TYPE.Status: {
                    if (typeof mgr.onNotify === "function") {
                        mgr.onNotify(parseInt(nodes[2]));
                    }
                }
                    break;
            }
        }
            break;
    }
};

/**
 * 接口请求超时处理回调函数
 * 所属 [私有]
 * @param sender
 * @param cid
 */
jSign.prototype.emitTimeout = function (sender, cid) {
    if (!!sender) {
        if (sender.rets.hasOwnProperty(cid.toString())) {
            let callback = sender.rets[cid.toString()].func;
            if (!!callback) {
                callback(false, ["function request timeout"]);
            }
        }
    }
};

/**
 * 接口调用请求
 * 所属 [私有]
 * @param pack
 * @param callback
 */
jSign.prototype.Emit = function (pack, callback) {

    let iid = pack[0];
    if (!this.hasOwnProperty("funcs")) {
        callback(false, "jSign object uninitialized");
        return;
    }

    if (typeof callback === "function") {
        this.funcs[iid] = callback;
    }
    if (this.cid > 1024) {
        this.cid = 1;
    }
    let cid = ++this.cid;
    let final = [cid.toString()];
    final.push.apply(final, pack);
    this.net.write(final.join(','));

    this.rets[cid.toString()] = {
        func: callback,
        timer: setTimeout(this.emitTimeout, 2000, this, cid)
    };
};

/**
 * 创建一个网络对象
 * 所属 [私有]
 * @returns {{ws: null, url: null, data: {mgr: null, callback: null}, status: {mgr: null, callback: null}, reconn_timer: null, timeout: number, connect: connect, disconnect: disconnect, reconnect: reconnect, do_reconnect: do_reconnect, getNetStatus: getNetStatus, setDataCallback: setDataCallback, setStatusCallback: setStatusCallback, write: write}}
 */
function createNet () {
    return {
        ws: null,
        url: null,
        data: { mgr: null, callback: null },
        status: { mgr: null, callback: null },
        reconn_timer: null,
        timeout: 2000,
        connect: function (url) {
            this.url = url;
            url = "ws://" + url;
            this.ws = new WebSocket(url);
            this.ws.owner = this;
            this.ws.onopen = function () {
                let owner = this.owner;
                if (!!owner.status.callback) {
                    owner.status.callback(owner.status.mgr, true);
                }
            };
            this.ws.onclose = function (e) {
                let owner = this.owner;
                if (!!owner.status.callback) {
                    owner.status.callback(owner.status.mgr, false);
                }
            };
            this.ws.onmessage = function (e) {
                let owner = this.owner;
                if (!!owner.data.callback) {
                    owner.data.callback(owner.data.mgr, e.data);
                }
            };
            this.ws.onerror = function (e) {
                let owner = this.owner;
                owner.disconnect();
            }
        },
        disconnect: function () {
            if (this.ws != null) {
                this.ws.close();
                this.ws = null;
            }
        },
        reconnect: function () {
            this.disconnect();
            this.connect(this.url);

        },
        do_reconnect: function (activate) {
            if (activate !== true && reconn_timer != null) {
                clearInterval(this.reconn_timer);
                this.reconn_timer = null;
            }
            if (activate === true && reconn_timer == null) {
                this.reconn_timer = setInterval(function (net) {
                    console.log("net reconnect");
                }, this.timeout);
            }
        },
        getNetStatus: function () {
            if (!!this.ws) {
                return this.ws.readyState === WebSocket.OPEN;
            }
            return false;
        },
        setDataCallback: function (mgr, callback) {
            this.data.mgr = mgr;
            this.data.callback = callback;
        },
        setStatusCallback: function (mgr, callback) {
            this.status.mgr = mgr;
            this.status.callback = callback;
        },
        write: function (data) {
            if (this.getNetStatus()) {
                try {
                    this.ws.send(data);
                }
                catch (e) {
                    return false;
                }
                return true;
            }
            return false;
        }
    }
}

export default jSign;

