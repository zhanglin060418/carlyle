<template>
  <div class="recharge-info">
    <div class="close-btn" @click="closedialog">
      <van-icon @click="closedialog" name="cross" />
    </div>
    <div class="bg"></div>
    <p class="title">{{ RecGuidelines.title }}</p>
    <div class="img-box">
      <img :src="imgUrl + RecGuidelines.urls[step - 1]" alt="" />
    </div>

    <div class="steps">
      <div v-for="item in RecGuidelines.urls.length" :class="step == item ? 'select' : ''"></div>
    </div>
    <div class="recharge-btn-box">
      <div @click="lastStep" v-if="step != 1" class="last-btn">
        Last step
      </div>
      <div @click="nextStep" :class="step == 1 ? 'btn_first' : 'next-btn'">
        {{ step == RecGuidelines.urls.length ? 'I got it!' : 'Next step' }}
      </div>
    </div>
    <div class="recharge-agree">
      <span class="agree-icon" :class="{ tongyi: isAgree }" @click="isAgree = !isAgree"></span>
      <p class="agree-txt">
        Don't remind me again next time
      </p>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      isAgree: false,
      imgUrl: localStorage.getItem('imgBaseUrl'),
      step: 2,
    }
  },
  props: {
    RecGuidelines: {
      type: Object,
      default: null,
    },
    closedig: {
      type: Function,
    },
  },
  created() {
    this.step = 1
  },
  mounted() {
    let _this = this
    if (localStorage.getItem('recguide') == 1) {
      _this.isAgree = true
    } else {
      _this.isAgree = false
    }
  },
  methods: {
    closedialog() {
      this.closedig()
    },
    lastStep() {
      if (this.step == 0) return
      this.step--
    },
    nextStep() {
      if (this.step == this.RecGuidelines.urls.length) {
        this.closedig()
        localStorage.setItem('recguide', this.isAgree ? 1 : 0)
        return
      }
      this.step++
    },
  },
}
</script>
