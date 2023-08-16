<template>
  <modMain :title="$t('dw.t81')" class="bill-main setting">
    <div class="top-fiexd">
      <topTabs :tabs="tabs" @itemClick="itemClick" />
    </div>
    <div class="bill">
        <van-pull-refresh v-model="isDownLoading" @refresh="onDownRefresh" :pullingText="pullingText" :loosingText="loosingText" :loadingText="loadingText">
          <van-list v-if="list && list.length > 0" v-model="isUpLoading" :finished="upFinished" :finished-text="$t('sys.nomore')" :immediate-check="false" offset="20" :loadingText="loadingText">
          <div class="content">
            <div class="tr-box" v-for="item in list" :key="item.id">
              <div class="top">
                <p>{{ setText(item.transactionType) }}</p>
                <span class="value" v-if="item.amount < 0" style="color: #2e91e0;font-size: 14px"> {{ parseFloat(item.amount)/100 }}</span>
                <span class="value" v-else style="color: #0a364a;font-size: 14px"> {{ parseFloat(item.amount)/100 }}</span>
              </div>
              <div class="bot">
<!--                <p>{{ item.remark }}</p>-->
                <p>{{ parseTime(item.transactionDate)}}</p>
<!--                <span :class="ifNe(item.type) ? 'red' : 'green'">
                  {{ ifNe(item.type) ? '-' : ''
                  }}{{ $utils.getkStr(item.amount, 2) }}</span>-->
              </div>
            </div>
          </div>
        </van-list>
        <no-data v-else-if="!isShowLoadding && (!incomeData || incomeData.length == 0)"></no-data>
      </van-pull-refresh>
    </div>
  </modMain>
</template>
<script>
import { mapGetters, mapActions } from 'vuex'
import {parseTime} from "../../utils/parseTime";
export default {
  data() {
    return {
      sym: '',
      userId:'',
      active: 0,
      total: 0,
      list: [],
      tabs: [
        {
          title: this.$t('dw.t115'),
          index: 0,
        },
        {
          title: this.$t('dw.t116'),
          index: 1,
        },
        {
          title: this.$t('dw.t117'),
          index: 2,
        },
      ],
      isUpLoading: false, // 上拉加载更多
      upFinished: false, //上拉加载完毕
      isDownLoading: false, // 下拉刷新
      isShowLoadding: false,
      pullingText: this.$t('sys.pullingText'),
      loosingText: this.$t('sys.loosingText'),
      loadingText: this.$t('sys.uploaddingText'),
      /*params: {
        page: 1,
        size: 10,
        type: ['CREDIT_INVITE_REGISTER'],
        startTime: null,
        endTime: null,
      },*/
      incomeData: null,
      total: 0,
    }
  },
  computed: {
    ...mapGetters({
      userInfo: 'user/userInfo',
    }),
  },
  beforeRouteEnter(to, from, next) {
    document.getElementsByTagName('body')[0].style.background = '#137ed7'
    next()
  },
  beforeRouteLeave(to, from, next) {
    document.getElementsByTagName('body')[0].style.background = ''
    next()
  },
  created() {
      let token = localStorage.getItem('token') || null
      if (token == null) {
          this.errDialog(this.$t('msg.loginFirst'))
          return this.$router.push("/login")
      }
      this.userId = localStorage.getItem('userId') || null
      this.sym = localStorage.getItem('localCurrency') || 'NGN'
      this.itemClick(0)
  },
  methods: {
    parseTime,
    ...mapActions({
      queryStatementPage: 'user/queryStatementPage',
      getTransactionHistoryToday: 'user/getTransactionHistoryToday',
      getTransactionHistoryYesterday: 'user/getTransactionHistoryYesterday',
      getTransactionHistoryLastWeek: 'user/getTransactionHistoryLastWeek',
    }),
    ifNe(type) {
      //  CREDIT_INVITE_REGISTER, CREDIT_INVITE_WAGER, CREDIT_LOGIN_PROFIT, CREDIT_LUCKY_DRAW, CREDIT_PAYMENT, CREDIT_PROFIT, CREDIT_SUBORDINATE, DEBIT_LUCKY_DRAW, DEBIT_WAGER, DEBIT_WITHDRAW_OTC, FREEZE_WITHDRAW_OTC, UNFREEZE_WITHDRAW_OTC
      let negs = [
        'DEBIT_LUCKY_DRAW',
        'DEBIT_WAGER',
        'DEBIT_WITHDRAW_OTC',
        'FREEZE_WITHDRAW_OTC',
        'DEBIT_BUY_GOOD',
      ]
      return negs.includes(type)
    },
    setText(text) {
      switch (text) {
        case 'Recharge':
           return this.$t('transType.Recharge')
        case 'Withdraw':
           return this.$t('transType.Withdraw')
        case 'Treasure_Reward':
           return this.$t('transType.Treasure_Reward')
        case 'Product_Daily_Interest':
           return this.$t('transType.Product_Daily_Interest')
        case 'Principal_Return':
           return this.$t('transType.Principal_Return')
        case 'Buy_Product_Balance':
           return this.$t('transType.Buy_Product_Balance')
        case 'Buy_Product_Online':
           return this.$t('transType.Buy_Product_Online')
        case 'Child_First_Purchase_Reward':
           return this.$t('transType.Child_First_Purchase_Reward')
        case 'Reward_Product':
           return this.$t('transType.Reward_Product')
        case 'Commission_A_Reward':
           return this.$t('transType.Commission_A_Reward')
        case 'Commission_B_Reward':
           return this.$t('transType.Commission_B_Reward')
        case 'Commission_C_Reward':
           return this.$t('transType.Commission_C_Reward')
        case 'Treasure_Transfer_In':
           return this.$t('transType.Treasure_Transfer_In')
        case 'Treasure_Transfer_Out':
           return this.$t('transType.Treasure_Transfer_Out')
        case 'Salary_Subsidy_Bonus':
           return this.$t('transType.Salary_Subsidy_Bonus')
        case 'Register_Reward':
              return this.$t('transType.Register_Reward')
        case 'Manual_Adjustment':
              return this.$t('transType.Manual_Adjustment')
        case 'SignIn_Reward':
              return this.$t('transType.SignIn_Reward')
        case 'Manual_Reward_Product':
              return this.$t('transType.Manual_Reward_Product')
        case 'Reward_Transfer_Out':
              return this.$t('transType.Reward_Transfer_Out')
      }
    },
    itemClick(type) {
      this.type = type
      this.getData()
    },
    async getData() {
      this.isShowLoadding = true
      const user_id = this.userId
      if(this.type == 0)
        this.getTransactionHistoryToday({userId: user_id}).then(res =>{
          this.isShowLoadding = false
          if(res.code == 200) {
            this.list = res.list
          }}
        )
      else if(this.type == 1)
        this.getTransactionHistoryYesterday({userId: user_id}).then(res =>{
          this.isShowLoadding = false
          if(res.code == 200) {
            this.list = res.list
          }}
        )
      else if(this.type == 2)
        await this.getTransactionHistoryLastWeek({userId: user_id}).then(res =>{
          this.isShowLoadding = false
          if(res.code == 200) {
            this.list = res.list
          }}
        )
      this.upFinished = true
      this.isDownLoading = false
      this.isUpLoading = false
      this.isLoading = false
      /*this.queryStatementPage(this.params).then(res => {
        this.isShowLoadding = false
        if (res.code == 0) {
          // this.incomeData = res.data.records || []
          this.total = Number(res.data.total || 0)
          this.loadFinished(res.data.records)
        } else {
          this.errDialog(res.msg)
        }
      })*/
    },
    loadFinished(rows) {
      this.upFinished = false
      // this.isLoading = true
      if (rows == null || rows.length === 0) {
        // 加载结束
        this.upFinished = true
        this.isDownLoading = false
        this.isUpLoading = false
        this.isLoading = false
        return
      }
      if (rows.length < this.params.size) {
        // 最后一页不足size条的情况
        this.upFinished = true
      }
      // 处理数据
      if (this.params.page === 1) {
        this.incomeData = rows
      } else {
        this.incomeData = this.incomeData.concat(rows)
      }
      //如果列表数据条数>=总条数，不再触发滚动加载
      if (this.incomeData.length >= this.total) {
        this.upFinished = true
      }
      this.isDownLoading = false
      this.isUpLoading = false
      this.isLoading = false
    },
    // 下拉刷新
    onDownRefresh() {
      // this.params.page = 1
      this.isShowLoadding = true
      setTimeout(() => {
        this.incomeData = []
        this.upFinished = false // 不写这句会导致你上拉到底过后在下拉刷新将不能触发下拉加载事件
        this.isDownLoading = true
        this.isUpLoading = true
        this.getData()
      }, 1000)
    },
    // 上拉加载请求方法
    onLoadList() {
      this.params.page += 1
      this.isShowLoadding = true
      setTimeout(() => {
        this.isUpLoading = true
        this.getData()
      }, 1000)
    },
  },
}
</script>
