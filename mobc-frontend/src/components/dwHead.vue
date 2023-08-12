<template>
  <div class="dw-head" :class="{ 'h-head': $route.path == '/home' }">
    <div class="person" @click="toPersonCenter">
      <div class="avatar">
        <img v-if="isLogin && userInfo.headImg" :src="imgBaseUrl + userInfo.headImg" alt="" />
        <img v-else src="static/assets/image/logo.png" alt="" />
      </div>
      <div class="account">
        <div class="c-login" v-if="false">{{ $t('dw.t139') }}</div>
        <div class="infos" v-else>
          <div class="username ell">
            {{ isLogin ? userInfo.username : '--' }}
          </div>
          <span>UID:C{{ isLogin ? userInfo.user_id : '--' }}</span>
        </div>
      </div>
    </div>
    <div class="balance" @click="toGo">
      <p class="money">
        {{ parseFloat(balanceData.availableAmt)/ 100 }}
      </p>
      <span>{{ $t('dw.t204') }}({{sym}})</span>
    </div>
  </div>
</template>
<script>
import {mapGetters, mapActions, mapMutations} from 'vuex'
export default {
  name: 'dwHead',
  data() {
    return {
      sym: '',
      userId: '',
      balanceData: null,
      totalWithdrawalBalance: 0,
      assetBalance: 0
    }
  },
  computed: {
    ...mapGetters({
      userInfo: 'user/userInfo',
      userAccount: 'user/userAccount',
    }),
    isLogin() {
      return this.userInfo && this.userInfo.username
    },
  },
  created() {
    this.sym = localStorage.getItem('localeCurrency') || 'NGN'
    this.userId = localStorage.getItem('userId') || null
    this.getUserTotalBalance()
  },
  methods: {
    ...mapActions({
      getUserBalance: 'user/getUserBalance',
    }),
    ...mapMutations('user', {
      setAccount: 'SET_ACCOUNT',
    }),
    toPersonCenter() {
      let path = this.isLogin ? '/setting' : '/login'
      this.$router.push(path)
    },

    async getUserTotalBalance() {
      let userBalance = await this.getUserBalance({
        userId: this.userId
      })
      this.balanceData = userBalance
    },

    toCheckin(data) {
      if (this.isLogin) {
        this.$router.push({
          path: '/check',
          query: {
            data:data
          },
        })
      } else {
        this.$router.push('/login')
      }
    },
    toGo() {
      if (this.isLogin) {
        this.$router.push({
          path: '/energy',
          query: {
            type: 1,
          },
        })
      } else {
        this.$router.push('/login')
      }
    },
  },
}
</script>
