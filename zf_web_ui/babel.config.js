

let plugins = [
  [
    "import",
    { libraryName: "ant-design-vue", libraryDirectory: "es", style: "css" }
  ],
  [
    "import",
    { libraryName: "vant", libraryDirectory: "es", style: true },
    'vant'
  ]
]
//在打包的时候，加入移除控制台输出插件
if (process.env.NODE_ENV === 'production' ) {
  plugins.push('transform-remove-console')
}

//plugins.push('transform-remove-console')


module.exports = {
  presets: ['@vue/cli-plugin-babel/preset'],
  plugins
}