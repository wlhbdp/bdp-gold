<template>
  <div class="header">
    <dv-decoration-10 style="height: 2px;" />
    <div class="header_title">
      <dv-decoration-8 style="width: 290px; height: 52px;" />
      <div class="header_text">{{ titleMap[currentTitle] }}</div>
      <dv-decoration-8 :reverse="true" style="width: 290px; height: 52px;" />
    </div>
    <dv-decoration-10 style="height: 2px; transform: rotateY(180deg);" />
  </div>
</template>

<script lang='ts'>
import { Vue, Component, Prop } from 'vue-property-decorator';

@Component({})
export default class Header extends Vue {
  @Prop({ default: '大屏' }) private currentTitle!: string

  private titleMap={
    'Bigscreen': '大屏',
    'DailyOrder': '每日活跃用户数',
  }
}

</script>
<style lang='less' scoped>
  .header {
    display: flex;
    justify-content: center;
    .header_title {
      display: flex;
      align-items: center;
      .header_text {
        width:100px;
        text-align: center;
        font-size: 24px;
        font-weight: bold;
        color: #fff;
      }
    }
  }
</style>