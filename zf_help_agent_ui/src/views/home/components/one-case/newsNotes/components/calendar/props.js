export default {
    // 多选模式
    multi: {
        type: Boolean,
        default: false,
    },
    // 范围模式
    range: {
        type: Boolean,
        default: false,
    },
    // 默认日期
    value: {
        type: Array,
        default: function () {
            return [];
        },
    },
    // 开始选择日期
    begin: {
        type: Array,
        default: function () {
            return [];
        },
    },
    // 结束选择日期
    end: {
        type: Array,
        default: function () {
            return [];
        },
    },
    // 是否小于10补零
    zero: {
        type: Boolean,
        default: false,
    },
    // 屏蔽的日期
    disabled: {
        type: Array,
        default: function () {
            return [];
        },
    },
    // 是否显示农历
    lunar: {
        type: Boolean,
        default: false,
    },
    // 自定义星期名称
    weeks: {
        type: Array,
        default: function () {
            return window.navigator.language.toLowerCase() == "zh-cn"
                ? ["日", "一", "二", "三", "四", "五", "六"]
                : ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];
        },
    },
    // 自定义月份
    months: {
        type: Array,
        default: function () {
            return window.navigator.language.toLowerCase() == "zh-cn"
                ? [
                    "一月",
                    "二月",
                    "三月",
                    "四月",
                    "五月",
                    "六月",
                    "七月",
                    "八月",
                    "九月",
                    "十月",
                    "十一月",
                    "十二月",
                ]
                : [
                    "January",
                    "February",
                    "March",
                    "April",
                    "May",
                    "June",
                    "July",
                    "August",
                    "September",
                    "October",
                    "November",
                    "December",
                ];
        },
    },
    // 自定义事件
    events: {
        type: Object,
        default: function () {
            return {};
        },
    },
}