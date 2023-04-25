<template>
  <div class="as-answer clearfix">
    <div class="system fr">
      <span>系统</span>
      <img src="@/assets/image/system.png" />
    </div>
    <div class="items fr" v-if="type === 1">
      您是要办理：
      <ul>
        <!-- 如果是`是否问答` -->
        <li v-if="message.changeItemFlag == 1">{{ message.answer }}</li>
        <!-- 如果是办事指南 -->
        <li v-else-if="message.infoFlag">{{ message.answer }}</li>
        <!-- 否则就获取事项名称 itemDict -->
        <template v-else-if="content && Object.keys(content).length > 0">
          <li
            v-for="(item, index) in content"
            :key="index"
            class="item-dict"
            @click="$emit('answerClick', item, index)"
          >
            {{ index }}
          </li>
        </template>
        <li v-else>{{ message.answer || '这个我不明白哈~' }}</li>
      </ul>
      <el-radio-group
        v-model.trim="radio"
        @change="radioChange"
        fill="#3d5fb5"
        v-if="message.changeItemFlag == 1"
      >
        <el-radio-button label="是"></el-radio-button>
        <el-radio-button label="否"></el-radio-button>
      </el-radio-group>
    </div>
  </div>
</template>

<script>
export default {
  name: "RightItem",
  props: ["id", "type", "content", "message"],
  data () {
    return {
      radio: '',
    }
  },
  methods: {
    radioChange (val) {
      this.$emit("radioSelect", val);
    }
  }
};
</script>

<style scoped lang="scss">
.as-answer {
  .system {
    width: 100%;
    text-align: right;

    img {
      vertical-align: middle;
      width: 34px;
      height: 34px;
      border-radius: 50%;
    }

    span {
      color: #1890ff;
      font-size: 14px;
    }
  }

  .items {
    background-color: rgba(33, 144, 246, 1);
    color: rgba(90, 234, 255, 1);
    font-size: 15px;
    padding: 12px 21px;
    margin-right: 45px;
    border-top-left-radius: 8px;
    border-bottom-left-radius: 8px;
    border-bottom-right-radius: 8px;

    ul {
      padding-left: 0;

      li {
        list-style: none;
        background-color: #3e9df4;
        color: #fff;
        border-radius: 5px;
        padding: 10px;
        min-width: 160px;
        margin-top: 5px;
      }

      .item-dict:hover {
        color: #ffc600;
        background-color: #1178f6;
        cursor: pointer;
      }
    }
  }
}
</style>