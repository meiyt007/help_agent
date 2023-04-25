/*
* @Description:告知单标签管理
@Author: dxl
**/
<template>
    <div>
            <el-button
              type="default"
              icon="el-icon-plus"
              size="mini"
              @click="handleInit"
              style="margin-bottom: 10px"
            >新增
            </el-button>
            <el-table
              v-loading="loading"
              :data="comboInformLabelList"
              border
              :fit="true"
            >
                <el-table-column
                  label="标签编码"
                  align="center"
                >
                    <template slot-scope="scope">
                            <el-input
                              v-model.trim="scope.row.labelCode"
                              placeholder="请输入标签编码"
                              maxlength="50"
                              show-word-limit
                            />
                    </template>
                </el-table-column>
                <el-table-column
                        label="排序号"
                        align="center"
                        prop="sort"
                >
                    <template slot-scope="scope">
                            <el-input-number
                                    v-model="scope.row.sort"
                                    :min="1"
                                    :max="9999"
                            />
                    </template>
                </el-table-column>
                <el-table-column
                        label="操作"
                        align="center"
                        class-name="small-padding fixed-width"
                >
                    <template slot-scope="scope">
                            <el-button
                                    size="mini"
                                    type="text"
                                    icon="iconfont zfsoft-shanchu"
                                    @click="handleDelete(scope.$index,scope.row)"
                                    v-hasPermi="['factor:lable:delete']"
                            >删除
                            </el-button>
                    </template>
                </el-table-column>
            </el-table>
    </div>
</template>

<script>
    import {list, save} from "@/api/onething/sxpz/inform/comboInformLabel.js";
    import {countComboOptInformLabelRelAmountByLabelOid} from "@/api/onething/sxpz/inform/comboOptInformLabelRelConfig.js";

    export default {
        components: {},
        name: "ComboInformLabelManageIndex",
        //定义获取父类传过来值的格式
        props: ["comboInformOid", "comboDirectoryOid"],
        data() {
            return {
                // 遮罩层
                loading: true,
                comboInformLabelList: [],
                // 表单参数
                form: {},
            }
        },
        created() {
            this.getList();
        },
        methods: {
            getList() {
                this.loading = true;
                list(this.comboInformOid).then((response) => {
                    this.comboInformLabelList = response.data;
                    this.loading = false;
                });
            },
            // 取消按钮
            close() {
                this.$emit("combo-inform-manage-index-close");
            },
            //增加
            handleInit() {
                this.comboInformLabelList.push({});
            },
            //删除子项模块
            async handleDelete(index, data) {
                let flag = true;
                if (data.oid != undefined) {
                    await countComboOptInformLabelRelAmountByLabelOid(data.oid).then(response => {
                        if (response.data > 0) {
                            this.msgError("该标签存在和情形的配置关系，请先删除配置！");
                            flag = false;
                        }
                    })
                }

                if (flag) {
                    let informLabelList = this.comboInformLabelList;
                    this.$confirm('是否确认删除?', "警告", {
                        confirmButtonText: "确定",
                        cancelButtonText: "取消",
                        type: "warning"
                    }).then(function () {
                        informLabelList.splice(index, 1);
                    })
                    this.comboInformLabelList = informLabelList;
                }

            },
            /** 提交按钮 */
            submitForm: function () {
                let s = new Set();
                let b = true;
                try {
                    this.comboInformLabelList.forEach((comboInformLabel) => {
                        if (null == comboInformLabel.labelCode || "" == comboInformLabel.labelCode) {
                            throw new Error("标签编码不能为空!");
                        }
                        if (null == comboInformLabel.sort) {
                            throw new Error("排序号不能为空!");
                        }
                        s.add(comboInformLabel.labelCode);
                    });
                } catch (e) {
                    b = false;
                    this.msgWarning(e.message);
                    return false;
                }

                if (b && s.size < this.comboInformLabelList.length) {
                    this.msgWarning("标签编码重复，请修改后再保存！");
                    b = false;
                    return false;
                }
                if (b) {
                    save(this.comboInformLabelList, this.comboInformOid, this.comboDirectoryOid).then((response) => {
                            if (response.code === 200) {
                                this.msgSuccess("保存成功");
                                this.$emit("init-close");
                            }
                        }
                    );
                }
                this.$emit("combo-inform-manage-index-close");
            },
        },
    };
</script>
