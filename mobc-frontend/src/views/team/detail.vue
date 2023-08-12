<template>
  <modMain title="Detail" class="detail-main">
    <div v-if="list && list.length" >
      <div class="person-detail">
        <div class="list-header">
          <p style="width: 100px; align-items: center">{{ $t('sa.txt477') }}</p>
          <p>{{ $t('dw.t207') }}</p>
          <p>{{ $t('dw.t118') }}</p>
        </div>
        <div class="person-box" v-for="item in list">
          <div class="info-box">
            <p class="name">{{ item.username }}</p>
            <p class="type" >{{ setText(item.transactionType) }}</p>
            <p class="profit"> {{ parseInt(item.amount)/100 }}</p>
          </div>
        </div>
      </div>
    </div>
    <no-data v-else></no-data>
  </modMain>
</template>
<script>
import { bus } from '@/utils/bus'
import {mapActions, mapGetters} from 'vuex'
export default {
  data() {
    return {
      list: [],
      index: 0,
      tempId: "",
      tempAmount: 0,
      type: -1
    }
  },
  mounted() {
  },
  computed: {
    ...mapGetters({
      userInfo: "user/userInfo"
    }),
  },
  methods: {
    ...mapActions({
      incomeOverview: 'user/incomeOverview',
      rewardHistory: 'user/rewardHistory'
    }),
    async getData() {
      const user_id = this.userInfo.user_id
      await this.rewardHistory({
        userId: user_id,
        teamLevel: this.type
      }).then(res =>{
        this.list = res.teamIncomeList
      })
    },
    setText(text) {
      switch (text) {
        case 'Buy_Product_Balance':
          return this.$t('bill.buyProduct')
        case 'Buy_Product_Online':
          return this.$t('bill.buyProductByOnline')
        case 'Withdraw':
          return this.$t('bill.withdraw')
        case 'Recharge':
          return this.$t('bill.recharge')
        case 'Product_Daily_Interest':
          return this.$t('bill.productDailyInterest')
        case 'Commission_A_Reward':
          return this.$t('bill.commissionAReward')
        case 'Commission_B_Reward':
          return this.$t('bill.commissionBReward')
        case 'Commission_C_Reward':
          return this.$t('bill.commissionCReward')
        case 'Treasure_Transfer_In':
          return this.$t('bill.treasureRecharge')
        case 'Treasure_Reward':
          return this.$t('bill.treasureReward')
        case 'Treasure_Transfer_Out':
          return this.$t('bill.treasureWithdraw')
        case 'Child_First_Purchase_Reward':
          return this.$t('bill.firstPurchaseReward')
        case 'Reward_Product':
          return this.$t('bill.giftProduct')
        case 'Principal_Return':
          return this.$t('bill.principalReturn')
      }
    },
  },
  watch: {
    "$route": {
      immediate: true,
      handler(val) {
        if(val.name !== "detail") return;
        const type = val.query.level;
        this.type = type
        this.getData()
      }
    }
  }
}
</script>
