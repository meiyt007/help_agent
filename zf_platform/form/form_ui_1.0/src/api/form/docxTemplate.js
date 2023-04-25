import request from '@/utils/request'

var applicationName = process.env.VUE_APP_FORM_API_ROUTE_PATH + '/manager/security/docx/template';

// 查询对象信息列表
export function page(query) {
  return request({
    url: applicationName + '/page',
    method: 'get',
    params: query
  })
}

// 对象的新增或者修改
export function save(data) {
  return request({
    url: applicationName + '/saveDocxTemplate',
    method: 'post',
    data: data
  })
}

//删除对象管理
export function del(id) {
  return request({
    url: applicationName + '/delete/' + id,
    method: 'post'
  })
}

// 启禁用对象信息
export function able(id) {
  return request({
    url: applicationName + '/able/'+id,
    method: 'post'
  })
}

// 获取单个对象信息
export function getOne(id) {
  return request({
    url: applicationName + '/getOne/'+id,
    method: 'get'
  })
}
// 获取单个对象信息
export function getDocxTemplateByTemplateOid(oid) {
  return request({
    url: applicationName + '/getDocxTemplateByTemplateOid/'+oid,
    method: 'get'
  })
}

/**
 * @description:  根据授权key查询模板列表（以下参数三选一）
 * @param designOid 设计oid
 * @param objectOid 存储对象oid
 * @param formCode 表单编码
 * @author: wuxx
 * @Date: 2021/12/2 15:14
 **/
//对象列表
export function querylist(query) {
  return request({
    url: applicationName + '/queryDocxTemplateList',
    method: 'get',
    params: query
  })
}

/**
 * @description: 填报数据导出docx或者pdf
 * @param designAndReportOid 设计主键-填报主键
 * @param linkDesignAndReportOids 关联设计主键-填报主键，多个用逗号隔开
 * @param docxTemplateOid 模板主键
 * @param isPdf 是否导出pdf
 * @author: wuxx
 * @Date: 2021/12/3 13:42
 **/
export function exportFormDataToDocx(query) {
  window.location.href = process.env.VUE_APP_BASE_API + "/form/manager/exportFormDataToDocx?designAndReportOid="+query.designAndReportOid+
  "&docxTemplateOid="+query.docxTemplateOid+"&linkDesignAndReportOids="+query.linkDesignAndReportOids+"&isPdf="+query.isPdf;
}


/**
 * @description: 填报数据导出docx或者pdf
 * @param designAndReportOid 设计主键-填报主键
 * @param linkDesignAndReportOids 关联设计主键-填报主键，多个用逗号隔开
 * @param docxTemplateOid 模板主键
 * @param isPdf 是否导出pdf
 * @param initDataMap 模板设置默认值，优先设置
 * @author: wuxx
 * @Date: 2021/12/3 13:42
 **/
export function exportFormDataToDocxPost(query) {
  let data = {
    designAndReportOid:query.designAndReportOid,
    linkDesignAndReportOids:query.linkDesignAndReportOids,
    docxTemplateOid:query.docxTemplateOid,
    isPdf:query.isPdf,
    initDataMap:{
      title:'优先设置的标题'
    }
  }
  let url = process.env.VUE_APP_BASE_API + process.env.VUE_APP_FORM_API_ROUTE_PATH + '/manager/exportFormDataToDocxPost';
  downLoadFile(url,data);
}

//下载填报的docx或pdf
function downLoadFile (url, params, fileName) {
  const xhr = new XMLHttpRequest()
  xhr.open('post', url)
  xhr.setRequestHeader('Content-Type', 'application/json')
  xhr.responseType = 'blob'
  xhr.send(JSON.stringify(params))
  xhr.onreadystatechange = () => {
    if (xhr.readyState === 4 && xhr.status === 200) {
      try {
        if(!fileName){
          let decodeFileName = decodeURIComponent(xhr.getResponseHeader('content-disposition').split("=")[1]);
          fileName = decodeFileName;
        }
      }catch (e) {
        if(params.isPdf){
          fileName = '填报数据.pdf';
        }else {
          fileName = '填报数据.docx';
        }
      }
      // 兼容IE，只有IE浏览器的navigator有这个方法，谷歌浏览器没有
      if (navigator.msSaveBlob) {
        return navigator.msSaveBlob(xhr.response, fileName)
      }
      const blobUrl = URL.createObjectURL(xhr.response)
      const link = document.createElement('a')
      link.href = blobUrl
      link.download = fileName
      link.click();
    }
  }
}


//获取关联得存储对象
export function getAppendObjects(authorizeKey,moduleOid) {
  let query={
    authorizeKey:authorizeKey,
    moduleOid:moduleOid
  }
  return request({
    url:  process.env.VUE_APP_FORM_API_ROUTE_PATH  + '/manager/security/object/queryDataSourceFormObjectList',
    method: 'get',
    params: query
  })
}

//获取关联得存储对象
export function getObjectColumns(objectOid) {
  return request({
    url:  process.env.VUE_APP_FORM_API_ROUTE_PATH  + '/manager/security/column/queryFormColumnStrByObjectOid?objectOid='+objectOid,
    method: 'get',
  })
}
