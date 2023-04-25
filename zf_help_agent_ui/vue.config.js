"use strict";
const path = require("path");
const webpackCommentPlugin = require("webpack-comment-plugin");

function resolve(dir) {
  return path.join(__dirname, dir);
}

const name = "智能帮办业务应用系统"; // 标题

// const port = process.env.port || process.env.npm_config_port || 8899; // 端口
const port = process.env.port || process.env.npm_config_port || 8095; // 端口 黄埔互联网端口
// const port = process.env.port || process.env.npm_config_port || 8085; // 端口 黄埔政务内网环境
const plugins =
  process.env.NODE_ENV === "development" ? [new webpackCommentPlugin()] : [];
// vue.config.js 配置说明
//官方vue.config.js 参考文档 https://cli.vuejs.org/zh/config/#css-loaderoptions
// 这里只列一部分，具体配置参考文档
module.exports = {
  // 部署生产环境和开发环境下的URL。
  // 默认情况下，Vue CLI 会假设你的应用是被部署在一个域名的根路径上
  // 例如 https://www.ruoyi.vip/。如果应用被部署在一个子路径上，你就需要用这个选项指定这个子路径。例如，如果你的应用被部署在 https://www.ruoyi.vip/admin/，则设置 baseUrl 为 /admin/。
  publicPath: "/manager",
  // 在npm run build 或 yarn build 时 ，生成文件的目录名称（要和baseUrl的生产环境路径一致）（默认dist）
  outputDir: "manager",
  // 用于放置生成的静态资源 (js、css、img、fonts) 的；（项目打包之后，静态资源会放在这个文件夹下）
  assetsDir: "static",
  // 是否开启eslint保存检测，有效值：ture | false | 'error'
  lintOnSave: process.env.NODE_ENV === "development",
  // 如果你不需要生产环境的 source map，可以将其设置为 false 以加速生产环境构建。
  productionSourceMap: false,
  // webpack-dev-server 相关配置
  devServer: {
    host: "0.0.0.0",
    port: port,
    proxy: {
      // detail: https://cli.vuejs.org/config/#devserver-proxy

      // 单办办件内部代理
      [process.env.VUE_CASE_API]: {
        target: process.env.VUE_CASE_URL,
        changeOrigin: true,
        pathRewrite: {
          ["^" + process.env.VUE_CASE_API]: ""
        }
      },
      // 综窗内部代理
      [process.env.VUE_APP_ZC_API]: {
        target: process.env.VUE_APP_ZC_URL,
        changeOrigin: true,
        pathRewrite: {
          ["^" + process.env.VUE_ZC_API]: ""
        }
      },
      //通用办件
      [process.env.VUE_APP_BASE_CASE_API]: {
        target: process.env.VUE_APP_BASE_CASE_URL,
        changeOrigin: true,
        pathRewrite: {
          ["^" + process.env.VUE_APP_BASE_CASE_API]: ""
        }
      },
      //case_service 业务接口
      [process.env.VUE_APP_BUSINESS_CASE_API]: {
        target: process.env.VUE_APP_BUSINESS_CASE_URL,
        changeOrigin: true,
        pathRewrite: {
          ["^" + process.env.VUE_APP_BUSINESS_CASE_API]: ""
        }
      },
      //事项
      [process.env.VUE_APP_SERVICE_API]: {
        target: process.env.VUE_APP_SERVICE_URL,
        changeOrigin: true,
        pathRewrite: {
          ["^" + process.env.VUE_APP_SERVICE_API]: ""
        }
      },
      //一件事事项
      [process.env.VUE_APP_THING_API]: {
        target: process.env.VUE_APP_BASE_API_CONNECT,
        changeOrigin: true,
        pathRewrite: {
          ["^" + process.env.VUE_APP_THING_API]: ""
        }
      },
      //统计分析
      [process.env.VUE_APP_STATISTIC_API]: {
        target: `http://10.22.19.40:9095`,
        changeOrigin: true,
        pathRewrite: {
          ["^" + process.env.VUE_APP_STATISTIC_API]: ""
        }
      },
      [process.env.VUE_APP_BASE_API]: {
        //target: process.env.VUE_APP_BASE_API_CONNECT,
        // target: `http://121.36.164.20:8088/`, // 正式环境
        // target: `http://127.0.0.1:8088/`, // 开发环境
        target: `http://172.21.178.38:8088/`,//黄埔线上的
        changeOrigin: true,
        pathRewrite: {
          ["^" + process.env.VUE_APP_BASE_API]: ""
        }
      },
      // 电子表单插件内部代理
      [process.env.VUE_APP_FORM_API]: {
        //target: process.env.VUE_APP_BASE_API_CONNECT,
        // target: `http://121.36.164.20:8088/`, // 正式环境
        // target: `http://127.0.0.1:8088/`, // 开发环境
        target: `http://172.21.178.38:8088/`,//黄埔线上的
        changeOrigin: true,
        pathRewrite: {
          ["^" + process.env.VUE_APP_FORM_API]: ""
        }
      },
      [process.env.VUE_APP_IMAGE_API]: {
        target: `http://139.9.123.180`,
        changeOrigin: true,
        pathRewrite: {
          ["^" + process.env.VUE_APP_IMAGE_API]: ""
        }
      }
    },
    disableHostCheck: true
  },
  css: {
    loaderOptions: {
      // 给scss-loader传递选项
      sass: {
        additionalData: `@import "~@/assets/styles/mixin/index.scss";`
      }
    }
  },
  configureWebpack: {
    devtool:
      process.env.NODE_ENV == "production"
        ? "source-map"
        : "cheap-module-eval-source-map",
    name: name,
    resolve: {
      alias: {
        "@": resolve("src")
      }
    },
    plugins: plugins
  },
  chainWebpack(config) {
    config.plugins.delete("preload"); // TODO: need test
    config.plugins.delete("prefetch"); // TODO: need test

    // set svg-sprite-loader
    config.module
      .rule("svg")
      .exclude.add(resolve("src/assets/icons"))
      .end();
    config.module
      .rule("icons")
      .test(/\.svg$/)
      .include.add(resolve("src/assets/icons"))
      .end()
      .use("svg-sprite-loader")
      .loader("svg-sprite-loader")
      .options({
        symbolId: "icon-[name]"
      })
      .end();

    // set preserveWhitespace
    config.module
      .rule("vue")
      .use("vue-loader")
      .loader("vue-loader")
      .tap(options => {
        options.compilerOptions.preserveWhitespace = true;
        return options;
      })
      .end();

    config.when(process.env.NODE_ENV !== "development", config => {
      config
        .plugin("ScriptExtHtmlWebpackPlugin")
        .after("html")
        .use("script-ext-html-webpack-plugin", [
          {
            // `runtime` must same as runtimeChunk name. default is `runtime`
            inline: /runtime\..*\.js$/
          }
        ])
        .end();
      config.optimization.splitChunks({
        chunks: "all",
        cacheGroups: {
          libs: {
            name: "chunk-libs",
            test: /[\\/]node_modules[\\/]/,
            priority: 10,
            chunks: "initial" // only package third parties that are initially dependent
          },
          elementUI: {
            name: "chunk-elementUI", // split elementUI into a single package
            priority: 20, // the weight needs to be larger than libs and app or it will be packaged into libs or app
            test: /[\\/]node_modules[\\/]_?element-ui(.*)/ // in order to adapt to cnpm
          },
          commons: {
            name: "chunk-commons",
            test: resolve("src/components"), // can customize your rules
            minChunks: 3, //  minimum common number
            priority: 5,
            reuseExistingChunk: true
          }
        }
      });
      config.optimization.runtimeChunk("single"),
        {
          from: path.resolve(__dirname, "./public/robots.txt"), //防爬虫文件
          to: "./" //到根目录下
        };
    });

    const urlLoader = config.module.rule("images");
    // 清除已有的所有 loader。
    // 如果你不这样做，接下来的 loader 会附加在该规则现有的 loader 之后。
    urlLoader.uses.clear();
    // 添加要替换的 loader
    urlLoader
      .test(/\.(png|jpe?g|gif)(\?.*)?$/)
      .use("url-loader")
      .loader("url-loader")
      .options({
        limit: 1024 * 1, // 8k
        name: "[path][name].[ext]", // 利用[path]路径获取文件夹名称并设置文件名
        fallback: "file-loader", // 当超过8192byte时，会回退使用file-loader
        context: path.resolve(__dirname, "./src"), //过滤掉[path]的相对路径
        publicPath: "/manager", //采用根路径
        esModule: false
      });

    const fontsLoader = config.module.rule("fonts");
    fontsLoader.uses.clear();
    fontsLoader
      .test(/\.(woff2?|eot|ttf|otf)(\?.*)?$/i)
      .use("url-loader")
      .loader("url-loader")
      .options({
        limit: 4096,
        fallback: {
          loader: "file-loader",
          options: {
            name: "static/fonts/[name].[hash:8].[ext]"
          }
        },
        esModule: false
      });
  }
};
