import request from '@/utils/request';

// 获取电子证照预览或者下载地址详情
export function getElecLicenUrl (elecLicenOid) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/elemLice/downloadLicenseByElecLicenOid?elecLicenOid=' + elecLicenOid,
    method: 'post',
  })
}

// 获取电子证照信息
export function queryElecLicenseByDirCode (materialOid, userName, idCard, billOid, caseOid) {
  return request({
    url: process.env.VUE_APP_DSXBL_ROUTE_PATH + '/elemLice/queryElecLicenseByDirCode?materialOid=' + materialOid + "&userName=" + userName + "&idCard=" + idCard + "&billOid=" + billOid+ "&caseOid=" + caseOid,
    method: 'post',
  })
}

/** 通过证照目录获取个人证号还是法人证照
 * @param {string} materialOid
 * @param {string} billOid
 */
export function queryElecLicenseType (materialOid, billOid) {
  return request.post(process.env.VUE_APP_DSXBL_ROUTE_PATH + `/elemLice/queryElecLicenseType?materialOid=${materialOid}&billOid=${billOid}`)
}
