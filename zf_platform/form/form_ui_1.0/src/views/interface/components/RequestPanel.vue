<template>
    <div class="app-container requestPanel">
      <el-input placeholder="请输入请求url" v-model="request.url" class="input-with-select">
        <el-select v-model="request.method" slot="prepend" placeholder="请选择">
          <el-option v-if="!isPre" v-for="method in requestMethods" :key="method" :value="method"></el-option>
          <el-option v-if="isPre" v-for="method in requestPreMethods" :key="method" :value="method"></el-option>
        </el-select>
        <el-button slot="append" @click="onSend" type="primary" icon="el-icon-position">发 送</el-button>
      </el-input>
     <!-- <el-button @click="saveSend" type="primary" size="small">保 存</el-button>-->
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="Params" key="1" name="first" >
          <data-table v-if="show0" :isPre="!isPre" :title="'Params'" :editable="true"
                      :data-source="request.params" @dataChanged="updateParams"></data-table>
        </el-tab-pane>
        <el-tab-pane label="Headers"  key="2" name="second">
          <data-table v-if="show1" :title="'Headers'" :editable="true"
                      :data-source="request.headers" :isPre="!isPre" @dataChanged="updateHeaders"></data-table>
        </el-tab-pane>
        <el-tab-pane label="Body" key="3" name="third">
          <request-body-panel v-if="show2" :body="request.body" @changed="updateBody"></request-body-panel>
        </el-tab-pane>
        <!--<el-tab-pane label="Pre-request Script" key="4" name="fourth" v-if="!isPre">
          <script-panel v-if="show3" :text="request.pre_request_script" :tips="pre_script_tips"
                        @changed="updatePreScript"></script-panel>
        </el-tab-pane>-->
        <el-tab-pane label="关联配置" key="5" name="five"  v-if="isPre">
          <LinkConfig v-if="show3" title="关联配置" :isPre="!isPre" :editable="true"
                      :data-source="request.link_config" @dataChanged="updateLinkConfig"></LinkConfig>
        </el-tab-pane>
      </el-tabs>
      <br/>
      <div v-if="displayResponse" >
        <hr/>
        <h4>Response</h4>
        <div style="float: right">
          <div><strong>Status:&nbsp;&nbsp;</strong> <span style="color: #2fb784;">
                        {{response.status +' '+response.statusText}}
                    </span>&nbsp;&nbsp;&nbsp;
            <strong>Time:&nbsp;&nbsp;</strong> <span style="color: #2fb784;">{{response.data.time}}</span>
          </div>
        </div>
        <el-tabs v-model="responseActiveName">
          <el-tab-pane label="Body" key="Body" name="first">
            <InCoder :key="new Date().getTime()" :value="JSON.stringify(response.data.data, null, 2)"></InCoder>
          </el-tab-pane>
          <el-tab-pane label="Headers" key="2" name="second">
            <data-table :key="new Date().getTime()" :title="'Headers'" :editable="false" :data-source="response.headers"></data-table>
          </el-tab-pane>
        </el-tabs>
        <br/>
        <br/>
      </div>

    </div>
</template>

<script>
import DataTable from "./DataTable";
import RequestBodyPanel from "./RequestBodyPanel";
import ScriptPanel from "./ScriptPanel";
import InCoder from "./InCoder";
import LinkConfig from "./LinkConfig";
import { requestData } from "@/api/interface/manager";
export default {
    name: "RequestPanel",
    components: {InCoder, ScriptPanel, RequestBodyPanel, DataTable,LinkConfig},
    props:{
      requestJson:{
        type:String,
        default:''
      },
      requestPreJson:{
        type:String,
        default:''
      },
      isPre:{
        type:Boolean,
        default:false
      },
    },
    methods: {
         getPreRequest(){
          return this.request;
        },
        onSend: function () {
            //请求地址
            if(''==this.request.url){
              this.msgError("请输入请求url");
              return false;
            }
            if(!this.checkURL(this.request.url)){
              this.msgError("请输入正确的请求url");
              return false;
            }
            //console.log('转换前：',this.request.pre_request_script)
            this.script_params = new Map();
            let requestScript  = this.request.pre_request_script;
            let _that = this;
            if('' != requestScript){
              //设置参数替换
              requestScript = requestScript.replace(/tools.setParam/g, '_that.script_params.set');
              //获取参数替换
              requestScript = requestScript.replace(/tools.getParam/g, '_that.script_params.get');
              //时间戳替换
              requestScript = requestScript.replace(/tools.getTimeStamp/g, 'new Date().getTime');
              //get请求替换
              requestScript = requestScript.replace(/tools.get/g, '_that.$axios.get');
              //post请求替换
              requestScript = requestScript.replace(/tools.post/g, '_that.$axios.post');
              //console.log(requestScript)
            }
              //转换成可执行的js
            let jsAsync = async function(){
              await eval(requestScript);
            }
            jsAsync().then(function() {
                //map转json
              let script_params_obj= Object.create(null);
              if(_that.script_params.size>0){
                for (let[k,v] of _that.script_params) {
                  script_params_obj[k] = v;
                }
                _that.request.script_params = script_params_obj;
              }

              //console.log('转换后：',requestScript)
              let requestJson = JSON.stringify(_that.request);
              //console.log( _that.request.script_params)
              let requestPreJson = null;
              if(!_that.isPre){
                requestPreJson =  _that.$emit('getPreRequestData').requestPreJson;
                //console.log('requestPreJson',requestPreJson);
              }
              let saveJson = {
                requestJson:requestJson,
                requestPreJson:requestPreJson
              };
              //console.log( requestJson)
              //请求之前置空
              _that.$emit('handleResponse','',requestJson);
              requestData(saveJson).then(res => {
                _that.displayResponse = true;
                _that.response = res;
                _that.$emit('handleResponse',_that.response.data.data,requestJson);
              }).catch(err => {
                //_that.msgError('请求失败！');
                console.log(err);
              });
            });
        },
        saveSend: function () {
          this.request.save_response = true;
          //请求地址
          if(''==this.request.url){
            this.msgError("请输入请求url");
            return false;
          }
          if(!this.checkURL(this.request.url)){
            this.msgError("请输入正确的请求url");
            return false;
          }
          let requestJson = JSON.stringify(this.request);
          requestData(requestJson).then(res => {
            this.msgSuccess('保存成功！');
          }).catch(err => {
            this.msgError('保存失败！');
            console.log(err);
          });
        },
        updateHeaders: function (data) {
            this.request.headers = data;
        },
        updateParams: function (data) {
            this.request.params = data;
        },
        updatePreScript: function (value) {
            this.request.pre_request_script = value;
        },
        updateLinkConfig: function (value) {
          this.request.link_config = value;
          this.$emit('handleResponse',null,JSON.stringify(this.request));
        },
        updateTests: function (value) {
            this.request.tests = value;
        },
        updateBody: function (body) {
            //console.log(body)
            this.request.body = body;
        },
        onClose: function () {
            this.visible = false;
        },
        handleClick(tab, event) {
          this.$set(this,'show'+tab.index,true);
        },
        checkURL(URL){
          var str=URL;
          //判断URL地址的正则表达式为:http(s)?://([\w-]+\.)+[\w-]+(/[\w- ./?%&=]*)?
          //下面的代码中应用了转义字符"\"输出一个字符"/"
          var Expression=/http(s)?:\/\/([\w-]+\.)+[\w-]+(\/[\w- .\/?%&=]*)?/;
          var objExp=new RegExp(Expression);
          if(objExp.test(str)==true){
            return true;
          }else{
            return false;
          }
        } ,
    },
    data: function () {
        return {
            activeName: 'first',
            responseActiveName: 'first',
            msg: '',
            requestMethods: [
                'GET', 'POST'
            ],
            /*requestMethods: [
              'GET', 'POST', 'PUT', 'DELETE', 'HEAD', 'OPTIONS', 'PATCH', 'TRACE'
            ],*/
            requestPreMethods: [
            'GET', 'POST'
           ],
            method: 'GET',
            script_params:new Map(),
            pre_script_tips: [
                {title: '获取一个变量', script: 'tools.getParam("paramName");'},
                {title: '设置一个变量', script: 'tools.setParam("paramName","value");'},
               /* {title: '计算MD5', script: 'tools.MD5("arg");'},
                {title: '计算SHA1', script: 'tools.SHA1("arg");'},
                {title: '计算SHA256', script: 'tools.SHA256("arg");'},*/
                {title: '获取时间戳', script: 'tools.getTimeStamp();'},
                {title: '发送一个GET请求', script: 'tools.get(\'http://172.168.250.6:10001/mock/67/common/getTest\').then(function(res){\n' +
                    '     console.log(res);\n' +
                    '    }).catch(function(err){\n' +
                    '     console.log(err);\n' +
                    '});'},
                {title: '发送一个POST请求', script: 'const requestData = JSON.stringify({ \'key\': \'this is json\' })\n' +
                    'tools.post(\'http://172.168.250.6:10001/mock/67/common/postTest\', requestData, {\n' +
                    '        header: {\n' +
                    '          \'Content-Type\': \'multipart/form-data\'\n' +
                    '        }\n' +
                    '      }) .then(function(res){\n' +
                    '           console.log(res)\n' +
                    '  \n' +
                    '      }) .catch(function(err){\n' +
                    '          console.log(err)\n' +
                    ' });'},
            ],
            tests_tips: [
                {title: '执行测试', script: 'tools.tests(expression);'},
                {title: '获取状态码', script: 'tools.getCode();'},
                {title: '获取状态消息', script: 'tools.getMsg();'},
                {title: '判断响应体是否包含字符串', script: 'tools.bodyContains("str");'},
                {title: '判断是否存在响应头', script: 'tools.hasHeader("headerName");'},
                {title: '获取响应头', script: 'tools.getHeader("headerName");'},
            ],
            displayResponse: false,
            request_body: {
                type: 'none',
                content: ''
            },
            request: {
                headers: {},
                tests: '',
                method: 'GET',
                //save_response: false,
                body: {
                    type: 'none',
                    content: ''
                },
                params: {},
                pre_request_script: '',
                url: '',
            },
            response: {
                headers: {},
                code: '',
                time: '',
                body: '',
                testsResult: {},
                status: '',
            },
            visible: false,
            cookies: [],
            show0:false,
            show1:false,
            show2:false,
            show3:false,
            show4:false,
            show5:false,
        };
    },

    created() {
      this.show0 = true;
      if(this.requestJson){
        this.request = this.requestJson?eval("(" + this.requestJson + ")"):null;
      }else if(this.requestPreJson){
        this.request = this.requestPreJson?eval("(" + this.requestPreJson + ")"):null;
      }
    }
}
</script>

<style>
  .requestPanel .el-select .el-input {
    width: 100px;
  }
  .requestPanel .input-with-select .el-input-group__prepend {
    background-color: #fff;
  }
  .requestPanel .el-tabs__item{
    font-weight: bolder;
  }

  .requestPanel .el-input-group{
    width: 100% !important;
  }
  .requestPanel .el-button--small{
    height: 35px;
    padding-top: 13px;
    margin-left: 5px;
  }
  .requestPanel .el-input-group__append{
    background-color: #1387e9;
    color: white;
  }
</style>
