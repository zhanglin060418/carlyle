<template>
  <modMain :title="$route.meta.title" class="product">
    <div v-if="purchasedList && purchasedList.length" >
    <div class="pro-list">
      <div class="box" v-for="list in purchasedList" @click="toDetail(list.orderNo)">
        <div class="top">
          <carousel :elements="list.item.coverImages.split(',').map(item => imgBaseUrl + item)" style="width: 80px;"/>
          <div class="info">
            <p v-if="$i18n.locale=='zh_CN'" class="t">{{list.item.name}}</p>
            <p v-else style="font-weight: bold;">{{list.item.nameEn}} </p>
            <p>{{ $t('dw.t168') }}: {{parseFloat(list.amount*list.item.dailyInterest/10000).toFixed(2)}}</p>
            <p>{{ $t('dw.t185') }}: {{list.item.cycle}} {{ $t('dw.t7') }}</p>
            <p>{{ $t('dw.t180') }}: {{ parseFloat(list.totalInterest/100).toFixed(2) }}</p>
            <p v-if="list.status != 'In progress'">{{ $t('dw.t189') }}: {{ formatDate(list.endDate) }}</p>
          </div>
          <div class="r-price">
            <p class="price" style="text-align: right">{{ currencyShape }}{{ list.amount/100 }}</p>
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
      userId:'',
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
    let token = localStorage.getItem('token') || null
    if (token == null) {
      this.errDialog(this.$t('msg.loginFirst'))
      return this.$router.push("/login")
    }
    this.userId = localStorage.getItem('userId') || null
    this.sym = localStorage.getItem("localCurrency") || 'NGN'
    this.getList()
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
      const user_id = this.userId
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
    formatDate(val) {
      if (val == null)
        return
      const now = new Date(val)
      var year = now.getFullYear();       //年
      var month = now.getMonth() + 1;     //月
      var day = now.getDate();            //日

      var clock = '';
      if(day < 10)
        clock += "0";
      clock += day + "/";
      if(month < 10)
        clock += "0";
      clock += month + "/";
      clock += year
      return(clock);
    },
  }
}
</script>

<style scoped>

</style>
