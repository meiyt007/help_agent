import request from '@/utils/request'

// 查询一件事主题信息列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/serviceManage/page',
    method: 'get',
    params: query
  })
}
export function pageList(query) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/serviceManage/page',
    method: 'get',
    params: query
  })
}
//配置选项
export function getOne(oid) {
  if(oid == undefined){
    oid ='';
  }
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/serviceManage/getComboServiceManageByManageOid/' + oid+'?oid='+oid,
    method: 'get'
  })
}

// 一件事选项的保存
export function save(data) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/serviceManage/saveComboServiceManage',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data: data
  })
}

//删除一件事选项管理
export function del(oid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/serviceManage/delComboServiceManage/' + oid+'?oid='+oid,
    method: 'delete'
  })
}

// 获取一条平板信息详情
export function getOneFile(attaOid) {
  return request({
    url: process.env.VUE_APP_ZC_ROUTE_PATH + '/manage/information/getOneFile/'+attaOid+'?attaOid=' + attaOid,
    method: 'get',
  })
}
