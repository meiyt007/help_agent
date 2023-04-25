//操作类型--状态
function auditStatus () {
  return {
    '0': '新建', '1': '待审核', '2': '审核未通过', '3': '发布', '4': '暂停', '5': '取消'
  };
}

auditStatus.prototype.getOperateType = function (type) {
  let operateTypeStr = '';
  if (type == '0') {
    operateTypeStr = '新建';
  } else if (type == '1') {
    operateTypeStr = '变更';
  } else if (type == '2') {
    operateTypeStr = '暂停';
  } else if (type == '3') {
    operateTypeStr = '取消';
  }
  return operateTypeStr;
}

export default auditStatus
