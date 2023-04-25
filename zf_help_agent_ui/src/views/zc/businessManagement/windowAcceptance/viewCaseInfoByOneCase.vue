<template>
  <el-tabs
    v-model="activeName"
    @tab-click="handleClick"
    style="overflow: hidden"
  >
    <el-tab-pane label="基本信息" name="first">
      <!--事项信息-->
      <div class="zf-zc-table--title">事项相关信息</div>
      <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
        <colgroup>
          <col width="12%" />
          <col width="21%" />
          <col width="12%" />
          <col width="21%" />
          <col width="12%" />
          <col width="22%" />
        </colgroup>
        <tr>
          <td><b>事项名称：</b></td>
          <td colspan="3">{{ form.serviceInfo.sxService.serviceName }}</td>
          <td><b>事项类型：</b></td>
          <td>{{ form.serviceInfo.sxService.serviceTypeName }}</td>
        </tr>
        <tr>
          <td><b>实施机构：</b></td>
          <td>{{ form.serviceInfo.sxService.organName }}</td>
          <td><b>承诺时限：</b></td>
          <td>
            {{ form.serviceInfo.sxServiceExtend.promiseLimit
            }}<span
              v-if="form.serviceInfo.sxServiceExtend.promiseLimitType == 'W'"
              >工作日</span
            ><span
              v-if="form.serviceInfo.sxServiceExtend.promiseLimitType == 'N'"
              >自然日</span
            ><span
              v-if="form.serviceInfo.sxServiceExtend.promiseLimitType == 'H'"
              >小时</span
            >
          </td>
          <td><b>法定时限：</b></td>
          <td>
            {{ form.serviceInfo.sxServiceExtend.legalLimit
            }}<span
              v-if="form.serviceInfo.sxServiceExtend.legalLimitType == 'W'"
              >工作日</span
            ><span v-if="form.serviceInfo.sxServiceExtend.legalLimitType == 'N'"
              >自然日</span
            ><span v-if="form.serviceInfo.sxServiceExtend.legalLimitType == 'H'"
              >小时</span
            >
          </td>
        </tr>
      </table>
      <!--申请人信息-->
      <div class="zf-zc-table--title">申请人/申请单位相关信息</div>
      <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
        <colgroup>
          <col width="12%" />
          <col width="21%" />
          <col width="12%" />
          <col width="21%" />
          <col width="12%" />
          <col width="22%" />
        </colgroup>
        <tr>
          <td><b>申请项目名称：</b></td>
          <td colspan="3">{{ form.info.projectName }}</td>
          <td><b>申请数量：</b></td>
          <td>{{ form.applyPerson.applyNumber }}</td>
        </tr>
        <!--<tr>
              <td><b>业务管辖地：</b></td><td colspan="3">{{form.applyPerson.bussVenueDistrictOid}}</td>

            </tr>-->
        <tr>
          <td><b>受理具体地点：</b></td>
          <td colspan="5">{{ form.applyPerson.specificLocation }}</td>
        </tr>
        <tr>
          <td v-if="form.applyPerson.applyUserType == '0'">
            <b>申请人姓名：</b>
          </td>
          <td v-else><b>申请单位名称：</b></td>
          <td>{{ form.applyPerson.applyUserName }}</td>
          <td><b>证件类型：</b></td>
          <td>{{ form.applyPerson.credentialType }}</td>
          <td><b>证件号：</b></td>
          <td>{{ form.applyPerson.credentialNumber }}</td>
        </tr>
        <tr>
          <td v-if="form.applyPerson.applyUserType == '0'">
            <b>申请人手机：</b>
          </td>
          <td v-else><b>申请单位手机：</b></td>
          <td>{{ form.applyPerson.applyUserPhone }}</td>
          <td v-if="form.applyPerson.applyUserType == '0'">
            <b>申请人电话：</b>
          </td>
          <td v-else><b>申请单位电话：</b></td>
          <td>{{ form.applyPerson.applyUserTel }}</td>
          <td><b>邮政编码：</b></td>
          <td>{{ form.applyPerson.applyPostCode }}</td>
        </tr>
        <tr>
          <td><b>通讯地址：</b></td>
          <td colspan="5">{{ form.applyPerson.applyUserAddress }}</td>
        </tr>
        <tr v-if="form.applyPerson.legalPersonName">
          <td><b>法定代表人：</b></td>
          <td colspan="5">{{ form.applyPerson.legalPersonName }}</td>
        </tr>
        <tr>
          <td><b>投资项目名称：</b></td>
          <td colspan="3">{{ form.caseExt.investProjecName }}</td>
          <td><b>投资项目编号：</b></td>
          <td>{{ form.caseExt.investProjectCode }}</td>
        </tr>
        <tr>
          <td><b>摘要内容：</b></td>
          <td colspan="5">{{ form.caseExt.projectAbstract }}</td>
        </tr>
      </table>
      <!--联系人信息-->
      <template v-if="form.applyPerson.contactUserName">
        <div class="zf-zc-table--title">联系人相关信息</div>
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="12%" />
            <col width="21%" />
            <col width="12%" />
            <col width="21%" />
            <col width="12%" />
            <col width="22%" />
          </colgroup>
          <tr>
            <td><b>联系人名称：</b></td>
            <td colspan="3">{{ form.applyPerson.contactUserName }}</td>
            <td><b>身份证号码：</b></td>
            <td>{{ form.applyPerson.contactCredentialNumber }}</td>
          </tr>
          <tr>
            <td><b>联系人手机：</b></td>
            <td>{{ form.applyPerson.contactUserPhone }}</td>
            <td><b>联系人电话：</b></td>
            <td>{{ form.applyPerson.contactUserTel }}</td>
            <td><b>联系人邮件：</b></td>
            <td>{{ form.applyPerson.contactEmail }}</td>
          </tr>
          <tr>
            <td><b>联系人备注：</b></td>
            <td colspan="5">{{ form.applyPerson.contactRemark }}</td>
          </tr>
        </table>
      </template>
      <!--收件相关信息-->
      <div class="zf-zc-table--title">收件相关信息</div>
      <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
        <colgroup>
          <col width="12%" />
          <col width="21%" />
          <col width="12%" />
          <col width="21%" />
          <col width="12%" />
          <col width="22%" />
        </colgroup>
        <tr>
          <td><b>送达方式：</b></td>
          <td colspan="5">
            <span v-if="form.caseExt.resultDeliveryWay == 1"> 快递送达</span>
            <span v-if="form.caseExt.resultDeliveryWay == 2">自行取件</span>
            <span v-if="form.caseExt.resultDeliveryWay == 3">其他</span>
          </td>
        </tr>
        <tr v-if="form.caseExt.resultDeliveryWay == 1">
          <td><b>收件人姓名：</b></td>
          <td>{{ form.applyPerson.addresseeName }}</td>
          <td><b>收件人邮编：</b></td>
          <td>{{ form.applyPerson.addresseePostCode }}</td>
          <td><b>收件人手机：</b></td>
          <td>{{ form.applyPerson.addresseePhone }}</td>
        </tr>
        <tr v-if="form.caseExt.resultDeliveryWay == 1">
          <td><b>收件人电话：</b></td>
          <td>{{ form.applyPerson.addresseeTel }}</td>
          <td><b>收件人地址：</b></td>
          <td colspan="3">{{ form.applyPerson.addresseeAddress }}</td>
        </tr>
        <tr v-if="form.caseExt.resultDeliveryWay == 1">
          <td><b>收件人详细地址：</b></td>
          <td colspan="5">{{ form.applyPerson.addresseeDetailAddress }}</td>
        </tr>
      </table>
    </el-tab-pane>
    <el-tab-pane label="办理环节" name="fourth">
      <div v-for="info in form.links">
        <div class="zf-zc-table--title">{{ info.linkName }}环节意见列表</div>
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="15%" />
            <col width="35%" />
            <col width="15%" />
            <col width="35%" />
          </colgroup>
          <tr>
            <td>
              <b>{{ info.linkName }}人：</b>
            </td>
            <td colspan="3">{{ info.personName }}</td>
          </tr>
          <tr>
            <td>
              <b>{{ info.linkName }}时间：</b>
            </td>
            <td>{{ info.finalDate }}</td>
            <td>
              <b>{{ info.linkName }}状态：</b>
            </td>
            <td>
              <span v-if="info.finalStatus == 1">受理通过</span>
              <span v-if="info.finalStatus == 2">不予受理</span>
              <span v-if="info.finalStatus == 10">承办</span>
              <span v-if="info.finalStatus == 20">审查通过</span>
              <span v-if="info.finalStatus == 21">退回</span>
              <span v-if="info.finalStatus == 30">批准</span>
              <span v-if="info.finalStatus == 20">通过</span>
              <span v-if="info.finalStatus == 21">退回</span>
              <span v-if="info.finalStatus == 31">同意</span>
              <span v-if="info.finalStatus == 32">准予许可</span>
              <span v-if="info.finalStatus == 33">不予许可</span>
              <span v-if="info.finalStatus == 40">出证办结</span>
              <span v-if="info.finalStatus == 41">不出证办结</span>
              <span v-if="info.finalStatus == 42">转报</span>
              <span v-if="info.finalStatus == 43">批准</span>
              <span v-if="info.finalStatus == 44">不予批准</span>
              <span v-if="info.finalStatus == 45">作废办件</span>
              <span v-if="info.finalStatus == 46">终止办件</span>
              <span v-if="info.finalStatus == 47">不予批准</span>
              <span v-if="info.finalStatus == 47">预审不通过</span>
              <span v-if="info.finalStatus == 0">不予受理</span>
            </td>
          </tr>
          <tr
            v-if="
              info.finalStatus != 45 &&
              info.finalStatus != 1 &&
              info.finalStatus != 2
            "
          >
            <td>
              <b>{{ info.linkName }}意见：</b>
            </td>
            <td colspan="3">{{ info.finalOpinion }}</td>
          </tr>
          <tr>
            <td><b>意见说明：</b></td>
            <td colspan="3">{{ info.finalOpinionDesc }}</td>
          </tr>
          <tr v-if="info.attaOid">
            <td><b>结果附件：</b></td>
            <td colspan="3">
              <div v-for="item in sysAttaAttas">
                {{ item.originName }}
                  <el-link type="primary" @click="downLoadResult(item.attaOid)"
                  >附件下载</el-link
                > 
              </div>
              <!--                <el-button @click="downLoadResult(info.attaOid)" type="primary" size="small">附件下载</el-button>-->
            </td>
          </tr>
        </table>
      </div>
    </el-tab-pane>
  </el-tabs>
</template>
<script>
import {
  getOneCase,
  getOneApplyPerson,
  getServiceInfo,
  getOneLinkInfo,
  getOneDict,
  getQlCaseExt
} from "@/api/zc/businessManagement/viewCaseInfo.js";
import { CodeToText } from "element-china-area-data";
import { getSysAttaByAttaOid } from "@/api/sys/atta";
export default {
  name: 'viewCaseInfoByOneCase',
  props: ['caseNumber'],
  data () {
    return {
      // 遮罩层
      loading: true,
      activeName: 'first',
      caseNumberIndex: this.caseNumber,
      caseOid: "",
      sjType: "",
      sysAttaAttas: [],
      // 表单参数
      form: {
        info: {},
        caseExt: {},
        applyPerson: {},
        serviceInfo: {},
        links: [],
      },
    };
  },
  created () { },
  //获取父页面的值
  mounted () {
    this.getOneCase();
  },
  methods: {
    handleClick (tab, event) {
    },
    viewDialog () {
      this.$emit('view-case');
    },
    //查询办件信息
    getOneCase () {
      getOneCase(this.caseNumberIndex).then(response => {
        this.form.info = response.data;
        this.caseOid = response.data.caseOid;
        this.getLinkInfo();
        this.getSdfs();
        this.getApplyInfo();
        //所属事项信息
        this.viewServiceInfo(response.data.serviceOid);
      })
    },
    //获取证件类型
    viewServiceInfo (serviceOid) {
      getServiceInfo(serviceOid).then(response => {
        this.form.serviceInfo = response.data;
      })
    },
    downLoadResult (attaOids) {
      if (attaOids) {
        /* let attaOidArr=attaOids.split(",");
         attaOidArr.forEach(ite=>{
           this.download(ite);
         })*/
        this.download(attaOids);
      } else {
        this.$message.error("暂无附件!")
      }
    },
    getApplyInfo () {
      //查询申请人信息
      getOneApplyPerson(this.caseOid).then(response => {
        this.form.applyPerson = response.data;
        this.getCredential(response.data.credentialType)

        if (this.form.applyPerson.addresseeAddress != null) {
          this.handleChange();
        }
      })
    },
    // 编辑格式化地址
    handleChange () {
      let str = this.form.applyPerson.addresseeAddress;
      str = str.substring(1, str.length - 1);
      let self = str.toString().replace(/"/g, '');
      var selfArr = self.split(",");
      if (null != selfArr) {
        var provinceCode = '';
        var zhixia = '';
        var city = '';
        if (null != selfArr[0]) {
          provinceCode = CodeToText[selfArr[0]];
        }
        if (null != selfArr[1]) {
          zhixia = '/' + CodeToText[selfArr[1]];
        }
        if (null != selfArr[2]) {
          city = '/' + CodeToText[selfArr[2]];
        }
        this.form.applyPerson.addresseeAddress = provinceCode + zhixia + city;
      }
    },
    getSdfs () {
      //送达方式
      getQlCaseExt(this.caseOid).then(response => {
        this.form.caseExt = response.data;
        this.sjType = this.form.caseExt.resultDeliveryWay;
      })
    },
    //获取证件类型
    getCredential (Type) {
      getOneDict(Type).then(response => {
        this.form.applyPerson.credentialType = response.data.name;
      })
    },
    getLinkInfo () {
      //查询环节
      getOneLinkInfo(this.caseOid).then(response => {
        this.form.links = response.data;
        this.form.links.forEach(link => {
          if (link.attaOid != null && link.attaOid != "") {
            let index = link.attaOid.indexOf(",");
            if (index > 0) {
              let oids = link.attaOid.split(",");
              oids.forEach(oid => {
                getSysAttaByAttaOid(oid).then(resp => {
                  this.sysAttaAttas.push(resp.data);
                })
              })
            } else {
              getSysAttaByAttaOid(link.attaOid).then(resp => {
                this.sysAttaAttas.push(resp.data);
              })
            }
          }
        })
      })
    },
  }
};
</script>
<style lang="scss" scoped>
.el-scrollbar__wrap {
  overflow: hidden;
}

.dialog-table {
  padding: 5px;
}
</style>
