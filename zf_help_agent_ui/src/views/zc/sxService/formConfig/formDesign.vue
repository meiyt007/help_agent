<template>
  <div class="formDesign">
    <FormDesign
      :authorizeKey="authorizeKey"
      :designOid="designOid"
      :formCode="formCode"
      :isPublish="true"
    />
  </div>
</template>

<script>
import {
  updateDesignOid,
  updateChildName
} from "@/api/zc/sxService/serviceFormConfig/formDesign";
import { queryChildFormName } from "@/api/form/manager";
export default {
  name: "formDesign",
  computed: {
    authorizeKey ({
      $route: {
        query: { authorizeKey }
      }
    }) {
      return authorizeKey;
    },

    serviceOid ({
      $route: {
        query: { serviceOid }
      }
    }) {
      return serviceOid;
    },

    designOid ({
      $route: {
        query: { designOid }
      }
    }) {
      return designOid;
    },

    formCode ({
      $route: {
        query: { formCode }
      }
    }) {
      return formCode;
    },

    formOid ({
      $route: {
        query: { formOid }
      }
    }) {
      return formOid;
    }
  },
  mounted () {
    window.addEventListener("message", this.getDesignData, true);
  },
  destroyed () {
    window.removeEventListener("message", this.getDesignData, true);
  },
  data () {
    return { formMainOid: "", designOid: "" };
  },
  methods: {
    // 保存designOid
    getDesignData (e) {
      if (e?.data?.indexOf?.("designSaveBackData") == 0) {
        let data = e.data.replace("designSaveBackData-", "");
        let formData = eval("(" + data + ")");
        updateDesignOid(
          this.formOid,
          formData.designOid,
          formData.formMainOid,
          this.serviceOid
        ).then(response => {
          if (response.code === 200) {
            queryChildFormName(this.authorizeKey, formData.formMainOid).then(
              res => {
                if (res.data != null) {
                  let names = "";
                  res.data.forEach(item => {
                    names += item.variableName + ",";
                  });
                  updateChildName(this.formOid, names).then(mess => {
                    localStorage.setItem("UPDATE_DESIGN_OID", 1);
                    console.log(mess.data);
                  });
                }
              }
            );
          } else {
            localStorage.removeItem("UPDATE_DESIGN_OID");
            this.$message.error("设计表单保存失败");
          }
        }).catch(() => {
          localStorage.removeItem("UPDATE_DESIGN_OID");
          this.$message.error("更新表单设计Oid失败");
        });
      }
    }
  }
};
</script>
