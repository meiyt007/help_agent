<template>
    <div>
        <div class="centerXY continue-wrap">
            <div class="btn centerXY">
                <img src="@/assets/images/home/scan.png" alt="">
            </div>
            <el-autocomplete top="200px" clearable class="inline-input" v-model="search" :fetch-suggestions="querySearch"
                placeholder="请输入内容" :trigger-on-focus="false" @select="handleSelect">
                <el-select v-model="select" slot="prepend" placeholder="请选择">
                    <el-option :key="data.code" :label="data.name" :value="data.code" v-for="data in certificateTypeList"></el-option>
                </el-select>
                <el-button :disabled="isData" slot="append" @click="searchTarget" icon="el-icon-search">搜索</el-button>
            </el-autocomplete>
        </div>

    </div>
</template>
<script>
import { getSelectCertificateType,queryCompanyName } from "@/api/modules/business";
export default {
    name: 'serviceContinue',
    data() {
        return {
            search: '',
            select: '',
            certificateTypeList:[],  //选择项
            list:{},
            isData:false, //当企业名称搜索为空，设置搜索按钮不可点
        }
    },
    watch:{
        select(val){
            if(val == '企业名称'){
                this.search = '';
            }else{
                this.isData = false;
            }
        }
    },
    mounted() {
        // this.restaurants = this.loadAll();
        this.getCertificateTypeList()
    },
    methods: {
        getCertificateTypeList() {
            let that = this;
            getSelectCertificateType({ type: 2 }).then(
                (res) => {
                    if (res.code === 200) {
                        let obj = {
                            name:'企业名称',
                            code:'企业名称'
                        }
                        that.certificateTypeList = res.data;
                        that.certificateTypeList.push(obj)
                        that.select = res.data[0].code
                    }
                }
            );
        },
        // 监听企业名称
        querySearch(queryString, cb) {
            console.log(this.select,queryString)
            let that = this;
            this.list = {};
            if(this.select == '企业名称'){
                queryCompanyName({name:this.search}).then(res=>{
                    if(res.data.length > 0){
                        var restaurants = res.data;
                        restaurants.forEach(function(item){
                            item['value'] = item.applyUserName +'（'+ item.credentialNumber + '）';
                        })
                        var results = restaurants;
                        that.isData = false;
                        // 调用 callback 返回建议列表的数据
                        cb(results);
                    }else{
                        cb([]);
                        that.$message({
                            message: '没有查到相关信息,请重新输入！',
                            type: 'warning'
                        });
                        that.isData = true;
                        that.list['applyUserName'] = that.search;
                        that.list['credentialNumber'] = '';
                    }
                })
                
            }else{
                cb([]);
                let obj ={
                    credentialType:this.select,
                    credentialNumber:this.search
                }
                this.list = obj;
            }
            
        },
        //模糊查询 选中
        handleSelect(item) {
            console.log(item);
            this.list = item;
        },
        // 搜索
        searchTarget(){
            if(this.select != '企业名称'){
                if(this.search.indexOf(";") > -1){
                    this.select = this.search.split(";")[0];
                    this.search = this.search.split(";")[1];
                }
                this.list['credentialType'] = this.select;
                this.list['credentialNumber'] = this.search
            }
            if(!this.search){
                this.$message({
                    message: '请输入查询信息！',
                    type: 'warning'
                });
                return;
            }
            // console.log(this.list)
            this.$emit("toBasic",this.list);
            // this.search = '';
            this.list = {};
        }
    }
}
</script>
<style lang="scss">
@import './index.scss';
</style>
