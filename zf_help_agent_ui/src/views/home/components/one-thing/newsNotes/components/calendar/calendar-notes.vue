<template>
  <div class="date-span-content__popover-content calendar-notes">
    <div class="popover-content-header">
      <div class="popover-content-title">{{ notesDay }}</div>
      <div class="popover-content-close" @click="$emit('close')">
        <i class="el-icon-close"></i>
      </div>
    </div>
    <el-scrollbar class="popover-content-scrollbar">
      <div
        class="popover-content-scrollbar__content-item"
        v-for="(item, idx) in notes"
        :key="item.id"
      >
        <div class="content-item-left">
          <div class="content-item-left--num">{{ idx + 1 }}</div>
          <div class="content-item-left--text">
            <span class="content-item-left--text-record">
              【{{ item.zslbDictName }}记事】
            </span>
            <el-tooltip
              effect="dark"
              :content="item.knowledgeContent"
              :disabled="isShowTooltip(item.knowledgeContent, 368.5)"
              :open-delay="300"
              placement="top"
            >
              <span class="content-item-left--text-note">
                {{ item.knowledgeContent }}
              </span>
            </el-tooltip>
          </div>
        </div>
        <div class="content-item-right">
          <template v-if="item.shareFlag === 0">
            <img
              src="@/assets/image/calendar/publish.png"
              width="16px"
              height="16px"
              alt=""
              @click="handlePublish(item.id)"
            />
            <img
              src="@/assets/image/calendar/edit.png"
              width="16px"
              height="16px"
              alt=""
              @click="handleEdit(item)"
            />
            <img
              src="@/assets/image/calendar/delete.png"
              width="16px"
              height="16px"
              alt=""
              @click="handleDelete(item.id)"
            />
          </template>
          <template v-else>
            <div class="content-item-right--published">
              <img
                src="@/assets/image/calendar/published.png"
                width="16px"
                height="16px"
                alt=""
              />
              <span>已共享</span>
            </div>
            <el-button type="text" @click="handlePublish(item.id)">
              取消共享
            </el-button>
          </template>
        </div>
      </div>
    </el-scrollbar>
  </div>
</template>

<script>
import { deleteNotesById, shareNotesById } from '@/api/home/calendar';

import { isShowTooltip } from '@/utils/utils.js'
export default {
  name: 'CalendarNotes',
  props: {
    notes: {
      type: Array,
      default: () => []
    },
    date: {
      type: Object,
      default: () => ({})
    },
  },
  data () {
    return {

    }
  },
  computed: {
    notesDay ({ date }) {
      return `${date.week}, ${date.month + 1}月${date.day}日`;
    }
  },
  methods: {
    isShowTooltip,
    handleEdit (item) {
      this.$emit('edit', item);
    },

    /** 如果删除空了 要重新查询渲染 */
    handleDelete (id) {
      // this.$confirm('是否删除当前选中记事?', '提示', {
      //   confirmButtonText: '确定',
      //   cancelButtonText: '取消',
      //   type: 'warning'
      // }).then(() => {
      deleteNotesById(id).then(() => {
        this.$emit('render', this.notes.length === 1);
        this.$message.success('删除成功!');
      })
      // }).catch();
    },

    handlePublish (id) {
      shareNotesById(id).then(() => {
        this.$emit('render');
      })
    },
  }
}
</script>

<style lang="scss">
@import '@/assets/styles/mixin/index.scss';
.date-span-content__popover.el-popover {
  padding: unset;

  .date-span-content__popover-content.calendar-notes {
    height: 280px;
    color: #193150;
    font-family: PingFang SC;

    .popover-content-header {
      height: 46px;
      display: flex;
      align-items: center;
      justify-content: space-between;
      border-bottom: 1px solid #e6ebf3;
      padding-left: 25px;

      .popover-content-title {
        position: relative;
        font-size: 18px;
        font-weight: 500;

        &::before {
          content: '';
          position: absolute;
          left: -10px;
          top: 2px;
          width: 6px;
          height: 20px;
          border-radius: 3px;
          background: #357bf0;
        }
      }

      .popover-content-close {
        width: 50px;
        height: 100%;
        background: #e6ebf3;
        border: 3px solid #e6ebf3;
        border-radius: 0px 5px 0px 0px;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 20px;
        cursor: pointer;
      }
    }

    .popover-content-scrollbar.el-scrollbar {
      height: calc(100% - 46px);

      .popover-content-scrollbar__content-item {
        height: 46px;
        border-bottom: 1px solid #e6ebf3;
        display: flex;
        align-items: center;
        justify-content: space-between;

        .content-item-left {
          padding-left: 12px;
          flex: 7;
          border-right: 1px solid #e6ebf3;
          display: flex;
          align-items: center;
          height: 100%;

          &--num {
            width: 22px;
            height: 22px;
            background: #96c2f1;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-family: DIN Black;
            color: #ffffff;
          }

          &--text {
            display: flex;
            flex: 1;

            &-record {
              font-size: 16px;
              font-weight: 600;
            }

            &-note {
              flex: 1;
              font-size: 16px;
              font-weight: 500;
              @include overflow-rows(1);
            }
          }
        }

        .content-item-right {
          flex: 3;
          display: flex;
          justify-content: space-around;

          img {
            cursor: pointer;
          }

          &--published {
            display: flex;
            align-items: center;

            img {
              margin-right: 5px;
            }
          }
        }
      }
    }
  }
}
</style>
