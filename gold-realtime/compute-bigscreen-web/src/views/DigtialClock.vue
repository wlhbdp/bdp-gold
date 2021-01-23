<template>
  <div class="clock">
    <p class="date">{{ date }}</p>
    <p class="time">{{ time }}</p>
  </div>
</template>

<script lang='ts'>
import { Vue, Component } from 'vue-property-decorator';
@Component({})
export default class DigtalClock extends Vue {
  private time: string = '';
  private date: string = '';
  private timer!: any;
  private week: any[] = ['星期天', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'];

  private mounted () {
    this.timer = setInterval(this.updateTime, 1000)
  }
  private beforeDestroy () {
    clearInterval(this.timer)
  }
  private updateTime() {
    const now = new Date();
    this.time = `${this.zeroPadding(now.getHours(), 2)}:${this.zeroPadding(now.getMinutes(), 2)}:${this.zeroPadding(now.getSeconds(), 2)}`;
    this.date = `${this.zeroPadding(now.getFullYear(), 4)}-${this.zeroPadding(now.getMonth()+1, 2)}-${this.zeroPadding(now.getDate(), 2)} ${this.week[now.getDay()]}`;
  }
  private zeroPadding(num: number, digit: number) {
    let zero = '';
    for (var i = 0; i < digit; i++) {
      zero += '0'
    }
    return (zero + num).slice(-digit)
  }
}

</script>
<style lang='less' scoped>
  .clock {
    color: #fff;

  }
</style>