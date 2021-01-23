<template>
  <div
    :id="id"
    :class="className"
    :style="{ height: height, width: width }"
  ></div>
</template>

<script lang="ts">
import { Vue, Component, Mixins, Prop, Watch } from "vue-property-decorator";
import EchartsMixins from "./mixins/resize";
import echarts from "echarts";
// import { IEchartsOption } from "@/share/type";
interface IEchartsOption {
  title: string;
  xAxisData: any[],
  legendData: any[],
  data: any[]
}

const colorList = [
  [
    "rgba(137, 189, 27, 0.3)",
    "rgba(137, 189, 27, 0)",
    "rgb(137,189,27)",
    "rgba(137,189,27,0.2)",
  ],
  [
    "rgba(219, 50, 51, 0.3)",
    "rgba(219, 50, 51, 0)",
    "rgb(219,50,51)",
    "rgba(219,50,51,0.2)",
  ],
  [
    "rgba(0, 136, 212, 0.3)",
    "rgba(0, 136, 212, 0)",
    "rgb(0, 136, 212)",
    "rgba(0, 136, 212, 0.2)",
  ],
  [
    "rgba(188, 19, 228, 0.3)",
    "rgba(188, 19, 228, 0)",
    "rgb(188, 19, 228)",
    "rgba(188, 19, 228, 0.2)",
  ],
  [
    "rgba(230, 215, 30, 0.3)",
    "rgba(230, 215, 30, 0)",
    "rgb(230, 215, 30)",
    "rgba(230, 215, 30, 0.2)",
  ],
  [
    "rgba(255, 165, 0, 0.3)",
    "rgba(255, 165, 0, 0)",
    "rgb(255, 165, 0)",
    "rgba(255, 165, 0, 0.2)",
  ],
  [
    "rgba(63, 222, 232, 0.3)",
    "rgba(63, 222, 232, 0)",
    "rgb(63, 222, 232)",
    "rgba(63, 222, 232, 0.2)",
  ],
  [
    "rgba(27, 19, 214, 0.3)",
    "rgba(27, 19, 214, 0)",
    "rgb(27, 19, 214)",
    "rgba(27, 19, 214, 0.2)",
  ],
  [
    "rgba(236, 6, 123, 0.3)",
    "rgba(236, 6, 123, 0)",
    "rgb(236, 6, 123)",
    "rgba(236, 6, 123, 0.2)",
  ],
  [
    "rgba(231,187,235, 0.3)",
    "rgba(231,187,235, 0)",
    "rgb(231,187,235)",
    "rgba(231,187,235, 0.2)",
  ],
  [
    "rgba(81,63,88, 0.3)",
    "rgba(81,63,88, 0)",
    "rgb(81,63,88)",
    "rgba(81,63,88, 0.2)",
  ],
  [
    "rgba(19,229,61, 0.3)",
    "rgba(19,229,61, 0)",
    "rgb(19,229,61)",
    "rgba(19,229,61, 0.2)",
  ],
  [
    "rgba(188,66,87, 0.3)",
    "rgba(188,66,87, 0)",
    "rgb(188,66,87)",
    "rgba(188,66,87, 0.2)",
  ],
  [
    "rgba(30,95,57, 0.3)",
    "rgba(30,95,57, 0)",
    "rgb(30,95,57)",
    "rgba(30,95,57, 0.2)",
  ],
  [
    "rgba(5,215,191, 0.3)",
    "rgba(5,215,191, 0)",
    "rgb(5,215,191)",
    "rgba(5,215,191, 0.2)",
  ],
  [
    "rgba(255,185,15, 0.3)",
    "rgba(255,185,15, 0)",
    "rgb(255,185,15)",
    "rgba(255,185,15, 0.2)",
  ],
  [
    "rgba(58,95,205, 0.3)",
    "rgba(58,95,205, 0)",
    "rgb(58,95,205)",
    "rgba(58,95,205, 0.2)",
  ],
  [
    "rgba(85,107,47, 0.3)",
    "rgba(85,107,47, 0)",
    "rgb(85,107,47)",
    "rgba(199,186,243, 0.2)",
  ],
  [
    "rgba(25,25,112, 0.3)",
    "rgba(25,25,112, 0)",
    "rgb(25,25,112)",
    "rgba(25,25,112, 0.2)",
  ],
  [
    "rgba(105,105,105, 0.3)",
    "rgba(105,105,105, 0)",
    "rgb(105,105,105)",
    "rgba(105,105,105, 0.2)",
  ],
  [
    "rgba(0,229,238, 0.3)",
    "rgba(0,229,238, 0)",
    "rgb(0,229,238)",
    "rgba(0,229,238, 0.2)",
  ],
];

@Component({
  name: "LineChart",
})
export default class extends Mixins(EchartsMixins) {
  private get option() {
    return {
      title: {
        text: this.echartsData.title,
        textStyle: {
          color: "#606266",
          fontSize: 16,
        },
      },
      tooltip: {
        trigger: "axis",
      },
      legend: {
        type: "scroll",
        top: 5,
        icon: "rect",
        itemWidth: 14,
        itemHeight: 5,
        itemGap: 13,
        data: this.echartsData.legendData,
        right: "4%",
        textStyle: {
          fontSize: 12,
          color: "#606266",
        },
      },
      grid: {
        left: 10,
        right: 25,
        bottom: 20,
        top: 50,
        containLabel: true,
      },
      xAxis: [
        {
          type: "category",
          boundaryGap: false,
          data: this.echartsData.xAxisData,
        },
      ],
      yAxis: [
        {
          type: "value",
          name: "件数",
          axisTick: {
            show: false,
          },
          axisLabel: {
            margin: 10,
            textStyle: {
              fontSize: 14,
            },
          },
        },
      ],
      series: this.echartsData.data.reduce(
        (total: any[], item: any, curIndex: number) => {
          total.push({
            name: item.name,
            type: "line",
            smooth: true,
            symbol: "circle",
            symbolSize: 5,
            showSymbol: false,
            lineStyle: {
              normal: {
                width: 1,
              },
            },
            areaStyle: {
              normal: {
                color: new echarts.graphic.LinearGradient(
                  0,
                  0,
                  0,
                  1,
                  [
                    {
                      offset: 0,
                      color: colorList[curIndex][0],
                    },
                    {
                      offset: 0.8,
                      color: colorList[curIndex][1],
                    },
                  ],
                  false
                ),
                shadowColor: "rgba(0, 0, 0, 0.1)",
                shadowBlur: 10,
              },
            },
            itemStyle: {
              normal: {
                color: colorList[curIndex][2], // lineColor
                borderColor: colorList[curIndex][3],
                borderWidth: 12,
              },
            },
            data: item.sizes,
          });
          return total;
        },
        []
      ),
    };
  }
  @Prop({ required: true }) public lineData: any;

  public echartsData: IEchartsOption = {
    title: "",
    xAxisData: [],
    legendData: [],
    data: [],
  };
  @Prop({ default: "chart" }) private className!: string;
  @Prop({ default: "200px" }) private width!: string;
  @Prop({ default: "200px" }) private height!: string;
  @Prop({ default: "" }) private title!: string;

  @Watch("lineData", { immediate: true, deep: true })
  private onLineDataChange(newValue: IEchartsOption) {
    try {
      this.echartsData = newValue;
      this.updateEchartsOption();
    } catch (error) {
      this.echartsData = {
        title: "",
        xAxisData: [],
        legendData: [],
        data: [],
      };
      this.updateEchartsOption();
    }
  }
}
</script>
<style lang="scss" scoped></style>
