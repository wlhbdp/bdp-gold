import echarts, { ECharts } from 'echarts';
import { Vue, Component, Prop, Watch } from 'vue-property-decorator';

@Component({
  name: 'EchartsMixins',
})

export default class extends Vue {
  [x: string]: any;
  @Prop({ default: 'chart', required: true }) public id!: string;
  public echartsData: object = {};
  protected chart!: ECharts | null;

  public mounted() {
    this.$nextTick(() => {
      this.initChart();
    });
  }
  public initChart() {
    if (!this.id) {
      return;
    }
    this.chart = echarts.init(document.getElementById(this.id) as HTMLDivElement);
    if (this.option) {
      this.updateEchartsOption();
    }
  }

  @Watch('option')
  public updateEchartsOption() {
    if (this.chart) {
      this.chart.clear();
      this.chart.setOption(this.option);
      this.initResizeEvent();
    }
  }

  public beforeDestroy() {
    this.destroyResizeEvent();
  }

  public activated() {
    this.initResizeEvent();
  }

  public deactivated() {
    this.destroyResizeEvent();
  }

  private chartResizeHandler() {
    if (this.chart) {
      this.chart.resize();
    }
  }

  private initResizeEvent() {
    if (this.chartResizeHandler) {
      window.addEventListener('resize', this.chartResizeHandler);
    }
  }

  private destroyResizeEvent() {
    if (this.chartResizeHandler) {
      window.removeEventListener('resize', this.chartResizeHandler);
    }
  }
}
