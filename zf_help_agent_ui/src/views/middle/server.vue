<template>
  <div class="app-container typeApp">
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="68px"
    >
      <el-form-item label="服务分类" prop="serverTypeOid">
        <treeselect
          v-model="queryParams.serverTypeOid"
          :options="serviceTypeOptions"
          style="width: 240px"
          noOptionsText="暂无数据"
          :defaultExpandLevel="1"
          placeholder="请输入服务分类"
        />
      </el-form-item>
      <el-form-item label="服务名称" prop="serverName">
        <el-input
          v-model.trim="queryParams.serverName"
          placeholder="请输入服务名称"
          clearable
          size="small"
        />
      </el-form-item>
      <el-form-item label="启禁用">
        <el-select
          v-model="queryParams.ableStatus"
          size="small"
          style="width: 240px"
        >
          <el-option
            v-for="(value, key) in ableStatusOptions"
            :key="key"
            :label="value"
            :value="key"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="服务状态">
        <el-select
          v-model="queryParams.serverStatus"
          size="small"
          style="width: 240px"
        >
          <el-option
            v-for="(value, key) in serverStatusOptions"
            :key="key"
            :label="value"
            :value="key"
          />
        </el-select>
      </el-form-item>
      <el-form-item class="fr mr0">
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
        >搜索
        </el-button
        >
        <el-button
          type="warning"
          icon="el-icon-refresh"
          size="mini"
          @click="resetQuery"
          class="ml5"
        >重置
        </el-button
        >
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="default"
          icon="el-icon-delete"
          size="mini"
          @click="submitBatchForm"
        >删除
        </el-button
        >
      </el-col>
    </el-row>
    <el-table
      ref="multipleTable"
      :data="serverList"
      tooltip-effect="dark"
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="60" align="center"></el-table-column>
      <el-table-column label="序号" width="55" type="index" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="用户名称"
        align="center"
        prop="middleUser.userName"
        show-overflow-tooltip
      />
      <el-table-column
        label="服务名称"
        align="center"
        prop="serverName"
        show-overflow-tooltip
      />
      <el-table-column
        label="服务版本"
        align="center"
        prop="serverVersion"
        show-overflow-tooltip
      />
      <el-table-column
        label="服务分类"
        align="center"
        prop="serverType.typeName"
        show-overflow-tooltip
      />
      <el-table-column
        label="服务状态"
        align="center"
        prop="serverStatus"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <p v-for="(value,key) in serverStatusOptions" :key="key" v-if="scope.row.serverStatus==key">{{ value }}</p>
        </template>
      </el-table-column>
      <el-table-column
        label="启禁用状态"
        align="center"
        prop="ableStatus"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <p v-if="scope.row.ableStatus=='Y'">启用</p>
          <p v-if="scope.row.ableStatus=='N'">禁用</p>
        </template>
      </el-table-column>
      <el-table-column
        label="接口数量"
        align="center"
        prop="num"
        show-overflow-tooltipF
      />
      <el-table-column
        label="操作"
        width='auto'
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-chakan"
            @click="handleView(scope.row)"
            v-hasPermi="['im:server:view']"
          >查看
          </el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-shenhe"
            v-if="isShowAuth(scope.row)"
            @click="handleExamine(scope.row)"
            v-hasPermi="['im:server:audit']"
          >审核
          </el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-shenhe"
            v-if="isShowAuthUp(scope.row)"
            @click="handleExamineUp(scope.row)"
            v-hasPermi="['im:server:audit']"
          >升级审核
          </el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-zfsoft-banbenpeizhi"
            @click="handleHistory(scope.row)"
            v-hasPermi="['im:server:history']"
          >历史版本
          </el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            @click="handleEnable (scope.row)"
            v-hasPermi="['im:server:able']"
          >启禁用
          </el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-s-check"
            @click="checkCheck (scope.row)"
            v-hasPermi="['im:server:able']"
          >立即检测
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-xiugai"
            @click="strategyWarn (scope.row)"
            v-hasPermi="['im:server:able']"
          >预警策略
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNumber"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 查看服务信息详细 -->
    <el-dialog
      title="服务信息"
      :close-on-click-modal="false"
      :visible.sync="openView"
      width="70%"
       custom-class="customClass"
      
      height="800px"
      append-to-body
      scrollbar
    >
      <div class="zf-zc-table--title">服务基本信息</div>
      <!--<h3>事项标题</h3>-->
      <table
        cellspacing="0"
        cellpadding="0"
        border="0"
        width="100%"
        class="zf-zc-table"
      >
        <colgroup>
          <col width="20%"/>
          <col width="30%"/>
          <col width="20%"/>
          <col width="30%"/>
        </colgroup>
        <tr>
          <td><b>用户名称：</b></td>
          <td>
            {{ middleUser.userName }}
          </td>
          <td><b>服务名称：</b></td>
          <td>
            {{ server.serverName }}
          </td>
        </tr>
        <tr>
          <td><b>服务图标：</b></td>
          <td>
            <img width="100px" :src="previewImageUrl+server.fastdfsNginxUrl" />
          </td>
          <td><b>服务版本：</b></td>
          <td>
            {{ server.serverVersion }}
          </td>
        </tr>
        <tr>
          <td><b>服务形式：</b></td>
          <td>
            {{ server.serverStyle | serverStyle }}
          </td>
          <td><b>服务分类：</b></td>
          <td>
            {{ serverType.typeName }}
          </td>
        </tr>
        <tr>
          <td><b>附件：</b></td>
          <td>
            <div v-for="(attaJSONObject, index) in server.attaJsonArray" :key="index">
              <div v-show="null != attaJSONObject.oid && '' != attaJSONObject.oid">
                <img width="100px" :src="previewImageUrl+attaJSONObject.fastdfsNginxUrl" />
              </div>
            </div>
          </td>
          <td><b>服务状态：</b></td>
          <td>
            <p v-for="(value,key) in serverStatusOptions" :key="key" v-if="server.serverStatus==key">{{ value }}</p>
          </td>
        </tr>
        <tr>
          <td><b>服务关键字：</b></td>
          <td>
            {{ server.serverKeys }}
          </td>
          <td><b>启禁用状态：</b></td>
          <td>
            {{ server.ableStatus == 'N' ? '禁用' : '启用' }}
          </td>
        </tr>
        <tr>
          <td><b>服务简介：</b></td>
          <td colspan="3">
            {{ server.serverMemo }}
          </td>
        </tr>
        <tr>
          <td><b>用户状态：</b></td>
          <td>
            {{ middleUser.userStatus | userStatus }}
          </td>
          <td><b>用户企业名称：</b></td>
          <td>
            {{ middleUser.companyName }}
          </td>
        </tr>
        <tr>
          <td><b>用户手机：</b></td>
          <td>
            {{ middleUser.mobile }}
          </td>
          <td><b>用户电子邮件：</b></td>
          <td>
            {{ middleUser.email }}
          </td>
        </tr>
      </table>
      <div class="zf-zc-table--title">接口信息列表</div>
      <el-table v-loading="loading" :data="serverInterfaceList" border>
        <el-table-column label="序号" width="50" type="index" align="center">
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="接口名称"
          align="center"
          prop="interfaceName"
          show-overflow-tooltip
        />
        <el-table-column
          label="请求方式"
          align="center"
          width="100"
          prop="requestMethod"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            {{ scope.row.requestMethod | requestMethodHandle }}
          </template>
        </el-table-column>
        <el-table-column
          label="请求地址"
          width="600"
          align="center"
          prop="requestAddr"
          show-overflow-tooltip
        />
        <el-table-column
          label="接口状态"
          width="100"
          align="center"
          prop="interfaceStatus"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            <p v-for="(value,key) in serverStatusOptions" :key="key" v-if="scope.row.interfaceStatus==key">
              {{ value }}</p>
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          width="150"
          align="center"
        >
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="iconfont zfsoft-chakan"
              @click="handleInterface(scope.row)"
              v-hasPermi="['im:server:view']"
            >查看
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="zf-zc-table--title">历史审核记录列表</div>
      <el-table width="100%" v-loading="loading" :data="serverAuthRecList" border>
        <el-table-column label="序号" width="50" type="index" align="center">
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="服务名称"
          align="center"
          prop="serverName"
        />
        <el-table-column
          label="操作员名称"
          width="200"
          align="center"
          prop="userName"
        />
        <el-table-column
          label="状态"
          width="200"
          align="center"
          prop="serverStatus"
        >
          <template slot-scope="scope">
            <p v-for="(value,key) in serverStatusOptions" :key="key" v-if="scope.row.serverStatus==key">{{ value }}</p>
          </template>
        </el-table-column>
        <el-table-column
          label="审核日期"
          width="200"
          align="center"
          prop="createDate"
        />
        <el-table-column
          label="原因"
          width="350"
          align="center"
          prop="memo"
        />
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="openView = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 查看接口信息详细 -->
    <el-dialog
      title="接口信息"
      :close-on-click-modal="false"
      :visible.sync="interfaceView"
      width="70%"
      append-to-body
      height="800px"
      scrollbar
    >
      <div class="zf-zc-table--title">接口基本信息</div>
      <!--<h3>事项标题</h3>-->
      <table
        cellspacing="0"
        cellpadding="0"
        border="0"
        class="zf-zc-table"
      >
        <colgroup>
          <col width="20%"/>
          <col width="30%"/>
          <col width="20%"/>
          <col width="30%"/>
        </colgroup>
        <tr>
          <td><b>接口名称：</b></td>
          <td>
            {{ serverInterface.interfaceName }}
          </td>
          <td><b>请求方式：</b></td>
          <td>
            {{ serverInterface.requestMethod | requestMethodHandle }}
          </td>
        </tr>
        <tr>
          <td><b>接口简介：</b></td>
          <td colspan="3">
            {{ serverInterface.serverMemo }}
          </td>
        </tr>
        <tr>
          <td><b>返回示例：</b></td>
          <td colspan="3">
            {{ serverInterface.backExample }}
          </td>
        </tr>
        <tr>
          <td><b>使用说明：</b></td>
          <td colspan="3">
            {{ serverInterface.instructions }}
          </td>
        </tr>
        <tr>
          <td><b>请求地址：</b></td>
          <td colspan="3">
            {{ serverInterface.requestAddr }}
          </td>
        </tr>
        <tr>
          <td><b>接口状态：</b></td>
          <td colspan="3">
            <p v-for="(value,key) in serverStatusOptions" :key="key" v-if="serverInterface.interfaceStatus==key">
              {{ value }}</p>
          </td>
        </tr>
      </table>
      <div class="zf-zc-table--title">接口参数HEADER信息</div>
      <el-table v-loading="loading" :data="interfaceHeaderList" border>
        <el-table-column label="序号" width="50" type="index" align="center">
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="参数名称"
          align="center"
          width="300"
          prop="headerName"
        />
        <el-table-column
          label="参数类型"
          align="center"
          width="200"
          prop="type"
        >
          <template slot-scope="scope">
            {{ scope.row.type | typeHandle }}
          </template>
        </el-table-column>
        <el-table-column
          label="是否必须"
          width="100"
          align="center"
          prop="needStatus"
        >
          <template slot-scope="scope">
            <p v-if="scope.row.needStatus=='Y'">是</p>
            <p v-if="scope.row.needStatus=='N'">否</p>
          </template>
        </el-table-column>
        <el-table-column
          label="描述"
          width="650"
          align="center"
          prop="headerMemo"
        />
      </el-table>

      <div class="zf-zc-table--title">接口参数PARAM信息</div>
      <el-table v-loading="loading" :data="interfaceParamList" border>
        <el-table-column label="序号" width="50" type="index" align="center">
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="参数名称"
          align="center"
          width="300"
          prop="paramName"
        />
        <el-table-column
          label="参数类型"
          width="200"
          align="center"
          prop="type"
        >
          <template slot-scope="scope">
            {{ scope.row.type | typeHandle }}
          </template>
        </el-table-column>
        <el-table-column
          label="是否必须"
          width="100"
          align="center"
          prop="needStatus"
        >
          <template slot-scope="scope">
            <p v-if="scope.row.needStatus=='Y'">是</p>
            <p v-if="scope.row.needStatus=='N'">否</p>
          </template>
        </el-table-column>
        <el-table-column
          label="描述"
          width="650"
          align="center"
          prop="paramMemo"
        />

      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="interfaceView = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 审核信息弹框 -->
    <el-dialog
      title="审核"
      :close-on-click-modal="false"
      :visible.sync="enableView"
      width="70%"
      append-to-body
      height="800px"
      scrollbar
    >
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        label-width="0px"
        class="demo-ruleForm"
      >
        <el-input v-show="false" v-model="form.serverOid"/>
        <div class="zf-zc-table--title">服务基本信息</div>
        <!--<h3>事项标题</h3>-->
        <table
          cellspacing="0"
          cellpadding="0"
          border="0"
          class="zf-zc-table"
        >
          <colgroup>
            <col width="20%"/>
            <col width="30%"/>
            <col width="20%"/>
            <col width="30%"/>
          </colgroup>
          <tr>
            <td><b>用户名称：</b></td>
            <td>
              {{ middleUser.userName }}
            </td>
            <td><b>服务名称：</b></td>
            <td>
              {{ server.serverName }}
            </td>
          </tr>
          <tr>
            <td><b>服务图标：</b></td>
            <td>
              <img width="100px" :src="previewImageUrl+server.fastdfsNginxUrl" />
            </td>
            <td><b>服务版本：</b></td>
            <td>
              {{ server.serverVersion }}
            </td>
          </tr>
          <tr>
            <td><b>服务形式：</b></td>
            <td>
              {{ server.serverStyle | serverStyle }}
            </td>
            <td><b>服务分类：</b></td>
            <td>
              {{ serverType.typeName }}
            </td>
          </tr>
          <tr>
            <td><b>附件：</b></td>
            <td>
              <div v-for="(attaJSONObject, index) in attaJsonArray" :key="index">
                <div v-show="null != attaJSONObject.oid && '' != attaJSONObject.oid">
                  <img width="100px" :src="previewImageUrl+attaJSONObject.fastdfsNginxUrl" />
                </div>
              </div>
            </td>
            <td><b>服务状态：</b></td>
            <td>
              <p v-for="(value,key) in serverStatusOptions" :key="key" v-if="server.serverStatus==key">{{ value }}</p>
            </td>
          </tr>
          <tr>
            <td><b>服务关键字：</b></td>
            <td>
              {{ server.serverKeys }}
            </td>
            <td><b>启禁用状态：</b></td>
            <td>
              {{ server.ableStatus == 'N' ? '禁用' : '启用' }}
            </td>
          </tr>
          <tr>
            <td><b>服务简介：</b></td>
            <td colspan="3">
              {{ server.serverMemo }}
            </td>
          </tr>
          <tr>
            <td><b>用户状态：</b></td>
            <td>
              {{ middleUser.userStatus | userStatus }}
            </td>
            <td><b>用户企业名称：</b></td>
            <td>
              {{ middleUser.companyName }}
            </td>
          </tr>
          <tr>
            <td><b>用户手机：</b></td>
            <td>
              {{ middleUser.mobile }}
            </td>
            <td><b>用户电子邮件：</b></td>
            <td>
              {{ middleUser.email }}
            </td>
          </tr>
          <tr>
            <td><b>审核状态：</b></td>
            <td colspan="3" v-if="examineType=='1'">
              <el-radio-group v-model="form.serverStatus">
                <el-radio :label="'2'">审核通过</el-radio>
                <el-radio :label="'3'">审核不通过</el-radio>
              </el-radio-group>
            </td>
            <td colspan="3" v-else>
              <el-radio-group v-model="form.serverStatus">
                <el-radio :label="'9'">升级审核通过</el-radio>
                <el-radio :label="'8'">升级审核不通过</el-radio>
              </el-radio-group>
            </td>
          </tr>
          <tr>
            <td><b>备注：</b></td>
            <td colspan="3">
              <el-input
                v-model.trim="form.memo"
                placeholder="请输入备注"
                clearable
                size="small"
              />
            </td>
          </tr>
        </table>
        <div class="zf-zc-table--title">接口信息列表</div>
        <el-table v-loading="loading" :data="serverInterfaceList" border>
          <el-table-column label="序号" width="50" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="接口名称"
            align="center"
            width="300"
            prop="interfaceName"
          />
          <el-table-column
            label="请求方式"
            align="center"
            width="100"
            prop="requestMethod"
          >
            <template slot-scope="scope">
              {{ scope.row.requestMethod | requestMethodHandle }}
            </template>
          </el-table-column>
          <el-table-column
            label="请求地址"
            width="600"
            align="center"
            prop="requestAddr"
          />
          <el-table-column
            label="接口状态"
            width="100"
            align="center"
            prop="interfaceStatus"
          >
            <template slot-scope="scope">
              <p v-for="(value,key) in serverStatusOptions" :key="key" v-if="scope.row.interfaceStatus==key">
                {{ value }}</p>
            </template>
          </el-table-column>
          <el-table-column
            label="操作"
            width="150"
            align="center"
          >
            <template slot-scope="scope" >
              <el-button
                v-if="examineType=='1'"
                size="mini"
                type="text"
                icon="iconfont zfsoft-chakan"
                @click="handleInterface(scope.row)"
                v-hasPermi="['im:server:view']"
              >查看
              </el-button>
              <el-button
                v-else
                size="mini"
                type="text"
                icon="iconfont zfsoft-chakan"
                @click="handleInterfaceUP(scope.row)"
                v-hasPermi="['im:server:view']"
              >查看
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <div class="zf-zc-table--title">历史审核记录列表</div>
        <el-table v-loading="loading" :data="serverAuthRecList" border>
          <el-table-column label="序号" width="50" type="index" align="center">
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="服务名称"
            align="center"
            width="300"
            prop="serverName"
          />
          <el-table-column
            label="操作员名称"
            width="200"
            align="center"
            prop="userName"
          />
          <el-table-column
            label="状态"
            width="200"
            align="center"
            prop="serverStatus"
          >
            <template slot-scope="scope">
              <p v-for="(value,key) in serverStatusOptions" :key="key" v-if="scope.row.serverStatus==key">
                {{ value }}</p>
            </template>
          </el-table-column>
          <el-table-column
            label="审核日期"
            width="200"
            align="center"
            prop="createDate"
          />
          <el-table-column
            label="原因"
            width="350"
            align="center"
            prop="memo"
          />
        </el-table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="submitForm">审 核</el-button>
        <el-button @click="enableView = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 历史版本信息列表 -->
    <el-dialog
      title="历史版本"
      :close-on-click-modal="false"
      :visible.sync="historyView"
      width="70%"
      height="400px"
      append-to-body
    >
      <el-input style="display: none"></el-input>
      <el-form
        :model="queryHistoryParams"
        ref="queryForm"
        :inline="true"
        label-width="68px"
      >
        <el-form-item label="服务分类" prop="serverTypeOid">
          <treeselect
          popper-class="treeselect"
            v-model="queryHistoryParams.serverTypeOid"
            :options="serviceTypeOptions"
            style="width: 240px"
            noOptionsText="暂无数据"
            :defaultExpandLevel="1"
            placeholder="请输入服务分类"
            :appendToBody="true"
          />
        </el-form-item>
        <el-form-item label="服务名称" prop="serverName">
          <el-input
            v-model.trim="queryHistoryParams.serverName"
            placeholder="请输入服务名称"
            clearable
            size="small"
          />
        </el-form-item>
        <el-form-item label="服务状态">
          <el-select
            v-model="queryHistoryParams.serverStatus"
            size="small"
            style="width: 240px"
          >
            <el-option
              v-for="(value, key) in serviceTypeMap"
              :key="key"
              :label="value"
              :value="key"
            />
          </el-select>
        </el-form-item>
        <el-form-item class="fr mr0">
          <el-button
            type="primary"
            icon="el-icon-search"
            size="mini"
            @click="historyQuery"
          >搜索
          </el-button
          >
          <el-button
            type="warning"
            icon="el-icon-refresh"
            size="mini"
            @click="resetQuery1"
            class="ml5"
          >重置
          </el-button
          >
        </el-form-item>
      </el-form>
      <el-table
        ref="multipleTable"
        :data="historyServerList"
        tooltip-effect="dark"
        style="width: 100%"
      >
        <el-table-column label="序号" width="55" type="index" align="center">
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="用户名称"
          align="center"
          prop="middleUser.userName"
          show-overflow-tooltip
        />
        <el-table-column
          label="服务名称"
          align="center"
          prop="serverName"
          show-overflow-tooltip
        />
        <el-table-column
          label="服务版本"
          align="center"
          prop="serverVersion"
          show-overflow-tooltip
        />
        <el-table-column
          label="服务分类"
          align="center"
          prop="serverType.typeName"
          show-overflow-tooltip
        />
        <el-table-column
          label="服务状态"
          align="center"
          prop="serverStatus"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            <p v-for="(value,key) in serverStatusOptions" :key="key" v-if="scope.row.serverStatus==key">{{ value }}</p>
          </template>
        </el-table-column>
        <el-table-column
          label="接口数量"
          align="center"
          prop="num"
          show-overflow-tooltip
        />
        <el-table-column
          label="操作"
          align="center"
          class-name="small-padding fixed-width"
        >
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="iconfont zfsoft-chakan"
              @click="handleHistoryView(scope.row)"
              v-hasPermi="['im:server:view']"
            >查看
            </el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <pagination
        v-show="historyTotal > 0"
        :total="historyTotal"
        :page.sync="queryHistoryParams.pageNumber"
        :limit.sync="queryHistoryParams.pageSize"
        @pagination="historyQuery"
      />
      <div  slot="footer" class="dialog-footer">
        <el-button @click="historyView = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 历史版本信息详情 -->
    <el-dialog
      title="历史服务信息"
      :close-on-click-modal="false"
      :visible.sync="oneHistoryView"
      width="70%"
      append-to-body
      height="800px"
      scrollbar
    >
      <div class="zf-zc-table--title">服务基本信息</div>
      <!--<h3>事项标题</h3>-->
      <table
        cellspacing="0"
        cellpadding="0"
        border="0"
        class="zf-zc-table"
      >
        <colgroup>
          <col width="20%"/>
          <col width="30%"/>
          <col width="20%"/>
          <col width="30%"/>
        </colgroup>
        <tr>
          <td><b>用户名称：</b></td>
          <td>
            {{ middleUserH.userName }}
          </td>
          <td><b>服务名称：</b></td>
          <td>
            {{ serverH.serverName }}
          </td>
        </tr>
        <tr>
          <td><b>服务图标：</b></td>
          <td>
            <img width="100px" :src="previewImageUrl+serverH.fastdfsNginxUrl" />
          </td>
          <td><b>服务版本：</b></td>
          <td>
            {{ serverH.serverVersion }}
          </td>
        </tr>
        <tr>
          <td><b>服务形式：</b></td>
          <td>
            {{ serverH.serverStyle | serverStyle }}
          </td>
          <td><b>服务分类：</b></td>
          <td>
            {{ serverTypeH.typeName }}
          </td>
        </tr>
        <tr>
          <td><b>附件：</b></td>
          <td>
            <div v-for="(attaJSONObject, index) in attaJsonArray" :key="index">
              <div v-show="null != attaJSONObject.oid && '' != attaJSONObject.oid">
                <img width="100px" :src="previewImageUrl+attaJSONObject.fastdfsNginxUrl" />
              </div>
            </div>
          </td>
          <td><b>服务状态：</b></td>
          <td>
            <p v-for="(value,key) in serverStatusOptions" :key="key" v-if="serverH.serverStatus==key">{{ value }}</p>
          </td>
        </tr>
        <tr>
          <td><b>服务关键字：</b></td>
          <td>
            {{ serverH.serverKeys }}
          </td>
          <td><b>启禁用状态：</b></td>
          <td>
            {{ serverH.ableStatus == 'N' ? '禁用' : '启用' }}
          </td>
        </tr>
        <tr>
          <td><b>服务简介：</b></td>
          <td colspan="3">
            {{ serverH.serverMemo }}
          </td>
        </tr>
        <tr>
          <td><b>用户状态：</b></td>
          <td>
            {{ middleUserH.userStatus | userStatus }}
          </td>
          <td><b>用户企业名称：</b></td>
          <td>
            {{ middleUserH.companyName }}
          </td>
        </tr>
        <tr>
          <td><b>用户手机：</b></td>
          <td>
            {{ middleUserH.mobile }}
          </td>
          <td><b>用户电子邮件：</b></td>
          <td>
            {{ middleUserH.email }}
          </td>
        </tr>
      </table>
      <div class="zf-zc-table--title">接口信息列表</div>
      <el-table v-loading="loading" :data="serverInterfaceListH" border>
        <el-table-column label="序号" width="50" type="index" align="center">
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="接口名称"
          align="center"
          width="300"
          prop="interfaceName"
        />
        <el-table-column
          label="请求方式"
          align="center"
          width="100"
          prop="requestMethod"
        >
          <template slot-scope="scope">
            {{ scope.row.requestMethod | requestMethodHandle }}
          </template>
        </el-table-column>
        <el-table-column
          label="请求地址"
          width="650"
          align="center"
          prop="requestAddr"
        />
        <el-table-column
          label="接口状态"
          width="100"
          align="center"
          prop="interfaceStatus"
        >
          <template slot-scope="scope">
            <p v-for="(value,key) in serverStatusOptions" :key="key" v-if="scope.row.interfaceStatus==key">
              {{ value }}</p>
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          width="100"
          align="center"
        >
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="iconfont zfsoft-chakan"
              @click="handleInterfaceH(scope.row)"
              v-hasPermi="['im:server:view']"
            >查看
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div slot="footer" class="dialog-footer">
        <el-button @click="oneHistoryView = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 历史版本查看接口信息详细 -->
    <el-dialog
      title="接口信息"
      :close-on-click-modal="false"
      :visible.sync="interfaceHView"
      width="70%"
      append-to-body
      height="800px"
      scrollbar
    >
      <div class="zf-zc-table--title">接口基本信息</div>
      <!--<h3>事项标题</h3>-->
      <table
        cellspacing="0"
        cellpadding="0"
        border="0"
        class="zf-zc-table"
      >
        <colgroup>
          <col width="20%"/>
          <col width="30%"/>
          <col width="20%"/>
          <col width="30%"/>
        </colgroup>
        <tr>
          <td><b>接口名称：</b></td>
          <td>
            {{ serverInterfaceH.interfaceName }}
          </td>
          <td><b>请求方式：</b></td>
          <td>
            {{ serverInterfaceH.requestMethod | requestMethodHandle }}
          </td>
        </tr>
        <tr>
          <td><b>接口简介：</b></td>
          <td colspan="3">
            {{ serverInterfaceH.serverMemo }}
          </td>
        </tr>
        <tr>
          <td><b>返回示例：</b></td>
          <td colspan="3">
            {{ serverInterfaceH.backExample }}
          </td>
        </tr>
        <tr>
          <td><b>使用说明：</b></td>
          <td colspan="3">
            {{ serverInterfaceH.instructions }}
          </td>
        </tr>
        <tr>
          <td><b>请求地址：</b></td>
          <td colspan="3">
            {{ serverInterfaceH.requestAddr }}
          </td>
        </tr>
        <tr>
          <td><b>接口状态：</b></td>
          <td colspan="3">
            <p v-for="(value,key) in serverStatusOptions" :key="key" v-if="serverInterfaceH.interfaceStatus==key">
              {{ value }}</p>
          </td>
        </tr>
      </table>
      <div class="zf-zc-table--title">接口参数HEADER信息</div>
      <el-table v-loading="loading" :data="interfaceHeaderListH" border>
        <el-table-column label="序号" width="50" type="index" align="center">
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="参数名称"
          align="center"
          width="300"
          prop="headerName"
        />
        <el-table-column
          label="参数类型"
          align="center"
          width="200"
          prop="type"
        >
          <template slot-scope="scope">
            {{ scope.row.type | typeHandle }}
          </template>
        </el-table-column>
        <el-table-column
          label="是否必须"
          width="100"
          align="center"
          prop="needStatus"
        >
          <template slot-scope="scope">
            <p v-if="scope.row.needStatus=='Y'">是</p>
            <p v-if="scope.row.needStatus=='N'">否</p>
          </template>
        </el-table-column>
        <el-table-column
          label="描述"
          width="650"
          align="center"
          prop="headerMemo"
        />
      </el-table>

      <div class="zf-zc-table--title">接口参数PARAM信息</div>
      <el-table v-loading="loading" :data="interfaceParamListH" border>
        <el-table-column label="序号" width="50" type="index" align="center">
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="参数名称"
          align="center"
          width="300"
          prop="paramName"
        />
        <el-table-column
          label="参数类型"
          width="200"
          align="center"
          prop="type"
        >
          <template slot-scope="scope">
            {{ scope.row.type | typeHandle }}
          </template>
        </el-table-column>
        <el-table-column
          label="是否必须"
          width="100"
          align="center"
          prop="needStatus"
        >
          <template slot-scope="scope">
            <p v-if="scope.row.needStatus=='Y'">是</p>
            <p v-if="scope.row.needStatus=='N'">否</p>
          </template>
        </el-table-column>
        <el-table-column
          label="描述"
          width="650"
          align="center"
          prop="paramMemo"
        />

      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="interfaceHView = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 预警策略配置信息 -->
    <el-dialog
      title="接口信息"
      :close-on-click-modal="false"
      :visible.sync="strategyView"
      width="70%"
      append-to-body
      height="400px"
      scrollbar
    >
      <div class="zf-zc-table--title">预警策略配置信息</div>
      <!--<h3>事项标题</h3>-->
      <el-form
        ref="noticeRule"
        :model="noticeRule"
        label-width="0px"
        class="demo-ruleForm"
      >
        <table
          cellspacing="0"
          cellpadding="0"
          border="0"
          class="zf-zc-table"
        >
          <colgroup>
            <col width="20%"/>
            <col width="30%"/>
          </colgroup>
          <tr>
            <td><i class="require">*</i><b>通知方式：</b></td>
            <td colspan="3">
              <el-form-item prop="noticeWay">
                <el-checkbox-group v-model="noticeWayList" @change="handleChange">
                  <el-checkbox v-for="date in noticeWayLists" :label="date.label" :key="date.name">{{date.name}}
                  </el-checkbox>
                </el-checkbox-group>
              </el-form-item>
            </td>
          </tr>
          <tr>
            <td><i class="require">*</i><b>通知人：</b></td>
            <td colspan="3">
              <el-form-item prop="userNames">
                <el-input
                  v-model.trim="noticeRule.userNames"
                  readonly
                  show-word-limit
                />
              </el-form-item>
              <el-button
                type="default"
                icon="el-icon-plus"
                size="mini"
                @click="userConfig"
              >添加
              </el-button>
            </td>
          </tr>
        </table>
      </el-form>
      <div slot="footer" align="center">
        <el-button type="primary" @click="submitNoticeRule">确 定</el-button>
        <el-button @click="strategyView = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 用户配置 -->
    <el-dialog
      title="用户配置"
      :close-on-click-modal="false"
      :visible.sync="userView"
      width="70%"
      append-to-body
    >
      <el-form
        :model="queryUserParams"
        ref="queryForm"
        :inline="true"
        label-width="68px"
      >
        <el-form-item label="姓名" prop="userName">
          <el-input
            v-model.trim="queryUserParams.userName"
            placeholder="请输入姓名"
            clearable
            size="small"
          />
        </el-form-item>
        <el-form-item label="手机号" prop="mobile">
          <el-input
            v-model.trim="queryUserParams.mobile"
            placeholder="请输入手机号"
            clearable
            size="small"
          />
        </el-form-item>
        <el-form-item class="fr mr0">
          <el-button
            type="primary"
            icon="el-icon-search"
            size="mini"
            @click="userQuery"
          >搜索
          </el-button
          >
          <el-button
            type="primary"
            icon="el-icon-search"
            size="mini"
            @click="userReset"
          >重置
          </el-button
          >
          <el-button
            type="primary"
            icon="iconfont zfsoft-chakan"
            size="mini"
            @click="addUser"
          >批量选择
          </el-button
          >
        </el-form-item>
      </el-form>
      <el-table
        ref="multipleTable"
        :data="middleUserList"
        tooltip-effect="dark"
        style="width: 100%"
        @selection-change="handleUserChange"
        height="580px"
        scrollbar
      >
        <el-table-column type="selection" width="60" align="center"></el-table-column>
        <el-table-column label="序号" width="55" type="index" align="center">
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="用户名称"
          align="center"
          prop="userName"
          show-overflow-tooltip
        />
        <el-table-column
          label="用户类型"
          align="center"
          prop="userType"
          show-overflow-tooltip
        />
        <el-table-column
          label="手机号"
          align="center"
          prop="mobile"
          show-overflow-tooltip
        />
        <el-table-column
          label="钉钉token信息"
          align="center"
          prop="dingDingToken"
          show-overflow-tooltip
        />
        <el-table-column
          label="邮箱"
          align="center"
          prop="email"
          show-overflow-tooltip
        />
        <el-table-column
          label="操作"
          align="center"
          class-name="small-padding fixed-width"
        >
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="iconfont zfsoft-chakan"
              @click="handleOneUser(scope.row)"
              v-hasPermi="['im:server:view']"
            >选择
            </el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <pagination
        v-show="userTotal > 0"
        :total="userTotal"
        :page.sync="queryUserParams.pageNumber"
        :limit.sync="queryUserParams.pageSize"
        @pagination="userQuery"
      />
      <div slot="footer" class="dialog-footer">
        <el-button @click="userView = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  page,
  getOne,
  getInterface,
  getServerStatus,
  getServiceType,
  enable,
  save,
  examine,
  pageHistory,
  getOneHistory,
  batchDelete,
  getInterfaceH,
  examineUp,
  handleInterfaceUP,
  saveUp,
  check,
  getStrategy,
  getMiddleUser,
  saveStrategy
} from '@/api/middle/server'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import { getSysAttaByAttaOid } from "@/api/sys/atta";

export default {
  components: { Treeselect },
  name: 'server',
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      //历史总条数
      historyTotal: 0,
      //用户总条数
      userTotal: 0,
      title: '',
      //服务分类
      serviceTypeOptions: [],
      serviceTypeMap: { '2': '审核通过', '9': '升级审核通过' },
      //启禁用
      ableStatusOptions: { 'Y': '启用', 'N': '禁用' },
      // optionValsList:[{'valOid':'2','name':'审核通过'},{'valOid':'3','name':'审核不通过'}],
      // 服务状态类型
      serverStatusOptions: {},
      //服务信息列表数据
      serverList: [],
      //历史版本信息
      historyServerList: [],
      // 查看显示弹出层
      openView: false,
      interfaceView: false,
      enableView: false,
      historyView: false,
      oneHistoryView: false,
      interfaceHView: false,
      strategyView: false,
      userView: false,
      previewImageUrl: '/case-api/pic/previewImage?fastdfsNginxUrl=',
      // 查询参数
      queryParams: {
        pageNumber: 1,
        pageSize: 10,
        serverTypeOid: null,
        serverName: '',
        ableStatus: '',
        serverStatus: ''
      },
      //历史版本查询参数
      queryHistoryParams: {
        pageNumber: 1,
        pageSize: 10,
        serverOid: '',
        serverName: '',
        serverStatus: ''
      },
      //用户查询参数
      queryUserParams: {
        pageNumber: 1,
        pageSize: 10,
        userName: '',
        mobile: ''
      },
      //审核提交表单
      form: {
        serverStatus: '',
        memo: '',
        serverOid: '',
        serverUpOid: ''
      },
      //接口信息
      serverInterfaceList: [],
      serverInterfaceListH: [],
      //历史审核信息
      serverAuthRecList: [],
      //查看详细-服务信息
      server: {},
      serverH: {
        fastdfsNginxUrl: ""
      },
      //中台用户
      middleUser: {},
      middleUserH: {},
      //服务分类信息
      serverType: {},
      serverTypeH: {},
      viewDialogOptions: [],
      //服务部署
      noticeRule: {
        userNames:'',
        noticeWay:''
      },
      //通知方式
      noticeWayList: [],
      //通知方式列表
      noticeWayLists: [{
        name: '钉钉token',
        label: 'DD'
      },
        {
          name: '邮件',
          label: 'EMAIL'
        },
        {
          name: '短信',
          label: 'SMS'
        }],
      //用户列表
      middleUserList: [],
      //批量删除
      multipleSelection: [],
      //用户批量
      multipleUser: [],
      //接口信息查看
      serverInterface: {},
      serverInterfaceH: {},
      interfaceHeaderList: [],
      interfaceHeaderListH: [],
      interfaceParamList: [],
      interfaceParamListH: [],
      attaJsonArray: [],
      // 1审核 2升级审核
      examineType: '',
      rules: {
        serverStatus: [
          { required: true, message: '审核状态不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  watch: {},
  created() {
    /** 获取列表数据 */
    this.getList()
    /** 获取服务状态数据 */
    getServerStatus().then(response => {
      this.serverStatusOptions = response
    })
    /** 获取服务分类树数据 */
    getServiceType().then(response => {
      this.serviceTypeOptions = response.data
    })
  },
  methods: {
    /** 查询数据列表 */
    getList() {
      this.loading = true
      page(this.queryParams).then(response => {
        this.serverList = response.data.datas
        this.total = response.data.totalRows
        this.loading = false
      })
    },
    /** 查看按钮操作 */
    handleView(row) {
      const oid = row.oid
      getOne(oid).then(response => {
        this.server = response.data.server
        this.middleUser = response.data.server.middleUser
        this.serverType = response.data.serverType
        this.attaJsonArray = response.data.attaJsonArray
        console.log("====attaJsonArray===")
        console.log(this.attaJsonArray)
        this.serverInterfaceList = response.data.serverInterfaceList
        this.serverAuthRecList = response.data.serverAuthRecList
        this.openView = true
        this.title = '查看服务信息'
      })
    },
    //批量删除
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    submitBatchForm: function() {
      let _that = this
      var oid = []
      if (_that.multipleSelection.length <= 0) {
        _that.$message.error('请选择删除!')
        return false
      }
      for (let ss = 0; ss < _that.multipleSelection.length; ss++) {
        oid.push(_that.multipleSelection[ss].oid)
      }
      var oids = oid.join(',')
      batchDelete(oids).then(response => {
        if (response.code === 200) {
          _that.msgSuccess('删除成功')
          this.getList()
        }
      })
    },
    //打开审核弹框
    handleExamine(row) {
      this.reset(1)
      const oid = row.oid
      examine(oid).then(response => {
        this.server = response.data.server
        this.middleUser = response.data.server.middleUser
        this.serverType = response.data.serverType
        this.serverInterfaceList = response.data.serverInterfaceList
        this.serverAuthRecList = response.data.serverAuthRecList
        this.attaJsonArray = response.data.attaJsonArray
        this.form.serverOid = response.data.server.oid
        this.examineType = '1'
        this.enableView = true
        this.title = '审核服务'
      })
    },
    // 升级审核
    handleExamineUp(row) {
      this.reset(2)
      const oid = row.oid
      examineUp(oid).then(response => {
        this.server = response.data.server
        this.middleUser = response.data.server.middleUser
        this.serverType = response.data.serverType
        this.serverInterfaceList = response.data.serverInterfaceList
        this.serverAuthRecList = response.data.serverAuthRecList
        this.attaJsonArray = response.data.attaJsonArray
        this.form.serverOid = response.data.server.serverOid
        this.form.serverUpOid = response.data.server.oid
        this.examineType = '2'
        this.enableView = true
        this.title = '升级审核服务'
      })
    },
    //提交审核
    submitForm: function() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.examineType == '2') {
            saveUp(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess('保存成功')
                this.enableView = false
                this.getList()
              }
            })
          } else {
            save(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess('保存成功')
                this.enableView = false
                this.getList()
              }
            })
          }
        } else {
          return false
        }
      })
    },
    //打开历史服务弹框
    handleHistory(row) {
      this.loading = true
      this.queryHistoryParams.serverOid = row.oid
      pageHistory(this.queryHistoryParams).then(response => {
        this.historyTotal = response.data.page.totalRows
        this.historyServerList = response.data.page.datas
        // this.interfaceHeaderList = response.data.serverFile
        this.historyView = true
        this.title = '历史服务信息'
        this.loading = false
      })
    },
    //历史中的查看
    handleHistoryView(row) {
      const oid = row.oid
      getOneHistory(oid).then(response => {
        this.serverH = response.data.server
        this.middleUserH = response.data.server.middleUser
        this.serverTypeH = response.data.serverType
        this.serverInterfaceListH = response.data.serverInterfaceList
        this.attaJsonArray = response.data.attaJsonArray
      });
      getSysAttaByAttaOid(this.serverH.iconAttaOid).then(response => {
        this.serverH.fastdfsNginxUrl = response.data.fastdfsNginxUrl
        this.oneHistoryView = true
        this.title = '查看历史服务信息'
        }
      )
    },
    //历史版本中的搜索
    historyQuery() {
      this.loading = true
      pageHistory(this.queryHistoryParams).then(response => {
        this.historyTotal = response.data.page.totalRows
        this.historyServerList = response.data.page.datas
        this.loading = false
      })
    },
    resetQuery1() {
      const serviceOid = this.queryHistoryParams.serverOid;
      this.queryHistoryParams = {};
      this.queryHistoryParams.serverOid = serviceOid;
      pageHistory(this.queryHistoryParams).then(resp => {
        this.historyTotal = resp.data.page.totalRows
        this.historyServerList = resp.data.page.datas
      })
    },
    //查看接口信息
    handleInterface(row) {
      const oid = row.oid
      getInterface(oid).then(response => {
        this.serverInterface = response.data.serverInterface
        this.interfaceHeaderList = response.data.interfaceHeaderList
        this.interfaceParamList = response.data.interfaceParamList
        this.interfaceView = true
        this.title = '查看接口信息'
      })
    },
    //查看接口UP信息
    handleInterfaceUP(row) {
      const oid = row.oid
      handleInterfaceUP(oid).then(response => {
        this.serverInterface = response.data.serverInterface
        this.interfaceHeaderList = response.data.interfaceHeaderList
        this.interfaceParamList = response.data.interfaceParamList
        this.interfaceView = true
        this.title = '接口信息'
      })
    },
    //历史版本查看接口
    handleInterfaceH(row) {
      const oid = row.oid
      console.log(oid)
      getInterfaceH(oid).then(response => {
        this.serverInterfaceH = response.data.serverInterface
        this.interfaceHeaderListH = response.data.interfaceHeaderList
        this.interfaceParamListH = response.data.interfaceParamList
        this.interfaceHView = true
        this.title = '历史版本查看接口信息'
      })
    },
    // 判断是否审核
    isShowAuth(row) {
      //如果服务状态为待审核就添加审核操作
      return row.serverStatus == 1
    },
    // 判断是否升级审核
    isShowAuthUp(row) {
      //如果服务状态为待审核就添加审核操作
      return row.serverStatus == 7
    },
    //启禁用
    handleEnable(row) {
      const oid = row.oid
      let message = ''
      if (row.ableStatus == 'Y') {
        message = '禁用'
      } else {
        message = '启用'
      }
      this.$confirm(
        '你确定要' + message + '吗？',
        '警告',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      )
        .then(function() {
          return enable(oid)
        })
        .then(() => {
          this.getList()
          this.msgSuccess(message + '成功')
        })
        .catch(function() {
        })
    },
    //立即检测
    checkCheck(row) {
      this.$confirm(
        '你确定要检测吗?',
        '警告',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      )
        .then(() => {
          const oid = row.oid
          check(oid).then(response => {
            this.$alert(response.data)
          })
        })
        .catch(function() {
        })
    },
    //通知方式修改
    handleChange(value) {
      let _that = this
      _that.noticeRule.noticeWay = value.join(',')
    },
    //用户批量添加
    handleUserChange(value) {
      this.multipleUser = value
    },
    //预警策略配置
    strategyWarn(row) {
      this.noticeReset()
      const oid = row.oid
      getStrategy(oid).then(response => {
        if (response.data.noticeRule != null) {
          this.noticeRule = response.data.noticeRule
          this.noticeWayList = this.noticeRule.noticeWay.split(',')
        } else {
          this.noticeRule.serverOid = oid
        }
        this.strategyView = true
        this.title = '预警策略配置'
      })
    },
    //批量选择用户
    addUser: function() {
      let _that = this
      var oid = []
      let name = []
      if (_that.multipleUser.length <= 0) {
        _that.$message.error('请至少选择一个通知人!')
        return false
      }
      for (let ss = 0; ss < _that.multipleUser.length; ss++) {
        oid.push(_that.multipleUser[ss].oid)
        name.push(_that.multipleUser[ss].userName)
      }
      //这边应该是选择了用户，把用户oid和姓名存到策略实体类中，未到数据库
      this.noticeRule.userNames = name.join(',')
      this.noticeRule.middleUserOids = oid.join(',')
      this.userView = false
    },
    //单个选择用户
    handleOneUser: function(row) {
      this.noticeRule.userNames = row.userName
      this.noticeRule.middleUserOids = row.oid
      this.userView = false
    },
    //表单提交
    submitNoticeRule: function() {
      this.$refs['noticeRule'].validate(valid => {
        if (valid) {
          console.log(this.noticeRule)
          if(this.noticeRule.noticeWay == undefined || this.noticeRule.noticeWay == ''){
            this.$alert("通知方式至少勾选一种")
            return false
          }
          if(this.noticeRule.userNames == undefined){
            this.$alert("至少选择一个通知人")
            return false
          }
          saveStrategy(this.noticeRule).then(response => {
            if (response.code === 200) {
              this.msgSuccess('保存成功')
              this.strategyView = false
              this.getList()
            }
          })
        } else {
          return false
        }
      })
    },
    //策略中添加用户
    userConfig() {
      //获取用户列表，并打开弹框
      getMiddleUser(this.queryUserParams).then(response => {
        console.log(response.data)
        this.middleUserList = response.data.datas
        this.userTotal = response.data.totalRows
        this.userView = true
        this.title = '选择用户'
      })
    },
    userQuery() {
      this.loading = true
      getMiddleUser(this.queryUserParams).then(response => {
        this.userTotal = response.data.totalRows
        this.middleUserList = response.data.datas
        this.loading = false
      })
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNumber = 1
      this.queryParams.pageSize = 10
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm('queryForm')
      this.queryParams = {}
      this.handleQuery()
    },
    //通知用户配置重置
    /** 重置按钮操作 */
    userReset() {
      this.resetForm('queryUserParams')
      this.queryUserParams = {
        pageNumber: 1,
          pageSize: 10,
          userName: '',
          mobile: ''
      }
      this.userQuery()
    },
    // 表单重置
    reset(type) {
      if(type==1){
        this.form = {
          serverStatus: '2',
          memo: '',
          serverOid: '',
          serverUpOid: ""
        }
      }else {
        this.form = {
          serverStatus: '9',
          memo: '',
          serverOid: '',
          serverUpOid: ""
        }
      }
      this.resetForm('form')
    },
    noticeReset() {
      this.noticeRule = {}
      this.noticeWayList = []
      this.resetForm('noticeRule')
    }

  },
  filters: {
    //服务形式
    serverStyle(data) {
      if (!data) {
        return '未提交'
      }
      const serverStyleMap = {
        0: 'API',
        1: 'HTML5'
      }
      return serverStyleMap[data]
    },

    //用户状态
    userStatus(data) {
      if (!data) {
        return '未提交'
      }
      const userStatusMap = {
        0: '新建',
        1: '待审核',
        2: '审核成功',
        3: '审核失败'
      }
      return userStatusMap[data]
    },

    //接口请求方式
    requestMethodHandle(data) {
      if (!data) {
        return '未提交'
      }
      const requestMethodMap = {
        0: 'GET',
        1: 'POST',
        2: 'PUT',
        3: 'DELETE'
      }
      return requestMethodMap[data]
    },

    //参数类型
    typeHandle(data) {
      if (!data) {
        return '未提交'
      }
      const statusMap = {
        0: '字符串',
        1: '数值',
        2: '日期'
      }
      return statusMap[data]
    }
  }
}
</script>
<style lang="scss" scoped>
 >>> .customClass{
  min-width: 1140px !important;
}
</style>
