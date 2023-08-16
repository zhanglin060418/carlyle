<template>
  <div class="sell-energy team-main">
    <div class="t-box">

      <div class="income-box">
        <p class="money">{{ parseFloat(treeData.childTotalIncome)/100 }}</p>
        <span>
          <!-- 总收入(₹) -->
          {{ $t('dw.t170') }}({{ sym }})
        </span>
      </div>
      <div class="income-box">
        <p class="money">{{ parseFloat(treeData.dailyIncome)/100 }}</p>
        <span>
          <!-- 今日收入 -->
          {{ $t('dw.t228') }}({{ sym }})
        </span>
      </div>
      <div class="income-box">
        <p>{{ parseFloat(rewardAmt)/100  }}</p>
        <span>
          <!-- 总人数 -->
          {{ $t('dw.rewardBalance')}}
        </span>
      </div>
      <div class="income-box">
          <div class="copy-btn" style="margin: 10px 0px; display: flex;justify-content:flex-start;align-items: center; color:#ffffff;border-radius: 6px; background:linear-gradient(180deg, #5d86a1 0%, #3376c4 100%); width: 100%; height:100%;" @click="goTransferOut">
              <span style ='margin-bottom: 5px; width: 100%; text-align: center; font-size: 18px'>{{ $t('btn.t33') }}</span>
          </div>
      </div>
    </div>
      <show-invite :show="showShare" @close="close" :code="userInfo.inviteCode">
      </show-invite>
    <div class="my-index-middle-box">
      <div class="my-index-middle" >
        <div class="arrow-wrap" >
          <strong>A</strong>
        </div>
        <div class="middle-item" @click="toMore(0)">
          <span>{{ $t('dw.t172') }}</span>
          <p>{{ firstNode.totalPeople }}</p>
        </div>
        <div class="middle-item" @click="toDetail(0)">
          <span>{{ $t('dw.t173') }}</span>
          <p>
<!--            {{ $utils.getPstr(treeData.totalProfit) }}-->
            {{ parseFloat(firstNode.totalProfit)/100 }}
          </p>
        </div>
        <div class="profit-item" @click="toDetail(0)">
          <span>{{ childCrowdRatio }}%</span>
        </div>
      </div>
      <div class="my-index-middle" >
        <div class="arrow-wrap" >
          <strong>B</strong>
        </div>
        <div class="middle-item" @click="toMore(1)">
          <span>{{ $t('dw.t172') }}</span>
          <p>{{ secondNode.totalPeople }}</p>
        </div>
        <div class="middle-item" @click="toDetail(1)">
          <span>{{ $t('dw.t173') }}</span>
          <p>
            {{ secondNode.totalProfit/100 }}
          </p>
        </div>
        <div class="profit-item" @click="toDetail(1)">
          <span>{{ grandCrowdRatio }}%</span>
        </div>
      </div>
      <div class="my-index-middle" >
        <div class="arrow-wrap" >
          <strong>C</strong>
        </div>
        <div class="middle-item" @click="toMore(2)">
          <span>{{ $t('dw.t172') }}</span>
          <p>{{ thirdNode.totalPeople }}</p>
        </div>
        <div class="middle-item" @click="toDetail(2)">
          <span>{{ $t('dw.t173') }}</span>
          <p>
            {{ thirdNode.totalProfit/100 }}
          </p>
        </div>
        <div class="profit-item" @click="toDetail(2)">
          <span>{{ greatGrandCrowdRatio }}%</span>
        </div>
      </div>
    </div>
<!--    <div class="invite-btn" @click="share">-->
<!--      <img class="img-money" src="static/assets/image/wind/icon_nook.png" alt="" />-->
<!--      {{ $t('sa.txt72') }}-->
<!--    </div>-->
    <h2 class="p-title">{{ $t('dw.t174') }}</h2>
    <div class="desc" style="background: #eaeeff;">
      <div class="ql-editor" >
          <p class="ql-editor" v-html="invitationText.descriptionEn" />
      </div>
    </div>

    <h2 class="p-title">{{ $t('dw.t175') }}</h2>
    <div class="desc" style="background: #eaeeff;">
        <p class="ql-editor" v-html="treatmentText.descriptionEn" />
    </div>
    <sharePopup :show="showShare" @close="close" :code="userInfo.inviteCode">
    </sharePopup>
    <van-popup v-model="showRule" class="popup-result limit-rule">
      <!-- <img
        class="poput-close"
        src="static/assets/image/dw/close.png"
        alt=""
        @click="showRule = false"
      /> -->
      <rule />
    </van-popup>
    <CustomerService/>
    <loadding v-if="isLoading"></loadding>
  </div>
</template>
<script>
import { bus } from '@/utils/bus'
import rule from './rule'
import { mapActions, mapGetters, mapMutations } from 'vuex'
import mixinsSerivce from '@/mixins/service'
import ShowInvite from "../../components/showInvite";
export default {
  mixins: [mixinsSerivce],
  components: {
    ShowInvite,
    rule,
  },
  data() {
    return {
      sym:'',
      showRule: false,
      list: [],
      treeData: {
        dailyIncome: 0,
        createdToday: 0,
        totalPeople: 0,
        childTotalIncome: 0,
      },
      thirdCount: 0,
      firstNode: {
        totalPeople: 0,
        totalProfit: 0,
      },
      secondNode: {
        totalPeople: 0,
        totalProfit: 0,
      },
      thirdNode: {
        totalPeople: 0,
        totalProfit: 0,
      },
      isLoading:false,
      animatedTop: false,
      scrollTimer: null, // 充值消息滚动定时器
      showShare: false,
      statusList: null, //奖励状态
      isMiss: false, //是否可以领奖
      isMissCnt: 0,
      AwardAmount: 10000,
      finishCount: 1, // 领取数量
      barWidth: 0,
      treatmentText: '',
      invitationText: '',
      childCrowdRatio: 0,
      grandCrowdRatio: 0,
      rewardAmt: 0,
      greatGrandCrowdRatio: 0,
      totalRechargeCount: 0,
      totalWithdrawCount: 0,
      firstPurchaseCommissionRatio: 0,
      purchaseTreasureRate: 0,
      rewardProductDailyInterest: 0
    }
  },
  created() {
    let token = localStorage.getItem('token') || null
    if(token == null) {
      this.errDialog(this.$t('msg.loginFirst'))
      return this.$router.push("/login")
    }
    this.sym = localStorage.getItem('localCurrency') || 'NGN'
  },
  computed: {
    ...mapGetters({
      userInfo: 'user/userInfo',
      userAccount: 'user/userAccount',
    }),
  },
  watch: {
    '$route'() {
      if (this.$route.name == 'team') {
        this.getData()
        this.getTreatmentText()
      }
    },
    userInfo: {
      deep: true,
      immediate: true,
      handler: function (val) {
        if(!val.user_id)
          return;
        this.getData()
        this.getTreatmentText()
      }
    }
  },
  methods: {
    ...mapActions({
      queryUserTreeNode: 'user/queryUserTreeNode',
      award: 'user/award', // 领取奖励
      awardStatus: 'user/awardStatus',
      overView: 'user/overView',
      getTotalTeam: 'user/getTotalTeam',
      getTeamTreatmentText: 'user/getTreatmentText'
    }),
    ...mapMutations({
      setMoreData: 'user/setMoreData',
    }),
    async getTreatmentText() {
      await this.getTeamTreatmentText().then(res => {
          if(res.data.length>0){
              for (let item of res.data) {
                  if(item.remark.includes('Invitation')){
                      this.invitationText = item;
                  }
                  if(item.remark.includes('Treatment')) {
                      this.treatmentText = item;
                  }

              }
          }
      })
    },
      goTransferOut(){
          this.$router.push({
              path: '/transferInviteFeeOut',
          })
      },
    async getData() {
      this.isLoading = true
      const user_id = this.userInfo.user_id
      let res = await this.overView({
        userId: user_id
      })

      this.list = res.teamOverview
      this.firstNode.totalPeople = this.list.childCount || 0;
      this.firstNode.totalProfit = this.list.childIncome || 0;

      this.secondNode.totalPeople = this.list.grandCount || 0;
      this.secondNode.totalProfit = this.list.grandIncome || 0;

      this.thirdNode.totalPeople = this.list.greatGrandCount || 0;
      this.thirdNode.totalProfit = this.list.greatGrandIncome || 0;

      this.treeData.childTotalIncome = this.list.childTotalIncome|| 0;
      this.treeData.totalPeople = this.firstNode.totalPeople + this.secondNode.totalPeople + this.thirdNode.totalPeople
      this.treeData.createdToday = this.list.todayChildCount || 0;
      this.treeData.dailyIncome = this.list.dailyIncome || 0;

      this.childCrowdRatio = this.list.childCrowdRatio
      this.grandCrowdRatio = this.list.grandCrowdRatio
      this.greatGrandCrowdRatio = this.list.greatGrandCrowdRatio

      this.totalRechargeCount = this.list.totalRechargeCount || 0
      this.totalWithdrawCount = this.list.totalWithdrawCount || 0
      this.firstPurchaseCommissionRatio = this.list.firstPurchaseCommissionRatio || 0
      this.purchaseTreasureRate = this.list.purchaseTreasureRate || 0
      this.rewardProductDailyInterest = this.list.rewardProductDailyInterest || 0
      this.rewardAmt = this.list.rewardAmt || 0
      this.isLoading = false
    },
      toMore(level) {
          this.$router.push({
              path: '/more',
              query: {
                  data:level,
              },
          })
      },
      toDetail(level) {
          this.$router.push({
              path: '/detail',
              query: {
                  data:level,
              },
          })
      },

    //获取当前奖励
    getAwardData() {
      let type = 'INVESTMENT_PEOPLE'
      this.awardStatus({ activityType: type }).then(res => {
        this.statusList = res.data || null
        this.isMiss = this.isHasGet(type)
      })
    },
    onClick() {
      let type = 'INVESTMENT_PEOPLE'
      if (!this.isMiss) {
        this.errDialog(this.$t('dw.t156'))
        return
      }
      this.paward(type)
    },
    paward(type) {
      this.award({ activityType: type, finishCount: this.finishCount }).then(
        res => {
          if (res.code == 0) {
            this.getAwardData()
            this.showDialog(
              this.$t('sa.reAmout') + ' ' + this.sym + this.AwardAmount
            )
          } else {
            this.errDialog(res.msg)
          }
        }
      )
    },
    share() {
      this.showShare = true
    },
    close() {
      this.showShare = false
    },
  },
  beforeDestroy() {
    this.scrollTimer && clearInterval(this.scrollTimer)
  },
  toService() {
    let idx = parseInt(Math.random() * this.wathsrls.length, 10)
    let _url = this.wathsrls[idx]
    if (window.webkit) {
      window.webkit.messageHandlers.openBrowser.postMessage(_url)
    } else if (window.appInterface) {
      if (typeof window.appInterface.openBrowser === 'function') {
        window.appInterface.openBrowser(_url)
      } else if (typeof window.appInterface.goToBrowser === 'function') {
        window.appInterface.goToBrowser(_url)
      } else {
        window.location.href = _url
      }
    } else {
      window.location.href = _url
    }
  },
}
</script>
<style>
.swiper-wrapper {
  transition-timing-function: linear !important;
}
</style>
