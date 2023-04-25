<template>
  <el-tabs v-model="activeName" @tab-click="handleClick" style="overflow: hidden;">
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
              <td colspan="3">{{form.serviceInfo.sxService.serviceName}}</td>
              <td><b>事项类型：</b></td>
              <td>{{form.serviceInfo.sxService.serviceTypeName}}</td>
            </tr>
            <tr>
              <td><b>实施机构：</b></td>
              <td>{{form.serviceInfo.sxService.organName}}</td>
              <td><b>承诺时限：</b></td>
              <td>{{form.serviceInfo.sxServiceExtend.promiseLimit}}<span v-if="form.serviceInfo.sxServiceExtend.promiseLimitType=='W'">工作日</span><span
                  v-if="form.serviceInfo.sxServiceExtend.promiseLimitType=='N'">自然日</span><span v-if="form.serviceInfo.sxServiceExtend.promiseLimitType=='H'">小时</span></td>
              <td><b>法定时限：</b></td>
              <td>{{form.serviceInfo.sxServiceExtend.legalLimit}}<span v-if="form.serviceInfo.sxServiceExtend.legalLimitType=='W'">工作日</span><span
                  v-if="form.serviceInfo.sxServiceExtend.legalLimitType=='N'">自然日</span><span v-if="form.serviceInfo.sxServiceExtend.legalLimitType=='H'">小时</span></td>
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
              <td colspan="3">{{form.info.projectName}}</td>
              <td><b>申请数量：</b></td>
              <td>{{form.applyPerson.applyNumber}}</td>
            </tr>
            <!--<tr>
              <td><b>业务管辖地：</b></td><td colspan="3">{{form.applyPerson.bussVenueDistrictOid}}</td>

            </tr>-->
            <tr>
              <td><b>受理具体地点：</b></td>
              <td colspan="5">{{form.applyPerson.specificLocation}}</td>

            </tr>
            <tr>
              <td v-if="form.applyPerson.applyUserType=='0'" ><b>申请人姓名：</b></td>
              <td v-else><b>申请单位名称：</b></td>
              <td>{{form.applyPerson.applyUserName}}</td>
              <td><b>证件类型：</b></td>
              <td>{{form.applyPerson.credentialType}}</td>
              <td><b>证件号：</b></td>
              <td>{{form.applyPerson.credentialNumber}}</td>

            </tr>
            <tr>
              <td v-if="form.applyPerson.applyUserType=='0'"><b>申请人手机：</b></td>
              <td v-else><b>申请单位手机：</b></td>
              <td>{{form.applyPerson.applyUserPhone}}</td>
              <td v-if="form.applyPerson.applyUserType=='0'"><b>申请人电话：</b></td>
              <td v-else><b>申请单位电话：</b></td>
              <td>{{form.applyPerson.applyUserTel}}</td>
              <td><b>邮政编码：</b></td>
              <td>{{form.applyPerson.applyPostCode}}</td>
            </tr>
            <tr>
              <td><b>通讯地址：</b></td>
              <td colspan="5">{{form.applyPerson.applyUserAddress}}</td>
            </tr>
            <tr v-if="form.applyPerson.legalPersonName">
              <td><b>法定代表人：</b></td>
              <td colspan="5">{{form.applyPerson.legalPersonName}}</td>
            </tr>
            <tr>
              <td><b>投资项目名称：</b></td>
              <td colspan="3">{{form.caseExt.investProjecName}}</td>
              <td><b>投资项目编号：</b></td>
              <td>{{form.caseExt.investProjectCode}}</td>
            </tr>
            <tr>
              <td><b>摘要内容：</b></td>
              <td colspan="5">{{form.caseExt.projectAbstract}}</td>
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
              <td colspan="3">{{form.applyPerson.contactUserName}}</td>
              <td><b>身份证号码：</b></td>
              <td>{{form.applyPerson.contactCredentialNumber}}</td>
            </tr>
            <tr>
              <td><b>联系人手机：</b></td>
              <td>{{form.applyPerson.contactUserPhone}}</td>
              <td><b>联系人电话：</b></td>
              <td>{{form.applyPerson.contactUserTel}}</td>
              <td><b>联系人邮件：</b></td>
              <td>{{form.applyPerson.contactEmail}}</td>
            </tr>
            <tr>
              <td><b>联系人备注：</b></td>
              <td colspan="5">{{form.applyPerson.contactRemark}}</td>
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
                <span v-if="form.caseExt.resultDeliveryWay==1"> 快递送达</span>
                <span v-if="form.caseExt.resultDeliveryWay==2">自行取件</span>
                <span v-if="form.caseExt.resultDeliveryWay==3">其他</span>
              </td>
            </tr>
            <tr v-if="form.caseExt.resultDeliveryWay==1">
              <td><b>收件人姓名：</b></td>
              <td>{{form.applyPerson.addresseeName}}</td>
              <td><b>收件人邮编：</b></td>
              <td>{{form.applyPerson.addresseePostCode}}</td>
              <td><b>收件人手机：</b></td>
              <td>{{form.applyPerson.addresseePhone}}</td>
            </tr>
            <tr v-if="form.caseExt.resultDeliveryWay==1">
              <td><b>收件人电话：</b></td>
              <td>{{form.applyPerson.addresseeTel}}</td>
              <td><b>收件人地址：</b></td>
              <td colspan="3">{{form.applyPerson.addresseeAddress}}</td>
            </tr>
            <tr v-if="form.caseExt.resultDeliveryWay==1">
              <td><b>收件人详细地址：</b></td>
              <td colspan="5">{{form.applyPerson.addresseeDetailAddress}}</td>
            </tr>
          </table>
    </el-tab-pane>
    <el-tab-pane label="办理环节" name="second">
      <template v-for="info in form.links">
        <div class="zf-zc-table--title">{{info.linkName}}环节意见列表</div>
          <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
            <colgroup>
              <col width="15%" />
              <col width="35%" />
              <col width="15%" />
              <col width="35%" />
            </colgroup>
            <tr>
              <td><b>{{info.linkName}}人：</b></td>
              <td colspan="3">{{info.personName}}</td>
            </tr>
            <tr>
              <td><b>{{info.linkName}}时间：</b></td>
              <td>{{info.finalDate}}</td>
              <td><b>{{info.linkName}}状态：</b></td>
              <td><span v-if="info.finalStatus==1">受理通过</span>
                <span v-if="info.finalStatus==2">不予受理</span>
                <span v-if="info.finalStatus==10">承办</span>
                <span v-if="info.finalStatus==20">审查通过</span>
                <span v-if="info.finalStatus==21">退回</span>
                <span v-if="info.finalStatus==30">批准</span>
                <span v-if="info.finalStatus==20">通过</span>
                <span v-if="info.finalStatus==21">退回</span>
                <span v-if="info.finalStatus==31">同意</span>
                <span v-if="info.finalStatus==32">准予许可</span>
                <span v-if="info.finalStatus==33">不予许可</span>
                <span v-if="info.finalStatus==40">出证办结</span>
                <span v-if="info.finalStatus==41">不出证办结</span>
                <span v-if="info.finalStatus==42">转报</span>
                <span v-if="info.finalStatus==43">批准</span>
                <span v-if="info.finalStatus==44">不予批准</span>
                <span v-if="info.finalStatus==45">作废办件</span>
                <span v-if="info.finalStatus==46">终止办件</span>
                <span v-if="info.finalStatus==47">不予批准</span>
                <span v-if="info.finalStatus==47">预审不通过</span>
                <span v-if="info.finalStatus==0">不予受理</span>
              </td>
            </tr>
            <tr v-if="info.finalStatus!=45 && info.finalStatus!=1 &&info.finalStatus!=2">
              <td><b>{{info.linkName}}意见：</b></td>
              <td colspan="3">{{info.finalOpinion}}</td>
            </tr>
            <tr>
              <td><b>意见说明：</b></td>
              <td colspan="3">{{info.finalOpinionDesc}}</td>
            </tr>
            <tr v-if="info.linkCode !='SL'">
              <td><b>附件：</b></td>
              <td colspan="3">
                <div v-for="item in sysAttaAttas">
                  {{item.originName}} <a @click="downLoad(item)"><span style="color: #0b76c7">查看附件</span></a>
                </div>
              </td>
            </tr>
          </table>
      </template>
    </el-tab-pane>
    <el-tab-pane label="补齐补正" name="third" v-if="form.bqbzInfo">
      <!--补齐补正信息-->
        <template v-for=" data in form.bqbzInfo">
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
              <td><b>补正到期时间：</b></td>
              <td>{{data.dueDate }}</td>
              <td><b>补正状态：</b></td>
              <td><span v-if="data.correctionStatus==0">待补正</span>
                <span v-if="data.correctionStatus==1">已补正受理</span>
                <span v-if="data.correctionStatus==2">补正过期未受理</span>
              </td>
              <td><b>补正类型：</b></td>
              <td><span v-if="data.bzType==1">补齐补正</span>
                <span v-if="data.bzType==0">告知补正</span>
              </td>
            </tr>
            <tr>
              <td><b>补正原因：</b></td>
              <td colspan="5">
                {{data.correctionReason }}
              </td>
            </tr>
            <tr>
              <td><b>办理时间：</b></td>
              <td colspan="5">
                {{data.patchTime }}
              </td>
            </tr>
            <tr>
              <td><b>办理意见：</b></td>
              <td colspan="5">
                {{data.patchOpinion }}
              </td>
            </tr>
            <tr>
              <td><b>备注：</b></td>
              <td colspan="5">
                {{data.memo }}
              </td>
            </tr>
          </table>
        </template>
    </el-tab-pane>
    <el-tab-pane label="证照签收" name="fourth" v-if="licenseQsInfo && licenseQsInfo.sendWay">
      <!--证照签收信息-->
          <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
            <colgroup>
              <col width="18%" />
              <col width="32%" />
              <col width="18%" />
              <col width="32%" />
            </colgroup>
            <tr>
              <td><b>送达方式：</b></td>
              <td><span v-if="licenseQsInfo.sendWay==1">快递送达</span>
                <span v-if="licenseQsInfo.sendWay==2">人工送达窗口</span>
              </td>
              <td><b>证件类型：</b></td>
              <td><span v-if="licenseQsInfo.carType==1">证照</span>
                <span v-if="licenseQsInfo.carType==2">批文</span>
                <span v-if="licenseQsInfo.carType==3">其他</span>
              </td>
            </tr>
            <tr v-if="licenseQsInfo.sendWay == 1">
              <td><b>快递目标：</b></td>
              <td colspan="3"><span v-if="licenseQsInfo.deliverTarget==1">窗口</span>
                <span v-if="licenseQsInfo.deliverTarget==2">申请人</span>
              </td>
            </tr>
            <tr v-if="licenseQsInfo.sendWay == 1">
              <td><b>快递公司：</b></td>
              <td>{{licenseQsInfo.deliverCompany}}</td>
              <td><b>快递单号：</b></td>
              <td>{{licenseQsInfo.deliverCompany}}</td>
            </tr>
            <tr>
              <td><b>送件人姓名：</b></td>
              <td>{{licenseQsInfo.sendPerson}}</td>
              <td><b>送件人手机号：</b></td>
              <td>{{licenseQsInfo.sendPhone}}</td>
            </tr>
            <tr>
              <td><b>送件部门：</b></td>
              <td>{{licenseQsInfo.organName}}</td>
              <td><b>送件时间：</b></td>
              <td>{{licenseQsInfo.sendTime}}</td>
            </tr>
            <tr>
              <td><b>签收状态：</b></td>
              <td><span v-if="licenseQsInfo.licenseInStorage==1">已签收</span>
                <span v-else>待签收</span>
              </td>
              <td><b>签收时间：</b></td>
              <td>{{licenseQsInfo.dateOfIssue}}
              </td>
            </tr>
            <tr>
              <td><b>是否需要短信通知：</b></td>
              <td colspan="3"><span v-if="licenseQsInfo.isCms==0">否</span>
                <span v-if="licenseQsInfo.isCms==1">是</span>
              </td>
            </tr>
          </table>
    </el-tab-pane>
    <el-tab-pane label="证照签发" name="five" v-if="licenseQfInfo && licenseQfInfo.oid">
      <!--证照签收信息-->
          <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
            <colgroup>
              <col width="18%" />
              <col width="32%" />
              <col width="18%" />
              <col width="32%" />
            </colgroup>
            <tr>
              <td><b>领证人姓名：</b></td>
              <td colspan="2">{{licenseQfInfo.receiveName}}
              </td>
              <td rowspan="5">
                <img :src="verifyImg"  class="user-avatar">
              </td>
            </tr>
            <tr>
              <td><b>领证人证件号码：</b></td>
              <td colspan="2">
                {{licenseQfInfo.receiveCardCode}}
              </td>
            </tr>
            <tr>
              <td><b>联系电话：</b></td>
              <td colspan="2">
                {{licenseQfInfo.receivePhone}}
              </td>
            </tr>
            <tr>
              <td><b>采集时间：</b></td>
              <td colspan="2">
                {{licenseQfInfo.createDate}}
              </td>
            </tr>
            <tr>
              <td><b>影像采集确认信息：</b></td>
              <td colspan="2">
                {{licenseQfInfo.addresseeAddress}}
              </td>
            </tr>
          </table>
    </el-tab-pane>
    <!-- <div class="btn-wrap">
      <div class="btn-list mt10">
        <el-button style="margin-left: 90%;" @click="viewDialog()">关闭</el-button>
      </div>
    </div> -->
    <!-- 关联办件详细信息 -->
    <el-dialog v-dialog-drag :visible.sync="attaView" v-if="attaView" @close="closeAttaView" title="附件查看" width="70%" append-to-body>
      <el-table :data="attaList" border>
        <el-table-column label="序号" align="center" min-width="5%">
          <template slot-scope="scope">
            {{ scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column label="材料名称" align="center" min-width="10%" prop="name" />
        <el-table-column label="原始名称" align="center" min-width="30%" prop="originName"/>
        <el-table-column label="操作" align="center" min-width="10%">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="iconfont zfsoft-chakan"  @click="viewFile(scope.row.attaOid)">查看</el-button>
            <el-button size="mini" type="text" icon="iconfont zfsoft-xiazai"  @click="downLoadFile(scope.row.attaOid)">下载</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <!--引入办件文件的预览弹出层-->
    <el-dialog v-dialog-drag title="文件预览" :visible.sync="showFile" v-if="showFile" @close="closeCaseFileView" width="60%"
               append-to-body>
      <case-file-view :attaOid="fileAttaOid" @father-click="closeCaseFileView"></case-file-view>
    </el-dialog>
  </el-tabs>
</template>
<script>
  import {
    getOneCase,
    getOneApplyPerson,
    getOneMaterialInfo,
    getOneLinkInfo,
    getQlCaseExt,
    getServiceInfo,
    getOneDict,getQlCorrectByCaseOid,getOneByCaseNumber,getZzqsByCaseOid,getZzqfByCaseOid,fileDownLoad
  } from "@/api/zc/businessManagement/viewCaseInfo.js";
  import {
    getCaseTitleValueList
  } from "@/api/zc/businessManagement/temporaryAcceptance";
  import {
    getSituationMaterialListByOids
  } from "@/api/zc/businessManagement/windowAcceptance";
  import {downFileByoid} from "@/api/zc/businessManagement/fileUpload";
  import caseFileView from "@/views/zc/businessManagement/caseBqbz/caseFileView";
  import {getElecLicenUrl} from "@/api/zc/businessManagement/elemLice";
  import {
    CodeToText
  } from 'element-china-area-data'
  import {getSysAttaByAttaOid} from "@/api/sys/atta";
  export default {
    name: 'viewCaseInfo',
    props: ['caseNumber'],
    components: {caseFileView},
    data() {
      return {
        // 遮罩层
        loading: true,
        activeName: 'first',
        caseNumberIndex: this.caseNumber,
        caseOid: "",
        titleValueList: [],
        checkList: [],
        situationName: '默认自定情形',
        verifyImg:"",
        sjType: "",
        //事项材料
        sxServiceMaterialList: [],
        materialOutInfo:[],
        licenseQsInfo:{},
        licenseQfInfo:{},
        attaList:[],
        sysAttaAttas:[],
        // 表单参数
        form: {
          info: {},
          caseExt: {},
          applyPerson: {},
          serviceInfo: [],
          material: [],
          links: [],
          bqbzInfo:[],
        },
        attaView:false,
        showFile:false,
        fileAttaOid:"",

      };
    },

    created() {},

    //获取父页面的值
    mounted() {
      this.getOneCase();
    },
    methods: {
      handleClick(tab, event) {
        /*if(tab.name=='five'){
          this.getBqbzInfo();
        }
        if(tab.name=='six'){
          this.getMaterialOut();
        }*/
      },
      viewDialog(){
        this.$emit('view-case');
      },
      //查询办件信息
      getOneCase() {
        getOneCase(this.caseNumberIndex).then(response => {
          this.form.info = response.data;
          this.caseOid = response.data.caseOid;

          this.getMaterialInfo();
          this.getLinkInfo();
          this.getSdfs();
          this.getApplyInfo();
          //所属事项信息
          this.viewServiceInfo(response.data.serviceOid);
          this.getQlCaseSituationList();
          this.getBqbzInfo();
          this.getMaterialOut();
          this.getZzqsInfo();
          this.getZzqfInfo();
        })
      },
      /** 办件情形获取 */
      getQlCaseSituationList() {
        let _that = this;
        //查询办件情形情形
        getCaseTitleValueList(_that.caseOid).then(response => {
          //填充标题选项
          this.titleValueList = response.data;
          if (response.data.length > 0) {
            response.data.forEach(item => {
              if (item.situationName != "") {
                this.situationName = item.situationName;
              }
              _that.checkList.push(item.valueOid)
            })
            _that.getMaterialList(this.form.info.serviceOid);
          }
        });
      },
      /** 点击选项值 选项有精细化材料展示精细化材料 */
      getMaterialList(serviceOid) {
        let _that = this;
        //根据被选中的选项获取关联材料
        getSituationMaterialListByOids(serviceOid, _that.checkList).then(response => {
          if (response.data.length > 0) {
            this.sxServiceMaterialList = response.data;
          }
        });
      },
      //获取证件类型
      viewServiceInfo(serviceOid) {
        getServiceInfo(serviceOid).then(response => {
          if(response.data !=null){
            this.form.serviceInfo = response.data;
          }
        })
      },
      getApplyInfo() {
        //查询申请人信息
        getOneApplyPerson(this.caseOid).then(response => {
          this.form.applyPerson = response.data;
          this.getCredential(response.data.credentialType)
          if (this.form.applyPerson.addresseeAddress !=null) {
            this.handleChange();
          }
        })
      },
      // 编辑格式化地址
      handleChange() {
        let str=this.form.applyPerson.addresseeAddress;
        str=str.substring(1,str.length-1);
        let self = str.toString().replace(/"/g,'');
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
      //获取证件类型
      getCredential(Type) {
        getOneDict(Type).then(response => {
          this.form.applyPerson.credentialType = response.data.name;
        })
      },
      getMaterialInfo() {
        //查询材料信息
        getOneMaterialInfo(this.caseOid).then(response => {
          this.form.material = response.data;
        })
      },
      getLinkInfo() {
        //查询环节
        getOneLinkInfo(this.caseOid).then(response => {
          this.form.links = response.data;
          if(this.form.links !=""){
            this.form.links.forEach(link => {
              if(link.attaOid !=null && link.attaOid != ""){
                console.log(link.attaOid);
               let index = link.attaOid.indexOf(",") ;
               if(index > 0){
                let oids =  link.attaOid.split(",");
                 oids.forEach(oid =>{
                   getSysAttaByAttaOid(oid).then(resp => {
                     this.sysAttaAttas.push(resp.data);
                   })
                 })
               }else{
                 getSysAttaByAttaOid(link.attaOid).then(resp => {
                   this.sysAttaAttas.push(resp.data);
                 })
               }
              }
            })
          }
        })
      },
      getSdfs() {
        //送达方式
        getQlCaseExt(this.caseOid).then(response => {
          this.form.caseExt = response.data;
          this.sjType = this.form.caseExt.resultDeliveryWay;
        })
      },
      getBqbzInfo() {
        //补齐补正信息
        getQlCorrectByCaseOid(this.caseOid).then(response => {
          this.form.bqbzInfo = response.data;
        })
      },
      //材料出库
      getMaterialOut(){
        getOneByCaseNumber(this.caseNumberIndex).then(response => {
          if(response.data){
           this.materialOutInfo=response.data;
          }
        });
      },
      //证照签收
      getZzqsInfo(){
        getZzqsByCaseOid(this.caseOid).then(response => {
          if(response.data){
            this.licenseQsInfo=response.data;
          }
        });
      },
      //证照签发
      getZzqfInfo(){
        getZzqfByCaseOid(this.caseOid).then(response => {
          if(response.data){
            this.licenseQfInfo=response.data;
            if(response.data.photoOid){
              //图片预加载
              downFileByoid(response.data.photoOid).then(response => {
                this.verifyImg = response.data+"?attaOid="+this.licenseQfInfo.photoOid;
              });
            }
          }
        });
      },
      //预览附件
      viewFile(attaOid) {
        this.fileAttaOid = attaOid;
        this.showFile = true;
      },
      handleViewAtta(row){
        this.attaList=row.attaList;
        this.attaView=true;
      },
      closeAttaView(){
        this.attaList=[];
        this.attaView=false;
      },
      //关闭预览附件
      closeCaseFileView() {
        this.showFile = false;
      },
      //电子证照预览
      viewElemsInfo(eleLincenseOid) {
        getElecLicenUrl(eleLincenseOid).then(response => {
          let urlElem = [];
          if (response.data) {
            urlElem = response.data;
            if (urlElem[0].viewOfdUrl) {
              window.open(urlElem[0].viewOfdUrl, 'width=1200px;height=800px;');
            } else {
              this.$message.error("暂时无法查看证照信息！");
            }
          }
        });
      },
      downLoadFile(attaOid){
        this.loading = true;
        fileDownLoad(attaOid).then(response => {
          window.location.href = response.data;
        });
        this.loading = false;
      },
      downLoad(atta){
        window.open(atta.fastdfsNginxUrl);
      },
    }
  };
</script>
<style lang="scss" scoped>
  .el-scrollbar__wrap {
    overflow: hidden
  }

  .dialog-table {
    padding: 5px;
  }
</style>
