/**
* @description: 根据容器宽度判断是否显示tooltip
* @param {String} text:需要显示tooltip的文本 
* @param {Number} largestWidth: 宽度临界值  
* @return {Boolean} 
*/
export function isShowTooltip (text, largestWidth) {
    // 创建一个动态span计算长度
    let span = document.createElement('span');
    span.style.display = 'inline-block';
    span.style.visibility = 'hidden';
    span.textContent = text;
    document.body.appendChild(span);
    const length = window.getComputedStyle(span).width.split('px')[0] * 1;
    document.body.removeChild(span);
    const width = Number(largestWidth);
    return length > width ? false : true;
}

/**
 * @desc  函数防抖---“立即执行版本” 和 “非立即执行版本” 的组合版本
 * @param  func 需要执行的函数
 * @param  wait 延迟执行时间（毫秒）
 * @param  immediate---true 表立即执行，false 表非立即执行
 **/
export function debounce (func, wait, immediate) {
    let timer;

    return function () {
        let context = this;
        let args = arguments;

        if (timer) clearTimeout(timer);
        if (immediate) {
            var callNow = !timer;
            timer = setTimeout(() => {
                timer = null;
            }, wait)
            if (callNow) func.apply(context, args)
        } else {
            timer = setTimeout(function () {
                func.apply(context, args)
            }, wait);
        }
    }
}

/**
 * @param { Promise } promise
 * @param { Object } errorExt - Additional Information you can pass to the err object
 * @return { Promise }
 */
export function awaitPromise (promise, errorExt = {}) {
    return promise
        .then(data => [null, data])
        .catch(err => [Object.assign(err, errorExt), undefined]);
}

/**
 *  @param {promise} promise 请求/promise异步处理/普通函数
 *  @param {function} callback 结果回调
 */
export function getResponse (promise, callback) {
    if (promise instanceof Promise) {
        return promise.then(data => callback(null, data)).catch(error => callback(error, null))
    } else {
        try {
            const res = promise();
            return callback(null, res);
        } catch (error) {
            return callback(error, null);
        }
    }
}