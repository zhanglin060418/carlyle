<template>
  <modMain :title="$route.meta.title" class="product">
    <div class="pro-list" v-if="purchasedList && purchasedList.length">
      <ul class="income-list income-head">
        <li class="income-item">Amount</li>
        <li class="income-item income-right">Time</li>
      </ul>
      <ul class="income-list income-body" v-for="list in purchasedList">
        <li class="income-item">
          {{ currencyShape }} {{ list.amount / 100 }}</li>
        <li class="income-item income-right">{{ formatDate(list.transactionDate) }}</li>
      </ul>
      <p v-show="showMoreInfo" class="load-more" @click="loadMore(orderNo)">Load more</p>
      <p v-show="showAllInfo" class="load-all">No more</p>
    </div>
    <no-data v-else></no-data>
    <loadding v-if="isLoading"></loadding>
  </modMain>
</template>

<script>
import { mapActions, mapGetters } from "vuex";
import PullList from "../../components/pullList.vue";

export default {
  components: { PullList },
  data() {
    return {
      purchasedList: [],
      total: 0,
      sym: '',
      userId: '',
      showMoreInfo:false,
      showAllInfo:false,
      isLoading: false,
      currentPage: 0,
      pageSize: 15,
      orderNo: ''
    }
  },
  created() {
    let token = localStorage.getItem('token') || null
    if (token == null) {
      this.errDialog(this.$t('msg.loginFirst'))
      return this.$router.push("/login")
    }
    this.orderNo = this.$route.query.data
    this.userId = localStorage.getItem('userId') || null
    this.sym = localStorage.getItem("localCurrency") || 'NGN'
    this.getList()
    if (this.sym == 'NGN')
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
      getPurchaseInterestList: 'getPurchaseInterestList'
    }),
    async getList() {
      this.currentPage = 0;
      const user_id = this.userId;
      let res = await this.getPurchaseInterestList({
        orderNo: this.orderNo,
        userId:user_id,
        currentPage:0,
        pageSize:this.pageSize
      })
      this.purchasedList = res.data;
      this.showMoreInfo = true;
      if(res.data.length<this.pageSize){
        this.showAllInfo = true;
        this.showMoreInfo = false;
      }
    },
    loadMore(orderNo){
      this.isLoading = true;
      const user_id = this.userId;
      const startPage = (this.currentPage + 1) * this.pageSize;
      this.getPurchaseInterestList({orderNo: orderNo,userId:user_id, currentPage: startPage, pageSize:this.pageSize}).then(response => {
        if(response.code==200){
          if(response.data.length>0){
            for (let item of response.data) {
              this.purchasedList.push(item);
            }
            this.currentPage++;
          }
          if(response.data.length<this.pageSize){
            this.showAllInfo = true;
            this.showMoreInfo = false;
          }
        }
        this.isLoading = false;
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

  .load-more {
    width: 100%;
    display: flex;
    justify-content: center;
    height: 20px;
    font-size: 14px;
    color:  #f45e1e;
    cursor: pointer;
  }

  .load-all{
    width: 100%;
    display: flex;
    justify-content: center;
    height: 20px;
    font-size: 14px;
    color:  red;
    cursor: pointer;
  }

  .product{
    background: white;
  }

.income-list {
  background:snow;
  list-style: none;
  padding: 0px;
  margin: 0px;
  width: 100%;
  height: 1rem;
  line-height: 20px;
  border: 1px solid white;
  border-top: 0px;
  font-size: 14px;
}

.income-item {
  display: block;
  width: 40%;
  float: left;
  text-align: center;
  line-height: 1rem;
}

.income-right {
  width: 60%;
}

.income-head {
  background: #3376c4;
  border-radius:5px;
  font-weight: 500;
  border-top: 1px solid white;
  margin-top: 15px;
}


.income-body {
  background: #eaeeff;
  border-radius:5px;
  border-top: 1px solid white;;
  font-size: 14px;
  margin-top: 1px;
}

.pro-list{
  padding-bottom: 0px;
}
</style>
