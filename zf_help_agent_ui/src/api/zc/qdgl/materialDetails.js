import request from '@/utils/request';
import axios from "axios";
//查询列表
export function page(query) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/serviceMaterial/getSxServiceMaterialPageListByServiceOid',
    method: 'get',
    params: query
  })
}

// 附件上传
export function uploadsxFile(data) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/sxSys/atta/uploadFile',
    method: 'POST',
    headers:{
      "Content-Type":"multipart/form-data",
    },
    data:data
  })
}

// 添加事项材料
export function insertSxServiceMaterialService(data) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/serviceMaterial/insertSxServiceMaterialService',
    method: 'POST',
    data:data
  })
}

// 根据materialOid 获取事项材料
export function getSxServiceMaterialByOid(query) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/serviceMaterial/getSxServiceMaterialHasFileByOid/'+query,
    method: 'get'
  })
}

// 根据materialOid 删除事项材料
export function delMaterialRow(query) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/serviceMaterial/delSxServiceMaterialByOid/'+query,
    method: 'get'
  })
}

// 通用下载方法
export function downloadMaterial(attaOid) {
  //VUE_APP_SX_API_PAGE
  //window.location.href = process.env.VUE_APP_SX_ROUTE_PATH + "/sxSys/atta/download/?attaOid=" +attaOid ;
  //window.location.href = process.env.VUE_APP_SX_ROUTE_PATH  + "/sxSys/atta/download/" +attaOid + '?access_token=' +  getToken();
  //downloadZipHandle(attaOid);
  window.location.href = process.env.VUE_APP_SERVICE_API + "/sxSys/atta/downloadFile?attaOid=" +attaOid ;
}
export function downloadZipHandle(down){
  axios({
    method: "GET",
    url: process.env.VUE_APP_SX_ROUTE_PATH + "/sxSys/atta/download/" ,
    params: {
      attaOid: down,
    },
    responseType: "blob",
  })
    .then((res) => {
      if (res.status == 200) {
        console.log(res);
        const contentDisposition  = res.headers["content-disposition"];
        // 运用window.decodeURI来解码，解决中文乱码问题
        let fileName = window.decodeURI(contentDisposition.substring(contentDisposition.indexOf('=')+1))
        // fileDownload(res.data,fileName)
        let blob = new Blob([res.data]);
        let url = window.URL.createObjectURL(blob);
        if (window.navigator.msSaveOrOpenBlob) {
          //msSaveOrOpenBlob方法返回bool值
          navigator.msSaveBlob(blob, fileName); //本地保存
        } else {
          let link = document.createElement("a"); //a标签下载
          link.href = url;
          link.download = fileName;
          document.body.appendChild(link)
          link.click();
          window.URL.revokeObjectURL(url);
        }
      }
    })
    .catch((err) => {
      console.log(err);
    });

  // window.location.href="http://172.168.252.142:9092/pic/download?zipRecId="+zipRecId;
  // downloadZip({ zipRecId }).then((res) => {
  //   console.log(res)
  //   const fileName = 'qqq';
  //   fileDownload(res,`测试文件_${new Date().toLocaleString()}.zip`)
  // });
}


// 根据业务主键获取单个附件信息
export function getSxSysAttaByOId(oid) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/sxSys/atta/getOne/'+oid,
    method: 'get'
  })

}

export function getMaterialPdf(fileOid) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH +'/sxSys/file/downloadFile/'+fileOid,
    method: 'get',
    headers: {
      'Content-Type': 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
    },
    responseType: 'blob'  //设置响应的数据类型为一个包含二进制数据的 Blob 对象，必须设置！！！
  })
}

// 保存事项材料与表单模板关系
export function saveMaterialFormTemplate (data) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/serviceMaterial/saveMaterialFormTemplate',
    method: 'post',
    data: data
  })
}

//
export function getMaterialFormTemplate(sxServiceOid, materialOid) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/affair/serviceMaterial/getMaterialFormTemplate?sxServiceOid=' + sxServiceOid + "&materialOid=" + materialOid,
    method: 'get'
  })
}
//获取签名配置列表
export function getSignList(materialOid){
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH + '/sxSign/getList',
    method: 'get',
    params: {
      materialOid: materialOid,
    },
  })
}

//新增或修改签名配置
export function saveSign(data){
  return request({
    url:process.env.VUE_APP_SX_ROUTE_PATH+'/sxSign/saveOrUpdate',
    method: 'post',
    data: data
  })
}

//逻辑删除签名配置
export function delSign(id) {
  return request({
    url: process.env.VUE_APP_SX_ROUTE_PATH +'/sxSign/delete',
    method: 'post',
    params: {
      id: id
    }
  })
}




