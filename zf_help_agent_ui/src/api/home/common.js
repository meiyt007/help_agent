import request from '@/utils/request';

const prefix = process.env.VUE_APP_ZC_ROUTE_PATH;

/**
 * 查询平均受理时效
 */
export function getAveragePrescription () {
    return request.get(prefix + '/front/getAveragePrescription');
}

/**
 * 添加每件事办理完成时间
 */
export function addAveragePrescription (params) {
    return request.post(prefix + '/front/addAveragePrescription', params);
}

/**
 * 查询所有的荣誉
 */
export function queryHonorList () {
    return request.get(prefix + '/honor/honorList');
}

/**
 * 查询每个员工自己的荣誉
 */
export function getPersonalHonors () {
    return request.get(prefix + '/honor/getPersonalHonors');
}

/**
 * 查询满意度情况
 */
export function getSatisfaction () {
    return request.get(prefix + '/manualEvaluation/getSatisfaction');
}