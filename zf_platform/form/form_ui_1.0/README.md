## 开发

```bash
# 克隆项目
git clone http://172.168.251.18/dynamic_form/source/form-ui.git

# 进入项目目录
cd form-ui

# 修改npm包远程仓库地址   http://172.168.251.67:4873 合肥npm仓库地址
npm config  set registry http://172.168.251.67:4873
yarn config  set registry http://172.168.251.67:4873

# 安装依赖
yarn install

#更新某个模块
npm install --save-dev f-render@latest

# 启动服务
yarn dev
```

浏览器访问 http://localhost:80

## 发布

```bash
# 构建测试环境
yarn build:stage

# 构建生产环境
yarn build:prod
```

# 更新包版本命令
```
yarn upgrade 包名@v版本号
eg: yarn upgrade vue-ele-form@v0.8.59
```
# 包推送远程仓库命令 
```
npm publish --registry http://172.168.251.67:4873/
```