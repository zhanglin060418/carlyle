<template>
  <div class="inout-details">
    <div class="top-money line05">
<!--      {{ $utils.getkStr(detail.reqNum || detail.realMoney) }}
      {{ detail.targetCurrency }}-->
      {{ sym }}
      {{ getTotalAmount(detail) }}
    </div>
    <div class="info-box ">
      <p class="tip">
        {{
          type == 1 ? $t('deposit.depositMethod') : $t('deposit.withdrawMethod')
        }}
      </p>
      <!-- <p class="value">{{ cashText(detail) }}</p> -->
      <p class="value">{{ type == 1 ? $t('btn.t20') : $t('btn.t19') }}</p>
    </div>
    <div class="info-box">
      <p class="tip">{{ $t('bill.status') }}</p>
      <p class="value">{{ stateText(detail.status) }}</p>
    </div>
     <div class="info-box" v-if="type == 2">
      <p class="tip">{{ $t('bill.fee') }}</p>
      <p class="value">{{ detail.fee/100 || '-' }}</p>
    </div>
     <div class="info-box">
      <p class="tip">{{ $t('bill.date') }}</p>
      <p class="value">{{ parseTime(detail.createTime) }}</p>
    </div>
    <div class="info-box">
      <p class="tip">{{ $t('bill.orderNo') }}</p>
      <p class="value">{{ detail.requestNo }}</p>
    </div>

    <div>
      <div class="payInfoUrl" v-if="detail.status == 'Pending' && type == 1" style="width: 70px;" @click="checkPaymentMethos">{{ $t('dw.t201') }}</div>
    </div>

    <div class="info-box" v-if="type == 2">
      <p class="tip">{{ $t('bill.remark') }}</p>
      <p class="value">{{ detail.remark || '-' }}</p>
    </div>
  </div>
</template>
<script>
import {parseTime} from "../../utils/parseTime";

export default {
  props: {
    detail: {
      type: Object,
      default: null,
    },
    // type: {
    //   type: Number,
    //   default: 1,
    // },
  },
  data() {
    return {
      sym: ''
      // detail: null,
    }
  },
  computed: {
    type() {
      return this.$route.query.type || 1
    },
    title() {
      return this.type == 1
        ? this.$t('bill.deposidesc')
        : this.$t('bill.withdrawdesc')
    },
  },
  created() {
    // this.detail = JSON.parse(this.$route.query.detail)
    this.sym = localStorage.getItem('localCurrency') || 'NGN'
  },
  methods: {
    parseTime,
    cashText({ cashType, payMethodName }) {
      switch (cashType) {
        case 'PAYMENT_COIN':
          return this.$t('bill.PAYMENTCOIN') //'充币'
        case 'WITHDRAW_COIN':
          return this.$t('bill.WITHDRAWCOIN') //'提币'
        case 'WITHDRAW_OTC':
          return payMethodName //'提币'
      }
    },
    stateText(status) {
      switch (status) {
        case 'Pending':
          return this.$t('status.PENDING')
        case 'In progress':
          return this.$t('status.PROCESSING') //'处理中'
        case 'SUCCESSFUL':
          return this.$t('status.SUCCESS')
        case 'Completed':
          return this.$t('status.SUCCESS') //'支付成功'
        case 'Failed':
          return this.$t('status.FAIL') //'支付失败'
        case 'Declined':
          return this.$t('status.DECLINED') //'支付失败'
      }
    },
    checkPaymentMethos() {
      let _url = this.detail.payInfoUrl
      if (window.webkit) {
        window.webkit.messageHandlers.openBrowser.postMessage(_url)
      } else if (window.appInterface) {
        window.appInterface.openBrowser(_url)
      } else {
        window.location.href = _url
      }
    },
    getTotalAmount(data) {
      let currentAmount = data.amount
      return currentAmount/100
    },
  },
}
</script>
