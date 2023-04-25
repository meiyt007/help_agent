<template>
  <el-form ref="form" :model="user" :rules="rules" label-width="80px">
    <el-form-item label="旧密码" prop="oldPassword">
      <el-input v-model="user.oldPassword" placeholder="请输入旧密码" type="password"/>
    </el-form-item>
    <el-form-item label="新密码" prop="newPassword">
      <el-input v-model="user.newPassword" placeholder="请输入新密码" type="password"/>
    </el-form-item>
    <el-form-item label="确认密码" prop="confirmPassword">
      <el-input v-model="user.confirmPassword" placeholder="请确认密码" type="password"/>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" size="mini" @click="submitPwd">保存</el-button>
      <el-button type="danger" size="mini" @click="close">关闭</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
  import {updatePassword} from "@/api/sys/login";
  import {validatePassword} from '@/utils/validate';
  import { encryptPwd } from '@/utils/jsencrypt'
  import user from "@/store/modules/user";
  export default {
    props: {
      user: {
        type: Object
      }
    },
    data() {
      const equalToPassword = (rule, value, callback) => {
        if (this.user.newPassword !== value) {
          callback(new Error("两次输入的密码不一致"));
        } else {
          callback();
        }
      };
      return {
        test: "pwd",
        // 表单校验
        rules: {
          oldPassword: [
            {required: true, message: "旧密码不能为空", trigger: "blur"}
          ],
          newPassword: [
            {required: true, message: "新密码不能为空", trigger: "blur"},
            {required: true, validator: validatePassword, trigger: 'blur'}
          ],
          confirmPassword: [
            {required: true, message: "确认密码不能为空", trigger: "blur"},
            {required: true, validator: equalToPassword, trigger: "blur"}
          ]
        }
      };
    },
    methods: {
      submitPwd() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            let oldPassword=this.user.oldPassword
            let newPassword=this.user.newPassword
            let confirmPassword=this.user.confirmPassword
            let userOid=this.user.userOid
            if("1"===this.user.config){
              oldPassword=encryptPwd(this.user.publicKey,oldPassword);
              newPassword=encryptPwd(this.user.publicKey,newPassword);
              confirmPassword=encryptPwd(this.user.publicKey,confirmPassword);
              userOid=this.user.userOid
            }
            const data = {
              oldPassword: oldPassword,
              password: newPassword,
              confirmPassword: confirmPassword,
              userOid: userOid
            }
            updatePassword(data).then(
              response => {
                if (response.code === 200) {
                  this.msgSuccess("修改成功");
                  this.$store.dispatch('LogOut').then(() => {
                    location.reload()
                  })
                }
              }
            );
          }
        });
      },
      close() {
        this.$store.dispatch("tagsView/delView", this.$route);
        this.$router.push({path: "/welcome"});
      }
    }
  };
</script>
