import request from '@/utils/request';

// 获取电子证照信息并更新获取到的数据到材料
export function queryindustryElecLicenseByDirCode (materialOid, userName, idCard, billOid, caseOid) {
  return request({
    url: process.env.VUE_APP_ZC_INDUSTRY_ROUTE_PATH + '/industryClassify/queryElecLicenseByDirCode?materialOid=' + materialOid + "&userName=" + userName + "&idCard=" + idCard + "&billOid=" + billOid+ "&caseOid=" + caseOid,
    method: 'post',
  })
}
