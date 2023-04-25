<template>
  <el-dialog
    width="623px"
    height="420px"
    scrollbar
    :title="title"
    :visible.sync="dialogVisible"
    :close-on-press-escape="false"
    :close-on-click-modal="false"
    custom-class="calendar-add-dialog"
    append-to-body
    center
  >
    <el-form
      :model="form"
      :rules="rules"
      ref="ruleForm"
      label-width="0px"
      :show-message="false"
    >
      <table
        cellspacing="0"
        cellpadding="0"
        border="0"
        class="calendar-add-dialog--container"
      >
        <colgroup>
          <col width="30%" />
          <col width="70%" />
        </colgroup>
        <tr>
          <td><i class="require">*</i><b>记事类型：</b></td>
          <td colspan="3">
            <el-form-item prop="zslbDictOid">
              <el-radio-group v-model="form.zslbDictOid">
                <el-radio
                  v-for="item in zslbDict"
                  :key="item.dictOid"
                  :label="item.dictOid"
                >
                  {{ item.name }}记事
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </td>
        </tr>
        <tr>
          <td><i class="require">*</i><b>记事标题：</b></td>
          <td colspan="3">
            <el-form-item prop="knowledgeTitle">
              <el-input
                v-model="form.knowledgeTitle"
                name="knowledgeTitle"
                maxlength="50"
                show-word-limit
                type="textarea"
                :rows="2"
              />
            </el-form-item>
          </td>
        </tr>
        <tr>
          <td><i class="require">*</i><b>记事内容：</b></td>
          <td>
            <el-form-item prop="knowledgeContent">
              <el-input
                v-model="form.knowledgeContent"
                name="knowledgeContent"
                type="textarea"
                :autosize="{ minRows: 2, maxRows: 15 }"
                maxlength="500"
                show-word-limit
              />
            </el-form-item>
          </td>
        </tr>
        <tr>
          <td style="text-align: right"><b>备注：</b></td>
          <td>
            <el-input
              v-model="form.note"
              name="note"
              maxlength="200"
              show-word-limit
              type="textarea"
              :autosize="{ minRows: 2, maxRows: 6 }"
            />
          </td>
        </tr>
      </table>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button type="primary" @click="handleSave">保 存</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { RULES } from './constant';
import { saveNotesByDate } from '@/api/home/calendar';
export default {
  name: 'CalendarAdd',
  props: {
    visible: Boolean,
    type: {
      type: String,
      default: 'add'
    },
    form: {
      type: Object
    },
    zslbDict: Array
  },
  data () {
    return {
      rules: RULES
    }
  },
  computed: {
    dialogVisible: {
      get () {
        return this.visible;
      },
      set () {
        this.$refs.ruleForm.clearValidate();
        this.$emit('update:visible', false);
      }
    },

    title () {
      return this.type === 'add' ? '今日新增记事' : '编辑记事';
    }
  },
  methods: {
    handleSave () {
      this.$refs.ruleForm.validate(async (valid) => {
        if (valid) {
          const { code } = await saveNotesByDate({
            ...this.form,
            zslbDictName: this.zslbDict.find(item => item.dictOid === this.form.zslbDictOid)?.name
          });
          if (code !== 200) return this.$message.warning(`${this.title}失败`);
          this.$emit('update:visible');
          this.$emit('render');
          this.$message.success(`${this.title}成功`);
        }
      })
    },
  }
}
</script>

<style scoped lang="scss">
.calendar-add-dialog--container {
  width: 100%;
  border-collapse: collapse;

  tr {
    height: 60px;

    td {
      border: 1px solid #e0e6f0;
    }

    td:first-child {
      text-align: right;
      background: #edf0f5;
    }

    >>> .el-form-item {
      margin-bottom: unset;
    }

    td:nth-child(2) {
      padding: 0 15px;
    }
  }
}
</style>
