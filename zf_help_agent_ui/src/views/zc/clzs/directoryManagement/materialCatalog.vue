/**
* @Author: liangss
* @Date: 2020-11-05 16:04:48
* @Last Modified by: liangss
* @Description: 材料目录管理
*/
<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--材料目录列表数据 start-->
      <el-col :span="24" :xs="24">
        <el-form
          :model="queryParams"
          ref="queryForm"
          :inline="true"
          label-width="70px"
        >
          <el-form-item label="目录名称" prop="catalogName">
            <el-input
              v-model.trim="queryParams.catalogName"
              placeholder="请输入目录名称"
              clearable
              size="small"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="目录编码" prop="catalogCode">
            <el-input
              v-model.trim="queryParams.catalogCode"
              placeholder="请输入目录编码"
              clearable
              size="small"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item class="fr mr0">
            <el-button
              type="primary"
              icon="el-icon-search"
              size="mini"
              @click="handleQuery"
              >搜索</el-button
            >
            <el-button
              type="warning"
              icon="el-icon-refresh"
              size="mini"
              @click="resetQuery"
              class="btn-reset"
              >重置</el-button
            >
          </el-form-item>
        </el-form>
        <el-row :gutter="10" class="mb8">
          <!--        <el-col :span="1.5">
            <el-button type="default" icon="el-icon-plus" size="mini" @click="handleInit" v-hasPermi="['sys:AreaSite:init']">新增</el-button>
          </el-col>
-->
          <el-col :span="1.5">
            <el-button
              type="default"
              icon="el-icon-plus"
              size="mini"
              @click="handleInit"
              >新增</el-button
            >
          </el-col>
        </el-row>
        <div class="primary-table">
          <!--  <h3><i class="el-icon-document"></i>事项标题</h3>-->
          <el-table :data="materalCatalogList" border :fit="true">
            <el-table-column type="expand">
              <template slot-scope="props">
                <template v-for="(item, index) in props.row.subList">
                  <el-form
                    label-position="left"
                    inline
                    class="demo-table-expand"
                  >
                    <el-form-item style="width: 3%" align="center">
                      <span>{{ index + 1 }}</span>
                    </el-form-item>
                    <el-form-item style="width: 15%" align="center">
                      <span>{{ item.catalogCode }}</span>
                    </el-form-item>
                    <el-form-item style="width: 14%" align="center">
                      <span>{{ item.catalogName }}</span>
                    </el-form-item>
<!--                    <el-form-item style="width: 15%" align="center">
                      <span>{{ item.width }}</span>
                    </el-form-item>
                    <el-form-item style="width: 14%" align="center">
                      <span>{{ item.height }}</span>
                    </el-form-item>-->
                    <el-form-item style="width: 15%" align="center">
                      <span>{{ item.materialCategoryName }}</span>
                    </el-form-item>
                    <el-form-item style="width: 14%" align="center">
                      <span>{{ item.modifyDate }}</span>
                    </el-form-item>
                    <el-form-item style="width: 5%" align="center">
                      <span>
                        <el-button
                          icon="el-icon-folder-add"
                          class="handle-btn"
                          @click="
                            handleChildView(
                              item.materialCatalogOid,
                              item.materialParentOid,
                              item.materialIdentificationTypeOid
                            )
                          "
                        ></el-button>
                      </span>
                    </el-form-item>
                  </el-form>
                </template>
              </template>
            </el-table-column>

            <el-table-column label="序号" width="55" align="center">
              <template slot-scope="scope">
                <span>{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              label="目录编码"
              prop="catalogCode"
              align="center"
              :show-overflow-tooltip="true"
            >
            </el-table-column>
            <el-table-column
              label="目录名称"
              prop="catalogName"
              align="center"
              :show-overflow-tooltip="true"
            >
            </el-table-column>
<!--            <el-table-column label="宽度" prop="width" align="center">
            </el-table-column>
            <el-table-column label="高度" prop="height" align="center">
            </el-table-column>-->
            <el-table-column
              label="所属类别"
              prop="materialCategoryName"
              align="center"
              :show-overflow-tooltip="true"
            >
            </el-table-column>
            <el-table-column
              label="修改时间"
              prop="modifyDate"
              align="center"
              :show-overflow-tooltip="true"
            >
            </el-table-column>
            <el-table-column
              label="操作"
              prop="handle"
              align="center"
              width="180"
            >
              <template slot-scope="scopew">
                <el-button
                  size="mini"
                  type="text"
                  icon="iconfont zfsoft-chakan"
                  @click="handleView(scopew.row)"
                  >查看</el-button
                >
                <el-button
                  size="mini"
                  type="text"
                  icon="iconfont zfsoft-shanchu"
                  @click="handleDelete(scopew.row)"
                  >删除</el-button
                >
                <el-button
                  size="mini"
                  type="text"
                  icon="iconfont zfsoft-xiugai"
                  @click="handleInit(scopew.row)"
                  >配置</el-button
                >
              </template>
            </el-table-column>
          </el-table>
        </div>
        <pagination
          v-show="total > 0"
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getList"
        />
      </el-col>
      <!--材料目录列表数据 end-->
    </el-row>

    <!-- 添加或修改咨材料类别对话框 -->
    <el-dialog
      v-dialog-drag
      height="600px"
      scrollbar
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="openInit"
      v-if="openInit"
      width="1100px"
      append-to-body
    >
      <div id="printTest">
        <el-form ref="form" :model="form" :rules="rules" label-width="0px">
          <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
            <colgroup>
              <col width="20%" />
              <col width="30%" />
              <col width="20%" />
              <col width="30%" />
            </colgroup>
            <tr>
              <td>
                <b><i class="require">*</i>所属材料类别：</b>
              </td>
              <td>
                <el-form-item prop="materialCategoryOid">
                  <el-select
                    @change="selectModel"
                    v-model="form.materialCategoryOid"
                    placeholder="请选择类型"
                  >
                    <el-option
                      v-for="mco in materialCategoryOptions"
                      :key="mco.materialCategoryOid"
                      :label="mco.categoryName"
                      :value="mco.materialCategoryOid"
                    ></el-option>
                  </el-select>
                </el-form-item>
              </td>
              <td>
                <b><i class="require">*</i>目录名称：</b>
              </td>
              <td>
                <el-form-item prop="catalogName">
                  <el-col :span="24">
                    <el-input
                      v-model.trim="form.catalogName"
                      placeholder="请输入目录名称"
                      maxlength="50"
                      show-word-limit
                    />
                  </el-col>
                </el-form-item>
              </td>
            </tr>


            <tr>
              <td>
                <b>备注：</b>
              </td>
              <td colspan="3">
                <el-form-item prop="note">
                  <el-col :span="24">
                    <el-input
                      type="textarea"
                      placeholder="请输入备注"
                      v-model.trim="form.note"
                      maxlength="2000"
                      show-word-limit
                    ></el-input>
                  </el-col>
                </el-form-item>
              </td>
            </tr>
          </table>

          <el-button type="primary" @click="addHtml" class="add-btn"
            >增加</el-button
          >
          <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table--common zf-zc-table--td-center">
            <colgroup>
              <col width="50" />
              <col width="220" />
<!--              <col width="70" />
              <col width="70" />
              <col width="90" />-->
              <col width="160" />
              <col width="70" />
              <col width="70" />
<!--              <col width="200" />-->
              <col width="200" />
              <col width="40" />
            </colgroup>
            <tr>
              <th>序号</th>
              <th>子项名称</th>
<!--              <th>宽度</th>
              <th>高度</th>
              <th>存在内部矩形</th>-->
              <th>卡证目录类型</th>
              <th>印章数量</th>
              <th>是否签字</th>
<!--              <th>百度模板id</th>-->
              <th>textIn模板id</th>
              <th>备注</th>
              <th>操作</th>
            </tr>
            <template v-for="(ruleForm, index) in form.subList">
              <template v-if="ruleForm.delFlag === 0">
                <tr>
                  <td>
                    <el-form-item prop="xuhao">
                      {{ index + 1 }}
                    </el-form-item>
                  </td>
                  <td>
                    <el-form-item
                      :prop="'subList.' + index + '.catalogName'"
                      :rules="rules.catalogNameChild"
                    >
                      <el-input v-model.trim="ruleForm.catalogName"></el-input>
                    </el-form-item>
                  </td>
                  <td>
                    <el-form-item prop="materialCategoryOid">
                      <el-select
                        v-model="ruleForm.materialIdentificationTypeOid"
                        placeholder="请选择卡证目录类型"
                        :clearable="true"
                      >
                        <el-option
                          v-for="mco in cardCatalogueListOptions"
                          :key="mco.oid"
                          :label="mco.cardCatalogueName"
                          :value="mco.oid"
                        ></el-option>
                      </el-select>
                    </el-form-item>
                  </td>
                  <td>
                    <el-form-item>
                      <el-input v-model="ruleForm.sealsNumber"></el-input>
                    </el-form-item>
                  </td>
                  <td>
                    <el-form-item>
                      <template v-if="ruleForm.isMultiPage === 'true'">
                        <el-checkbox
                          checked
                          v-model="ruleForm.isMultiPage"
                        ></el-checkbox>
                      </template>
                      <template v-else>
                        <el-checkbox
                          v-model="ruleForm.isMultiPage"
                        ></el-checkbox>
                      </template>
                    </el-form-item>
                  </td>
<!--                  <td>
                    <el-form-item>
                      <el-input
                        v-model.trim="ruleForm.baiduTemplateId"
                      ></el-input>
                    </el-form-item>
                  </td>-->
                  <td>
                    <el-form-item>
                      <el-input
                        v-model.trim="ruleForm.textinTemplateId"
                      ></el-input>
                    </el-form-item>
                  </td>
                  <td>
                    <el-form-item>
                      <el-input v-model.trim="ruleForm.note"></el-input>
                    </el-form-item>
                  </td>
                  <td>
                    <el-form-item>
                      <!--  <template slot-scope="scopew">-->
                      <el-button
                        style="border: 0px"
                        icon="el-icon-delete"
                        @click="delHtml(index)"
                        class="handle-btn"
                      ></el-button>
                      <!-- </template>-->
                    </el-form-item>
                  </td>
                </tr>
              </template>
            </template>
          </table>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">保存</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 材料目录详细 start-->
    <el-dialog
      v-dialog-drag
      height="600px"
      scrollbar
      :title="title"
      :visible.sync="openView"
      width="1100px"
      v-if="openView"
      append-to-body
    >
      <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
        <colgroup>
          <col width="20%" />
          <col width="30%" />
          <col width="20%" />
          <col width="30%" />
        </colgroup>
        <tr>
          <td>
            <b>所属材料类别：</b>
          </td>
          <td>
            {{ form.materialCategoryName }}
          </td>
          <td>
            <b>目录名称：</b>
          </td>
          <td>
            {{ form.catalogName }}
          </td>
        </tr>

        <tr>
          <td>
            <b>备注：</b>
          </td>
          <td colspan="3">
            {{ form.note }}
          </td>
        </tr>
      </table>

      <table
        cellspacing="0"
        cellpadding="0"
        border="0"
        class="zf-zc-table--common zf-zc-table--td-center"
      >
        <colgroup>
          <col width="200" />
          <col width="100" />
<!--          <col width="55" />
          <col width="70" />
          <col width="100" />-->
          <col width="70" />
          <col width="70" />
<!--          <col width="200" />-->
          <col width="200" />
          <col width="100" />
        </colgroup>
        <tr>
          <th>子项名称</th>
          <th>材料识别类型</th>
          <th>印章数量</th>
          <th>是否签字</th>
<!--          <th>百度模板id</th>-->
          <th>textIn模板id</th>
          <th>备注</th>
        </tr>

        <template v-for="(item, index) in form.subList">
          <tr>
            <td>
              {{ item.catalogName }}
            </td>
            <td>

              <template v-for="cardCatalogue in cardCatalogueListOptions">
                <template v-if="item.materialIdentificationTypeOid === cardCatalogue.oid">
                  {{ cardCatalogue.cardCatalogueName }}
                </template>
              </template>
            </td>
            <td>
              {{ item.sealsNumber }}
            </td>
            <td>
              <template v-if="item.isMultiPage === 'true'"> 是 </template>
              <template v-else> 否 </template>
            </td>
            <td>
              {{ item.baiduTemplateId }}
            </td>
<!--            <td>
              {{ item.textinTemplateId }}
            </td>-->
            <td>
              {{ item.note }}
            </td>
          </tr>
        </template>
      </table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openView = false">关 闭</el-button>
      </div>
    </el-dialog>
    <!-- 材料目录详细end -->

    <el-dialog
      v-dialog-drag
      :visible.sync="item.show"
      v-for="item in dialogOptions"
      :title="item.title"
      width="1100px"
      height="600px"
      scrollbar
      append-to-body
    >
      <material-catalog-element
        :materialCatalogOid="item.materialCatalogOid"
        :materialParentOid="item.materialParentOid"
        :cardCatalogueOid="item.cardCatalogueOid"
        :title="item.title"
        @father-click="handleChildView"
      ></material-catalog-element>
    </el-dialog>
  </div>
</template>

<script>
/*import { presonalpage,page,getOne,saveOrUpdate,del,share} from "@/api/zc/knowledgeBasvalidatee/notepad.js";*/
import {
  page,
  getMaterialCategoryList,
  getAllCardCatalogueList,
  getOne,
  initAll,
  saveOrUpdate,
  del,
  checkIsReationSx, checkIsReationYjssx, checkMaterialCatalogRepeat,
  getAttaBase64ByUrl,
} from "@/api/zc/clzs/directoryManagement/materialCatalog.js";
import materialCatalogElement from '@/views/zc/clzs/directoryManagement/materialCatalogElement';
import Treeselect from '@riophae/vue-treeselect';
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import {
  checkIsRelation
} from "@/api/zc/clzs/directoryManagement/materialCatalogElement";

export default {
  name: "MaterialCatalog",
  components: {
    Treeselect,
    materialCatalogElement
  },
  data () {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据
      materalCatalogList: [],
      //查看目录元素
      dialogOptions: [],
      //子项
      subList: [],
      //知识类别
      materialCategoryOptions: {},
      //卡证目录类别
      cardCatalogueListOptions:[],
      //材料识别类型map
      LICE_OCR_TYPE_MAP: {},
      //A3
      SIZE_TYPE_A3_MAP: {},
      //A4
      SIZE_TYPE_A4_MAP: {},
      //知识分类字典oid
      dictOidvalue: "",
      // 弹出层标题
      title: "",
      categoryName: "",
      // 新增/修改显示弹出层
      openInit: false,
      // 查看显示弹出层
      openView: false,
      //共享记事本列表
      openElementView: false,
      materialCategoryName: "",
      //实体
      materialCatalog: {},
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        catalogName: null,
        catalogCode: null
      },
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        materialCategoryOid: [{
          required: true,
          message: "请选择所属材料类别",
          trigger: "blur"
        }],
        catalogName: [{
          required: true,
          message: "目录名称不能为空",
          trigger: "blur"
        }],
        width: [{
          required: true,
          message: "宽度不能为空",
          trigger: "blur"
        }],
        height: [{
          required: true,
          message: "高度不能为空",
          trigger: "blur"
        }],
        catalogNameChild: [{
          required: true,
          message: "子项名称不能为空",
          trigger: "blur"
        }],

      }
    };
  },
  created () {

    this.getList();
    /** 获取材料类别数据 */
    getMaterialCategoryList().then(response => {
      this.materialCategoryOptions = response.data;
    });
    /** 获取卡证目录列表 */
    getAllCardCatalogueList().then(response => {
      this.cardCatalogueListOptions = response.data;
    });


   /* initAll().then(response => {
      this.LICE_OCR_TYPE_MAP = response.data.LICE_OCR_TYPE_MAP;
      this.SIZE_TYPE_A3_MAP = response.data.SIZE_TYPE_A3_MAP;
      this.SIZE_TYPE_A4_MAP = response.data.SIZE_TYPE_A4_MAP;
    });*/
  },

  methods: {
    //添加子项模块
    addHtml: function () {
      this.form.subList.push({
        delFlag: 0,
        width: this.SIZE_TYPE_A4_MAP.width,
        height: this.SIZE_TYPE_A4_MAP.height,
      })
    },
    //删除子项模块
    delHtml: function (index) {
      this.materialCatalog = {};
      this.materialCatalog = this.form.subList[index];
      this.materialCatalog.delFlag = 1;
      this.form.subList.splice(index, 1);
      if (undefined != this.form.materialCatalogOid && null != this.form.materialCatalogOid) {
        if (this.materialCatalog.materialCatalogOid != '') {
          this.form.subList.push(this.materialCatalog);
        }
      }
      //this.form.subList.splice(index,1);
    },
    //获取选择材料类别的名称
    selectModel (val) {
      this.categoryName = val ? this.materialCategoryOptions.find(ele => ele.materialCategoryOid === val).categoryName :
        ''
      this.form.materialCategoryName = this.categoryName;
    },
    //选择宽高类型
    changewidthHeightType: function (val) {
      if (val == 'A3') {
        this.form.width = this.SIZE_TYPE_A3_MAP.width;
        this.form.height = this.SIZE_TYPE_A3_MAP.height;
      } else if (val == 'A4') {
        this.form.width = this.SIZE_TYPE_A4_MAP.width;
        this.form.height = this.SIZE_TYPE_A4_MAP.height;
      }
    },

    /** 查询我的材料目录列表 */
    getList () {
      this.loading = true;
      page(this.queryParams).then(response => {
        this.materalCatalogList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      });

      //get64临时
      /* getAttaBase64ByUrl().then(response => {
        alert(response)
       });*/
    },
    // 取消按钮
    cancel () {
      this.openInit = false;
      this.reset();
    },
    // 表单重置
    reset () {
      Object.assign(this.form, this.$options.data().form)
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery () {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery () {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 查看按钮操作 */
    handleView (row) {
      let _that = this;
      _that.reset();
      const oid = row.id;
      getOne(oid).then(response => {
        _that.form = response.data;
        _that.openView = true;
        _that.title = "查看材料目录信息";
      });
    },
    /** 新增和修改按钮操作 */
    handleInit (row) {
      let _that = this;
      _that.reset();
      if (row.id) {
        const mcoid = row.materialCatalogOid;
        /* checkIsReationSx(mcoid).then(response => {
           let list=[];
           list=response.data;
           if (list && list.length>0) {
             _that.msgSuccess("该材料目录已被材料关联不可修改");
           } else {
             checkIsReationYjssx(mcoid).then(response=>{
               let yjs=[];
               yjs=response.data;
               if(yjs && yjs.length>0){
                 _that.msgSuccess("该材料目录已被材料关联不可修改");
               }else{
                 getOne(row.id).then(response => {
                   _that.openInit = true;
                   _that.form = response.data;
                 })
               }
             })
           }
         });
*/
        getOne(row.id).then(response => {
          _that.openInit = true;
          _that.form = response.data;
        })
      } else {
        _that.openInit = true;
        _that.form = {
          id: '',
          materialCategoryOid: '',
          materialCategoryName: '',
          materialCatalogOid: '',
          catalogName: '',
          widthHeightType: 'A4',
          angleType: '',
          width: _that.SIZE_TYPE_A4_MAP.width,
          height: _that.SIZE_TYPE_A4_MAP.height,
          subList: [],
        };
      }
      _that.title = row.id ? "修改材料目录信息" : "新增材料目录信息";
    },

    /** 提交按钮 */
    submitForm: function () {
      let _that = this;
      let noRepeat = true;
      /*  alert(JSON.stringify( _that.form))*/
      _that.$refs["form"].validate(valid => {
        if (valid) {
          //判断父级目录名称是否重复
          checkMaterialCatalogRepeat(_that.form.materialCatalogOid, _that.form.catalogName).then(responseGet => {
            if (responseGet.data == '0') {
              //判断子级目录名称是否重复
              if (_that.form.subList != null && _that.form.subList.length > 1) {
                let chlidName = [];
                for (let catalog of _that.form.subList) {
                  chlidName.push(catalog.catalogName);
                }
                let repeat = chlidName.join(",") + ",";
                for (let i = 0; i < chlidName.length; i++) {
                  if (repeat.replace(chlidName[i] + ",", "").indexOf(chlidName[i] + ",") > -1) {
                    _that.msgWarning("子级名称:" + repeat[i] + " 已存在，不能重复添加！");
                    noRepeat = false;
                    break;
                  }
                }
              }
              if (noRepeat) {
                saveOrUpdate(_that.form).then(response => {
                  if (response.code === 200) {
                    _that.msgSuccess("保存成功");
                    _that.openInit = false;
                    setTimeout(() => {
                      _that.getList();
                    }, 10);

                  }
                });
              }
            } else {
              _that.msgWarning("目录名称已存在，不能重复添加！");
            }
          });

        }
      });
    },
    /** 删除按钮操作 */
    handleDelete (row) {
      let _that = this;
      const oid = row.id;
      const mcoid = row.materialCatalogOid;
      checkIsReationSx(mcoid).then(response => {
        let list = [];
        list = response.data;
        if (list && list.length > 0) {
          _that.msgSuccess("该材料目录已被材料关联不可删除");
        } else {
          checkIsReationYjssx(mcoid).then(response => {
            let yjs = [];
            yjs = response.data;
            if (yjs && yjs.length > 0) {
              _that.msgSuccess("该材料目录已被材料关联不可删除");
            } else {
              _that.$confirm("是否确认删除?", "警告", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning"
              })
                .then(function () {
                  return del(oid);
                })
                .then(() => {
                  _that.getList();
                  _that.msgSuccess("删除成功");
                })
                .catch(function () { });
            }
          })
        }
      });


    },
    //材料目录元素编制页面
    handleChildView (materialCatalogOid, materialParentOid,cardCatalogueOid) {
      let item = {
        materialCatalogOid: materialCatalogOid,
        show: true,
        materialParentOid: materialParentOid,
        cardCatalogueOid:cardCatalogueOid,
        title: '材料目录元素编制'
      };
      this.dialogOptions.push(item);
    },

  },
};
</script>
<style lang="scss" scoped>
.primary-table h3 {
  font-size: 14px;
  font-weight: normal;
  color: #333;
  margin: 0px 0px 10px 0px;
}

.primary-table .handle-btn {
  border: none;
  background: none;
  padding: 0;
  font-size: 16px;
}

.primary-table .el-table,
.primary-table .el-table th {
  font-size: 13px;
}

.primary-table .el-form-item:first-child {
  margin-left: 8px;
}

.primary-table .el-form-item {
  margin-bottom: 0;
  margin-right: 0;
  margin-left: 0px !important;
  padding: 0;
  box-sizing: border-box;
  font-size: 13px;
}

.primary-table .el-form-item__content {
  font-size: 13px;
}

.primary-table .inputTable .el-form-item:first-child {
  margin-left: 0;
}

.primary-table .inputTable .el-form-item {
  width: 100px;
}

.ruleForm-table table {
  border: 1px solid #ebeef5;
  border-collapse: collapse;
}

.ruleForm-table table tr th {
  color: #515a6e;
  font-size: 13px;
  background-color: #f8f8f9;
  text-align: left;
}

.ruleForm-table table tr th,
.ruleForm-table table tr td {
  padding: 17px 10px;
  font-size: 13px;
  color: #606266;
  border: 1px solid #ebeef5;
}

.ruleForm-table .el-form-item {
  margin-left: 0px !important;
  width: 120px;
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

.add-btn {
  margin: 10px 0px;
  float: right;
}

.dialog-table table.tc tr td {
  text-align: center;
}
</style>
