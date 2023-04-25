/** 数组非空转字符串 */
export const handleArr2String = (arr) => {
    return arr?.map?.(item => item)?.join(',') || '';
}