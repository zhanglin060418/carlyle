<template>
  <div class="product">
    <div class="card-page">
      <div class="usertop">
        <div class="top">
          <p class="totlle">{{ $t('dw.t204') }}</p>
          <h2><span style="font-size: medium"> </span>{{ parseFloat(availableAmt/100) }}</h2>
        </div>
        <div class="mid">
          <div class="box">
            <p>{{ $t('dw.t179') }}</p>
            <p class="num"><span style="font-size: small"> </span><span style="font-size:medium">{{ parseFloat(purchaseData.amount/100) }}</span></p>
          </div>
          <div class="box">
            <p>{{ $t('dw.t180') }}</p>
            <p class="num"><span style="font-size: small"></span><span style="font-size:medium">{{ parseFloat(purchaseData.totalInterest/100) }}</span></p>
          </div>
          <div class="box">
            <p>{{ $t('dw.t181') }}</p>
            <p class="num"><span style="font-size:medium"> {{ purchaseData.cycle }}</span></p>
          </div>
        </div>
        <div class="bot">
          <div class="btn-sm" @click="purchaseDetail">
            <p>{{ $t('dw.t182') }}</p>
          </div>
          <div class="btn-sm"  @click="toGo">
            <van-icon name="point-gift-o"/>
            {{ $t('dw.t183') }}
          </div>
        </div>
      </div>
    </div>
    <div class="today-earnings" style="">
      <p class="p-title line05">{{ $t('dw.t184') }}</p>
      <template v-if="newPurchase!=''">
        <vue-seamless-scroll :data="newPurchase" :class-option="classOption" class="earning-box">
            <ul class="earning-list">
              <li class="earning-item" v-for="item in newPurchase">
                <div >
                    <!-- purchase  and withdraw and recharge-->
                  <van-icon name="static/assets/image/icon/icon-addbank.png" /> User {{ item.userName.substring(0, 3) }} **** {{ item.userName.substring(item.userName.length-3, item.userName.length) }} {{item.remark}}
                    {{  currencyShape  }}{{ parseFloat(item.amount) }} now
                </div>
              </li>
            </ul>
        </vue-seamless-scroll>
      </template>
    </div>

    <van-pull-refresh
        v-model="isDownLoading"
        @refresh="onDownRefresh"
        :pullingText="pullingText"
        :loosingText="loosingText"
        :loadingText="loadingText"
    >
      <van-list
          v-if="productList && productList.length > 0"
          v-model="isUpLoading"
          :finished="upFinished"
          :finished-text="$t('sys.nomore')"
          :immediate-check="false"
          offset="20"
          :loadingText="loadingText"
      >
        <div class="pro-list">
          <div class="box line05" v-for="item in productList" @click="toDetail(item)">
            <div class="top" v-if="item.isVisible == 0 && item.name != $t('dw.t196')">
              <carousel :elements="item.coverImages.split(',').map(item => imgBaseUrl + item)" style="width: 80px;"/>
              <div class="info">
                <p v-if="$i18n.locale=='zh_CN'" class="t">{{item.name}}</p>
                <p v-else class="t">{{item.nameEn}}</p>
                <p v-if="item.type == '赠送产品'"class="">{{ $t('dw.t168') }}: {{item.dailyInterest}}%</p>
                <p v-else class="">{{ $t('dw.t168') }}: {{parseFloat(item.minimumBuy*item.dailyInterest/10000).toFixed(2)}}</p>
                <p>{{ $t('dw.t185') }}: {{item.cycle}} {{ $t('dw.t7') }}</p>
                <p class="" v-if="item.name != 'Reward Product'" >{{ $t('dw.t5') }}: {{ item.copies }}/{{ item.copies - item.buyCount }}</p>
                <p class="" v-else-if="item.name == 'Reward Product'" >Count: {{ item.buyCount}}</p>
              </div>
              <div class="r-price" v-if="item.type != '赠送产品'">
                <p class="price" style=" text-align: right">{{  currencyShape  }}{{ parseFloat(item.minimumBuy / 100) }}</p>
                <div class="buy" v-if="item.onSale == 0&&(item.copies!=item.buyCount)" style="width: 70px;">{{ $t('btn.t2') }}</div>
                <div class="buy" v-else-if="item.onSale == 1 ||item.copies==item.buyCount" style="width: 70px;">{{ $t('btn.t34') }}</div>
              </div>
            </div>
            <div v-if="item.isVisible == 0 && item.showProgressBar == 0" class="pro-progress">
              <div class="progress-bar" :style="{ width: item.progress+'%' }"></div>
            </div>
          </div>
        </div>
      </van-list>
    </van-pull-refresh>
    <CustomerService/>
  </div>
</template>
<script>
import carousel from "../../components/carousel.vue";
import {mapActions, mapGetters} from "vuex";
import mixinsSerivce from '@/mixins/service';

export default {
  mixins: [mixinsSerivce],
  components: {
    carousel
  },
  data() {
    return {
      sym: '',
      currencyShape: '',
      isUpLoading: false, // 上拉加载更多
      upFinished: false, //上拉加载完毕
      isDownLoading: false, // 下拉刷新
      isShowLoadding: false,
      pullingText: this.$t('sys.pullingText'),
      loosingText: this.$t('sys.loosingText'),
      loadingText: this.$t('sys.uploaddingText'),
      totalInvestment: 0,
      totalInterest: 0,
      totalProductCount: 0,
      availableAmt: 0,
      list: [
        {
          bar: '100%',
        },
        {
          bar: '70%',
        },
        {
          bar: '60%',
        },
        {
          bar: '50%',
        },
        {
          bar: '30%',
        },
      ],
      newPurchase: [],
      productList: [],
      purchaseData: null,
      dailyInterest: 0,
      total: 0,
      asset: null,
      classOption: {
        step: 0.2, // 数值越大速度滚动越快
        limitMoveNum: 2, // 开始无缝滚动的数据量 this.dataList.length
        hoverStop: false, // 是否开启鼠标悬停stop
        direction: 1, // 0向下 1向上 2向左 3向右
        openWatch: true, // 开启数据实时监控刷新dom
        singleHeight: 0, // 单步运动停止的高度(默认值0是无缝不停止的滚动) direction => 0/1
        singleWidth: 0, // 单步运动停止的宽度(默认值0是无缝不停止的滚动) direction => 2/3
        waitTime: 2000
      },
      queryParams: {
        name: null,
        nameIn: null,
        nameEn: null,
        nameRu: null,
        coverImages: null,
        category: null,
        type: null,
        rewardProduct: null,
        dailyInterest: null,
        cycle: null,
        minimumBuy: null,
        maximumBuy: null,
        fundType: null,
        progress: null,
        showProgressBar: null,
        sellingTimestamp: null,
        totalFund: null,
        copies: null,
        isVisible: null,
        onSale: null,
        hasGroupBuyOption: null,
        luckyNumberRangeStart: null,
        luckyNumberRangeEnd: null,
        status: null,
        description: null,
        descriptionEn: null,
        descriptionIn: null,
        descriptionRu: null,
        createdTime: null,
        updatedTime: null
      },
      interval: null

    }
  },
  watch: {
    userInfo: {
      deep: true,
      immediate: true,
      handler: function (val) {
        if(!val.user_id)
          return;
        this.getPurchaseHistoryList()
        this.getUserTotalBalance()
        this.getNewsPurchase()
      }
    },
    "$route": {
      immediate: true,
      deep: true,
      handler: function (to) {
        if (to.name === "fund") {
          this.getList()
          this.interval = setInterval(() => {
            this.getNewsPurchase()
          }, 60000)
        } else {
          clearInterval(this.interval)
        }
      }
    }
  },
  created() {
    let token = localStorage.getItem('token') || null
    if(token == null) {
      this.errDialog(this.$t('msg.loginFirst'))
      return this.$router.push("/login")
    }
    this.sym = localStorage.getItem('localeCurrency') || 'NGN'
    if(this.sym == 'NGN')
      this.currencyShape = '₦'
    else this.currencyShape = '¥'
    this.getList();
  },
  computed: {
    ...mapGetters({
      userInfo: 'user/userInfo',
      userAccount: 'user/userAccount',
    }),
  },
  methods: {
    ...mapActions('user', {
      listProduct: 'listProduct', // 充值记录
      getPurchaseCount: 'getPurchaseCount',
      getPurchaseNews: 'getPurchaseNews',
      getUserBalance: 'getUserBalance',
    }),
    async getList() {
      let res = await this.listProduct()
      this.productList = res.productList
      this.total = this.productList.length
    },
    async getPurchaseHistoryList() {
      const user_id = this.userInfo.user_id
      let res = await this.getPurchaseCount({
        userId: user_id
      })
      this.purchaseData = res.data
    },
    async getUserTotalBalance() {
      const user_id = this.userInfo.user_id
      let userBalance = await this.getUserBalance({
        userId: user_id
      })
      this.availableAmt = userBalance.availableAmt || 0
    },
    async getNewsPurchase() {
      const user_id = this.userInfo.user_id
      if(user_id === null ){return}else{
        let res = await this.getPurchaseNews({
          userId: user_id
        })
        this.newPurchase = res.data
      }
    },
    toDetail(data) {
      this.$router.push({
        path: '/funddetail',
        query: {
        data: JSON.stringify(data),
        },
      })
    },
    purchaseDetail() {
      this.$router.push({
        path: '/product',
        query: {
          type: '1',
        },
      })
    },
    toGo() {
      this.$router.push({
        path: '/team',
      })
    },
    onDownRefresh() {
      this.isShowLoadding = true
      setTimeout(() => {
        this.incomeData = []
        this.upFinished = false // 不写这句会导致你上拉到底过后在下拉刷新将不能触发下拉加载事件
        this.isDownLoading = false
        this.isUpLoading = false
        this.getList()
      }, 1000)
    },
  },
}
</script>
<style>
/* 清空滚动条 */
::-webkit-scrollbar {
 display: none;
}

.earning-box{
  height: 3rem ;
  overflow: hidden;
  width: 100%;
  border: 1px solid #ebeef5;
  overflow: hidden;
  box-sizing: border-box;
  padding: 0 5px;
}

.earning-list{
  display: block;
  padding: 0;
  margin: 0;
  color: #141515;
  background: #fff;
  border-top: 1px solid #E1E1E1;
  border-bottom: 1px solid #E1E1E1;
  line-height: 0.8em;
}

.earning-item{
  height: 0.8rem;
    width: 100%;
    line-height: 15px;
    text-align: center;
    padding-left: 0.3rem;
    margin-bottom: 0.1rem;
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: flex-start;
    border-radius: 6px;
    border: 1px solid #3376c4;
    overflow: hidden;
}
</style>
