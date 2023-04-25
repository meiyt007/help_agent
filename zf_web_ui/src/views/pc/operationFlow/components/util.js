/**
 * 处理热门情形选择 情形选择数据
 * @param {array} list 情形选项列表
 * @param {boolean} isHot 是否是热门情形
 */
export const formatSituationCheckList = (list, isHot = false) => {
  return list.map((item) => {
    // 筛选出选中的
    const isSelectedList = item.sxServiceOptionVals.filter(
      (i) => i.isSelected == "1"
    );
    // 是单选就字符串 多选就数组
    let isSelected = item.moreStatus === 0 ? "" : [];
    if (isSelectedList.length > 0) {
      if (isSelectedList.length >= 1 && item.moreStatus === 1) {
        isSelected = isSelectedList.map((i) => i.oid);
      } else {
        isSelected = isSelectedList[0].oid;
      }
    }
    return {
      fillFlag: item.fillFlag, // 是否是必选
      id: item.id,
      moreStatus: item.moreStatus, // 是否是单选 0 1 多选
      oid: item.oid,
      serviceOid: item.serviceOid,
      sort: item.sort,
      name: item.name,
      isSelected: isSelected, // string | array
      answers:
        item.sxServiceOptionVals?.map?.((child) => {
          return {
            id: child.id,
            isSelected: child.isSelected == "1", // boolean
            name: child.name,
            oid: child.oid,
            titleOid: child.titleOid,
            sort: child.sort,
            // isDisabled: isHot, // fix: 3202 【智能问答情形选择】配置的情形，建议选项不要置灰，可灵活调整
            defaultFlag: child.defaultFlag,
          };
        }) || [],
    };
  });
};

/**
 * 处理defaultFlag问题
 */
export const handleDefaultFlag = (list) => {
  return list.reduce((prev, cur) => {
    // 是否含有defaultFlag = 1 并且未被选中的
    const isFlag = cur.answers.some(
      (item) =>
        item.defaultFlag === 1 &&
        (!cur.isSelected || cur.isSelected.length === 0)
    );

    if (isFlag) {
      prev.push({
        ...cur,
        answers: cur.answers.filter(
          (item) =>
            item.defaultFlag === 1 &&
            (!cur.isSelected || cur.isSelected.length === 0)
        ),
      });
    }

    return prev;
  }, []);
};

/**
 * 去重
 * @param {array} list 情形选项列表
 */
export const removeDuplicate = (list) => {
  return list.reduce((prev, cur) => {
    const hasExit = prev.find((item) => item.oid === cur.oid);
    // 如果之前存在并且当前的已经被选中
    if (!!hasExit && !!cur.isSelected) {
      prev = prev.filter((item) => item.oid !== hasExit.oid);
      // prev.splice(prev.findIndex(item => item.oid === cur.oid), 1);
    }

    // 如果之前存在 但是当前的未被选中
    if (!!hasExit && !cur.isSelected) {
      return prev;
    }

    prev.push(cur);
    return prev;
  }, []);
};

/**
 * 获取到所有的选中项oids List
 * @param {array} list 情形选项列表
 */
export const getAllSelectedOidsList = (list) => {
  return list
    .map((item) => item.isSelected)
    .filter((item) => item)
    .filter((item) => {
      if (typeof item === "string") {
        return item;
      }

      if (Array.isArray(item) && item.length > 0) {
        return item;
      }
    });
};

/**
 * 获取到所有的选中项oid
 * @param {array} list 情形选项列表
 */
export const getAllSelectedOids = (list) => {
  return getAllSelectedOidsList(list).flat().join(",");
};

/**
 * 判断所有选项是否必选
 * @param {array} list 情形选项列表
 * @returns {object} 必选项
 */
export const checkAllSituationsSelected = (list) => {
  return (
    list
      .filter((item) => item.fillFlag === 1)
      .filter(
        (item) => !item.isSelected || item.isSelected.length === 0
      )?.[0] ?? {}
  );
};

/**
 * 筛选办件与情形标题选项关系
 * @param {array} list 情形选项列表
 */
export const getQlTitleValList = (list) => {
  return list.reduce((prev, cur) => {
    // 单选
    if (cur.moreStatus === 0 && !!cur.isSelected) {
      prev.push({ titleOid: cur.oid, valueOid: cur.isSelected });
    }

    // 多选
    if (cur.moreStatus === 1 && cur.isSelected.length > 0) {
      prev = prev.concat(
        cur.isSelected.map((item) => ({ titleOid: cur.oid, valueOid: item }))
      );
    }
    return prev;
  }, []);
};
