

export default class Queue {

  constructor(task) {
    this.task = task;
    this.list = [ ];
  }

  add(item) {
    let me = this;
    let list = me.list;

    list.push(item);

    if (typeof me.waiting !== 'function') {
      let waiting = function () {
        me.waiting = null;
        remove();
      };

      let remove = function () {
        if (me.list) {
          let item = list.shift();
          if (item) {
            me.waiting = waiting;
            me.task(item, waiting);
          }
        }
      };
      remove();
    }
  }

  size() {
    return this.list.length;
  }

  clear() {
    this.list.length = 0;
  }

  destroy() {
    this.list = this.waiting = null;
  }
}