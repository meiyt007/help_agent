//视频咨询
import { endVideoHelp,exitRoom } from "@/api/modules/helpAgent";
export default{
    data(){
        return{
            // finishType:'',   //
        }
    },
    methods:{
        // 打开视频
        startOpenVideo(data,type){
            if(data.accessId){
                localStorage.setItem("accessId",data.accessId);
            }
            if(data.id){
                localStorage.setItem("accessId",data.id);
            }
            
            if(data.roomId){
                localStorage.setItem("roomId",data.roomId);
            }
            if(data.roomOid){
                localStorage.setItem("roomId",data.roomOid);
            }
            if(data.screenVideoSig){
                localStorage.setItem('screenVideoSig',data.screenVideoSig)
            }
            if(data.screenVideoNum){
                localStorage.setItem('screenVideoNum',data.screenVideoNum)
            }
            localStorage.setItem("mainSig",data.sig);
            localStorage.setItem("userId",data.videoNum);
            localStorage.setItem("videoNum",data.videoNum);
            localStorage.setItem("roomNumber",data.roomNumber);
            localStorage.setItem("appId",data.videoAppId);
            localStorage.setItem("userName",this.$store.state.user.basicUserInfo.name);
        },
        //清空视频数据
        clearVideoData(){
            localStorage.removeItem('accessId');
            localStorage.removeItem('roomId');
            localStorage.removeItem('mainSig');
            localStorage.removeItem('userId');
            localStorage.removeItem('videoNum');
            localStorage.removeItem('roomNumber');
            localStorage.removeItem('appId');
            localStorage.removeItem('userName');
            localStorage.removeItem('screenVideoSig');
            localStorage.removeItem('screenVideoNum');
            localStorage.removeItem('callUserId');
        },
        //结束视频
        closeVideoHelp(callBack){
            let that = this;
            let callUserId = localStorage.getItem('callUserId');
            let loginId = this.$store.state.user.basicUserInfo.id;
            if(callUserId != loginId){
                exitRoom({
                    accessId:localStorage.getItem('accessId'),
                    workUserId:this.$store.state.user.basicUserInfo.id,
                    roomId:localStorage.getItem('roomId'),
                }).then(res=>{
                    if(res.code == 200){
                        that.clearVideoData();
                        callBack('exit')
                    }
                })
            }else{
                endVideoHelp({
                    roomId:localStorage.getItem('roomId')
                }).then(res=>{
                    if(res.code == 200){
                        that.clearVideoData();
                        callBack('end')
                    }
                })
            }
        }
    }
}