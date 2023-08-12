<template>
  <modMain :title="title" class="noPadding">
    <div class="how-img">
      <template v-if="type == 1">
        <img v-if="lang == 'en'" src="static/assets/image/how/img_pajc.png" alt="" />
        <img v-else src="static/assets/image/how/img_pajc_cn.png" alt="" />
      </template>
      <template v-else>
        <img v-if="lang == 'en'" src="static/assets/image/how/img_hbjc.png" alt="" />
        <img v-else src="static/assets/image/how/img_hbjc_cn.png" alt="" />
      </template>
    </div>
  </modMain>
</template>
<script>
export default {
  data() {
    return {
      lang: localStorage.getItem('locale') || 'en_US',
    }
  },
  computed: {
    type() {
      return this.$route.query.type || 1
    },
    title() {
      return this.type == 1 ? 'PAXFULF Tutorial' : 'Huobi Tutorial'
    },
  },
}
</script>
