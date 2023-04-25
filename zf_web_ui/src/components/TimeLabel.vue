<template>
  <div class="cmp-time-label">
    <span class='time-label'>{{formatTimer}}</span>
  </div>
</template>

<script>

import { mapState } from 'vuex'

export default {
  name: 'TimeLabel',
  data () {
    return {
      time: 0,
      timer: null
    }
  },
  props: {
    src: String,
    label: String
  },
  computed: {
    ...mapState({
      joined: state => state.joined
    }),
    hour: function () {
      return Math.floor(this.time / 3600)
    },
    minute: function () {
      return Math.floor((this.time - this.hour * 3600) / 60)
    },
    second: function () {
      return this.time - this.hour * 3600 - this.minute * 60
    },
    formatTimer: function () {
      let time = ''
      if (this.hour > 9) {
        time += this.hour
      }
      else {
        time += ('0' + this.hour)
      }
      time += ':'
      if (this.minute > 9) {
        time += this.minute
      }
      else {
        time += ('0' + this.minute)
      }
      time += ':'
      if (this.second > 9) {
        time += this.second
      }
      else {
        time += ('0' + this.second)
      }
      return time
    }
  },
  watch: {
    joined: {
      handler: function (newValue, oldValue) {
        if (newValue) {
          this.time = 0
          this.timer = setInterval(() => {
          this.time += 1
          }, 1000)
        }
        else {
          clearInterval(this.timer)
          this.timer = null
        }
      },
      immediate: true
    }
  },
  methods: {
  },
  mounted () {
  },
  components: {
  },

  boforeDestroy () {
    if (this.timer) {
      clearInterval(this.timer)
      this.timer = null
    }
  }
}
</script>
<style scoped lang="less">
    .cmp-time-label{
      .time-label{
        color: #000;
        font-size: 14px;
      }
    }

</style>
