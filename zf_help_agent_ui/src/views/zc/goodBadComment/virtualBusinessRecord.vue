<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true">
      <el-form-item label="办事人员姓名" label-width="100px">
        <el-input
          v-model.trim="queryParams.caseUserName"
          placeholder="请输入办事人员姓名"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="工作人员姓名" label-width="100px">
        <el-input
          v-model.trim="queryParams.createUserName"
          placeholder="请输入工作人员姓名"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="服务评价" label-width="70px">
        <treeselect
          v-model="queryParams.serviceEvaluation"
          :options="serviceEvaluationOptions"
          style="width: 240px"
          noOptionsText="暂无数据"
          :defaultExpandLevel="1"
          placeholder="请输入服务评价"
        />
      </el-form-item>
      <el-form-item label="是否推送" label-width="70px">
        <treeselect
          v-model="queryParams.pushFlag"
          :options="pushOptions"
          style="width: 240px"
          noOptionsText="暂无数据"
          :defaultExpandLevel="1"
          placeholder="请输入是否推送"
        />
      </el-form-item>
      <el-form-item class="fr mr0" label-width="50px">
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
    <el-table
      v-loading="loading"
      :data="virtualBusinessRecordList"
      border
      height="calc(100% - 120px)"
    >
      <el-table-column label="序号" align="center" type="index" />
      <el-table-column
        label="办事人员姓名"
        align="center"
        prop="caseUserName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="联系电话"
        align="center"
        prop="phone"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="服务评价"
        align="center"
        prop="serviceEvaluation"
        :show-overflow-tooltip="true"
      >
        <template slot-scope="{ row: { serviceEvaluation } }">
          <span v-if="serviceEvaluation === '5'">非常满意</span>
          <span v-else-if="serviceEvaluation === '4'">满意</span>
          <span v-else-if="serviceEvaluation === '3'">基本满意</span>
          <span v-else-if="serviceEvaluation === '2'">不满意</span>
          <span v-else-if="serviceEvaluation === '1'">非常不满意</span>
        </template>
      </el-table-column>
      <el-table-column
        label="工作人员姓名"
        align="center"
        prop="createUserName"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="评分时间"
        align="center"
        prop="createDate"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="是否推送好差评"
        align="center"
        prop="pushFlag"
        :show-overflow-tooltip="true"
      >
        <template slot-scope="{ row: { pushFlag } }">
          <span v-if="pushFlag === 0">已推送</span>
          <span v-else-if="pushFlag === 1">未推送</span>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        width="400"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-chakan"
            @click="handleView(scope.row)"
            v-hasPermi="['sys:user:view']"
            >查看
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-shouquan"
            @click="handleEdit(scope.row)"
            v-hasPermi="['sys:user:edit']"
            >编辑
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-shouquan"
            @click="handlePush(scope.row)"
            v-hasPermi="['sys:user:push']"
            >推送
          </el-button>
<!--          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-shouquan"
            @click="handleEmotion(scope.row)"
            v-hasPermi="['sys:user:emotion']"
            v-if="scope.row.emotionShow"
            >人脸情绪
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-shouquan"
            @click="emotionCount(scope.row)"
            v-if="scope.row.emotionShow"
            v-hasPermi="['sys:user:count']"
            >情绪统计
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="iconfont zfsoft-shouquan"
            @click="handleAudio(scope.row)"
            v-if="scope.row.audioShow"
            v-hasPermi="['sys:user:audio']"
            >录音播放下载
          </el-button>-->
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

    <!--编辑-->
    <el-dialog
      title="编辑"
      :visible.sync="editFlag"
      v-if="editFlag"
      :close-on-click-modal="false"
      width="1100px"
      v-dialog-drag
    >
      <div>
        <el-form ref="form" :model="form" :rules="rules" label-width="0px">
          <div class="zf-zc-table--title">用户评价信息</div>
          <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
            <colgroup>
              <col width="15%" />
              <col width="30%" />
              <col width="15%" />
              <col width="40%" />
            </colgroup>
            <tr>
              <td><b>办事人员：</b></td>
              <td>
                {{ form.caseUserName }}
              </td>
              <td><b>服务评价：</b></td>
              <td>
                <treeselect
                  v-model="form.serviceEvaluation"
                  :options="serviceEvaluationOptions"
                  style="width: 240px"
                  noOptionsText="暂无数据"
                  :defaultExpandLevel="1"
                  placeholder="请输入服务评价"
                />
              </td>
            </tr>
            <tr>
              <td><b>联系电话：</b></td>
              <td>
                {{ form.phone }}
              </td>
              <td><b>工作人员：</b></td>
              <td>
                {{ form.createUserName }}
              </td>
            </tr>
            <tr>
              <td><b>事项名称：</b></td>
              <td>
                {{ form.serviceName }}
              </td>
              <td><b>办件编号：</b></td>
              <td>
                {{ form.caseNumber }}
              </td>
            </tr>
            <tr>
              <td><b>具体评价项：</b></td>
              <td colspan="3">
                {{ form.evaluationItem }}
              </td>
            </tr>
          </table>
          <div class="zf-zc-table--title" v-if="editShow">情绪评价信息</div>
          <table
            cellspacing="0"
            cellpadding="0"
            border="0"
            class="zf-zc-table"
            v-if="editShow"
          >
            <colgroup>
              <col width="15%" />
              <col width="30%" />
              <col width="15%" />
              <col width="40%" />
            </colgroup>
            <tr>
              <td><b>满意度分：</b></td>
              <td>
                {{ form.satisfactionNum }}
              </td>
              <td><b>情绪评价：</b></td>
              <td>
                <template v-if="form.emotionEvaluation === '5'"
                  >非常满意</template
                >
                <template v-if="form.emotionEvaluation === '4'">满意</template>
                <template v-if="form.emotionEvaluation === '3'"
                  >基本满意</template
                >
                <template v-if="form.emotionEvaluation === '2'"
                  >不满意</template
                >
                <template v-if="form.emotionEvaluation === '1'"
                  >非常不满意</template
                >
              </td>
            </tr>
          </table>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitEdit">确 定</el-button>
        <el-button @click="editFlag = false">取 消</el-button>
      </div>
    </el-dialog>

    <!--编辑-->
    <el-dialog
      title="查看"
      :visible.sync="viewFlag"
      v-if="viewFlag"
      :close-on-click-modal="false"
      width="1100px"
      v-dialog-drag
    >
      <div>
        <el-form ref="form" :model="form" :rules="rules" label-width="0px">
          <div class="zf-zc-table--title">用户评价信息</div>
          <table cellspacing="0" cellpadding="0" border="0" class="zf-zc-table">
            <colgroup>
              <col width="15%" />
              <col width="30%" />
              <col width="15%" />
              <col width="40%" />
            </colgroup>
            <tr>
              <td><b>办事人员：</b></td>
              <td>
                {{ form.caseUserName }}
              </td>
              <td><b>服务评价：</b></td>
              <td>
                <template v-if="form.serviceEvaluation === '5'"
                  >非常满意</template
                >
                <template v-if="form.serviceEvaluation === '4'">满意</template>
                <template v-if="form.serviceEvaluation === '3'"
                  >基本满意</template
                >
                <template v-if="form.serviceEvaluation === '2'"
                  >不满意</template
                >
                <template v-if="form.serviceEvaluation === '1'"
                  >非常不满意</template
                >
              </td>
            </tr>
            <tr>
              <td><b>联系电话：</b></td>
              <td>
                {{ form.phone }}
              </td>
              <td><b>工作人员：</b></td>
              <td>
                {{ form.createUserName }}
              </td>
            </tr>
            <tr>
              <td><b>事项名称：</b></td>
              <td>
                {{ form.serviceName }}
              </td>
              <td><b>办件编号：</b></td>
              <td>
                {{ form.caseNumber }}
              </td>
            </tr>
            <tr>
              <td><b>具体评价项：</b></td>
              <td colspan="3">
                {{ form.evaluationItem }}
              </td>
            </tr>
          </table>
          <div class="zf-zc-table--title" v-if="isShow">情绪评价信息</div>
          <table
            cellspacing="0"
            cellpadding="0"
            border="0"
            class="zf-zc-table"
            v-if="isShow"
          >
            <colgroup>
              <col width="15%" />
              <col width="30%" />
              <col width="15%" />
              <col width="40%" />
            </colgroup>
            <tr>
              <td><b>满意度分：</b></td>
              <td>
                {{ form.satisfactionNum }}
              </td>
              <td><b>情绪评价：</b></td>
              <td>
                <template v-if="form.emotionEvaluation === '5'"
                  >非常满意</template
                >
                <template v-if="form.emotionEvaluation === '4'">满意</template>
                <template v-if="form.emotionEvaluation === '3'"
                  >基本满意</template
                >
                <template v-if="form.emotionEvaluation === '2'"
                  >不满意</template
                >
                <template v-if="form.emotionEvaluation === '1'"
                  >非常不满意</template
                >
              </td>
            </tr>
          </table>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer" style="text-align: center">
        <el-button @click="viewFlag = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 人脸情绪记录 -->
    <el-dialog
      v-dialog-drag
      :visible.sync="emotionFlag"
      v-if="emotionFlag"
      title="人脸情绪记录"
      width="1100px"
      height="700px"
      scrollbar
      append-to-body
    >
      <emotion
        :virtualBusinessRecordOid="form.virtualBusinessNum"
        :caseUserName="form.caseUserName"
        :createUserName="form.createUserName"
        :caseOid="form.caseOid"
        @emotionClose="emotionClose"
      ></emotion>
      <div slot="footer" style="text-align: center">
        <el-button @click="emotionFlag = false"> 关闭 </el-button>
      </div>
    </el-dialog>

    <el-dialog
      v-dialog-drag
      :visible.sync="drawFlag"
      v-if="drawFlag"
      title="人脸情绪数据统计"
      width="900px"
      append-to-body
    >
      <template>
        <div id="myChart" :style="{ width: '100%', height: '410px' }"></div>
      </template>
      <div slot="footer" style="text-align: center">
        <el-button @click="drawFlag = false">关 闭</el-button>
      </div>
    </el-dialog>

    <el-dialog
      title="录音播放"
      v-dialog-drag
      :visible.sync="audioFlag"
      v-if="audioFlag"
      width="550px"
      height="200px"
      append-to-body
    >
      <template>
        <div style="margin-top: 10px; height: 38px; outline: none">
          <audio
            :src="audioAddress"
            controls="controls"
            style="width: 100%; outline: none"
          ></audio>
        </div>
        <div style="text-align: center; margin-top: 35px">
          <el-button @click="audioFlag = false">关 闭</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>
<script>
import Treeselect from '@riophae/vue-treeselect'
import iconfont from '@/views/common/iconfontSelect'
import emotion from '@/views/zc/goodBadComment/emotion'
import {
  getSatisfactionList, page, getManualEvaluation,
  updateManualEvaluation, pushManualEvaluation, getEmotionListById,
  getEmotionList, getVirtualBusinessRecordByOid, getOneCase, getThingCase
} from '@/api/zc/goodBadComment/virtualBusinessRecord'
import { mapGetters } from 'vuex'
import { getConfigByCode } from "@/api/sys/config";
export default {
  name: "VirtualBusinessRecord",
  components: { Treeselect, iconfont, emotion, },

  data () {
    return {
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        caseUserName: "",
        createUserName: "",
        serviceEvaluation: null,
        pushFlag: null,
      },
      form: {
        caseUserName: "",
        createUserName: "",
        serviceEvaluation: "",
        phone: "",
        evaluationItem: "",
        emotionEvaluation: "",
        satisfactionNum: "",
        virtualBusinessNum: "",
        oid: "",
        caseOid: "",
        caseNumber: "",
        serviceName: ""
      },
      loading: false,
      virtualBusinessRecordList: [],
      total: 0,
      pushOptions: [{ id: 0, label: "已推送" }, { id: 1, label: "未推送" }],
      serviceEvaluationOptions: [],
      editFlag: false,
      viewFlag: false,
      emotionFlag: false,
      drawFlag: false,
      isShow: true,
      editShow: true,
      audioFlag: false,
      audioAddress: "",
      seriesData: [],
      xAxisData: [],
      audio: {
        // 该字段是音频是否处于播放状态的属性
        playing: false
      },
      rules: {
        serviceEvaluation: [
          {
            required: true,
            message: "所选服务评价不能为空",
            trigger: "blur"
          }
        ]
      }
    }
  },
  computed: {
    ...mapGetters([
      "wgpjFlag"
    ]),
  },
  created () {
    this.getSatisfactionList();
    this.getList();
  },
  filters: {
    // 使用组件过滤器来动态改变按钮的显示
    transPlayPause (value) {
      return value ? '暂停' : '播放'
    }
  },
  methods: {
    getSatisfactionList () {
      getSatisfactionList().then(res => {
        this.serviceEvaluationOptions = res;
      })
    },
    /** 搜索按钮操作 */
    handleQuery () {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    resetQuery () {
      this.queryParams.caseUserName = ""
      this.queryParams.createUserName = ""
      this.queryParams.serviceEvaluation = null
      this.queryParams.pushFlag = null
      this.getList();
    },
    async getList () {
      this.loading = true;
      let that = this;
      await page(this.queryParams)
        .then(response => {
          var list = response.data.data;
          this.total = response.data.total;
          for (let i = 0; i < list.length; i++) {
            if (list[i].virtualBusinessNum == "") {
              this.$set(list[i], "emotionShow", false);
              this.$set(list[i], "audioShow", false);
            } else {
              getEmotionList({
                pageNum: 1,
                pageSize: 10,
                virtualBusinessOid: list[i].virtualBusinessNum,
                emotion: "",
                score: "",
                caseOid: list[i].caseOid
              }).then(res => {
                if (res.data.data.length > 0) {
                  this.$set(list[i], "emotionShow", true);
                } else {
                  this.$set(list[i], "emotionShow", false);
                }
              })
              getVirtualBusinessRecordByOid(list[i].virtualBusinessNum).then(res => {
                if (res.code == 200) {
                  if (res.data.audioAddress != null && res.data.audioAddress != "") {
                    this.$set(list[i], "audioShow", true);
                  } else {
                    this.$set(list[i], "audioShow", false);
                  }
                }
              })
            }
          }
          this.virtualBusinessRecordList = list;
          console.log(this.virtualBusinessRecordList)
          this.loading = false;
        })
        .catch(function () {
          that.loading = false;
        });
    },
    async handleEdit (row) {
      if (row.serviceEvaluation > 3 && row.serviceEvaluation != "" && row.serviceEvaluation != null && row.serviceEvaluation != undefined) {
        this.$alert('好评不可编辑', '提示', {
          confirmButtonText: '确定',
        });

      } else {
        var isWgpj = false ;
        await getConfigByCode("WGPJ")
          .then(res=> {
            if(res.code == 200 && res.data.isAble == 1 && res.data.value == "0"){
              isWgpj =true;
            }
          });
        getManualEvaluation(row.oid).then(res => {
          if (res.code == 200) {
            this.form.caseUserName = res.data.caseUserName;
            this.form.createUserName = res.data.createUserName;
            this.form.serviceEvaluation = res.data.serviceEvaluation;
            this.form.phone = res.data.phone;
            this.form.emotionEvaluation = res.data.emotionEvaluation;
            this.form.satisfactionNum = res.data.satisfactionNum;
            this.form.evaluationItem = res.data.evaluationItem;
            this.form.virtualBusinessNum = res.data.virtualBusinessNum;
            this.form.caseNumber = row.caseNumber;
            this.form.oid = res.data.oid;
            if (this.form.satisfactionNum == "" && this.form.emotionEvaluation == "") {
              this.editShow = false;
            } else if (isWgpj == false) {
              this.editShow = false;
            } else if (row.emotionShow == false) {
              this.editShow = false;
            }
            if (this.form.emotionEvaluation < 4) {
              this.editShow = false;
            }
            this.editFlag = true;
          }
        })
        getOneCase(row.caseNumber).then(response => {
          this.form.serviceName = response.data.serviceName;
        })
      }
    },
    submitEdit () {
      if (this.form.serviceEvaluation == "" || this.form.serviceEvaluation == null) {
        this.$alert('服务评价不能为空', '提示', {
          confirmButtonText: '确定',
        });
      } else {
        updateManualEvaluation(this.form).then(res => {
          if (res.code == 200) {
            this.editFlag = false;
            this.getList();
          } else {
            this.$message.error("保存失败！")
          }
        }).catch(function () {
          this.editFlag = false;
        })
      }
    },
    handleAudio (row) {
      this.audioFlag = true;
      getVirtualBusinessRecordByOid(row.virtualBusinessNum).then(res => {
        if (res.code == 200) {
          if (res.data != null && res.data != "") {
            this.audioAddress = res.data.audioAddress;
          }
        }
      })
    },
    handleView (row) {
      this.isShow = true;
      this.form.serviceName = "";
      getManualEvaluation(row.oid).then(res => {
        if (res.code == 200) {
          this.form.caseUserName = res.data.caseUserName;
          this.form.createUserName = res.data.createUserName;
          this.form.serviceEvaluation = res.data.serviceEvaluation;
          this.form.phone = res.data.phone;
          this.form.emotionEvaluation = res.data.emotionEvaluation;
          this.form.satisfactionNum = res.data.satisfactionNum;
          this.form.evaluationItem = res.data.evaluationItem;
          this.form.caseNumber = row.caseNumber;
          if (this.form.satisfactionNum == "" && this.form.emotionEvaluation == "") {
            this.isShow = false;
          } else if (this.wgpjFlag == false) {
            this.isShow = false;
          } else if (row.emotionShow == false) {
            this.isShow = false;
          }
          this.viewFlag = true;
        }
      })
      getOneCase(row.caseNumber).then(response => {
        if (response.data.serviceName != "" && response.data.serviceName != null) {
          this.form.serviceName = response.data.serviceName;
        }
      })
      getThingCase(row.caseNumber).then(response => {
        if (response.data != "" && response.data != null) {
          this.form.serviceName = response.data.projectName;
        }
      })
    },
    handlePush (row) {
      if (row.pushFlag == 0) {
        this.$alert('不可推送', '提示', {
          confirmButtonText: '确定',
        });
      } else {
        if (row.pushFlag != "" && row.pushFlag != null && row.pushFlag != undefined) {
          const oid = row.oid;
          this.$confirm('是否确认推送?', "警告", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(function () {
            return pushManualEvaluation(oid);
          }).then(() => {
            this.msgSuccess("推送成功");
            this.getList();
          }).catch(function () { });
        }
      }
    },
    handleEmotion (row) {
      this.form.virtualBusinessNum = row.virtualBusinessNum;
      this.form.caseUserName = row.caseUserName;
      this.form.createUserName = row.createUserName;
      this.form.caseOid = row.caseOid;
      this.emotionFlag = true;
    },
    emotionClose () { this.emotionFlag = false; },
    emotionCount (row) {
      this.xAxisData = [];
      this.seriesData = [];
      let queryParamsId = {};
      queryParamsId.virtualBusinessOid = row.virtualBusinessNum;
      queryParamsId.caseOid = row.caseOid;
      getEmotionListById(queryParamsId).then(res => {
        if (res.code == 200) {
          var emotionList = res.data;
          for (let i = 0; i < emotionList.length; i++) {
            var temp = { name: emotionList[i].createDate, value: emotionList[i].score }
            this.xAxisData.push(emotionList[i].createDate);
            this.seriesData.push(temp);
          }
          console.log(this.xAxisData, this.seriesData)
          this.drawFlag = true;
          this.$nextTick(() => {
            this.drawLine();
          });
        }
      })
    },
    drawLine () {
      // 基于准备好的dom，初始化echarts实例
      let myChart = this.$echarts.init(document.getElementById("myChart"));
      // 绘制图表

      myChart.setOption({
        tooltip: {
          trigger: 'axis'
        },
        grid: {
          x: "7%",// x 偏移量
          y: "5%",// y 偏移量
          width: "87%", // 宽度
          height: "79%"// 高度
        },
        xAxis: [
          {
            type: 'category',
            data: this.xAxisData,
          }
        ],
        yAxis: {
          type: "value",
          // 网格线设置
          splitLine: {
            show: false,
          },
          axisLable: {
            show: false,
          }
        },
        series:
          [
            {
              name: '分数',
              type: 'line',
              data: this.seriesData,
            }
          ],
      });
    },
  },
}

</script>
<style>
::v-deep audio:focus {
  outline: none;
}
</style>
