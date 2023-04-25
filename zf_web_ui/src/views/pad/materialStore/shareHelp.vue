<template>
    <div class="share-container " v-loading="loadingService" element-loading-text="拼命加载中"
        element-loading-background="transparent">
        <div class="centerX">
            <div class="left">  
                <div class="search-wrap">
                    <div class="search centerY">
                        <el-input placeholder="输入关键词搜索" v-model="filterText"  clearable>
                            <i slot="prefix" class="el-input__icon el-icon-search"></i>
                        </el-input>
                    </div>
                </div>
                <div class="left-content">
                    <el-tree
                        :data="groupList"
                        show-checkbox
                        default-expand-all
                        node-key="id"
                        ref="tree"
                        highlight-current
                        :props="defaultProps"
                        :filter-node-method="filterNode"
                        @check-change="handleChange"
                        >
                    </el-tree>
                    <!-- <el-checkbox-group v-model="checkLeft" @change="leftChange">
                        <template v-if="search ">
                            <el-checkbox :key="worker.workNumber" v-for="worker in searchList"  :label="worker">{{worker.name}}</el-checkbox>
                        </template>  
                        <template v-else>
                            <el-collapse v-model="activeNames" @change="handleChange">
                                <el-collapse-item :key="group.groupId" :title="group.groupName" :name="group.groupId" v-for="group in allServiceList">
                                    <el-checkbox :key="worker.workNumber" v-for="worker in group.haWorkUsers"  :label="worker">{{worker.name}}</el-checkbox>
                                </el-collapse-item>
                            </el-collapse>
                        </template>  
                        
                    </el-checkbox-group> -->
                </div>
            </div>
            <div class="right">
                <div class="search-wrap">选中人员</div>
                <div class="left-content">
                    <div class="checkItem centerYBetween" :key="index" v-for="(select,index)  in  dataChoose">
                        <div>{{select.label}}</div>
                        <i class="el-icon-close" @click="deleteAssistant(select)"></i>
                    </div>
                </div>
                <div class="result centerYBetween">
                    <div class="num">已选<span>{{ dataChoose.length }}</span>项</div>
                    <div class="result-clear" @click="clearSelect">清空</div>
                </div>
            </div> 
        </div>
        <span slot="footer" class="dialog-footer center">
            <el-button @click="shareClose">取 消</el-button>
            <el-button type="primary" @click="shareConfirm">确 定</el-button>
        </span>
    </div>
</template>
<script>
    import { getWorkUserList } from "@/api/modules/guideService";
    import { getGroupList } from "@/api/modules/helpAgent";
    export default {
        name:'',
        data(){
            return{
                loadingService:false,
                filterText: "",
                groupList: [],
                defaultProps: {
                    children: "children",
                    label: "label",
                },
                dataChoose:this.chooseAssistant
            }
        },
        computed: {
            basicUserInfo() {
                return this.$store.state.user.basicUserInfo;
            },
        },
        props:{
            //选中人员
            chooseAssistant: {
                type: Array,
                default: () => [],
            },
        },  
        watch:{
            dataChoose(val){
                console.log(val)
            },
            filterText(val) {
                this.$refs.tree.filter(val);
            },
        },  
        mounted(){
            // this.getGroupList();
            this.getWorkUserList();
            this.dataChoose= [];
        },
        methods:{
            shareConfirm(){
                this.$emit('shareConfirm',this.dataChoose);
                this.dataChoose = [];
                this.$refs.tree.setCheckedNodes([]); 
                
            },  
            shareClose(){
                this.dataChoose = [];
                this.$refs.tree.setCheckedNodes([]);
                this.$emit('shareClose');
            },  
            //获取帮办人员列表
            getWorkUserList() {
                const data = {
                    name: "",
                    haType: "",
                };
                getWorkUserList(data).then((res) => {
                    if (res.code === 200) {
                    res.data.forEach((item) => {
                        const obj = {
                            id: item.groupId,
                            label: item.groupName,
                            children: [],
                        };
                        const result = this.groupList.find((data) => {
                            return data.id === item.groupId;
                        });
                        if (!result) {
                        this.groupList.push(obj);
                        }
                    });

                    res.data.forEach((item) => {
                        this.groupList.forEach((ite) => {
                            if (item.groupId === ite.id) {
                                let obj = {};
                                if (item.id === this.basicUserInfo.id) {
                                    obj = {
                                        id: item.id,
                                        label: item.name,
                                        disabled: true,
                                    };
                                } else {
                                    obj = {
                                        id: item.id,
                                        label: item.name,
                                    };
                                }
                                ite.children.push(obj);
                            }
                        });
                    });
                    this.groupList = [...this.groupList];
                    }
                });
            },
            filterNode(value, data, node) {
                if (!value) return true;
                let parentNode = node.parent;
                let labels = [node.label];
                let level = 1;
                while (level < node.level) {
                    labels = [...labels, parentNode.label];
                    parentNode = parentNode.parent;
                    level++;
                }
                return labels.some((label) => label.indexOf(value) !== -1);
            },
            //选择帮办人员
            handleChange() {
                this.dataChoose = [];
                this.$refs.tree.getCheckedNodes().forEach((item) => {
                    if (!item.children) {
                    this.dataChoose.push(item);
                    }
                });
            },
            //删除选中帮办人员
            deleteAssistant(data) {
                this.chooseAssistant.forEach((item, index) => {
                    if (data.id === item.id) {
                    this.dataChoose.splice(index, 1);
                    }
                });
                this.$refs.tree.setCheckedNodes(this.dataChoose);
            },
            //清空搜索
            clearSelect(){
                this.dataChoose = [];
                this.$refs.tree.setCheckedNodes(this.dataChoose);
            },
            // // 左侧多选
            // leftChange(val){
            //     // console.log(val)
            // },
            //  //获取分组列表
            // getGroupList() {
            //     let that = this;
            //     that.loadingService = true;
            //     getGroupList()
            //     .then((res) => {
            //         that.loadingService  = false;       
            //         if (res.code === 200) {
            //             this.allServiceList = res.data;
            //             if(res.data.length){
            //                 let arr = [];
            //                 res.data.forEach(function(item){
            //                     if(item.haWorkUsers.length){
            //                        arr =  arr.concat(item.haWorkUsers);
            //                     }
            //                 })
            //                 that.workerList = arr;
            //             }
            //         }
            //     })
            //     .catch((err) => {
            //         console.log(err);
            //     });
            // },
        }
    }
</script>
<style lang="scss">
    @import './index.scss';
</style>