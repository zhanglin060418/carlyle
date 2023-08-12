<template>
  <modMain
    :title="$route.meta.title"
    :left-arrow="false"
    :class="$route.path == '/home' ? 'noPadding' : ''"
  >
    <div class="layout-main">
      <!-- :class="{ pbot: !isApp && isShowNav && isShowDown }" -->
      <!-- <div
      class="top-down"
      v-if="!isApp && isShowNav && isShowDown"
      @click="$utils.downApp"
    >
      <div class="close-down" @click.stop="isShowDown = false">
        <van-icon name="cross" />
      </div>
      <div>
        <img src="static/assets/image/logo.png" alt="" />
        <span>{{ $t('sa.txt279') }}</span>
      </div>
      <div class="bt">Mengunduh</div>
    </div> -->
      <!-- <div class="top-fiexd" v-if="isShowNav">
      <topTabs :tabs="headMenu" @itemClick="itemClick" />
    </div> -->
      <keep-alive>
        <router-view :key="$route.fullPath"></router-view>
      </keep-alive>
      <the-foot></the-foot>
    </div>
  </modMain>
</template>
<script>
export default {
  data() {
    return {
      headMenu: [
        {
          path: '/quotation',
          title: 'aa',
          name: 'Quotation',
        },
        {
          path: '/company',
          title: 'bb',
          name: 'Company',
        },
        {
          path: '/technique',
          title: 'cc',
          name: 'Technique',
        },
      ],
      isApp: '',
      isShowDown: true,
    }
  },
  computed: {
    isShowNav() {
      // let menus = this.headMenu.map(d => d.path)
      // return menus.includes(this.$route.path)
      return this.$route.path == '/home'
    },
  },
  created() {
    this.isApp = sessionStorage.getItem('appId')
  },
  methods: {
    itemClick(idx) {
      this.$router.push({
        path: this.headMenu[idx].path,
      })
    },
  },
}
</script>
