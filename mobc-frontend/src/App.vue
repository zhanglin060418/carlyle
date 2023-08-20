<template>
  <div id="app">
    <div class="top-meng"></div>
    <keep-alive>
      <router-view v-if="$route.meta.keepAlive"></router-view>
    </keep-alive>
    <router-view v-if="!$route.meta.keepAlive"></router-view>
    <!-- 公用弹出层 -->
    <result-popup ref="showResult" :pData="pData" />

    <!-- <van-popup v-model="showNotic" @closed="closeNotic" class="popup-result limit-rule notic" :close-on-click-overlay="false" style="height:350px">
      <div class="content">
        <img class="abs hg" src="static/assets/image/hg.png" alt="" />
        <img class="abs nbot" src="static/assets/image/n_bot.png" alt="" />
        <div class="poput-rule">
          <h2>{{ $t('dw.t150') }}</h2>
          <p>Karena jumlah pengguna bertambah terlalu cepat, server perlu ditingkatkan dan mungkin tidak tersedia untuk sementara. Harap tunggu, ini akan memakan waktu sekitar 2 jam. Mohon bersabar atas ketidaknyamanan yang dialami semua orang.</p>
        </div>
      </div>
    </van-popup> -->
  </div>
</template>

<script>
import {mapActions, mapGetters, mapMutations} from 'vuex'
import { bus } from '@/utils/bus'
import sellBuyMixins from './mixins/sellBuy'
export default {
  name: 'App',
  // mixins: [sellBuyMixins],
  data() {
    return {
      showNotic: true,
      show: false,
      showSell: false,
      personData: null,
      showUpgraded: true,
      pData: null,
      sellBuyData: null,
      inputPrice: '',
    }
  },
  watch: {
    inputPrice(newval, olval) {
      this.sellData.price = newval.toString().replace(/,/gi, '')
    },
  },
  async created() {
    bus.$off('openDialog')
    bus.$on('openDialog', (type = 0) => {
      let d = {
        btnTxt: this.$t('btn.t14'),
        contentTxt: this.$t('dw.t62'),
        type: type,
      }
      if (type == 1) {
        d.btnTxt = this.$t('btn.t15')
        d.contentTxt = this.$t('dw.t63')
      } else if (type == 2) {
        d.btnTxt = this.$t('btn.t10')
        d.contentTxt = this.$t('dw.t64')
      } else if (type == 3) {
        d.btnTxt = this.$t('btn.t16')
        d.contentTxt = this.$t('dw.t65')
      } else if (type == 4) {
        d.btnTxt = this.$t('btn.t10')
        d.contentTxt = this.$t('dw.t66')
      } else if (type == 5) {
        d.btnTxt = this.$t('btn.t35')
        d.contentTxt = this.$t('dw.t631')
      }
      this.pData = d
      this.$refs.showResult.show = true
    })
    bus.$off('openBuyOrSell')
    bus.$on('openBuyOrSell', data => {
      this.showSell = true
      this.sellBuyData = data
    })
    let token = localStorage.getItem('token')
    if(token != null) {
      let userData = await this.getUserData()
      this.updateUser(userData)
    }
  },
  methods: {
    ...mapActions('user', {
      getUserData: 'getUserData'
    }),
    ...mapMutations({
      updateUser: 'user/UPDATE_USER'
    }),
    closeLimited() {
      console.log('关闭抢购弹出层')
    },
    closeSellPop() {
      this.showSuccess = false
      this.sellBuyData = null
      this.sellData = {
        orderId: null,
        price: null,
      }
    },
    onInputPrice(e) {
      let dom = e.target
      let num = dom.value || ''
      var numStr = num.toString()
      numStr = numStr.replace(/,/gi, '')
      dom.value = numStr.replace(/(\d)(?=(?:\d{3})+$)/g, '$1,')
    },
  },
}
</script>
