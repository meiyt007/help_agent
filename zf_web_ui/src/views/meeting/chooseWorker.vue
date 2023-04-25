<template>
    <div class="depart-container">
        <div class="content">
            <div class="workers">
                <div class="item" @click="toPhone(item.phone,item.name)" :key="item.id" v-for="item in list">
                    <div class="name">{{item.name}}</div>
                    <div class="num">工号:{{ item.workNumber }}</div>
                    <div class="phone flex"><span>号码:</span><span>{{item.phone}}</span></div>
                    <div class="remark flex"><span>备注:</span><span>{{item.memo}}</span></div>
                    <div class="btn" v-if="type == 'voice'">电话咨询</div> 
                    <div class="btn" v-else>视频咨询</div> 
                </div>
                <!-- <div class="item">
                    <div class="name">张老师</div>
                    <div class="num">工号:20202</div>
                    <div class="phone flex"><span>号码:</span><span>0648-343434</span></div>
                    <div class="remark flex"><span>备注:</span><span>需人工转分机02-2323</span></div>
                    <div class="btn">视频咨询</div> 
                </div>
                <div class="item">
                    <div class="name">张老师</div>
                    <div class="num">工号:20202</div>
                    <div class="phone flex"><span>号码:</span><span>0648-343434</span></div>
                    <div class="remark flex"><span>备注:</span><span>需人工转分机02-2323</span></div>
                    <div class="btn">视频咨询</div> 
                </div>
                <div class="item">
                    <div class="name">张老师</div>
                    <div class="num">工号:20202</div>
                    <div class="phone flex"><span>号码:</span><span>0648-343434</span></div>
                    <div class="remark flex"><span>备注:</span><span>需人工转分机02-2323</span></div>
                    <div class="btn">视频咨询</div> 
                </div>
                <div class="item">
                    <div class="name">张老师</div>
                    <div class="num">工号:20202</div>
                    <div class="phone flex"><span>号码:</span><span>0648-343434</span></div>
                    <div class="remark flex"><span>备注:</span><span>需人工转分机02-2323</span></div>
                    <div class="btn">视频咨询</div> 
                </div> -->
            </div>
        </div>    
        <div class="dialog-footer center">
            <p @click="cancelDialog">上一步</p>
      </div>
      <!-- <el-dialog
        :visible.sync="videoShow"
        width="60%"
        class=""
        custom-class="serviceTransfer videoShow"
        v-dialogDrag
        :fullscreen="true"
        :show-close="false"
        >
        <videoPhone v-if="videoShow" :name="name" :phone="phone"  @close="closeVideo"/>
        </el-dialog> -->
        <!-- <el-dialog
        :visible.sync="shipinShow"
        width="90%"
        class=""
        custom-class="serviceTransfer videoShow"
        v-dialogDrag
        >
            <iframe style="width:100%;height:100%;" src="tcmeeting://?roomID=102&account=tc01&password=123456&rnd=1 " frameborder="0"></iframe>
        </el-dialog> -->
    </div>
</template>
<script>
    import { queryVideoPage } from "@/api/modules/business";
    // import videoPhone from './videoPhone.vue';
    export default{
        name:'chooseWorker',
        data(){
            return{
                videoShow:false,
                list:[],
                phone:'',
                shipinShow:false
            }
        },
        props:{
            organOid: {
                type: String,
                default: () => "",
            },
            type: {
                type: String,
                default: () => "",
            },
        },
        components:{
            // videoPhone
        },
        mounted(){
            let that = this;
            queryVideoPage({
                organOid:this.organOid
            }).then(res=>{
                if(res.data.data.length > 0){
                    this.list = res.data.data;
                }else{
                    that.$message.warning("该部门暂无工作人员！"); 
                    that.cancelDialog();
                }
                
            })
        },  
        methods:{
            toPhone(phone,name){
                this.type = 'voice';
                if(this.type == 'voice'){
                    this.phone = phone;
                    this.name = name;
                    this.videoShow = true;
                }else{
                   this.shipinShow = true;
                }
                
            },
            closeVideo(){
                this.videoShow = false;
            },
            cancelDialog(){
                this.$emit("closeWorker");
            }
        }
    }
</script>
<style lang="scss" scoped>  
    @import './chooseWorker.scss';
    
   
        ::v-deep  .el-dialog__header{
            display: none !important;
        }
        ::v-deep  .el-dialog__body{
            padding: 0 !important;
            max-height: 100% !important;
            height: 100% !important;
        }
    
</style>