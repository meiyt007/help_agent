<!-- 选择情形测试页面 -->
<template>
    <div>
        <div class="situa-container">
            <el-carousel ref="card" type="card"  @change="cardChange" height="41.5882rem" arrow="never" :autoplay="false" :initial-index="1"	 indicator-position="outside" :loop="true">
                <el-carousel-item v-for="item in 6" :key="item">
                    <div class="situa-item">
                        <div class="step"><span>{{ item }}</span>/6</div>
                        <div class="targt">本次办理类型？</div>
                        <div class="situa-content">
                            <el-checkbox-group v-model="checkList" class="chooseOption">
                                <el-checkbox label="营业执照换发1"></el-checkbox>
                                <el-checkbox label="营业执照换发2"></el-checkbox>
                                <el-checkbox label="营业执照换发3"></el-checkbox>
                                <el-checkbox label="营业执照换发4"></el-checkbox>
                                <el-checkbox label="营业执照换发5"></el-checkbox>
                            </el-checkbox-group>
                            <el-radio-group v-model="radio" class="chooseOption">
                                <el-radio :label="3">备选项</el-radio>
                                <el-radio :label="6">备选项</el-radio>
                                <el-radio :label="9">备选项</el-radio>
                            </el-radio-group>
                        </div>
                    </div>
                </el-carousel-item>
            </el-carousel>
        </div>
        <div class="guidance-foot">
            <p><span>上一步</span></p>
            <p><span>保存</span></p>
            <p>下一步</p>
        </div>

    </div>
</template>
<script>
import Hammer from 'hammerjs';
export default {
    name: 'situationNewGuidance',
    data() {
        return {
            checkList: [],
            radio: 3,
        }   
    },
    computed: {
    },
    watch:{
        
    },
    mounted(){
        //事项基本信息展开与收起
        // var square = document.querySelector('.situa-container');
        // var hammer = new Hammer(square,{
        //     touchAction: 'pan-y'  // 兼容y轴方向的滚动
        // });
        // hammer.get('pinch').set({ enable: true });
        // hammer.on('panup',function(){
        //     that.guideIn = true;
        // })
        // hammer.on('pandown',function(){
        //     that.guideIn = false;
        // })
        // hammer.on('pinchin',function(){
        //     alert('pinchin')
        // })
        // hammer.on('swipeleft',function(){
        //     alert('swipeleft')
        // })
        // hammer.on('swiperight',function(){
        //     alert('swiperight')
        // })
        this.slideBanner();
    },
    methods:{
        cardChange(index){
            console.log(index)
        },
        // 滑动切换
        slideBanner() {
            //选中的轮播图
            var box = document.querySelector('.el-carousel__container');
            var startPoint = 0;
            var stopPoint = 0;
            //重置坐标
            var resetPoint = function () {
                startPoint = 0;
                stopPoint = 0;
            }
            //手指按下
            box.addEventListener("touchstart", function (e) {
                //手指点击位置的X坐标
                startPoint = e.changedTouches[0].pageX;
            });
            //手指滑动
            box.addEventListener("touchmove", function (e) {
                //手指滑动后终点位置X的坐标
                stopPoint = e.changedTouches[0].pageX;
            });
            //当手指抬起的时候，判断图片滚动离左右的距离
            let that = this
            box.addEventListener("touchend", function (e) {
                // console.log("1：" + startPoint);
                // console.log("2：" + stopPoint);
                if (stopPoint == 0 || startPoint - stopPoint == 0) {
                    resetPoint();
                    return;
                }
                if (startPoint - stopPoint > 0) {
                    resetPoint();
                    that.$refs.card.next();
                    return;
                }
                if (startPoint - stopPoint < 0) {
                    resetPoint();
                    that.$refs.card.prev();
                    return;
                }
            });
        },
        
    },
    destroyed() {
      
    }
    
}
</script>
<style lang="scss" scoped>
    @import './css/situationNewGuidance.scss';
</style>