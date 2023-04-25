<template>
    <div>
      <el-radio-group v-model="b_type" style="margin-bottom: 15px">
        <el-radio v-for="type in bodyTypes" :key="type" :value="type" :label="type">{{type}}</el-radio>
        <!--<el-select v-model="r_option" @change="changeMode" v-show="b_type==='raw'" size="small" placeholder="请选择">
          <el-option
            v-for="option in rawOptions" :key="option.key"
            :label="option.value"
            :value="option.key">
          </el-option>
        </el-select>-->
      </el-radio-group>
      <hr/>
      <div v-show="b_type==='none'">
          <p class="strong"><strong>This request does not have body</strong></p>
      </div>
      <div v-show="b_type==='form-data'">
          <DataTableFormData :title="b_type" :data-source="form_data"
                      @dataChanged="updateFormData" :editable="true"></DataTableFormData>
      </div>
      <div v-show="b_type==='x-www-form-urlencoded'">
          <data-table :title="b_type" :data-source="x_www_urlencoded"
                      @dataChanged="updateUrlEncodedData" :editable="true"></data-table>
      </div>
      <div v-if="b_type==='raw(JSON)'">
          <InCoder theme="panda-syntax" :value="text" :readOnly="false" @input="updateText" ref="inCoderRef" :language="language"></InCoder>
      </div>
     <div v-show="b_type==='binary'">
        <Binary @dataChanged="updateBinary" :value="binary_data" ></Binary>
      </div>

    </div>
</template>

<script>
    import DataTable from "./DataTable";
    import DataTableFormData from "./DataTableFormData";
    import InCoder from "./InCoder";
    import Binary from "./Binary";
    export default {
        name: "RequestBodyPanel",
        components: {InCoder, DataTable,Binary,DataTableFormData},
        props: ['body'],
        data: function () {
            let request_body = this.body;
            return {
                rawOptions: [
                    {key: 'javascript', value: 'javascript'},
                    {key: 'text', value: 'Text'},
                    {key: 'css', value: 'CSS'},
                    {key: 'application/json', value: 'JSON'},
                    {key: 'xml', value: 'XML'},
                    {key: 'html', value: 'HTML'},
                ],
                //bodyTypes: ['none', 'form-data', 'x-www-form-urlencoded','binary', 'raw'],
                bodyTypes: ['none', 'form-data', 'x-www-form-urlencoded', 'raw(JSON)'],
                r_option: 'application/json',
                b_type: 'raw(JSON)',
                form_data: [],
                x_www_urlencoded: [],
                r_body: request_body,
                text: '',
                language:'json',
                binary_data:''

            };
        },
        created() {
            if (this.r_body.type === 'none') {
                this.b_type = 'none';
                return;
            }
            if (this.r_body.type === 'form-data') {
                this.b_type = this.r_body.type;
                this.form_data = this.r_body.content;
                return;
            }
            if (this.r_body.type === 'x-www-form-urlencoded') {
                this.b_type = this.r_body.type;
                this.x_www_urlencoded = this.r_body.content;
                return;
            }
            if (this.r_body.type === 'binary') {
              this.b_type = this.r_body.type;
              this.binary_data = this.r_body.content;
              return;
            }

            this.b_type = 'raw(JSON)';
            let item = this.rawOptions.find(o => {
                return o.key === this.r_body.type;
            });
            this.r_option = item.value;
            this.text = this.r_body.content;
        },
        methods: {
            // 更改模式
            changeMode(val) {
              this.$refs.inCoderRef.changeMode(val);
            },
            commitBody: function () {
                let body = {};
                if (this.b_type !== 'raw(JSON)') {
                    body.type = this.b_type;
                    if (this.b_type === 'x-www-form-urlencoded') {
                        body.content = this.x_www_urlencoded;
                    } else if (this.b_type === 'form-data') {
                        body.content = this.form_data;
                    } else if (this.b_type === 'binary') {
                        body.content = this.binary_data;
                    }

                } else {
                   /* let temp = this.rawOptions.find(o => {
                        return o.key === this.r_option;
                    });*/
                    body.type = 'application/json';
                    body.content = this.text;
                }
                this.$emit('changed', body);
            },
            updateFormData(data) {
                this.form_data = data;
            },
            updateUrlEncodedData(data) {
                this.x_www_urlencoded = data;
            },
            updateText(text) {
                this.text = text;
            },
            updateBinary: function (binary) {
              this.binary_data = binary;
            },
        },
        watch: {
            b_type: function () {
                this.commitBody();
            },
            r_option: function () {
                this.commitBody();
            },
            form_data: function () {
                this.commitBody();
            },
            x_www_urlencoded: function () {
                this.commitBody();
            },
            text: function () {
                this.commitBody();
            },
            binary_data: function () {
              this.commitBody();
            }
        }
    }
</script>

<style scoped>
    .strong {
        text-align: center;
        font-size: 18px;
        font-style: italic;
    }
</style>
