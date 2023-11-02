<template>
  <modMain :title="this.$route.query.type == 'withdraw' ? $route.meta.withdrawtitle : item.name == this.$t('dw.t196') ? $route.meta.title : 'Confirm Buy'">
    <div class="sell-energy">
    <div class="t-box top" v-if="this.$route.query.type == 'pruchase' && this.item.name != this.$t('dw.t196')">
      <span v-if="listVouchers.length == 0">{{ $t('payDetail.text56') }}</span>
      <span v-else>{{ $t('payDetail.text57') }}</span>
      <template>
        <div class="yinhangka" :class="{ has: listVouchers.length > 0 }"  @click="goShowVoucher">
          <div class="add-btn" v-if="listVouchers.length == 0" >
            {{ $t('payDetail.text56') }}
          </div>
          <div class="card-info" v-else  >
            <img src="static/assets/image/lucky/coupon.png" alt="" />
            <div class="info">
              <p>{{voucherItem.nameEn}}  ({{currencyShape}}:{{ parseFloat(voucherItem.amount/100)}})</p>
              <span>{{$t('dw.t233')}}: {{ formatDate(voucherItem.endDate)}} </span>
            </div>
          </div>
          <van-icon name="arrow" />
        </div>
      </template>
    </div>
    <div class="sell-energy energy">
    <div class="t-box bot" v-if="item.fundType == '区间'">
      <p v-if="item.name != $t('dw.t196')">
        {{ $t('payDetail.text370') }} ( {{ $t('dw.t192') }} : {{ parseFloat(item.minimumBuy/100 )}}
          - {{ parseFloat(this.item.maximumBuy/100) }} )
      </p>
      <div class="reqMoney line05">
        {{ sym }}
        <input
            type="text"
            v-model.trim="reqNum"
            :placeholder="$t('dw.t193')"
        />
      </div>
      <p v-if="this.$route.query.type == 'withdraw'" class="shengyu">
        {{ $t('dw.t79') }}：{{ parseFloat(assetBalance)/100 }}
      </p>
      <p v-else-if="selectedBalance" class="shengyu">
        <span class="showBalance">{{ $t('payDetail.text39') }}：{{ parseFloat(balance/100)}}</span> <span v-if="item.name == $t('dw.t196')" class="allIn" @click="goselectAll">All in</span>
      </p>
      <div class="buy-box">
        <div
            class="m-box"
            :class="{ active: num == reqNum }"
            v-for="num in buyList"
            @click="chooseMoney(num)"
        >
          {{ num }}
          <van-icon name="success ok" v-if="num == reqNum" />
        </div>
      </div>
    </div>
    <div class="t-box bot" v-else>
      <div class="reqMoney">
        {{ sym }}
        {{ item.minimumBuy/100 }}
      </div>
      <p v-if="selectedBalance" class="line05 shengyu">
        {{ $t('payDetail.text39') }}：{{ parseFloat(balance/100)}}
      </p>
    </div>
    <div class="popup-btn" @click="buyClick()">
      {{ $t('btn.t27') }} {{ sym }} {{ ' ' }} {{ this.item.fundType == '定额' ? this.item.minimumBuy/100 : reqNum }}
    </div>

      <van-popup
              v-model="showVoucher"
              round
              position="bottom"
              safe-area-inset-bottom
              class="popup-sell-buy"
              :style="{ height: '60%',margin: '0% 0% 0% 0%' }"
      >
        <div class="sell-buy popup-box energy-popup">
          <div class="head">
            <div class="title">
              {{ $t('payDetail.text57') }}
            </div>
          </div>
          <div class="content">
            <div class="bank-main">
              <div
                      class="display-box line05"
                      :class="{ active: voucherItem.id == item.id }"
                      v-for="item in listVouchers"
                      :key="item.id"
                      @click="voucherClick(item)"
              >
                <div class="left">
                  <img src="static/assets/image/lucky/coupon.png" alt="" />
                  <div class="infos">
                    <p>
                      {{ item.nameEn }}
                    </p>
                    <span>
                   {{$t('dw.t233')}}: {{ formatDate(item.endDate)}} </span
                    >
                  </div>
                </div>
                <div class="right" v-if="item.id == voucherItem.id">
                  <van-icon name="checked check" />
                </div>
              </div>
              <div class="display-box line05" @click="toCoupon()">
                <div class="left">
                  <!-- <img src="static/assets/image/dw/add-bank-b.png" alt="" /> -->
                  <div class="infos">
                    <p class="new">All Vouchers</p>
                  </div>
                </div>
                <div class="right">
                  <van-icon name="arrow" />
                </div>
              </div>
            </div>
            <div class="popup-btn lv" @click="showVoucher = false">
              {{ $t('sys.confirm') }}
            </div>
          </div>
        </div>
      </van-popup>
    <!-- Choose Methods Dialog -->
    <van-popup
        v-model="showPurchaseDetailDlg"
        round
        position="bottom"
        safe-area-inset-bottom
        class="popup-confirm-buy"
        :style="{ height: '100%', background: '#eaeeff' }"
    >
      <div class="sell-buy popup-box energy-popup">
        <div class="head">
          <div class="title">
            {{ $t('dw.t222') }}
          </div>
          <img class="poput-close" style="height: 25px; width: 25px" src="static/assets/image/dw/close_circle@3x.png" alt="" @click="showPurchaseDetailDlg = false"/>
        </div>
        <div class="content">
          <div class="usertop">
            <div class="top">
              <p class="totlle">{{ $t('dw.t204') }}</p>
              <h2>{{  currencyShape  }}: {{ parseFloat(balance/100) }}</h2>
            </div>
          </div>
          <div class="bank-main">
            <div class="display-box" style="height: 35px" >
              <div class="left">
                <div class="infos">
                  <p class="new">{{ $t('dw.t216') }}</p>
                </div>
              </div>
              <div class="right">
                <p class="new">{{ reqNum }}({{ sym }})</p>
              </div>
            </div>
            <div class="display-box" style="height: 35px" v-if="voucherItem!=null&&voucherItem.amount>0">
              <div class="left">
                <div class="infos">
                  <p class="new">Cash</p>
                </div>
              </div>
              <div class="right">
                <p class="new">{{ reqNum- (voucherItem.amount/100)}}({{ sym }})</p>
              </div>
            </div>
            <div class="display-box" style="height: 35px" v-if="voucherItem!=null&&voucherItem.amount>0">
              <div class="left">
                <div class="infos">
                  <p class="new">Voucher</p>
                </div>
              </div>
              <div class="right">
                <p class="new">{{(voucherItem.amount/100)}}({{ sym }})</p>
              </div>
            </div>
            <div class="display-box" style="height: 35px">
              <div class="left">
                <div class="infos">
                  <p class="new">{{ $t('dw.t217') }}</p>
                </div>
              </div>
              <div class="right">
                <p class="new">{{ item.dailyInterest }}%</p>
              </div>
            </div>
            <div class="display-box" style="height: 35px">
              <div class="left">
                <div class="infos">
                  <p class="new">{{ $t('dw.t218') }}</p>
                </div>
              </div>
              <div class="right">
                <p class="new">{{ reqNum * item.dailyInterest / 100 }}</p>
              </div>
            </div>
            <div class="display-box" style="height: 35px">
              <div class="left">
                <div class="infos">
                  <p class="new">{{ $t('dw.t219') }}</p>
                </div>
              </div>
              <div class="right">
                <p class="new">{{ item.cycle }}{{ $t('dw.t7')}}</p>
              </div>
            </div>
            <div class="display-box" style="height: 35px">
              <div class="left">
                <div class="infos">
                  <p class="new">{{ $t('dw.t220') }}</p>
                </div>
              </div>
              <div class="right">
                <p class="new">{{ item.copies - item.buyCount }}</p>
              </div>
            </div>
            <div class="display-box" style="height: 35px">
              <div class="left">
                <div class="infos">
                  <p class="new">{{ $t('dw.t221') }}</p>
                </div>
              </div>
              <div class="right">
                <p class="new" style="color: #ff0026">{{ reqNum }}+({{ reqNum }}x{{item.dailyInterest}}%x{{item.cycle}})={{(reqNum * (item.dailyInterest * item.cycle+100)/100).toFixed(2)}}</p>
              </div>
            </div>
          </div>
          <div class="popup-btn pw" @click="toConfirmPaymentPwd">
            {{ $t('sys.confirm') }}
          </div>
        </div>
      </div>
    </van-popup>
    <top-info ref="InfoRef" @sure="buyClick" :user="userInfo"></top-info>
    <van-dialog
        v-model="verifyPayDlgOpen"
        :title="$t('user.cpfPayment')"
        :show-cancel-button="true"
        :close-on-click-overlay="true"
        :cancelButtonText="$t('sys.cancel')"
        :confirm-button-text="$t('sys.confirm')"
        cancelButtonColor="#1fbf75"
        @confirm="verifyPaymentPassword"
    >
    <base-input
        v-model.trim="payPassword"
        type="password"
        :placeholder="$t('home.paymentPwd')"
    ></base-input>
    </van-dialog>
    <van-popup
            v-model="showLucky"
            round
            position="bottom"
            safe-area-inset-bottom
            class="popup-lucky"
            :style="{ height:'14.2rem',width:'76%',margin: '0% 12% 25% 12% ' }">
      <div class="sell-lucky" @click="closeLucky">
        <div class="content-money">
          <p class="money">{{currencyShape}} {{luckyAmount/100}}</p>
        </div>
      </div>
    </van-popup>
    <van-popup
              v-model="showDraws"
              round
              position="bottom"
              safe-area-inset-bottom
              class="popup-draws"
              :style="{ height:'9.3rem',width:'76%',margin: '0% 12% 45% 12% ' }">
        <div class="sell-draws" @click="closeDraws">
          <div class="content-money">
            <p class="money">{{currencyShape}} {{drawsAmount/100}}</p>
          </div>
        </div>
      </van-popup>
  </div>
    <loadding v-if="isLoading"></loadding>
    </div>
  </modMain>
</template>

<script>
import topInfo from "../energy/info.vue";
import { bus } from '@/utils/bus'
import {mapActions, mapGetters, mapMutations} from "vuex";

export default {
  components: {
    topInfo,
  },
  data() {
    return {
      currencyShape: '',
      luckyAmount:0,
      drawsAmount:0,
      showDraws: false,
      isLoading: false,
      showLucky: false,
      showVoucher: false,
      listVouchers: [],
      voucherItem: null,
      sym: '',
      userId: '',
      payPassword: '',
      verifyPayDlgOpen: false,
      title: "",
      item: null,
      currentShow: false,
      reqNum: null,
      postNum: null,
      prizeType :'',
      balance: 0,
      assetBalance: 0,
      pList:[],
      purchaseMonery: {
        drawsId: 0,
        amount: 0,
      },
      type: '',
      selectedBalance: true,
      showPurchaseDetailDlg: false,
    }
  },
  computed: {
    buyList() {
      if( this.item.name == this.$t('dw.t196')) {
        if (this.currentitem) {
          let arr = this.currentitem.selection.split(',')
          return arr.map(d => Number(d))
        }
        return [ 50000, 100000, 500000]
      }
      let init = this.item.minimumBuy / 100
      let max = this.item.maximumBuy /100
      let val = (max - init) / 5
      if (this.currentitem) {
        let arr = this.currentitem.selection.split(',')
        return arr.map(d => Number(d))
      }
      return [init, init+val*1, init+val*2, init+val*3, init+val*4, max]
    },
  },
  created() {
    //获取通道
    this.userId = localStorage.getItem('userId') || null
    this.item = JSON.parse(this.$route.query.data)
    this._getVoucherList()
    this.getData()
    this.type = this.$route.query.type
    this.sym = localStorage.getItem('localeCurrency') || 'NGN'
    if(this.sym == 'NGN')
      this.currencyShape = '₦'
    else this.currencyShape = '¥'
  },
  methods: {
    ...mapActions({
      addToPurchase: 'user/addToPurchase',
      transferInAsset: 'user/transferInAsset',
      transferOutAsset: 'user/transferOutAsset',
      getUserBalance: 'user/getUserBalance',
      getBankCard: 'bankCard/getBankCard',
      getVoucherList: 'user/getVoucherList',
      recharge: 'user/recharge',
      verifyPayPassword: 'user/verifyPayPassword',
      getPurchaseListForBuy: 'user/getPurchaseListForBuy'

    }),
    ...mapMutations('user', {
      setAccount: 'SET_ACCOUNT',
    }),
    goShowVoucher(){
      if(this.listVouchers.length>0){
        this.showVoucher = true;
      }
    },
    voucherClick(item) {
      this.purchaseMonery.drawsId = item.id
      this.voucherItem = item
    },
    _getVoucherList() {
      const user_id = this.userInfo.user_id;
      const productId  = this.item.id;
      this.getVoucherList({productId:productId}).then(res => {
        if (res.code == 200) {
          this.listVouchers = res.data || []
          if (this.listVouchers.length > 0) {
            this.voucherClick(this.listVouchers[0])
          }
        }
      })
    },

    async getData() {
      await this.getUserBalance({userId: this.userId}).then(res => {
        this.balance = res.availableAmt || 0
        this.assetBalance = res.assetBalance || 0
      })

      await this.getPurchaseListForBuy().then(response => {
        this.pList = response.data;
      })
    },
    chooseMoney(num) {
      this.reqNum = num
    },
    goselectAll(){
      this.reqNum =this.balance/100;
    },
    buyClick() {
      if(this.reqNum==0){
        this.errDialog("Please enter the amount!")
        return
      }
      this.postNum = this.reqNum * 100
      if (this.item.name == this.$t('dw.t196')) {
        if(this.$route.query.type == 'withdraw') {
          if (this.postNum > this.assetBalance) {
            this.errDialog(this.$t('sa.txt370'))
            return;
          }
        }
        else if(this.selectedBalance) {
          if (this.postNum > this.balance) {
            this.errDialog(this.$t('sa.txt370'))
            return;
          }
        }
      } else {
        if (this.item.fundType == "定额") {
          this.reqNum = this.item.minimumBuy/100
          this.postNum = this.item.minimumBuy
        }
        if (this.postNum < this.item.minimumBuy || this.postNum > this.item.maximumBuy) {
          this.errDialog(this.$t('msg.inputAmount') + " : \n" + (this.item.minimumBuy / 100) + " - " + (this.item.maximumBuy / 100))
          return
        }

        if(this.voucherItem!=null && this.voucherItem.amount>0){

          if(this.selectedBalance && this.postNum > (this.balance + this.voucherItem.amount)) {
              this.errDialog(this.$t('sa.txt370'))
              return;
          }
        }else if (this.selectedBalance && this.postNum > this.balance) {
          this.errDialog(this.$t('sa.txt370'))
          return;
        }
      }
      if ( this.item.name == this.$t('dw.t196') )
        this.buy();
      else
        this.showPurchaseDetailDlg = true
      // this.verifyPayDlgOpen = true
    },
    // Choose Online Purchase
    // onlineClick() {
    //   this.selectedBalance = false
    // },
    // balanceClick() {
    //   this.selectedBalance = true
    // },
    // toRecharge() {
    //   this.$router.push({
    //     path: '/energy',
    //     query: {
    //       type: '1',
    //     },
    //   })
    // },

    toConfirmPaymentPwd() {
      this.showPurchaseDetailDlg = false
      this.buy();
    },
    async verifyPaymentPassword(){
      this.isLoading = true
      this.verifyPayPassword({password: this.payPassword}).then(res =>{
        this.isLoading = false
        if(res.code == 200) {
         this.buy()
        }
        else {
          this.errDialog(this.$t('msg.failedPayment'))
          this.verifyPayDlgOpen = false
          return false
        }
      })
    },
    toCoupon() {
        const user_id = this.userInfo.user_id;
        this.$router.push({
          path: '/coupon',
          query: {
            data:user_id
          },
        })
    },
    closeLucky(){
      this.showLucky = false;
      this.$router.push({
        path: '/fund',
      })
    },
    closeDraws(){
      this.showDraws = false;
      this.$router.push({
        path: '/fund',
      })
    },
    async buy() {
      this.verifyPayDlgOpen = false
      const user_id = this.userInfo.user_id
      if(this.item.name == this.$t('dw.t196')) {
        this.isLoading = true
        if(this.type == "withdraw") {
          const formData = {
            userId: user_id,
            balance: this.assetBalance,
            amount: this.postNum
          }
          this.transferOutAsset(formData).then(res => {
            this.isLoading = false
            if(res.code == 200) {
              this.errDialog(this.$t('msg.treasureWithdrawSuccess'))
              this.$router.push({
                path: '/profit',
              })
            }
            else {
              this.errDialog(res.msg)
              return
            }
          })
        } else {
          const formData = {
            userId: user_id,
            balance: this.assetBalance,
            amount: this.postNum
          }
          this.isLoading = true
          await this.transferInAsset(formData).then(res => {
            this.isLoading = false
            if(res.code == 200) {
              this.errDialog(this.$t('msg.addTreasureSuccess'))
              this.$router.push({
                path: '/profit',
              })
            }
            else {
              this.errDialog(res.msg)
            }
          })
        }
      } else {
        let type = this.selectedBalance ? 0 : 1
        let drawsId = 0;
        if(this.voucherItem!=null && this.voucherItem.amount>0){
          drawsId = this.voucherItem.id
        }
        const formData = {
          drawsId:drawsId,
          userId: user_id,
          productId: this.item.id,
          amount: this.postNum,
          type: type
        }
        this.isLoading = true
        this.addToPurchase(formData).then(res => {
          this.isLoading = false
          if(res.code == 200) {
            if(res.data.isVoucher =='0'){
              this.drawsAmount = res.data.voucherObainAmount;
              this.showDraws = true;

            }else if(res.data.isLucky == '0'){
              this.luckyAmount = res.data.luckyAmt;
              this.showLucky = true;
            }else{
              this.$router.push({
                path: 'fund',
              })
              if(this.pList.length<1){
                bus.$emit('openDialog', 1)
              }else{
                bus.$emit('openDialog', 5)
              }
            }
          }
          else {
            if(res.msg == 'Not enough Copies')
              this.errDialog(this.$t('msg.notEnoughCopies'))
            else
              this.errDialog(res.msg)
            return
          }
        })
      }
      return true
    },
    formatDate(val) {
      if (val == null)
        return
      const now = new Date(val)
      var year = now.getFullYear();       //年
      var month = now.getMonth() + 1;     //月
      var day = now.getDate();            //日
      var clock = '';
      if(day < 10)
        clock += "0";
      clock += day + "/";
      if(month < 10)
        clock += "0";
      clock += month + "/";
      clock += year
      return(clock);
    },
  },
}
</script>
<style>
div.van-popup--bottom.van-popup--round {
  border-radius: 0 0 0 0;
}
div.van-popup--bottom {
  background: #f3f3f2;
  box-shadow: inset 0px 2px 4px rgb(243, 243, 242);
  border-radius: 20px 20px 0px 0px;
}
</style>
