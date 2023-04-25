import request from '@/utils/request';

const prefix = process.env.VUE_APP_ZC_ROUTE_PATH;

/**
 * 记事本查询接口
 * 根据某年的某一月查询当月存在记事的所有日期及对应日期的对应内容
 * @param {string} time 年份-月份
 */
export function getAllNotesByDate (params) {
    return request.post(prefix + '/notepad/getOneMouthNotepad?time=' + params);
}

/**
 * 新增/修改某天的记事
 */
export function saveNotesByDate (params) {
    return request.post(prefix + '/notepad/dateTosaveNotepad', params);
}

/**
 * 根据主键删除记事本信息
 */
export function deleteNotesById (params) {
    return request.post(prefix + '/notepad/deleteNotepadById?id=' + params);
}

/**
 * 分享某条记事
 */
export function shareNotesById (params) {
    return request.post(prefix + '/notepad/shareFlagNotepadById?id=' + params);
}