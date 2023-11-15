<template>
  <div class="dw-head" :class="{ 'h-head': $route.path == '/home' }">
    <div class="person">
      <div class="avatar" @click="toPersonCenter">
        <img v-if="isLogin && userInfo.headImg" :src="imgBaseUrl + userInfo.headImg" alt="" />
        <img v-else src="static/assets/image/logo.png" alt="" />
      </div>
      <div class="account">
        <div class="c-login" v-if="false">{{ $t('dw.t139') }}</div>
        <div class="infos" v-else>
          <div class="username ell">
            <span> {{ userName }} <img id="dis" @click="showAnHide" alt="" /></span>
          </div>
          <span class="uId" @click="goShowVipRate">
            UID:C{{ isLogin ? userInfo.user_id : '--' }}
            <lable  style=" background-image: linear-gradient(to bottom, red, #fd8403, #ff0);-webkit-background-clip: text;-webkit-text-fill-color: #fd8403;margin-left: 5px; font-weight:bold; font-size:15px;margin-top: 5px">
             <template v-if="balanceData.vipLevel>0">
              <img src="static/assets/image/my/vip.png" style="width:auto ;height: 10px; margin-bottom: 5px;margin-right: 2px" alt="" />
             </template>
              <template v-else>
              <img src="static/assets/image/my/no-vip.png" style="width:auto ;height: 10px; margin-bottom: 5px;margin-right: 2px" alt="" />
             </template>
            </lable>
          </span>
        </div>
      </div>
    </div>
    <div class="balance" @click="toGo">
      <p class="money">
        {{ parseFloat(balanceData.availableAmt)/ 100 }}
      </p>
      <span>{{ $t('dw.t204') }}({{sym}})</span>
    </div>
    <van-popup
            v-model="showVipRate"
            round
            position="bottom"
            safe-area-inset-bottom
            class="popup-vipRate"
            :style="{ height:'10.6rem',width:'96%',margin: '0% 2% 45% 2% ' }">
      <div class="sell-vipRate" @click="closeVipRate">
        <div class="content-money">
          <p class="money"></p>
        </div>
      </div>
    </van-popup>
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
      userName:'',
      flag:false,
      balanceData: null,
      showVipRate:false,
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
    this.userName = this.userInfo.username;
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
    goShowVipRate(){
      this.showVipRate = true;
    },
    closeVipRate(){
      this.showVipRate = false;
    },
    showAnHide() {
        let dis = document.querySelector('#dis')
        this.flag = !this.flag;
        let minName = this.userInfo.username.substring(0, 3)+'*******'+this.userInfo.username.substring(this.userInfo.username.length-3, this.userInfo.username.length);
        if (this.flag) {
          dis.className = 'hover'
          this.userName = minName
        } else {
          dis.className = ''
          this.userName = this.userInfo.username;
        }
      }
  },
}
</script>
