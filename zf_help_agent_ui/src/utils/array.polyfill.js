/**
 * @Author shmao
 * @Date 2021/10/28
 */

Array.prototype.at = function (index) {
    var k = index >= 0 ? index : this.length + index;
    return (k < 0 || k >= this.length) ? undefined : this[k];
}