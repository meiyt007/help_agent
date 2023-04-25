<template>

    <a  :href="url" :download="title + '帮助文档.pdf'" >
      <div>
        <svg-icon icon-class="question"/>
        <span>帮助文档</span>
      </div>
    </a>
    <!--<svg-icon icon-class="question" @click="goto"/>-->
</template>

<script>
export default {
  name: 'ZfsoftDoc',
  data() {
    return {
      title:this.Common.title,
      url: process.env.BASE_URL+'HELP_DOC.pdf'
    }
  },
  methods: {
  }
}
</script>
