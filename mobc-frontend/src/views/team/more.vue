<template>
  <modMain title="More" class="setting">
    <div class="main person-more" v-if="list && list.length">
      <div class="person-box" v-for="item in list">
        <!-- <img v-if="item.imgUrl" :src="imgBaseUrl + item.headImg" alt="" />
        <img v-else src="static/assets/image/xapp/icon_home_default2.png" alt="" /> -->
        <div class="info-main">
          <img
            v-if="item.userAvatar"
            class="dw"
            :src="imgBaseUrl + item.userAvatar"
            alt=""
          />
          <img v-else class="dw" src="static/assets/image/default-aa.png" alt="" />
        </div>
        <div class="info-box">
<!--          <p class="name ">ID:{{ item.uid | strAbb }}</p>
          <p class="profit">${{ $utils.getkStr(item.totalProfit) }}</p>
          <p>{{ $t('dw.t8') }}</p>-->
          <p class="name ">ID : {{ item.userName }}</p>
          <p>{{ $t('dw.t8') }}</p>
          <p class="profit"> {{ parseInt(item.income || 0)/100 }}</p>
        </div>
      </div>
    </div>
    <no-data v-else></no-data>
    <loadding v-if="isLoading"></loadding>
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
      isLoading:false,
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
    }),
    async getData() {
      const user_id = this.userInfo.user_id
        this.isLoading = true
      let res = this.incomeOverview({
        userId: user_id,
        teamLevel: this.type
      }).then(res =>{
        this.list = res.teamIncomeOverview
        this.isLoading = false
      })
    }
  },
  watch: {
    "$route": {
      immediate: true,
      handler(val) {
        if(val.name !== "more") return;
        const type = val.query.level;
        this.type = type
        this.getData()
      }
    }
  }
}
</script>
