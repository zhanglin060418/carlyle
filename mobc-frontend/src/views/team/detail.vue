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
