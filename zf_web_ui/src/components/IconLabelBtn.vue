<template>
  <div class="cmp-icon-label-btn"
    @click='btnClick'
  >
        <img :src="require('../assets/'+src)" class='btn-icon'/>
        <span class='btn-label'>{{label}}</span>
  </div>
</template>

<script>
export default {
  name: 'IconLabelBtn',
  data () {
    return {
    }
  },
  methods: {
    btnClick () {
      this.$emit('btnclick')
    }
  },
  props: {
    src: String,
    label: String
  },
  mounted () {
    console.log(1234, this.src)
  }
}
</script>
<style scoped lang="less">
    .cmp-icon-label-btn{
        // background: red;
        width: 100px;
        height: 144px;
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        cursor: pointer;
        .btn-icon{
            width: 100px;
            height: 100px;
        }
        .btn-label{
            width: 100px;
            height: 22px;
            color: #333;
            font-size: 16px;
        }
    }
</style>
