/**
* 验证规则详细信息
* @author: liangss
* @date: 2020-11-12
*/

<template>
  <div class="el-table__header-wrapper dialog-table require-table">
    <table cellspacing="0" cellpadding="0" border="0"  style="margin-bottom: 20px">
      <colgroup>
        <col width="20%" />
        <col width="30%" />
        <col width="20%" />
        <col width="30%" />
      </colgroup>
      <tr>
        <td>
          <b>事项编码：</b>
        </td>
        <td >
          {{ implementCode }}
        </td>
        <td>
          <b>事项名称：</b>
        </td>
        <td >
          {{ serviceName }}
        </td>
      </tr>
    </table>

    <h3><i class="el-icon-document"></i>单项验证规则</h3>
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
            <td v-show="indexx==0" :rowspan="item.materialCatalogList.length">{{ index+1 }}</td>
            <td v-show="indexx==0" :rowspan="item.materialCatalogList.length"> {{ item.materialName }}</td>
          <td>
              <div class="add-box">
              <span>{{materialCatalog.catalogName}}
                <template  v-if="materialCatalog.catalogCode!='' &&materialCatalog.catalogCode!=null">
                    ({{materialCatalog.catalogCode}})
                 </template>
              </span>
              </div>
          </td>

        <td>
                <template v-if="materialCatalog.template !=null">
                    <template  v-for="(rule,indexmc) in faModelRuleValidationList">
                      <template v-if="rule.validateType === VALIDATION_TYPE_NOTNULL && rule.delFlag===0 && rule.catalogOid===materialCatalog.materialCatalogOid ">
                      <div class="require-item"   >
                        {{rule.templateMetadataName }}({{rule.templateMetadataCode}})
                      </div>
                      </template>
                    </template>

                  </template>
          </td>
        <td>
            <template v-if="materialCatalog.template !=null">
              <template  v-for="(rule,indexmc) in faModelRuleValidationList">
                <template v-if="rule.validateType === VALIDATION_TYPE_NULL && rule.delFlag===0 && rule.catalogOid===materialCatalog.materialCatalogOid ">
                  <div class="require-item"  >
                   {{rule.templateMetadataName }}（{{rule.templateMetadataCode}}）
                  </div>
                </template>
              </template>

            </template>
        </td>
        <td>
            <template v-if="materialCatalog.template !=null">
              <template  v-for="(rule,indexmc) in faModelRuleValidationList">
                <template v-if="rule.validateType === VALIDATION_TYPE_CONTENT && rule.delFlag===0 && rule.catalogOid===materialCatalog.materialCatalogOid">
                  <div class="require-item"   >
                    {{rule.templateMetadataName }}({{rule.templateMetadataCode}})=={{rule.thanContent}}
                  </div>
                </template>
              </template>

            </template>
        </td>
        <td>
            <template v-if="materialCatalog.template !=null">
              <template  v-for="(rule,indexmc) in faModelRuleValidationList">
                <template v-if="rule.validateType === VALIDATION_TYPE_STR_CONTAIN && rule.delFlag===0 && rule.catalogOid===materialCatalog.materialCatalogOid">
                  <div class="require-item"   >
                    {{rule.templateMetadataName }}({{rule.templateMetadataCode}})=={{rule.thanContent}}
                  </div>
                </template>
              </template>

            </template>
        </td>
        <td>
            <template v-if="materialCatalog.template !=null">
              <template  v-for="(rule,indexmc) in faModelRuleValidationList">
                <template v-if="rule.validateType === VALIDATION_TYPE_RANGE && rule.delFlag===0 && rule.catalogOid===materialCatalog.materialCatalogOid">
                  <div class="require-item"  >
                    {{rule.templateMetadataName }}({{rule.templateMetadataCode}})
                    下限：{{rule.contentDown }};上限：{{rule.contentUp }}
                  </div>
                </template>
              </template>

            </template>
        </td>
        <td>
            <template v-if="materialCatalog.template !=null">
              <template  v-for="(rule,indexmc) in faModelRuleValidationList">
                <template v-if="rule.validateType === VALIDATION_TYPE_DATE_COMPARE && rule.delFlag===0 && rule.catalogOid===materialCatalog.materialCatalogOid">
                  <div class="require-item"  >
                    {{rule.templateMetadataName }}({{rule.templateMetadataCode}})
                    日期格式：{{rule.contentDown }};比对日期：{{rule.contentUp }}
                  </div>
                </template>
              </template>
            </template>
        </td>
        <td>
            <template v-if="materialCatalog.template !=null">
              <template  v-for="(rule,indexmc) in faModelRuleValidationList">
                <template v-if="rule.validateType === VALIDATION_TYPE_HAND_SIGN && rule.delFlag===0 && rule.catalogOid===materialCatalog.materialCatalogOid">
                  <div class="require-item"  >
                    {{rule.templateMetadataName }}{{rule.templateMetadataCode}}
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
            </tr>
          </template>
        </template>
      </table>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button size="medium" @click="guanbi()" class="close-btn">关闭</el-button>
    </div>
  </div>
</template>

<script>
import {initFaModelRule,initAll} from "@/api/zc/clzs/modelManagement/faModelRuleValidation.js";
export default {
 props: ['serviceOid','implementCode','serviceName'],
  data() {
    return {
      num: 0,
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
      // 查询参数
      queryParams: {
        serviceOid:this.serviceOid,
      },
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
    guanbi(){
      this.$emit('father-click','N');//调用父页面关闭子页面的功能
    },

    initInfo() {
      this.loading = true;
      initFaModelRule(this.queryParams).then(response => {
        this.serviceMaterList=response.data.serviceMaterList;
        this.faModelRuleValidationList=response.data.faModelRuleValidationList;
        this.loading = false;
      });
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
/*.add-box ul li:hover {
  border: 1px solid #0b61a3;
  background-color: #fff;
}
.add-box ul li:hover .btn-add {
  color: #0b61a3;
  display: block;
}*/
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
/*.require-item:hover {
  background-color: #ffffff;
  border: 1px solid #0b61a3;
}*/
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
  margin:30px auto 0px auto;
}


</style>
