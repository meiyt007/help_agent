

module.exports = {
  // title: initConfig['title'] || '基础研发平台',

  /**
   * 是否系统布局配置
   */
  showSettings: false,

  /**
   * 是否显示 tagsView
   */
  // tagsView: true,
  tagsView: localStorage ? ((localStorage.getItem("tagsView") == null) || localStorage.getItem("tagsView") == 'true') : true,

  /**
   * 是否固定头部
   */
  // fixedHeader: false,
  fixedHeader: localStorage ? (localStorage.getItem("fixedHeader") == 'true') : false,

  /**
   * 是否显示logo
   */
  // sidebarLogo: true,
  sidebarLogo: localStorage ? ((localStorage.getItem("sidebarLogo") == null) || localStorage.getItem("sidebarLogo") == 'true') : true,

    /**
   * 是否切换顶部导航栏
   */
  // navBarChange: true,
  navBarChange: localStorage ? ((localStorage.getItem("navBarChange") == null) || localStorage.getItem("navBarChange") == 'true') : true,

  /**
   * @type {string | array} 'production' | ['production', 'development']
   * @description Need show err logs component.
   * The default is only used in the production env
   * If you want to also use it in dev, you can pass ['production', 'development']
   */
  errorLog: 'production'
}
