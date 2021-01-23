<template>
  <div id="home">
    <dv-full-screen-container class="bg">
      <dv-loading v-if="loading">Loading...</dv-loading>
      <template v-else>
        <Header :currentTitle="curComp" />
        <div class="main">
          <transition name="component-fade" mode="out-in">
            <component :is="curComp"></component>
          </transition>
        </div>
      </template>
    </dv-full-screen-container>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from 'vue-property-decorator';
import Header from '@/components/Header.vue';
import Bigscreen from './Bigscreen.vue';
import DigtialClock from './DigtialClock.vue';

@Component({
  components: {
    Header,
    Bigscreen,
    DigtialClock,
  }
})
export default class Home extends Vue {
  private loading: boolean = true;
  private curComp: string = 'Bigscreen'

  private mounted () {
    setTimeout(() => {
      this.loading = false
    }, 2000)
    // setInterval(() => {
    //   if (this.curComp === 'Bigscreen') {
    //     this.curComp = 'DailyOrder'
    //   } else {
    //     this.curComp = 'Bigscreen'
    //   }
    // }, 3000)
  }
}
</script>

<style lang="less">
  #home {
    color: #d3d6dd;
    background-color: #000000;
    width: 100%;
    height: 100vh;
    .bg {
      box-sizing: border-box;
      padding: 8px 8px 0 8px;
      background-image: url("../assets/pageBg.png");
      background-size: cover;
      background-position: center center;
      .main {
        width: 100%;
        .component-fade-enter-active, .component-fade-leave-active {
          transition: opacity .3s ease;
        }
        .component-fade-enter, .component-fade-leave-to {
          opacity: 0;
        }
      }
    }
  }
</style>