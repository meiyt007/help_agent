module.exports = {
  // presets: ["@vue/cli-plugin-babel/preset"],
  presets: [
    ['@vue/app',
      {
        debug:true,
        // Error in mounted hook: "TypeError: 对象不支持“includes”属性或方法"
        //项目中用到的polyfill
        useBuiltIns: "entry",
        polyfills: [ 'es6.promise' , 'es6.symbol', 'es6.array.iterator', 'es6.array.find-index' , 'es7.array.includes' , 'es6.string.includes'],
      }
    ]
  ],
  plugins: [
    "@babel/plugin-proposal-optional-chaining",
    "@babel/plugin-proposal-nullish-coalescing-operator"
  ]
};
