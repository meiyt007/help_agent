/**
* 验证规则配置
* @author: liangss
* @date: 2020-11-12
*/
<template>
  <div class="el-table__header-wrapper dialog-table require-table">
    <h3><i class="el-icon-document"></i>验证规则配置</h3>
    <div class="select-list">
      <span
        v-for="(item, index) in selectData"
        :key="item.index"
        :class="{ current: index == num }"
        @click="selectChange(index)"
      >{{ item.title }}</span
      >
    </div>
    <div class="require-box">
      <i class="el-icon-warning-outline"></i
      >{{this.explain}}
    </div>
    <table cellspacing="0" cellpadding="0" border="0">
      <colgroup>
        <col width="5%" />
        <col width="10%" />
        <col width="20%" />
        <col width="9%" />
        <col width="9%" />
        <col width="9%" />
        <col width="9%" />
        <col width="9%" />
        <col width="9%" />
      </colgroup>
      <tr>
        <th>序号</th>
        <th>材料名称</th>
        <th>目录子项名称（目标编码）</th>
        <th>不能为空</th>
        <th>必须为空</th>
        <th>文本验证</th>
        <th>文本包含</th>
        <th>数字范围</th>
        <th>日期比较</th>
        <th>手写验证</th>
      </tr>

      <template  v-for="(item,index) in serviceMaterList">
        <tr v-for="(materialCatalog,indexx) in item.materialCatalogList" >
            <td v-show="indexx==0" :rowspan="item.materialCatalogList.length">{{ index+1 }} </td>
            <td v-show="indexx==0" :rowspan="item.materialCatalogList.length"> {{ item.materialName }}</td>
          <td>
              <div class="add-box">
              <span>{{materialCatalog.catalogName}}
                 <template  v-if="materialCatalog.catalogCode!='' &&materialCatalog.catalogCode!=null">
                    ({{materialCatalog.catalogCode}})
                 </template>
              </span>
              <template v-if="materialCatalog.template !=null">
                <ul  v-for="(faModelTemplateBlock,indexmc) in materialCatalog.faModelTemplateBlockList">
                  <li @click="addYZ(index,indexx,indexmc,item.comboDirectoryOid,item.materialName,materialCatalog.catalogName,item.materialOid,materialCatalog.materialCatalogOid,faModelTemplateBlock.faModelTemplateBlockOid,faModelTemplateBlock.blockName,faModelTemplateBlock.blockCode )">
                <!--  <li @click="addYZ(index,indexx,indexmc,item.serviceOid,item.materialSampleName,materialCatalog.catalogName,item.materialOid,materialCatalog.materialCatalogOid,faModelTemplateBlock.faModelTemplateBlockOid,faModelTemplateBlock.blockName,faModelTemplateBlock.blockCode )">-->
                    <span>
                      {{faModelTemplateBlock.blockName }}
                     <template  v-if="faModelTemplateBlock.blockCode!='' &&faModelTemplateBlock.blockCode!=null">
                     ({{ faModelTemplateBlock.blockCode }})
                    </template>
                    </span>
                    <el-button
                      type="default"
                      icon="el-icon-circle-plus-outline"
                      class="btn-add"
                    ></el-button>
                  </li>
                </ul>
              </template>
            </div>
          </td>

          <td>
                  <template v-if="materialCatalog.template !=null">
                      <template  v-for="(rule,indexmc) in faModelRuleValidationList">
                        <template v-if="rule.validateType === VALIDATION_TYPE_NOTNULL && rule.delFlag===0 && rule.sxMaterialOid===item.materialOid  && rule.catalogOid===materialCatalog.materialCatalogOid ">
                        <div class="require-item"   @click="delYZ(indexmc )">
                          ● {{rule.templateMetadataName }}({{rule.templateMetadataCode}})
                          <el-button
                          type="default"
                          icon="el-icon-error"
                          class="btn-close"
                        ></el-button>
                        </div>
                        </template>
                      </template>

                    </template>
          </td>
          <td>
              <template v-if="materialCatalog.template !=null">
                <template  v-for="(rule,indexmc) in faModelRuleValidationList">
                  <template v-if="rule.validateType === VALIDATION_TYPE_NULL && rule.delFlag===0 && rule.sxMaterialOid===item.materialOid && rule.catalogOid===materialCatalog.materialCatalogOid ">
                    <div class="require-item"   @click="delYZ(indexmc )">
                      ● {{rule.templateMetadataName }}（{{rule.templateMetadataCode}}）
                      <el-button
                      type="default"
                      icon="el-icon-error"
                      class="btn-close"
                    ></el-button>
                    </div>
                  </template>
                </template>

              </template>
          </td>
          <td>
              <template v-if="materialCatalog.template !=null">
                <template  v-for="(rule,indexmc) in faModelRuleValidationList">
                  <template v-if="rule.validateType === VALIDATION_TYPE_CONTENT && rule.delFlag===0 && rule.sxMaterialOid===item.materialOid && rule.catalogOid===materialCatalog.materialCatalogOid">
                    <div class="require-item"   @click="delYZ(indexmc )">
                      ● {{rule.templateMetadataName }}({{rule.templateMetadataCode}})=={{rule.thanContent}}
                      <el-button
                      type="default"
                      icon="el-icon-error"
                      class="btn-close"
                    ></el-button>
                    </div>
                  </template>
                </template>

              </template>
          </td>
          <td>
              <template v-if="materialCatalog.template !=null">
                <template  v-for="(rule,indexmc) in faModelRuleValidationList">
                  <template v-if="rule.validateType === VALIDATION_TYPE_STR_CONTAIN && rule.delFlag===0 && rule.sxMaterialOid===item.materialOid && rule.catalogOid===materialCatalog.materialCatalogOid">
                    <div class="require-item"   @click="delYZ(indexmc )">
                      ● {{rule.templateMetadataName }}({{rule.templateMetadataCode}})=={{rule.thanContent}}
                      <el-button
                      type="default"
                      icon="el-icon-error"
                      class="btn-close"
                    ></el-button>
                    </div>
                  </template>
                </template>

              </template>
          </td>
          <td>
              <template v-if="materialCatalog.template !=null">
                <template  v-for="(rule,indexmc) in faModelRuleValidationList">
                  <template v-if="rule.validateType === VALIDATION_TYPE_RANGE && rule.delFlag===0 && rule.sxMaterialOid===item.materialOid && rule.catalogOid===materialCatalog.materialCatalogOid">
                    <div class="require-item"   @click="delYZ(indexmc )">
                      ● {{rule.templateMetadataName }}({{rule.templateMetadataCode}})
                      下限：{{rule.contentDown }};上限：{{rule.contentUp }}
                      <el-button
                      type="default"
                      icon="el-icon-error"
                      class="btn-close"
                    ></el-button>
                    </div>
                  </template>
                </template>

              </template>
          </td>
          <td>
              <template v-if="materialCatalog.template !=null">
                <template  v-for="(rule,indexmc) in faModelRuleValidationList">
                  <template v-if="rule.validateType === VALIDATION_TYPE_DATE_COMPARE && rule.delFlag===0 && rule.sxMaterialOid===item.materialOid && rule.catalogOid===materialCatalog.materialCatalogOid">
                    <div class="require-item"   @click="delYZ(indexmc )">
                      ● {{rule.templateMetadataName }}({{rule.templateMetadataCode}})
                      日期格式：{{rule.contentDown }};比对日期：{{rule.contentUp }}
                      <el-button
                      type="default"
                      icon="el-icon-error"
                      class="btn-close"
                    ></el-button>
                    </div>
                  </template>
                </template>
              </template>
          </td>
          <td>
              <template v-if="materialCatalog.template !=null">
                <template  v-for="(rule,indexmc) in faModelRuleValidationList">
                  <template v-if="rule.validateType === VALIDATION_TYPE_HAND_SIGN && rule.delFlag===0 && rule.sxMaterialOid===item.materialOid && rule.catalogOid===materialCatalog.materialCatalogOid">
                    <div class="require-item"   @click="delYZ(indexmc )">
                      ● {{rule.templateMetadataName }}({{rule.templateMetadataCode}})<el-button
                      type="default"
                      icon="el-icon-error"
                      class="btn-close"
                    ></el-button>
                    </div>
                  </template>
                </template>
              </template>
          </td>

        </tr>
      </template>
    </table>
    <div class="mt20 set-table">
      <h3><i class="el-icon-document"></i>关联验证规则</h3>
      <table cellspacing="0" cellpadding="0" border="0">
        <colgroup>
          <col width="12%" />
          <col width="12%" />
          <col width="12%" />
          <col width="12%" />
          <col width="12%" />
          <col width="12%" />
          <col width="12%" />
        </colgroup>
        <tr>
          <th>序号</th>
          <th>材料名称</th>
          <th>目录子项名称</th>
          <th>区块名称[编码]</th>
          <th>比对材料名称</th>
          <th>比对目录子项名称</th>
          <th>比对区块名称[编码]</th>
          <th>操作</th>
        </tr>
        <template  v-for="(rule,indexmc) in faModelRuleValidationList">
            <template v-if="(rule.validateType === VALIDATION_TYPE_CONTENT_THAN ||rule.validateType ===  VALIDATION_TYPE_SIMILARITY)&& rule.delFlag===0 ">
            <tr>

              <td>
                <template v-if="rule.validateType === VALIDATION_TYPE_CONTENT_THAN">
                关联对比
                </template>
                <template v-if="rule.validateType === VALIDATION_TYPE_SIMILARITY">
                  相识度(>={{rule.similar }}%)
                </template>
              </td>
              <td>{{rule.sxMaterialName }}</td>
              <td>{{rule.catalogName }}</td>
              <td>{{rule.templateMetadataName}}({{rule.templateMetadataCode}})</td>
              <td>{{rule.thanSxMaterialName }}</td>
              <td>{{rule.thanCatalogName }}</td>
              <td>{{rule.thanTemplateMetadataName}}({{rule.thanTemplateMetadataCode}})</td>
              <td>
                <el-button
                  type="default"
                  icon="el-icon-error"
                  class="btn-close btn-close-position"
                  @click="delYZ(indexmc )"
                ></el-button>
              </td>
            </tr>
          </template>
        </template>
      </table>
    </div>

<div style="vertical-align: middle;text-align: center">
  <el-button style="display: inline-block;" type="primary"  size="medium" @click="queding()" class="save-btn">确定</el-button>
  <el-button style="display: inline-block;"  size="medium" @click="guanbi()" class="close-btn">关闭</el-button>
</div>


   <!-- 文本验证-->
    <el-dialog :close-on-click-modal="false" :title="title" :visible.sync="openContent" v-if="openContent" width="900px" append-to-body>
      <el-form ref="form"  label-width="0px" class="dialog-table">
        <table cellspacing="0" cellpadding="0" border="0" class="el-table__body">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td>
              <b><i class="require">*</i>文本验证：</b>
            </td>
            <td  colspan="3">
              <el-form-item prop="validContent">
                <el-col :span="24">
                  <el-input v-model.trim="validContent" placeholder="请输入文本" maxlength="50" show-word-limit />
                </el-col>
              </el-form-item>
            </td>
          </tr>
        </table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="getContent">保存</el-button>
        <el-button @click="cancelgetContent">取 消</el-button>
      </div>
    </el-dialog>

   <!-- 文本包含-->
    <el-dialog :close-on-click-modal="false" :title="title" :visible.sync="openContain" v-if="openContain" width="900px" append-to-body>
      <el-form ref="form"  label-width="0px" class="dialog-table">
        <table cellspacing="0" cellpadding="0" border="0" class="el-table__body">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td>
              <b><i class="require">*</i>文本包含：</b>
            </td>
            <td  colspan="3">
              <el-form-item prop="validContain">
                <el-col :span="24">
                  <el-input v-model.trim="validContain" placeholder="请输入文本" maxlength="50" show-word-limit />
                </el-col>
              </el-form-item>
            </td>
          </tr>
        </table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="getContain">保存</el-button>
        <el-button @click="cancelgetContain">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 数字范围-->
    <el-dialog :close-on-click-modal="false" :title="title" :visible.sync="openRange" v-if="openRange" width="900px" append-to-body>
      <el-form ref="form"  label-width="0px" class="dialog-table">
        <table cellspacing="0" cellpadding="0" border="0" class="el-table__body">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td>
              <b><i class="require">*</i>下限：</b>
            </td>
            <td  colspan="3">
              <el-form-item prop="contentDownValid">
                <el-col :span="24">
                  <el-input v-model.trim="contentDownValid" placeholder="请输入下限" maxlength="50" show-word-limit />
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <b><i class="require">*</i>上限：</b>
            </td>
            <td  colspan="3">
              <el-form-item prop="contentUpValid">
                <el-col :span="24">
                  <el-input v-model.trim="contentUpValid" placeholder="请输入上限" maxlength="50" show-word-limit />
                </el-col>
              </el-form-item>
            </td>
          </tr>
        </table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="getRange">保存</el-button>
        <el-button @click="cancelRange">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 日期比较-->
    <el-dialog :close-on-click-modal="false" :title="title" :visible.sync="openDateCompare" v-if="openDateCompare" width="900px" append-to-body>
      <el-form ref="form"  label-width="0px" class="dialog-table">
        <table cellspacing="0" cellpadding="0" border="0" class="el-table__body">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td>
              <b><i class="require">*</i>日期格式：</b>
            </td>
            <td  colspan="3">
              <el-form-item prop="contentDateFormat">
                <el-col :span="24">
                  <el-input v-model.trim="contentDateFormat" placeholder="请输入日期格式" maxlength="50" show-word-limit />
                </el-col>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <b><i class="require">*</i>比较日期（当值为CURRENT_DATE时，与实时时间比对）：</b>
            </td>
            <td  colspan="3">
              <el-form-item prop="contentDateValue">
                <el-col :span="24">
                  <el-input v-model.trim="contentDateValue" placeholder="请输入比较日期" maxlength="50" show-word-limit />
                </el-col>
              </el-form-item>
            </td>
          </tr>
        </table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="getDateCompare">保存</el-button>
        <el-button @click="cancelDateCompare">取 消</el-button>
      </div>
    </el-dialog>

    <!--相识度-->
    <el-dialog :close-on-click-modal="false" :title="title" :visible.sync="openSimilarity" v-if="openSimilarity" width="900px" append-to-body>
      <el-form ref="form"  label-width="0px" class="dialog-table">
        <table cellspacing="0" cellpadding="0" border="0" class="el-table__body">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td>
              <b><i class="require">*</i>相识度：</b>
            </td>
            <td  colspan="3">
              <el-form-item prop="validSimilarity">
                <el-col :span="24">
                  <el-input v-model.trim="validSimilarity" placeholder="请输入相识度,相识度只能是两位正整数" maxlength="2" show-word-limit />
                </el-col>
              </el-form-item>
            </td>
          </tr>
        </table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="getContentSimilarity">保存</el-button>
        <el-button @click="cancelSimilarity">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>

import {initComboFaModelRule,initAll,saveOrUpdate} from "@/api/onething/clzs/modelManagement/comboFaModelRuleValidation";
export default {
 props: ['comboDirectoryOid'],
  data() {
    return {
      num: 0,
      selectData: [
        {
          index: "0",
          title: "不能为空",
        },
        {
          index: "1",
          title: "必须为空",
        },
        {
          index: "2",
          title: "文本验证",
        },
        {
          index: "3",
          title: "文本包含",
        },
        {
          index: "4",
          title: "数字范围",
        },
        {
          index: "5",
          title: "手写验证",
        },
        {
          index: "6",
          title: "日期比较",
        },
        {
          index: "7",
          title: "关联对比",
        },
        {
          index: "8",
          title: "相似度",
        },
      ],
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 弹出层标题
      title: "",
     //材料列表
      serviceMaterList:[],
      //验证规则列表
      faModelRuleValidationList:[],
      //验证信息
      faModelRuleValidation:{
      },
      VALIDATION_TYPE_NOTNULL:'',
      VALIDATION_TYPE_NULL:'',
      VALIDATION_TYPE_CONTENT:'',
      VALIDATION_TYPE_STR_CONTAIN:'',
      VALIDATION_TYPE_RANGE:'',
      VALIDATION_TYPE_DATE_COMPARE:'',
      VALIDATION_TYPE_HAND_SIGN:'',
      VALIDATION_TYPE_CONTENT_THAN:'',
      VALIDATION_TYPE_SIMILARITY:'',
      //说明
      explain:"请点击目录子项中的区块名称，选择不能为空的区块。",
      //验证类型
      validateType:'NOTNULL',
      //文本验证
      validContent:"",
      //文本包含
      validContain:"",
      //下限
      contentDownValid :"",
      //上限
      contentUpValid:"",
      //日期格式
      contentDateFormat :"",
      //日期
      contentDateValue:"",
      //相识度
      validSimilarity:"",
      // 查询参数
      queryParams: {
        comboDirectoryOid:this.comboDirectoryOid,
      },
      // 表单参数
      form: {
      },
      //打开文本验证
      openContent: false,
       //打开文本包含
      openContain: false,
      //数字范围
      openRange: false,
      //日期范围
      openDateCompare: false,
      //相识度
      openSimilarity: false,
      modelRuleManageOptionsnew:[],

      defaultProps: {
        children: "children",
        label: "label"
      },

    };
  },
  created() {
    //初始化数据
    this.initInfo();
    /** 初始化数据 */
    initAll().then(response => {
      this.VALIDATION_TYPE=response.data.VALIDATION_TYPE;
      this.VALIDATION_TYPE_NOTNULL=response.data.VALIDATION_TYPE_NOTNULL;
      this.VALIDATION_TYPE_NULL=response.data.VALIDATION_TYPE_NULL;
      this.VALIDATION_TYPE_CONTENT=response.data.VALIDATION_TYPE_CONTENT;
      this.VALIDATION_TYPE_STR_CONTAIN=response.data.VALIDATION_TYPE_STR_CONTAIN;
      this.VALIDATION_TYPE_RANGE=response.data.VALIDATION_TYPE_RANGE;
      this.VALIDATION_TYPE_DATE_COMPARE=response.data.VALIDATION_TYPE_DATE_COMPARE;
      this.VALIDATION_TYPE_HAND_SIGN=response.data.VALIDATION_TYPE_HAND_SIGN;
      this.VALIDATION_TYPE_CONTENT_THAN=response.data.VALIDATION_TYPE_CONTENT_THAN;
      this.VALIDATION_TYPE_SIMILARITY=response.data.VALIDATION_TYPE_SIMILARITY;
    });
  },
  methods: {
    queding(){
      this.loading = true;
     saveOrUpdate(this.faModelRuleValidationList).then(response => {
          if (response.code === 200) {
            this.msgSuccess("保存成功");
            this.loading = false;
            setTimeout(() => {
              this.$emit('father-click','N');//调用父页面关闭子页面的功能
            }, 10);

          }
        });
    },
    guanbi(){
      this.$emit('father-click','N');//调用父页面关闭子页面的功能
    },
    selectChange(index) {
      this.num = index;
      if(index==0){
        this.validateType=this.VALIDATION_TYPE_NOTNULL;
        this.explain="请点击目录子项中的区块名称，选择不能为空的区块。";
      }else if(index==1){
        this.validateType=this.VALIDATION_TYPE_NULL;
        this.explain="请点击目录子项中的区块名称，选择为空的区块。";
      }else if(index==2){
        this.openContent = true;
        this.validateType=this.VALIDATION_TYPE_CONTENT;
      }else if(index==3){
        this.openContain = true;
        this.validateType=this.VALIDATION_TYPE_STR_CONTAIN;
      }else if(index==4){
        this.openRange = true;
        this.validateType=this.VALIDATION_TYPE_RANGE;
      }else if(index==5){
        this.explain="请点击目录子项中的区块名称，选择手写签名的区块。";
        this.validateType=this.VALIDATION_TYPE_HAND_SIGN;
      }else if(index==6){
        this.openDateCompare = true;
        this.validateType=this.VALIDATION_TYPE_DATE_COMPARE;
      }else if(index==7){
        this.explain="请点击两个区块，完成比对验证规则的配置。";
        this.validateType=this.VALIDATION_TYPE_CONTENT_THAN;
      }else if(index==8){
        this.openSimilarity = true;
        this.validateType=this.VALIDATION_TYPE_SIMILARITY;
      }
    },
    cancelgetContent() {
      this.openContent = false;
      this.reset();
    },
    cancelgetContain() {
      this.openContain = false;
      this.reset();
    },
    cancelRange() {
      this.openRange = false;
      this.reset();
    },
    cancelDateCompare() {
      this.openDateCompare = false;
      this.reset();
    },
    cancelSimilarity() {
      this.openSimilarity = false;
      this.reset();
    },
    initInfo() {
      this.loading = true;
      initComboFaModelRule(this.queryParams).then(response => {
        this.serviceMaterList=response.data.serviceMaterList;
        this.faModelRuleValidationList=response.data.faModelRuleValidationList;
        this.loading = false;
      });
    },
    //添加验证
    addYZ(index,indexx,indexmc,comboDirectoryOid,materialSampleName,catalogName,materialOid,materialCatalogOid,faModelTemplateBlockOid,blockName,blockCode ){
      /*comboDirectoryOid,item.materialName*/
      let sfczFlag='N';
      let packageModelRuleValidation={}
      if(this.validateType!=''){
        //关联比对 相似度
        if(this.validateType==this.VALIDATION_TYPE_CONTENT_THAN || this.validateType==this.VALIDATION_TYPE_SIMILARITY){
          var json = JSON.stringify(this.faModelRuleValidation);
            if(json=='{}'){
              this.faModelRuleValidation.faModelRuleValidationOid='';
              /*this.faModelRuleValidation.serviceOid=serviceOid;*/
              this.faModelRuleValidation.comboDirectoryOid=comboDirectoryOid;
              this.faModelRuleValidation.validateType=this.validateType;
              this.faModelRuleValidation.sxMaterialOid=materialOid;
              this.faModelRuleValidation.catalogOid=materialCatalogOid;
              this.faModelRuleValidation.sxMaterialName=materialSampleName;
              this.faModelRuleValidation.catalogName=catalogName;
              this.faModelRuleValidation.templateMetadataOid=faModelTemplateBlockOid;
              this.faModelRuleValidation.templateMetadataName=blockName;
              this.faModelRuleValidation.templateMetadataCode=blockCode;
            }else{
              this.faModelRuleValidation.delFlag=0;
              if(this.validateType==this.VALIDATION_TYPE_SIMILARITY){//相识度
                this.faModelRuleValidation.similar=this.validSimilarity;
              }
              this.faModelRuleValidation.comboDirectoryOid=comboDirectoryOid;
              this.faModelRuleValidation.thanSxMaterialOid=materialOid;
              this.faModelRuleValidation.thanCatalogOid=materialCatalogOid;
              this.faModelRuleValidation.thanSxMaterialName=materialSampleName;
              this.faModelRuleValidation.thanCatalogName=catalogName;
              this.faModelRuleValidation.thanTemplateMetadataOid=faModelTemplateBlockOid;
              this.faModelRuleValidation.thanTemplateMetadataName=blockName;
              this.faModelRuleValidation.thanTemplateMetadataCode=blockCode;
              this.faModelRuleValidationList.push(this.faModelRuleValidation);
              this.num=111;
              this.explain="已选择验证类型：";
              this.validateType="";
              this.faModelRuleValidation={};
            }

        }else{
              packageModelRuleValidation.delFlag=0;
              packageModelRuleValidation.faModelRuleValidationOid='';
              packageModelRuleValidation.comboDirectoryOid=comboDirectoryOid;
              packageModelRuleValidation.validateType=this.validateType;
              packageModelRuleValidation.sxMaterialOid=materialOid;
              packageModelRuleValidation.catalogOid=materialCatalogOid;
              packageModelRuleValidation.sxMaterialName=materialSampleName;
              packageModelRuleValidation.catalogName=catalogName;
              packageModelRuleValidation.templateMetadataOid=faModelTemplateBlockOid;
              packageModelRuleValidation.templateMetadataName=blockName;
              packageModelRuleValidation.templateMetadataCode=blockCode;

              if(this.validateType==this.VALIDATION_TYPE_CONTENT){//文本验证
                packageModelRuleValidation.thanContent=this.validContent;
                this.num=111;
                this.explain="已选择验证类型：";
                this.validateType="";
              }else if(this.validateType==this.VALIDATION_TYPE_STR_CONTAIN){//文本包含
                packageModelRuleValidation.thanContent=this.validContain;
                this.num=111;
                this.explain="已选择验证类型：";
                this.validateType="";
              }else if(this.validateType==this.VALIDATION_TYPE_RANGE){//数字范围
                packageModelRuleValidation.contentDown=this.contentDownValid;
                packageModelRuleValidation.contentUp=this.contentUpValid;
                this.num=111;
                this.explain="已选择验证类型：";
                this.validateType="";
              }else if(this.validateType==this.VALIDATION_TYPE_DATE_COMPARE){//日期比较
                this.num=111;
                this.explain="已选择验证类型：";
                this.validateType="";
                packageModelRuleValidation.contentDateFormat=this.contentDateFormat;
                packageModelRuleValidation.contentDateValue=this.contentDateValue;
              }else if(this.validateType==this.VALIDATION_TYPE_NOTNULL){//不为空
                sfczFlag=this.ruleCheck(this.VALIDATION_TYPE_NOTNULL,faModelTemplateBlockOid,comboDirectoryOid,materialCatalogOid,materialOid);
              } else if(this.validateType==this.VALIDATION_TYPE_NULL){//为空
                sfczFlag=this.ruleCheck(this.VALIDATION_TYPE_NULL,faModelTemplateBlockOid,comboDirectoryOid,materialCatalogOid,materialOid);
              }
              if(sfczFlag!='Y'){
                this.faModelRuleValidationList.push(packageModelRuleValidation);
              }
        }

      }else {
        this.$alert("请先选择验证类型，再点击区块名称!");
      }
    },
    ruleCheck(ruleType, blockOid,comboDirectoryOid,materialCatalogOid,materialOid) {
      let flag='N';
      if(ruleType == this.VALIDATION_TYPE_NOTNULL) {
        for(var i = 0; i < this.faModelRuleValidationList.length; i++) {
          var ruleInfo = this.faModelRuleValidationList[i];
          if(ruleInfo.delFlag==0 &&  ruleInfo.templateMetadataOid == blockOid && ruleInfo.validateType == this.VALIDATION_TYPE_NULL  && ruleInfo.comboDirectoryOid == comboDirectoryOid  && ruleInfo.catalogOid == materialCatalogOid && ruleInfo.sxMaterialOid == materialOid  ) {
            this.$alert("当前区块已经配置【必须为空】规则，不能配置【不能为空】规则！");
            flag= 'Y';
          }else if(ruleInfo.delFlag==0 && ruleInfo.templateMetadataOid == blockOid && ruleInfo.validateType == this.VALIDATION_TYPE_NOTNULL  && ruleInfo.comboDirectoryOid == comboDirectoryOid  && ruleInfo.catalogOid == materialCatalogOid && ruleInfo.sxMaterialOid == materialOid ){
            this.$alert("当前区块已经配置【不能为空】配置，不能重复配置！");
            flag= 'Y';
          }
        }
      }
      if(ruleType == this.VALIDATION_TYPE_NULL) {	//当前配置的规则：必须为空，必须为空不能存在非空和文本验证的规则
        for(var i = 0; i < this.faModelRuleValidationList.length; i++) {
          var ruleInfo = this.faModelRuleValidationList[i];
          if(ruleInfo.delFlag==0 && ruleInfo.templateMetadataOid == blockOid && ( ruleInfo.catalogOid == materialCatalogOid && ruleInfo.sxMaterialOid == materialOid )  && (ruleInfo.validateType == this.VALIDATION_TYPE_NOTNULL || ruleInfo.validateType == this.VALIDATION_TYPE_CONTENT)) {
            this.$alert("当前区块已经配置【不能为空】或【文本验证】规则，不能配置【必须为空】规则！");
            flag= 'Y';
          }else if(ruleInfo.delFlag==0 && ruleInfo.templateMetadataOid == blockOid && ruleInfo.validateType == this.VALIDATION_TYPE_NULL &&( ruleInfo.catalogOid == materialCatalogOid && ruleInfo.sxMaterialOid == materialOid ) ){
            this.$alert("当前区块已经配置【必须为空】配置，不能重复配置！");
            flag= 'Y';
          }
        }
      }
      return flag;
    },
    //去除验证
    delYZ(indexmc ){
      this.faModelRuleValidation={};
      this.faModelRuleValidation=this.faModelRuleValidationList[indexmc];
      this.faModelRuleValidation.delFlag=1;
      this.faModelRuleValidationList.splice(indexmc,1);
      if(this.faModelRuleValidation.faModelRuleValidationOid!=''){
        this.faModelRuleValidationList.push(this.faModelRuleValidation);
      }
    },
    //获取文本验证
    getContent(){
      if(null==this.validContent||this.validContent==''){
        this.msgWarning("文字不能为空");
      }else{
        this.explain="输入比对文字后，选择需要进行文本验证的区块。比对文字："+this.validContent;
        this.openContent = false;
      }
    },
    //获取文本包含
    getContain(){
      if(null==this.validContain||this.validContain==''){
        this.msgWarning("文字不能为空");
      }else{
        this.explain="输入包含文字后，选择需要进行文本验证的区块。包含文字："+this.validContain;
        this.openContain = false;
      }
    },
    //获取数字范围
    getRange(){
      const re = /^[1-9]{1,}[\d]*$/;
      const contentDownCheck = re.test(this.contentDownValid);
      const contentUpCheck = re.test(this.contentDownValid);
      if((null==this.contentDownValid||this.contentDownValid=='')&&(null==this.contentUpValid||this.contentUpValid=='')){
        this.msgWarning("上限和下限不能同时为空");
      }else{
        if(!contentDownCheck){
          this.msgWarning("下限必须为数字");
        }else if(!contentUpCheck){
          this.msgWarning("上限必须为数字");
        }else{
          this.explain=" 输入上下限范围后，选择需要进行验证的区块。下限："+this.contentDownValid+";上限："+this.contentUpValid;
          this.openRange = false;
        }
      }
    },
    //获取日期比对
    getDateCompare(){
      if((null==this.contentDateFormat||this.contentDateFormat=='')||(null==this.contentDateValue||this.contentDateValue=='')){
        this.msgWarning("日期格式和比对日期不能为空");
      }else{
        this.explain=" 输入日期格式和比对日期后，选择需要进行验证的区块。日期格式："+this.contentDateValue+";比对日期："+this.contentDateValue;
        this.openDateCompare = false;
      }
    },
    //相识度
    getContentSimilarity(){
      const re = /^[1-9]{1,}[\d]*$/;
      const rsCheck = re.test(this.validSimilarity);
      if(null==this.validSimilarity||this.validSimilarity==''){
        this.msgWarning("相识度不能为空");
      }else{
        if(rsCheck){
          this.explain="请输入相似度后，点击两个区块，完成相似度规则的配置。相似度>="+this.validSimilarity+"%";
          this.openSimilarity = false;
        }else{
          this.msgWarning("相识度只能为正整数");
        }
      }
    },

  },
};
</script>

<style lang="scss" scoped>
.select-list span {
  display: inline-block;
  vertical-align: middle;
  color: #47657d;
  font-size: 14px;
  background-color: #f1f5f9;
  height: 40px;
  line-height: 40px;
  margin: 0px 10px 10px 0px;
  padding: 0px 35px 0px 20px;
  border-radius: 5px;
  cursor: pointer;
}
// .select-list span:first-child {
//   background-color: #fff6f1;
//   color: #2d506b;
// }
.select-list span.current {
  background-color: #4d87b5;
  color: #fff;
  position: relative;
}
.select-list span.current:after {
  content: "";
  position: absolute;
  width: 19px;
  height: 13px;
  right: 10px;
  top: 13px;
  background: url(../../../../assets/image/check-icon.png) no-repeat center;
  background-size: cover;
}
.require-box {
  font-size: 12px;
  color: #ff0000;
  margin: 0px 0px 10px 0px;
  height: 30px;
  line-height: 30px;
  text-align: center;
  background-color: #fff7f7;
}
.require-table table tr th {
  background-color: #f8f9fb;
}
.require-table table tr td {
  text-align: center;
  vertical-align: top;
}
.require-table table tr td:nth-of-type(2n + 1) {
  background-color: #fff;
}
.add-box ul {
  margin: 0;
  padding: 0;
}
.add-box ul li {
  list-style: none;
  text-align: left;
  height: 30px;
  line-height: 30px;
  background-color: #f8f9fb;
  border: 1px solid #f8f9fb;
  margin-top: 5px;
  text-indent: 5px;
  cursor: pointer;
  color: #0b61a3;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  position: relative;
}
.btn-add {
  display: inline-block;
  vertical-align: middle;
  border: none;
  padding: 0;
  position: absolute;
  right: 8px;
  top: 8px;
  cursor: pointer;
  display: none;
}
.add-box ul li:hover {
  border: 1px solid #0b61a3;
  background-color: #fff;
}
.add-box ul li:hover .btn-add {
  color: #0b61a3;
  display: block;
}
.require-item {
  vertical-align: top;
  padding: 20px 10px;
  box-sizing: border-box;
  background-color: #f5f5f5;
  border: 1px solid #f5f5f5;
  position: relative;
  cursor: pointer;
  margin-bottom: 5px;
}
/*  DCDCDC  background-color: #ffffff;*/
.require-item:hover {
  background-color: #ffffff;
  border: 1px solid #0b61a3;
}
.btn-close {
  position: absolute;
  right: 3px;
  bottom: 3px;
  padding: 0;
  border: none;
  color: #ff0000;
}
.btn-close-position {
  position: inherit;
}
.set-table table tr td{
  vertical-align: middle;
}
.save-btn,.close-btn{
  width: 150px;
  padding: 15px 0px;
  margin:30px auto 0px auto;
}


</style>
