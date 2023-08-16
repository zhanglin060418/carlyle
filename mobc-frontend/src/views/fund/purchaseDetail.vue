<template>
  <modMain :title="$route.meta.title" class="product">
    <div v-if="purchasedList && purchasedList.length" >
    <div class="pro-list">
      <div class="box" v-for="list in purchasedList" @click="toDetail(list.orderNo)">
        <div class="top">
          <carousel :elements="list.item.coverImages.split(',').map(item => imgBaseUrl + item)" style="width: 80px;"/>
          <div class="info">
            <p v-if="$i18n.locale=='zh_CN'" class="t">{{list.item.name}}</p>
            <p v-else>{{list.item.nameEn}} </p>
            <p>{{ $t('dw.t168') }}: {{list.item.dailyInterest}}%</p>
            <p>{{ $t('dw.t185') }}: {{list.item.cycle}} {{ $t('dw.t7') }}</p>
            <p>{{ $t('dw.t180') }}: {{ parseFloat(list.totalInterest/100).toFixed(2) }}</p>
            <p v-if="list.status != 'In progress'">{{ $t('dw.t189') }}: {{ list.endDate }}</p>
          </div>
          <div class="r-price">
            <p class="price" style="text-align: center">{{ currencyShape }}{{ list.amount/100 }}</p>
            <div v-if="list.payBack == 0 && list.status == 'Success'" class="progress">{{ $t('dw.t198') }}</div>
            <div v-if="list.status == 'In progress'" class="wait_payment">{{ $t('dw.t199') }}</div>
            <div v-if="list.status == 'Failed'" class="failed">{{ $t('status.FAIL') }}</div>
            <div v-if="list.payBack == 1" class="pay_back">{{ $t('dw.t200') }}</div>
          </div>
        </div>
       </div>
      </div>
    </div>
    </div>
    <no-data v-else></no-data>
  </modMain>
</template>

<script>
import {mapActions, mapGetters} from "vuex";
import PullList from "../../components/pullList.vue";

export default {
  components: {PullList},
  data() {
    return {
      purchasedList: [],
      productList: [],
      total: 0,
      sym: '',
      currencyShape: '',
      params: {
        start: '',
        end: '',
        page: 1,
        size: 15,
      },
    }
  },
  created() {
    this.getList()
    this.sym = localStorage.getItem("localCurrency") || 'NGN'
    if(this.sym == 'NGN')
      this.currencyShape = '₦'
    else this.currencyShape = '¥'
  },
  computed: {
    ...mapGetters({
      userInfo: 'user/userInfo',
    }),
  },
  methods: {
    ...mapActions('user', {
      getPurchaseHistory: 'getPurchaseHistory',
      listProduct: 'listProduct'
    }),
    async getList() {
      const user_id = this.userInfo.user_id
      let res = await this.getPurchaseHistory({
        userId: user_id
      })
      this.purchasedList = res.data
    },
    toDetail(data) {
      this.$router.push({
        path: '/incomelist',
        query: {
          data: data,
        },
      })
    },
  }
}
</script>

<style scoped>

</style>
