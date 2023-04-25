<template>
  <label
    class="el-checkbox"
    :class="[{ 'is-disabled': isDisabled }, { 'is-checked': isChecked }]"
    @click="handleChange"
  >
    <span
      class="el-checkbox__input"
      :class="{
        'is-disabled': isDisabled,
        'is-checked': isChecked,
      }"
    >
      <span class="el-checkbox__inner"></span>
    </span>
  </label>
</template>

<script>
export default {
  name: 'checkbox',
  props: {
    isDisabled: Boolean,
    checked: Boolean
  },
  data () {
    return {
      isChecked: false
    }
  },
  watch: {
    checked: {
      immediate: true,
      handler () {
        this.isChecked = this.checked;
      }
    }
  },
  methods: {
    handleChange () {
      this.isChecked = !this.isChecked;
      this.$emit('change', this.isChecked);
    }
  }
}
</script>