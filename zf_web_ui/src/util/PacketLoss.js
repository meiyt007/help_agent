export default class PacketLoss {
  constructor() {
    this.queue = []
  }

  addData(packets, packetsLost) {
    while (this.queue.length >= 10) {
      this.queue.shift()
    }
    this.queue.push([Number(packets), Number(packetsLost)])
  }

  getPacketLossRate() {
    let length = this.queue.length
    let result
    if (length == 0) {
      result = 0
    } else if (length < 10) {
      let all = this.queue[length - 1][0]
      let lost = this.queue[length - 1][1]
      result = lost / all
    } else if (length == 10) {
      let all = Math.abs(this.queue[length - 1][0] - this.queue[0][0])
      let lost = Math.abs(this.queue[length - 1][1] - this.queue[0][1])
      result = lost / all
    } else {
      result = 0
    }

    if (result >= 1) {
      result = 1;
    }

    if (Number.isNaN(result)) {
      result = 0
    }

    return (result * 100).toFixed(2);
  }
}