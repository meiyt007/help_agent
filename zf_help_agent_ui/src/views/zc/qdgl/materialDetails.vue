<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="108px"
    >
      <el-form-item label="材料名称" prop="searchName">
        <el-input
          v-model.trim="queryParams.searchName"
          placeholder="请输入材料名称"
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
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8 fl">
      <el-col :span="1.5">
        <el-button
          type="default"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['material:details:add']"
          >新增
        </el-button>
      </el-col>
    </el-row>
    <el-table v-loading="loading" :data="sxMatarialList" border>
      <el-table-column label="序号" align="center" type="index" />
      <el-table-column
        label="材料名称"
        align="center"
        prop="materialName"
        :show-overflow-tooltip="true"
      />
      <!--<el-table-column label="材料类型" align="center" prop="materialType" :show-overflow-tooltip="true"/>-->
      <el-table-column label="材料类型">
        <template slot-scope="scope">
          <span v-if="scope.row.materialType == '0'">原件</span>
          <span v-if="scope.row.materialType == '1'">复印件</span>
          <span v-if="scope.row.materialType == '2'">原件和复印件</span>
          <span v-if="scope.row.materialType == '3'">原件或复印件</span>
        </template>
      </el-table-column>

      <el-table-column label="自制材料提交方式">
        <template slot-scope="scope">
          <span v-if="scope.row.madeMaterialType == '0'">智能制作材料</span>
          <span v-if="scope.row.madeMaterialType == '1'">免于提交材料</span>
          <span v-if="scope.row.madeMaterialType == '2'">自备材料</span>
        </template>
      </el-table-column>

      <el-table-column
        label="材料样本名称"
        align="center"
        prop="materialSampleName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="电子表单名称"
        align="center"
        prop="electronicFormName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="表单模板名称"
        align="center"
        prop="templateName"
        :show-overflow-tooltip="true"
      />
      <!--<el-table-column label="来源渠道" align="center" prop="materialSource"  :show-overflow-tooltip="true"/>-->
<!--      <el-table-column label="来源渠道">
        <template slot-scope="scope">
          <span v-if="scope.row.materialSource == 0">申请人自备</span>
          <span v-if="scope.row.materialSource == 1">政府部门核发</span>
          <span v-if="scope.row.materialSource == 2">其它</span>
        </template>
      </el-table-column>
      <el-table-column
        label="纸质材料份数"
        align="center"
        prop="paperNumber"
        :show-overflow-tooltip="true"
      />-->
      <!--<el-table-column label="排序号" align="center" prop="sort"/>-->

      <el-table-column
        label="操作"
        align="center"
        width="300"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-chakan"
            @click="handleMaterialView(scope.row)"
            v-hasPermi="['material:details:query']"
            >查看
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            @click="handleMaterialUpdate(scope.row)"
            v-hasPermi="['material:details:update']"
            >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-shanchu"
            @click="handleMaterialDeleteRow(scope.row)"
            v-hasPermi="['material:details:del']"
            >删除
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            @click="handleMaterialSplit(scope.row)"
            v-hasPermi="['material:details:split']"
            >颗粒化材料
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            @click="handleSignView(scope.row)"
            v-hasPermi="['material:details:query']"
          >签名配置
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            @click="handleInitForm(scope.row)"
            v-hasPermi="['material:details:split']"
          >模板配置
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
    <!-- 添加或修改用户配置对话框 -->

    <!-- 添加或修改材料对话框 -->
    <el-dialog
    v-dialog-drag
      :title="title"
      :visible.sync="materialOpen"
      :close-on-click-modal="false"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <el-form
        ref="maForm"
        :model="mForm"
        :rules="matrialRules"
        label-width="0px"
      >
        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <el-input v-show="false" v-model="mForm.id" />
          <el-input v-show="false" v-model="mForm.materialOid" />
          <el-input v-show="false" v-model="mForm.serviceOid" />
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="10%" />
            <col width="40%" />
          </colgroup>
          <tr>
            <td><i class="require">*</i><b>材料名称：</b></td>
            <td colspan="3">
              <el-form-item prop="materialName">
                <el-input
                  v-model.trim="mForm.materialName"
                  placeholder=""
                  maxlength="500"
                  show-word-limit
                />
              </el-form-item>
            </td>
          </tr>

          <tr>
            <td><i class="require">*</i><b>材料形式：</b></td>
            <td colspan="3">
              <el-form-item prop="materialFormat">
                <el-radio v-model="mForm.materialFormat" :label="1"
                  >纸质</el-radio
                >
                <el-radio v-model="mForm.materialFormat" :label="2"
                  >电子版</el-radio
                >
                <el-radio v-model="mForm.materialFormat" :label="3"
                >扫描</el-radio
                >
                <el-radio v-model="mForm.materialFormat" :label="4"
                  >容缺补正</el-radio
                >
                <el-radio v-model="mForm.materialFormat" :label="5"
                >证照</el-radio
                >
                <el-radio v-model="mForm.materialFormat" :label="7"
                >告知承诺</el-radio
                >
                <el-radio v-model="mForm.materialFormat" :label="8"
                >扫码上传</el-radio
                >
              </el-form-item>
            </td>

          </tr>
          <tr>
            <td><i class="require">*</i><b>材料类型：</b></td>
            <td colspan="3">
              <el-form-item prop="materialType">
                <el-radio v-model="mForm.materialType" :label="0"
                >原件</el-radio
                >
                <el-radio v-model="mForm.materialType" :label="1"
                >复印件</el-radio
                >
                <el-radio v-model="mForm.materialType" :label="2"
                >原件和复印件</el-radio
                >
              </el-form-item>
            </td>
          </tr>

          <tr>
            <td><i class="require">*</i><b>自制材料提交方式：</b></td>
            <td colspan="3">
              <el-form-item prop="madeMaterialType">
                <el-radio v-model="mForm.madeMaterialType" :label="0" >智能制作材料</el-radio >
                <el-radio v-model="mForm.madeMaterialType" :label="1" >免于提交材料</el-radio >
                <el-radio v-model="mForm.madeMaterialType" :label="2" >自备材料</el-radio >
              </el-form-item>
            </td>
          </tr>

          <tr>
            <td><b>材料类型名称：</b></td>
            <td>
              <el-form-item prop="materialTypeName">
                <el-input
                  v-model.trim="mForm.materialTypeName"
                  placeholder=""
                  maxlength="100"
                  show-word-limit
                />
              </el-form-item>
            </td>
            <td><i class="require">*</i><b>材料顺序：</b></td>
            <td>
              <el-form-item prop="materialSort">
                <!--                  <el-input
                    v-model.trim="mForm.materialSort"
                    placeholder=""
                    maxlength="10"
                    show-word-limit
                  />-->
                <el-input-number
                  v-model="mForm.materialSort"
                  :min="1"
                  :max="999999"
                ></el-input-number>
              </el-form-item>
            </td>
          </tr>

          <tr>
            <td><b>材料样本名称：</b></td>
            <td colspan="3">
              <el-form-item prop="materialSampleName">
                <el-input
                  v-model.trim="mForm.materialSampleName"
                  placeholder=""
                  maxlength="200"
                  show-word-limit
                />
              </el-form-item>
            </td>
          </tr>

          <tr>
            <td><b>材料样本：</b></td>
            <!--              <el-form-item   prop="materialSampleAddr">
                &lt;!&ndash;<el-input  type="file" v-model.trim="mForm.materialSampleAddr" name="文件上传" placeholder=""  show-word-limit/>&ndash;&gt;
              </el-form-item>-->
            <td colspan="3">
              <el-form-item prop="materialSampleAddr">
                <el-upload
                  class="upload-demo"
                  action=""
                  :on-remove="removeMaterialSampleAddr"
                  multiple
                  :http-request="uploadFilesMaterialSampleAddr"
                  :before-upload="beforeUpload"
                  :on-error="uploadError"
                  :file-list="fileList"
                  accept="accept"
                >
                  <el-button size="mini" type="primary" icon="el-icon-upload"
                    >点击上传</el-button
                  >
                </el-upload>
              </el-form-item>
              <el-form-item
                prop="materialSampleOriginName"
                v-if="
                  this.mForm.materialSampleAddr != '' &&
                  this.mForm.materialSampleAddr != null
                "
              >
                <el-form-item v-for="item in this.mForm.sxServiceMaterialSampleAttaList">
                  <span>
                    {{item.materialSampleName}}
                  </span>
                  <el-link
                    type="primary"
                    @click="downloadFileUrl(item.materialSampleAddr)"
                    >下载</el-link
                  >
                  <el-button
                    size="mini"
                    type="text"
                    icon="iconfont zfsoft-shanchu"
                    @click="handleDeleteMaterialSample()"
                    >删除</el-button
                  >
                </el-form-item>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><b>电子表单名称：</b></td>
            <td>
              <el-form-item prop="electronicFormName">
                <el-input
                  v-model.trim="mForm.electronicFormName"
                  placeholder=""
                  maxlength="200"
                  show-word-limit
                />
              </el-form-item>
            </td>
            <td><i class="require">*</i><b>材料份数：</b></td>
            <td>
              <el-form-item prop="paperNumber">
                <!--                  <el-input
                    v-model.trim="mForm.paperNumber"
                    placeholder=""
                    maxlength="10"
                    show-word-limit
                  />-->
                <el-input-number
                  v-model="mForm.paperNumber"
                  :min="1"
                  :max="999999"
                ></el-input-number>
              </el-form-item>
            </td>
          </tr>

          <tr>
            <td><b>电子表单：</b></td>
            <!--            <td colspan="3">
              <el-form-item   prop="materialSampleAddr">
                <el-input  type="file" v-model.trim="mForm.materialSampleAddr" name="文件上传" placeholder=""  show-word-limit/>
              </el-form-item>
            </td>-->
            <td colspan="3">
              <el-form-item prop="electronicFormAddr">
                <el-upload
                  class="upload-demo"
                  action=""
                  :on-remove="removeElectronicFormAddr"
                  multiple
                  :http-request="uploadFilesElectronicFormAddr"
                  :before-upload="beforeUploadElectronicFormAddr"
                  :on-error="uploadError"
                  :file-list="fileElectronicFormAddrList"
                  accept="accept"
                >
                  <el-button size="mini" type="primary" icon="el-icon-upload"
                    >点击上传</el-button
                  >
                </el-upload>
              </el-form-item>
              <el-form-item
                prop="electronicFormOriginName"
                v-if="
                  this.mForm.electronicFormAddr != '' &&
                  this.mForm.electronicFormAddr != null
                "
              >
                <el-form-item v-for="item in this.mForm.sxServiceMaterialElectronicAttaList">
                  <span>{{item.electronicFormName}}</span>
                  <el-link
                    type="primary"
                    @click="downloadFileUrl(item.electronicFormAddr)"
                    >下载</el-link
                  >
                <el-button
                    size="mini"
                    type="text"
                    icon="iconfont zfsoft-shanchu"
                    @click="handleDeleteElectronicForm()"
                    >删除</el-button
                  >
                </el-form-item>
              </el-form-item>
            </td>
          </tr>

          <tr>
            <td><b>材料空表：</b></td>
            <td colspan="3">
              <el-form-item prop="materialEmptyAddr">
                <el-upload
                  class="upload-demo"
                  action=""
                  :on-remove="removeMaterialEmptyAddr"
                  multiple
                  :http-request="uploadFilesMaterialEmptyAddr"
                  :before-upload="beforeUploadMaterialEmptyAddr"
                  :on-error="uploadError"
                  :file-list="fileMaterialEmptyAddrList"
                  accept="accept"
                >
                  <el-button size="mini" type="primary" icon="el-icon-upload"
                    >点击上传</el-button
                  >
                </el-upload>
              </el-form-item>
              <el-form-item
                prop="materialEmptyOriginName"
                v-if="
                  this.mForm.materialEmptyAddr != '' &&
                  this.mForm.materialEmptyAddr != null
                "
              >
                <el-form-item v-for="item in this.mForm.sxServiceMaterialEmptyAttaList">
                  <span>
                    {{item.materialEmptyName}}
                  </span>
                  <el-link
                    type="primary"
                    @click="downloadFileUrl(item.materialEmptyAddr)"
                    >下载</el-link
                  >
                  <el-button
                    size="mini"
                    type="text"
                    icon="iconfont zfsoft-shanchu"
                    @click="handleDeleteMaterialEmpty()"
                    >删除</el-button
                  >
                </el-form-item>
              </el-form-item>
              <!-- <el-form-item
                prop="materialEmptyOriginName"
              >
                <div v-for="item in this.mForm.sxServiceMaterialEmptyAttaList">
                  {{ item.materialEmptyName }}
                  <el-link
                    type="primary"
                    @click="downloadFileUrl(item.materialEmptyAddr)"
                  >下载</el-link>
                  <el-button
                    size="mini"
                    type="text"
                    icon="iconfont zfsoft-shanchu"
                    @click="handleDeleteMaterialEmpty()"
                  >删除</el-button>
                </div>
              </el-form-item> -->
            </td>
          </tr>

          <tr>
            <td><i class="require">*</i><b>来源渠道：</b></td>
            <td colspan="3">
              <el-form-item prop="materialSource">
                <el-radio v-model="mForm.materialSource" :label="0"
                  >申请人自备</el-radio
                >
                <el-radio v-model="mForm.materialSource" :label="1"
                  >政府部门核发</el-radio
                >
                <el-radio v-model="mForm.materialSource" :label="2"
                  >其它</el-radio
                >
              </el-form-item>
            </td>
          </tr>
          <tr v-if="mForm.materialSource==2">
            <td>
              <b>其他来源：</b>
            </td>
            <td colspan="3">
              <el-form-item prop="otherMaterialSource">
                <el-input v-model.trim="mForm.otherMaterialSource" placeholder="请输入出具部门" maxlength="100"
                          show-word-limit />
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>事项材料是否必须：</b></td>
            <td colspan="3">
              <el-form-item prop="mustFlag">
                <el-radio v-model="mForm.mustFlag" :label="0">必须</el-radio>
                <el-radio v-model="mForm.mustFlag" :label="1">非必须</el-radio>
                <el-radio v-model="mForm.mustFlag" :label="2">容缺后补</el-radio>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><b>签名角色：</b></td>
            <td colspan="3">
              <el-form-item >
                <el-radio v-model="mForm.roleType" :label="0">个人</el-radio>
                <el-radio v-model="mForm.roleType" :label="1">多角色</el-radio>
              </el-form-item>
            </td>
          </tr>

          <tr>
            <td><b>签名须知：</b></td>
            <td colspan="3">
              <el-form-item>
                <el-input
                  type="textarea"
                  v-model.trim="mForm.memo"
                  placeholder="签名须知"
                  maxlength="200"
                  show-word-limit
                />
              </el-form-item>
            </td>
          </tr>

          <tr>
            <td><b>材料规格：</b></td>
            <td colspan="3">
              <el-form-item prop="materialSpecification">
                <el-input
                  type="textarea"
                  v-model.trim="mForm.materialSpecification"
                  placeholder="材料规格"
                  maxlength="200"
                  show-word-limit
                />
              </el-form-item>
            </td>
          </tr>

          <tr>
            <td><b>填报须知：</b></td>
            <td colspan="3">
              <el-form-item prop="makeNotice">
                <el-input
                  type="textarea"
                  v-model.trim="mForm.makeNotice"
                  placeholder="填报须知"
                  maxlength="200"
                  show-word-limit
                />
              </el-form-item>
            </td>
          </tr>

          <tr>
            <td><b>受理标准：</b></td>
            <td colspan="3">
              <el-form-item prop="acceptStandard">
                <el-input
                  type="textarea"
                  v-model.trim="mForm.acceptStandard"
                  placeholder="受理标准"
                  maxlength="200"
                  show-word-limit
                />
              </el-form-item>
            </td>
          </tr>

          <tr>
            <td><b>材料依据：</b></td>
            <td colspan="3">
              <el-form-item prop="materialBasis">
                <el-input
                  type="textarea"
                  v-model.trim="mForm.materialBasis"
                  placeholder="材料依据"
                  maxlength="200"
                  show-word-limit
                />
              </el-form-item>
            </td>
          </tr>

          <tr>
            <td><b>材料要求：</b></td>
            <td colspan="3">
              <el-form-item prop="materialRequirement">
                <el-input
                  type="textarea"
                  v-model.trim="mForm.materialRequirement"
                  placeholder="材料要求"
                  maxlength="200"
                  show-word-limit
                />
              </el-form-item>
            </td>
          </tr>

          <tr>
            <td><b>审查要点：</b></td>
            <td colspan="3">
              <el-form-item prop="examinePoint">
                <el-input
                  type="textarea"
                  v-model.trim="mForm.examinePoint"
                  placeholder="审查要点"
                  maxlength="2000"
                  show-word-limit
                />
              </el-form-item>
            </td>
          </tr>

          <tr>
            <td><b>备注：</b></td>
            <td colspan="3">
              <el-form-item prop="remark">
                <el-input
                  type="textarea"
                  v-model.trim="mForm.remark"
                  placeholder="备注"
                  maxlength="3000"
                  show-word-limit
                />
              </el-form-item>
            </td>
          </tr>
        </table>
      </el-form>
      <div slot="footer" class="zf-text-center">
        <el-button type="primary" @click="submitMForm">确 定</el-button>
        <el-button @click="cancelMaterial">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 查看材料对话框 -->
    <el-dialog
    v-dialog-drag
      :title="queryTitle"
      :visible.sync="queryMaterialOpen"
      :close-on-click-modal="false"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >

        <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
          <el-input v-show="false" v-model="qmForm.id" />
          <el-input v-show="false" v-model="qmForm.materialOid" />
          <el-input v-show="false" v-model="qmForm.serviceOid" />
          <colgroup>
            <col width="20%" />
            <col width="30%" />
            <col width="10%" />
            <col width="40%" />
          </colgroup>
          <tr>
            <td><b>材料名称：</b></td>
            <td colspan="3">

                <!--<el-input v-model.trim="mForm.materialName" placeholder=""  show-word-limit/>-->
                <el-col :span="24">
                  {{ qmForm.materialName }}
                </el-col>

            </td>
          </tr>

          <tr>
            <td><b>材料形式：</b></td>
            <td>
                {{ juedgeMaterialFormat }}
            </td>
            <td><b>材料类型：</b></td>
            <td>
                {{ juedgeMaterialType }}
            </td>
          </tr>

          <tr>
            <td><b>自制材料提交方式：</b></td>
            <td> {{ homeMadeMaterialType }}</td>
          </tr>

          <tr>
            <td><b>材料类型名称：</b></td>
            <td>

                <!--<el-input v-model.trim="mForm.materialTypeName" placeholder=""  show-word-limit/>-->
                <el-col :span="24">
                  {{ qmForm.materialTypeName }}
                </el-col>

            </td>
            <td><b>材料顺序：</b></td>
            <td>

                <!--<el-input v-model.trim="mForm.materialSort" placeholder=""  show-word-limit/>-->
                <el-col :span="24" v-if="this.qmForm.materialSort == '0'">1</el-col>
                <el-col :span="24" v-if="this.qmForm.materialSort != '0'">{{ qmForm.materialSort }}</el-col>
            </td>
          </tr>

          <tr>
            <td><b>材料样本名称：</b></td>
            <td colspan="3">

                <!--<el-input v-model.trim="mForm.materialSampleName" placeholder=""  show-word-limit/>-->
                <el-col :span="24">
                  {{ qmForm.materialSampleName }}
                </el-col>

            </td>
          </tr>

          <tr>
            <td><b>材料样本：</b></td>

            <td colspan="3">
              <!-- <el-form-item
                prop="materialSampleOriginName"
                v-if="
                  this.qmForm.materialSampleOriginName != '' &&
                  this.qmForm.materialSampleOriginName != null
                "
              > -->
                <div  v-if="
                  this.qmForm.materialSampleAddr != '' &&
                  this.qmForm.materialSampleAddr != null
                ">
                  <div v-for="item in this.qmForm.sxServiceMaterialSampleAttaList">
                    {{ item.materialSampleName }}
                    <el-link
                        type="primary"
                        @click="downloadFileUrl(item.materialSampleAddr)"
                    >下载</el-link>
                  </div>
                  <!--                 <el-input
                    v-model.trim="qmForm.materialSampleOriginName"
                    placeholder=""
                    show-word-limit
                    style="width: 80%"
                  />-->
                </div>
                <!--<el-button
                    size="mini"
                    type="text"
                    icon="iconfont zfsoft-shanchu"
                    @click="handleDeleteMaterialSample()"
                  >删除</el-button>-->

            </td>
          </tr>
          <tr>
            <td><b>电子表单名称：</b></td>
            <td>

                <!--<el-input v-model.trim="mForm.electronicFormName" placeholder=""  show-word-limit/>-->
                <el-col :span="24">
                  {{ qmForm.electronicFormName }}
                </el-col>

            </td>
            <td><b>材料份数：</b></td>
            <td>

                <!--<el-input v-model.trim="mForm.paperNumber" placeholder=""  show-word-limit/>-->
                <el-col :span="24">
                  {{ qmForm.paperNumber }}
                </el-col>

            </td>
          </tr>

          <tr>
            <td><b>电子表单：</b></td>

            <td colspan="3">
                <div    v-if="
                  this.qmForm.electronicFormAddr != '' &&
                  this.qmForm.electronicFormAddr != null
                ">
                  <div v-for="item in this.qmForm.sxServiceMaterialElectronicAttaList">
                    {{ item.electronicFormName }}
                    <el-link
                        type="primary"
                        @click="downloadFileUrl(item.electronicFormAddr)"
                    >下载</el-link>
                  </div>
                </div>
                <!--<el-button
                    size="mini"
                    type="text"
                    icon="iconfont zfsoft-shanchu"
                    @click="handleDeleteElectronicForm()"
                  >删除</el-button>-->
            </td>
          </tr>

          <tr>
            <td><b>材料空表：</b></td>

            <td colspan="3">
                <div  v-if="
                  this.qmForm.materialEmptyAddr != '' &&
                  this.qmForm.materialEmptyAddr != null
                ">
                  <div v-for="item in this.qmForm.sxServiceMaterialEmptyAttaList">
                    {{ item.materialEmptyName }}
                    <el-link
                        type="primary"
                        @click="downloadFileUrl(item.materialEmptyAddr)"
                    >下载</el-link>
                  </div>
                </div>
                <!--<el-button
                    size="mini"
                    type="text"
                    icon="iconfont zfsoft-shanchu"
                    @click="handleDeleteMaterialEmpty()"
                  >删除</el-button>-->

            </td>
          </tr>

          <tr>
            <td><b>来源渠道：</b></td>
            <td colspan="3">

                <!--                  <el-radio disabled v-model="qmForm.materialSource" :label="0"
                    >申请人自备</el-radio
                  >
                  <el-radio disabled v-model="qmForm.materialSource" :label="1"
                    >政府部门核发</el-radio
                  >
                  <el-radio disabled v-model="qmForm.materialSource" :label="2"
                    >其它</el-radio
                  >-->
                {{ juedgeMaterialSource }}

            </td>
          </tr>

          <tr>
            <td><b>事项材料是否必须：</b></td>
            <td colspan="3">
                {{ juedgeMustFlag }}
            </td>
          </tr>
          <tr>
            <td><b>签名角色：</b></td>
            <td colspan="3">
              {{ signRoleType }}
            </td>
          </tr>
          <tr>
            <td><b>签名须知：</b></td>
            <td colspan="3">

              <!--<el-input v-model.trim="mForm.materialSpecification" placeholder="材料规格"  show-word-limit/>-->
              <el-col :span="24">
                {{ qmForm.memo }}
              </el-col>

            </td>
          </tr>
          <tr>
            <td><b>材料规格：</b></td>
            <td colspan="3">

                <!--<el-input v-model.trim="mForm.materialSpecification" placeholder="材料规格"  show-word-limit/>-->
                <el-col :span="24">
                  {{ qmForm.materialSpecification }}
                </el-col>

            </td>
          </tr>

          <tr>
            <td><b>填报须知：</b></td>
            <td colspan="3">

                <!--<el-input v-model.trim="mForm.makeNotice" placeholder="填报须知"  show-word-limit/>-->
                <el-col :span="24">
                  {{ qmForm.makeNotice }}
                </el-col>

            </td>
          </tr>

          <tr>
            <td><b>受理标准：</b></td>
            <td colspan="3">

                <!--<el-input v-model.trim="mForm.acceptStandard" placeholder="受理标准"  show-word-limit/>-->
                <el-col :span="24">
                  {{ qmForm.acceptStandard }}
                </el-col>

            </td>
          </tr>

          <tr>
            <td><b>材料依据：</b></td>
            <td colspan="3">

                <!--<el-input v-model.trim="mForm.materialBasis" placeholder="材料依据"  show-word-limit/>-->
                <el-col :span="24">
                  {{ qmForm.materialBasis }}
                </el-col>

            </td>
          </tr>

          <tr>
            <td><b>材料要求：</b></td>
            <td colspan="3">

                <!--<el-input v-model.trim="mForm.materialRequirement" placeholder="材料要求"  show-word-limit/>-->
                <el-col :span="24">
                  {{ qmForm.materialRequirement }}
                </el-col>

            </td>
          </tr>

          <tr>
            <td><b>审查要点：</b></td>
            <td colspan="3">

                <!--<el-input v-model.trim="mForm.examinePoint" placeholder="审查要点"  show-word-limit/>-->
                <el-col :span="24">
                  {{ qmForm.examinePoint }}
                </el-col>

            </td>
          </tr>

          <tr>
            <td><b>备注：</b></td>
            <td colspan="3">

                <!--<el-input v-model.trim="mForm.remark" placeholder="备注"  show-word-limit/>-->
                <el-col :span="24">
                  {{ qmForm.remark }}
                </el-col>

            </td>
          </tr>
        </table>

      <div slot="footer" class="zf-text-center">
        <!--<el-button type="primary" @click="submitMForm">确 定</el-button>-->
        <el-button @click="cancelQueryMaterial">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 颗粒化材料-->

    <!-- 信息详细 -->
    <el-dialog
      v-dialog-drag
      :visible.sync="splitView"
      v-if="splitView"
      title="颗粒化材料"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
      :close-on-click-modal="false"
    >
      <sx-material-split
        :serviceOid="sxServiceOid"
        :parentMaterialOid="parentMaterialOid"
        @closeMaterialSplit="closeMaterialSplit"
      ></sx-material-split>
      <div slot="footer" class="zf-text-center">
        <el-button @click="splitView = false">
          关闭
        </el-button>
      </div>
    </el-dialog>

    <!--表单模板配置-->
    <el-dialog
      v-dialog-drag
      :visible.sync="formView"
      :close-on-click-modal="false"
      width="600px"
      height="350px"
      title="模板配置"
      scrollbar
      append-to-body
    >
      <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
        <colgroup>
          <col width="30%" />
          <col width="70%" />
        </colgroup>
        <tr>
          <td><i class="require">*</i><b>材料名称：</b></td>
          <td>
            {{this.formMaterial.materialName}}
          </td>
        </tr>
        <tr>
          <td><i class="require">*</i><b>选择表单：</b></td>
          <td>
            <template>
              <el-select v-model="formCode" clearable placeholder="请选择" >
                <el-option
                  v-for="item in sxFormList"
                  :key="item.formCode"
                  :label="item.formName"
                  :value="item.formCode">
                </el-option>
              </el-select>
            </template>

            <!-- <template>
              <el-select v-model="formCode" placeholder="请选择" @change="changeSelect">
                <el-option
                  v-for="item in sxFormList"
                  :label="item.formName"
                  :value="item.formCode">
                </el-option>
              </el-select>
            </template> -->
          </td>
        </tr>
        <tr>
          <td><i class="require">*</i><b>选择模板：</b></td>
          <td>
            <template>
              <el-select v-model="templateOid" clearable placeholder="请选择" >
                <el-option
                  v-for="item in sxFormTemplateList"
                  :key="item.docxTemplateOid"
                  :label="item.docxTemplateName"
                  :value="item.docxTemplateOid">
                </el-option>
              </el-select>
            </template>
            <!-- <template>
              <el-select v-model="templateOid" placeholder="请选择">
                <el-option
                  v-for="item in sxFormTemplateList"
                  :label="item.docxTemplateName"
                  :value="item.docxTemplateOid">
                </el-option>
              </el-select>
            </template> -->
          </td>
        </tr>
      </table>

      <div slot="footer" class="zf-text-center">
        <el-button type="primary" @click="submitOpt">确 定</el-button>
        <el-button @click="closeFormView">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 签名设置 -->
    <el-dialog
      v-dialog-drag
      height="600px"
      scrollbar
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="signView"
      v-if="signView"
      width="1100px"
      append-to-body
    >
      <div id="printTest">
        <el-form ref="form" :model="form" label-width="0px">
          <el-button type="primary" @click="addHtml" class="add-btn"
          >增加</el-button
          >
          <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table--common zf-zc-table--td-center">
            <colgroup>
              <col width="50" />
              <col width="220" />
              <col width="160" />
              <col width="160" />
              <col width="160" />
              <col width="560" />
              <col width="70" />
            </colgroup>
            <tr>
              <th>序号</th>
              <th>角色名称</th>
              <th>是否多人</th>
              <th>签章类型</th>
              <th>排序</th>
              <th>短信模板</th>
              <th>操作</th>
            </tr>
            <template v-for="(data, index) in form.signList">
              <template>
                <tr>
                  <td>
                    <el-form-item prop="xuhao">
                      {{ index + 1 }}
                    </el-form-item>
                  </td>
                  <td>
                    <el-form-item>
                      <el-input
                        maxlength="32"
                        v-model="data.roleName"
                      ></el-input>
                    </el-form-item>
                  </td>
                  <td>
                    <el-form-item>
                      <el-select
                        v-model="data.manyPeople"
                        placeholder="请选择是否多人"
                      >
                        <el-option label="是" value="Y"></el-option>
                        <el-option label="否" value="N"></el-option>
                      </el-select>
                    </el-form-item>
                  </td>
                  <td>
                    <el-form-item>
                      <el-select
                        v-model="data.type"
                        placeholder="签章类型"
                      >
                        <el-option label="个人" value="0"></el-option>
                        <el-option label="企业" value="1"></el-option>
                      </el-select>
                    </el-form-item>
                  </td>
                  <td>
                    <el-form-item>
                      <el-input
                        maxlength="5"
                        v-model="data.sort"
                      ></el-input>
                    </el-form-item>
                  </td>
                  <td>
                    <el-form-item>
                      <el-input
                        v-model.trim="data.message"
                        maxlength="100"
                      ></el-input>
                    </el-form-item>
                  </td>
                  <td>
                    <el-form-item>
                      <el-button
                        style="border: 0px"
                        icon="el-icon-minus"
                        @click="delHtml(index)"
                        v-if="isShowDel(data) == true"
                        class="handle-btn"
                      ></el-button>
                      <el-button
                        style="border: 0px"
                        icon="el-icon-delete"
                        v-if="isShowDel(data) == false"
                        @click="deleteSign(data.id,index)"
                        class="handle-btn"
                      ></el-button>
                    </el-form-item>
                  </td>
                </tr>
              </template>
            </template>
          </table>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitSign">保 存</el-button>
        <el-button @click="closeSignView">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  page,
  uploadsxFile,
  insertSxServiceMaterialService,
  getSxServiceMaterialByOid,
  downloadMaterial,
  delMaterialRow,
  getSignList,
  saveSign,
  delSign
} from "@/api/zc/qdgl/materialDetails.js";
import { pageMaterialSplitList, getSxFormList, getTemplateByFormCode} from "@/api/zc/qdgl/sxService.js";
import {saveMaterialFormTemplate, getMaterialFormTemplate} from "@/api/zc/qdgl/materialDetails.js";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import {
  validIntNoZero,
  validIDCard,
  validatePhoneTwo,
  validatePassword,
  validateEmail,
  validateNumber
} from "@/utils/validate";
import { validateNumberNoPonint } from "@/utils/validate";
import sxMaterialSplit from "@/views/zc/qdgl/sxMaterialSplit.vue";
export default {
  //定义获取父页面传过来的值的格式
  props: ["sxServiceOid"],
  components: { sxMaterialSplit },
  data () {
    return {
      fileList: [],
      fileElectronicFormAddrList: [],
      fileMaterialEmptyAddrList: [],
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 用户数据
      //userList: [],
      sxMatarialList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      //open: false,
      //openView: false,
      //openRole: false,
      materialOpen: false,
      //headImageSrc: undefined,
      //disable: true,
      // 表单参数
      //form: { sex: 'M', districtOidSelect: '' },
      //表单参数mForm
      mForm: {
        materialFormat: "1",
        materialType: "0",
        madeMaterialType: "0",
        materialSource: "0",
        mustFlag: "0",
        roleType: "0",
        memo: "",
        materialSampleAddr: "",
        materialSampleAddrYl: "",
        electronicFormAddr: "",
        materialEmptyAddr: "",
        materialSort: "1"
      },
      qmForm: {},
      queryTitle: "",
      queryMaterialOpen: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        searchName: "",
        serviceOid: ""
      },
      serviceOid: "",
      parentMaterialOid: "",
      matrialRules: {
        materialName: [
          { required: true, message: "材料名不能为空", trigger: "blur" }
        ],
        materialSort: [
          { required: true, message: "材料顺序不能为空", trigger: "blur" }
        ],
        paperNumber: [
          { required: true, message: "材料份数不能为空", trigger: "blur" }
        ],
        materialFormat: [
          { required: true, message: "材料形式必须选择", trigger: "blur" }
        ],
        materialType: [
          { required: true, message: "材料类型必须选择", trigger: "blur" }
        ],
        madeMaterialType: [
          { required: true, message: "自制材料提交方式必须选择", trigger: "blur" }
        ],
        materialSource: [
          { required: true, message: "来源渠道必须选择", trigger: "blur" }
        ],
        mustFlag: [{ required: true, message: "必选项", trigger: "blur" }],
        // roleType: [{ required: true, message: "必选项", trigger: "blur" }]
      },
      splitView: false,
      // 表单模板定义
      formView: false,
      formMaterial: {
        materialOid: "",
        materialName: "",
        formCode: ""
      },
      formCode: "",
      templateOid: "",
      templateName: "",
      sxFormList: [],
      sxFormTemplateList: [],
      //签名配置flag
      signView:false,
      //签名配置数据
      form:{
        signList:[],
        materialOid: ''
      },
    };
  },
  computed: {
    // 计算属性的 getter
    juedgeMaterialType: function () {
      //return this.form.enabledFlag == "1"?'启用':'禁用'
      var ch = this.qmForm.materialType;
      if (ch == "0") {
        return "原件";
      } else if (ch == "1") {
        return "复印件";
      } else if (ch == "2") {
        return "原件和复印件";
      }
    },

    homeMadeMaterialType: function () {
      let ch = this.qmForm.madeMaterialType;
      if (ch == "0") {
        return "智能制作材料";
      } else if (ch == "1") {
        return "免于提交材料";
      } else if (ch == "2") {
        return "自备材料";
      }
    },

    juedgeMaterialFormat: function () {
      //return this.form.enabledFlag == "1"?'启用':'禁用'
      var ch = this.qmForm.materialFormat;
      if (ch == "1") {
        return "纸质";
      } else if (ch == "2") {
        return "电子版";
      } else if (ch == "3") {
        return "扫描";
      } else if (ch == "8") {
        return "扫码上传";
      } else if (ch == "5") {
        return "证照";
      } else if (ch == "4") {
        return "容缺补正";
      }else if (ch == "7") {
        return "告知承诺";
      }
    },
    juedgeMaterialSource: function () {
      //return this.form.enabledFlag == "1"?'启用':'禁用'
      var ch = this.qmForm.materialSource;
      if (ch == "0") {
        return "申请人自备";
      } else if (ch == "1") {
        return "政府部门核发";
      } else if (ch == "3") {
        return "其它";
      }
    },
    juedgeMustFlag: function () {
      //return this.form.enabledFlag == "1"?'启用':'禁用'
      var ch = this.qmForm.mustFlag;
      if (ch == "0") {
        return "必须";
      } else if (ch == "1") {
        return "非必须";
      } else if (ch == "2") {
        return "容缺后补";
      }
    },
    signRoleType: function () {
      //return this.form.enabledFlag == "1"?'启用':'禁用'
      var ch = this.qmForm.roleType;
      if (ch == "0") {
        return "个人";
      } else if (ch == "1") {
        return "企业";
      }
    }
  },
  created () {
    //this.getList()
    //this.getDistrictTree()
    //this.getUserTypt()
  },
  //获取父页面的值
  mounted () {
    //this.serviceOid = this.sxServiceOid;
    this.queryParams.serviceOid = this.sxServiceOid;
    this.mForm.serviceOid = this.sxServiceOid;
    //this.getSxServiceOne();
    this.getList();
  },
  methods: {
     /** 重置按钮操作 */
     resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    //获取签名配置列表
    handleSignView(row){
      const materialOid = row.materialOid
      this.form.materialOid = materialOid;
      this.loading = true
      getSignList(materialOid).then(response => {
        this.form.signList = response.data
        this.loading = false
        this.signView = true
      })
    },
    //添加子项模块
    addHtml: function () {
      this.form.signList.push({
      })
    },
    //删除子项模块
    delHtml: function (index) {
      this.form.signList.splice(index, 1);
    },
    //逻辑删除
    deleteSign:function (id,index) {
      //这边要有两步操作，第一是修改数据库，第二是在页面删除
      delSign(id).then(response => {
        if (response.code === 200) {
          this.msgSuccess("删除成功");
          this.form.signList.splice(index, 1);
        }
      })
    },
    //保存签名
    submitSign:function(){
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.signList == null || this.form.signList.length == 0) {
            this.msgWarning("请至少录入一个签名！");
            return false;
          }else {
            let childName = [];
            let childSort = [];
            for (let sxSign of this.form.signList) {
              //角色名
              if(sxSign.roleName==null||(sxSign.roleName=='')){
                this.msgWarning("角色名称不能为空！");
                return false;
              }
              childName.push(sxSign.roleName);
              //是否多人
              if(sxSign.manyPeople==null||(sxSign.manyPeople=='')){
                this.msgWarning("请选择是否多人！");
                return false;
              }
              //签章类型
              if(sxSign.type==null||(sxSign.type=='')){
                this.msgWarning("请选择是签章类型！");
                return false;
              }
              //排序
              const re = /^[1-9]{1,}[\d]*$/;
              if(!re.test(sxSign.sort)||parseInt(sxSign.sort) <= 0){
                this.msgWarning("排序号必须为正整数！");
                return false;
              }
              childSort.push(sxSign.sort);
              //短信模板
              if(sxSign.message==null||(sxSign.message=='')){
                this.msgWarning("短信模板不能为空！");
                return false;
              }
            }
            let repeat = childName.join(",") + ",";
            let repeatSort = childSort.join(",") + ",";
            for (let i = 0; i < childName.length; i++) {
              for(let j =i+1;j<childName.length;j++){
                if(childName[i] == childName[j]){
                  this.msgWarning("角色名称:" + childName[i] + " 已存在，不能重复添加！");
                  return false;
                }
                if(childSort[i] == childSort[j]){
                  this.msgWarning("排序号:" + childSort[i] + " 已存在，不能重复添加！");
                  return false;
                }
              }
            }
          }
          saveSign(this.form).then((response) => {
            if (response.code === 200) {
              this.msgSuccess("保存成功");
              this.signView = false;
              this.getList();
            }
          });
        } else {
          return false;
        }
      })
    },
    //关闭签名配置弹框
    closeSignView() {
      this.signView = false;
    },
    //判断显示哪个删除按钮
    isShowDel (row) {
      return !row.id && row.id== undefined;
    },
    /** 删除按钮操作 */
    handleMaterialDeleteRow (row) {
      pageMaterialSplitList(row.materialOid).then(response => {
        let xhMaterailList=response.data;
        if(xhMaterailList.length>0){
          this.$message.error("该材料包含细化材料需先删除细化材料后才能删除");
        }else{
          const materialName = row.materialName;
          this.$confirm('是否确认删除"' + materialName + '"的数据项?', "警告", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          })
            .then(function () {
              return delMaterialRow(row.materialOid);
            })
            .then(() => {
              this.getList();
              this.msgSuccess("删除成功");
            })
            .catch(function () { });
        }

      });


    },
    resetQueryMaterial () {
      this.qmForm = {};
      this.resetForm("qmForm");
    },
    // 取消按钮
    cancelQueryMaterial () {
      this.resetQueryMaterial();
      this.queryMaterialOpen = false;
      //this.resetMaterial();
    },
    handleMaterialView (row) {
      //this.reset()
      var that = this;
      this.resetQueryMaterial();
      //this.qmForm = {};
      //this.resetForm('qmForm');
      //this.resetMaterial();
      //this.handleDeleteElectronicForm ();
      //this.handleDeleteMaterialSample ();
      //this.handleDeleteMaterialEmpty();
      getSxServiceMaterialByOid(row.materialOid).then(res => {
        let result = res.data;
        this.qmForm = result;
        this.queryMaterialOpen = true;
        this.queryTitle = "查看材料信息";
      });
    },
    // 取消按钮
    cancelMaterial () {
      this.materialOpen = false;
      this.resetMaterial();
    },
    handleDeleteMaterialEmpty () {
      this.mForm.sxServiceMaterialEmptyAttaList = [];
      this.fileMaterialEmptyAddrList = [];
      this.mForm.materialEmptyOriginName = "";
      this.mForm.materialEmptyAddr = "";
    },
    handleDeleteElectronicForm () {
      this.mForm.sxServiceMaterialElectronicAttaList = [];
      this.fileElectronicFormAddrList = [];
      this.mForm.electronicFormOriginName = "";
      this.mForm.electronicFormAddr = "";
    },
    handleDeleteMaterialSample () {
      this.mForm.sxServiceMaterialSampleAttaList = [];
      this.fileList = [];
      this.mForm.materialSampleOriginName = "";
      this.mForm.materialSampleAddr = "";
      this.mForm.materialSampleAddrYl = "";
    },
    //下载附件
    downloadFile (attaOid) {
      downloadMaterial(attaOid);
    },
    downloadFileUrl (url) {
      window.open(url);
    },
    removeMaterialSampleAddr () {
      this.fileList = [];
      this.mForm.materialSampleAddr = "";
    },
    removeElectronicFormAddr () {
      this.fileElectronicFormAddrList = [];
      this.mForm.electronicFormAddr = "";
    },
    removeMaterialEmptyAddr () {
      this.fileMaterialEmptyAddrList = [];
      this.mForm.materialEmptyAddr = "";
    },
    submitMForm () {
      this.$refs["maForm"].validate(valid => {
        if (valid) {
          insertSxServiceMaterialService(JSON.stringify(this.mForm)).then(
            response => {
              if (response.code == 200) {
                this.$message.success("保存成功！");
                this.materialOpen = false;
                //刷新列表
                this.getList();
              } else {
                this.$message.error("保存失败！");
              }
            }
          );
        }
      });
    },
    /** 失败后返回 */
    uploadError (resp) {
      let _that = this;
      _that.msgError("文件上传失败");
    },
    /** 上传附件请求操作 */
    beforeUpload (file) {
      let _that = this;
      let isRightSize = file.size / 1024 / 1024 < 100;
      if (!isRightSize) {
        _that.$message.error("文件大小超过 100MB");
      }
      _that.fileList = [];
      _that.fileList.push(file);
      return isRightSize;
    },
    /** 上传附件请求操作 */
    beforeUploadElectronicFormAddr (file) {
      let _that = this;
      let isRightSize = file.size / 1024 / 1024 < 100;
      if (!isRightSize) {
        _that.$message.error("文件大小超过 100MB");
      }
      _that.fileElectronicFormAddrList = [];
      _that.fileElectronicFormAddrList.push(file);
      return isRightSize;
    },
    /** 上传附件请求操作 */
    beforeUploadMaterialEmptyAddr (file) {
      let _that = this;
      let isRightSize = file.size / 1024 / 1024 < 100;
      if (!isRightSize) {
        _that.$message.error("文件大小超过 100MB");
      }
      _that.fileMaterialEmptyAddrList = [];
      _that.fileMaterialEmptyAddrList.push(file);
      return isRightSize;
    },
    /** 上传附件 */
    uploadFilesMaterialSampleAddr (file) {
      const loading = this.$loading({
        lock: true,
        text: "正在上传附件",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)",
        customClass:'loadCoupon'
      });
      let _that = this;
      let formData = new FormData();
      formData.append("file", file.file);
      uploadsxFile(formData).then(respon => {
        this.mForm.materialSampleOriginName = respon.data.name;
        this.mForm.materialSampleAddr = respon.data.oid;
        this.mForm.materialSampleAddrYl = respon.data.attaUrl;
      }).finally(()=>{
        loading.close()
      });
    },

    /** 上传附件 */
    uploadFilesElectronicFormAddr (file) {
      const loading = this.$loading({
        lock: true,
        text: "正在上传附件",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)",
        customClass:'loadCoupon'
      });
      let _that = this;
      let formData = new FormData();
      formData.append("file", file.file);
      uploadsxFile(formData).then(respon => {
        this.mForm.electronicFormAddr = respon.data.oid;
        this.mForm.electronicFormOriginName = respon.data.name;
        this.mForm.materialEmptyAddr = respon.data.attaUrl;
      }).finally(()=>{
        loading.close()
      });
    },

    /** 上传附件 */
    uploadFilesMaterialEmptyAddr (file) {
      const loading = this.$loading({
        lock: true,
        text: "正在上传附件",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)",
        customClass:'loadCoupon'
      });
      let _that = this;
      let formData = new FormData();
      formData.append("file", file.file);
      uploadsxFile(formData).then(respon => {
        this.mForm.materialEmptyAddr = respon.data.oid;
        this.mForm.materialEmptyOriginName = respon.data.name;
      }).finally(()=>{
        loading.close()
      });
    },
    /** 查询用户列表 */
    getList () {
      this.loading = true;
      let that = this;
      page(this.queryParams)
        .then(response => {
          this.sxMatarialList = response.data.data;
          this.total = response.data.total;
          this.loading = false;
        })
        .catch(function () {
          that.loading = false;
        });
    },
    /** 搜索按钮操作 */
    handleQuery () {
      this.queryParams.page = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery () {
      //this.resetForm('queryForm')
      //this.resetForm('queryForm')
      //this.queryParams.name = ''
      //this.queryParams.districtOid = null
      // this.queryParams.organOid = null
      //this.queryParams.districtOidSelect = null;
      //this.queryParams.organOidSelect = null;
      this.getList();
    },
    /** 新增按钮操作 */
    handleAdd: function () {
      this.materialOpen = true;
      this.resetMaterial();
      this.mForm.materialSort = 0;
      this.mForm.paperNumber = 0;
      this.fileList = [];
      this.fileElectronicFormAddrList = [];
      this.fileMaterialEmptyAddrList = [];
      this.title = "新增材料信息";
    },
    resetMaterial () {
      var chServiceOid = this.mForm.serviceOid;
      this.mForm = {};
      this.resetForm("mForm");
      this.mForm.serviceOid = chServiceOid;
    },
    /** 修改材料按钮操作 */
    handleMaterialUpdate (row) {
      var that = this;
      this.resetMaterial();
      this.handleDeleteElectronicForm();
      this.handleDeleteMaterialSample();
      this.handleDeleteMaterialEmpty();
      getSxServiceMaterialByOid(row.materialOid).then(res => {
        let result = res.data;
        this.mForm = result;
        // console.log("-----------"+JSON.stringify(this.mForm))
        this.materialOpen = true;
        this.title = "修改材料信息";
      });
    },
    handleMaterialSplit (row) {
      this.splitView = true;
      this.parentMaterialOid = row.materialOid;
    },
    closeMaterialSplit () {
      this.splitView = false;
    },
    // 表单模板配置
    handleInitForm(row) {
      this.formCode = "";
      this.templateOid = "";
      getMaterialFormTemplate(this.sxServiceOid, row.materialOid).then(res => {
        getSxFormList(this.sxServiceOid).then(resp =>{
          this.sxFormList = resp.data;
        })
        if(res.data != null) {
          this.formCode = res.data.formCode;
          this.templateOid = res.data.templateOid;
          this.templateName = res.data.templateName;
          getTemplateByFormCode(this.formCode).then(res =>{
            this.sxFormTemplateList = res.data;
          })
        }
        this.formView = true;
        this.formMaterial.materialOid = row.materialOid;
        this.formMaterial.materialName = row.materialName;
      })
    },
    closeFormView() {
      this.formView = false;
    },
    changeSelect(val) {
      this.sxFormTemplateList = [];
      getTemplateByFormCode(val).then(resp => {
        this.sxFormTemplateList = resp.data;
        this.sxFormTemplateList.forEach(temp =>{
          this.templateOid = temp.docxTemplateOid;
          this.templateName = temp.docxTemplateName;
        })
      })
    },
    submitOpt() {
      this.formMaterial.serviceOid = this.sxServiceOid;
      this.formMaterial.templateOid = this.templateOid;
      this.formMaterial.templateName = this.templateName
      this.formMaterial.formCode = this.formCode;
      this.sxFormList.forEach(obj =>{
        if(obj.formCode == this.formCode) {
          this.formMaterial.designOid = obj.designOid;
        }
      })
      saveMaterialFormTemplate(this.formMaterial).then(respon => {
        if(respon.data == 1) {
          this.msgSuccess("保存成功");
          this.formView = false;
        } else {
          this.msgError("保存失败");
        }
      });
    }
  }
};
</script>
<style scoped>
table {
  border-collapse: collapse;
}
.postClass .treeselect {
  width: 98% !important;
}
.upload-demo >>> .el-upload-list {
  display: inline-block;
  transform: translateY(8px);
}
</style>
