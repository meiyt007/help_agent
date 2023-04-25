import request from '@/utils/request';

// 获取电子证照信息并更新获取到的数据到材料
export function queryElecLicenseByDirCode (materialOid, userName, idCard, billOid, caseOid) {
  return request({
    url: process.env.VUE_APP_ZC_ONETHING_ROUTE_PATH + '/comboClassify/queryElecLicenseByDirCode?materialOid=' + materialOid + "&userName=" + userName + "&idCard=" + idCard + "&billOid=" + billOid+ "&caseOid=" + caseOid,
    method: 'post',
  })
}
