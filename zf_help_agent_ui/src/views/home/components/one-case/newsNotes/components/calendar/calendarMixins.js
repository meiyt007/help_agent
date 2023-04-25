import { getAllNotesByDate } from '@/api/home/calendar.js';
const date = new Date(),
    today = `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;
export default {
    mounted () {
        this.getDictList('ZSLB').then(({ data, code }) => {
            if (code !== 200) return this.zslbDict = [];
            this.zslbDict = data;
        })
    },
    computed: {
        /** 
         * 获取当前年-月
         */
        currentYearAndMonth () {
            return String(this.year) + '-' + String(this.month + 1).padStart(2, '0');
        },
    },
    methods: {
        // 处理存在的记事
        async setDefaultNotesDays (isClose) {
            const data = await getAllNotesByDate(this.currentYearAndMonth);
            if (!Array.isArray(data) && data.length === 0) return;
            this.days?.forEach?.(day => {
                day.forEach(item => {
                    isClose && (item.popover = false);
                    const target = this.checkIfHasNotes(data, item.day);
                    item.hasNotes = !!target;
                    if (item.hasNotes) {
                        item.notes = target.list;
                    }
                })
            });
            this.$forceUpdate();
        },

        /** 
         * 判断是否存在
         */
        checkIfHasNotes (data, day) {
            const targetDay = `${this.year}-${String(this.month + 1).padStart(2, 0)}-${String(day).padStart(2, '0')}`;
            return data.find(item => item.date === targetDay);
        },

        /** 
         * 点击关闭 popover
         */
        handleClose (child) {
            this.$nextTick(() => {
                child.popover = false;
                child.selected = false;
            })
        },

        /**
         * 点击隐藏
          */
        handleHide (child) {
            this.$nextTick(() => {
                child.selected = false;
            })
        },

        handleAddCalendar (type, data) {
            // this.$emit(type);
            this.visible = true;
            this.type = type;
            this.addForm = data || {
                zslbDictOid: this.zslbDict?.[0]?.dictOid,
                knowledgeTitle: '',
                knowledgeContent: '',
                note: '',
            };
        },

        /** 
         * 是否是今天
         */
        isToday (day) {
            return `${this.currentYearAndMonth}-${String(day).padStart(2, '0')}` === today;
        }
    }
}