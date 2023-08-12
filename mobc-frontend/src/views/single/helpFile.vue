<template>
  <div class="help-file">
    <!-- <img
      @click.stop="onStep"
      class="step1"
      v-show="step == 1"
      src="static/assets/image/dw/xins/y1.jpg"
      alt=""
    />
    <img
      class="step2"
      v-show="step == 2"
      src="static/assets/image/dw/xins/y2.jpg"
      border="0"
      usemap="#planetmap"
      alt="Planets"
    />
    <img
      @click.stop="onStep"
      class="step3"
      v-show="step == 3"
      src="static/assets/image/dw/xins/y3.jpg"
      alt=""
    />
    <img
      @click.stop="onStep"
      class="step4"
      v-show="step == 4"
      src="static/assets/image/dw/xins/y4.jpg"
      alt=""
    />
    <img
      @click.stop="onStep"
      class="step4"
      v-show="step == 5"
      src="static/assets/image/dw/xins/y5.jpg"
      alt=""
    />
    <img
      @click.stop="onStep"
      v-show="step == 2"
      class="hands"
      src="static/assets/image/xapp/xins/left-hand.png"
      alt=""
      id="imghands"
    /> -->
    <div class="step-dom" v-show="step == 2" @click.stop="onStep"></div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      step: 1,
    }
  },
  methods: {
    onStep() {
      if (this.step == 5) {
        localStorage.setItem('_is_first_open', new Date().getTime())
        this.$router.replace('/')
      }
      this.step = this.step + 1
    },
  },
}
</script>
<style lang="less">
.help-file {
  width: 100%;
  position: relative;
  img {
    display: inline-block;
    width: 100%;
  }
  .hands {
    width: 44px;
    position: absolute;
    animation: scaleDraw 0.5s linear 0s infinite alternate;
    top: 213px;
    right: 83px;
  }
  .step-dom {
    position: absolute;
    z-index: 7;
    background: transparent;
    width: 200px;
    height: 50px;
    top: 183px;
    right: 30px;
  }

  @keyframes scaleDraw {
    0% {
      top: 213px;
    }
    100% {
      top: 233px;
    }
  }
}
</style>
