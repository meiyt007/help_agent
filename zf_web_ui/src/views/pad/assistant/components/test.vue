<template>
    <div class="search-container">
          <!-- <div class="btnArea" @click="test1()">
            <span>企业信息公示系统</span>
          </div> -->
          <!-- <el-button size="medium" round @click="test1()" type="primary">企业信息公示系统</el-button>
          <el-button size="medium" round @click="test2()" type="primary">上海企业登记档案查阅系统</el-button>
          <el-button size="medium" round @click="test3()" type="primary">《上海市居住证》积分申请系统</el-button>
          <el-button size="medium" round @click="test4()" type="primary">网络预约出租汽车驾驶员从业资格证核发</el-button>
          <el-button size="medium" round @click="test5()" type="primary">政务公开</el-button> -->
        <div class="outer">
            <div class="item" @click="test1()">
                <img :src="require('@/assets/images/pad/outer/1.png')" alt="">
                <div>国家企业信用信息公示系统(上海)</div>
            </div>
            <div class="item" @click="test2()">
                <img :src="require('@/assets/images/pad/outer/2.png')" alt="">
                <div>上海市市场监督管理局(档案查询)</div>
            </div>
            <div class="item" @click="test3()">
                <img :src="require('@/assets/images/pad/outer/3.png')" alt="">
                <div>上海市人力资源和社会保障公共服务平台</div>
            </div>
            <div class="item" @click="test4()">
                <img :src="require('@/assets/images/pad/outer/4.png')" alt="">
                <div>网络预约出租汽车驾驶员从业资格证核发</div>
            </div>
            <div class="item" @click="test5()">
                <img :src="require('@/assets/images/pad/outer/5.png')" alt="">
                <div>上海市黄浦区人民政府政务公开</div>
          </div>
          <!-- 页面打印 -->
          <!-- <div class="item" @click="test6()">
                <img :src="require('@/assets/images/pad/outer/5.png')" alt="">
                <div>页面打印</div>
          </div> -->
          <!-- pdf打印 -->
          <!-- <div class="item" @click="test7()">
                <img :src="require('@/assets/images/pad/outer/5.png')" alt="">
                <div>pdf打印</div>
          </div> -->
          <!-- 图片打印 -->
          <!-- <div class="item" @click="test8()">
                <img :src="require('@/assets/images/pad/outer/5.png')" alt="">
                <div>图片打印</div>
          </div> -->
        </div>
    </div>
</template>
<script>
    export default{
        name:'test',
        data(){
            return{

            }
        },
        methods: {
            test1() {
            android.setWebUrl('https://sh.gsxt.gov.cn/index.html');
            },
            test2() {
            android.setWebUrl('https://fw.scjgj.sh.gov.cn/achieve_outer/apply/notice');
            },
            test3() {
            android.setWebUrl('http://jzzjf.rsj.sh.gov.cn/jzzjf/');
            },
            test4() {
            android.setWebUrl('https://zwdt.sh.gov.cn/govPortals/bsfw/item/4df7e909-5471-49cf-adf6-3cfc7f4604bd');
            },
            test5() {
            android.setWebUrl('https://www.shhuangpu.gov.cn/zw/ysqxxgksl.html');
            },
            // test6() {
            //     android.jsCallAndroidArgs();
            // },
            // test7() {
            //     android.printPdf("http://172.21.178.38:8000/group1/M00/01/8F/rBWyJmPbf0uAeLCAAAk1JXDtKTo973.pdf");
            // },
            // test8() {
            //     android.printPdf("http://172.21.178.38:8000/group1/M00/01/C0/rBWyJmP_A-CAUy20AACGuUyWFLU892.jpg");
            // },
        }
    }
</script>
<style lang="scss" scoped>
    .search-container{
        padding-top: 2.0588rem;
        text-align: left;
        width: 100%;
        margin: 0 auto;
    }
    .btnArea{
        margin-top: 20px;
    }
    .el-button--medium.is-round{
        padding: 15px 20px;
        font-size: 16px;
    }
    .outer{
        .item {
            width: calc((100% - 4.4118rem)/4);
            margin-right: 1.4706rem;
            margin-bottom: 1.7647rem;
            float: left;
            height: 12.6471rem;
            background-color: #ffffff;
	        box-shadow: 0rem 0.1765rem 1.4118rem 0rem rgba(4, 11, 31, 0.08);
	        border-radius: 0.4706rem;
            font-size: 1.1765rem;
            color: #383f58;
            text-align: center;
            padding-top: 1.9412rem;
            padding-left: 15px;
            padding-right: 15px;
            img{
                height: 4.4706rem;
                margin-bottom: 1.0588rem;
            }
        }

        &::after{
            content: '';
            display: block;
            clear: both;
        }
        &>div:nth-child(4n){
            margin-right: 0;
        }
    }
</style>



