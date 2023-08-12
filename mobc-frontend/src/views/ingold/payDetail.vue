<template>
  <modMain>
    <textarea id="input"></textarea>
    <div class="charge-info">
      <div class="title">
        <!-- <div class="ico"></div> -->
        <span>请通过 </span> <span> 手机银行/支付宝 </span>
        <span> 转账到如下商户的银行账户</span>
      </div>
      <ul>
        <li class="no-border" v-if="payData.amount">
          <p>{{ $t('payDetail.text12') }}：</p>
          <p class="pay-amount">
            ￥<span>{{ payData.amount }}</span>
          </p>
          <div
            type="hollow"
            class="button"
          >
            {{ $t('payDetail.text3') }}
          </div>
        </li>
        <li class="mini">
          <p class="tips">
            请复制正确带小数点金额，否则不到账，损失由客户自行承担!
          </p>
        </li>
        <li>
          <p>{{ $t('payDetail.text2') }}：</p>
          <p>{{ payData.xingming ? payData.xingming : '' }}</p>
          <div
            type="hollow"
            class="button"
          >
            {{ $t('payDetail.text3') }}
          </div>
        </li>
        <li>
          <p>{{ $t('payDetail.text4') }}：</p>
          <p>{{ payData.bank }}</p>

          <div
            type="hollow"
            class="button"
          >
            {{ $t('payDetail.text3') }}
          </div>
        </li>
        <li>
          <p>{{ $t('payDetail.text5') }}：</p>
          <p>{{ payData.bank_no }}</p>

          <div
            type="hollow"
            class="button"
          >
            {{ $t('payDetail.text3') }}
          </div>
        </li>
        <li v-if="payData.bankofdeposit">
          <p>{{ $t('payDetail.text6') }}：</p>
          <p>{{ payData.bankofdeposit }}</p>
          <div
            type="hollow"
            class="button"

          >
            {{ $t('payDetail.text3') }}
          </div>
        </li>
        <li v-if="payData.postscript && payData.postscript != ''">
          <p>{{ $t('payDetail.text13') }}：</p>
          <p>{{ payData.postscript }}</p>
          <div
            type="hollow"
            class="button"
          >
            {{ $t('payDetail.text3') }}
          </div>
        </li>

        <!-- <li><p class="red">（请按照实际金额支付，避免无法匹配到账）</p></li> -->
      </ul>
      <p v-if="payData.postscript" class="tip">
        未填写附言、汇款金额不正确、使用支付宝、微信皆会造成延迟到帐，若有以上情形，请主动联络客服！
      </p>
    </div>
  </modMain>
</template>
<script>
export default {
  data() {
    return {
      // corName: "广州欧子贸易有限公司",
      // cardName: "中国建设银行广州市广元西路支行",
      // cardId: "44050149070600000984",
      // name: "何飞信",

      payData: {
        xingming: '',
        productname: '',
        bkname: '',
        bkno: '',
        amount: '',
        acctname: '',
        postscript: '',
      },
      isAndroid: false,
      isIOS: false,
    }
  },
  created() {
    // this.getPayList();
    let data = JSON.parse(sessionStorage.getItem('payData'))
    if (data && data.postscript) {
      // 卓越支付
      this.payData = data
      let {
        productname,
        bkname,
        bkno,
        amount,
        acctname,
        postscript,
      } = this.payData
      this.payData.xingming = acctname
      this.payData.bank = bkname
      this.payData.bank_no = bkno
      this.payData.amount = amount
      this.payData.postscript = postscript
    }
    var u = navigator.userAgent,
      app = navigator.appVersion
    this.isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1 //g
    this.isIOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/) //ios终端
  },
  methods: {
    andCopy(val) {
      if (window.appInterface) {
        window.appInterface.copyCurrencyAddress(val)
      }
    },
    copy(val) {
      this.$vux.toast.text(this.$t('payDetail.text10'), 'middle')
    },
  },
}
</script>

<style >
</style>
