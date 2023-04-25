<template>
  <div class="verfication">
    <el-table :data="tableData" style="width: 100%">
      <el-table-column prop="sort" width="50" label="序号" align="center"></el-table-column>
      <el-table-column prop="rulesName" label="预检项" show-overflow-tooltip >
         <template slot-scope="{row}">
          <div class="infoMessage">
         <span>{{row.rulesName}}</span>
         <i style="color:#E6A23C;margin-left:8px" class="el-icon-warning" v-show="row.message"></i>
          <span style="color:#E6A23C;">{{row.message}}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="预检结果" align="center" width="230">
        <template slot-scope="{row}">
          <div class="oprate">
            	<span v-if="row.status == 1" style="color:#05B495;">
						<i class="el-icon-success"></i> <span style="margin-left:8px">符合</span>
					</span>
					<span v-else style="color:red;" >
						<i class="el-icon-error"></i><span style="margin-left:8px">不符合</span>
					</span>
            <!-- <div class="check"  @click="handleValidate(row)">
              <img :src="require('@/assets/image/intelligent/check.png')" alt="">
              <el-link type="primary">核验</el-link>
            </div>
            <div :class="row.attaOid?'see':'noSee'"  @click="handlePreview(row)">
              <img :src="require('@/assets/image/intelligent/see.png')" alt="">
              <span>查看参考附件</span>
            </div> -->
          </div>


        </template>
      </el-table-column>
    </el-table>

    <el-dialog
      v-dialog-drag
      title="文件预览"
      v-if="visible"
      :visible.sync="visible"
      width="1158px"
      height="800px"
      append-to-body
    >
      <sx-file-view :attaOid="attaOid" @father-click="closeFileViewNew" />
    </el-dialog>
  </div>
</template>

<script>
import { listSxConditionalRules,checkPreInspection } from "@/api/zc/qdgl/sxService";
import sxFileView from "@/views/zc/qdgl/sxFileView";
import axios from 'axios'
export default {
  name: 'verfication',
  props: {
    serviceOid: {
      type: String,
      default: ""
    },
    fillUserInfo:{
      type:Object,
      default:{}
    },
    cegisterType: {
      type: String,
      default: "0"
    },
    inspectionCondition:{
      type:Boolean
    }
  },
  components: { sxFileView },
  data () {
    return {
      tableData: [],
      visible: false,
      attaOid: ''
    }
  },
  created(){
   this.querySxServicePrecheckList()
  },
  methods: {
    querySxServicePrecheckList () {
      this.$getResponse(listSxConditionalRules({ serviceOid: this.serviceOid,ruleType:1 }), (error, res) => {
        if (error || res.code !== 200) return;
        this.tableData = res.data.sort((a, b) => a.sort - b.sort).map(item => ({ ...item, switch: false }));
        this.tableData.forEach(item=>{
          item.status = 0
          const data = {
            sxServiceOid:this.serviceOid,
            type:this.cegisterType,
						uniqueCode: this.fillUserInfo.credentialNumber
          }
          axios.post(`/dev-api/dzcpt-service-provider/windows/execute/${item.interApiCode}`,data).then(res=>{
            if(res.data.data){
               this.$set(item,'status',1)
            }else{
              console.log("111111",res.data.message)
              this.$set(item,'message',res.data.message)
               this.$set(item,'status',0)
            }
          })
        })
            setTimeout(()=>{
        this.$emit("checkVerFication",this.tableData)
      })
      })
    },

    handleValidate (row) {
      try {
        window.open(row.precheckAddress);
      } catch (error) {
        console.log(error);
      }
    },

    handlePreview (row) {
      this.attaOid = row.attaOid;
      this.visible = true;
    },

    closeFileViewNew () {
      this.visible = false;
    },

    validate () {
      return this.tableData.find(item => !item.switch);
    }
  },
  watch:{
    inspectionCondition:{
      handler(val){
        if(val){
          this.tableData = []
          this.querySxServicePrecheckList();
        }
      }
    }
  }
}
</script>
<style lang="scss" scoped>
    ::v-deep .el-table {
      .el-table__header-wrapper {
        .el-table__header {
          thead {
            tr {
              .el-table__cell {
                background-color: #edf0f5 !important;
                text-align: center!important;
              }
            }
          }
        }
      }
      .el-table__body-wrapper {
        .el-table__body {
          tbody {
            .el-table__row {
              .el-table__cell {
                background-color: #fff !important;
                border-color: #e0e6f0 !important;
                .oprate{
                  width: 100%;
                  height: 100%;
                  display: flex;
                  align-items: center;
                  justify-content: center;
                }
                .infoMessage{
                  padding: 0;
                  margin: 0;
                  height: 100%;
                  width: 100%;
                  display: flex;
                  align-items: center;
                  justify-content: flex-start;
                }
              }
            }
          }
        }
      }
    }
</style>
