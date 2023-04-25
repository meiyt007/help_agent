

export {
    getQueryVariable,       // 从url中获取对应的参数
    isExistQueryVariable    // 判断url路径中是否携带参数
};

/**
 * 从url中获取对应的参数
 * @param {想要换取的参数} variable 
 * @returns 
 */
function getQueryVariable(variable) {
    var query = window.location.search.substring(1);
    var vars = query.split("&");
    for (var i=0;i<vars.length;i++) {
            var pair = vars[i].split("=");
            if(pair[0] == variable){return pair[1];}
    }
    return(false);
}

/**
 * 判断url路径中是否携带参数
 * @param {url} url 
 * @returns 
 */
function isExistQueryVariable (url) {
    return (url.indexOf('?') > -1);
}