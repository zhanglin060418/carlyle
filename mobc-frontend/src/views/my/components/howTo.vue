<template>
  <div class="novice-main">
    <div class="novice-list-info help1" @click="goDetail(1)">
      <h2>{{ $t('dw.t119') }}</h2>
      <!-- <img src="static/assets/image/dw/help_1.png" /> -->
    </div>
    <div class="novice-list-info help2" @click="goDetail(2)">
      <h2>{{ $t('dw.t120') }}</h2>
      <!-- <img src="static/assets/image/dw/help_2.png" /> -->
    </div>
    <div class="novice-list-info help3" @click="goDetail(3)">
      <h2>{{ $t('dw.t121') }}</h2>
      <!-- <img src="static/assets/image/dw/help_3.png" /> -->
    </div>
    <div class="novice-list-info help4" @click="goDetail(4)">
      <h2>{{ $t('dw.t122') }}</h2>
      <!-- <img src="static/assets/image/dw/help_4.png" /> -->
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      imglist: [
        {
          type: 1,
          en: require('@/assets/image/xapp/help1.png'),
          // pt: require('@/assets/image/xapp/help1.png'),
        },
        {
          type: 2,
          en: require('@/assets/image/xapp/help3.png'),
          // pt: require('@/assets/image/xapp/help4.png'),
        },
        {
          type: 3,
          en: require('@/assets/image/xapp/help2.png'),
          // pt: require('@/assets/image/xapp/help2.png'),
        },
        {
          type: 4,
          en: require('@/assets/image/xapp/help4.png'),
          // pt: require('@/assets/image/xapp/help3.png'),
        },
      ],
    }
  },
  methods: {
    goDetail(type) {
      this.$router.push({
        path: '/help',
        query: {
          type,
        },
      })
    },
  },
}
</script>
