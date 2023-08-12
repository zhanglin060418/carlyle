<template>
  <modMain :title="$t('sa.txt72')">
    <div class="landpage">
      <h1>Invite friends to register and receive cash rewards</h1>
      <div class="land-box land1" @click.stop="share('A')">
        <div class="btn">{{$t('sa.txt131')}}</div>
      </div>
      <div class="land-box land2" @click.stop="share('B')">
        <div class="btn">{{$t('sa.txt131')}}</div>
      </div>
      <div class="land-box land3" @click.stop="share('C')">
        <div class="btn">{{$t('sa.txt131')}}</div>
      </div>
    </div>
    <!-- <van-button type="primary" @click="share">点击分享</van-button> -->
  </modMain>
</template>
<script>
import { mapGetters } from 'vuex'
export default {
  data() {
    return {

    }
  },
  computed: {
    ...mapGetters({
      userInfo: 'user/userInfo'
    })
  },
  methods: {
    share(s = '') {
      let _link = window.config.domain + 's.html?c=' + s + this.userInfo.symbolCode
      let _content = `Segera daftarkan diri Anda dan berikan peralatan senilai 100K, penghasilan harian Rp2500, tarik tunai dalam hitungan detik.${_link}`
      if (window.webkit) {
        window.webkit.messageHandlers.shareSocial.postMessage(_content)
      } else if (window.appInterface) {
        window.appInterface.shareSocial(_content)
      } else {
        this.showDialog(_content)
      }
    }
  }
}
</script>
