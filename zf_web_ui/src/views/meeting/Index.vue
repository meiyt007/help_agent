<template>
  <div class="router-item router-index">
    <div class="btn-container">
      <IconLabelBtn src='index/create_meet.png' label='开始通话' @btnclick='startMeetFn'/>
      <IconLabelBtn src='index/join_meet.png' label='加入通话' @btnclick='joinMeetFn'/>
    </div>
  </div>
</template>

<script>

import IconLabelBtn from '@/components/IconLabelBtn.vue'
export default {
  name: 'Index',
  mounted(){
  },
  methods: {
    startMeetFn () {
      this.$router.push('join')
    },
    joinMeetFn () {
      this.$router.push('join')
    }
  },
  components: {
    // HelloWorld
    IconLabelBtn
  }
}
</script>
<style lang='less'>
  .router-index{
    display: flex;
    flex-direction: row;
    justify-content: center;
    align-items: center;
    .btn-container{
      width:400px;
      height:144px;
      display:flex;
      flex-direction:row;
      justify-content: space-between;

    }
  }
</style>
<style lang="scss"  scoped>
  @import './global.css';
</style>
