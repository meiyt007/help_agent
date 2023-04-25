<template>
  <div>
    <el-form-item label="编号" v-show="false" >
      <el-input v-model="id" readonly></el-input>
    </el-form-item>
    <el-form-item label="流程名称">
      <!--<el-input v-model="name" readonly></el-input>-->
      {{name}}
    </el-form-item>
    <!--<el-form-item label="目标命名空间" v-show="false">
      <el-input v-model="process_namespace" readonly></el-input>
    </el-form-item>-->
    <!--<el-form-item label="描述" v-show="false">
      <el-input type="textarea" v-model="description" readonly></el-input>
    </el-form-item>-->
  </div>
</template>
<script>
export default {
  //依赖注入
  props: ['element','flowName'],
  data () {
    return {
      id: '',
      name: this.flowName,
      process_namespace: '',
      description: ''
    }
  },
  methods:{
    changeName(name){
      this.name = name;
    }
  },
  created() {
   // console.log(this.flowName)
  },
  watch: {
    // 监控element值，当发生改变时获取响应的属性
   /*element: {
      deep: true,
      immediate: true,
      handler(newVal, oldVal) {
        if(newVal) {
          this.id = newVal.businessObject.get('id');
          this.name = newVal.businessObject.get('name');
          alert(this.name)
          this.process_id = this.id;
          this.process_namespace = newVal.businessObject.get('process_namespace');
        }
      }
    }*/
  }
}

</script>
