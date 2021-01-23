<template>
  <div class="bigscreen">
    <div class="left">
      <dv-border-box-7>
        <line-chart id="rule_line" width="100%" height="100%" :lineData="lineData" />
      </dv-border-box-7>
      <dv-border-box-12>近7天订单趋势</dv-border-box-12>
      <dv-border-box-10>近30天用户转化率分布</dv-border-box-10>
      <!-- <dv-border-box-12>近7天新增订单用户分类情况</dv-border-box-12> -->
    </div>
    <div class="center">
      <div class="top">
        <span>总笔数：1000</span>
        <span>总金额：100000</span>
      </div>
      <china-map id="china-map" className="china-map" width="100%" height="calc(100vh - 100px)" />
    </div>
    <div class="right">
      <dv-border-box-13>近7天订单通过客户等级分布</dv-border-box-13>
      <dv-border-box-13>近30天各渠道新用户的通过率</dv-border-box-13>
      <dv-border-box-13>近7天用户活跃趋势</dv-border-box-13>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator';
import ChinaMap from '@/components/charts/ChinaMap.vue';
import DigtialClock from './DigtialClock.vue';
import LineChart from '@/components/charts/LineChart.vue';

interface IEchartsOption {
  title: string;
  xAxisData: any[],
  legendData: any[],
  data: any[]
}

@Component({
  components: {
    ChinaMap,
    DigtialClock,
    LineChart,
  }
})
export default class Bigscreen extends Vue {
  @Prop() private msg!: string;
  private lineData: IEchartsOption = {
    title: '曲线图',
    xAxisData: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'],
    legendData: ['A商品', 'B商品', 'C商品'],
    data: [
      { name: 'A商品', sizes: [18, 36, 65, 70, 78, 40, 33] },
      { name: 'B商品', sizes: [12, 50, 51, 55, 70, 30, 20] },
      { name: 'C商品', sizes: [10, 30, 31, 50, 40, 20, 10] },
    ]
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="less">
  .bigscreen {
    color: #fff;
    display: flex;
    .left {
      flex: 1;
      display: flex;
      flex-direction: column;
    }
    .center {
      flex: 2;
      width: 100%;
      height: calc(100vh - 60px);
      .top {
        font-size: 24px;
        width: 100%;
        height: 40px;
        line-height: 40px;
        display: flex;
        justify-content: space-around;
      }
    }
    .right {
      flex: 1;
      display: flex;
      flex-direction: column;
    }
  }
</style>
