<template>
  <div class="mine">
    <dw-head />
    <div class="pannel-box my-income">
      <!-- <i class="i-vet" v-for="i in 4" :class="'vet' + i"></i> -->
      <div class="in-box">
        <span class="money">{{ balanceData.lockBalance/100 }}</span>
        <span class="desc">{{ $t('dw.t203') }}({{ sym }})</span>
      </div>
      <div class="in-box">
        <span class="money">{{ balanceData.transitAmt/100 }}</span>
        <span class="desc">{{ $t('dw.t791') }}({{ sym }})</span>
      </div>
      <div class="in-box">
        <span class="money">{{ incomeData.total_income/100 }}</span>
        <!--        <span class="desc">{{ $t('dw.t791') }}({{ sym }})</span>-->
        <span class="desc">{{ $t('dw.t225') }}({{ sym }})</span>
      </div>
      <div class="in-box">
        <span class="money">{{ incomeData.daily_income/100 }}</span>
        <span class="desc">{{ $t('dw.t78') }}({{ sym }})</span>
      </div>
    </div>
    <div class="asset-page">
      <div class="usertop" @click="toDetail(treasure)">
        <div class="content">
          <div class="top header">
            <p class="title"> <span>{{ $t('dw.t79') }}</span> <span style="font-size: small">:</span> {{ parseFloat(balanceData.assetBalance)/100 }} </p>
            <img style="width: 20px;height: 20px" src="static/assets/image/icon/icon-right.png"/>
            <h2></h2>
          </div>
          <div class="top left">
            <h3><span style="font-size: small"></span>{{ parseFloat(balanceData.totalTreasureReward)/100 }}</h3>
            <p class="title">{{ $t('dw.t206') }}</p>
          </div>
          <div class="top right">
            <h3><span style="font-size: small"></span>{{ parseFloat(balanceData.todayTreasureReward)/100 }}</h3>
            <p class="title">{{ $t('dw.t205') }}</p>
          </div>
        </div>
      </div>
      <div class="pannel-btns">
        <template v-if="isLogin">
          <div class="btn buy" @click="toGo('/energy?type=1')">
            <van-icon name="pending-payment" />
            {{ $t('btn.t20') }}
          </div>
          <div class="btn sell" @click="toGo('/energy?type=2')">
            <van-icon name="paid" />
            {{ $t('btn.t19') }}
          </div>
        </template>
        <template v-else>
          <div class="btn sell" @click="$router.push('/register')">
            {{ $t('home.register') }}
          </div>
          <div class="btn buy" @click="$router.push('/login')">
            {{ $t('home.login') }}
          </div>
        </template>
      </div>
    </div>
    <div class="team-wrap menu-list">
      <div class="team-cell line05" @click="toCheckin()">
        <img src="static/assets/image/wind/icon/icon-check.png" alt="" />
        <div class="info">
          <p>{{ $t('dw.t227') }}</p>
        </div>
        <van-icon name="arrow r-arr" />
      </div>
      <div class="team-cell line05" @click="toCoupon()">
        <img src="static/assets/image/lucky/coupon.png" alt="" />
        <div class="info">
          <p>Voucher</p>
        </div>
        <van-icon name="arrow r-arr" />
     </div>
      <div class="team-cell line05" @click="toGo('/bill')">
        <img src="static/assets/image/wind/icon/icon-bill.png" alt="" />
        <div class="info">
          <p>{{ $t('dw.t159') }}</p>
        </div>
        <van-icon name="arrow r-arr" />
      </div>
      <div class="team-cell line05" @click="toGo('/inOutList?type=1')">
        <img src="static/assets/image/wind/icon/icon-r.png" alt="" />
        <div class="info">
          <p>{{ $t('dw.t160') }}</p>
        </div>
        <van-icon name="arrow r-arr" />
      </div>
      <div class="team-cell line05" @click="toGo('/inOutList?type=2')">
        <img src="static/assets/image/wind/icon/icon-w.png" alt="" />
        <div class="info">
          <p>{{ $t('dw.t161') }}</p>
        </div>
        <van-icon name="arrow r-arr" />
      </div>
      <div class="team-cell line05" @click="toGo('/bankcard')">
        <img src="static/assets/image/wind/icon/icon-bank.png" alt="" />
        <div class="info">
          <p>{{ $t('dw.t162')  }}</p>
        </div>
        <van-icon name="arrow r-arr" />
      </div>
      <div class="team-cell line05" @click="downAppEx">
        <img src="static/assets/image/wind/icon/download.png" alt="" />
        <div class="info">
          <p>{{ $t('dw.t163')  }}</p>
        </div>
        <van-icon name="arrow r-arr" />
      </div>
      <div class="team-cell " @click="toPersonCenter">
        <img src="static/assets/image/wind/icon/setting.png" alt="" />
        <div class="info">
          <p>Setting</p>
        </div>
        <van-icon name="arrow r-arr" />
      </div>
    </div>
    <groupService />
    <CustomerService/>
  </div>
</template>
<script>
  import { mapActions, mapGetters, mapMutations } from 'vuex'
  import mixinsSerivce from '@/mixins/service'
  export default {
    mixins: [mixinsSerivce],
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
    },
    data() {
      return {
        sym:'',
        treasure: undefined,
        currencyShape: '',
        incomeData:null,
        balanceData:null,
      }
    },
    computed: {
      ...mapGetters({
        userInfo: 'user/userInfo',
        userAccount: 'user/userAccount',
      }),
    },
    beforeRouteEnter(to, from, next) {
      next(vm => {
        // if (window.appInterface) {
        //   window.appInterface.setStatusColor('#02834e')
        // }
        // vm.getQueryUser()
      })
    },
    watch: {
      '$route'() {
        if (this.$route.name == 'profit') {
          this.getQueryUser()
          this.getTreasure()
        }
      },
      userInfo: {
        deep: true,
        immediate: true,
        handler: function (val) {
          if(!val.user_id)
            return;
          this.getQueryUser()
          this.getTreasure()
        }
      }
    },
    methods: {
      ...mapActions({
        logout: 'user/logout',
        getUser: 'user/query',
        getDailyIncome: 'user/getDailyIncome',
        getAsset: 'user/getAsset',
        getUserBalance: 'user/getUserBalance'
      }),
      ...mapMutations('user', {
        quitLogin: 'quitLogin',
      }),
      async getTreasure() {
        await this.getAsset().then(res =>{
          this.treasure = res.data
        })
      },
      async getQueryUser() {
        const user_id = this.userInfo.user_id
        let dailyIncome = await this.getDailyIncome({
          userId: user_id
        })
        this.incomeData= dailyIncome

        let balance = await this.getUserBalance({
          userId: user_id
        })
        this.balanceData = balance;
      },
      toCheckin() {
        if (this.isLogin) {
          const user_id = this.userInfo.user_id;
          this.$router.push({
            path: '/check',
            query: {
              data:user_id
            },
          })
        } else {
          this.$router.push('/login')
        }
      },
      toCoupon() {
        if (this.isLogin) {
          const user_id = this.userInfo.user_id;
          this.$router.push({
            path: '/coupon',
            query: {
              data:user_id
            },
          })
        } else {
          this.$router.push('/login')
        }
      },
      toGo(path) {
        let isOk = false
        if (path == '/setting' || path == '/helpcenter') {
          isOk = true
        } else {
          if (this.isLogin) {
            isOk = true
          }
        }
        if (isOk) {
          this.$router.push({
            path,
          })
        } else {
          this.$router.push({
            path: '/login',
          })
        }
      },
      toDetail(data) {
        if(this.isLogin) {
          this.$router.push({
            path: '/funddetail',
            query: {
              data: JSON.stringify(data),
            },
          })
        }
        return
      },
      toPersonCenter() {
        let path = this.isLogin ? '/setting' : '/login'
        this.$router.push(path)
      },
      downAppEx() {
        console.log(" come on start downAppEx ");
        let _downUrl = ''
        let u = navigator.userAgent
        let isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/)
        let isAndroid = u.indexOf('Android') > -1 || u.indexOf('Adr') > -1 //android终端
        if (isiOS) {

          _downUrl = "https://app.carlyles.online/carlyle";
        } else if (isAndroid) {
          // _downUrl =
          //     'https://superwind-003.s3-accelerate.amazonaws.com/apk/SuperWind.apk'

          var protocol = location.protocol; // 获取协议，例如："http:" 或 "https:"
          var hostname = location.hostname; // 获取域名，例如："www.example.com"
          var currUrl =  protocol+"//"+hostname
          var port = location.port;
          //兼容下测试环境带端口的访问
          if(port!=''){
            currUrl =  protocol+"//"+hostname+":"+port;
          }

          //如果dev测试环境另外算
          if(process.env.NODE_ENV === 'development'){
            _downUrl = process.env.NODE_ENV_URL+"/profile/upload/apk/carlyle.apk"
          }else{
            //这里其实可以查询最新的版本的，后续补充，哎一地鸡毛
            _downUrl = currUrl+"/prod-api/profile/upload/apk/carlyle.apk"
          }
        }

        console.log("  downAppEx url: " + _downUrl);
        setTimeout(() => {
          window.location.href = _downUrl
        }, 100)
      },

      tologout() {
        this.$dialog
                .confirm({
                  title: '',
                  message: this.$t('sys.sureSignout'),
                  cancelButtonText: this.$t('sys.cancel'),
                  confirmButtonText: this.$t('sys.confirm'),
                })
                .then(() => {
                  this.logout()
                  localStorage.removeItem('token')
                  localStorage.removeItem('USER_INFO')
                  sessionStorage.clear()
                  this.quitLogin()
                  // this.$router.push('/')
                  window.location.href = '/'
                })
                .catch(() => {
                  // on cancel
                })
      },
    },
  }
</script>
