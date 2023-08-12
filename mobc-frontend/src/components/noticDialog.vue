<template>
  <van-overlay z-index="555" :show="show" :lock-scroll="false">
    <div class="mid-dialog">
      <div class="contaner">
        <div class="title">{{title}}</div>
        <div class="content" v-html="content"></div>
        <base-btn :disabled="false" @btnClick="gotIt" :btnTitle="$t('sys.gotit')"></base-btn>
      </div>
    </div>
  </van-overlay>
</template>
<script>
export default {
  data() {
    return {
      content: '',
      title: 'Announcement',
      show: false
    }
  },
  methods: {
    gotIt() {
      this.show = false
      localStorage.setItem('indexAlert', 1)
    }
  }
}
</script>
