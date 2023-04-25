/**
 * 验证合法字符--并且不能有数字开头
 * @param{string} str
 * @returns {Boolean}
 */
export function validateLegalStrNoNumber (rule, value, callback) {
    let reg = /^[^0-9][A-Za-z0-9_]+$/
    if (value !== '') {
        if (!reg.test(value)) {
            callback(new Error('请输入由字母、数字或下划线组成的非数字开头字符'))
        } else {
            callback()
        }
    } else {
        callback(new Error('请输入由字母、数字或下划线组成的非数字开头字符'))
    }
}

export function validataVariableName (rule, value, callback) {
    if (value != '') {
        let index = -1;
        if (this.soucrce == 0) {
            index = this.childDataIndex;
        } else {
            index = this.childIndex;
        }
        let list = this.form.objectExtandList.slice();
        if (index != -1) {
            list.splice(index, 1);
        }
        let nameList = list.filter(d => d.variableName == value);
        let columnList = this.form.columnList.filter(d => d.objectProperty == value);
        if (nameList.length > 0) {
            callback(new Error('当前对象名称已存在，请重新输入'));
        } else if (columnList.length > 0) {
            callback(new Error('当前对象名称已存在，请重新输入'));
        } else {
            callback();
        }
    } else {
        callback();
    }
};

export function validateRepeat (rule, value, callback) {
    if (value != '') {
        let columnList = this.form.columnList.filter(d => d.objectProperty == value);

        if (columnList.length > 1) {
            callback(new Error('当前对象属性已存在，请重新输入'));
        } else {
            callback();
        }
    } else {
        callback();
    }
};