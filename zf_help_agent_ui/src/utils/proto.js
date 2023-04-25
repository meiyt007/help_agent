/**
 * functions set on vue prototype
 * @Date 2021/8/9
 * @by shmao
 *  */
import Vue from 'vue';
import { getResponse } from './utils';
/**
 * 模板字符串 解析 可选链
 * @param {object|array} obj
 * @param {string} str like 'object.a.b' 'arr[0].a'
 * @param {any} def 默认值
 */
const handleOptionalChaining = (obj, str, def) => {
    const type = handleValueType(obj);
    if (type !== 'object' && type !== 'array') {
        throw new Error('[handleOptionalChaining error] => The first argument is not object or array');
    }
    if (str === '' || !str) {
        console.warn(
            '[handleOptionalChaining warning] => The second argument is invalid, try to give a valid chain call string of objects'
        );
        return '';
    }
    const arr =
        type === 'object'
            ? str.split('.').slice(1)
            : str
                .replace(/\[/g, '.')
                .replace(/\]/g, '.')
                .split('.')
                .filter((i) => i);

    let index = arr.length;
    let result = '';
    while (index--) {
        result = result ? result?.[arr[0]] : obj?.[arr[0]];
        arr.splice(0, 1);
    }
    return def ? result ?? def : result;
};

/** 判断变量是什么类型 */
const handleValueType = (value) => {
    return Object.prototype.toString
        .call(value)
        .slice(8, -1)
        .toLocaleLowerCase();
};

const _funcs = [
    ['handleOptionalChaining', handleOptionalChaining],
    ['handleValueType', handleValueType],
    ['$getResponse', getResponse]
];
/**
 * vue prototype function utils
 */
const handleVueUtils = (funcName, func) => {
    if (funcName in Vue) return;
    Vue.prototype[funcName] = func;
};

for (const [funcName, func] of _funcs) {
    handleVueUtils(funcName, func);
}


export { handleOptionalChaining, handleValueType };
