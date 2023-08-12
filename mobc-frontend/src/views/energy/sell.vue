<template>
  <div class="sell-energy">
    <div class="t-box top">
      <span v-if="bankcards.length == 0">{{ $t('payDetail.text17') }}</span>
      <span v-else>{{ $t('payDetail.text41') }}</span>
      <template>
        <div
          class="yinhangka"
          :class="{ has: bankcards.length > 0 }"
          @click="showBank = true"
        >
          <div
            class="add-btn"
            v-if="bankcards.length == 0"
            @click.stop="toAddCard"
          >
            <van-icon name="plus" />
            {{ $t('payDetail.text17') }}
          </div>
          <div class="card-info" v-else>
             <img src="static/assets/image/dw/add-bank-l.png" alt="" />
            <div class="info">
              <p>
                {{ bankItem.bank.name }}(
                {{ bankItem.cardNo.toString().substring(0, 4) }} **** ****
                {{
                  bankItem.cardNo.toString().substring(
                    bankItem.cardNo.toString().length - 4,
                    bankItem.cardNo.toString().length
                  )
                }} )
              </p>
              <span>{{ $t('payDetail.text49') }}</span>
            </div>
          </div>
          <van-icon name="arrow" />
        </div>
      </template>
    </div>
    <div class="t-box bot">
      <p>{{ $t('payDetail.text37') }}</p>
      <div class="reqMoney line05">
        {{ sym }}
        <!-- <input type="text" v-model.trim="reqMoney" placeholder="Silakan masukkan jumlah penarikan" /> -->
        <van-field
          v-model="reqMoney"
          type="digit"
          label=""
          :placeholder="$t('payDetail.text50')"
          :border="false"
        />
      </div>
      <!------- Fee ------>
       <div class="fee-box line05">
        <p>
          {{ $t('bill.fee') }}：<span class="red">{{ fee }} </span
          ><span v-if="reqMoney"> ({{ withdrawFee }}%)</span>
        </p>
        <p>
          {{ $t('payDetail.text38') }}：<span class="red">{{ reqMoney * (100 - withdrawFee) / 100 }}</span>
        </p>
      </div>
      <p class="shengyu">
        {{ $t('payDetail.text39') }}：{{ accountBalance / 100 }}
      </p>
      <div class="desc">
        <p>1.{{ $t('sa.txt265') }}</p>
        <p>
          {{ $t('sa.txt266') }}
        </p>
        <p>2.{{ $t('sa.txt267') }}</p>
        <p style="font-weight:bold;color:#3376c4 ">
          {{ $t('sa.txt268') }}
        </p>
        <p>3.{{ $t('sa.txt269') }}</p>
        <p style="font-weight:bold;color:#3376c4 " >
          {{ $t('sa.txt270') }} {{ sym }} {{ minAmount/100}}</p>
        <p>4.{{ $t('sa.txt271') }}</p>
        <p>
          {{ $t('sa.txt272') }}
          <a href="javascript:void(0)" @click="toService()">WhatsApp</a>
          {{ $t('sa.txt273') }}
        </p>
      </div>
    </div>
    <div class="btn-sell" @click="sellClick">
      {{ $t('sa.txt138') }}
    </div>
    <van-popup
      v-model="showBank"
      round
      position="bottom"
      safe-area-inset-bottom
      class="popup-sell-buy"
      :style="{ height: '40%' }"
    >
      <div class="sell-buy popup-box energy-popup">
        <div class="head">
          <div class="title">
            {{ $t('payDetail.text41') }}
          </div>
          <!-- <img
            class="poput-close"
            src="static/assets/image/dw/close.png"
            alt=""
            @click="showBank = false"
          /> -->
        </div>
        <div class="content">
          <div class="bank-main">
            <div
              class="display-box line05"
              :class="{ active: bankItem.cardId == item.cardId }"
              v-for="item in bankcards"
              :key="item.id"
              @click="bankClick(item)"
            >
              <div class="left">
                <!-- <img src="static/assets/image/dw/add-bank-l.png" alt="" /> -->
                <div class="infos">
                  <p>{{ item.bank.name }}</p>
                  <span>
                    {{ item.cardNo.substring(0, 4) }} **** ****
                    {{
                      item.cardNo.substring(
                        item.cardNo.length - 4,
                        item.cardNo.length
                      )
                    }}</span
                  >
                </div>
              </div>
              <div class="right" v-if="item.id == bankItem.id">
                <van-icon name="checked check" />
              </div>
            </div>
            <div class="display-box line05" @click="toAddCard">
              <div class="left">
                <!-- <img src="static/assets/image/dw/add-bank-b.png" alt="" /> -->
                <div class="infos">
                  <p class="new">{{ $t('payDetail.text40') }}</p>
                </div>
              </div>
              <div class="right">
                <van-icon name="arrow" />
              </div>
            </div>
          </div>
          <div class="popup-btn lv" @click="showBank = false">
            {{ $t('sys.confirm') }}
          </div>
        </div>
      </div>
    </van-popup>
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
import { bus } from '@/utils/bus'
import { mapActions, mapGetters } from 'vuex'
import mixinsSerivce from '@/mixins/service'
import payDetail from "../ingold/payDetail.vue";
export default {
  mixins: [mixinsSerivce],
  data() {
    return {
      withdrawFee: 0,
      isLoading: false,
      sym: '',
      showBank: false,
      verifyPayDlgOpen: false,
      payPassword: '',
      reqMoney: null,
      reqNum: null,
      withdrawmoney: {
        passagewayId: '',
        bindId: '',
        fundPassword: '',
        targetCurrency: 'IDR',
        cardNo: '',
      },
      bankItem: null,
      isSubmit: false,
      bankcards: [],
      accountBalance: 0,
      minAmount: 0,
    }
  },
  computed: {
    ...mapGetters({
      userInfo: 'user/userInfo',
      userAccount: 'user/userAccount',
    }),
    fee() {
      if (this.reqMoney) {
        return this.$utils.accMul(this.reqMoney, this.withdrawFee / 100 , 0)
      }
      return 0
    },
  },
  created() {
    this.sym = localStorage.getItem('localCurrency') || 'NGN'
    this._getBankCard()
    this.getPaymentRule()
  },
  methods: {
    ...mapActions({
      getBankCard: 'bankCard/getBankCard',
      withdraw: 'user/withdraw',
      verifyPayPassword: 'user/verifyPayPassword',
      getPaymentRules: 'user/getPaymentRules',
      getUserBalance: 'user/getUserBalance',
    }),
    async getPaymentRule() {
      const user_id = this.userInfo.user_id
      let userBalance = await this.getUserBalance({
        userId: user_id
      })
      this.accountBalance = userBalance.availableAmt || 0
      let res = await this.getPaymentRules()
      this.minAmount = res.outMinAmount || 0
      this.maxAmount = res.outMaxAmount || 0
      this.withdrawFee = res.outFeeRate || 0
    },
    _getBankCard() {
      const user_id = this.userInfo.user_id
      this.getBankCard({userId: user_id}).then(res => {
        if (res.code == 200) {
          this.bankcards = res.data || []
          if (this.bankcards.length > 0) {
            this.bankClick(this.bankcards[0])
          }
        }
      })
    },
    bankClick(item) {
      this.withdrawmoney.cardNo = item.cardNo
      this.withdrawmoney.bindId = item.id
      this.bankItem = item
    },
    sellClick() {
      this.reqNum = this.reqMoney * 100
      if (this.isSubmit) return
      let minAmount = this.minAmount / 100

      if (this.withdrawmoney.cardNo == '') {
        this.errDialog(this.$t('payDetail.text31'))
        return
      }
      if (this.reqMoney == null) {
        this.errDialog(this.$t('payDetail.text50'))
        return;
      }
      if (this.reqNum * 1 < this.minAmount) {
        //充值金额不能小于
        this.errDialog(this.$t('payDetail.text34') + ' ' + this.sym + ' ' + minAmount)
        return
      }
      if(this.reqNum * 1 > this.accountBalance) {
          this.errDialog(this.$t('payDetail.text35') + ' ' + this.$t('payDetail.text39') + ' : ' + this.sym + ' ' + this.accountBalance/100)
        return
      }

      if(this.reqNum * 1 > this.maxAmount) {
        this.errDialog(this.$t('payDetail.text35') + ' ' + this.sym + ' ' + this.maxAmount/100)
        return
      }
      this.verifyPayDlgOpen = true
    },
    verifyPaymentPassword()
    {
      this.isLoading = true
      this.verifyPayPassword({password: this.payPassword}).then(res =>{
        this.isLoading = false
        if(res.code == 200) {
          this.sell()
        }
        else {
          this.errDialog(this.$t('msg.failedPayment'))
          this.verifyPayDlgOpen = false
          return false
        }
      })
    },
    sell() {
      const user_id = this.userInfo.user_id
      let params = {
        userId: user_id,
        cardId: this.bankItem.id,
        amount: Number(this.reqNum)
      }
      let _this = this
      this.isSubmit = true
      this.isLoading = true
      this.withdraw(params).then(res => {
        this.isLoading = false
        this.isSureCpf = false
        this.isSubmit = false
        if (res.code == 200) {
          _this.$router.push({
            path: 'result-success',
            query: {
              type: 2,
            },
          })
        } else {
          if (res.code == 5025) {
            this.$dialog.alert({
              title: this.$t('payDetail.text36'),
              message: this.onlineTime,
            })
          } else {
            _this.errDialog(res.msg)
          }
        }
      })
      .catch(err => {
        this.isSubmit = false
        this.errDialog(err.msg)
      })
    },
    toAddCard() {
      this.$router.push({
        path: '/addcard',
        query: {
          from: 'sell',
        },
      })
    },
  },
}
</script>
