<template>
    <div>
        <el-form
            v-if="showAddDocument"
            :model="documentInfo"
            :rules="documentRules"
            ref="document"
            label-width="100px"
        >
            <el-form-item label="姓名" prop="name">
                <el-input
                    v-model.trim="documentInfo.name"
                    placeholder="请输入姓名"
                ></el-input>
            </el-form-item>
            <el-form-item label="手机号码" prop="phone">
                <el-input
                    v-model.trim="documentInfo.phone"
                    placeholder="请输入手机号码"
                ></el-input>
            </el-form-item>
            <el-form-item label="证件类型" prop="cardType">
            <el-select
                v-model="documentInfo.cardType"
                placeholder="请选择证件类型"
            >
                <el-option label="身份证" value="1"></el-option>
                <el-option label="港澳通行证" value="2"></el-option>
                <el-option label="护照" value="3"></el-option>
            </el-select>
            </el-form-item>
            <el-form-item label="证件号码" prop="cardNo">
            <el-input
                v-model.trim="documentInfo.cardNo"
                placeholder="请输入证件号码"
            ></el-input>
            </el-form-item>
            <!-- <el-form-item label="备注" prop="resourceInfo">
            <el-input type="textarea" v-model="documentInfo.resourceInfo"></el-input>
            </el-form-item> -->
        </el-form>
        <div slot="footer" class="dialog-footer">
            <p @click="compliteNext">确定</p>
        </div>
    </div>
</template>
<script>
    import { takeNumHelpInfo } from "@/api/modules/helpAgent";
    export default{
        name:'getNumber',
        data(){
            return{
                showAddDocument:true,
                documentInfo: {
                    name: "",
                    phone:'',
                    cardType: "1",
                    cardNo:'',
                    companyName:'',
                    companyCode:'',
                    workUserIds:'',
                    takeNumberType:'3',
                    groupId:'',
                    haType:'1'
                },
                documentRules: {
                    name: [{ required: true, message: "请输入姓名", trigger: "blur" }],
                    cardNo: [{ required: true, message: "请输入证件号码", trigger: "blur" }],
                    phone: [{ required: true, message: "请输入手机号码", trigger: "blur" }],
                    cardType: [
                    { required: true, message: "请选择证件类型", trigger: "change" },
                    ],
                },
            }
        },
        mounted(){
            this.documentInfo.workUserIds =  this.$route.query.workUserIds;
            this.documentInfo.groupId =  this.$route.query.groupId;
        },
        methods:{
            //补录信息  确定
            compliteNext(){
                let that = this;
                this.$refs["document"].validate((valid) => {
                    if (valid) {
                        takeNumHelpInfo(that.documentInfo).then(res=>{
                            if(res.code == 200){
                                this.$message("取号成功！");
                            }
                        })
                    }
                })
            },
        }
    }
</script>
<style lang="scss" scoped>
  @import './index.scss';
</style>