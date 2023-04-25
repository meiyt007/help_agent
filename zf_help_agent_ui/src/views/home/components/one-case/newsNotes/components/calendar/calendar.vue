
<template>
  <div class="calendar">
    <div class="calendar-tools">
      <span class="calendar-prev" @click="prev">
        <svg
          width="20"
          height="20"
          viewBox="0 0 16 16"
          version="1.1"
          xmlns="http://www.w3.org/2000/svg"
          xmlns:xlink="http://www.w3.org/1999/xlink"
        >
          <g class="transform-group">
            <g transform="scale(0.015625, 0.015625)">
              <path
                d="M671.968 912c-12.288 0-24.576-4.672-33.952-14.048L286.048 545.984c-18.752-18.72-18.752-49.12 0-67.872l351.968-352c18.752-18.752 49.12-18.752 67.872 0 18.752 18.72 18.752 49.12 0 67.872l-318.016 318.048 318.016 318.016c18.752 18.752 18.752 49.12 0 67.872C696.544 907.328 684.256 912 671.968 912z"
                fill="#ffffff"
              />
            </g>
          </g>
        </svg>
      </span>
      <span class="calendar-next" @click="next">
        <svg
          width="20"
          height="20"
          viewBox="0 0 16 16"
          version="1.1"
          xmlns="http://www.w3.org/2000/svg"
          xmlns:xlink="http://www.w3.org/1999/xlink"
        >
          <g class="transform-group">
            <g transform="scale(0.015625, 0.015625)">
              <path
                d="M761.056 532.128c0.512-0.992 1.344-1.824 1.792-2.848 8.8-18.304 5.92-40.704-9.664-55.424L399.936 139.744c-19.264-18.208-49.632-17.344-67.872 1.888-18.208 19.264-17.376 49.632 1.888 67.872l316.96 299.84-315.712 304.288c-19.072 18.4-19.648 48.768-1.248 67.872 9.408 9.792 21.984 14.688 34.56 14.688 12 0 24-4.48 33.312-13.44l350.048-337.376c0.672-0.672 0.928-1.6 1.6-2.304 0.512-0.48 1.056-0.832 1.568-1.344C757.76 538.88 759.2 535.392 761.056 532.128z"
                fill="#ffffff"
              />
            </g>
          </g>
        </svg>
      </span>
      <div class="calendar-info" @click.stop="changeYear">
        <!-- {{monthString}} -->
        <div class="year">{{ year }}<span>年</span></div>
        <div class="month">
          <div class="month-inner" :style="{ top: -(this.month * 20) + 'px' }">
            <span v-for="m in months" :key="m">{{ m }}</span>
          </div>
        </div>
      </div>
    </div>
    <table cellpadding="5">
      <thead>
        <tr>
          <td v-for="week in weeks" :key="week" class="week">{{ week }}</td>
        </tr>
      </thead>
      <tbody style="position: relative">
        <tr
          v-for="(day, k1) in days"
          :key="k1"
          style="{'animation-delay',(k1*30)+'ms'}"
        >
          <td
            v-for="(child, k2) in day"
            :key="k2"
            :class="{
              selected: child.selected,
              disabled: child.disabled,
            }"
            @click="handleSelectDate(k1, k2, $event, child)"
          >
            <span
              class="date-span"
              :class="{
                red:
                  k2 == 0 ||
                  k2 == 6 ||
                  ((child.isLunarFestival || child.isGregorianFestival) &&
                    lunar),
                'date-span-content__has-notes': child.hasNotes,
              }"
            >
              <el-popover
                v-if="child.hasNotes"
                placement="left-start"
                width="710"
                trigger="click"
                :visible-arrow="false"
                popperClass="date-span-content__popover"
                v-model="child.popover"
                @hide="
                  () => {
                    handleHide(child)
                  }
                "
              >
                <CalendarNotes
                  :notes="child.notes"
                  :date="{
                    week: child.week,
                    day: child.day,
                    month: month,
                  }"
                  @close="handleClose(child)"
                  @edit="
                    (data) => {
                      handleAddCalendar('edit', data)
                    }
                  "
                  @render="setDefaultNotesDays"
                />
                <span
                  slot="reference"
                  class="date-span-content"
                  :class="{
                    'date-span-content__today': isToday(child.day),
                  }"
                >
                  {{ child.day }}
                </span>
              </el-popover>

              <span
                v-else
                class="date-span-content"
                :class="{ 'date-span-content__today': isToday(child.day) }"
              >
                {{ child.day }}
              </span>
            </span>
            <div class="text" v-if="child.eventName != undefined">
              {{ child.eventName }}
            </div>
            <div
              class="text"
              :class="{
                isLunarFestival: child.isLunarFestival,
                isGregorianFestival: child.isGregorianFestival,
              }"
              v-if="lunar"
            >
              {{ child.lunar }}
            </div>
          </td>
        </tr>
        <!-- 添加记事信息 -->
        <el-button
          class="add-date-events"
          type="primary"
          icon="el-icon-plus"
          circle
          @click="handleAddCalendar('add')"
        />
      </tbody>
    </table>

    <div class="calendar-years" :class="{ show: yearsShow }">
      <span
        v-for="y in years"
        :key="y"
        @click.stop="selectYear(y)"
        :class="{ active: y == year }"
      >
        {{ y }}
      </span>
    </div>

    <!-- 新增记事 -->
    <AddCalendar
      :visible.sync="visible"
      :type="type"
      :form="addForm"
      :zslbDict="zslbDict"
      @render="setDefaultNotesDays"
    />
  </div>
</template>

<script>
import calendar from "./calendar.js";
import Props from './props';
import { FESTIVAL, WEEKS } from './constant';

import CalendarMixins from './calendarMixins';

import CalendarNotes from './calendar-notes';
import AddCalendar from './calendar-add';
export default {
  name: 'VueCalendar',
  mixins: [CalendarMixins],
  props: Props,
  components: { CalendarNotes, AddCalendar },
  data () {
    return {
      years: [],
      yearsShow: false,
      year: 0,
      month: 0,
      day: 0,
      days: [],
      multiDays: [],
      today: [],
      festival: FESTIVAL,
      rangeBegin: [],
      rangeEnd: [],
      visible: false,
      type: 'add', /** add edit */
      addForm: {},
      zslbDict: [],
    };
  },
  watch: {
    events () {
      this.render(this.year, this.month);

    },
    value () {
      this.init();
    },
  },
  mounted () {
    this.init();
    // this.setToday();
  },
  methods: {
    init () {
      let now = new Date();
      this.year = now.getFullYear();
      this.month = now.getMonth();
      this.day = now.getDate();
      if (this.value.length > 0) {
        if (this.range) {
          //范围
          this.year = parseInt(this.value[0][0]);
          this.month = parseInt(this.value[0][1]) - 1;
          this.day = parseInt(this.value[0][2]);
          let year2 = parseInt(this.value[1][0]);
          let month2 = parseInt(this.value[1][1]) - 1;
          let day2 = parseInt(this.value[1][2]);
          this.rangeBegin = [this.year, this.month, this.day];
          this.rangeEnd = [year2, month2, day2];
        } else if (this.multi) {
          //多选
          this.multiDays = this.value;
          this.year = parseInt(this.value[0][0]);
          this.month = parseInt(this.value[0][1]) - 1;
          this.day = parseInt(this.value[0][2]);
        } else {
          //单选
          this.year = parseInt(this.value[0]);
          this.month = parseInt(this.value[1]) - 1;
          this.day = parseInt(this.value[2]);
        }
      }
      this.render(this.year, this.month);
    },
    // 渲染日期
    render (y, m) {
      let firstDayOfMonth = new Date(y, m, 1).getDay(); //当月第一天
      let lastDateOfMonth = new Date(y, m + 1, 0).getDate(); //当月最后一天
      let lastDayOfLastMonth = new Date(y, m, 0).getDate(); //最后一月的最后一天
      this.year = y;
      let seletSplit = this.value;
      let i,
        line = 0,
        temp = [],
        nextMonthPushDays = 1;
      for (i = 1; i <= lastDateOfMonth; i++) {
        let day = new Date(y, m, i).getDay(); //返回星期几（0～6）
        let k;
        // 第一行
        if (day == 0) {
          temp[line] = [];
        } else if (i == 1) {
          temp[line] = [];
          k = lastDayOfLastMonth - firstDayOfMonth + 1;
          for (let j = 0; j < firstDayOfMonth; j++) {
            // console.log("第一行",lunarYear,lunarMonth,lunarValue,lunarInfo)
            temp[line].push(
              Object.assign(
                { day: k, disabled: true },
                this.getLunarInfo(
                  this.computedPrevYear(),
                  this.computedPrevMonth(true),
                  k
                ),
                this.getEvents(
                  this.computedPrevYear(),
                  this.computedPrevMonth(true),
                  k
                )
              )
            );
            k++;
          }
        }

        if (this.range) {
          // 范围
          // console.log("日期范围",this.getLunarInfo(this.year,this.month+1,i))
          let options = Object.assign(
            { day: i },
            this.getLunarInfo(this.year, this.month + 1, i),
            this.getEvents(this.year, this.month + 1, i)
          );
          if (this.rangeBegin.length > 0) {
            let beginTime = Number(
              new Date(
                this.rangeBegin[0],
                this.rangeBegin[1],
                this.rangeBegin[2]
              )
            );
            let endTime = Number(
              new Date(this.rangeEnd[0], this.rangeEnd[1], this.rangeEnd[2])
            );
            let stepTime = Number(new Date(this.year, this.month, i));
            if (beginTime <= stepTime && endTime >= stepTime) {
              options.selected = true;
            }
          }
          if (this.begin.length > 0) {
            let beginTime = Number(
              new Date(
                parseInt(this.begin[0]),
                parseInt(this.begin[1]) - 1,
                parseInt(this.begin[2])
              )
            );
            if (beginTime > Number(new Date(this.year, this.month, i)))
              options.disabled = true;
          }
          if (this.end.length > 0) {
            let endTime = Number(
              new Date(
                parseInt(this.end[0]),
                parseInt(this.end[1]) - 1,
                parseInt(this.end[2])
              )
            );
            if (endTime < Number(new Date(this.year, this.month, i)))
              options.disabled = true;
          }
          if (this.disabled.length > 0) {
            if (
              this.disabled.filter((v) => {
                return (
                  this.year === v[0] && this.month === v[1] - 1 && i === v[2]
                );
              }).length > 0
            ) {
              options.disabled = true;
            }
          }
          temp[line].push(options);
        } else if (this.multi) {
          //多选
          let options;
          // 判断是否选中
          if (
            this.value.filter((v) => {
              return (
                this.year === v[0] && this.month === v[1] - 1 && i === v[2]
              );
            }).length > 0
          ) {
            options = Object.assign(
              { day: i, selected: true },
              this.getLunarInfo(this.year, this.month + 1, i),
              this.getEvents(this.year, this.month + 1, i)
            );
          } else {
            options = Object.assign(
              { day: i, selected: false },
              this.getLunarInfo(this.year, this.month + 1, i),
              this.getEvents(this.year, this.month + 1, i)
            );
            if (this.begin.length > 0) {
              let beginTime = Number(
                new Date(
                  parseInt(this.begin[0]),
                  parseInt(this.begin[1]) - 1,
                  parseInt(this.begin[2])
                )
              );
              if (beginTime > Number(new Date(this.year, this.month, i)))
                options.disabled = true;
            }
            if (this.end.length > 0) {
              let endTime = Number(
                new Date(
                  parseInt(this.end[0]),
                  parseInt(this.end[1]) - 1,
                  parseInt(this.end[2])
                )
              );
              if (endTime < Number(new Date(this.year, this.month, i)))
                options.disabled = true;
            }
            if (this.disabled.length > 0) {
              if (
                this.disabled.filter((v) => {
                  return (
                    this.year === v[0] && this.month === v[1] - 1 && i === v[2]
                  );
                }).length > 0
              ) {
                options.disabled = true;
              }
            }
          }

          temp[line].push(options);
        } else {
          // 单选
          // console.log(this.lunar(this.year,this.month,i));
          let chk = new Date();
          let chkY = chk.getFullYear();
          let chkM = chk.getMonth();
          // 匹配上次选中的日期
          if (
            parseInt(seletSplit[0]) == this.year &&
            parseInt(seletSplit[1]) - 1 == this.month &&
            parseInt(seletSplit[2]) == i
          ) {
            // console.log("匹配上次选中的日期",)
            temp[line].push(
              Object.assign(
                { day: i, selected: true },
                this.getLunarInfo(this.year, this.month + 1, i),
                this.getEvents(this.year, this.month + 1, i)
              )
            );
            this.today = [line, temp[line].length - 1];
          }
          // 没有默认值的时候显示选中今天日期
          else if (
            chkY == this.year &&
            chkM == this.month &&
            i == this.day &&
            this.value == ""
          ) {
            // console.log("今天",)
            temp[line].push(
              Object.assign(
                { day: i, selected: false }, /** 今天不默认选中 */
                this.getLunarInfo(this.year, this.month + 1, i),
                this.getEvents(this.year, this.month + 1, i)
              )
            );
            this.today = [line, temp[line].length - 1];
          } else {
            // 普通日期
            // console.log("设置可选范围",)
            let options = Object.assign(
              { day: i, selected: false, },
              this.getLunarInfo(this.year, this.month + 1, i),
              this.getEvents(this.year, this.month + 1, i),
            );
            if (this.begin.length > 0) {
              let beginTime = Number(
                new Date(
                  parseInt(this.begin[0]),
                  parseInt(this.begin[1]) - 1,
                  parseInt(this.begin[2])
                )
              );
              if (beginTime > Number(new Date(this.year, this.month, i)))
                options.disabled = true;
            }
            if (this.end.length > 0) {
              let endTime = Number(
                new Date(
                  parseInt(this.end[0]),
                  parseInt(this.end[1]) - 1,
                  parseInt(this.end[2])
                )
              );
              if (endTime < Number(new Date(this.year, this.month, i)))
                options.disabled = true;
            }
            if (this.disabled.length > 0) {
              if (
                this.disabled.filter((v) => {
                  return (
                    this.year === v[0] && this.month === v[1] - 1 && i === v[2]
                  );
                }).length > 0
              ) {
                options.disabled = true;
              }
            }
            temp[line].push(options);
          }
        }
        // 到周六换行
        if (day == 6 && i < lastDateOfMonth) {
          line++;
        } else if (i == lastDateOfMonth) {
          // line++
          let k = 1;
          for (let d = day; d < 6; d++) {
            // console.log(this.computedNextYear()+"-"+this.computedNextMonth(true)+"-"+k)
            temp[line].push(
              Object.assign(
                { day: k, disabled: true },
                this.getLunarInfo(
                  this.computedNextYear(),
                  this.computedNextMonth(true),
                  k
                ),
                this.getEvents(
                  this.computedNextYear(),
                  this.computedNextMonth(true),
                  k
                )
              )
            );
            k++;
          }
          // 下个月除了补充的前几天开始的日期
          nextMonthPushDays = k;
        }
      } //end for
      // console.log(this.year+"/"+this.month+"/"+this.day+":"+line)
      // 补充第六行让视觉稳定
      if (line <= 5 && nextMonthPushDays > 0) {
        // console.log({nextMonthPushDays:nextMonthPushDays,line:line})
        for (let i = line + 1; i <= 5; i++) {
          temp[i] = [];
          let start = nextMonthPushDays + (i - line - 1) * 7;
          for (let d = start; d <= start + 6; d++) {
            temp[i].push(
              Object.assign(
                { day: d, disabled: true },
                this.getLunarInfo(
                  this.computedNextYear(),
                  this.computedNextMonth(true),
                  d
                ),
                this.getEvents(
                  this.computedNextYear(),
                  this.computedNextMonth(true),
                  d
                )
              )
            );
          }
        }
      }
      this.days = temp;
      // 处理记事
      this.$nextTick(async () => {
        await this.setDefaultNotesDays();
      });
    },
    computedPrevYear () {
      let value = this.year;
      if (this.month - 1 < 0) {
        value--;
      }
      return value;
    },
    computedPrevMonth (isString) {
      let value = this.month;
      if (this.month - 1 < 0) {
        value = 11;
      } else {
        value--;
      }
      // 用于显示目的（一般月份是从0开始的）
      if (isString) {
        return value + 1;
      }
      return value;
    },
    computedNextYear () {
      let value = this.year;
      if (this.month + 1 > 11) {
        value++;
      }
      return value;
    },
    computedNextMonth (isString) {
      let value = this.month;
      if (this.month + 1 > 11) {
        value = 0;
      } else {
        value++;
      }
      // 用于显示目的（一般月份是从0开始的）
      if (isString) {
        return value + 1;
      }
      return value;
    },
    // 获取农历信息
    getLunarInfo (y, m, d) {
      let lunarInfo = calendar.solar2lunar(y, m, d);
      let lunarValue = lunarInfo.IDayCn;
      // console.log(lunarInfo)
      let isLunarFestival = false;
      let isGregorianFestival = false;
      if (
        this.festival.lunar[lunarInfo.lMonth + "-" + lunarInfo.lDay] !=
        undefined
      ) {
        lunarValue =
          this.festival.lunar[lunarInfo.lMonth + "-" + lunarInfo.lDay];
        isLunarFestival = true;
      } else if (this.festival.gregorian[m + "-" + d] != undefined) {
        lunarValue = this.festival.gregorian[m + "-" + d];
        isGregorianFestival = true;
      }
      return {
        lunar: lunarValue,
        isLunarFestival: isLunarFestival,
        isGregorianFestival: isGregorianFestival,
      };
    },
    // 获取自定义事件
    getEvents (y, m, d) {
      if (Object.keys(this.events).length == 0) return false;
      let eventName = this.events[y + "-" + m + "-" + d];
      let data = {};
      if (eventName != undefined) {
        data.eventName = eventName;
      }
      return data;
    },
    // 上月
    prev (e) {
      e.stopPropagation();
      if (this.month == 0) {
        this.month = 11;
        this.year = parseInt(this.year) - 1;
      } else {
        this.month = parseInt(this.month) - 1;
      }
      this.render(this.year, this.month);

      this.$emit("selectMonth", this.month + 1, this.year);
      this.$emit("prev", this.month + 1, this.year);
    },
    //  下月
    next (e) {
      e.stopPropagation();
      if (this.month == 11) {
        this.month = 0;
        this.year = parseInt(this.year) + 1;
      } else {
        this.month = parseInt(this.month) + 1;
      }
      this.render(this.year, this.month);

      this.$emit("selectMonth", this.month + 1, this.year);
      this.$emit("next", this.month + 1, this.year);
    },
    // 选中日期
    handleSelectDate (k1, k2, e, data) {
      if (e != undefined) e.stopPropagation();
      // 日期范围
      if (this.range) {
        if (this.rangeBegin.length == 0 || this.rangeEndTemp != 0) {
          this.rangeBegin = [this.year, this.month, this.days[k1][k2].day];
          this.rangeBeginTemp = this.rangeBegin;
          this.rangeEnd = [this.year, this.month, this.days[k1][k2].day];
          this.rangeEndTemp = 0;
        } else {
          this.rangeEnd = [this.year, this.month, this.days[k1][k2].day];
          this.rangeEndTemp = 1;
          // 判断结束日期小于开始日期则自动颠倒过来
          if (
            +new Date(this.rangeEnd[0], this.rangeEnd[1], this.rangeEnd[2]) <
            +new Date(
              this.rangeBegin[0],
              this.rangeBegin[1],
              this.rangeBegin[2]
            )
          ) {
            this.rangeBegin = this.rangeEnd;
            this.rangeEnd = this.rangeBeginTemp;
          }
          // 小于10左边打补丁
          let begin = [];
          let end = [];
          if (this.zero) {
            this.rangeBegin.forEach((v, k) => {
              if (k == 1) v = v + 1;
              begin.push(this.zeroPad(v));
            });
            this.rangeEnd.forEach((v, k) => {
              if (k == 1) v = v + 1;
              end.push(this.zeroPad(v));
            });
          } else {
            begin = this.rangeBegin;
            end = this.rangeEnd;
          }
          // console.log("选中日期",begin,end)
          this.$emit("select", begin, end);
        }
        this.render(this.year, this.month);

      } else if (this.multi) {
        // 如果已经选过则过滤掉
        let filterDay = this.multiDays.filter((v) => {
          return (
            this.year === v[0] &&
            this.month === v[1] - 1 &&
            this.days[k1][k2].day === v[2]
          );
        });
        if (filterDay.length > 0) {
          this.multiDays = this.multiDays.filter((v) => {
            return (
              this.year !== v[0] ||
              this.month !== v[1] - 1 ||
              this.days[k1][k2].day !== v[2]
            );
          });
        } else {
          this.multiDays.push([
            this.year,
            this.month + 1,
            this.days[k1][k2].day,
          ]);
        }
        this.days[k1][k2].selected = !this.days[k1][k2].selected;
        this.$emit("select", this.multiDays);
      } else {
        // 取消上次选中
        if (this.today.length > 0) {
          this.days.forEach((v) => {
            v.forEach((vv) => {
              vv.selected = false;
              vv.popover = false;
            });
          });
        }
        // 设置当前选中天
        this.days[k1][k2].selected = true;
        // 设置popover
        this.days[k1][k2].popover = true;
        // 设置星期
        this.days[k1][k2].week = `星期${WEEKS[k2]}`;

        this.day = this.days[k1][k2].day;
        this.today = [k1, k2];
        this.$emit("select", [
          this.year,
          this.zero ? this.zeroPad(this.month + 1) : this.month + 1,
          this.zero
            ? this.zeroPad(this.days[k1][k2].day)
            : this.days[k1][k2].day,
        ], data);
      }
    },
    changeYear () {
      if (this.yearsShow) {
        this.yearsShow = false;
        return false;
      }
      this.yearsShow = true;
      this.years = [];
      for (let i = ~~this.year - 10; i < ~~this.year + 10; i++) {
        this.years.push(i);
      }
    },
    selectYear (value) {
      this.yearsShow = false;
      this.year = value;
      this.render(this.year, this.month);

      this.$emit("selectYear", value);
    },
    // 返回今天
    setToday () {
      // let now = new Date();
      // this.year = now.getFullYear();
      // this.month = now.getMonth();
      // this.day = now.getDate();
      // this.render(this.year, this.month);
      // 遍历当前日找到选中
      this.days.forEach((item) => {
        let day = item.find((child) => {
          return child.day == this.day && !child.disabled;
        });
        if (day != undefined) {
          day.today = true;
        }
      });
    },
    // 日期补零
    zeroPad (n) {
      return String(n < 10 ? "0" + n : n);
    },
  },
};
</script>

<style scoped lang="scss">
.calendar {
  margin: auto;
  width: 100%;
  min-width: 300px;
  background: #fff;
  font-family: 'PingFang SC', 'Hiragino Sans GB', 'STHeiti', 'Microsoft YaHei',
    'WenQuanYi Micro Hei', sans-serif;
  user-select: none;
  max-height: 370px;
  overflow: hidden;
  margin: auto;
  border-radius: 27px;
  box-shadow: 0px 6px 30px 0px rgba(51, 152, 255, 0.4);
  position: relative;
  z-index: 9;
}
.calendar-tools {
  height: 38px;
  background: #357bf0;
  border-radius: 27px 27px 0px 0px;
  font-size: 20px;
  line-height: 38px;
  color: #fff;
}
.calendar-tools span {
  cursor: pointer;
}
.calendar-tools span svg {
  margin-top: 10px;
}
.calendar-prev {
  width: 14.28571429%;
  float: left;
  text-align: center;
}
.calendar-info {
  padding-top: 3px;
  font-size: 16px;
  line-height: 1.3;
  text-align: center;
}
.calendar-info > div.month {
  margin: auto;
  height: 20px;
  width: 70px;
  text-align: center;
  color: #fff;
  overflow: hidden;
  position: relative;
  display: inline-block;
  font-size: 19px;
  /* line-height: 20px; */
  margin-top: 5px;
  font-weight: bold;
}
.calendar-info > div.month .month-inner {
  position: absolute;
  left: 0;
  top: 0;
  height: 240px;
  transition: top 0.5s cubic-bezier(0.075, 0.82, 0.165, 1);
}
.calendar-info > div.month .month-inner > span {
  display: block;
  font-size: 16px;
  height: 20px;
  width: 90px;
  overflow: hidden;
  text-align: center;
}
.calendar-info > div.year {
  font-size: 10px;
  line-height: 1;
  color: #fff;
  font-size: 17px;
  display: inline-block;
  vertical-align: middle;
  margin-top: -8px;
  font-weight: bold;
}
.calendar-info > div.year > span {
  font-weight: normal;
}
.calendar-next {
  width: 14.28571429%;
  float: right;
  text-align: center;
}

.calendar table {
  clear: both;
  width: 100%;
  margin-bottom: 10px;
  border-collapse: collapse;
  color: #444444;
}
.calendar td {
  margin: 2px !important;
  padding: 0px 0;
  width: 14.28571429%;
  height: 48px;
  text-align: center;
  vertical-align: middle;
  font-size: 14px;
  line-height: 125%;
  cursor: pointer;
  position: relative;
  vertical-align: top;
}
.calendar td.week {
  font-size: 10px;
  pointer-events: none !important;
  cursor: default !important;
}
.calendar td.disabled {
  color: #ccc;
  pointer-events: none !important;
  cursor: default !important;
  /* 隐藏非本月的日期 */
  visibility: hidden;
}
.calendar td.disabled div {
  color: #ccc;
}
.calendar td span {
  // display: block;
  // max-width: 40px;
  width: 43px;
  height: 43px;
  font-size: 16px;
  margin: 0px auto;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
}
.calendar
  td:not(.selected)
  span:not(.red):not(.date-span-content__today):hover {
  background: #f3f8fa;
  color: #444;
}
.calendar td:not(.selected) span.red:hover {
  background: #f9efef;
}
.calendar td:not(.disabled) span.red {
  color: #ff5a7b;
}
/* 选中样式 */
.calendar td.selected {
  .date-span {
    border: 1px solid #357bf0;
    box-shadow: 0px 0px 13px 0px #357bf0;

    &-content__has-notes {
      &.red {
        &::after {
          display: none;
        }
      }

      &::after {
        display: none;
      }
    }

    &.red {
      border: 1px solid #ea6151;
      box-shadow: 0px 0px 13px 0px #ea6151;

      .date-span-content {
        background: #ea6151;
      }
    }

    &-content {
      color: #fff;
      background: #357bf0;
      position: relative;
      width: 33px;
      height: 33px;
      border-radius: 50%;
    }
  }
}
.calendar {
  .date-span {
    &-content__has-notes {
      color: #357bf0;
      position: relative;

      &.red {
        &::after {
          background: #ea6151;
        }
      }

      &::after {
        content: '';
        width: 4px;
        height: 4px;
        background: #0172f6;
        border-radius: 50%;
        position: absolute;
        bottom: 0px;
      }
    }

    &-content__today {
      background: #94bcff;
      border-radius: 50%;
      width: 33px;
      height: 33px;
      color: #fff;
    }
  }
}
.calendar td .text {
  position: absolute;
  top: 28px;
  left: 0;
  right: 0;
  text-align: center;

  padding: 2px;
  font-size: 8px;
  line-height: 1.2;
  color: #444;
}
.calendar td .isGregorianFestival,
.calendar td .isLunarFestival {
  color: #ea6151;
}
.calendar td.selected span.red {
  // background-color: #ea6151;
  color: #fff;
}
.calendar td.selected span.red:hover {
  // background-color: #ea6151;
  color: #fff;
}
.calendar thead td {
  text-transform: uppercase;
  height: 30px;
  vertical-align: middle;
}
.calendar-button {
  text-align: center;
}
.calendar-button span {
  cursor: pointer;
  display: inline-block;
  min-height: 1em;
  min-width: 5em;
  vertical-align: baseline;
  background: #5e7a88;
  color: #fff;
  margin: 0 0.25em 0 0;
  padding: 0.6em 2em;
  font-size: 1em;
  line-height: 1em;
  text-align: center;
  border-radius: 0.3em;
}
.calendar-button span.cancel {
  background: #efefef;
  color: #666;
}
.calendar-years {
  position: absolute;
  left: 0px;
  top: 38px;
  right: 0px;
  bottom: 0px;
  background: #fff;
  display: flex;
  justify-content: start;
  align-items: center;
  flex-wrap: wrap;
  overflow: auto;
  transition: all 0.5s cubic-bezier(0.075, 0.82, 0.165, 1);
  opacity: 0;
  pointer-events: none;
  transform: translateY(-10px);
}
.calendar-years.show {
  opacity: 1;
  pointer-events: auto;
  transform: translateY(0px);
}
.calendar-years > span {
  margin: 1px 5px;
  display: inline-block;
  width: 60px;
  line-height: 30px;
  border-radius: 20px;
  text-align: center;
  border: 1px solid #fbfbfb;
  color: #999;
  cursor: pointer;
}
.calendar-years > span.active {
  border: 1px solid #ea6151;
  background-color: #ea6151;
  color: #fff;
}

>>> .add-date-events {
  position: absolute;
  width: 55px;
  height: 55px;
  right: 20px;
  bottom: 0px;

  .el-icon-plus {
    color: #fff;
    font-size: 24px;
  }
}
</style>