import { mapGetters } from "vuex";
import { MAKE } from "@/utils/config";

export default {
    data () {
        return {
            voiceStart: false, // 是否点击语音
            voiceContent: '',
            showTooltip: false,
            websocket: null,
        }
    },
    computed: {
        ...mapGetters(["deviceMap"]),
    },
    watch: {
        voiceStart () {
            if (this.voiceStart) {
                this.startWebSocket();
                // this.handleTest();
            } else {
                this.onclose();
                // this.handleTestOver();
            }
        }
    },
    destroyed () {
        this.onclose();
    },
    methods: {
        handleVoiceStart () {
            this.voiceStart = !this.voiceStart;
        },

        tooltipOver () {
            if (!this.voiceStart) {
                this.showTooltip = true;
            }
        },

        tooltipLeave () {
            if (!this.voiceStart) {
                this.showTooltip = false;
            }
        },

        startWebSocket () {
            if (this.websocket) {
                this.onclose();
                return;
            }

            const wss = this.deviceMap?.[MAKE];
            this.websocket = new WebSocket(wss);

            this.onerror();

            this.onopen();

            this.onmessage();
        },

        onerror () {
            if (this.websocket) {
                this.websocket.onerror = () => {
                    this.voiceStart = false;
                    this.showTooltip = false;
                    this.websocket = null;
                    this.$message.warning('麦克风连接失败');
                }
            }
        },

        onopen () {
            if (this.websocket) {
                this.websocket.onopen = () => {
                    this.$message.success('麦克风连接成功');
                    this.send("start");
                }
            }
        },

        onclose () {
            if (this.websocket) {
                this.websocket.close();
                this.websocket = null;
                this.voiceStart = false;
                this.showTooltip = false;
            }
        },

        onmessage () {
            if (this.websocket) {
                this.websocket.onmessage = (event) => {
                    if (event.data) {
                        let objMsg = {};

                        try {
                            objMsg = JSON.parse(event.data);
                        } catch { }

                        this.voiceContent = objMsg?.message?.replace?.(/\s*/g, "");

                        if (!this.voiceContent) return;

                        this.$emit('input', this.voiceContent);
                    }
                }
            }
        },

        send (msg) {
            if (this.websocket) {
                console.log('%c [send msg]:', 'color:red;font-weight:700;', msg);
                this.websocket.send(msg);
            }
        },

        handleInput (event) {
            // should not emit input during composition
            // see: https://github.com/ElemeFE/element/issues/10516
            if (this.isComposing) return;

            // hack for https://github.com/ElemeFE/element/issues/8548
            // should remove the following line when we don't support IE
            if (event.target.value === this.nativeInputValue || event.target.value === this.voiceContent) return;

            this.$emit('input', event.target.value);

            // ensure native input value is controlled
            // see: https://github.com/ElemeFE/element/issues/12850
            if (this.voiceStart) {
                this.$nextTick(this.setInputValue);
            } else {
                this.$nextTick(this.setNativeInputValue);
            }
        },

        setInputValue () {
            const input = this.getInput();
            if (!input) return;
            if (input.value === this.voiceContent) return;
            input.value = this.voiceContent;
        },

        handleTest () {
            this.voiceContent = '开始';
            this.$emit('input', this.voiceContent);
            setTimeout(() => {
                this.voiceContent = '测试'
                this.$emit('input', this.voiceContent);

                setTimeout(() => {
                    this.voiceContent = '测试12313'
                    this.$emit('input', this.voiceContent);

                    setTimeout(() => {
                        this.handleTestOver();
                    }, 3000);

                }, 3000);
            }, 3000);
        },

        handleTestOver () {
            this.voiceContent = '结束';
            this.$emit('input', this.voiceContent);
            this.voiceStart = false;
            this.showTooltip = false;
        }
    }
}