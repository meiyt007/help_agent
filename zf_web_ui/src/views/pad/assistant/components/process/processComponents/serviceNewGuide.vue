<!-- 办事指南 -->
<template>
    <div class="guide">
        <div class="guide-title">事项基本信息<span>注：双指靠近或拉远，可实现展开与收起</span></div>
        <div class="guide-table square" :class="{'guide-in':guideIn,'guide-out':!guideIn}">
            <table>
                <tr>
                    <td width="15%">事项名称：</td>
                    <td width="35%">营业执照遗失补领、换发</td>
                    <td width="15%">事项类型：</td>
                    <td width="35%">行政许可</td>
                </tr>
                <tr>
                    <td>实施机构：</td>
                    <td>黄浦区市场监督管理局</td>
                    <td>办理地点：</td>
                    <td>黄浦区行政服务中心二楼C01-C10号窗口</td>
                </tr>
                <tr>
                    <td>办理时间：</td>
                    <td>周一至周五（9:00-11:30    13:30-16:30）</td>
                    <td>咨询电话：</td>
                    <td>021-63529090</td>
                </tr>
                <tr>
                    <td>承诺时限：</td>
                    <td>8 (工作日)</td>
                    <td>法定时限：</td>
                    <td>6 (工作日)</td>
                </tr>
                <tr>
                    <td width="15%">申请受理条件：</td>
                    <td colspan="3">申请材料齐全，形式符合要求，具体见申请材料目录</td>
                </tr>
                <tr>
                    <td width="15%">办事流程：</td>
                    <td colspan="3">
                        <div>1.上传材料</div>
                        <div>2.受理</div>
                        <div>1.上传材料</div>
                        <div>2.受理</div>
                    </td>
                </tr>
            </table> 
            <img  class="switch" src="@/assets/images/pad/switch.png" alt="">  
        </div>
        <div class="guide-title guide-material">材料列表 <span class="num">共3种材料</span>
            <div class="type centerXY">
                <div class="tosubmit">需提交</div>
                <div class="nosubmit">免提交</div>
                <div class="todo">容缺后补</div>
            </div>
        </div>
        <div class="material-list">
            <div class="item centerY">
                <img src="@/assets/images/pad/example01.png" alt="">
                <div>
                    <div class="name">申请书</div>
                    <div class="btns">
                        <span class="paper">纸质</span>
                        <span class="origin">原件</span>
                        <span class="paper">复印件</span>
                        <span class="num">1份</span>
                    </div>
                </div>
            </div>
            <div class="item centerY">
                <img src="@/assets/images/pad/example03.png" alt="">
                <div>
                    <div class="name">申请书</div>
                    <div class="btns">
                        <span class="paper">纸质</span>
                        <span class="origin">原件</span>
                        <span class="paper">复印件</span>
                        <span class="num">1份</span>
                    </div>
                </div>
            </div>
            <div class="item centerY">
                <img src="@/assets/images/pad/example02.png" alt="">
                <div>
                    <div class="name">申请书</div>
                    <div class="btns">
                        <span class="paper">纸质</span>
                        <span class="origin">原件</span>
                        <span class="paper">复印件</span>
                        <span class="num">1份</span>
                    </div>
                </div>
            </div>
        </div>
        <div class="guide-footer centerXY">
            <div class="prev" @click="transmit">一键传递</div>
            <div class="next" @click="">好的，已告知办事人 </div>
        </div>
        <div class="transmit-coupon" v-if="transmitShow">
            <img id="transmit"  :key="Math.random()" class="transmit" :src="transmitUrl" alt=""></img>
        </div>
        
    </div>
</template>
<script>
    import Hammer from 'hammerjs';

    export default{
        name:'serviceNewGuide',
        data(){
            return{
                transmitShow:false,
                transmitUrl:'',
                guideIn:true,  // 手势靠近 or 拉远
            }
        },
        mounted(){
            let that =this;
            //事项基本信息展开与收起
            var square = document.querySelector('.square');
            var hammer = new Hammer(square,{
                touchAction: 'pan-y'  // 兼容y轴方向的滚动
            });
            hammer.get('pinch').set({ enable: true });
            hammer.on('pinchin',function(){
                that.guideIn = true;
            })
            hammer.on('pinchout',function(){
                that.guideIn = false;
            })
        },  
        methods:{
            // 一键传递
            transmit(){
                let that = this;
                that.transmitUrl = require('@/assets/images/pad/transmit.gif');
                that.transmitShow= true;
                // $('#transmit').attr("src",require('@/assets/images/pad/transmit.gif'));
                setTimeout(function(){
                    that.transmitShow = false;
                    that.transmitUrl = '';
                    document.getElementById('transmit').setAttribute("src",'');
                },3000)
            }
        }
    }
</script>
<style lang="scss" scoped>
    @import './css/serviceNewGuide.scss';
</style>