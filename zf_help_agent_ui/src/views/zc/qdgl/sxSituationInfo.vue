<template>
  <div style="height: 100%">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="72px" @submit.native.prevent>
      <el-form-item label="情形名称" prop="situationName">
        <el-input v-model.trim="queryParams.situationName" placeholder="请输入情形名称" clearable size="small"
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item class="fr mr0">
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery" class="btn-reset">重置
        </el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8 fl">
      <el-col :span="1.5">
        <el-button type="default" icon="el-icon-plus" size="mini" @click="handleInit" v-hasPermi="['service:mag:init']">
          新增</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="sxSituationList" border :fit="true">
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="情形名称" align="center" prop="situationName" :show-overflow-tooltip="true" />
      <el-table-column label="排序号" align="center" prop="sort" :show-overflow-tooltip="true" />
      <el-table-column label="操作" align="center" width="250" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="iconfont zfsoft-xiugai" @click="handleInit(scope.row)"
            v-hasPermi="['sys:reg:view']">修改</el-button>
          <el-button size="mini" type="text" icon="iconfont zfsoft-shanchu" @click="handleDel(scope.row)"
            v-hasPermi="['service:qx:init']">删除</el-button>
          <el-button size="mini" type="text" icon="iconfont zfsoft-penzhi" @click="handleConfig(scope.row)"
            v-hasPermi="['service:qxxx:init']">情形选项配置</el-button>
          <!--          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            @click="handleDel(scope.row)"
            v-hasPermi="['service:qxcl:init']"
          >选项材料配置</el-button
          >-->
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 新增修改 -->
    <el-dialog v-dialog-drag :visible.sync="addUpdateFlag" v-if="addUpdateFlag" :title="addUpdateTitle" width="1100px"
      append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="0px">
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <colgroup>
            <col width="15%" />
            <col width="35%" />
            <col width="15%" />
            <col width="35%" />
          </colgroup>
          <tr>
            <td>
              <i class="require">*</i>
              <b>情形名称：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="situationName">
                <el-input v-model.trim="form.situationName" maxlength="100" show-word-limit placeholder="请输入情形名称" />
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td>
              <i class="require">*</i>
              <b>排序号：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="sort">
                <!--<el-input :rows="2" v-model.trim="form.sort" placeholder="请输入情形名称"/>-->
                <el-input-number v-model="form.sort" @keydown.native="inputLimit" :min="1" :max="999999" label></el-input-number>
              </el-form-item>
            </td>
          </tr>
        </table>
      </el-form>
      <div slot="footer" style="text-align: center">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 情形选项配置 -->
    <el-dialog v-dialog-drag :visible.sync="configFormFlag" v-if="configFormFlag" :title="configFormTitle"
      width="1100px" scrollbar height="700px" append-to-body>
      <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table el-table__body mt20" style="width: 100%">
        <tr>
          <td>
            <div v-if="sxServiceOptionTitles.length > 0">
              <div class="forList" v-for="(data, index) in sxServiceOptionTitles" :key="index">
                <div class="check-list" v-if="
                    data.isHavingCorrelation == 0 ||
                      data.isHavingCorrelation == 2
                  ">
                  <h3>{{ data.name }}</h3>
                  <template v-if="data.moreStatus == '0'">
                    <!--<el-radio-group v-model="checkList[index]">-->
                    <el-radio-group v-model="data.isSelecteds">
                      <el-radio v-for="(item, idx) in data.sxServiceOptionVals" :key="idx" :disabled="ifDis"
                        :label="item.oid" @change="changeTitle(data.oid, item.oid)">{{ item.name }}</el-radio>
                    </el-radio-group>
                  </template>
                  <template v-if="data.moreStatus == '1'">
                    <el-checkbox-group v-model="data.isSelecteds">
                      <el-checkbox v-for="item in data.sxServiceOptionVals" :disabled="ifDis" :key="item.oid"
                        @change="changeTitle(data.oid, item.oid)" :id="item.oid" :label="item.oid">{{ item.name }}
                      </el-checkbox>
                    </el-checkbox-group>
                  </template>
                </div>
              </div>
            </div>
            <div v-else>暂无数据</div>
          </td>
        </tr>
      </table>

      <el-table v-loading="loading" :data="materialList" border style="margin-top:15px">
        <el-table-column label="序号" align="center" type="index" width="60" />
        <el-table-column label="材料名称" align="center" prop="materialName" :show-overflow-tooltip="true" />
        <el-table-column label="材料类型">
          <template slot-scope="scope">
            <span v-if="scope.row.materialType == '0'">原件</span>
            <span v-if="scope.row.materialType == '1'">复印件</span>
            <span v-if="scope.row.materialType == '2'">原件和复印件</span>
          </template>
        </el-table-column>
        <el-table-column label="来源渠道">
          <template slot-scope="scope">
            <span v-if="scope.row.materialSource == 0">申请人自备</span>
            <span v-if="scope.row.materialSource == 1">政府部门核发</span>
            <span v-if="scope.row.materialSource == 2">其它</span>
          </template>
        </el-table-column>
        <el-table-column prop="materialFormat" align="center" width="150" label="材料形式">
          <template slot-scope="scope">
            <span v-if="scope.row.materialFormat == 1">纸质</span>
            <span v-if="scope.row.materialFormat == 2">电子版</span>
            <span v-if="scope.row.materialFormat == 3">证照</span>
            <span v-if="scope.row.materialFormat == 4">容缺补正</span>
          </template>
        </el-table-column>
        <el-table-column label="纸质材料份数" align="center" prop="paperNumber" :show-overflow-tooltip="true" />
      </el-table>

      <div slot="footer" style="text-align: center">
        <el-button type="primary" @click="situationConfigAdd" style="margin-right:5px">
          保 存
        </el-button>
        <el-button @click="configCancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 信息详细 -->
    <el-dialog v-dialog-drag :visible.sync="infoView" v-if="infoView" title="事项信息" width="1100px" height="700px"
      scrollbar append-to-body>
      <view-sx-service-info :sxServiceOid="serviceOid" @view-service="viewServiceClose"></view-sx-service-info>
      <div slot="footer" class="zf-text-center">
        <el-button @click="infoView = false">
          关闭
        </el-button>
      </div>
    </el-dialog>

    <!-- 情形配置 -->
    <el-dialog v-dialog-drag :visible.sync="situationView" v-if="situationView" title="配置情形" width="900px"
      height="600px" append-to-body>
      <sx-situation-info :serviceOid="serviceOid" @config-service="configServiceClose"></sx-situation-info>
      <div slot="footer" class="zf-text-center">
        <el-button @click="situationView = false">
          关闭
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {
    pageSituation
  } from "@/api/zc/qdgl/sxService.js";
  import {
    insertSxSituationInfo,
    getSxServiceSituationByOid,
    delSxServiceSituationByOid,
    getSxServiceTitlesNoRelation,
    querySxServiceSituationRelationBySituationAndTitle,
    saveSxServiceSituations,
    getSxServiceTitlesByRelationOids,
    getAllSelectedOids,
    getSituationMaterialListByOids,
    unique
  } from "@/api/zc/qdgl/sxSituationInfo.js";
  export default {
    name: "SxSituationInfo",
    components: {},
    props: ["serviceOid"],
    data() {
      return {
        isSituationShow: false,
        addUpdateTitle: "",
        addUpdateFlag: false,
        // 遮罩层
        loading: true,
        // 总条数
        total: 0,
        // 应用表格数据
        sxSituationList: [],
        configFormFlag: false,
        configFormTitle: "",
        sxServiceOptionTitles: [],
        sxServiceOptionOids: [],
        checkList: [],
        checkBoxList: [],
        ifDis: false,
        saveSituationId: "",
        valOids: "",
        currentOid: "",
        currentTitleOid: "",
        rootTitleOid: "",
        // 弹出层标题
        title: "",
        // 查看显示弹出层
        infoView: false,
        openInit: false,
        situationView: false,
        materialList: [],
        //item: {isSelecteds},
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          serviceOid: this.serviceOid,
          situationName: ""
        },
        // 表单参数
        form: {
          serviceOid: this.serviceOid,
          sort: 0,
          situationName: ""
        },
        rules: {
          situationName: [{
            required: true,
            message: "情形名称不能为空",
            trigger: "blur"
          }],
          sort: [{
            required: true,
            message: "排序号不能为空",
            trigger: "blur"
          }]
        }
      };
    },
    created() {
      //this.getList();
    },
    //获取父页面的值
    mounted() {
      this.form.serviceOid = this.serviceOid;
      this.queryParams.serviceOid = this.serviceOid;
      this.getList();
    },
    methods: {
      changeTitleBox(item, val) {},
      /** 改变选项值 */
      async changeTitle(titleOid, optionValOid) {
        //const oids = getAlSelectedOids(this.sxServiceOptionTitles);
        const oids = getAllSelectedOids(this.sxServiceOptionTitles);
        if (oids.indexOf(optionValOid) == -1) {
          optionValOid = "";
        }
        //const oids = "";
        const {
          data,
          code,
          message
        } = await getSxServiceTitlesByRelationOids({
          serviceOid: this.serviceOid,
          valOids: oids,
          currentOid: optionValOid,
          currentTitleOid: titleOid,
          rootTitleOid: this.sxServiceOptionTitles[0].oid
        });
        if (code !== 200) return message && this.$message.warning(message);
        // this.situationCheckList = formatSituationCheckList(data);
        //this.situationCheckList = removeDuplicate(formatSituationCheckList(data));
        //this.sxServiceOptionTitles = data;
        this.sxServiceOptionTitles = [];
        var uniqueData = unique(data);
        for (let i = 0; i < uniqueData.length; i++) {
          var item = uniqueData[i];
          // 多选
          const isSelectedList = item.sxServiceOptionVals.filter(
            i => i.isSelected == "1"
          );
          // 是单选就字符串 多选就数组
          let isSelected = item.moreStatus === 0 ? "" : [];
          if (isSelectedList.length > 0) {
            if (isSelectedList.length >= 1 && item.moreStatus === 1) {
              isSelected = isSelectedList.map(i => i.oid);
            } else {
              isSelected = isSelectedList[0].oid;
            }
          }
          item.isSelecteds = isSelected;
          this.sxServiceOptionTitles.push(item);
          //this.sxServiceOptionTitles.push(response.data[i]);
        }
        this.getsxServiceMaterialList(oids);
      },
      situationConfigAdd() {
        var situationId = this.saveSituationId;
        var configs = [];
        for (let i = 0; i < this.sxServiceOptionTitles.length; i++) {
          var item = this.sxServiceOptionTitles[i];
          if (
            typeof item.isSelecteds == "string" &&
            item.isSelecteds != null &&
            item.isSelecteds.length > 0
          ) {
            var config = {
              situationOid: situationId,
              optionOid: item.isSelecteds
            };
            configs.push(config);
          } else if (typeof item.isSelecteds == "object") {
            item.isSelecteds.forEach(element => {
              var config = {
                situationOid: situationId,
                optionOid: element
              };
              configs.push(config);
            });
          }
        }
        if (configs.length == 0) {
          var config = {
            situationOid: situationId,
            optionOid: -1
          };
          configs.push(config);
        }
        saveSxServiceSituations(configs).then(response => {
          if (response.code === 200) {
            this.configFormFlag = false;
            this.getList();
          } else {}
        });
      },
      resetConfigData() {
        this.saveSituationId = "";
        this.sxServiceOptionTitles = [];
        this.sxServiceOptionOids = [];
        this.checkBoxList = [];
        this.checkList = {};
      },
      async querySxServiceSituationRelationBySituationAndTitleMethod(
        queryParams
      ) {
        const {
          data,
          code,
          message
        } = await querySxServiceSituationRelationBySituationAndTitle(queryParams);
        for (let i = 0; i < data.length; i++) {
          var item = data[i];
          // 多选
          const isSelectedList = item.sxServiceOptionVals.filter(
            i => i.isSelected == "1"
          );
          // 是单选就字符串 多选就数组
          let isSelected = item.moreStatus === 0 ? "" : [];
          if (isSelectedList.length > 0) {
            if (isSelectedList.length >= 1 && item.moreStatus === 1) {
              isSelected = isSelectedList.map(i => i.oid);
            } else {
              isSelected = isSelectedList[0].oid;
            }
          }
          item.isSelecteds = isSelected;
          var flag = true;
          this.sxServiceOptionOids.forEach(element => {
            if (element == item.oid) {
              flag = false;
            }
          });
          if (flag) {
            this.sxServiceOptionTitles.push(item);
            this.sxServiceOptionOids.push(item.oid);
          }
          //this.sxServiceOptionTitles.push(response.data[i]);
        }
      },
      //情形选项配置
      async handleConfig(row) {
        const loading = this.$loading({
          lock: true,
          text: "正在获取情形选项配置...",
          spinner: "el-icon-loading",
          background: "rgba(0, 0, 0, 0.7)"
        });
        this.resetConfigData();
        this.saveSituationId = row.oid;
        //row.oid
        const {
          data,
          code,
          message
        } = await getSxServiceTitlesNoRelation({
          serviceOid: this.serviceOid
        });
        if (code !== 200) {
          loading.close();
          return message && this.$message.warning(message);
        }
        //this.sxServiceOptionTitles =  data;
        //this.sxServiceOptionTitles = [];
        let arrPromise = [];
        var arrays = data;
        arrays.forEach(item => {
          var queryParams = {
            serviceOid: this.serviceOid,
            titleOid: item.oid,
            situationId: row.oid
          };
          arrPromise.push(
            querySxServiceSituationRelationBySituationAndTitle(queryParams)
          );
        });
        Promise.all(arrPromise)
          .then(ret => {
            let selectvalus = [];
            for (let {
                data,
                code,
                message
              } of ret) {
              for (let i = 0; i < data.length; i++) {
                var item = data[i];
                // 多选
                const isSelectedList = item.sxServiceOptionVals.filter(
                  i => i.isSelected == "1"
                );
                // 是单选就字符串 多选就数组
                let isSelected = item.moreStatus === 0 ? "" : [];
                if (isSelectedList.length > 0) {
                  if (isSelectedList.length >= 1 && item.moreStatus === 1) {
                    isSelected = isSelectedList.map(i => i.oid);
                  } else {
                    isSelected = isSelectedList[0].oid;
                  }
                }
                if (isSelected) {
                  selectvalus.push(isSelected);
                }
                item.isSelecteds = isSelected;
                var flag = true;
                this.sxServiceOptionOids.forEach(element => {
                  if (element == item.oid) {
                    flag = false;
                  }
                });
                if (flag) {
                  this.sxServiceOptionTitles.push(item);
                  this.sxServiceOptionOids.push(item.oid);
                }
              }
            }
            //获取材料信息
            this.getsxServiceMaterialList(selectvalus);
            loading.close();
            this.configFormFlag = true;
            this.configFormTitle = "情形选项配置";
          })
          .catch(() => {
            loading.close();
          });
      },
      //删除
      handleDel(row) {
        const oid = row.oid;
        this.$confirm("是否确认删除?", "警告", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          })
          .then(function () {
            return delSxServiceSituationByOid(oid);
          })
          .then(() => {
            this.msgSuccess("删除成功");
            this.getList();
          })
          .catch(function () {});
      },
      /** 排序号类型限制 */
      inputLimit (e) {
        let key = e.key
        // 不允许输入'e'和'+'
        if (key === 'e' || key === '+') {
          e.returnValue = false
          return false
        }
        return true
      },
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            insertSxSituationInfo(this.form).then(response => {
              if (response.code == 200) {
                this.$message.success("保存成功！");
                this.addUpdateFlag = false;
                this.resetSxSituation();
                //刷新列表
                this.getList();
              } else {
                this.$message.error("保存失败！");
              }
            });
          }
        });
      },
      /** 查看按钮操作 */
      handleMa(row) {
        this.serviceOid = row.serviceOid;
        this.infoViewMa = true;
      },
      /** 查询列表 */
      getList() {
        this.loading = true;
        pageSituation(this.queryParams).then(response => {
          this.sxSituationList = response.data.data.data;
          this.total = response.data.data.total;
          this.loading = false;
        });
      },
      // 取消按钮
      cancel() {
        //this.reset();
        this.addUpdateFlag = false;
        this.resetSxSituation();
      },
      configCancel() {
        //this.reset();
        this.configFormFlag = false;
        this.resetConfigData();
        //this.resetSxSituation();
      },
      resetSxSituation() {
        var chServiceOid = this.form.serviceOid;
        this.form = {};
        this.resetForm("form");
        this.form.serviceOid = chServiceOid;
      },
      // 表单重置
      reset() {
        Object.assign(this.form, this.$options.data().form);
        this.resetForm("form");
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.handleQuery();
      },
      /** 查看按钮操作 */
      handleView(row) {
        this.serviceOid = row.serviceOid;
        this.infoView = true;
      },
      viewServiceClose() {
        this.infoView = false;
      },

      handleInit(row) {
        let _that = this;
        this.form.serviceOid = this.serviceOid;
        this.form.oid = "";
        this.addUpdateTitle = "新增情形信息";
        if (row.oid) {
          getSxServiceSituationByOid(row.oid).then(res => {
            _that.form = res.data;
            _that.addUpdateTitle = "修改情形信息";
          });
        }
        this.addUpdateFlag = true;
      },
      //关闭新增页面
      initServiceClose() {
        this.openInit = false;
        this.getList();
      },
      handlePlace(row) {
        this.serviceOid = row.serviceOid;
        this.placeFlag = true;
      },

      handleSituationInit(row) {
        this.situationView = true;
        this.serviceOid = row.serviceOid;
      },
      configServiceClose() {
        this.situationView = false;
      },
      //根据所有选项值查询事项材料
      getsxServiceMaterialList(oids) {
        getSituationMaterialListByOids(this.serviceOid, oids).then(res => {
          this.materialList = res.data;

        })

      }
    }
  };

</script>
<style lang="scss" scoped>
  .dialog-table {
    padding: 20px;
    box-sizing: border-box;
  }

  .dialog-table h3 {
    font-size: 14px;
    font-weight: normal;
    color: #333;
    margin: 0px 0px 10px 0px;
  }

  .dialog-table table {
    width: 100%;
  }

  .dialog-table table {
    border: 1px solid #dfe6ec;
    border-collapse: collapse;
  }

  .dialog-table table tr td {
    border: 1px solid #dfe6ec;
    padding: 17px 8px;
    font-size: 12px;
    color: #515a6e;
    text-align: right;
  }

  .dialog-table table tr td:nth-of-type(2n) {
    color: #606266;
    text-align: left;
  }

  .dialog-table .el-form-item {
    margin-bottom: 0;
  }

  .el-form-item--small .el-form-item_content {
    margin-left: 0px !important;
  }

  .line {
    text-align: center;
  }

  .forList {
    border: 1px solid #ccc;
    padding: 0 20px 20px 20px;
    border-bottom: none;
  }

  .forList:nth-last-child(1) {
    border-bottom: 1px solid #ccc !important;
  }

</style>
