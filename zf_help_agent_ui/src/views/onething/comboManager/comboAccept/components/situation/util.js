/** 获取当前组件层级 */
export const getCurrentComponentsIndex = (instance) => {
    let index = 0;
    let parent = instance.$parent;
    while (parent) {
        if (parent.$options.name === 'Situation') {
            break;
        }

        if (parent.$options.name === 'SituationItem') {
            index++;
        }

        parent = parent.$parent;
    }

    return index;
}

export const isObject = (val) => {
    return Object.prototype.toString.call(val) === '[object Object]';
}