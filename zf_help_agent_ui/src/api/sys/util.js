import request from '@/utils/request'
/*声明引用这个变量*/
export function importScript (path, success, error) {
  var oS = document.createElement('script')
  oS.src = path
  document.getElementsByTagName('head')[0].appendChild(oS)
  oS.onload = function () {
    success && success()
  }

  oS.onerror = function () {
    error && error()
  }
}


