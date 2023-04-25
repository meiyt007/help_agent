const modulesFiles = require.context("./modules", true, /\.js$/);
const modules = modulesFiles.keys().reduce((modules, modulePath) => {
  const value = modulesFiles(modulePath); //每个模块中的内容
  modules = Object.assign(modules, value);
  return modules;
}, {});

console.log(modules);

module.exports = modules;
