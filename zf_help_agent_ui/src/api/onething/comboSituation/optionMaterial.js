import request from '@/utils/request'


/*
 * @Description:查询情形配置-配置选项-关联材料信息列表
 **/
export function page(query) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/optionMaterial/page',
    method: 'get',
    params: query
  })
}

/*
 * @Description:获取单个情形配置-配置选项-关联材料信息
 **/
export function getOne(oid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/optionMaterial/getOne/' + oid,
    method: 'get'
  })
}

/*
 * @Description:根据主题OID与选项值OID获取配置对象
 **/
export function getOptionMaterialByDirOidValOid(comboDireOid,valOid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/optionMaterial/getOptionMaterialByDirOidValOid?comboDirectoryOid='+comboDireOid+'&valOid='+valOid,
    method: 'get'
  })
}

/*
 * @Description:查询一件事目录关联的事项材料
 **/
export function querySxServiceMaterList(comboDireOid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/directoryMaterial/querySxServiceMaterList/'+comboDireOid,
    method: 'get'
  })
}
/*
 * @Description:保存材料配置
 **/
export function saveOptionMaterial(data){
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/optionMaterial/saveOptionMaterialMaterialRel',
    method: 'post',
    headers:{
      "Content-Type":"application/json;charset=UTF-8",
    },
    data:data
  })
}

/*
 * @Description:情形材料配置分页
 **/
export function optionMaterialVoPage(query) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH + '/combo/optionMaterial/optionMaterialVoPage',
    method: 'get',
    params: query
  })
}

/*
 * @Description:初始化情形材料配置
 **/
export function initAttaRel(comboDireOid, relOid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/optionMaterial/initComboOptionMaterial?comboDirectoryOid='+comboDireOid+'&relOid='+relOid,
    method: 'get'
  })
}


/*
 * @Description:查询所有的材料
 **/
export function queryAllMarerialList(dirOid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/directoryMaterial/queryAllMaterialListByDirOid/'+dirOid,
    method: 'get'
  })
}

//删除选项关系
export function delMaterialRel(oid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/optionMaterial/delMaterialRel/' + oid,
    method: 'delete'
  })
}


export function getOptionMaterialByValOid(valOid) {
  return request({
    url: process.env.VUE_APP_THING_ROUTE_PATH +'/combo/optionMaterial/getOptionMaterialByValOid?valOid='+valOid,
    method: 'get'
  })
}
