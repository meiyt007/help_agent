/** 人证比对 */

import HardWareScan from "@/views/common/hardwareScan";
import { showCallMessage, } from "@/api/zc/businessManagement/doubleScreenInteraction";
export default {
    components: { HardWareScan, },
    data () {
        return {
            hardWareVisible: false,
            rzbdResult: ''
        }
    },
    methods: {
        //调用摄像头获取图像信息
        getImageCamera () {
            this.hardWareVisible = true;
        },

        closeHard (rzbdResult) {
            this.rzbdResult = this.$refs?.scanForm.rzbdResult || rzbdResult;
            this.hardWareVisible = false;
            //推送小屏办事指南信息
            //this.openWelcome();
        },

        //超级综窗柜台双屏登录打开用户信息
        /*async openWelcome () {
            let url = window.location.origin;
            let data = {};
            data.userName = this.loginUser.userName;
            data.organName = this.loginUser.organName;
            data.isWorking = 1;
            let pushUrl =
                url +
                "/serviceHall/suspendedService.html?jsonObject=" +
                JSON.stringify(data);
            await showCallMessage(pushUrl).then(response => {
                console.log(response);
            });
        },*/
    }
}
