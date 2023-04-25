<template>
    <div class="consult-container">
        <div class="header">
            <div class="back centerXY" @click="$router.back()">
                <img align="center" src="@/assets/images/back.png" alt="">
                <span>返回</span>
            </div>
            <div><span class="title">视频通话</span><span><i class="el-icon-s-home"></i> 房间1    00:00:00</span></div>
        </div>
        <div class="consult-wrap">
            <div class="right flex">
                <div class="people">
                    <div class="item">
                        <img src="@/assets/images/people.png" alt="">
                        <div class="detail centerYBetween">
                            <div class="name">张乐燕</div>
                            <div>
                                <img src="@/assets/images/horn.png" alt="">
                                <img src="@/assets/images/video.png" alt=""></img>
                            </div> 
                        </div>
                    </div>
                    <div class="item">
                        <img src="@/assets/images/people.png" alt="">
                        <div class="detail centerYBetween">
                            <div class="name">张乐燕</div>
                            <div>
                                <img src="@/assets/images/horn.png" alt="">
                                <img src="@/assets/images/video.png" alt=""></img>
                            </div> 
                        </div>
                    </div>
                    <div class="item">
                        <img src="@/assets/images/people.png" alt="">
                        <div class="detail centerYBetween">
                            <div class="name">张乐燕</div>
                            <div>
                                <img src="@/assets/images/horn.png" alt="">
                                <img src="@/assets/images/video.png" alt=""></img>
                            </div> 
                        </div>
                    </div>
                    <div class="item">
                        <img src="@/assets/images/people.png" alt="">
                        <div class="detail centerYBetween">
                            <div class="name">张乐燕</div>
                            <div>
                                <img src="@/assets/images/horn.png" alt="">
                                <img src="@/assets/images/video.png" alt=""></img>
                            </div> 
                        </div>
                    </div>
                </div>
                <div class="worker">
                    <div class="title">工作人员列表</div>
                    <div class="wrap">
                        <div class="search centerY">
                            <el-input placeholder="输入关键词搜索" v-model="search" clearable>
                                <i slot="prefix" class="el-input__icon el-icon-search"></i>
                            </el-input>
                        </div>
                        <div class="work-content">
                            <el-collapse v-model="activeNames" @change="handleChange">
                                <el-collapse-item title="社区服务中心" name="1">
                                <div class="centerYBetween item off">
                                        <span class="centerXY">
                                            <img src="@/assets/images/male.png" alt="">
                                            <span>刘茜茜</span>
                                        </span>    
                                        <span class="status">离线</span>
                                </div>
                                <div class="centerYBetween item on">
                                        <span class="centerXY">
                                            <img src="@/assets/images/male.png" alt="">
                                            <span>刘茜茜</span>
                                        </span>    
                                        <span class="status">已连线</span>
                                </div>
                                <div class="centerYBetween item busy">
                                        <span class="centerXY">
                                            <img src="@/assets/images/male.png" alt="">
                                            <span>刘茜茜</span>
                                        </span>    
                                        <span class="status">占线</span>
                                </div>
                                </el-collapse-item>
                                <el-collapse-item title="社区服务中心" name="2">
                                    <div class="centerYBetween item off">
                                        <span class="centerXY">
                                            <img src="@/assets/images/male.png" alt="">
                                            <span>刘茜茜</span>
                                        </span>    
                                        <span class="status">离线</span>
                                    </div>
                                    <div class="centerYBetween item on">
                                            <span class="centerXY">
                                                <img src="@/assets/images/male.png" alt="">
                                                <span>刘茜茜</span>
                                            </span>    
                                            <span class="status">已连线</span>
                                    </div>
                                    <div class="centerYBetween item busy">
                                            <span class="centerXY">
                                                <img src="@/assets/images/male.png" alt="">
                                                <span>刘茜茜</span>
                                            </span>    
                                            <span class="status">占线</span>
                                    </div>
                                </el-collapse-item>
                                <el-collapse-item title="社区服务中心" name="3">
                                    <div class="centerYBetween item off">
                                        <span class="centerXY">
                                            <img src="@/assets/images/male.png" alt="">
                                            <span>刘茜茜</span>
                                        </span>    
                                        <span class="status">离线</span>
                                    </div>
                                    <div class="centerYBetween item on">
                                            <span class="centerXY">
                                                <img src="@/assets/images/male.png" alt="">
                                                <span>刘茜茜</span>
                                            </span>    
                                            <span class="status">已连线</span>
                                    </div>
                                    <div class="centerYBetween item busy">
                                            <span class="centerXY">
                                                <img src="@/assets/images/male.png" alt="">
                                                <span>刘茜茜</span>
                                            </span>    
                                            <span class="status">占线</span>
                                    </div>
                                </el-collapse-item>
                                <el-collapse-item title="社区服务中心" name="4">
                                </el-collapse-item>
                                <el-collapse-item title="社区服务中心" name="5">
                                </el-collapse-item>
                            </el-collapse>
                        </div>
                        
                    </div>
                    
                </div>
            </div>
        </div>
        <!-- 点击弹窗 -->
        <div class="share-coupon centerXY" v-show="shareCoupon">
            <div class="item" @click="">
                <img src="@/assets/images/pad/consult/1.png" alt="">
                <div class="name">拍照</div>
            </div>
            <div class="item">
                <img src="@/assets/images/pad/consult/2.png" alt="">
                <div class="name">摄像头</div>
            </div>
            <div class="item">
                <img src="@/assets/images/pad/consult/3.png" alt="">
                <div class="name">主讲</div>
            </div>
            <div class="item" @click="">
                <img src="@/assets/images/pad/consult/4.png" alt="">
                <div class="name">麦克风</div>
            </div>
            <div class="item">
                <img src="@/assets/images/pad/consult/5.png" alt="">
                <div class="name">扬声器</div>
            </div>
            <div class="item">
                <img src="@/assets/images/pad/consult/6.png" alt="">
                <div class="name">文件上传</div>
            </div>
        </div>
    </div>
</template>
<script >
export default{
    name:'videoConsultation',
    data(){
        return{
            activeNames:[],
            shareCoupon:true,
            search:''
        }   
    },
    methods:{
        handleChange(val) {
            console.log(val);
        }
    }
}
</script>
<style lang="scss">
    @import './index.scss';
</style>