/**
* @Author: liangxm
* @Date: 2020-12-02
* @Description: 补齐补正
*/
<template>
  <div class="app-container">
    <!--列表数据-->
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="70px">
      <el-form-item label="申请人" prop="applyUserName">
        <el-input v-model.trim="queryParams.applyUserName" placeholder="请输入申请人" clearable size="100"
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="办件编号" prop="caseNumber">
        <div class="card-item">
          <el-input v-model.trim="queryParams.caseNumber" placeholder="请输入办件编号" clearable size="100"
            @keyup.enter.native="handleQuery">
            <!-- <template slot="append">
              <el-button class="scan-btn" type="primary" @click="openScan" />
            </template> -->
          </el-input>
        </div>
      </el-form-item>
      <el-form-item label="申请项目名称" prop="projectName" label-width="108px">
        <el-input v-model.trim="queryParams.projectName" placeholder="请输入申请项目名称" clearable size="100"
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item class="fr mr0">
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button type="warning" icon="el-icon-refresh" size="mini" @click="resetQuery" class="btn-reset">重置
        </el-button>
      </el-form-item>
    </el-form>
    <!-- 列表信息-->
    <el-table :data="caseRegList" v-loading="loading" border :fit="true">
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="申请项目名称" align="center" prop="projectName" :show-overflow-tooltip="true" />
      <el-table-column label="办件编号" align="center" prop="caseNumber" :show-overflow-tooltip="true" />

      <el-table-column label="申请人" align="center" prop="applyUserName" :show-overflow-tooltip="true" />
      <el-table-column label="证件号" align="center" prop="credentialNumber" :show-overflow-tooltip="true" />
      <el-table-column label="登记日期" align="center" prop="createDate" :show-overflow-tooltip="true" />
      <el-table-column label="补正时限" align="center" prop="rqhbTime" :show-overflow-tooltip="true" />
      <el-table-column label="补正是否超期" align="center" prop="expireFlag">
        <template slot-scope="scope">
          <p v-if="scope.row.expireFlag == 0">超期</p>
          <p v-if="scope.row.expireFlag == 1">未超期</p>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="180" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="iconfont zfsoft-chakan" @click="viewAccept(scope.row)"
            v-hasPermi="['sys:caseRqbz:view']">查看</el-button>
          <el-button v-if="scope.row.expireFlag == 1" size="mini" type="text" icon="iconfont zfsoft-xiugai"
            @click="handleInit(scope.row)" v-hasPermi="['sys:caseRqbz:init']">补正</el-button>
          <el-button v-if="scope.row.expireFlag == 0" size="mini" type="text" icon="iconfont zfsoft-xiugai"
            @click="handleInit(scope.row)" v-hasPermi="['sys:caseRqbz:init']">作废</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />
    <!-- 办件材料信息 -->
    <el-dialog v-dialog-drag :close-on-click-modal="false" :title="dialogTitle" :visible.sync="openInit" width="900px"
      append-to-body>
      <div>
        <div class="zf-zc-table--title">容缺材料信息</div>
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table--common zf-zc-table--td-center">
          <colgroup>
            <col width="10%" />
            <col width="20%" />
            <col width="10%" />
            <col width="60%" />
          </colgroup>
          <tr>
            <th>序号</th>
            <th>材料名称</th>
            <th>收取份数</th>
            <th>收取方式</th>
          </tr>
          <tbody v-for="(data, index) in caseMaterials" :key="index">
            <tr>
              <td style="text-align: center">{{ index + 1 }}</td>
              <td>{{ data.materialName }}</td>
              <td style="text-align: center">{{ data.collectionNumber }}</td>
              <td>
                <el-radio-group v-model="data.collectionType" @change="handleRowChange(data, index)">
                  <el-radio label="1">纸质收取</el-radio>
                  <el-radio label="2">附件上传</el-radio>
                  <el-radio label="3">扫描</el-radio>
                  <el-radio label="5">证照库</el-radio>
                </el-radio-group>
              </td>
            </tr>

            <tr v-if="
                data.collectionType == 2 ||
                data.collectionType == 3 ||
                data.collectionType == 5
              ">
              <td colspan="5" style="background-color: #fafbfc">
                <div class="combo-handle-data">
                  <div class="handle-data-list">
                    <div class="handle-data-list-item" v-for="(dataAtta, idx) in attaList[index]" :key="idx">
                      <div class="grid-content qdcg-text">
                        <div v-if="data.collectionType == 5" @click="viewElemsInfo(dataAtta.attaOid)"
                          class="handle-data-list-item--link">
                          {{ dataAtta.elecLicenName }} -
                          {{ dataAtta.elecLicenNumber }}
                        </div>
                        <div v-else @click="viewFile(dataAtta.attaOid)" class="handle-data-list-item--link">
                          材料名称 - {{ dataAtta.originName }}
                        </div>
                      </div>
                      <div>
                        <el-button type="text" icon="el-icon-view" v-if="
                            data.collectionType == 2 || data.collectionType == 3
                          " @click="viewFile(dataAtta.attaOid)">
                          查看
                        </el-button>
                        <el-button type="text" icon="el-icon-delete" v-if="
                            data.collectionType == 2 || data.collectionType == 3
                          " @click="
                            deleteMaterialAtta(
                              data.caseMaterialOid,
                              dataAtta.attaOid,
                              index
                            )
                          ">
                          删除
                        </el-button>
                        <el-button type="text" icon="el-icon-view" v-if="data.collectionType == 5"
                          @click="viewElemsInfo(dataAtta.attaOid)">
                          查看
                        </el-button>
                      </div>
                    </div>
                  </div>

                  <el-upload class="upload-demo" action="" :http-request="uploadFiles" :before-upload="beforeUpload"
                    :on-error="uploadError" :file-list="fileList" :show-file-list="showFileList" :accept="accept">
                    <el-button v-if="data.collectionType == 2" size="mini" type="primary" icon="el-icon-upload"
                      @click="pushMaterialOid(data.caseMaterialOid, index)">
                      点击上传
                    </el-button>
                  </el-upload>
                  <el-button v-if="data.collectionType == 3" size="mini" type="primary" icon="el-icon-upload"
                    @click="openGPYDialog(data, index)">
                    点击扫描
                  </el-button>
                  <el-button v-if="data.collectionType == 5" size="mini" type="primary" icon="el-icon-upload" @click="
                      getElecLicenInfo(
                        data.materialName,
                        data.materialOid,
                        data.caseMaterialOid,
                        data.sourceType,
                        index
                      )
                    ">
                    获取证照
                  </el-button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 一件事办件查看开始 -->
    <el-dialog v-dialog-drag :visible.sync="caseOpenView" v-if="caseOpenView" @close="closeOpenView" :title="title"
      width="1100px" height="700px" scrollbar append-to-body>
      <view-combo-case-new :caseOid="indexCaseOid" @view-case="closeOpenView"></view-combo-case-new>
    </el-dialog>
    <!--一件事办件查看结束-->

    <!--引入文件的预览弹出层-->
    <el-dialog v-dialog-drag title="文件预览" :visible.sync="viewFileShow" v-if="viewFileShow" width="1100px"
      append-to-body>
      <combo-case-file-view :attaOid="fileAttaOid" @father-click="closeFileView"></combo-case-file-view>
    </el-dialog>

    <GPYUpload v-if="dialogVisible" isOnethingRqbz :dialogVisible.sync="dialogVisible" :qlCaseMaterial="qlCaseMaterial"
      @sendUploadFilesList="getUploadFilesList" />
  </div>
</template>

<script>
  import {
    page,
    saveOrUpdate,
    rqhbMaterialByCaseOid,
    checkTimeExpire,
    getPubMaterialInfo
  } from "@/api/onething/comboManager/comboAccept/combocaseRqbz";
  import viewComboCase from "@/views/onething/comboManager/comboAccept/viewComboCase";
  import {
    uploadCaseMaterialFile
  } from "@/api/onething/comboManager/comboAccept/initComboCase";
  import {
    uploadFileByPaths
  } from "@/api/zc/businessManagement/fileUpload";
  import {
    getElecLicenUrl,
    queryElecLicenseByDirCode
  } from "@/api/zc/businessManagement/elemLice";
  import {
    getComboCaseByOid
  } from "@/api/onething/comboManager/comboAccept/caseBqbz";
  import comboCaseFileView from "@/views/onething/comboManager/comboAccept/comboCaseFileView";
  import {
    breakComboCase,
    dishonestPersonByUser,
    checkApplyUserInDishonest,
    enterDishonestPerson
  } from "@/api/onething/comboManager/comboAccept/tempComboAccept";
  import {
    saveOut
  } from "@/api/onething/comboManager/comboAccept/initComboCase";
  import ViewComboCaseNew from "@/views/onething/comboManager/comboAccept/viewComboCaseNew";
  import {
    activeScanningGun,
    openScanningGun
  } from "@/api/zc/businessManagement/charge";
  /** 高拍仪 */
  import GPYUpload from "@/views/components/gpy-dialog/index.vue";
  export default {
    name: "CombocaseRqbz",
    components: {
      ViewComboCaseNew,
      viewComboCase,
      comboCaseFileView,
      GPYUpload
    },
    props: {
      fileList: null,
      accept: {
        type: String,
        default: '.jpg,.jpeg,.png,.pdf,.PDF,.doc,.docx,.DOC,.DOCX'
      },
    },
    data() {
      return {
        total: 0,
        // 列表数据
        caseRegList: [],
        tableData: [],
        formData: {},
        // 办件查看
        caseOpenView: false,
        indexCaseOid: '',
        //附件集合
        attaList: [],
        viewFileShow: false,
        fileAttaOid: '',
        //材料与上传成功的附件集合
        materialAttaList: [],
        materialCollectionType: [],
        attr_tr_info_: false,
        //材料业务主键
        materialOid: null,
        rowNum: 1,
        showFileList: false,
        // 弹窗标题
        dialogTitle: '',
        openView: false,
        // 新增/修改显示弹出层
        openInit: false,
        caseMaterials: [],
        elemLicenseList: [],
        applyPerson: {},
        labelPosition: "top",
        attaIndex: "",
        // 表单参数
        form: {
          caseOid: '',
          materialAtta: [],
          materialCollect: [],
          elemLicense: [],
          comboDireOid: "",
        },
        // 表单校验
        rules: {
          /*resultDesc: [
            { required: true, message: "意见说明不能为空", trigger: "blur" }
          ]*/
        },

        indexCaseNumber: "",
        // 预审状态
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          caseNumber: "",
          caseStatus: '2',
          projectName: ""
        },

        dialogVisible: false,
        qlCaseMaterial: {},

        uploadFilesList: []
      };
    },
    created() {
      this.initQuery();
      this.getList();
    },
    destroyed() {
      //在离开此页面的时候主动关闭socket
      this.socketApi.webSocketClose();
    },
    methods: {
      initQuery() {
        this.queryParams.caseNumber = this.$route.query.caseNumber;
      },
      handleRowChange(data, index) {
        this.materialCollectionType.forEach((item, i) => {
          //判断材料主键是否已经存在，存在移除
          if (item.materialOid == data.caseMaterialOid) {
            this.materialCollectionType.splice(i, 1);
          }
        })
        let list = {};
        list.materialOid = data.caseMaterialOid;
        list.collectionType = data.collectionType;
        this.materialCollectionType.push(list);
        //清空附件信息
        this.materialAttaList.forEach((att, j) => {
          if (att.materialOid == data.caseMaterialOid) {
            this.materialAttaList.splice(j, 1);
          }
        })
        this.$set(this.attaList, index, []);
        //清除电子证照信息
        this.elemLicenseList.forEach((ite, i) => {
          if (ite.materialOid == data.caseMaterialOid) {
            //移除原来的重新赋值
            this.elemLicenseList.splice(i, 1);
          }
        })
      },
      pushMaterialOid(materialOid, index) {
        this.materialOid = materialOid;
        this.attaIndex = index; //标识材料索引
      },
      /** 保存材料附件信息 */
      saveMaterialAtta() {
        if (this.fileList.length == 0) {
          this.msgInfo("请至少上传附件");
          return false;
        }
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },
      // 取消按钮
      cancel() {
        this.openInit = false;
        this.reset();
      },
      /** 查询列表 */
      getList() {
        this.loading = true;
        page(this.queryParams).then(response => {
          this.caseRegList = response.data.data;
          this.total = response.data.total;
          this.loading = false;
        });
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.$route.query.caseNumber = "";
        this.queryParams.caseNumber = "";
        this.handleQuery();
      },
      // 表单重置
      reset() {
        Object.assign(this.form, this.$options.data().form)
        this.resetForm("form");
      },
      /** 查看按钮操作 */
      handleView(row) {
        this.indexCaseNumber = row.caseNumber;
        this.openView = true;
        this.dialogTitle = "查看详情";
      },
      /** 办件查看按钮操作 */
      viewAccept(row) {
        this.indexCaseOid = row.caseOid;
        this.caseOpenView = true;
        this.title = "一件事办件信息";
      },
      /** 办件查看关闭按钮操作 */
      closeOpenView() {
        this.caseOpenView = false;
      },
      //预览附件
      viewFile(attaOid) {
        this.fileAttaOid = attaOid;
        this.viewFileShow = true;
      },

      //关闭预览附件
      closeFileView() {
        this.viewFileShow = false;
      },
      /** 出库操作 */
      handleInit(row) {
        this.indexCaseNumber = row.caseNumber;
        if (row.expireFlag == 0) {
          this.$confirm('此办件容缺补正时间已超期，是否确认作废办件？', "警告", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(() => {
            breakComboCase(row.caseOid).then(res => {
              if (res.data != "") {
                this.msgSuccess("办件作废成功！");
                this.getList();
                //添加失信人
                this.dishonestPersonList(row.caseOid);
              }
            })
          });
        } else {
          this.form.caseOid = row.caseOid;
          getComboCaseByOid(row.caseOid).then(response => {
            this.applyPerson = response.data;
            this.form.comboDireOid = response.data.comboDireOid;
            //判断申请人是否是失信人员，失信人员不予补正
            checkApplyUserInDishonest(this.applyPerson.applyUserName, this.applyPerson.credentialNumber).then(
              response => {
                if (response.data && response.data.length > 0) {
                  this.$message.error("失信人员不予补正")
                  return false;
                }
                rqhbMaterialByCaseOid(row.caseOid).then(response => {
                  this.caseMaterials = response.data;
                });
                this.openInit = true;
                this.dialogTitle = "容缺后补";
              })
          })
        }
      },
      /** 失信人名单 */
      dishonestPersonList(caseOid) {
        enterDishonestPerson(caseOid).then(response => {
          if (response.data != "") {
            this.msgSuccess("该申请人被纳入失信人名单！");
          }
        })
      },
      /** 提交按钮 */
      submitForm() {
        console.log(this.form)
        let saveOutFlag = false; //保存材料出库信息的标识
        let materialOids = "";
        this.form.materialAtta = this.materialAttaList;
        this.form.materialCollect = this.materialCollectionType;
        this.form.elemLicense = this.elemLicenseList;
        if (this.materialCollectionType.length > 0) {
          for (let j = 0; j < this.materialCollectionType.length; j++) {
            materialOids += this.materialCollectionType[j].materialOid + ";";
            let type = this.materialCollectionType[j];
            let mustAttaFlag = false;
            if (type.collectionType) {
              //看是否为纸质，则无材料
              if (type.collectionType == 1) {
                //无材料
                saveOutFlag = true;
                mustAttaFlag = true;
              } else if (type.collectionType == 2) {
                if (this.materialAttaList.length > 0) {
                  for (let k = 0; k < this.materialAttaList.length; k++) {
                    const atta = this.materialAttaList[k];
                    if (atta.materialOid == type.materialOid) {
                      mustAttaFlag = true;
                      break;
                    }
                  }
                }
              } else if (type.collectionType == 3) {
                if (this.uploadFilesList.length > 0) {
                  for (let k = 0; k < this.uploadFilesList.length; k++) {
                    const atta = this.uploadFilesList[k];
                    if (atta.caseMaterialOid == type.materialOid) {
                      mustAttaFlag = true;
                      break;
                    }
                  }
                }
              } else if (type.collectionType == 4) {
                mustAttaFlag = true;
                //容缺后补
              } else if (type.collectionType == 5) { //电子证照
                if (this.elemLicenseList.length > 0) {
                  for (let k = 0; k < this.elemLicenseList.length; k++) {
                    const atta = this.elemLicenseList[k];
                    if (atta.materialOid == type.materialOid) {
                      mustAttaFlag = true;
                      break;
                    }
                  }
                }
              }
            } else {
              this.$message.error("请选择材料收取方式!");
              return false;
            }
            if (mustAttaFlag == false) {
              this.$message.error("材料未收取!");
              return false;
            }
          }
        } else {
          this.$message.error("请选择材料收取方式!");
          return false;
        }
        saveOrUpdate(this.form).then(response => {
          if (response.code === 200) {
            this.msgSuccess("保存成功");
            this.openInit = false;
            //判断如果有纸质收取生成出库材料信息记录
            if (saveOutFlag) {
              this.saveMaterialOut(materialOids);
            }
            setTimeout(() => {
              this.getList();
            }, 10);
          }
        });
      },
      /** 综窗保存材料出库信息 */
      saveMaterialOut(materialOids) {
        let outMaterial = {};
        outMaterial.caseOid = this.form.caseOid;
        outMaterial.caseNumber = this.indexCaseNumber;
        outMaterial.applyUserName = this.applyPerson.applyUserName;
        outMaterial.comboDirOid = this.form.comboDireOid;
        // outMaterial.serviceTypeName=this.form.serviceTypeName;
        outMaterial.cardNumber = this.applyPerson.credentialNumber;
        outMaterial.materialOids = materialOids;
        saveOut(outMaterial).then(response => {
          if (response.data == "") {
            this.msgError("材料出库信息保存失败！");
            return false;
          }
        })
      },
      //失败后返回
      uploadError(resp) {
        this.msgError("文件上传失败");
      },
      /** 上传附件 */
      uploadFiles(file) {
        let formData = new FormData();
        formData.append("files", file.file);
        uploadCaseMaterialFile(formData).then(response => {
          if (response.data != "") {
            let resultAtta = [];
            if (this.attaList.length > 0) {
              if (this.attaList[this.attaIndex]) {
                resultAtta = [].concat(this.attaList[this.attaIndex]);
              } else {}
            }
            response.data.forEach((data, index) => {
              resultAtta.push(data); //存放每一个上传的附件
              //放置材料与附件的关系,生成多条记录
              let list = {};
              list.materialOid = this.materialOid;
              list.attaOid = data.oid;
              this.materialAttaList.push(list);
            });
            console.log(this.materialAttaList)
            this.attaList[this.attaIndex] = [].concat(resultAtta);
            this.$forceUpdate(); //强制视图刷新
          }
        })
      },
      //上传之前
      beforeUpload(file) {
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
        return isLt2M;
      },
      //删除附件信息
      deleteMaterialAtta(caseMaterialOid, attaOid, index) {
        this.materialAttaList.forEach((item, i) => {
          //判断材料主键是否已经存在，存在移除
          if (item.materialOid == caseMaterialOid && item.attaOid == attaOid) {
            this.materialAttaList.splice(i, 1);
          }
          //剔除附件list中的附件
          if (this.attaList[index]) {
            //剔除附件信息重新加载页面
            this.attaList[index].forEach((ind, i) => {
              if (ind.oid == attaOid) {
                this.attaList[index].splice(i, 1);
              }
            })
          }
        })
        this.$forceUpdate(); //强制更新视图
      },
      base64ToFile(urlData, fileName) {
        let arr = urlData.split(',');
        // let mime = arr[0].match(/:(.*?);/)[1];
        let bytes = atob(arr[0]); // 解码base64
        let n = bytes.length
        let ia = new Uint8Array(n);
        while (n--) {
          ia[n] = bytes.charCodeAt(n);
        }
        return new File([ia], fileName, {
          type: 'image/jpeg'
        });
      },
      scanCard(caseMaterialOid, index) {
        //caseMaterialOid--所属材料主键
        //index--所属操作行索引
        //Device：设备类型、
        let info = '{"device":"HighCamera", "type":"' + caseMaterialOid + ',' + index + '"}';
        //建立socket连接
        this.socketApi.initWebSocket(this.socketError);
        this.socketApi.sendSock(info, this.getConfigResult);
      },
      // 接收socket回调函数返回数据的方法
      getConfigResult(data) {
        if (data.status == 0) {
          //高拍仪
          if (data.type) {
            let types = data.type.split(",");
            //所属材料主键
            let materialOid = types[0];
            //扫描所属行索引
            let attaIndex = types[1];
            if (data.device == "HighCamera") {
              if (data.content.Cameras64) {
                let base64s = data.content.Cameras64.split(",");
                for (let i = 0; i < base64s.length; i++) {
                  if (!base64s[i]) {
                    continue;
                  }
                  let file = this.base64ToFile(base64s[i], "scanPicture" + i + ".jpg");
                  let formD = new FormData();
                  formD.append("files", file);
                  uploadCaseMaterialFile(formD).then(response => {
                    if (response.data != "") {
                      let resultAtta = [];
                      if (this.attaList.length > 0) {
                        if (this.attaList[attaIndex]) {
                          resultAtta = [].concat(this.attaList[attaIndex]);
                        } else {}
                      }
                      response.data.forEach((data, index) => {
                        resultAtta.push(data); //存放每一个上传的附件
                        //放置材料与附件的关系,生成多条记录
                        let list = {};
                        list.materialOid = materialOid;
                        list.attaOid = data.oid;
                        this.materialAttaList.push(list);
                      });
                      this.attaList[attaIndex] = [].concat(resultAtta);
                      this.$forceUpdate(); //强制视图刷新
                    }
                  })
                }
              }
            }
          }
        }
        if (data.status == 1) {
          this.$message.error(data.message)
        }
      },
      //初始化socket发生错误回调
      socketError() {
        this.$message.error("请检查设备或连接是否正常")
      },
      getElecLicenInfo(materialName, materialOid, caseMaterialOid, sourceType, index) {
        let userName = "";
        let idCard = "";
        idCard = this.applyPerson.credentialNumber;
        userName = this.applyPerson.applyUserName;
        if (userName && idCard) {
          if (sourceType != 1) { //公共材料
            //查询公共材料信息获取证照目录
            getPubMaterialInfo(materialOid).then(response => {
              let pubMaterial = response.data || {};
              if (pubMaterial.elecBillOid) {
                this.setElec(materialName, materialOid, userName, idCard, caseMaterialOid, index, pubMaterial
                  .elecBillOid);
              } else {
                this.$message.warning(
                  `未查到姓名【${userName}】,证照号【${idCard}】下材料名为【${materialName}】的证照信息`
                );
                return;
              }
            })
          } else { //不是公共材料
            this.setElec(materialName, materialOid, userName, idCard, caseMaterialOid, index);
          };
        } else {
          this.$message.warning("申请人/申请单位和证件号不能为空！");
          return;
        }
      },
      setElec(materialName, materialOid, userName, idCard, caseMaterialOid, index, elecBillOid = "") {
        queryElecLicenseByDirCode(materialOid, userName, idCard, elecBillOid).then(
          response => {
            if (response.code !== 200) {
              this.$message.error("请检查证照相关配置！");
              return;
            }

            if (response.data) {
              let res = {};
              let eleArr = [];
              res.originName = response.data.licenseNumber;
              res.attaOid = response.data.elecLicenOid;
              res.materialOid = caseMaterialOid;
              res.elecLicenNumber = response.data.elecLicenNumber;
              res.elecLicenName = response.data.elecLicenName;
              eleArr[0] = res;
              this.$set(this.attaList, index, eleArr);
              //判断elemLicenseList是否已经存在办件的证照信息
              if (this.elemLicenseList) {
                this.elemLicenseList.forEach((ite, i) => {
                  if (ite.materialOid == caseMaterialOid) {
                    //移除原来的重新赋值
                    this.elemLicenseList.splice(i, 1);
                  }
                });
              }
              this.elemLicenseList.push(res);
            } else {
              this.$message.warning(
                `未查到姓名【${userName}】,证照号【${idCard}】下材料名为【${materialName}】的证照信息`
              );
              return;
            }
          }
        );
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
      /** 打开高拍仪拍照 */
      openGPYDialog(data, index) {
        console.log(
          "%c [data,index]:",
          "color:red;font-weight:700;",
          data,
          index
        );
        this.qlCaseMaterial = {
          ...data
        };
        this.dialogVisible = true;
      },
      //打开扫码枪
      openScan() {
        openScanningGun().then(res => {
          if (res.StateCode == 0) {
            //成功后激活扫码枪
            activeScanningGun()
              .then(response => {
                if (response.StateCode == 0) {
                  this.$message.success("扫描成功");
                  this.queryParams.caseNumber = response.data;
                } else if (response.StateCode == -1) {
                  this.$message.error("扫码枪扫描超时");
                } else if (response.StateCode == -2) {
                  this.$message.error("扫码枪没有打开");
                }
              })
              .catch(err => {
                console.log(err);
              });
          } else if (res.StateCode == -1) {
            this.$message.error(res.tips);
          } else if (res.StateCode == -4) {
            //扫码枪已打开
            activeScanningGun()
              .then(response => {
                if (response.StateCode == 0) {
                  this.$message.success("扫描成功");
                  this.queryParams.caseNumber = response.data;
                } else if (response.StateCode == -1) {
                  this.$message.error("扫码枪扫描超时");
                } else if (response.StateCode == -2) {
                  this.$message.error("扫码枪没有打开");
                }
              })
              .catch(err => {
                console.log(err);
              });
          } else {
            this.$message.error("加载配置失败");
          }
        });
      },

      getUploadFilesList(list) {
        this.uploadFilesList = list || [];;
      }
    },

  };

</script>
<style lang="scss" scoped>
  .combo-handle-data {
    display: flex;
    justify-content: flex-end;
    padding: 0 20px;

    .handle-data-list {
      flex: 1;
      display: flex;
      flex-direction: column;

      .handle-data-list-item {
        flex: 1;
        display: flex;
        padding: 0 20px;
        justify-content: space-between;
        align-items: center;

        .handle-data-list-item--link {
          text-decoration: underline;
          cursor: pointer;

          &:hover {
            color: #409eff;
          }
        }
      }
    }
  }

  /*.right-btn-group {
  position: absolute;
  right: 130px;
  top: 10px;
}*/

  .right-btn-group-two {
    position: absolute;
    right: 10px;
    top: 10px;
  }

  .right-btn-group .el-button {
    padding: 6px 8px;
    font-size: 12px;
    margin-left: 5px;
    background-color: #0ba2b8;
    border: 1px solid #0ba2b8;
  }

  .right-btn-group .el-button:last-child {
    background-color: #47657d;
    border: 1px solid #47657d;
  }

  .right-btn-group .el-button:last-child:hover {
    background-color: #4d708b;
    border: 1px solid #4d708b;
  }

  .card-item {
    .el-form-item {
      position: relative;
    }

    .el-input {
      margin-bottom: 0px;
      border-radius: 3px;

      &:last-child {
        margin-bottom: 0;
      }
    }
  }

  .scan-btn {
    position: absolute;
    height: 30px;
    background: #c1d0d9 url(~@/assets/image/scan-icon.png) no-repeat center center !important;
    border: none;
    box-shadow: none;
    top: 10px;
    border-radius: 1px;
  }

</style>
