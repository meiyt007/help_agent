
export const handleSignleParams = (list) => {
    const mainTable = list[0];
    return handleTable(mainTable, 'single');
};

export const handleMasterSalveParams = (list) => {
    let result = {};
    const mainTable = list.filter(item => item.isFormMain === '1')[0];

    result = handleTable(mainTable, 'masterSalve');

    const others = list.filter(item => item.isFormMain === '0');

    let children = [];

    others.forEach((item,) => {
        children.push(handleTable(item, 'masterSalve'));
    });

    result.children = children;

    return result;
};

export const handleSubFormParams = (list) => {
    return handleMasterSalveParams(list, 'subForm');
};

const handleColumnList = (list, datasourceOid) => {
    return list.map(item => {
        return {
            datasourceOid,
            maxLenth: item.columnLenght,
            columnType: item.columnType,
            dataType: item.dataType,
            columnName: item.fieldCode,
            demo: item.fieldName,
            // notNull: '0',
        }
    })
};

const handleTable = (table, formType) => {
    let result = {};
    const { instance, } = table;
    if (instance) {
        const { objectCode, isFormMain, id, fieldTypeOid } = table;
        const { $refs, isLogicFormMain, isSingle, dataForm: { objectName, moduleOid, objectForm, objectOid, datasourceOid }, authorizeKey, formCode } = instance || {};

        let _columnList = isLogicFormMain ? [] : handleColumnList($refs?.columnChild?.form?.columnList ?? [], datasourceOid);

        let isExitObjectExtand = isFormMain === '0' && !isLogicFormMain && !isSingle;

        if (isExitObjectExtand) {
            const { formConfigRelation: { ruleForm } } = $refs;
            result.formObjectExtand = { ...ruleForm };
        }

        result = {
            ...result,
            id,
            relationObjectBusinessId: fieldTypeOid,
            objectOid, // 存储对象Oid
            authorizeKey,
            datasourceOid, // 数据源Oid
            datasourceName: '',
            moduleOid, // 模块Oid
            objectName, // 存储对象名称
            objectCode, // 存储对象编码
            objectForm, // 对象表名
            saveType: isLogicFormMain ? '1' : '0', // { '0': '存储对象', '1': '逻辑对象' } 单表和主从 0 子列表传 1
            isSave: isLogicFormMain ? '0' : '1', // 主表是否填报保存 (逻辑主表是0)
            formCode, // 表单编码
            columnList: _columnList,
            idIsVarchar: isSingle || (formType === 'masterSalve' && isFormMain === '1') ? '1' : '', // 主键是否varchar类型  ——目前只针对综窗的mysql  1是  其他不是
        };

        return result;
    } else {
        const { id, columnList, isFormMain, formObjectExtand, dataForm, authorizeKey, formCode, objectCode, isSave, idIsVarchar, fieldTypeOid } = table;
        const { objectName, moduleOid, objectForm, objectOid, datasourceOid } = dataForm;

        if (isFormMain !== '1') {
            result.formObjectExtand = { ...formObjectExtand };
        }

        result = {
            ...result,
            id,
            relationObjectBusinessId: fieldTypeOid,
            objectOid, // 存储对象Oid
            authorizeKey,
            datasourceOid, // 数据源Oid
            datasourceName: '',
            moduleOid, // 模块Oid
            objectName, // 存储对象名称
            objectCode, // 存储对象编码
            objectForm, // 对象表名
            isSave, // 主表是否填报保存 (逻辑主表是0)
            formCode, // 表单编码
            columnList,
            idIsVarchar
        };

        return result;
    }
}

// 判断columnList的length是否为0
export const handleColumnListLengthIsZero = (obj, formType) => {
    if (formType === 'single') {
        if (obj.columnList.length === 0) {
            return { objectName: obj.objectName };
        }
        return undefined;
    }

    if (formType === 'masterSlave') {
        let result = obj.columnList.length === 0 ? { objectName: obj.objectName } : obj.children.find(item => item.columnList.length === 0);
        return result;
    }

    if (formType === 'subForm') {
        return obj.children.find(item => item.columnList.length === 0);
    }
}

// 合并columnList
export const mergeColumnList = (list) => {
    return list.reduce((prev, cur, idx) => {
        const { instance, columnList, } = cur;
        if (idx === 0) {
            prev = prev.concat(instance.$refs?.columnChild?.form?.columnList ?? []);
        } else {
            if (instance) {
                prev = prev.concat(instance.$refs?.columnChild?.form?.columnList ?? []);
            } else {
                prev = prev.concat(columnList);
            }
        }
        return prev;
    }, []);
}