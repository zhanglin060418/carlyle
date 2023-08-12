<template>
  <modMain :title="$t('sa.txt69')" class="noPadding" from="inMoney">
    <div class="buy-usdt">
      <div class="watchview" @click="watchVideo">
        <img src="static/assets/image/xapp/watch.png" alt="" /> {{ $t('sa.txt129') }}
      </div>
      <div class="deposit-main">
        <div class="deposit-ipt">
          <input
            class="ipt"
            type="text"
            v-model="usdtNum"
            @focus="focus"
            @blur="blur"
            placeholder="请输入数量"
          />
          <span>IDR</span>
        </div>
      </div>
      <div class="mid">
        <div class="hFlex pad30 box">
          <!-- <p>购买数量</p> -->
          <div class="num-box">
            <template v-for="item in disPlayList">
              <div
                @click="selectNum(item)"
                class="num-item"
                :class="{ active: item == usdtNum, 'lv-box': item >= 50 }"
              >
                <p class="default">R {{ $utils.getPstr(item) }}</p>
              </div>
            </template>
          </div>
          <div class="btn-buy" @click="buy">
            {{ $t('sa.payment') }}
          </div>
        </div>
      </div>
      <loadding v-if="isLoading" />
      <!-- <van-popup class="right-popup" v-model="showMethods" position="right" get-container="body" :style="{ width: '100%', height: '100%' }" ref="popMethods" @click-overlay="showMethods = false" safe-area-inset-top>
      <methods :list="list" :currentItem="currentitem" @checked="checked"></methods>
    </van-popup> -->
      <!-- <van-popup class="right-popup" v-model="showPayment" position="right" get-container="body" :style="{ width: '100%', height: '100%' }" ref="popMethods" @click-overlay="showPayment = false" safe-area-inset-top>
        <payment />
      </van-popup> -->
      <!-- <van-popup class="right-popup" v-model="showCpf" position="right" get-container="body" :style="{ width: '100%', height: '100%' }" ref="popMethods" @click-overlay="showCpf = false" safe-area-inset-top>
        <cpf @closeCpf="closeCpf" />
      </van-popup> -->
      <van-dialog
        v-model="showCpfInfo"
        :title="$t('user.cpfInfo')"
        show-cancel-button
        :cancelButtonText="$t('user.update')"
        :confirm-button-text="$t('sys.confirm')"
        cancelButtonColor="#1fbf75"
        @cancel="updateCpf"
        @confirm="sureCpf"
        closeOnClickOverlay
      >
        <div class="cpf-info" v-if="userInfo">
          <div class="item">
            <span>{{ $t('user.name') }}</span>
            <span>{{ userInfo.name }}</span>
          </div>
          <div class="item">
            <span>{{ $t('user.email') }}</span>
            <span>{{ userInfo.email }}</span>
          </div>
          <!-- <div class="item">
            <span>{{ $t('user.cpf') }}</span>
            <span>{{ userInfo.cpfCode || '-' }}</span>
          </div> -->
          <div class="item">
            <span>{{ $t('user.mobile') }}</span>
            <span>{{ userInfo.mobilePhone }}</span>
          </div>
        </div>
      </van-dialog>
      <!-- <van-dialog class="recharge-info-dialog" v-model="showRechargeInfo" title="" :show-confirm-button="false">
        <rechargeInfo v-if="showRechargeInfo" :closedig="closeshowRechargeInfo" :RecGuidelines="RecGuidelines" />
      </van-dialog> -->
      <van-popup v-model="showVideo">
        <div class="pop-info" style="padding:0">
          <div class="close-video" @click="showVideo = false">
            <van-icon name="clear" />
          </div>
        </div>
      </van-popup>
      <div class="video-main"></div>
      <!-- <com-service /> -->
      <div v-html="formHTML"></div>
    </div>
  </modMain>
</template>
<script>
import { mapActions, mapGetters, mapMutations } from 'vuex'
import cpf from './cpf'
// import payment from './payment'
import { bus } from '@/utils/bus'
export default {
  components: {
    cpf,
    // payment,
  },
  computed: {
    ...mapGetters({
      userInfo: 'user/userInfo',
    }),
    currentitem() {
      return this.$store.getters.currentPaymethod()
    },
    cssSkin() {
      return this.$store.getters.getCssSkin()
    },
    // usdtNumStr() {
    //   return this.$utils.getPstr(this.usdtNum)
    // },
    msg() {
      if (
        (this.usdtNum < this.miniLimit && this.usdtNum != 0) ||
        this.usdtNum > this.maxLimit
      ) {
        return this.buytext
      } else {
        return ''
      }
    },
    exchangeRateData() {
      if (this.exchangeRateList && this.exchangeRateList.length > 0) {
        if (this.params.reqCurrency) {
          let data = this.exchangeRateList.find(
            d => d.currency == this.params.reqCurrency
          )
          if (!data) {
            data = this.exchangeRateList[0]
          }
          return data
        } else {
          return this.exchangeRateList[0]
        }
      } else {
        return {
          currency: 'USD',
          exchangeRate: 1,
          symbol: '$',
        }
      }
    },
    reqNum() {
      // let toFixedStr = this.$options.filters['toFixedStr']
      let n = this.exchangeRateData.exchangeRate * this.usdtNum
      return n + ''.indexOf('.') > 0 ? n.toFixed(2) : n
    },
    minAmount() {
      // let minAmounts = [70, 60, 50, 40, 30, 0]
      if (this.currentitem) {
        return this.currentitem.minAmount
      }
      return 10 //minAmounts[this.userInfo.level]
    },
    disPlayList() {
      if (this.currentitem) {
        return this.currentitem.selection.split(',')
      }
      return []
    },
    currency() {
      if (this.exchangeRateData) {
        return this.exchangeRateData.currency
      }
      return ''
    },
  },
  data() {
    return {
      showVideo: false,
      RecGuidelines: null,
      isAgree: true,
      showMethods: false,
      showPayment: false,
      showCpf: false,
      showCpfInfo: false,
      isSureCpf: false,
      showRechargeInfo: false,
      usdtNum: 200,
      disabled: false,
      commonList: [],
      value: '1',
      payType: '',
      inputbuytext: '',
      buytext: '',
      miniLimit: 0,
      maxLimit: 0,
      rate: 0,
      currencySymbol: '',
      loading: false,
      merchantId: '',
      volume: '',
      volumerate: '',
      tradeList: [],
      showGg: false,
      list: [],
      showBank: false,
      itemList: null, //充值金额
      exchangeRateList: [],
      params: {
        passagewayId: '', //通道ID
        reqNum: '', //请求到账币种数量，USDT的数量
        reqCurrency: 'USD', //请求支付币种，如CNY、USD、EUR
        reqMoney: '', //请求支付币种数量
      },
      needKyc: true, //是否需要kyc认证
      isLoading: false,
      loadTimer: null,
      scrollTop: 0,
      formHTML: '',
    }
  },
  beforeRouteLeave(to, from, next) {
    if (to.path == '/paymethods') {
      this.usdtNum = 200
    }
    from.meta.keepAlive = true
    next()
  },
  created() {
    if (!this.currentitem) this.$router.go(-1)
  },
  mounted() {},
  activated() {
    this.showCpfInfo = false
  },
  methods: {
    ...mapActions({
      place_order: 'user/place_order', //支付请求
    }),
    ...mapMutations({
      setCurrentPaymethod: 'setCurrentPaymethod',
      setHtml: 'setHtml',
    }),
    watchVideo() {
      this.showVideo = true
    },
    closeshowRechargeInfo() {
      this.showRechargeInfo = false
    },
    focus() {
      this.scrollTop = document.scrollingElement.scrollTop
    },
    blur() {
      document.scrollingElement.scrollTo(0, this.scrollTop)
    },
    accAdd(num1, num2 = 0) {
      var baseNum, baseNum1, baseNum2
      try {
        baseNum1 = num1.toString().split('.')[1].length
      } catch (e) {
        baseNum1 = 0
      }
      try {
        baseNum2 = num2.toString().split('.')[1].length
      } catch (e) {
        baseNum2 = 0
      }
      baseNum = Math.pow(10, Math.max(baseNum1, baseNum2))
      return ((num1 * baseNum + num2 * baseNum) / baseNum).toFixed(2)
    },
    selectNum(item) {
      this.usdtNum = item
    },
    openshowPayment() {
      this.showPayment = true
      bus.$off('closePayment')
      bus.$on('closePayment', () => {
        this.showPayment = false
      })
    },
    // closeCpf() {
    //   this.showCpf = false
    // },
    updateCpf() {
      // this.showCpf = true
      this.$router.push({
        path: '/cpf',
      })
    },
    sureCpf() {
      this.showCpfInfo = false
      this.isSureCpf = true // CPF专享 这个点击确定才进行下一步
      this.buy()
    },
    isCpf() {
      let user = this.userInfo
      let cpfData = {
        name: '',
        mobilePhone: '',
        email: '',
      }
      Object.keys(cpfData).forEach(key => {
        cpfData[key] = user[key]
      })
      let arr = Object.keys(cpfData).filter(
        v => !cpfData[v] && typeof cpfData[v] !== 'number'
      )
      return arr.length <= 0 //true 代表认证过了
    },
    buy() {
      if (this.currentitem.payApiId == 9) {
        if (!this.isSureCpf) {
          if (!this.isCpf()) {
            this.$router.push('/cpf')
            return
          } else {
            this.showCpfInfo = true
            return
          }
        }
      }
      let param = {
        passagewayId: this.currentitem.id, //'1407344680428896256',
        reqMoney: this.usdtNum,
        reqCurrency: 'IDR',
      }

      this.isLoading = true
      this.place_order(param)
        .then(res => {
          this.isLoading = false
          // this.isSureCpf = false
          if (res && res.code == 0) {
            let obj = res.data
            if (!obj) return
            let { type, content } = obj
            if (type == 1) {
              //表示跳转支付页面
              this.$router.push({
                path: '/payiframe',
                query: {
                  src: content,
                },
              })
            } else if (type == 2) {
              //返回支付卡片信息
              // 跳转卡片信息
              sessionStorage.setItem('payData', JSON.stringify(res.data))
              // this.$router.push({ path: '/payDetail' })
            } else if (type == 4) {
              //新窗口打开充值链接 马甲包加一个跳转方法
              let _url = content
              if (window.webkit) {
                window.webkit.messageHandlers.openBrowser.postMessage(_url)
              } else if (window.appInterface) {
                window.appInterface.openBrowser(_url)
              } else {
                window.location.href = content
                // window.open(content, '_blank') // 此方法不兼容ios书签模式
                // this.$utils.openWindow(content)
              }
            } else if (type == 5) {
              this.formHTML = content
              this.$nextTick(() => {
                document.getElementById('paymentForm').submit()
              })
            } else {
              this.$router.push({ path: '/deposit' })
            }
          } else {
            this.errDialog(res.msg)
          }
          // else if (res.code == 5032) {
          //   this.dialogKyc()
          //   return
          // }
        })
        .catch(err => {
          this.isLoading = false
          this.isSureCpf = false
          this.errDialog('System abnormal')
        })
    },
    // dialogKyc() {
    //   this.$dialog
    //     .confirm({
    //       message: this.$t('dialog.kycMsg'), //'请先进行kyc认证',
    //       showCancelButton: true,
    //       confirmButtonText: this.$t('sys.goVerification'),
    //       cancelButtonText: this.$t('sys.cancel'),
    //     })
    //     .then(res => {
    //       if (window.webkit) {
    //         window.webkit.messageHandlers.goVerification.postMessage({})
    //       } else if (window.appInterface) {
    //         window.appInterface.goVerification({})
    //       } else {
    //         this.errDialog('just later.')
    //       }
    //     })
    //     .catch(() => {
    //       return
    //     })
    // },
  },
  beforeDestroy() {
    this.isLoading = false
  },
}
</script>
