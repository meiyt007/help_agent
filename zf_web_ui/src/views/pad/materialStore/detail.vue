<template>
    <div class="material-container">
        <div class="header">
            <div class="back centerXY" @click="$router.back()">
                <img align="center" src="@/assets/images/back.png" alt="">
                <span>返回</span>
            </div>
            <div>详情</div>
        </div>
        <div class="content">
            <img src="@/assets/images/square.png" alt="">
           
        </div>
        <div class="share-coupon centerXY">
            <div class="btn help">分享给帮办人员</div>
            <div class="btn handle">发送给办事人</div>
        </div>
    </div>
</template>
<script>

export default {
    name: 'materialStore',
    data(){
        return{
        }
    },
    methods:{
    }
}
</script>
<style lang="scss">
    @import './index.scss';
</style>