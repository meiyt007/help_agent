<template>
  <div className="in-coder-panel">
    <textarea ref="textarea"></textarea>
  </div>
</template>

<script type="text/ecmascript-6">
  // 引入全局实例
  import _CodeMirror from "codemirror";

  // 核心样式
  import "codemirror/lib/codemirror.css";
  // 引入主题后还需要在 options 中指定主题才会生效
  import "codemirror/theme/duotone-light.css"
  import "codemirror/theme/panda-syntax.css"


  // 需要引入具体的语法高亮库才会有对应的语法高亮效果
  // codemirror 官方其实支持通过 /addon/mode/loadmode.js 和 /mode/meta.js 来实现动态加载对应语法高亮库
  // 但 vue 貌似没有无法在实例初始化后再动态加载对应 JS ，所以此处才把对应的 JS 提前引入
  import "codemirror/mode/javascript/javascript.js";
  import "codemirror/mode/css/css.js";
  import "codemirror/mode/xml/xml.js";

  // 折叠
  import 'codemirror/addon/fold/foldgutter.css'
  import 'codemirror/addon/fold/foldcode'
  import 'codemirror/addon/fold/foldgutter'
  import 'codemirror/addon/fold/brace-fold'
  import 'codemirror/addon/fold/comment-fold'

  // 尝试获取全局实例
  const CodeMirror = window.CodeMirror || _CodeMirror;

  export default {
    name: "in-coder",
    props: {
      // 外部传入的内容，用于实现双向绑定
      value: String,
      // 外部传入的语法类型
      language: {
        type: String,
        default: 'json'
      },
      readOnly: {
        type: Boolean,
        default: true
      },
      lineNumbers: {
        type: Boolean,
        default: true
      },
      theme: {
        type: String,
        default: 'duotone-light'
      },
    },
    data() {
      return {
        // 默认的语法类型
        mode: 'json',
        // 编辑器实例
        coder: null,
        // 默认配置
        options: {
          mode: 'application/json',
          // 缩进格式
          tabSize: 2,
          // 主题，对应主题库 JS 需要提前引入
          theme: this.theme,
          // 显示行号
          lineNumbers: this.lineNumbers,
          line: true,
          readOnly: this.readOnly,
          fixedGutter: true,
          scrollbarStyle: null,
          lineWrapping: true,
          matchBrackets: true,
          foldGutter: true,
          gutters: ['CodeMirror-linenumbers', 'CodeMirror-foldgutter', 'CodeMirror-lint-markers'],
          autoFormatJson: true
        },
        // 支持切换的语法高亮类型，对应 JS 已经提前引入
        // 使用的是 MIME-TYPE ，不过作为前缀的 text/ 在后面指定时写死了
        modes: [
          {
            value: "css",
            label: "css"
          },
          {
            value: "javascript",
            label: "javascript"
          },
          {
            value: "html",
            label: "xml"
          },
          {
            value: "xml",
            label: "xml"
          },
          {
            value: "json",
            label: "application/json"
          },
          {
            value: "text",
            label: "text"
          },
        ],

      };
    },
    mounted() {
      // 初始化
      this._initialize();
    },
    methods: {
      // 初始化
      _initialize() {
        // 初始化编辑器实例，传入需要被实例化的文本域对象和默认配置
        this.coder = CodeMirror.fromTextArea(this.$refs.textarea, this.options);
        // 编辑器赋值
        this.coder.setValue(this.value);
        // 支持双向绑定
        this.coder.on("change", coder => {
          this.code = coder.getValue();
          if (this.$emit) {
            this.$emit("input", this.code);
          }
        });
        //失去焦点
        this.coder.on("blur", coder => {
          try{
            let val = coder.getValue();
            if (this.mode == "application/json" && val) {
              this.coder.setValue(this.formatStrInJson(val));
            }
          } catch (e) {
            // 啥也不做
          }
        });

        // 尝试从父容器获取语法类型
        if (this.language) {
          // 获取具体的语法类型对象
          let modeObj = this._getLanguage(this.language);
          // 判断父容器传入的语法是否被支持
          if (modeObj) {
            this.mode = modeObj.label;
          }

        }
      },
      // 获取当前语法类型
      _getLanguage(language) {
        // 在支持的语法类型列表中寻找传入的语法类型
        return this.modes.find(mode => {
          // 所有的值都忽略大小写，方便比较
          let currentLanguage = language.toLowerCase();
          let currentLabel = mode.label.toLowerCase();
          let currentValue = mode.value.toLowerCase();

          // 由于真实值可能不规范，例如 java 的真实值是 x-java ，所以讲 value 和 label 同时和传入语法进行比较
          return (
            currentLabel === currentLanguage || currentValue === currentLanguage
          );
        });
      },
      // 更改模式
      changeMode(val) {
        // 获取修改后的语法
        let label = this._getLanguage(val).label.toLowerCase();
        this.coder.setOption("mode", label);
        // 修改编辑器的语法配置
        //this.coder.setOption("mode", `text/${label}`);
        // 允许父容器通过以下函数监听当前的语法值
        this.$emit("language-change", label);
      },

      addScript: function(text) {
        let temp = this.coder.getValue();
        if (temp.length === 0) {
          this.coder.setValue(text);
        } else {
          this.coder.setValue(temp + '\n' + text);
        }
      },
      removeAll: function() {
        this.coder.setValue('');
      },
      // 格式化字符串为json格式字符串
      formatStrInJson(strValue) {
        return JSON.stringify(
          JSON.parse(strValue),
          null,
          2
        );
      }
    },
  };
</script>

<style>

</style>
