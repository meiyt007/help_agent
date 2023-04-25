<template>
  <div>
    <div class="inCoderDiv">
      <InCoder theme="panda-syntax" :value="local" :readOnly="false" @input="updateLocal" ref="inCoder"></InCoder>
    </div>
    <div class="inCoderCode">
      <h3>SNIPPETS</h3>
      <a class="code" v-for="(tip,index) in tips" :key="index" @click.prevent="addText(index)">
        {{tip.title}}</a>

      <a class="code" style="color:#192c308f;" @click.prevent="removeAll">清 空</a>
    </div>
  </div>
</template>

<script>

    import InCoder from "./InCoder";

    export default {
        name: "ScriptPanel",
        components: {InCoder},
        props: ['text', 'tips'],
        data: function () {

            return {
                local: this.text,
            }
        },
        methods: {
            updateLocal: function (msg) {
                this.local = msg;
            },
            addText: function (index) {
                this.$refs.inCoder.addScript(this.tips[index].script);
            },
           removeAll: function () {
            this.$refs.inCoder.removeAll();
          },
        },
        watch: {
            local: function () {
                this.$emit('changed', this.local);
            }
        }
    }
</script>

<style scoped>
  .inCoderDiv{
    width: 83%;float: left;
  }
  .inCoderCode{
    width: 12%;float: left;margin-left: 15px
  }
  .code {
      font-family: 'PT Mono', 'Microsoft Yahei Mono', 'Source Code Pro', 'Consolas', sans-serif;
      margin: 7px;
      display: block;
      font-size: 14px;
      color: #40a9ff;
  }
</style>
