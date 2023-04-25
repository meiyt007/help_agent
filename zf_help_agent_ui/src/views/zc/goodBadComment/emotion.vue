<template>
  <div>
    <el-form
      :model="queryParams"
      ref="queryForm"
      :inline="true"
      label-width="70px"
    >
      <el-form-item label="情绪">
        <el-input
          v-model.trim="queryParams.emotion"
          placeholder="请输入情绪"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="满意度分">
        <el-input
          v-model.trim="queryParams.score"
          placeholder="请输入满意度分"
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
    <el-table v-loading="loading" :data="emotionList" border>
      <el-table-column label="序号" align="center" type="index" width="60" />
      <el-table-column
        label="办事人员姓名"
        align="center"
        prop="caseUserName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="满意度分"
        align="center"
        prop="score"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="情绪"
        align="center"
        prop="emotion"
        :show-overflow-tooltip="true"
      >
      </el-table-column>

      <el-table-column
        label="情绪置信度"
        align="center"
        prop="emotionDegree"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="工作人员姓名"
        align="center"
        prop="createUser"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="时间"
        align="center"
        prop="createDate"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="操作"
        align="center"
        width="120"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-chakan"
            @click="handleView(scope.row)"
            v-hasPermi="['qdgl:link:view']"
            >查看详情</el-button
          >
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

    <el-dialog
      title="查看情绪信息"
      :visible.sync="emotionView"
      v-if="emotionView"
      :close-on-click-modal="false"
      width="50%"
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
          <td><b>办事人员姓名：</b></td>
          <td>
            {{ form.caseUserName }}
          </td>
          <td><b>满意度分：</b></td>
          <td>
            {{ form.score }}
          </td>
        </tr>
        <tr>
          <td><b>情绪：</b></td>
          <td>{{ form.emotion }}</td>
          <td><b>情绪置信度：</b></td>
          <td>
            {{ form.emotionDegree }}
          </td>
        </tr>
        <tr>
          <td><b>工作人员姓名：</b></td>
          <td>
            {{ form.createUser }}
          </td>
          <td><b>时间：</b></td>
          <td>
            {{ form.createDate }}
          </td>
        </tr>
        <tr v-if="emotionImage != '' && emotionImage != null">
          <td>
            <b>人脸情绪图片：</b>
          </td>
          <td colspan="3">
            <div>
              <img style="width: 300px; height: 220px" :src="emotionImage" />
            </div>
          </td>
        </tr>
      </table>
      <div slot="footer" class="dialog-footer" style="text-align: center">
        <el-button @click="viewCancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { getEmotionList, getEmotionRecognitionRecordById } from '@/api/zc/goodBadComment/virtualBusinessRecord'
import iconfont from '@/views/common/iconfontSelect'
import { mapGetters } from "vuex";
import { FASTDFS_PATH } from "@/utils/config";
export default {
  name: 'emotion',
  components: { iconfont, },
  props: ['virtualBusinessRecordOid', 'caseUserName', 'createUserName', 'caseOid'],
  data () {
    return {
      // 遮罩层
      loading: false,
      emotionList: [],
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        virtualBusinessOid: this.virtualBusinessRecordOid,
        caseOid: this.caseOid,
        emotion: "",
        score: "",
      },
      form: {},
      emotionView: false,
      emotionImage: ""
    };
  },
  computed: {
    ...mapGetters(["wgtpFlag", "deviceMap"])
  },
  created () {
    this.getList();
  },
  methods: {
    /** 搜索按钮操作 */
    handleQuery () {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    resetQuery () {
      this.queryParams.emotion = "";
      this.queryParams.score = "";
      this.getList();
    },
    getList () {
      if (this.queryParams.virtualBusinessOid != "" && this.queryParams.virtualBusinessOid != null) {
        this.emotionList = [];
        this.loading = true;
        getEmotionList(this.queryParams).then(res => {
          if (res.code == 200) {
            var data = res.data.data;
            for (let i = 0; i < data.length; i++) {
              this.emotionList.push({
                id: data[i].id,
                code: data[i].code,
                photoNum: data[i].photoNum,
                emotion: data[i].emotion,
                emotionDegree: data[i].emotionDegree,
                emotionTop: data[i].emotionTop,
                emotionRight: data[i].emotionRight,
                emotionBottom: data[i].emotionBottom,
                emotionLeft: data[i].emotionLeft,
                createDate: data[i].createDate,
                createUser: this.createUserName,
                caseUserName: this.caseUserName,
                score: data[i].score,
                virtualBusinessOid: data[i].virtualBusinessOid,
                location: data[i].location,
                types: data[i].types,
                picAddress: data[i].picAddress,
              })
            }
            console.log(this.emotionList)
            this.total = res.data.total;
            this.loading = false;
          }
        }).catch(function () {
        });
      }
    },
    handleView (row) {
      this.emotionImage = "";
      getEmotionRecognitionRecordById(row.id).then(res => {
        if (res.code == 200) {
          this.form.caseUserName = this.caseUserName;
          this.form.score = res.data.score;
          this.form.emotion = res.data.emotion;
          this.form.emotionDegree = res.data.emotionDegree;
          this.form.createUser = this.createUserName;
          this.form.createDate = res.data.createDate;
          if (res.data.picAddress != "" && res.data.picAddress != null && res.data.picAddress != "null" && this.wgtpFlag == true) {
            let url = window.location.origin;
            // var imageData = res.data.picAddress.split(process.env.VUE_APP_FASTDFS_API);
            var imageData = res.data.picAddress.split(this.deviceMap?.[FASTDFS_PATH]);
            this.emotionImage = url + process.env.VUE_APP_IMAGE_API + imageData[1];
          }
          this.emotionView = true;
        }
      })
    },
    viewCancel () {
      this.emotionView = false;
      this.form.caseUserName = "";
      this.form.score = "";
      this.form.emotion = "";
      this.form.emotionDegree = "";
      this.form.createUser = "";
      this.form.createDate = "";
    },
  },
}
</script>
