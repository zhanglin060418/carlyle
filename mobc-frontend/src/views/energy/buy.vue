<template>
  <div class="sell-energy">
    <!-- <div class="content">
      <template v-if="buyList && buyList.length">
        <div
          :key="idx"
          v-for="(item, idx) in buyList"
          class="list-box"
          @click="chooseMoney(item)"
        >
          <div class="top">
            <img
              :src="require('@/assets/image/dw/buy/jb_' + idx + '.png')"
              alt=""
            />
            <p>x{{ $utils.getJt(item) }}</p>
          </div>
          <div class="bot-btn">{{ sym }}{{ $utils.getJt(item) }}</div>
        </div>
      </template>
      <loadding v-if="isLoading" />
      <no-data v-else-if="!isLoading && buyList.length == 0"></no-data>
    </div> -->
    <div class="t-box bot">
      <div class="reqMoney line05">
        {{ sym }}
        <input
          type="number"
          id="inputMoney"
          v-model.trim="reqNum"
          :placeholder="$t('dw.t193')"
        />
      </div>
<!------- Fee ------>
<!--       <div class="fee-box line05">
        <p>
          {{ $t('bill.fee') }}：<span class="red">{{ fee }} </span
          ><span v-if="reqMoney"> ({{ reqNum }}*10%={{ fee }})</span>
        </p>
        <p>
          {{ $t('payDetail.text38') }}：<span class="red">{{ reqNum * 0.9}}</span>
        </p>
      </div>-->
       <p class="shengyu">
        {{ $t('payDetail.text39') }}：{{ balance / 100 }}
      </p>


<!--        <p style="margin-bottom: 10px">-->
<!--        <van-button round type="info" :icon="opaylogo"  color="rgb(29, 201, 155)" size="small" @click="chooseChannel(opay)">oPay PayCom</van-button>-->
<!--        <van-button round type="info" :icon="wemalogo"  color="#9A0D82" size="small"  @click="chooseChannel(opay)">Wema Bank PLC</van-button>-->
<!--        </p>-->

        <van-radio-group direction="horizontal" v-model="selectChannel" style="margin-bottom: 10px">
<!--            <van-radio name="opay" checked-color="rgb(29, 201, 155)">-->
<!--                    <img class="img-icon" :src="opaylogo" />oPay PayCom-->
<!--            </van-radio>-->
<!--            <van-radio name="wema" checked-color="#9A0D82">-->
<!--                   <img class="img-icon" :src="wemalogo" />Wema Bank PLC-->
<!--            </van-radio>-->


            <van-radio v-for="option in channelList"
                       :key="option.channelId" :name="option.channelId" :value="option.channelId" checked-color="#ffb13d"
                       style="border: 1px solid #ffb13d;padding: 5px;border-radius: 5px;padding-bottom: 8px">
               <label style="font-size: 14px;background-color: white;height: 25px;padding: 7px">
                   <img class="img-icon" :src="imgBaseUrl+option.displayLogo" style="width: 25px;height: 25px;vertical-align: bottom" />
                   {{ option.displayName }}
               </label>
            </van-radio>
        </van-radio-group>

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
    <div class="popup-btn" @click="buyClick()">
      {{ $t('btn.t27') }} {{ sym }} {{ ' ' }} {{ reqNum }}
    </div>
    <top-info ref="InfoRef" @sure="buyClick" :user="userInfo"></top-info>
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
</template>
<script>
import topInfo from './info'
import { mapGetters, mapActions } from 'vuex'
import Vue from 'vue';
import {RadioGroup, Radio, Toast} from 'vant';

Vue.use(Radio);
Vue.use(RadioGroup);
export default {
  components: {
    topInfo,
  },
  data() {
    return {
      sym: '',
      verifyPayDlgOpen: false,
      hasPayPwd: false,
      reqNum: null,
      reqMoney: null,
      isLoading: false,
      payPassword: null,
      inMinAmount: 0,
      inMaxAmount: 20000000,
      balance: 0,
      wemalogo:require('@/assets/image/wema.png'),
      opaylogo:require('@/assets/image/opay.png'),
      selectChannel:1,
      channelList:[]
    }
  },
  computed: {
    ...mapGetters({
      userInfo: 'user/userInfo',
      userAccount: 'user/userAccount',
    }),
    minAmount() {
      return this.inMinAmount
    },
    maxAmount() {
      return this.inMaxAmount
    },
    fee() {
      if (this.reqNum) {
        return this.$utils.accMul(this.reqNum, 0.1, 0)
      }
      return 0
    },
    buyList() {
      if (this.currentitem) {
        let arr = this.currentitem.selection.split(',')
        return arr.map(d => Number(d))
      }
      return [3000, 5000, 10000,200000, 500000, 1000000]
    },
  },
  created() {
    //获取通道
    console.log('获取通道')
    this.sym = localStorage.getItem('localCurrency') || 'NGN'
    this.hasPayPwd = localStorage.getItem('payment_password')
    this.getPaymentRule()
    this.guangbiao()
  },
  methods: {
    ...mapActions({
      place_order: 'user/place_order', //支付请求
      recharge: 'user/recharge',
      verifyPayPassword: 'user/verifyPayPassword',
      getPayRules: 'user/getPaymentRules',
      getUserBalance: 'user/getUserBalance',
    }),
    async getPaymentRule() {
      const user_id = this.userInfo.user_id
      let userBalance = await this.getUserBalance({
        userId: user_id
      })
      this.balance = userBalance.availableAmt

      let res = await this.getPayRules()
      this.inMinAmount = res.inMinAmount || 0
      this.inMaxAmount = res.inMaxAmount || 0
      this.channelList = res.channelList;
      if(this.channelList==null && this.channelList.length==0){
          Toast("Please check bank channel!");
      }else{
          this.selectChannel = this.channelList[0].channelId;
      }
    },
    async guangbiao(){
          setTimeout(function (){
              let onFocus = document.querySelector('#inputMoney');
              onFocus.focus()
          },1000)
      },

    chooseMethod() {
      this.currentShow = true
    },
    chooseMoney(num) {
      this.reqNum = num
    },
    chooseChannel(channel) {

    },

buyClick() {
      this.reqMoney = this.reqNum * 100
      if(this.reqNum == null) {
        this.errDialog(this.$t('dw.t193'))
        return
      }
      if (this.reqMoney * 1 < this.inMinAmount) {
        //充值金额不能小于
        this.errDialog(this.$t('payDetail.text3401') + ' ' + this.sym + ' ' + this.inMinAmount/100)
        return
      }
      if (this.reqMoney * 1 > this.inMaxAmount) {
        //充值金额不能大于
        this.errDialog(this.$t('payDetail.text3501') + ' ' + this.sym + ' '+ this.inMaxAmount/100)
        return
      }
      // if(this.hasPayPwd == 'false') {
      //   this.$dialog
      //       .confirm({
      //         title: '',
      //         message: this.$t('sys.setPaymentPwd'),
      //         cancelButtonText: this.$t('sys.cancel'),
      //         confirmButtonText: this.$t('sys.confirm'),
      //       })
      //       .then(() => {
      //         this.$router.push('/resetPayPwd')
      //       })
      //       .catch(() => {
      //         // on cancel
      //       })
      // }
      // else
      //   this.verifyPayDlgOpen = true

    this.buy()
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
      const user_id = this.userInfo.user_id
      //这里需要获取当前界面的调用url避免多个域名的时候跳转回调有问题
      const domainHost = window.location.host;
      console.log("current buy domainHost:" + domainHost)
      const formData = {
        userId: user_id,
        amount: this.reqMoney,
        domain:domainHost,
        selectChannel:this.selectChannel
      }

      await this.recharge(formData).then(res =>{

        if(res.code == 200) {
          let _url = res.recharge.payInfoUrl

          if (window.webkit) {
            // window.webkit.messageHandlers.openBrowser.postMessage(_url)
            window.location.href = _url
          } else if (window.appInterface) {
            window.appInterface.openBrowser(_url)
          } else {
            window.location.href = _url
          }
          this.isLoading = false
          //this.$router.replace('/profit')
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

