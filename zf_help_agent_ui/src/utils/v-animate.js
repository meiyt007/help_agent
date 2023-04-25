import Vue from "vue";
// 全局指令点击添加动画的指令
const animate = Vue.directive("animate", {
  inserted: (el, binding) => {
    let timer = null;
    el._animate = (e) => {
      e.stopPropagation();
      let cls =
        binding.value && binding.value.class
          ? binding.value.class
          : "bounceAnimate";
      if (el.classList.contains(cls)) {
        el.classList.remove(cls);
      } else {
        el.classList.add(cls);
        if (timer) {
          clearTimeout(timer);
          timer = null;
        }
        timer = setTimeout(() => {
          el.classList.remove(cls);
        }, 400);
      }
    }
    el.addEventListener("click", el._animate, false);
  },
  unbind: (el) => {
    el.removeEventListener("click", el._animate);
  }
});

export default animate;
