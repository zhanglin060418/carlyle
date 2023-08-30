<template>
  <modMain :title="item.name != $t('dw.t196')? $route.meta.title : $route.meta.assetstitle">
    <div class="fund-detail" v-if="item.name != $t('dw.t196')">
      <div class="detail-head">
        <div class="top">
          <!-- <van-image lazy-load :src="'http://localhost:8080' + item.coverImages.split(',')[0]" /> -->
          <carousel :elements="item.coverImages.split(',').map(item => imgBaseUrl + item)" style="width: 111px;"/>
          <div class="title">
            <h2 v-if="$i18n.locale=='zh_CN'">{{ item.name }}</h2>
            <h2 v-else>{{ item.nameEn }}</h2>
          </div>
        </div>
        <div class="mid">
          <div class="box">
            <p><strong>{{ currencyShape }}{{ item.minimumBuy / 100 }}</strong></p>
            <p>{{ $t('dw.t190') }}</p>
          </div>
          <div class="box">
            <p><strong>{{ item.dailyInterest }}%</strong></p>
            <p>{{ $t('dw.t168') }}</p>
          </div>
          <div class="box">
            <p>
              <strong>{{ item.cycle }} <span>{{ $t('dw.t7') }}</span></strong>
            </p>
            <p>{{ $t('dw.t185') }}</p>
          </div>
          <div class="box">
            <p><strong>{{ item.copies }}</strong></p>
            <p>{{ $t('dw.t5') }}</p>
          </div>
        </div>
        <div class="pro-progress">
          <div class="progress-bar" :style="{ width: item.progress+'%' }"></div>
        </div>
      </div>
      <div class="post-content ql-snow">
        <h2 class="fund-info-title">{{ $t('dw.t191') }}</h2>
        <p class="ql-editor" v-if="$i18n.locale=='zh_CN'" v-html="item.description" />
        <p class="ql-editor" v-else v-html="item.descriptionEn" />
        <p><br style="white-space: normal;" /></p>
        <p><br /></p>
      </div>
    </div>
    <div v-if="item.onSale == 0 && item.name != $t('dw.t196') && item.type != '赠送产品'&&item.copies!=item.buyCount">
      <div class="inver-btn" @click="confirmPurchase(item)">{{ $t('dw.t47') }}</div>
    </div>
    <div class="fund-detail" v-if="item.name == $t('dw.t196')">
      <div class="detail-head">
        <div class="top">
<!--          <img src="static/assets/image/logo.png" alt=""/>-->
          <carousel :elements="item.coverImages.split(',').map(item => imgBaseUrl + item)" style="width: 111px;"/>
          <div class="title">
            <h2>Added value wallet</h2>
            <p><strong> {{ $t('dw.t168') }} :  {{ item.dailyInterest }}%</strong></p>
          </div>
        </div>
      </div>
      <div class="post-content ql-snow">
        <h2 class="fund-info-title">{{ $t('dw.t194') }}</h2>
        <p class="ql-editor" v-if="$i18n.locale=='zh_CN'" v-html="item.description" />
        <p class="ql-editor" v-else v-html="item.descriptionEn" />
        <p><br style="white-space: normal;" /></p>
        <p><br /></p>
      </div>
    </div>
    <div class="my-btn-box" style="position: fixed; bottom: 0; width: 100%; padding-right: 24px" v-if="item.name == $t('dw.t196')">
      <div class="transferIn" @click="confirmPurchase(item)">
        <p>{{ $t('btn.t32') }}</p>
      </div>
      <div class="transferOut" @click="confirmWithdraw(item)">
        <p>{{ $t('btn.t33') }}</p>
      </div>
    </div>
  </modMain>
</template>
<script>
import { mapActions, mapGetters} from "vuex";
import ModMain from "../../components/modMain.vue";
import currency from "../single/currency.vue";

export default {
  components: {ModMain},
  data() {
    return {
      sym: '',
      currencyShape: '',
      item: null,
      purchasedList: [],
      dailyInterest: 0,
      hasPayPwd: false,
      loading: false,
      queryParams: {
        dailyInterest: null,
        cycle: null,
        minimumBuy: null,
        maximumBuy: null,
        fundType: null,
        progress: null,
        totalFund: null,
        currFund: null,
        status: null
      },
    }
  },
  created() {
    this.item = JSON.parse(this.$route.query.data)
    this.sym = localStorage.getItem('localeCurrency') || 'NGN'
    this.hasPayPwd = localStorage.getItem('payment_password')
    if(this.sym == 'NGN')
      this.currencyShape = '₦'
    else this.currencyShape = '¥'
  },
  computed: {
    currency() {
      return currency
    },
    ...mapGetters({
      userInfo: 'user/userInfo',
    }),
  },
  methods: {
    ...mapActions('user', {
      getPurchaseHistory: 'getPurchaseHistory',
    }),
    async confirmPurchase(data) {
        this.$router.push({
          path: '/confirmpurchase',
          query: {
            data: JSON.stringify(data),
          },
        })
    },
    confirmWithdraw(data) {
      this.$router.push({
        path: '/confirmpurchase',
        query: {
          data: JSON.stringify(data),
          type: 'withdraw'
        },
      })
    },
  }
}
</script>
