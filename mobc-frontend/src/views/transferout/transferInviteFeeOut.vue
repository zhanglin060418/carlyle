<template>
    <div>
  <mod-main :title="title" style="margin-bottom: -20px"></mod-main>
  <div class="sell-energy">
    <div class="t-box bot">
      <p class="shengyu">
        {{ $t('payDetail.text39') }}：{{ balance / 100 }}
      </p>
      <div class="reqMoney line05">
        {{ sym }}
        <input
          type="number"
          v-model.trim="reqNum"
          :placeholder="$t('dw.t193')"
        />
      </div>
      <div class="buy-box">
        <div
          class="m-box"
          :class="{ active: num == reqNum }"
          v-for="num in buyList"
          @click="chooseMoney(num)"
        >
          {{ num }}
          <van-icon name="success ok" v-if="num == reqNum" />
        </div>
      </div>
    </div>
    <div class="popup-btn" @click="sbtClick()">
     Submit
    </div>
    <top-info ref="InfoRef" @sure="sbtClick" :user="userInfo"></top-info>
    <van-dialog
        v-model="verifyPayDlgOpen"
        :title="$t('user.cpfPayment')"
        :show-cancel-button="true"
        :close-on-click-overlay="true"
        :cancelButtonText="$t('sys.cancel')"
        :confirm-button-text="$t('sys.confirm')"
        cancelButtonColor="#1fbf75"
        @confirm="verifyPaymentPassword"
    >
      <base-input
          v-model.trim="payPassword"
          type="password"
          :placeholder="$t('home.paymentPwd')"
      ></base-input>
    </van-dialog>
    <loadding v-if="isLoading"></loadding>
  </div>
    </div>
</template>
<script>
import { mapGetters, mapActions } from 'vuex'
import Vue from 'vue';
import {RadioGroup, Radio, Toast} from 'vant';

Vue.use(Radio);
Vue.use(RadioGroup);
export default {
  data() {
    return {
      title:'Reward Transfer Out',
      sym: '',
      userId: '',
      verifyPayDlgOpen: false,
      hasPayPwd: false,
      reqNum: null,
      reqMoney: null,
      isLoading: false,
      payPassword: null,
      balance: 0,
      selectChannel:1,
      channelList:[]
    }
  },
  computed: {
    ...mapGetters({
      userInfo: 'user/userInfo',
      userAccount: 'user/userAccount',
    }),
    buyList() {
      if (this.currentitem) {
        let arr = this.currentitem.selection.split(',')
        return arr.map(d => Number(d))
      }
      return [2000,5000,8000,100000, 200000,1000000]
    },
  },
  created() {
    //获取通道
    console.log('获取通道')
    this.userId = localStorage.getItem('userId') || null
    this.sym = localStorage.getItem('localCurrency') || 'NGN'
    this.hasPayPwd = localStorage.getItem('payment_password')
    this.getPaymentRule()
  },
  methods: {
    ...mapActions({
      place_order: 'user/place_order', //支付请求
      rewardTransferOut: 'user/rewardTransferOut',
      verifyPayPassword: 'user/verifyPayPassword',
      getPayRules: 'user/getPaymentRules',
      getUserBalance: 'user/getUserBalance',
    }),
    async getPaymentRule() {
      const user_id = this.userId
      let userBalance = await this.getUserBalance({
        userId: user_id
      })
      this.balance = userBalance.rewardAmt
    },
    chooseMoney(num) {
      this.reqNum = num
    },
    sbtClick() {
      this.reqMoney = this.reqNum * 100

      if(this.balance == 0){
        this.errDialog("Your balance is insufficient!")
        return
      }
      if (this.reqMoney * 1 > this.balance) {
        //提现金额不能大于
        this.errDialog(this.$t('payDetail.text3502') + ' ' + this.sym + ' '+ this.balance/100)
        return
      }
      if(this.hasPayPwd == 'false') {
        this.$dialog
            .confirm({
              title: '',
              message: this.$t('sys.setPaymentPwd'),
              cancelButtonText: this.$t('sys.cancel'),
              confirmButtonText: this.$t('sys.confirm'),
            })
            .then(() => {
              this.$router.push('/resetPayPwd')
            })
            .catch(() => {
              // on cancel
            })
      }
      else
        this.verifyPayDlgOpen = true
    },
    async verifyPaymentPassword(){
      this.isLoading = true
      this.verifyPayPassword({password: this.payPassword}).then(res =>{
        this.isLoading = false
        if(res.code == 200) {
          this.buy()
        }
        else {
          this.errDialog(this.$t('msg.failedPayment'))
          this.verifyPayDlgOpen = false
          return false
        }
      })
    },
    async buy() {
      this.isLoading = true
      const user_id = this.userId
      const formData = {
        userId: user_id,
        amount: this.reqMoney
      }
      await this.rewardTransferOut(formData).then(res =>{
        if(res.code == 200) {
          this.isLoading = false
          this.errDialog(this.$t('msg.InterestTransferSuccess'))
          this.$router.push({
            path: '/team',
          })
        }else {
          this.isLoading = false
          this.errDialog(res.msg)
          return
        }
      })
    }
  }
}
</script>

<style>
    .img-icon {
        height: 20px;
    }
</style>

