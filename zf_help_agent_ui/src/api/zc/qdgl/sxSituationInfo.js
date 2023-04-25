import request from '@/utils/request';
// 添加事项材料
export function insertSxSituationInfo(data) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/sxServiceSituation/saveSxServiceSituation',
    method: 'POST',
    data:data
  })
}

export function getSxServiceSituationByOid(query) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/sxServiceSituation/getSxServiceSituationByOid/'+query,
    method: 'get'
  })
}

//删除
export function delSxServiceSituationByOid(id) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/sxServiceSituation/delSxServiceSituationByOid/'+id,
    method: 'get'
  })
}

// 3、默认自定义情形查询当前事项下标题没有跟任何其他选项值关联的数据
export const getSxServiceTitlesNoRelation = (params) => {
  return request.get(process.env.VUE_APP_SX_ROUTE_PATH + '/affair/sxService/getSxServiceTitlesNoRelation', { params })
}

export function querySxServiceSituationRelationBySituationAndTitle(query) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/sxService/querySxServiceSituationRelationBySituationAndTitle',
    method: 'get',
    params: query
  })
}

//保存情形关联选项关系
export function saveSxServiceSituations(data) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/sxServiceSituationOption/saveSxServiceSituations',
    method: 'POST',
    data:data
  })
}

export function getSituationMaterialListByOids(serviceOid,optionValOids) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/serviceMaterial/getSituationMaterialListByOids?serviceOid='+serviceOid+"&optionValOids="+optionValOids,
    method: 'get',

  })
}

// 4、默认自定义情形查询点击标题下选项值，查询出相关联的标题信息
export const getSxServiceTitlesByRelationOids = (params) => {
  return request.get(process.env.VUE_APP_SX_ROUTE_PATH + '/affair/sxService/querySxServiceSituationRelation', { params })
}


/**
 * 获取到所有的选中项oids List
 * @param {array} list 情形选项列表
 */
export const getAllSelectedOidsList = (list) => {
  //return list.map(item => item.isSelected).filter(item => item || item.length > 0);
  //return list.map(item =>  item.isSelecteds !==undefined &&item.isSelecteds != null && item.isSelecteds.length > 0).filter(item => item || item.length > 0);
  var selectedIds = [];
  for (let i = 0; i < list.length; i++) {
      var item =  list[i];
      console.log(item.isSelecteds);
      if(typeof item.isSelecteds == "string" && item.isSelecteds!=null&&item.isSelecteds.length>0){
        selectedIds.push(item.isSelecteds);
      }else if(typeof item.isSelecteds == "object"){
        item.isSelecteds.forEach(element => {
          selectedIds.push(element);
      })
    }
  }
  return  selectedIds;
}

/**
 * 获取到所有的选中项oid
 * @param {array} list 情形选项列表
 */
export const getAllSelectedOids = (list) => {
  return getAllSelectedOidsList(list).join(',');
}

export function unique(arr) {
  const res = new Map();
  return arr.filter((arr) => !res.has(arr.oid) && res.set(arr.oid, 1))
}
