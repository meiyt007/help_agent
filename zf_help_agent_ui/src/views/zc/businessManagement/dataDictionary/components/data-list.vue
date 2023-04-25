<template>
  <div class="pack-list">
    <h3>数据集信息</h3>
    <el-table
      :data="dictList"
      border
      style="width: 100%"
      v-loading="loadingTable"
    >
      <el-table-column type="index" label="序号" width="60"> </el-table-column>
      <el-table-column prop="packingTime" label="打包时间" width="220">
      </el-table-column>
      <el-table-column prop="publishExplain" label="描述" width="220">
      </el-table-column>
      <el-table-column label="下载" width="130">
        <template slot-scope="scope">
          <div class="download-btn" @click="downloadZipHandle(scope.row.id)">
            <img
              src="@/assets/image/intelligent/download.png"
              alt=""
              srcset=""
            />
            下载
          </div>
        </template>
      </el-table-column>
      <el-table-column label="发布状态" width="150">
        <template slot-scope="scope">
          <div
            class="fb-status"
            :class="{ 'wait-status': scope.row.isPublish == 0 }"
          >
            {{ scope.row.isPublish == 0 ? '待发布' : '已发布' }}
          </div>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <div class="handle-group">
            <el-button
              type="primary"
              size="mini"
              icon="el-icon-s-promotion"
              class="fb-btn"
              v-if="scope.row.isPublish == 0"
              @click="updatePublishHandle(scope.row, scope.row.id)"
              >发布</el-button
            >
            <el-button
              type="danger"
              size="mini"
              icon="el-icon-delete"
              class="delete-btn"
              @click="deleteZipHandle(scope.row.id)"
              >删除</el-button
            >
          </div>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <el-pagination
      background
      @current-change="handleCurrentChange"
      :current-page.sync="pageNum"
      :page-size="pageSize"
      layout="total, prev, pager, next"
      :total="total"
    >
    </el-pagination>
    <!-- 打包 -->
    <!-- <el-button
      type="primary"
      size="medium"
      class="btn-pack"
      @click="$emit('packListHandle')"
      >打包数据集</el-button
    > -->
  </div>
</template>

<script>
import {
  getDictApi,
  queryTDataSetZipRecS,
  downloadZip,
  updateIsPublish,
  deleteZipList,
} from "@/api/zc/businessManagement/dict";

import axios from 'axios';
import fileDownload from "js-file-download";

export default {
  name: "dataList",
  data () {
    return {
      dictList: [],
      checked: false,
      textarea: "",
      pageNum: 1,
      pageSize: 10,
      total: null,
      loadingTable: true,
    };
  },
  props: {},
  created () {
    this.getAllDictList();
  },
  methods: {
    //获取打包列表数据
    getAllDictList () {
      queryTDataSetZipRecS({
        pageNum: this.pageNum,
        pageSize: this.pageSize,
      }).then((res) => {
        if (res.code == 200) {
          this.dictList = res.data.data;
          this.pageNum = res.data.pageNum;
          this.pageSize = res.data.pageSize;
          this.total = res.data.total;
          this.loadingTable = false;
        }
      });
    },
    //分页
    handleCurrentChange (val) {
      this.pageNum = val;
      this.getAllDictList();
    },

    //下载压缩文件
    downloadZipHandle (zipRecId) {
      axios({
        method: "GET",
        url:
          process.env.VUE_APP_BASE_API +
          process.env.VUE_APP_DSXBL_ROUTE_PATH +
          "/pic/download",
        params: {
          zipRecId: zipRecId,
        },
        responseType: "blob",
      })
        .then((res) => {
          if (res.status == 200) {
            const contentDisposition = res.headers["content-disposition"];
            // 运用window.decodeURI来解码，解决中文乱码问题
            let fileName = window.decodeURI(
              contentDisposition.substring(contentDisposition.indexOf("=") + 1)
            );
            // fileDownload(res.data,fileName)
            let blob = new Blob([res.data], { type: "application/zip" });
            let url = window.URL.createObjectURL(blob);
            if (window.navigator.msSaveOrOpenBlob) {
              //msSaveOrOpenBlob方法返回bool值
              navigator.msSaveBlob(blob, fileName); //本地保存
            } else {
              let link = document.createElement("a"); //a标签下载
              link.href = url;
              link.download = fileName;
              document.body.appendChild(link);
              link.click();
              window.URL.revokeObjectURL(url);
              document.body.removeChild(link);
            }
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },

    //发布
    updatePublishHandle (row, zipRecId) {
      this.$confirm("确定发布?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          updateIsPublish({
            id: zipRecId,
            isPublish: 1,
          }).then((res) => {
            if (res.code == 200) {
              row.isPublish = 0;
              this.$message.success("发布成功");
              this.getAllDictList();
            }
          });
        })
        .catch(() => {
          this.$message.info("取消发布");
        });
    },

    //删除数据集列表
    deleteZipHandle (zipRecId) {
      this.$confirm("此操作将永久删除该文件, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          deleteZipList({
            id: zipRecId,
          }).then((res) => {
            if (res.data.status == 0) {
              this.$message.success(res.data.msg);
              this.getAllDictList();
            }else if(res.data.status == 1){
              this.$message.error(res.data.msg);
            }
          });
        })
        .catch((err) => {
          this.$message.info("已取消删除");
        });
    },
  },
};
</script>

<style lang="less" scoped>
.pack-list {
  padding: 20px;
  h3 {
    font-size: 14px;
    color: #2a344c;
    position: relative;
    margin: 0 0 30px 0;
    &::before {
      position: absolute;
      content: '';
      left: -10px;
      top: 0px;
      content: '';
      width: 3px;
      height: 15px;
      background-color: #2e7dff;
    }
  }
  .download-btn {
    cursor: pointer;
    color: #0e54ff;
    img {
      display: inline-block;
      vertical-align: middle;
      width: 18px;
      height: 16px;
    }
  }
  .fb-status {
    color: #999;
  }
  .wait-status {
    color: #3a47bf;
  }
  .handle-group {
    .el-button {
      border: none;
      background-color: inherit !important;
      box-shadow: none;
      color: #0e54ff;
      font-size: 14px;
      padding: 0 5px;
    }
    .delete-btn {
      color: #f74601;
    }
  }

  .btn-pack {
    display: block;
    margin: 30px auto 0 auto;
  }
  ::v-deep.el-table td,
  ::v-deep.el-table th {
    text-align: center;
  }
}
.el-pagination {
  margin-top: 15px;
  text-align: right;
}
::v-deep.el-pagination__total {
  height: 33px !important;
  line-height: 33px !important;
}
</style>
