export default {
  init: function() {
    const AK = 'TWFBZ-ZOEKK-M2FJW-AFDJQ-25X55-PCFJ2'
    const TMap_URL = 'https://map.qq.com/api/gljs?v=1.exp&libraries=tools,service&key=' + AK + '&callback=onMapCallback'
    return new Promise((resolve, reject) => {
      // 如果已加载直接返回
      if (typeof TMap !== 'undefined') {
        resolve(TMap)
        return true
      }
      // 地图异步加载回调处理
      window.onMapCallback = function() {
        resolve(TMap)
      }

      // 插入script脚本
      const scriptNode = document.createElement('script')
      scriptNode.setAttribute('type', 'text/javascript')
      scriptNode.setAttribute('src', TMap_URL)
      document.body.appendChild(scriptNode)
    })
  }
}
