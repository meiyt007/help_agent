/** 智能咨询 */
import { mapGetters, mapMutations } from "vuex";
import { MAKE } from "@/utils/config";
import { getIntelligentAnswer } from "@/api/zc/intelligentAq/intelligentAq";
export default {
    data () {
        return {
            // 人工服务 智能问答
            artificialService: false,
            isNotFullScreen: true, // 不是全屏状态 只针对1920分辨率下 不全屏 高度!=1080 宽度=1920
            situationAnswerList: [], // 情景选项信息默认选中信息
            microphoneSwitch: false, // 智能问答麦克风
            intelligentMsgList: [],
            websocket: null,
            flag: false,
            text: "道路货物运输经营许可",
        }
    },
    computed: {
        ...mapGetters(["open", "switchOnOff", "deviceMap", "znzxFlag", "currentPersonNum"]),
        getOpen () {
            return this.open;
        },
        getswitchOnOff () {
            return this.switchOnOff;
        },
        isWindowAcceptance ({ $route: { path } }) {
            const isWindowAcceptance = path.includes("windowAcceptance") || path.includes("comboWindowAccept");
            if (isWindowAcceptance) {
                this.artificialService = false;
            }
            return isWindowAcceptance;
        }
    },
    watch: {

        isWindowAcceptance: {
            immediate: true,
            handler (val) {
                if (val) {
                    this.artificialService = false;
                } else {
                    this.getOpenBollen();
                }
            },
        },

        commonVisible: {
            handler (val) {
                // 清除缓存
                localStorage.removeItem('idCardInfo');

                if (!val) {
                    // 单办
                    this.queryParams.serviceOids = [];
                    this.serviceOid = '';

                    // 一件事
                    this.queryParams.comboDirectoryOids = [];
                    this.comboDirectoryOid = '';
                }
            },
            immediate: true
        },

        getOpen () {
            this.artificialService = this.open;
        },
        getswitchOnOff () {
            this.microphoneSwitch = this.switchOnOff;
            this.onSwitchAq();
        },

        currentPersonNum: {
            immediate: true,
            handler () {
                this.intelligentMsgList = [];
                this.situationAnswerList = [];
            }
        }
    },
    beforeCreate () {
        // 注册全局监听事件
        this.onWindowResize = () => {
            this.isNotFullScreen =
                document.body.scrollHeight < window.screen.height &&
                document.body.scrollWidth === window.screen.width;
        };
    },
    created () {
        this.onMicrophoneSwitch();
        this.getOpenBollen();
        this.getWebSocketMessage();
        window.addEventListener("resize", this.onWindowResize);
    },
    mounted () {
        this.$nextTick(this.onWindowResize);

        this.$bus.$off('ON_ACCEPTANCE_LIST_REFRESH').$on('ON_ACCEPTANCE_LIST_REFRESH', this.getList)
    },
    updated () {
        // 聊天定位到底部
        if (this.isWindowAcceptance || !this.znzxFlag) return;
        let ele = this.$refs.asMain;
        ele.scrollTop = ele.scrollHeight;
    },
    beforeDestroy () {
        window.removeEventListener("resize", this.onWindowResize);

        this.$bus.$off('ON_ACCEPTANCE_LIST_REFRESH');
    },
    methods: {
        ...mapMutations(["CHANGE_SWITCHONOFF", "CHANGE_OPEN"]),
        handleArtificialService () {
            this.artificialService = true;
        },
        foldingArtificialService () {
            this.artificialService = false;
            // this.microphoneSwitch = false;
            // this.CHANGE_OPEN(false);
            // this.CHANGE_SWITCHONOFF(false);
            // this.send("stop");
        },
        //智能咨询展开关闭开关
        getOpenBollen () {
            if (this.open) {
                this.artificialService = this.open;
            }
        },
        //打开麦克风开始语音咨询
        onMicrophoneSwitch () {
            this.onSwitchAq();
        },
        onSwitchAq () {
            if (this.microphoneSwitch) {
                getIntelligentAnswer("/r");
                this.send("start");
                this.flag = true;
            } else if (!this.microphoneSwitch && this.flag) {
                // this.closeWebSocket();
                this.send("stop");
            }
        },

        /** 监听麦克风发送的消息 */
        async getWebSocketMessage () {
            //判断当前浏览器是否支持WebSocket
            if ("WebSocket" in window) {
                const wss = this.deviceMap?.[MAKE];
                console.log(wss);
                this.websocket = new WebSocket(wss); // 这里会传递一些数据，
            } else {
                this.$message.error("当前浏览器 Not support websocket");
            }
            //连接发生错误的回调方法
            this.websocket.onerror = function () {
                console.log("WebSocket连接发生错误");
            };
            //连接成功建立的回调方法
            this.websocket.onopen = () => {
                console.log("WebSocket连接成功");
            };
            //接收到消息的回调方法
            this.websocket.onmessage = async event => {
                let objMsg = {};
                try {
                    objMsg = JSON.parse(event.data);
                } catch { }
                // console.log(objMsg);
                this.text = objMsg?.message?.replace?.(/\s*/g, "");
                if (!this.text) return;

                if (objMsg.type === "realTime") {
                    this.intelligentMsgList = this.intelligentMsgList.filter(
                        item => item.type !== 0
                    );
                    this.intelligentMsgList.push({
                        type: 0,
                        content: this.text,
                        me: true
                    });
                }

                if (objMsg.type == "final") {
                    // 删除上个 realTime 的
                    this.intelligentMsgList = this.intelligentMsgList.filter(
                        item => item.type !== 0
                    );
                    // this.text = event.data.substring(event.data.lastIndexOf(":") + 1);
                    this.intelligentMsgList.push({
                        type: 1,
                        content: this.text,
                        me: true
                    });

                    const { code, data } = await getIntelligentAnswer(this.text);
                    if (code !== 200 && data.status == 0) return;
                    this.intelligentAnswers = JSON.parse(data);

                    // 情形选项
                    if (this.intelligentAnswers.solutionDict) {
                        this.situationAnswerList = Object.keys(
                            this.intelligentAnswers.solutionDict
                        );
                    }
                    this.intelligentMsgList.push({
                        type: 1,
                        message: this.intelligentAnswers,
                        content: this.intelligentAnswers.itemDict,
                        me: false
                    });

                    if (this.intelligentAnswers?.itemDict) {
                        let list = Object.keys(this.intelligentAnswers.itemDict);
                        if (list.length === 1) {
                            this.handleIntelligentAnswer(this.intelligentAnswers.itemDict[list[0]]);
                        }
                    }
                }
            };
            //连接关闭的回调方法
            this.websocket.onclose = () => {
                console.log("WebSocket连接关闭");
            };
        },

        handleIntelligentAnswer (val) {
            // 如果没打开聊天框 就return 
            if (!this.artificialService) return;

            // 如果是相同的事项 就return
            if (this.serviceOid == val || this.comboDirectoryOid == val) {
                return;
            }
            this.loading = true;
            this.getList(val).then(response => {
                this.queryParams.pageNum = 1;
                this.serviceList = response.data.data;
                this.total = response.data.total;
                this.loading = false;
                if (this.serviceList.length > 0) {
                    this.commonVisible = false;
                    this.$nextTick(() => {
                        this.registerCase(this.serviceList[0], '', true);
                    })
                }

            });
            this.queryParams.serviceOids = [];
            this.queryParams.comboDirectoryOids = [];
        },

        send (msg) {
            this.websocket.send(msg);
            console.log(msg);
        },

        //关闭WebSocket连接
        closeWebSocket () {
            this.websocket.onclose = function () {
                console.log("WebSocket连接关闭");
            };
            this.websocket.close();
        },

        // 办事指南
        radioListfunction (text) {
            this.intelligentMsgList.push({
                type: 1,
                content: text,
                me: true
            });

            getIntelligentAnswer(text).then(res => {
                if (res.code == 200 && res.data.status !== 0) {
                    this.intelligentAnswers = JSON.parse(res.data);
                    let flag = this.intelligentAnswers.changeItemFlag;
                    if (this.intelligentAnswers.solutionDict) {
                        this.situationAnswerList = Object.keys(
                            this.intelligentAnswers.solutionDict
                        );
                    }
                    this.intelligentMsgList.push({
                        type: 1,
                        message: this.intelligentAnswers,
                        content: this.intelligentAnswers.itemDict,
                        me: false
                    });

                    let list = Object.keys(this.intelligentAnswers?.itemDict ?? {});

                    if (list.length === 1) {
                        let val = this.intelligentAnswers.itemDict[list[0]];
                        // 如果是相同的事项 就return
                        if (this.serviceOid == val || this.comboDirectoryOid == val) {
                            return;
                        }
                        this.loading = true;
                        this.getList(val).then(response => {
                            this.queryParams.pageNum = 1;
                            this.serviceList = response.data.data;
                            this.total = response.data.total;
                            this.loading = false;
                            if (this.serviceList.length > 0 && flag == 0) {
                                this.commonVisible = false;
                                setTimeout(() => {
                                    this.registerCase(this.serviceList[0], '', true);
                                }, 200);
                            }
                        });

                        this.queryParams.serviceOids = [];

                        this.queryParams.comboDirectoryOids = [];

                    }

                }
            })
        },

        /** 点击事项 */
        handleAnswerClick (val) {
            if (val) {
                this.handleIntelligentAnswer(val);
            }
        }
    }
}
