<template>
  <modMain :title="title" class="setting">
    <div class="record-main">
      <pull-list
        :params="params"
        :total="total"
        @getList="getList"
        @setPage="setPage"
        @setListData="setListData"
        ref="pullComp"
      >
        <div
          slot="content"
          class="record-list"
          v-for="item in outinList"
          @click="item.open = !item.open"
        >
          <!-- @click="goToDetail(item)" -->
          <div class="h-top">
            <div class="left-box">
              <p class="orderNo">{{ item.requestNo }}</p>
              <p class="usdt">
                {{ sym }}
                {{ getTotalAmount(item) }}
              </p>
              <p class="time">{{ parseTime(item.createTime) }}</p>
            </div>
            <div
              class="right-state"
              :class="{
                success:
                  item.status == 'Completed',
                refused: item.status == 'In progress',
                declined: item.status == 'Declined'
              }"
            >
              {{ stateText(item.status) }}
            </div>
            <i class="icon-arrow" :class="{ down: item.open }"></i>
          </div>
          <template v-if="item.open">
            <detail-info :detail="item" :type="type" />
          </template>
        </div>
      </pull-list>
    </div>
  </modMain>
</template>
<script>
import {mapActions, mapGetters, mapMutations} from 'vuex'
import detailInfo from './details'
import {parseTime} from "../../utils/parseTime";
export default {
  components: {
    detailInfo,
  },
  data() {
    return {
      sym: '',
      printAmount: '',
      outinList: [],
      title: '',
      total: 0,
      params: {
        start: '',
        end: '',
        page: 1,
        size: 15,
        statusList: ['SUCCESS'],
      },
    }
  },
  computed: {
    ...mapGetters({
      userInfo: 'user/userInfo',
    }),
    type() {
      return this.$route.query.type || 1
    },
  },
  created() {
    this.sym = localStorage.getItem('localCurrency') || 'NGN'
    this.title =
      this.type == 1
        ? this.$t('bill.deposithistory')
        : this.$t('bill.withdrawhistory')
  },
  watch: {
    userInfo: {
      deep: true,
      immediate: true,
      handler: function (val) {
        if(!val.user_id)
          return;
        this.getList()
      }
    }
  },
  methods: {
    parseTime,
    ...mapActions('user', {
      paymentList: 'paymentList', // 充值记录
      withdrawList: 'withdrawList', // 提币记录
    }),
    ...mapMutations('user', {
      setWithdrawDetail: 'SET_WITHDRAW_DETAIL',
    }),
    stateText(status) {
      switch (status) {
        case 'Pending':
          return this.$t('status.PENDING')
        case 'In progress':
          return this.$t('status.PROCESSING')
        case 'SUCCESSFUL':
          return this.$t('status.SUCCESS')
        case 'Completed':
          return this.$t('status.SUCCESS')
        case 'Failed':
          return this.$t('status.PENDING')
        case 'Declined':
          return this.$t('status.DECLINED')
      }
    },
    async getList() {
      let res = null
      if (this.type == 1) {
        const user_id = this.userInfo.user_id
        res = await this.paymentList({
          userId: user_id
        })
      } else {
        // this.params.statusList = null
        const user_id = this.userInfo.user_id
        res = await this.withdrawList({
          userId: user_id
        })
      }
      if (res && res.code == 200) {
        console.log('充值体现记录', res)
        // let rows = res.data.records
        let rows = res.data
        rows.forEach(d => {
          d.open = false
        })
        await this.$refs.pullComp.loadFinished(rows)
      } else {
        this.errDialog(res.msg)
      }
    },

    goToDetail(item) {
      // this.setWithdrawDetail(item)
      let strData = JSON.stringify(item)
      this.$router.push({
        path: '/inoutDetail',
        query: {
          type: this.type,
          detail: strData,
        },
      })
    },
    getTotalAmount(data) {
      let currentAmount = data.amount
      if( this.type != 1) {
        currentAmount += data.fee
      }
      return currentAmount/100
    },
    /****======下拉刷新两个回调======*****/
    setListData(data) {
      this.outinList = data
    },
    setPage(ismore = true) {
      if (ismore) {
        this.params.page++
      } else {
        this.params.page = 1
      }
    },
    /****======end======*****/
  },
}
</script>
