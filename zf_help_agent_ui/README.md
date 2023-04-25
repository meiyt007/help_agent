## 开发

```bash
# 克隆项目
git clone http://172.168.251.18/zfsoft_big_win/source/zf_dzc_UI/-/tree/DEVELOP

# 进入项目目录
cd ruoyi-ui

#设置仓库地址
npm config  set registry http://172.168.251.67:4873

# 安装依赖
npm install

# 建议不要直接使用 cnpm 安装依赖，会有各种诡异的 bug。可以通过如下操作解决 npm 下载速度慢的问题
npm install --registry=https://registry.npm.taobao.org
#鄂尔多斯项目临时切换到互联网地址
npm set @f-render:registry=http://151m5x9562.iask.in:9058/

#  中国省市区级联数据
`npm install element-china-area-data -S`

# 启动服务
npm run dev
```

浏览器访问 http://localhost:80

## 发布

```bash
# 构建测试环境
npm run build:stage

# 构建生产环境
npm run build:prod
```