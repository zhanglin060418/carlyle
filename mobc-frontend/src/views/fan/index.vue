<template>
  <!-- <modMain class="noPadding"> -->
  <div class="fans">
    <van-nav-bar
      class="fan-van-tab"
      title=""
      left-text="BACK"
      left-arrow
      @click-left="onClickLeft"
      fixed
    />
    <!-- <img src="static/assets/image/xapp/xins/fans1.jpg" alt="" /> -->
    <com-service />
  </div>
  <!-- </modMain> -->
</template>

<script>
export default {
  name: 'XAppIndex',
  methods: {
    onClickLeft() {
      let fromPath = this.$route.query.from || 'home'
      if (fromPath == 'product') {
        this.$router.push('/asset')
      } else {
        this.$router.go(-1)
      }
    },
  },
}
</script>
