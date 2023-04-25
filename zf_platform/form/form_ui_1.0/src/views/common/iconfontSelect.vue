<template>
       <ul class="icon_lists dib-box">
        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-banjianzhinan"></span>
          <div class="name">
            办件指南
          </div>
          <div class="code-name">zfsoft-banjianzhinan
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-jigoumingcheng"></span>
          <div class="name">
            机构名称
          </div>
          <div class="code-name">zfsoft-jigoumingcheng
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-shenqingrenyonghu"></span>
          <div class="name">
            申请人用户
          </div>
          <div class="code-name">zfsoft-shenqingrenyonghu
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-yanzhengma"></span>
          <div class="name">
            验证码
          </div>
          <div class="code-name">zfsoft-yanzhengma
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-banjianbianma"></span>
          <div class="name">
            办件编码
          </div>
          <div class="code-name">zfsoft-banjianbianma
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-shehuixinyongdaima"></span>
          <div class="name">
            社会信用代码
          </div>
          <div class="code-name">zfsoft-shehuixinyongdaima
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-zaixianyuyue"></span>
          <div class="name">
            在线预约
          </div>
          <div class="code-name">zfsoft-zaixianyuyue
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-zhengjianhao"></span>
          <div class="name">
            证件号
          </div>
          <div class="code-name">zfsoft-zhengjianhao
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-shouyemianzhuang"></span>
          <div class="name">
            首页（面状）
          </div>
          <div class="code-name">zfsoft-shouyemianzhuang
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-shezhixiugaixinxi"></span>
          <div class="name">
            设置（修改信息）
          </div>
          <div class="code-name">zfsoft-shezhixiugaixinxi
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-dizhimianzhuang"></span>
          <div class="name">
            地址（面状）
          </div>
          <div class="code-name">zfsoft-dizhimianzhuang
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-shijian"></span>
          <div class="name">
            时间
          </div>
          <div class="code-name">zfsoft-shijian
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-lianxidianhua"></span>
          <div class="name">
            联系电话
          </div>
          <div class="code-name">zfsoft-lianxidianhua
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-tuwenliebiaodianji"></span>
          <div class="name">
            图文列表（点击）
          </div>
          <div class="code-name">zfsoft-tuwenliebiaodianji
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-liebiaodianji"></span>
          <div class="name">
            列表（点击）
          </div>
          <div class="code-name">zfsoft-liebiaodianji
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-chuangkoubanliliucheng"></span>
          <div class="name">
            窗口办理流程
          </div>
          <div class="code-name">zfsoft-chuangkoubanliliucheng
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-wangshangbanliliucheng"></span>
          <div class="name">
            网上办理流程
          </div>
          <div class="code-name">zfsoft-wangshangbanliliucheng
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-banjiangongshi"></span>
          <div class="name">
            办件公示
          </div>
          <div class="code-name">zfsoft-banjiangongshi
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-gongchengjianshexiangmufuwu"></span>
          <div class="name">
            工程建设项目服务1
          </div>
          <div class="code-name">zfsoft-gongchengjianshexiangmufuwu
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-fuwuqingdan"></span>
          <div class="name">
            服务清单
          </div>
          <div class="code-name">zfsoft-fuwuqingdan
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-shiqingqingdan"></span>
          <div class="name">
            事情清单
          </div>
          <div class="code-name">zfsoft-shiqingqingdan
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-shujugongkai"></span>
          <div class="name">
            数据公开
          </div>
          <div class="code-name">zfsoft-shujugongkai
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-zhengminhudong"></span>
          <div class="name">
            政民互动
          </div>
          <div class="code-name">zfsoft-zhengminhudong
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-quanliqingdan"></span>
          <div class="name">
            权力清单
          </div>
          <div class="code-name">zfsoft-quanliqingdan
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-zhuxiaoqiyeyichuangtong"></span>
          <div class="name">
            注销企业一窗通
          </div>
          <div class="code-name">zfsoft-zhuxiaoqiyeyichuangtong
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-danweichaxun"></span>
          <div class="name">
            单位查询
          </div>
          <div class="code-name">zfsoft-danweichaxun
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-gonganduanxinbaojinghaoma"></span>
          <div class="name">
            公安短信报警号码
          </div>
          <div class="code-name">zfsoft-gonganduanxinbaojinghaoma
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-dianlixitongkefudianhua"></span>
          <div class="name">
            电力系统客服电话
          </div>
          <div class="code-name">zfsoft-dianlixitongkefudianhua
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-gongjijindaikuanxinxichaxun"></span>
          <div class="name">
            公积金贷款信息查询
          </div>
          <div class="code-name">zfsoft-gongjijindaikuanxinxichaxun
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-daolujiaotongshigubaojingtai"></span>
          <div class="name">
            道路交通事故报警台
          </div>
          <div class="code-name">zfsoft-daolujiaotongshigubaojingtai
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-falvzixunrexian"></span>
          <div class="name">
            法律咨询热线
          </div>
          <div class="code-name">zfsoft-falvzixunrexian
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-gonggongweishenggongyirexian"></span>
          <div class="name">
            公共卫生公益热线
          </div>
          <div class="code-name">zfsoft-gonggongweishenggongyirexian
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-jidongcheweizhangchaxun"></span>
          <div class="name">
            机动车违章查询
          </div>
          <div class="code-name">zfsoft-jidongcheweizhangchaxun
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-gerenchaxun"></span>
          <div class="name">
            个人查询
          </div>
          <div class="code-name">zfsoft-gerenchaxun
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-guhuahuafeijiaona"></span>
          <div class="name">
            固话话费缴纳
          </div>
          <div class="code-name">zfsoft-guhuahuafeijiaona
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-gongjijinjiaocunmingxichaxun"></span>
          <div class="name">
            公积金缴存明细查询
          </div>
          <div class="code-name">zfsoft-gongjijinjiaocunmingxichaxun
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-hushizhiyezhucexinxichaxun"></span>
          <div class="name">
            护士执业注册信息查询
          </div>
          <div class="code-name">zfsoft-hushizhiyezhucexinxichaxun
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-mingongweiquanrexiandianhua"></span>
          <div class="name">
            民工维权热线电话
          </div>
          <div class="code-name">zfsoft-mingongweiquanrexiandianhua
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-qiyehangzhengchufaxinxi"></span>
          <div class="name">
            企业行政处罚信息
          </div>
          <div class="code-name">zfsoft-qiyehangzhengchufaxinxi
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-qixiangkepu"></span>
          <div class="name">
            气象科普
          </div>
          <div class="code-name">zfsoft-qixiangkepu
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-qiyexukexinxichaxun"></span>
          <div class="name">
            企业许可信息查询
          </div>
          <div class="code-name">zfsoft-qiyexukexinxichaxun
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-shangbaogonggao"></span>
          <div class="name">
            商报公告
          </div>
          <div class="code-name">zfsoft-shangbaogonggao
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-qiyejibenxinxichaxun"></span>
          <div class="name">
            企业基本信息查询
          </div>
          <div class="code-name">zfsoft-qiyejibenxinxichaxun
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-kaifashangchaxun"></span>
          <div class="name">
            开发商查询
          </div>
          <div class="code-name">zfsoft-kaifashangchaxun
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-shoujihuafeijiaona"></span>
          <div class="name">
            手机话费缴纳
          </div>
          <div class="code-name">zfsoft-shoujihuafeijiaona
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-gaokaochengjichaxun"></span>
          <div class="name">
            高考成绩查询
          </div>
          <div class="code-name">zfsoft-gaokaochengjichaxun
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-sheshuichaxun"></span>
          <div class="name">
            涉税查询
          </div>
          <div class="code-name">zfsoft-sheshuichaxun
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-nashuichaxun"></span>
          <div class="name">
            纳税查询
          </div>
          <div class="code-name">zfsoft-nashuichaxun
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-gongjijinjiaocunxinxichaxun"></span>
          <div class="name">
            公积金缴存信息查询
          </div>
          <div class="code-name">zfsoft-gongjijinjiaocunxinxichaxun
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-shuifeijiaona"></span>
          <div class="name">
            水费缴纳
          </div>
          <div class="code-name">zfsoft-shuifeijiaona
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-shangbiaochaxun"></span>
          <div class="name">
            商标查询
          </div>
          <div class="code-name">zfsoft-shangbiaochaxun
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-tianqiyubaochaxundianhua"></span>
          <div class="name">
            天气预报查询电话
          </div>
          <div class="code-name">zfsoft-tianqiyubaochaxundianhua
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-huanjingbaohujubaorexian"></span>
          <div class="name">
            环境保护举报热线
          </div>
          <div class="code-name">zfsoft-huanjingbaohujubaorexian
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-xuelichaxun"></span>
          <div class="name">
            学历查询
          </div>
          <div class="code-name">zfsoft-xuelichaxun
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-weishengkaoshichengjichaxun"></span>
          <div class="name">
            卫生考试成绩查询
          </div>
          <div class="code-name">zfsoft-weishengkaoshichengjichaxun
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-dianfeijiaona"></span>
          <div class="name">
            电费缴纳
          </div>
          <div class="code-name">zfsoft-dianfeijiaona
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-youzhengkehufuwurexian"></span>
          <div class="name">
            邮政客户服务热线
          </div>
          <div class="code-name">zfsoft-youzhengkehufuwurexian
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-shebaoyibao"></span>
          <div class="name">
            社保医保
          </div>
          <div class="code-name">zfsoft-shebaoyibao
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-zhongdengzhiyexuexiaobiyezhengshu"></span>
          <div class="name">
            中等职业学校毕业证书
          </div>
          <div class="code-name">zfsoft-zhongdengzhiyexuexiaobiyezhengshu
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-shenfenzhengbanzhengjinduchaxun"></span>
          <div class="name">
            身份证办证进度查询
          </div>
          <div class="code-name">zfsoft-shenfenzhengbanzhengjinduchaxun
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-nashuifuwurexian"></span>
          <div class="name">
            纳税服务热线
          </div>
          <div class="code-name">zfsoft-nashuifuwurexian
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-weishengkaoshiweiyuanhuizhichengkaoshi"></span>
          <div class="name">
            卫生考试委员会职称考试
          </div>
          <div class="code-name">zfsoft-weishengkaoshiweiyuanhuizhichengkaoshi
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-zhichengzhengzhikaoshichengjichaxun"></span>
          <div class="name">
            职称政治考试成绩查询
          </div>
          <div class="code-name">zfsoft-zhichengzhengzhikaoshichengjichaxun
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-qiyexinyongxinxigongshi"></span>
          <div class="name">
            企业信用信息公示
          </div>
          <div class="code-name">zfsoft-qiyexinyongxinxigongshi
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-tuixiurenyuandaiyujibenxinxi"></span>
          <div class="name">
            退休人员待遇基本信息
          </div>
          <div class="code-name">zfsoft-tuixiurenyuandaiyujibenxinxi
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-yibaojiaofei"></span>
          <div class="name">
            医保缴费
          </div>
          <div class="code-name">zfsoft-yibaojiaofei
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-zhongkaochengjichaxun"></span>
          <div class="name">
            中考成绩查询
          </div>
          <div class="code-name">zfsoft-zhongkaochengjichaxun
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-yishengzhiyezhucexinxichaxun"></span>
          <div class="name">
            医生职业注册信息查询
          </div>
          <div class="code-name">zfsoft-yishengzhiyezhucexinxichaxun
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-xinshengerzhongmingchaxun"></span>
          <div class="name">
            新生儿重名查询
          </div>
          <div class="code-name">zfsoft-xinshengerzhongmingchaxun
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-shuishangyuxianqiujiudianhua"></span>
          <div class="name">
            水上遇险求救电话
          </div>
          <div class="code-name">zfsoft-shuishangyuxianqiujiudianhua
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-shiminrexian"></span>
          <div class="name">
            市民热线
          </div>
          <div class="code-name">zfsoft-shiminrexian
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-quanguotielukefuzhongxin"></span>
          <div class="name">
            全国铁路客服中心
          </div>
          <div class="code-name">zfsoft-quanguotielukefuzhongxin
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-zhiliangjiandutousudianhua"></span>
          <div class="name">
            质量监督投诉电话
          </div>
          <div class="code-name">zfsoft-zhiliangjiandutousudianhua
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-jiagejiandujubaodianhua"></span>
          <div class="name">
            价格监督举报电话
          </div>
          <div class="code-name">zfsoft-jiagejiandujubaodianhua
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-tousurexian"></span>
          <div class="name">
            投诉热线
          </div>
          <div class="code-name">zfsoft-tousurexian
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-zhufangbaozhangguanli"></span>
          <div class="name">
            住房保障管理
          </div>
          <div class="code-name">zfsoft-zhufangbaozhangguanli
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-zhufanggongjijinkefurexian"></span>
          <div class="name">
            住房公积金客服热线
          </div>
          <div class="code-name">zfsoft-zhufanggongjijinkefurexian
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-baoxian"></span>
          <div class="name">
            保险
          </div>
          <div class="code-name">zfsoft-baoxian
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-baoshiyewuzhuanyonghaoma"></span>
          <div class="name">
            报时业务专用号码
          </div>
          <div class="code-name">zfsoft-baoshiyewuzhuanyonghaoma
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-daijiaoyanglaojin"></span>
          <div class="name">
            代缴养老金
          </div>
          <div class="code-name">zfsoft-daijiaoyanglaojin
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-chengzhenzhigongcanbaorenjibenxinxi"></span>
          <div class="name">
            城镇职工参保人基本信息
          </div>
          <div class="code-name">zfsoft-chengzhenzhigongcanbaorenjibenxinxi
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-ditufuwu1"></span>
          <div class="name">
            地图服务
          </div>
          <div class="code-name">zfsoft-ditufuwu1
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-cehuizizhichaxun"></span>
          <div class="name">
            测绘资质查询
          </div>
          <div class="code-name">zfsoft-cehuizizhichaxun
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-chengzhenzhigongcankaojiaofeijilu"></span>
          <div class="name">
            城镇职工参考缴费记录
          </div>
          <div class="code-name">zfsoft-chengzhenzhigongcankaojiaofeijilu
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-anbumenfenlei"></span>
          <div class="name">
            按部门分类
          </div>
          <div class="code-name">zfsoft-anbumenfenlei
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-anzhutifenlei"></span>
          <div class="name">
            按主题分类
          </div>
          <div class="code-name">zfsoft-anzhutifenlei
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-banshizhinan"></span>
          <div class="name">
            办事指南
          </div>
          <div class="code-name">zfsoft-banshizhinan
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-chengbaobujiaojisuan"></span>
          <div class="name">
            城保补缴计算
          </div>
          <div class="code-name">zfsoft-chengbaobujiaojisuan
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-ditufuwu"></span>
          <div class="name">
            地图服务
          </div>
          <div class="code-name">zfsoft-ditufuwu
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-chejianjisuanqi"></span>
          <div class="name">
            车检计算器
          </div>
          <div class="code-name">zfsoft-chejianjisuanqi
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-qiyeyichuangtong"></span>
          <div class="name">
            企业一窗通
          </div>
          <div class="code-name">zfsoft-qiyeyichuangtong
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-remenfuwu"></span>
          <div class="name">
            热门服务
          </div>
          <div class="code-name">zfsoft-remenfuwu
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-juzhuzhengjifenmonijisuanqi"></span>
          <div class="name">
            居住证积分模拟计算器
          </div>
          <div class="code-name">zfsoft-juzhuzhengjifenmonijisuanqi
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-touzixiangmuzaixianshenpijianguanpingtai"></span>
          <div class="name">
            投资项目在线审批监管平台
          </div>
          <div class="code-name">zfsoft-touzixiangmuzaixianshenpijianguanpingtai
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-zaixianbanli"></span>
          <div class="name">
            在线办理
          </div>
          <div class="code-name">zfsoft-zaixianbanli
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-yanglaojinmonijisuanqi"></span>
          <div class="name">
            养老金模拟计算器
          </div>
          <div class="code-name">zfsoft-yanglaojinmonijisuanqi
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-jidongchejiaotongweizhang"></span>
          <div class="name">
            机动车交通违章
          </div>
          <div class="code-name">zfsoft-jidongchejiaotongweizhang
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-yigeshiqing"></span>
          <div class="name">
            一个事情
          </div>
          <div class="code-name">zfsoft-yigeshiqing
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-budongchandengji"></span>
          <div class="name">
            不动产登记
          </div>
          <div class="code-name">zfsoft-budongchandengji
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-gongchengjianshexiangmufuwu-"></span>
          <div class="name">
            工程建设项目服务-2
          </div>
          <div class="code-name">zfsoft-gongchengjianshexiangmufuwu-
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-yaopinxinxichaxun"></span>
          <div class="name">
            药品信息查询
          </div>
          <div class="code-name">zfsoft-yaopinxinxichaxun
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-zhucehuijishichengjichaxun"></span>
          <div class="name">
            注册会计师成绩查询
          </div>
          <div class="code-name">zfsoft-zhucehuijishichengjichaxun
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-fapiaozhenweichaxun"></span>
          <div class="name">
            发票真伪查询
          </div>
          <div class="code-name">zfsoft-fapiaozhenweichaxun
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-qiyexinxichaxun"></span>
          <div class="name">
            企业信息查询
          </div>
          <div class="code-name">zfsoft-qiyexinxichaxun
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-gaodengjiaoyuxuelizhengshuchaxun"></span>
          <div class="name">
            高等教育学历证书查询
          </div>
          <div class="code-name">zfsoft-gaodengjiaoyuxuelizhengshuchaxun
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-qitatongleibanlixiang"></span>
          <div class="name">
            其他同类办理项
          </div>
          <div class="code-name">zfsoft-qitatongleibanlixiang
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-bumenrukou"></span>
          <div class="name">
            部门入口
          </div>
          <div class="code-name">zfsoft-bumenrukou
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-xiangguanwangshangbanli"></span>
          <div class="name">
            相关网上办理
          </div>
          <div class="code-name">zfsoft-xiangguanwangshangbanli
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-zhuyeweizhi"></span>
          <div class="name">
            主页位置
          </div>
          <div class="code-name">zfsoft-zhuyeweizhi
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-gongyongshiye"></span>
          <div class="name">
            公用事业
          </div>
          <div class="code-name">zfsoft-gongyongshiye
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-chujingrujing"></span>
          <div class="name">
            出境入境
          </div>
          <div class="code-name">zfsoft-chujingrujing
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-hujibanli"></span>
          <div class="name">
            户籍办理
          </div>
          <div class="code-name">zfsoft-hujibanli
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-gonggonganquan"></span>
          <div class="name">
            公共安全
          </div>
          <div class="code-name">zfsoft-gonggonganquan
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-diyazhiya"></span>
          <div class="name">
            抵押质押
          </div>
          <div class="code-name">zfsoft-diyazhiya
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-guihuajianshe"></span>
          <div class="name">
            规划建设
          </div>
          <div class="code-name">zfsoft-guihuajianshe
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-jiaotongchuhang"></span>
          <div class="name">
            交通出行
          </div>
          <div class="code-name">zfsoft-jiaotongchuhang
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-hangzhengjiaofei"></span>
          <div class="name">
            行政缴费
          </div>
          <div class="code-name">zfsoft-hangzhengjiaofei
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-shelibiangeng"></span>
          <div class="name">
            设立变更
          </div>
          <div class="code-name">zfsoft-shelibiangeng
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-lizhituixiu"></span>
          <div class="name">
            离职退休
          </div>
          <div class="code-name">zfsoft-lizhituixiu
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-jiuyechuangye"></span>
          <div class="name">
            就业创业
          </div>
          <div class="code-name">zfsoft-jiuyechuangye
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-shehuibaozhang"></span>
          <div class="name">
            社会保障
          </div>
          <div class="code-name">zfsoft-shehuibaozhang
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-siwangxunzang"></span>
          <div class="name">
            死亡殉葬
          </div>
          <div class="code-name">zfsoft-siwangxunzang
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-hunyindengji"></span>
          <div class="name">
            婚姻登记
          </div>
          <div class="code-name">zfsoft-hunyindengji
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-jiaoyukeyan"></span>
          <div class="name">
            教育科研
          </div>
          <div class="code-name">zfsoft-jiaoyukeyan
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-huanbaolvhua"></span>
          <div class="name">
            环保绿化
          </div>
          <div class="code-name">zfsoft-huanbaolvhua
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-minzuzongjiao"></span>
          <div class="name">
            民族宗教
          </div>
          <div class="code-name">zfsoft-minzuzongjiao
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-sifagongzheng"></span>
          <div class="name">
            司法公正
          </div>
          <div class="code-name">zfsoft-sifagongzheng
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-ruwufuyi"></span>
          <div class="name">
            入伍服役
          </div>
          <div class="code-name">zfsoft-ruwufuyi
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-shengyushouyang"></span>
          <div class="name">
            生育收养
          </div>
          <div class="code-name">zfsoft-shengyushouyang
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-lvyouguanguang"></span>
          <div class="name">
            旅游观光
          </div>
          <div class="code-name">zfsoft-lvyouguanguang
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-wenhuatiyu"></span>
          <div class="name">
            文化体育
          </div>
          <div class="code-name">zfsoft-wenhuatiyu
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-xiaofeiweiquan"></span>
          <div class="name">
            消费维权
          </div>
          <div class="code-name">zfsoft-xiaofeiweiquan
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-yiliaoweisheng"></span>
          <div class="name">
            医疗卫生
          </div>
          <div class="code-name">zfsoft-yiliaoweisheng
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-zhiyezige"></span>
          <div class="name">
            职业资格
          </div>
          <div class="code-name">zfsoft-zhiyezige
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-zhengjianbanli"></span>
          <div class="name">
            证件办理
          </div>
          <div class="code-name">zfsoft-zhengjianbanli
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-zhishichanquan"></span>
          <div class="name">
            知识产权
          </div>
          <div class="code-name">zfsoft-zhishichanquan
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-youdaifuxu"></span>
          <div class="name">
            优待抚恤
          </div>
          <div class="code-name">zfsoft-youdaifuxu
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-zhufangbaozhang"></span>
          <div class="name">
            住房保障
          </div>
          <div class="code-name">zfsoft-zhufangbaozhang
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-zhunyingzhunban"></span>
          <div class="name">
            准营准办
          </div>
          <div class="code-name">zfsoft-zhunyingzhunban
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-anquanshengchan"></span>
          <div class="name">
            安全生产
          </div>
          <div class="code-name">zfsoft-anquanshengchan
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-farenzhuxiao"></span>
          <div class="name">
            法人注销
          </div>
          <div class="code-name">zfsoft-farenzhuxiao
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-danganwenwu"></span>
          <div class="name">
            档案文物
          </div>
          <div class="code-name">zfsoft-danganwenwu
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-gonganxiaofang"></span>
          <div class="name">
            公安消防
          </div>
          <div class="code-name">zfsoft-gonganxiaofang
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-guotuheguihuajianshe"></span>
          <div class="name">
            国土和规划建设
          </div>
          <div class="code-name">zfsoft-guotuheguihuajianshe
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-jiaotongyunshu"></span>
          <div class="name">
            交通运输
          </div>
          <div class="code-name">zfsoft-jiaotongyunshu
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-nonglinmuyu"></span>
          <div class="name">
            农林牧渔
          </div>
          <div class="code-name">zfsoft-nonglinmuyu
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-jianyanjianyi"></span>
          <div class="name">
            检验检疫
          </div>
          <div class="code-name">zfsoft-jianyanjianyi
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-kejichuangxin"></span>
          <div class="name">
            科技创新
          </div>
          <div class="code-name">zfsoft-kejichuangxin
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-nianjiannianshen"></span>
          <div class="name">
            年检年审
          </div>
          <div class="code-name">zfsoft-nianjiannianshen
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-haiguankouan"></span>
          <div class="name">
            海关口岸
          </div>
          <div class="code-name">zfsoft-haiguankouan
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-rongzixindai"></span>
          <div class="name">
            融资信贷
          </div>
          <div class="code-name">zfsoft-rongzixindai
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-shangwumaoyi"></span>
          <div class="name">
            商务贸易
          </div>
          <div class="code-name">zfsoft-shangwumaoyi
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-wentijiaoyu"></span>
          <div class="name">
            文体教育
          </div>
          <div class="code-name">zfsoft-wentijiaoyu
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-shuishoucaiwu"></span>
          <div class="name">
            税收财务
          </div>
          <div class="code-name">zfsoft-shuishoucaiwu
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-shuiwuqixiang"></span>
          <div class="name">
            水务气象
          </div>
          <div class="code-name">zfsoft-shuiwuqixiang
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-zhiliangjishu"></span>
          <div class="name">
            质量技术
          </div>
          <div class="code-name">zfsoft-zhiliangjishu
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-yingduiqihoubianhua"></span>
          <div class="name">
            应对气候变化
          </div>
          <div class="code-name">zfsoft-yingduiqihoubianhua
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-zizhirenzheng"></span>
          <div class="name">
            资质认证
          </div>
          <div class="code-name">zfsoft-zizhirenzheng
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-renliziyuan"></span>
          <div class="name">
            人力资源
          </div>
          <div class="code-name">zfsoft-renliziyuan
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-zhaobiaopaimai"></span>
          <div class="name">
            招标拍卖
          </div>
          <div class="code-name">zfsoft-zhaobiaopaimai
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-shewaifuwu"></span>
          <div class="name">
            涉外服务
          </div>
          <div class="code-name">zfsoft-shewaifuwu
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-touzishenpi"></span>
          <div class="name">
            投资审批
          </div>
          <div class="code-name">zfsoft-touzishenpi
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-fangleizhuangzhijiance"></span>
          <div class="name">
            防雷装置检测
          </div>
          <div class="code-name">zfsoft-fangleizhuangzhijiance
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-anquanshengchanjiancejianyan"></span>
          <div class="name">
            安全生产检测检验
          </div>
          <div class="code-name">zfsoft-anquanshengchanjiancejianyan
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-anquanpingjia"></span>
          <div class="name">
            安全评价
          </div>
          <div class="code-name">zfsoft-anquanpingjia
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-fangsheweishengjishufuwu"></span>
          <div class="name">
            放射卫生技术服务
          </div>
          <div class="code-name">zfsoft-fangsheweishengjishufuwu
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-gongchengzaojiazixun"></span>
          <div class="name">
            工程造价咨询
          </div>
          <div class="code-name">zfsoft-gongchengzaojiazixun
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-gongchengsheji"></span>
          <div class="name">
            工程设计
          </div>
          <div class="code-name">zfsoft-gongchengsheji
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-gongchengjianli"></span>
          <div class="name">
            工程监理
          </div>
          <div class="code-name">zfsoft-gongchengjianli
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-dizhizaihaizhiligongchengjianli"></span>
          <div class="name">
            地质灾害治理工程监理
          </div>
          <div class="code-name">zfsoft-dizhizaihaizhiligongchengjianli
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-dizhizaihaizhiligongchengsheji"></span>
          <div class="name">
            地质灾害治理工程设计
          </div>
          <div class="code-name">zfsoft-dizhizaihaizhiligongchengsheji
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-huanjingwuranzhilisheshiyunying"></span>
          <div class="name">
            环境污染治理设施运营
          </div>
          <div class="code-name">zfsoft-huanjingwuranzhilisheshiyunying
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-jingyingzhengquanyewu"></span>
          <div class="name">
            经营证券业务
          </div>
          <div class="code-name">zfsoft-jingyingzhengquanyewu
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-gonglushuiyungongchengshiyanjiance"></span>
          <div class="name">
            公路水运工程试验检测
          </div>
          <div class="code-name">zfsoft-gonglushuiyungongchengshiyanjiance
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-jiliangbiaozhunqijuxiaoyan"></span>
          <div class="name">
            计量标准器具校验
          </div>
          <div class="code-name">zfsoft-jiliangbiaozhunqijuxiaoyan
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-heyanhechafuwu"></span>
          <div class="name">
            核验核查服务
          </div>
          <div class="code-name">zfsoft-heyanhechafuwu
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-gongchengkancha"></span>
          <div class="name">
            工程勘察
          </div>
          <div class="code-name">zfsoft-gongchengkancha
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-shigongtushejiwenjianshencha"></span>
          <div class="name">
            施工图设计文件审查
          </div>
          <div class="code-name">zfsoft-shigongtushejiwenjianshencha
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-lvshishiwusuofuwu"></span>
          <div class="name">
            律师事务所服务
          </div>
          <div class="code-name">zfsoft-lvshishiwusuofuwu
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-dizhizaihaiweixianxingpinggu"></span>
          <div class="name">
            地质灾害危险性评估
          </div>
          <div class="code-name">zfsoft-dizhizaihaiweixianxingpinggu
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-cehui"></span>
          <div class="name">
            测绘
          </div>
          <div class="code-name">zfsoft-cehui
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-jianshegongchengzhiliangjiance"></span>
          <div class="name">
            建设工程质量检测
          </div>
          <div class="code-name">zfsoft-jianshegongchengzhiliangjiance
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-renfanggongchengfanghushebeizhiliangjiance"></span>
          <div class="name">
            人防工程防护设备质量检测
          </div>
          <div class="code-name">zfsoft-renfanggongchengfanghushebeizhiliangjiance
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-fangdichanpinggu"></span>
          <div class="name">
            房地产评估
          </div>
          <div class="code-name">zfsoft-fangdichanpinggu
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-renfanggongchengjianli"></span>
          <div class="name">
            人防工程监理
          </div>
          <div class="code-name">zfsoft-renfanggongchengjianli
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-tankuangquancaikuangquanpinggu"></span>
          <div class="name">
            探矿权采矿权评估
          </div>
          <div class="code-name">zfsoft-tankuangquancaikuangquanpinggu
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-wenwubaohugongchengjianli"></span>
          <div class="name">
            文物保护工程监理
          </div>
          <div class="code-name">zfsoft-wenwubaohugongchengjianli
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-shuitubaochifanganbianzhi"></span>
          <div class="name">
            水土保持方案编制
          </div>
          <div class="code-name">zfsoft-shuitubaochifanganbianzhi
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-kuangchanziyuankaifaliyongfanganbianzhi"></span>
          <div class="name">
            矿产资源开发利用方案编制
          </div>
          <div class="code-name">zfsoft-kuangchanziyuankaifaliyongfanganbianzhi
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-shuiziyuanlunzheng"></span>
          <div class="name">
            水资源论证
          </div>
          <div class="code-name">zfsoft-shuiziyuanlunzheng
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-tudipinggu"></span>
          <div class="name">
            土地评估
          </div>
          <div class="code-name">zfsoft-tudipinggu
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-zaolingongchengguihuasheji"></span>
          <div class="name">
            造林工程规划设计
          </div>
          <div class="code-name">zfsoft-zaolingongchengguihuasheji
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-gongchengzixun"></span>
          <div class="name">
            工程咨询
          </div>
          <div class="code-name">zfsoft-gongchengzixun
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-gongzhengfuwu"></span>
          <div class="name">
            公证服务
          </div>
          <div class="code-name">zfsoft-gongzhengfuwu
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-wenwubaohugongchengshigong"></span>
          <div class="name">
            文物保护工程施工
          </div>
          <div class="code-name">zfsoft-wenwubaohugongchengshigong
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-renfanggongchengshigongtushejiwenjianshencha"></span>
          <div class="name">
            人防工程施工图设计文件审查
          </div>
          <div class="code-name">zfsoft-renfanggongchengshigongtushejiwenjianshencha
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-shuiligongchengjianli"></span>
          <div class="name">
            水利工程监理
          </div>
          <div class="code-name">zfsoft-shuiligongchengjianli
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-shuitubaochijiance"></span>
          <div class="name">
            水土保持监测
          </div>
          <div class="code-name">zfsoft-shuitubaochijiance
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-xinxixitonggongchengjianli"></span>
          <div class="name">
            信息系统工程监理
          </div>
          <div class="code-name">zfsoft-xinxixitonggongchengjianli
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-zaolingongchengjianli"></span>
          <div class="name">
            造林工程监理
          </div>
          <div class="code-name">zfsoft-zaolingongchengjianli
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-zhiyeweishengjishufuwu"></span>
          <div class="name">
            职业卫生技术服务
          </div>
          <div class="code-name">zfsoft-zhiyeweishengjishufuwu
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-huijishishiwusuofuwu"></span>
          <div class="name">
            会计师事务所服务
          </div>
          <div class="code-name">zfsoft-huijishishiwusuofuwu
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-shuiwushishiwusuofuwu"></span>
          <div class="name">
            税务师事务所服务
          </div>
          <div class="code-name">zfsoft-shuiwushishiwusuofuwu
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-jianshexiangmuhuanjingyingxiangpingjia"></span>
          <div class="name">
            建设项目环境影响评价
          </div>
          <div class="code-name">zfsoft-jianshexiangmuhuanjingyingxiangpingjia
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-chengxiangguihuabianzhi"></span>
          <div class="name">
            城乡规划编制
          </div>
          <div class="code-name">zfsoft-chengxiangguihuabianzhi
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-tudikancha"></span>
          <div class="name">
            土地勘察
          </div>
          <div class="code-name">zfsoft-tudikancha
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-zichanpinggu"></span>
          <div class="name">
            资产评估
          </div>
          <div class="code-name">zfsoft-zichanpinggu
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-shuiwenshuiziyuantiaochapingjia"></span>
          <div class="name">
            水文水资源调查评价
          </div>
          <div class="code-name">zfsoft-shuiwenshuiziyuantiaochapingjia
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-jiagepinggu"></span>
          <div class="name">
            价格评估
          </div>
          <div class="code-name">zfsoft-jiagepinggu
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-zhaiquanxinyongpingji"></span>
          <div class="name">
            债券信用评级
          </div>
          <div class="code-name">zfsoft-zhaiquanxinyongpingji
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-tudiguihua"></span>
          <div class="name">
            土地规划
          </div>
          <div class="code-name">zfsoft-tudiguihua
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-yaowulinchuangshiyanfuwu"></span>
          <div class="name">
            药物临床试验服务
          </div>
          <div class="code-name">zfsoft-yaowulinchuangshiyanfuwu
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-haiyushiyonglunzheng"></span>
          <div class="name">
            海域使用论证
          </div>
          <div class="code-name">zfsoft-haiyushiyonglunzheng
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-linyetiaochaguihuasheji"></span>
          <div class="name">
            林业调查规划设计
          </div>
          <div class="code-name">zfsoft-linyetiaochaguihuasheji
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-shifangqiqiufuwu"></span>
          <div class="name">
            施放气球服务
          </div>
          <div class="code-name">zfsoft-shifangqiqiufuwu
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-jianyanjiancefuwu"></span>
          <div class="name">
            检验检测服务
          </div>
          <div class="code-name">zfsoft-jianyanjiancefuwu
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-jiaotonggongchengjianli"></span>
          <div class="name">
            交通工程监理
          </div>
          <div class="code-name">zfsoft-jiaotonggongchengjianli
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-renfanggongchengheqitarenfangfanghusheshisheji"></span>
          <div class="name">
            人防工程和其他人防防护设施设计
          </div>
          <div class="code-name">zfsoft-renfanggongchengheqitarenfangfanghusheshisheji
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-shuiligongchengzhiliangjiance"></span>
          <div class="name">
            水利工程质量检测
          </div>
          <div class="code-name">zfsoft-shuiligongchengzhiliangjiance
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-sifa"></span>
          <div class="name">
            司法
          </div>
          <div class="code-name">zfsoft-sifa
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-wenwubaohugongchengkanchasheji"></span>
          <div class="name">
            文物保护工程勘察设计
          </div>
          <div class="code-name">zfsoft-wenwubaohugongchengkanchasheji
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-ziliaoshangchuan"></span>
          <div class="name">
            资料上传
          </div>
          <div class="code-name">zfsoft-ziliaoshangchuan
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-shenchashouli"></span>
          <div class="name">
            审查受理
          </div>
          <div class="code-name">zfsoft-shenchashouli
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-chushen"></span>
          <div class="name">
            初审
          </div>
          <div class="code-name">zfsoft-chushen
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-chushendafu"></span>
          <div class="name">
            初审答复
          </div>
          <div class="code-name">zfsoft-chushendafu
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-fushen"></span>
          <div class="name">
            复审
          </div>
          <div class="code-name">zfsoft-fushen
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-fushendafu"></span>
          <div class="name">
            复审答复
          </div>
          <div class="code-name">zfsoft-fushendafu
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-xiangmudaima"></span>
          <div class="name">
            项目代码
          </div>
          <div class="code-name">zfsoft-xiangmudaima
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-kanchadanwei"></span>
          <div class="name">
            勘察单位
          </div>
          <div class="code-name">zfsoft-kanchadanwei
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-shejidanwei"></span>
          <div class="name">
            设计单位
          </div>
          <div class="code-name">zfsoft-shejidanwei
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-tushengjigou"></span>
          <div class="name">
            图省机构
          </div>
          <div class="code-name">zfsoft-tushengjigou
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-qianming"></span>
          <div class="name">
            签名
          </div>
          <div class="code-name">zfsoft-qianming
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-shizhongshihoujianguan"></span>
          <div class="name">
            事中事后监管
          </div>
          <div class="code-name">zfsoft-shizhongshihoujianguan
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-pingmusuoding"></span>
          <div class="name">
            屏幕锁定
          </div>
          <div class="code-name">zfsoft-pingmusuoding
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-daochu"></span>
          <div class="name">
            导出
          </div>
          <div class="code-name">zfsoft-daochu
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-yincang"></span>
          <div class="name">
            隐藏
          </div>
          <div class="code-name">zfsoft-yincang
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-zhankai"></span>
          <div class="name">
            展开
          </div>
          <div class="code-name">zfsoft-zhankai
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-baocun"></span>
          <div class="name">
            保存
          </div>
          <div class="code-name">zfsoft-baocun
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-quxiao1"></span>
          <div class="name">
            取消
          </div>
          <div class="code-name">zfsoft-quxiao1
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-dakaishexiangtou"></span>
          <div class="name">
            打开摄像头
          </div>
          <div class="code-name">zfsoft-dakaishexiangtou
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-paizhao"></span>
          <div class="name">
            拍照
          </div>
          <div class="code-name">zfsoft-paizhao
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-shenfenzhengduqu"></span>
          <div class="name">
            身份证读取
          </div>
          <div class="code-name">zfsoft-shenfenzhengduqu
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-zuidahua"></span>
          <div class="name">
            最大化
          </div>
          <div class="code-name">zfsoft-zuidahua
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-tiaoguo"></span>
          <div class="name">
            跳过
          </div>
          <div class="code-name">zfsoft-tiaoguo
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-tuwenliebiaoweidianji"></span>
          <div class="name">
            图文列表（未点击）
          </div>
          <div class="code-name">zfsoft-tuwenliebiaoweidianji
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-liebiaoweidianji"></span>
          <div class="name">
            列表（未点击）
          </div>
          <div class="code-name">zfsoft-liebiaoweidianji
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-fangda"></span>
          <div class="name">
            放大
          </div>
          <div class="code-name">zfsoft-fangda
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-huanyuan"></span>
          <div class="name">
            还原
          </div>
          <div class="code-name">zfsoft-huanyuan
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-pingjiaguanli"></span>
          <div class="name">
            评价管理
          </div>
          <div class="code-name">zfsoft-pingjiaguanli
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-banjianyewutongji"></span>
          <div class="name">
            办件业务统计
          </div>
          <div class="code-name">zfsoft-banjianyewutongji
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-banjianzhengzhaoguanli"></span>
          <div class="name">
            办件证照管理
          </div>
          <div class="code-name">zfsoft-banjianzhengzhaoguanli
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-hudongchaxun"></span>
          <div class="name">
            互动查询
          </div>
          <div class="code-name">zfsoft-hudongchaxun
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-hudongguanli"></span>
          <div class="name">
            互动管理
          </div>
          <div class="code-name">zfsoft-hudongguanli
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-banjianchaxun"></span>
          <div class="name">
            办件查询
          </div>
          <div class="code-name">zfsoft-banjianchaxun
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-chuangkoujiaohao"></span>
          <div class="name">
            窗口叫号
          </div>
          <div class="code-name">zfsoft-chuangkoujiaohao
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-cailiaochuku"></span>
          <div class="name">
            材料出库
          </div>
          <div class="code-name">zfsoft-cailiaochuku
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-mingcileijieshileizhishikuguanli"></span>
          <div class="name">
            名词类解释类知识库管理
          </div>
          <div class="code-name">zfsoft-mingcileijieshileizhishikuguanli
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-hulianwangyushen"></span>
          <div class="name">
            互联网预审
          </div>
          <div class="code-name">zfsoft-hulianwangyushen
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-saomiaoyiguanli"></span>
          <div class="name">
            扫描仪管理
          </div>
          <div class="code-name">zfsoft-saomiaoyiguanli
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-renzhengheyan"></span>
          <div class="name">
            人证核验
          </div>
          <div class="code-name">zfsoft-renzhengheyan
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-qujiaohaoguanli"></span>
          <div class="name">
            取叫号管理
          </div>
          <div class="code-name">zfsoft-qujiaohaoguanli
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-pingtaiyunhangpeizhi"></span>
          <div class="name">
            平台运行配置
          </div>
          <div class="code-name">zfsoft-pingtaiyunhangpeizhi
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-zhengceleizhishikuguanli"></span>
          <div class="name">
            政策类知识库管理
          </div>
          <div class="code-name">zfsoft-zhengceleizhishikuguanli
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-zancunshouli"></span>
          <div class="name">
            暂存受理
          </div>
          <div class="code-name">zfsoft-zancunshouli
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-zhishikuguanli"></span>
          <div class="name">
            知识库管理
          </div>
          <div class="code-name">zfsoft-zhishikuguanli
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-zhengminhudongtongji"></span>
          <div class="name">
            政民互动统计
          </div>
          <div class="code-name">zfsoft-zhengminhudongtongji
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-zonghechaxun"></span>
          <div class="name">
            综合查询
          </div>
          <div class="code-name">zfsoft-zonghechaxun
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-zhengzhaoguanli"></span>
          <div class="name">
            证照管理
          </div>
          <div class="code-name">zfsoft-zhengzhaoguanli
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-wendaleizhishikuguanli"></span>
          <div class="name">
            问答类知识库管理
          </div>
          <div class="code-name">zfsoft-wendaleizhishikuguanli
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-yewushouli"></span>
          <div class="name">
            业务受理
          </div>
          <div class="code-name">zfsoft-yewushouli
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-qingdanchaxun"></span>
          <div class="name">
            清单查询
          </div>
          <div class="code-name">zfsoft-qingdanchaxun
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-tongyifazheng"></span>
          <div class="name">
            统一发证
          </div>
          <div class="code-name">zfsoft-tongyifazheng
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-woyaozixun"></span>
          <div class="name">
            我要咨询
          </div>
          <div class="code-name">zfsoft-woyaozixun
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-woyaojianyi"></span>
          <div class="name">
            我要建议
          </div>
          <div class="code-name">zfsoft-woyaojianyi
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-zaixianshenbao"></span>
          <div class="name">
            在线申报
          </div>
          <div class="code-name">zfsoft-zaixianshenbao
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-wuliu"></span>
          <div class="name">
            物流
          </div>
          <div class="code-name">zfsoft-wuliu
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-zaixianyuyue1"></span>
          <div class="name">
            在线预约
          </div>
          <div class="code-name">zfsoft-zaixianyuyue1
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-dizhi"></span>
          <div class="name">
            地址
          </div>
          <div class="code-name">zfsoft-dizhi
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-mima-suoding"></span>
          <div class="name">
            密码-锁定
          </div>
          <div class="code-name">zfsoft-mima-suoding
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-jiesuo"></span>
          <div class="name">
            解锁
          </div>
          <div class="code-name">zfsoft-jiesuo
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-banbenpeizhi"></span>
          <div class="name">
            版本配置
          </div>
          <div class="code-name">zfsoft-banbenpeizhi
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-beiwanglu"></span>
          <div class="name">
            备忘录
          </div>
          <div class="code-name">zfsoft-beiwanglu
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-saoma"></span>
          <div class="name">
            扫码
          </div>
          <div class="code-name">zfsoft-saoma
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-shouye"></span>
          <div class="name">
            首页
          </div>
          <div class="code-name">zfsoft-shouye
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-riqi"></span>
          <div class="name">
            日期
          </div>
          <div class="code-name">zfsoft-riqi
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-yonghuzhongxin"></span>
          <div class="name">
            用户中心
          </div>
          <div class="code-name">zfsoft-yonghuzhongxin
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-wenhao-bangzhu"></span>
          <div class="name">
            问号-帮助
          </div>
          <div class="code-name">zfsoft-wenhao-bangzhu
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-caidan-heng"></span>
          <div class="name">
            菜单-横
          </div>
          <div class="code-name">zfsoft-caidan-heng
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-caidan-fangkuai"></span>
          <div class="name">
            菜单-方块
          </div>
          <div class="code-name">zfsoft-caidan-fangkuai
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-xiaoxi"></span>
          <div class="name">
            消息
          </div>
          <div class="code-name">zfsoft-xiaoxi
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-huanfu"></span>
          <div class="name">
            换肤
          </div>
          <div class="code-name">zfsoft-huanfu
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-yichuangzongheguanli"></span>
          <div class="name">
            一窗综合管理
          </div>
          <div class="code-name">zfsoft-yichuangzongheguanli
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-shujuguanli1"></span>
          <div class="name">
            数据管理
          </div>
          <div class="code-name">zfsoft-shujuguanli1
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-yichuangbanli"></span>
          <div class="name">
            一窗办理
          </div>
          <div class="code-name">zfsoft-yichuangbanli
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-yulan"></span>
          <div class="name">
            预览
          </div>
          <div class="code-name">zfsoft-yulan
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-wangzhanguanli"></span>
          <div class="name">
            网站管理
          </div>
          <div class="code-name">zfsoft-wangzhanguanli
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-xinzeng"></span>
          <div class="name">
            新增
          </div>
          <div class="code-name">zfsoft-xinzeng
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-wenjianshangchuan"></span>
          <div class="name">
            文件上传
          </div>
          <div class="code-name">zfsoft-wenjianshangchuan
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-tongjibaobiao"></span>
          <div class="name">
            统计报表
          </div>
          <div class="code-name">zfsoft-tongjibaobiao
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft--dengru"></span>
          <div class="name">
            1-登入
          </div>
          <div class="code-name">zfsoft--dengru
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-duoyun"></span>
          <div class="name">
            多云
          </div>
          <div class="code-name">zfsoft-duoyun
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-yutian"></span>
          <div class="name">
            雨天
          </div>
          <div class="code-name">zfsoft-yutian
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-yintian"></span>
          <div class="name">
            阴天
          </div>
          <div class="code-name">zfsoft-yintian
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-xuetian"></span>
          <div class="name">
            雪天
          </div>
          <div class="code-name">zfsoft-xuetian
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-leiyushandian"></span>
          <div class="name">
            雷雨闪电
          </div>
          <div class="code-name">zfsoft-leiyushandian
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-budongchanyichuang"></span>
          <div class="name">
            不动产一窗
          </div>
          <div class="code-name">zfsoft-budongchanyichuang
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-huyanmoshi"></span>
          <div class="name">
            护眼模式
          </div>
          <div class="code-name">zfsoft-huyanmoshi
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-daibanrenwu"></span>
          <div class="name">
            待办任务
          </div>
          <div class="code-name">zfsoft-daibanrenwu
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-gongzuotai"></span>
          <div class="name">
            工作台
          </div>
          <div class="code-name">zfsoft-gongzuotai
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-nvxing"></span>
          <div class="name">
            女性
          </div>
          <div class="code-name">zfsoft-nvxing
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-jichuzhicheng"></span>
          <div class="name">
            基础支撑
          </div>
          <div class="code-name">zfsoft-jichuzhicheng
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-nanxing"></span>
          <div class="name">
            男性
          </div>
          <div class="code-name">zfsoft-nanxing
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-liantongbumen"></span>
          <div class="name">
            联通部门
          </div>
          <div class="code-name">zfsoft-liantongbumen
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-xuanzhong"></span>
          <div class="name">
            选中
          </div>
          <div class="code-name">zfsoft-xuanzhong
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-rijianmoshi1"></span>
          <div class="name">
            日间模式
          </div>
          <div class="code-name">zfsoft-rijianmoshi1
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-shujuguanli"></span>
          <div class="name">
            数据管理
          </div>
          <div class="code-name">zfsoft-shujuguanli
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-tongjifenxi"></span>
          <div class="name">
            统计分析
          </div>
          <div class="code-name">zfsoft-tongjifenxi
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-shaixuan"></span>
          <div class="name">
            筛选
          </div>
          <div class="code-name">zfsoft-shaixuan
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-yejianmoshi"></span>
          <div class="name">
            夜间模式
          </div>
          <div class="code-name">zfsoft-yejianmoshi
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-quyuhuafen"></span>
          <div class="name">
            区域划分
          </div>
          <div class="code-name">zfsoft-quyuhuafen
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-tianqi"></span>
          <div class="name">
            天气
          </div>
          <div class="code-name">zfsoft-tianqi
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-xiala"></span>
          <div class="name">
            下拉
          </div>
          <div class="code-name">zfsoft-xiala
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-jifen"></span>
          <div class="name">
            积分
          </div>
          <div class="code-name">zfsoft-jifen
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-banlixiang"></span>
          <div class="name">
            办理项
          </div>
          <div class="code-name">zfsoft-banlixiang
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-dakai"></span>
          <div class="name">
            打开
          </div>
          <div class="code-name">zfsoft-dakai
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-tatrea"></span>
          <div class="name">
            tatrea
          </div>
          <div class="code-name">zfsoft-tatrea
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-biaoge"></span>
          <div class="name">
            表格
          </div>
          <div class="code-name">zfsoft-biaoge
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-duibi"></span>
          <div class="name">
            对比
          </div>
          <div class="code-name">zfsoft-duibi
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-DIV"></span>
          <div class="name">
            DIV
          </div>
          <div class="code-name">zfsoft-DIV
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-fenxiang"></span>
          <div class="name">
            分享
          </div>
          <div class="code-name">zfsoft-fenxiang
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-dianzan"></span>
          <div class="name">
            点赞
          </div>
          <div class="code-name">zfsoft-dianzan
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-guanbi"></span>
          <div class="name">
            关闭
          </div>
          <div class="code-name">zfsoft-guanbi
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-gengxin"></span>
          <div class="name">
            更新
          </div>
          <div class="code-name">zfsoft-gengxin
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-dengji1"></span>
          <div class="name">
            等级
          </div>
          <div class="code-name">zfsoft-dengji1
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-feizhengchang"></span>
          <div class="name">
            非正常
          </div>
          <div class="code-name">zfsoft-feizhengchang
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-gengduo"></span>
          <div class="name">
            更多
          </div>
          <div class="code-name">zfsoft-gengduo
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-guidang"></span>
          <div class="name">
            归档
          </div>
          <div class="code-name">zfsoft-guidang
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-guangbo"></span>
          <div class="name">
            广播
          </div>
          <div class="code-name">zfsoft-guangbo
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-guanzhu"></span>
          <div class="name">
            关注
          </div>
          <div class="code-name">zfsoft-guanzhu
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-jindutiao"></span>
          <div class="name">
            进度条
          </div>
          <div class="code-name">zfsoft-jindutiao
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-putongwenzianniu"></span>
          <div class="name">
            普通文字按钮
          </div>
          <div class="code-name">zfsoft-putongwenzianniu
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-jiucuo"></span>
          <div class="name">
            纠错
          </div>
          <div class="code-name">zfsoft-jiucuo
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-huiyuan"></span>
          <div class="name">
            会员
          </div>
          <div class="code-name">zfsoft-huiyuan
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-shangjiantou"></span>
          <div class="name">
            上箭头
          </div>
          <div class="code-name">zfsoft-shangjiantou
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-jiujitujing"></span>
          <div class="name">
            救济途径
          </div>
          <div class="code-name">zfsoft-jiujitujing
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-jinggao"></span>
          <div class="name">
            警告
          </div>
          <div class="code-name">zfsoft-jinggao
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-shenglvehao"></span>
          <div class="name">
            省略号
          </div>
          <div class="code-name">zfsoft-shenglvehao
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-banzhengjieguo"></span>
          <div class="name">
            办证结果
          </div>
          <div class="code-name">zfsoft-banzhengjieguo
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-tijiao"></span>
          <div class="name">
            提交
          </div>
          <div class="code-name">zfsoft-tijiao
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-quxiaorenling"></span>
          <div class="name">
            取消认领
          </div>
          <div class="code-name">zfsoft-quxiaorenling
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-lianbanjigou"></span>
          <div class="name">
            联办机构
          </div>
          <div class="code-name">zfsoft-lianbanjigou
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-fanhuidingbu"></span>
          <div class="name">
            返回顶部
          </div>
          <div class="code-name">zfsoft-fanhuidingbu
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-queren"></span>
          <div class="name">
            确认
          </div>
          <div class="code-name">zfsoft-queren
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-tupianpaiban"></span>
          <div class="name">
            图片排版
          </div>
          <div class="code-name">zfsoft-tupianpaiban
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-tubiao"></span>
          <div class="name">
            图标
          </div>
          <div class="code-name">zfsoft-tubiao
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-teshuchengxu"></span>
          <div class="name">
            特殊程序
          </div>
          <div class="code-name">zfsoft-teshuchengxu
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-shoucang"></span>
          <div class="name">
            收藏
          </div>
          <div class="code-name">zfsoft-shoucang
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-xiajiantou"></span>
          <div class="name">
            下箭头
          </div>
          <div class="code-name">zfsoft-xiajiantou
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-shenchagongzuoxize"></span>
          <div class="name">
            审查工作细则
          </div>
          <div class="code-name">zfsoft-shenchagongzuoxize
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-quxiaozanting"></span>
          <div class="name">
            取消暂停
          </div>
          <div class="code-name">zfsoft-quxiaozanting
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-tijiaoshenhe"></span>
          <div class="name">
            提交审核
          </div>
          <div class="code-name">zfsoft-tijiaoshenhe
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-xinwen"></span>
          <div class="name">
            新闻
          </div>
          <div class="code-name">zfsoft-xinwen
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-zhuce"></span>
          <div class="name">
            注册
          </div>
          <div class="code-name">zfsoft-zhuce
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-zuojiantou"></span>
          <div class="name">
            左箭头
          </div>
          <div class="code-name">zfsoft-zuojiantou
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-shurukuang"></span>
          <div class="name">
            输入框
          </div>
          <div class="code-name">zfsoft-shurukuang
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-youjiantou"></span>
          <div class="name">
            右箭头
          </div>
          <div class="code-name">zfsoft-youjiantou
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-zhengchang"></span>
          <div class="name">
            正常
          </div>
          <div class="code-name">zfsoft-zhengchang
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-zhengcefagui"></span>
          <div class="name">
            政策法规
          </div>
          <div class="code-name">zfsoft-zhengcefagui
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-zhongjiefuwuyaosu"></span>
          <div class="name">
            中介服务要素
          </div>
          <div class="code-name">zfsoft-zhongjiefuwuyaosu
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-xiangguanshishiqingdan"></span>
          <div class="name">
            相关实施清单
          </div>
          <div class="code-name">zfsoft-xiangguanshishiqingdan
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-jinru"></span>
          <div class="name">
            进入
          </div>
          <div class="code-name">zfsoft-jinru
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-Szhihangyici"></span>
          <div class="name">
            5S执行一次
          </div>
          <div class="code-name">zfsoft-Szhihangyici
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-chakanyonghu"></span>
          <div class="name">
            查看用户
          </div>
          <div class="code-name">zfsoft-chakanyonghu
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-fuzhi"></span>
          <div class="name">
            复制
          </div>
          <div class="code-name">zfsoft-fuzhi
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-baojing"></span>
          <div class="name">
            报警
          </div>
          <div class="code-name">zfsoft-baojing
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-Szhihangyici1"></span>
          <div class="name">
            1S执行一次
          </div>
          <div class="code-name">zfsoft-Szhihangyici1
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-bianzhi"></span>
          <div class="name">
            编制
          </div>
          <div class="code-name">zfsoft-bianzhi
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-biangeng"></span>
          <div class="name">
            变更
          </div>
          <div class="code-name">zfsoft-biangeng
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-hebing"></span>
          <div class="name">
            合并
          </div>
          <div class="code-name">zfsoft-hebing
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-huifu"></span>
          <div class="name">
            恢复
          </div>
          <div class="code-name">zfsoft-huifu
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-chexiao"></span>
          <div class="name">
            撤销
          </div>
          <div class="code-name">zfsoft-chexiao
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-chakan"></span>
          <div class="name">
            查看
          </div>
          <div class="code-name">zfsoft-chakan
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-dengji"></span>
          <div class="name">
            登记
          </div>
          <div class="code-name">zfsoft-dengji
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-fabu"></span>
          <div class="name">
            发布
          </div>
          <div class="code-name">zfsoft-fabu
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-biangengjilu"></span>
          <div class="name">
            变更记录
          </div>
          <div class="code-name">zfsoft-biangengjilu
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-feiqi"></span>
          <div class="name">
            废弃
          </div>
          <div class="code-name">zfsoft-feiqi
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-lijizhihangyici"></span>
          <div class="name">
            立即执行一次
          </div>
          <div class="code-name">zfsoft-lijizhihangyici
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-qiyong"></span>
          <div class="name">
            启用
          </div>
          <div class="code-name">zfsoft-qiyong
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-chaifen"></span>
          <div class="name">
            拆分
          </div>
          <div class="code-name">zfsoft-chaifen
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-huanjieshezhi"></span>
          <div class="name">
            环节设置
          </div>
          <div class="code-name">zfsoft-huanjieshezhi
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-renling"></span>
          <div class="name">
            认领
          </div>
          <div class="code-name">zfsoft-renling
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-quxiao"></span>
          <div class="name">
            取消
          </div>
          <div class="code-name">zfsoft-quxiao
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-lishibanben"></span>
          <div class="name">
            历史版本
          </div>
          <div class="code-name">zfsoft-lishibanben
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-jinyong"></span>
          <div class="name">
            禁用
          </div>
          <div class="code-name">zfsoft-jinyong
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-huishouzhan"></span>
          <div class="name">
            回收站
          </div>
          <div class="code-name">zfsoft-huishouzhan
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-jiankong"></span>
          <div class="name">
            监控
          </div>
          <div class="code-name">zfsoft-jiankong
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-shouquan"></span>
          <div class="name">
            授权
          </div>
          <div class="code-name">zfsoft-shouquan
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-shezhi"></span>
          <div class="name">
            设置
          </div>
          <div class="code-name">zfsoft-shezhi
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-quxiaofabu"></span>
          <div class="name">
            取消发布
          </div>
          <div class="code-name">zfsoft-quxiaofabu
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-liuchengbianzhi"></span>
          <div class="name">
            流程编制
          </div>
          <div class="code-name">zfsoft-liuchengbianzhi
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-shenhejilu"></span>
          <div class="name">
            审核记录
          </div>
          <div class="code-name">zfsoft-shenhejilu
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-tongbu"></span>
          <div class="name">
            同步
          </div>
          <div class="code-name">zfsoft-tongbu
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-xiugai"></span>
          <div class="name">
            修改
          </div>
          <div class="code-name">zfsoft-xiugai
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-xiazai"></span>
          <div class="name">
            下载
          </div>
          <div class="code-name">zfsoft-xiazai
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-zanting"></span>
          <div class="name">
            暂停
          </div>
          <div class="code-name">zfsoft-zanting
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-shanchu"></span>
          <div class="name">
            删除
          </div>
          <div class="code-name">zfsoft-shanchu
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-shenhe"></span>
          <div class="name">
            审核
          </div>
          <div class="code-name">zfsoft-shenhe
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-zixiang"></span>
          <div class="name">
            子项
          </div>
          <div class="code-name">zfsoft-zixiang
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-yijianshezhi"></span>
          <div class="name">
            意见设置
          </div>
          <div class="code-name">zfsoft-yijianshezhi
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-yidong"></span>
          <div class="name">
            移动
          </div>
          <div class="code-name">zfsoft-yidong
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-liuchengchakan"></span>
          <div class="name">
            流程查看
          </div>
          <div class="code-name">zfsoft-liuchengchakan
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-zhuxiao"></span>
          <div class="name">
            注销
          </div>
          <div class="code-name">zfsoft-zhuxiao
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-zijigou"></span>
          <div class="name">
            子机构
          </div>
          <div class="code-name">zfsoft-zijigou
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-tiaozheng"></span>
          <div class="name">
            调整
          </div>
          <div class="code-name">zfsoft-tiaozheng
          </div>
        </li>

        <li  @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-shangyi"></span>
          <div class="name">
            上移
          </div>
          <div class="code-name">zfsoft-shangyi
          </div>
        </li>

        <li @click="getSelectClass" class="dib" >
          <span class="icon iconfont zfsoft-xiayi"></span>
          <div class="name">
            下移
          </div>
          <div class="code-name">zfsoft-xiayi
          </div>
        </li>
      </ul>
</template>
<script>
import '@/assets/styles/iconfontSelect.css' // iconfontSelect.css
export default {
    name: "iconfont",
    data() {
      return {
        //图标标题
        //iconSelectTitle:'选择按钮图标'
      };
    },
     methods: {
      /** 选择按钮图标 */
      getSelectClass(res){
        // 将子组件与父组件联合起来
        this.$emit('father-click',res.target.className.substring(5));
      },
      //关闭弹框的事件
      closeDialog(){

      }
    }
  };
</script>
<style scoped>
  ul.dib-box{
    padding: 0;
  }
  .icon_lists .iconfont{
    font-size: 30px !important;
  }
  .dib{
    width: 90px;
    display: inline-block;
    vertical-align: top;
    margin: 0 5px;
  }
  .el-scrollbar__wrap {
    overflow-x: hidden !important;
  }
</style>
