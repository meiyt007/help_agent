<template>
    <div class="matrial-add">
        <div>
            <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="120px" class="demo-ruleForm">
                <el-form-item class="single-form" label="资源名称" prop="name">
                    <el-input v-model="ruleForm.name"></el-input>
                </el-form-item>
                <el-form-item class="single-form"  label="资源类型" prop="region">
                    <el-select v-model="ruleForm.region" placeholder="请选择活动区域">
                        <el-option label="区域一" value="shanghai"></el-option>
                        <el-option label="区域二" value="beijing"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item class="single-form"  label="文件上级" prop="superior">
                    <el-select v-model="ruleForm.superior" placeholder="请选择文件上级">
                        <el-option label="区域一" value="shanghai"></el-option>
                        <el-option label="区域二" value="beijing"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item  class="single-form"   label="所属工作人员" prop="worker">
                    <el-select v-model="ruleForm.worker" placeholder="请选择所属工作人员">
                        <el-option label="区域一" value="shanghai"></el-option>
                        <el-option label="区域二" value="beijing"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="附件上传" prop="file">
                    <el-button size="small" type="primary">点击上传</el-button>
                </el-form-item>
                <input type="file" id="fileInputValue" style="display: none"></input>
                <el-form-item label="备注" prop="desc">
                    <el-input type="textarea" v-model="ruleForm.desc"></el-input>
                </el-form-item>
                <el-form-item class="form-btn">
                    <el-button class="btn-prev" @click="resetForm('ruleForm')">取消</el-button>
                    <el-button class="btn-next" type="primary" @click="submitForm('ruleForm')">确定</el-button>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>
<script>

export default {
    name: 'materialAdd',
    data() {
        return {
            ruleForm: {
                name: '',
                region: '',
                superior:'',
                worker:'',
                desc: '',
                file:''
            },
            rules: {
                name: [
                    { required: true, message: '请输入资源名称', trigger: 'blur' },
                ],
                region: [
                    { required: true, message: '请选择资源类型', trigger: 'change' }
                ],
                superior:[
                    { required: true, message: '请选择文件上级', trigger: 'change' }
                ],
                worker:[
                    { required: true, message: '请选择所属工作人员', trigger: 'change' }
                ],
                // desc: [
                //     { required: true, message: '请填写备注', trigger: 'blur' }
                // ],
                file:[
                    { required: true, message: '请选择上传文件', trigger: 'change' }
                ],
            }
        }
    },
    methods: {
        submitForm(formName) {
            const that = this
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    const data = new FormData()
                    data.append('file', that.ruleForm.file)
                    alert('submit!');
                } else {
                    alert('error submit!!');
                    return false;
                }
            });
        },
        resetForm(formName) {
            this.$refs[formName].resetFields();
            this.$emit('close')
        },
        // 上传
        fileUpload: function() {
            this.uploadType = type
            document.querySelector('input[type=file]').click()
            const that = this
            const file = document.querySelector('input[type=file]')
            file.onchange = () => {
                var fileData = file.files[0]
                // console.log(fileData)
                that.ruleForm.file = fileData
                // 创建用来读取此文件的对象
                var reader = new FileReader()
                // 使用该对象读取file文件
                reader.readAsDataURL(fileData)
                // 读取文件成功后执行的方法函数
                reader.onload = function(e) {
                }
            }
        },
    }
}
</script>
<style lang="scss">
@import './index.scss';
</style>