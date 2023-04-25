/**
* 材料审查要点配置管理
* @author: chenjm
* @date: 2020-11-10
*/
<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--事项数据-->
      <el-col :span="24" :xs="24">
        <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="108px">
          <!--<el-form-item label="事项类型" prop="serviceTypeOidDict">
            <el-select v-model="queryParams.serviceTypeOidDict"  placeholder="请选择事项类型"  @change="selectChanged" >
              <el-option
                v-for="dict in typeOptions"
                :key="dict.code"
                :label="dict.name"
                :value="dict.code"
              ></el-option>
            </el-select>
          </el-form-item>-->

          <el-form-item label="事项名称" prop="serviceName" v-show="this.queryParams.sShow">
            <el-input
              v-model.trim="queryParams.serviceName"
              placeholder="请输入事项名称"
              clearable
              size="small"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>

          <el-form-item label="实施编码" prop="implementCode" v-show="this.queryParams.sShow">
            <el-input
              v-model.trim="queryParams.implementCode"
              placeholder="请输入实施编码"
              clearable
              size="small"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>

         <el-form-item label="一件事名称" prop="comboDirectoryName" v-show="this.queryParams.cShow">
          <el-input
            v-model.trim="queryParams.comboDirectoryName"
            placeholder="请输入一件事名称"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          />
          </el-form-item>


          <el-form-item label="一件事编码" prop="comboDirectoryCode" v-show="this.queryParams.cShow">
            <el-input
              v-model.trim="queryParams.comboDirectoryCode"
              placeholder="请输入一件事编码"
              clearable
              size="small"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>

          <el-form-item class="fr mr0">
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery" class="btn-reset">重置</el-button>
           <!-- <el-button type="warning" icon="el-icon-refresh" size="mini" @click="openMaterialComparison" class="btn-reset">重置</el-button>-->
          </el-form-item>
        </el-form>
      <!--  <img src="{{img}}">-->

        <!--事项列表-->
        <el-table v-loading="loading" :data="sxServiceList" v-show="this.queryParams.sShow" border :fit="true">
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column label="所属区划" align="center" prop="districtName" :show-overflow-tooltip="true"/>
          <el-table-column label="所属机构" align="center"  prop="organName" :show-overflow-tooltip="true"/>
          <el-table-column label="事项名称"  align="center" prop="serviceName" :show-overflow-tooltip="true"/>
          <el-table-column label="实施编码"  align="center" prop="implementCode" :show-overflow-tooltip="true"/>
          <el-table-column label="事项类型"  align="center" prop="serviceTypeName" :show-overflow-tooltip="true"/>
          <el-table-column label="服务对象"  align="center" :show-overflow-tooltip="true">
          <template slot-scope="scope">
            <template v-if="scope.row.serviceObject.includes('1')">
              自然人
            </template>
            <template v-if="scope.row.serviceObject.includes('2')">
              企业法人
            </template>
            <template v-if="scope.row.serviceObject.includes('3')">
              事业法人
            </template>
            <template v-if="scope.row.serviceObject.includes('4')">
              社会组织法人
            </template>
            <template v-if="scope.row.serviceObject.includes('5')">
              非法人企业
            </template>
            <template v-if="scope.row.serviceObject.includes('6')">
              行政机关
            </template>
          </template>
          </el-table-column>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="iconfont zfsoft-xiugai"  @click="handleInitSx(scope.row)" >配置</el-button>
            </template>
          </el-table-column>
        </el-table>

        <!--一件事列表-->
        <el-table v-loading="loading" :data="taocanList" border :fit="true" v-show="this.queryParams.cShow">
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{scope.$index + 1}}</span>
            </template>
          </el-table-column>
          <el-table-column label="一件事名称"  align="center" prop="comboDirectoryName" :show-overflow-tooltip="true"/>
          <el-table-column label="所属主题"   align="center" prop="themeName" :show-overflow-tooltip="true"/>
          <el-table-column label="一件事编码" align="center" prop="comboDirectoryCode" :show-overflow-tooltip="true"/>
        <!--  <el-table-column label="服务对象" align="center" prop="comboServiceObject" />-->
          <el-table-column label="服务对象"  align="center" :show-overflow-tooltip="true">
            <template slot-scope="scope">
              <template v-if="scope.row.comboServiceObject.includes('1')">
                自然人
              </template>
              <template v-if="scope.row.comboServiceObject.includes('2')">
                企业法人
              </template>
              <template v-if="scope.row.comboServiceObject.includes('3')">
                事业法人
              </template>
              <template v-if="scope.row.comboServiceObject.includes('4')">
                社会组织法人
              </template>
              <template v-if="scope.row.comboServiceObject.includes('5')">
                非法人企业
              </template>
              <template v-if="scope.row.comboServiceObject.includes('6')">
                行政机关
              </template>
              <template v-if="scope.row.comboServiceObject.includes('9')">
                其他组织
              </template>
            </template>
          </el-table-column>
          <el-table-column label="所属区划"    align="center" prop="districtName" :show-overflow-tooltip="true"/>
          <el-table-column label="主办部门"    align="center" prop="mainOrganName" :show-overflow-tooltip="true"/>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="iconfont zfsoft-xiugai"  @click="handleInitTc(scope.row)" >配置</el-button>
            </template>
          </el-table-column>
        </el-table>
        <!--<pagination v-show="total > 0 && this.queryParams.cShow" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getTaocanList"/>-->

        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getListAll"/>

      </el-col>
    </el-row>
    <!-- 单个事项材料列表-->
    <el-dialog v-dialog-drag scrollbar height="600px" :close-on-click-modal="false" :title="title" :visible.sync="openInitSx" v-if="openInitSx" width="1070px" append-to-body>
          <el-form ref="form" :model="form" :rules="rules" label-width="0px" class="dialog-table">
              <el-table v-loading="loading" :data="form.serviceMaterialList" border>
                <el-table-column label="序号" width="65" type="index" align="center">
                  <template slot-scope="scope">
                    <span>{{ scope.$index + 1 }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="材料名称" align="center" width="300" prop="materialName"  />
                <el-table-column label="材料类型" show-overflow-tooltip="true" align="center" >
                  <template slot-scope="scope">
                    <p  v-if="scope.row.materialType===0">原件</p>
                    <p v-if="scope.row.materialType===1">复印件</p>
                    <p v-if="scope.row.materialType===2">原件和复印件</p>
                  </template>
                </el-table-column>
                <el-table-column label="材料形式" align="center" show-overflow-tooltip="true" >
                  <template slot-scope="scope">
                    <p  v-if="scope.row.materialFormat===1">纸质</p>
                    <p v-if="scope.row.materialFormat===2">电子版</p>
                  </template>
                </el-table-column>
                <el-table-column label="份数" align="center" width="70" prop="paperNumber"  />
                <el-table-column label="必要性" width="80" align="center" >
                  <template slot-scope="scope">
                    <template v-if="scope.row.mustFlag===0">
                      必须
                    </template>
                    <template v-if="scope.row.mustFlag===1">
                    非必须
                  </template>
                    <template v-if="scope.row.mustFlag===2">
                      容缺后补
                    </template>
                  </template>
                </el-table-column>
                <el-table-column label="审查要点" align="center" show-overflow-tooltip="true" prop="examinePoint"  />
                <el-table-column label="操作" align="center"  class-name="small-padding fixed-width">
                  <template slot-scope="scope">
                    <el-button size="mini" type="text" icon="iconfont zfsoft-xiugai"  @click="peizhi(scope.row,'SX')" >配置</el-button>
                  </template>
                </el-table-column>
            </el-table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelSx">关 闭</el-button>
      </div>
    </el-dialog>

   <!-- 一件事事项材料列表-->
    <el-dialog v-dialog-drag height="600px" scrollbar :close-on-click-modal="false" :title="title" :visible.sync="openInitTc" v-if="openInitTc" width="1020px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="0px" class="dialog-table">
        <el-table v-loading="loading" :data="form.serviceMaterList" border>
          <el-table-column label="序号" width="75" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column label="材料名称" align="center" width="300" prop="materialName"  />
          <el-table-column label="材料类型" width="100" align="center" prop="materialType"  >
            <template slot-scope="scope">
              <template v-if="scope.row.materialType===0">
                原件
              </template>
              <template v-if="scope.row.materialType===1">
                复印件
              </template>
              <template v-if="scope.row.materialType===2">
                原件和复印件
              </template>
            </template>
          </el-table-column>
          <el-table-column label="材料形式" align="center" width="120" prop="materialFormat"  >
            <template slot-scope="scope">
              <template v-if="scope.row.materialFormat===1">
                纸质
              </template>
              <template v-if="scope.row.materialFormat===2">
                电子版
              </template>
            </template>
          </el-table-column>
          <el-table-column label="份数" align="center" width="70" prop="paperNumber"  />
          <el-table-column label="必要性" width="80" align="center" >
            <template slot-scope="scope">
              <template v-if="scope.row.mustFlag===0">
                必须
              </template>
              <template v-if="scope.row.mustFlag===1">
                非必须
              </template>
              <template v-if="scope.row.mustFlag===2">
                容缺后补
              </template>
            </template>
          </el-table-column>
          <el-table-column label="配置状态" align="center" width="100" prop="checkFlag"  >
            <template slot-scope="scope">
              <template v-if="scope.row.checkFlag===0">
                未配置
              </template>
              <template v-if="scope.row.checkFlag===1">
                已配置
              </template>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center"  class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="iconfont zfsoft-xiugai"  @click="peizhi(scope.row,'TC')" >配置</el-button>
            </template>
          </el-table-column>
        </el-table>
       <!-- <el-form-item v-for="(item,index) in form.serviceNameList" >
          <div class="dealTaskItem">
            <i data-v-36a7fdc5="" class="el-icon-s-grid"></i> 事项：{{item}}
          </div>
          <el-table v-loading="loading" :data="form.serviceMaterList[index]">
            <el-table-column label="序号" width="65" type="index" align="center">
              <template slot-scope="scope">
                <span>{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column label="材料名称" align="center" width="330" prop="materialSampleName"  />
            <el-table-column label="材料形式" width="100" align="center" >
              <template slot-scope="scope">
                <template v-if="scope.row.materialFormat===0">
                  原件
                </template>
                <template v-if="scope.row.materialFormat===1">
                  复印件
                </template>
                <template v-if="scope.row.materialFormat===2">
                  原件或复印件
                </template>
              </template>
            </el-table-column>
            <el-table-column label="材料提交方式" align="center" width="120" prop="materialSpecification"  />
            <el-table-column label="份数" align="center" width="70" prop="paperNumber"  />
            <el-table-column label="必要性" width="80" align="center" >
              <template slot-scope="scope">
                <template v-if="scope.row.mustFlag===0">
                  必须
                </template>
                <template v-if="scope.row.mustFlag===1">
                  非必须
                </template>
              </template>
            </el-table-column>
            <el-table-column label="配置状态" align="center" width="90" prop="examinePoint"  />
            <el-table-column label="操作" align="center" width="80" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button size="mini" type="text" icon="iconfont zfsoft-xiugai"  @click="peizhi(scope.row)" v-hasPermi="['sys:reg:update']">配置</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-form-item>-->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelTc">关 闭</el-button>
      </div>
    </el-dialog>



    <el-dialog v-dialog-drag height="600px" scrollbar title="审查要点梳理编辑" :visible.sync="openAttaListView" width="1000px" append-to-body>
      <div class="app-container">
        <table cellspacing="0" cellpadding="0" border="0"  style="margin-bottom: 20px" class="zf-zc-table">
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="20%" />
            <col width="30%" />
          </colgroup>
          <tr>
            <td>
              <b>材料名称：</b>
            </td>
            <td  colspan="3">
              {{ materialSampleName }}
            </td>
          </tr>
        </table>

        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-upload
              :action="uploadUrl()"
              :on-error="uploadError"
              :file-list="fileList"
              :before-upload="beforeUpload"
              :on-success="uploadSuccess"
              accept="accept">
              <el-button type="primary" size="small">点击上传<i class="el-icon-upload el-icon--right"></i></el-button>
            </el-upload>
          </el-col>
        </el-row>

     <el-table v-loading="loading" :data="attaList" border>
          <el-table-column label="序号" width="55" type="index" align="center">
            <template slot-scope="scope">
              <span>{{scope.$index + 1}}</span>
            </template>
          </el-table-column>
          <el-table-column label="文件名" align="center" prop="attaName" />
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">

              <el-link type="primary" @click="downloadFile(scope.row.attaOid)">下载</el-link> |
              <el-link type="primary" @click="openExaminePointCardingView(scope.row)">预览</el-link> |
              <el-link type="primary" @click="openExaminePointCardingManage(scope.row)">编辑</el-link>|
              <el-link type="primary" @click="deleteFile(scope.row)">删除</el-link>
            </template>
          </el-table-column>
        </el-table>

      </div>
    </el-dialog>
    <!--审查要点配置-->
    <el-dialog v-dialog-drag title="审查要点配置" :visible.sync="initPointOptions"
               @close="closeEditView" width="60%"  append-to-body>
      <iframe :src=url  rameborder="0" width="100%" height="600px"></iframe>
      <div slot="footer" class="dialog-footer">
        <el-button @click="initPointOptions = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!--预览审查要点配置-->
    <el-dialog v-dialog-drag title="预览审查要点配置" :visible.sync="initPointOptionsView"
               @close="closeFileView" width="60%"  append-to-body>
      <iframe :src=fileViewurl  rameborder="0" width="100%" height="600px"  @father-click="closeFileView"  ></iframe>
      <div slot="footer" class="dialog-footer">
        <el-button @click="initPointOptionsView = false">关 闭</el-button>
      </div>
    </el-dialog>



    <el-dialog v-dialog-drag :visible.sync="item.show" v-for="item in materialComparisonOptions" @close="outerVisible"
               :title="item.title" width="1100px"  height="700px" scrollbar append-to-body>
    <!--  <iframe :src=fileViewurlList  rameborder="0" width="100%" height="600px"  @father-click="closeFileView"  ></iframe>-->
        <package-material-comparison :sampleInfoOid="item.sampleInfoOid"
                               :comboDirectoryOid="item.comboDirectoryOid"
                               :materiaOid="item.materiaOid"
                               :caseOid="item.caseOid"
                               :fileViewurl="item.fileViewurl"
                               :title="item.title"   @father-click="openTempletePic" >

        </package-material-comparison>
    </el-dialog>

  </div>
</template>

<script>
import { queryDirectoryMaterialList, page, getSxServiceOne, saveOrUpdate, getTaocanList, saveOrUpdateAhsSamplePicInfo, getAhsSamplePicInfoList, deleteAhsSamplePicInfo } from "@/api/zc/businessManagement/materialCheckKeyManage.js";
import { uploadFile } from "@/api/sys/atta";
import '@riophae/vue-treeselect/dist/vue-treeselect.css';
import packageMaterialComparison from '@/views/zc/businessManagement/materialCheckKeyManage/packageMaterialComparison';
import { getDictList } from "@/api/sys/dict";
export default {
  name: "MaterialCheckKeyManage",
  components: { packageMaterialComparison },
  data () {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 应用表格数据-事项
      sxServiceList: [],
      //应用表格数据-一件事
      taocanList: [],
      // 事项类型
      serviceTypeOptions: [],
      //材料比对页面
      materialComparisonOptions: [],
      //目录列表
      catalogList: [],
      // 弹出层标题
      title: "",
      // 新增/修改显示弹出层--事项
      openInitSx: false,
      // 新增/修改显示弹出层--一件事
      openInitTc: false,
      // 查看显示弹出层
      openView: false,
      //配置审查要点
      url: process.env.BASE_URL + 'bc/index.html',
      initPointOptions: false,
      //查看配置审查要点
      fileViewurl: '',
      fileViewurlList: '',
      initPointOptionsView: false,
      //配置
      openAttaListView: false,
      expandedKeys: [],
      userExpandedKeys: [],
      //事项类型
      typeOptions: [],
      //筛选事项列表
      serviceList: [],
      //事项列表主键
      serviceOids: "",
      //一件事列表主键
      comboDirectoryOid: "",
      //一件事事项
      serviceListHas: [],
      //单办事项
      serviceListHasNot: [],

      fileList: [],
      attaList: [],
      materialSampleName: '',
      viewDialogOptions: [],
      examinePointOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        serviceName: "",
        serviceTypeOidDict: "",
        implementCode: "",
        comboDirectoryName: "",
        comboDirectoryCode: "",
        serviceStatus: "3",
        status: '1',
        sShow: true,
        cShow: true

      },
      defaultProps: {
        children: "children",
        label: "label"
      },
      accept: {
        type: String,
        default: '.jpg,.jpeg,.png'
      },
      // 表单参数
      form: {
        id: '', //逻辑主键
        serviceOid: "",
        implementCode: "",
        serviceName: "",
        serviceMaterList: [],
        serviceMaterialList: [],
        materialCatalogOid: [],
        catalogName: [],
        serviceNameList: [],

      },
      form1: {
        serviceOid: "",
        materialOids: [],
        materialCatalogOids: []

      },
      ahsSamplePicInfo: {
        id: '', //逻辑主键
      },
      // 表单校验
      rules: {},
    };
  },
  created () {
    this.getList();
    this.queryParams.sShow = true;
    //this.queryParams.cShow=true;
    this.queryParams.serviceTypeOidDict = "dbsx";
  },
  methods: {
    /*     getDounload(attaOid).then(response => {
            this.taocanList = response.data.data;
            this.total = response.data.total;
            this.loading = false;
          });
        sysAttaService/download*/
    getListAll () {
      if (this.queryParams.serviceTypeOidDict == 'tcsx') {
        this.getTaocanList();
        this.sxServiceList = [];
      } else if (this.queryParams.serviceTypeOidDict == 'dbsx') {
        this.getList();
        this.taocanList = [];
      }
    },
    //获取一件事目录列表
    getTaocanList () {
      this.queryParams.sShow = false;
      this.queryParams.cShow = true;
      this.loading = true;
      getTaocanList(this.queryParams).then(response => {
        this.taocanList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      });
      console.log(this.queryParams)
    },
    selectChanged (value) {
      this.queryParams.pageNum = 1;
      //获取一件事事项
      if (value == "tcsx") {
        this.queryParams.sShow = false;
        this.queryParams.cShow = true;
        this.queryParams.serviceName = "";
        this.queryParams.implementCode = "";
        this.getTaocanList();
      } else if (value == "dbsx") { //获取单办事项
        this.queryParams.sShow = true;
        this.queryParams.cShow = false;
        this.queryParams.comboDirectoryName = "";
        this.queryParams.comboDirectoryCode = "";
        this.getList();
      }
    },
    /** 查询事项列表 */
    getList () {
      this.queryParams.sShow = true;
      this.queryParams.cShow = false;
      this.loading = true;
      page(this.queryParams).then(response => {
        this.sxServiceList = response.data.data;
        this.total = response.data.total;
        this.loading = false;
      });
      console.log(this.queryParams)
    },
    //事项类型
    getServiceTypt () {
      getDictList('SCSXLX').then(response => {
        this.typeOptions = response.data;
      }
      )
    },
    //配置样表
    peizhi (row, sxOrTc) {
      /* alert(JSON.stringify(row))
       console.log(row)*/
      /*  alert(this.ahsSamplePicInfo.sampleInfoOid)*/
      let aspi = {};//查询配置样表的参数
      this.openAttaListView = true;
      if (sxOrTc == 'TC') {//一件事
        this.materialSampleName = row.materialName;
        this.ahsSamplePicInfo.comboDirectoryOid = row.comboDirectoryOid;
        aspi.comboDirectoryOid = row.comboDirectoryOid;
      } else {//事项
        this.materialSampleName = row.materialSampleName;
        this.ahsSamplePicInfo.sampleInfoOid = row.serviceOid;
        aspi.sampleInfoOid = row.serviceOid;
      }
      this.ahsSamplePicInfo.materiaOid = row.oid;
      aspi.materiaOid = row.materialOid;
      //alert(JSON.stringify(this.ahsSamplePicInfo))
      console.log(JSON.stringify(this.ahsSamplePicInfo))
      this.getaspiList(this.ahsSamplePicInfo);
    },
    //获取样表列表
    getaspiList (aspi) {
      getAhsSamplePicInfoList(aspi).then(response => {
        this.attaList = response.data.ahsSamplePicInfoList;
      });

    },
    // 取消按钮--事项
    cancelSx () {
      this.openInitSx = false;
      //this.reset();
    },
    // 取消按钮--一件事
    cancelTc () {
      this.openInitTc = false;
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
      if (this.queryParams.serviceTypeOidDict == 'tcsx') {
        this.getTaocanList();
        this.sxServiceList = [];
      } else if (this.queryParams.serviceTypeOidDict == 'dbsx') {
        this.getList();
        this.taocanList = [];
      }

    },
    /** 重置按钮操作 */
    resetQuery () {
      let chooseType = this.queryParams.serviceTypeOidDict;
      this.resetForm("queryForm");
      this.queryParams.serviceTypeOidDict = chooseType;
      this.handleQuery();

    },
    /** 查看按钮操作 */
    handleView (row) {
      let _that = this;
      _that.reset();
      const oid = row.serviceOid;
      getSxServiceOne(oid).then(response => {
        _that.form = response.data.sxService;
        _that.openView = true;
        _that.title = "查看事项信息";
      });
    },
    /** 新增和修改按钮操作 --事项*/
    handleInitSx (row) {
      let _that = this;
      _that.reset();
      if (row.serviceOid) {
        _that.form.serviceMaterList = [];
        _that.form.serviceMaterialList = [];
        getSxServiceOne(row.serviceOid).then(response => {
          _that.openInitSx = true;
          _that.form.serviceOid = response.data.sxService.serviceOid;
          _that.form.serviceName = response.data.sxService.serviceName;
          _that.form.implementCode = response.data.sxService.implementCode;
          _that.form.serviceMaterList = response.data.sxServiceMaterials;
          _that.form.serviceMaterialList = response.data.serviceMaterialList;
          /* alert( _that.form.serviceMaterialList)*/
          let materList = _that.form.serviceMaterList;
          /* for(let mater of materList){
             //this.form.materialCatalogOid.push(mater.materialOid);//这个在材料表加完字段后改为下面一行的代码
             this.form.materialCatalogOid.push(mater.materialCatalogOid);
           }*/
        });

      } else {
        _that.openInitSx = false;
      }
      _that.title = "事项材料审查要点配置";

    },
    /** 新增和修改按钮操作 --一件事*/
    handleInitTc (row) {
      let _that = this;
      _that.reset();
      if (row.comboDirectoryOid) {
        queryDirectoryMaterialList(row.comboDirectoryOid).then(response => {
          this.openInitTc = true;
          this.form.serviceMaterList = response.data;

        })


        /*      pageServiceOidList(row.comboDirectoryOid).then(response => {
                this.openInitTc = true;
                let comboServiceList= response.data;
                if(comboServiceList==''){
                  _that.$message.error('一件事目录未关联事项!');
                  _that.openInitTc = false;
                }else{
                  for(let comboService of comboServiceList){
                    getSxServiceOne(comboService.serviceOid).then(response1 => {
                      let serviceNameGet= response1.data.sxService.serviceName;
                      let sxServiceMaterialsGet=  response1.data.sxServiceMaterials;
                      // let json={"serviceNameGet":serviceNameGet,"sxServiceMaterialsGet":sxServiceMaterialsGet};
                      this.form.serviceMaterList.push(sxServiceMaterialsGet);
                      this.form.serviceNameList.push(serviceNameGet);
                    });
                  }
                }
              });*/
      } else {
        _that.openInitTc = false;
      }
      _that.title = "一件事材料审查要点配置";

    },
    /** 提交按钮 */
    submitForm: function () {
      let _that = this;
      this.form1.serviceOid = _that.form.serviceOid;
      this.form1.comboDirectoryOid = _that.form.comboDirectoryOid;
      let materList = _that.form.serviceMaterialList;
      /* let materialCatalogOidList=  _that.form.materialCatalogOid;
       for(let catalogOid of materialCatalogOidList){
         if(catalogOid==null){
           catalogOid="";
         }
         this.form1.materialCatalogOids.push(catalogOid);
       }
       for(let mater of materList){
         this.form1.materialOids.push(mater.materialOid);
       }*/
      console.log(_that.form1)

      return;
      saveOrUpdate(_that.form1).then(response => {
        if (response.code === 200) {
          //如果关联目录发生变化，删除已配置的验证规则 这个功能没有做
          _that.msgSuccess("保存成功");
          _that.addDialogShow = false;
          setTimeout(() => {
            _that.getList();
            _that.openInitSx = false;
          }, 10);
        }
      });
    },
    //选择配置项附件
    selectAttas () {
      this.getAttaList();
      this.openAttaListView = true;
    },
    //下载附件
    downloadFile (attaOid) {
      this.download(attaOid);
    },
    //预览附件
    viewFileNew (attaOid) {
      let item = { show: true, attaOid: attaOid };
      this.viewDialogOptions.push(item);
    },
    iconByType (path) {
      return path.substring(path.lastIndexOf(".") + 1, path.length);
    },
    //关闭预览附件
    /* closeFileView(){
       this.viewDialogOptions.pop();
     },*/
    //成功后返回
    uploadSuccess (resp) {
      this.fileList = [];
      let type = this.iconByType(resp.data.name);
      if (type == 'jpg' || type == 'JPG' || type == 'jpeg' || type == 'JPEG' || type == 'png' || type == 'PNG') {
        this.ahsSamplePicInfo.attaOid = resp.data.oid;
        this.ahsSamplePicInfo.attaName = resp.data.name;
        this.ahsSamplePicInfo.comparePicFile = resp.data.url;
        saveOrUpdateAhsSamplePicInfo(this.ahsSamplePicInfo).then(response => {
          if (response.code === 200) {
            this.msgSuccess("保存成功");
            setTimeout(() => {
              this.getaspiList(this.ahsSamplePicInfo);
            }, 10);

          }
        });
      }

    },
    //失败后返回
    uploadError (resp) {
      this.msgError("文件上传失败");
    },
    uploadUrl () {
      return uploadFile();
    },
    //上传之前
    beforeUpload (file) {
      if (file.name.indexOf("%00") > -1 || file.name.indexOf("./") > -1 || file.name.indexOf(".\\") > -1) {
        this.msgError('上传文件名称非法！');
        return false;
      }
      const fileSize = file.size;
      if (0 == fileSize) {
        this.msgError('上传文件不能为空！');
        return false;
      }
      const isLt2M = file.size / 1024 / 1024 < 100;
      if (!isLt2M) {
        this.msgError('上传文件大小不能超过 100MB！');
      }
      const type = this.iconByType(file.name);
      if (type == 'jpg' || type == 'JPG' || type == 'jpeg' || type == 'JPEG' || type == 'png' || type == 'PNG') {
      } else {
        this.msgError('请上传图片类型的附件');
      }
      return isLt2M;
    },
    //删除样表
    deleteFile (row) {
      const oid = row.id;
      this.$confirm("是否确认删除?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(function () {
          return deleteAhsSamplePicInfo(oid);
        })
        .then(() => {
          this.getaspiList(this.ahsSamplePicInfo);
          this.msgSuccess("删除成功");
        })
        .catch(function () { });
    },


    //打开验证规则配置页面
    openExaminePointCardingManage (item) {
      let materiaOid = item.materiaOid;
      let serviceOid = item.sampleInfoOid;
      let ahsSamplePicInfoOid = item.ahsSamplePicInfoOid;
      let attaOid = item.attaOid;
      let comboDirectoryOid = item.comboDirectoryOid;
      this.url = process.env.BASE_URL + 'picture/edit.html?serviceOid=' + serviceOid + '&materiaOid=' + materiaOid + '&attaOid=' + attaOid + '&ahsSamplePicInfoOid=' + ahsSamplePicInfoOid + '&comboDirectoryOid=' + comboDirectoryOid,
        window.open(this.url);
    },
    //打开审查要点配置详细
    openExaminePointCardingView (item) {
      let materiaOid = item.materiaOid;
      let serviceOid = item.sampleInfoOid;
      let ahsSamplePicInfoOid = item.ahsSamplePicInfoOid;
      let attaOid = item.attaOid;
      let comboDirectoryOid = item.comboDirectoryOid;
      this.fileViewurl = process.env.BASE_URL + 'picture/prview.html?serviceOid=' + serviceOid + '&materiaOid=' + materiaOid + '&attaOid=' + attaOid + '&ahsSamplePicInfoOid=' + ahsSamplePicInfoOid + '&comboDirectoryOid=' + comboDirectoryOid,
        this.initPointOptionsView = true;
      /* window.open(fileViewurl.url);*/
    },
    closeEditView () {
      this.initPointOptions = false;
    },
    closeFileView () {
      this.initPointOptionsView = false;
    },

    //打开材料比对页面
    openMaterialComparison () {
      let sampleInfoOid = '297ea8f9738ee02c01738f27706f0098';
      let comboDirectoryOid = '';
      let materiaOid = '297ea8f9738ee02c01738f28ad71009f';
      let caseOid = "";
      let fileViewurls = process.env.BASE_URL + 'picture/prviewList.html?materiaOid=' + materiaOid + '&sampleInfoOid=' + sampleInfoOid + '&comboDirectoryOid=' + comboDirectoryOid;
      this.fileViewurlList = fileViewurls;
      let item = { show: true, title: '材料比对', sampleInfoOid: sampleInfoOid, comboDirectoryOid: comboDirectoryOid, materiaOid: materiaOid, fileViewurl: fileViewurls, caseOid: caseOid };
      this.materialComparisonOptions.push(item);
    }

  },
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
.primary-table h3 {
  font-size: 14px;
  font-weight: normal;
  color: #333;
  margin: 0px 0px 10px 0px;
}
.primary-table {
  padding: 20px;
  box-sizing: border-box;
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
  width: 130px;
  margin-right: 0;
  margin-left: 14px;
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
.el-select {
  width: 90%;
}
</style>
