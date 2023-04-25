export default {
  bind (el, binding) {
    el._clickoutside = (e) => {
      const classList = e.target.classList;
      if (e.type === "contextmenu") {
        // 判断是否在popover中
        if (el.parentElement.contains(e.target)
          && (classList.contains('content-item')
            || (classList.contains('el-image'))
            || (classList.contains('el-image__inner'))
            || (classList.contains('content-item-close'))
            || (classList.contains('content-item-preview'))
            || (classList.contains('content-item-index'))
            || (classList.contains('el-scrollbar__wrap'))
            || (classList.contains('contextmenu-node__label'))
            || (classList.contains('el-icon-caret-right'))
            || (classList.contains('el-icon-caret-left'))
            || (classList.contains('el-icon-check'))
          )) {
          return;
        }
      }
      if (!classList.contains(el)) {
        binding.value && binding.value();
      }
    }
    window.addEventListener('click', el._clickoutside);
    window.addEventListener('contextmenu', el._clickoutside);
  },

  unbind (el) {
    window.removeEventListener('click', el._clickoutside);
    window.removeEventListener('contextmenu', el._clickoutside);
  }
};
