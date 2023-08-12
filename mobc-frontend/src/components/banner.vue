<template>
  <div
    class="banner"
    :style="$route.path == '/quotation' ? 'padding:0 16px;' : ''"
  >
    <van-swipe
      class="ban-swipe"
      :autoplay="4000"
      indicator-color="white"
      stop-propagation
      v-if="$route.path == '/quotation'"
    >
      <!-- <van-swipe-item>
        <img
          @click="swiperClick('/fan')"
          src="static/assets/image/xapp/xins/h-ban.jpg"
          alt=""
        />
      </van-swipe-item> -->
      <van-swipe-item>
        <!-- <img
          @click="swiperClick('/tg')"
          src="static/assets/image/xapp/xins/banner3.png"
          alt=""
        /> -->
      </van-swipe-item>
      <van-swipe-item>
        <!-- <img
          @click="swiperClick('/team')"
          src="static/assets/image/xapp/xins/banner1.jpg"
          alt=""
        /> -->
      </van-swipe-item>
      <!-- <van-swipe-item>
        <img
          @click="swiperClick('/drawLucky')"
          src="static/assets/image/xapp/xins/banner2.jpg"
          alt=""
        />
      </van-swipe-item> -->
    </van-swipe>
  </div>
</template>
<script>
import { mapGetters } from 'vuex'

export default {
  name: 'banner',
  data() {
    return {
    }
  },
  computed: {
    ...mapGetters({
      userInfo: 'user/userInfo',
    }),
  },
  methods: {
    isLogin() {
      if (!this.userInfo || !this.userInfo.username) {
        this.$router.push('/login')
        return false
      }
      return true
    },
    toTeam() {
      if (this.$route.path == '/quotation') {
        this.$router.push('/team')
      }
    },
    swiperClick(path) {
      if (path == '/tg') {
        if (this.isLogin()) {
          let _url = 'https://t.me/+qjWLk9N-uQ5lOGE1'
          if (window.webkit) {
            window.webkit.messageHandlers.openBrowser.postMessage(_url)
          } else if (window.appInterface) {
            if (typeof window.appInterface.openBrowser === 'function') {
              window.appInterface.openBrowser(_url)
            } else if (typeof window.appInterface.goToBrowser === 'function') {
              window.appInterface.goToBrowser(_url)
            } else {
              window.location.href = _url
            }
          } else {
            window.location.href = _url
          }
        }
      } else {
        this.$router.push({
          path: path,
        })
      }
    },
  },
}
</script>
