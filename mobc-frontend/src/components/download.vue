<template>
  <div class="share-info">
    <div class="download" @click="downLoad">
      {{ $t('sa.txt165') }}
    </div>
    <p class="already" v-if="$route.path == '/signin-success'" @click="$router.push('/login')">
      {{ $t('sa.txt166') }}
    </p>
    <!-- <div class="apps">
      <img src="static/assets/image/ios.png" alt="" @click="downLoad" />
      <img src="static/assets/image/android.png" alt="" @click="downLoad" />
    </div> -->
  </div>
</template>
<script>
export default {
  data() {
    return {}
  },
  methods: {
    downLoad() {
      console.log("invke downApp()")
      this.$utils.downApp()
    },
  },
}
</script>
