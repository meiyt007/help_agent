/**
 * @param {number} length
 * @returns {String}
 */
export function RandomStr(length) {
    let len = length || 32;
    const chars = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678';
    const charsLength = chars.length;
    let str = ''
    for (let i = 0; i < len; i++) {
        let index = Math.floor(Math.random() * charsLength);
        str += chars[index]
    }
    return str
}

/**
 * 格式化文件大小
 * @param {String} value
 * @returns {String} 
 */

export function renderSize(value) {
    if (null == value || value == '') {
        return "0 Bytes";
    }
    const unitArr = new Array("Bytes", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB");
    let index = 0;
    const srcsize = parseFloat(value);
    index = Math.floor(Math.log(srcsize) / Math.log(1024));
    let size = srcsize / Math.pow(1024, index);
    size = size.toFixed(2);//保留的小数位数
    return size + unitArr[index];
}

/**
 * @param {Arr} files
 * @returns {Arr}
 */

export function getfiles(arr) {
    let new_arr = [];
    arr.forEach(item => {
        new_arr.push({
            id: item.id,
            oid: "",
            filename: item.name,
            size: item.size,
            sizeStr: renderSize(item.size),
            percent: item.percent,
            status: item.status,
            type: item.type
        })
    });

    return new_arr
}

/**
 * 根据id获取数组index
 * @param {String} id
 * @param {Array} arr
 * @returns {number} 
 */
export function getFileIndex(arr,id) {
    for (let index = 0; index < arr.length; index++) {
        if(arr[index].id == id) {
            return index
        }
    }
    return -1
}