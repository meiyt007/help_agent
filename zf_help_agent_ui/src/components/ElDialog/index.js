/** 
 * extends el-dialog component
 * by shmao 2021/8/5
 */

import { Dialog } from "element-ui";
import './index.scss';

export default {
    name: 'ElDialog',
    extends: Dialog,
    props: {
        height: String, // 新增属性 弹框高度
        scrollbar: Boolean, // 新增属性 弹框内部滚动
        closeOnClickModal: { // 修改原表单属性
            type: Boolean,
            default: false
        },
        closeOnPressEscape: { // 修改原表单属性
            type: Boolean,
            default: false
        },

        bodyBackgroundColor: {
            type: String,
            default: ''
        },

        footerCenter: {
            type: Boolean,
            default: true
        }
    },

    data () {
        return {
            elDialogBodyHeight: '',
            key: 0,
        }
    },

    computed: {
        style () {
            let style = {};
            if (!this.fullscreen) {
                style.marginTop = this.top;
                if (this.width) {
                    style.width = this.width;
                }
                if (this.height) {
                    style.height = this.height;
                }
                return style;
            }
        },
    },

    watch: {
        visible: {
            handler (val) {
                if (val) {
                    this.closed = false;
                    this.$emit('open');
                    this.$el.addEventListener('scroll', this.updatePopper);
                    this.$nextTick(() => {
                        this.$refs.dialog.scrollTop = 0;
                    });
                    if (this.appendToBody) {
                        document.body.appendChild(this.$el);
                    }

                    /** 
                     * dynamic computing dialog height when dialog opened, 
                     * because if dialog dont set `v-if` directive, 
                     * but dialog will render immediately but dialog not opened
                     *  */
                    this.setDialogBodyHeight();

                    this.$nextTick(this.setDialogPosition);
                } else {
                    this.$el.removeEventListener('scroll', this.updatePopper);
                    if (!this.closed) this.$emit('close');
                    if (this.destroyOnClose) {
                        this.$nextTick(() => {
                            this.key++;
                        });
                    }
                }
            }
        }
    },

    methods: {
        /** set dialog body height */
        setDialogBodyHeight () {
            this.$nextTick(() => {
                let marginBottom = 0, marginTop = 0;
                if (this.$refs.footer) {
                    const style = window.getComputedStyle(this.$refs.footer, null);
                    marginBottom = style['margin-bottom'];
                    marginTop = style['margin-top'];
                }
                if (this.height) {
                    this.elDialogBodyHeight = `calc(100% - ${this.$refs?.header?.offsetHeight ?? 0}px - ${this.$refs?.footer?.offsetHeight ?? 0}px - ${marginBottom} - ${marginTop})`;
                }
            })
        }
    },

    beforeCreate () {
        this.setDialogPosition = () => {
            if (!this.visible) return;
            /** 
             * set dialog position
             * if dialog height >= clientHeight, dialog position in client center
             * else keep default
             */
            const el = this.$refs.dialog;
            const clientHeight = document.documentElement.clientHeight,
                clientWidth = document.documentElement.clientWidth;

            if (el.offsetHeight >= (clientHeight / 2)) {
                const top = (clientHeight - el.offsetHeight) / 2 + 'px',
                    left = (clientWidth - el.offsetWidth) / 2 + 'px';
                /** don't set top when client height <= el-dialog height */
                if (clientHeight > el.offsetHeight) {
                    el.style.top = top;
                }
                el.style.left = left;
                el.style.margin = 'unset';
            }
        }

        window.addEventListener('resize', this.setDialogPosition);
    },

    mounted () {
        /** 
         * dynamic computing dialog height when dialog opened, 
         * because if dialog dont set `v-if` directive, 
         * but dialog will render immediately but dialog not opened
         * 
         * this is for set `v-if`
         *  */
        this.setDialogBodyHeight();

        this.$nextTick(this.setDialogPosition);
    },

    beforeDestroy () {
        window.removeEventListener('resize', this.setDialogPosition);
    },
    render (...rest) {
        let vNodes = Dialog.render.apply(this, rest);

        vNodes.componentOptions.children[0].children[0].children[0].data.ref = 'header';

        if (this.rendered && this.scrollbar) {
            vNodes.componentOptions.children[0].children[0].children[1] = (
                <div
                    class="el-dialog__body"
                    style={{
                        height: this.elDialogBodyHeight,
                        backgroundColor: this.bodyBackgroundColor
                    }}
                >
                    <el-scrollbar ref="scrollbar" style="height: 100%">
                        {this.$slots.default}
                    </el-scrollbar>
                </div >
            )
        } else if (this.rendered) {
            vNodes.componentOptions.children[0].children[0].children[1] = (
                <div class="el-dialog__body" style={{
                    height: this.elDialogBodyHeight,
                    backgroundColor: this.bodyBackgroundColor
                }}>
                    {this.$slots.default}
                </div>
            )
        }

        if (this.$slots.footer) {
            vNodes.componentOptions.children[0].children[0].children[2] = (
                <div ref="footer" class="el-dialog__footer" style={{
                    textAlign: this.footerCenter || this.center ? 'center' : 'right'
                }}>
                    {this.$slots.footer}
                </div>
            )
        }

        return vNodes;
    }
}