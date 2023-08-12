<template>
  <div class="verification done">
    <img v-if="type == '1' || type == '2'" src="static/assets/image/icon/icon_grrz_wc@2x.png" alt="">
    <img v-else src="static/assets/image/icon/icon_grrz_wc_failed.png" alt="">
    <p v-if="type == '1'" class="mark">{{$t('bill.deposisuccess')}}</p>
    <p v-if="type == '0'" class="mark">{{$t('bill.deposifailed')}}</p>
    <p v-if="type == '2'" class="mark">{{$t('bill.withdrawalsuccess')}}</p>
    <div class="btn-box btn-fixed">
      <base-btn :btnTitle="$t('bill.complete')" :disabled="false" @btnClick="btnClick('/profit')"></base-btn>
      <base-btn :btnTitle="$t('bill.viewrecord')" :disabled="false" class="deposit" @btnClick="btnClick('/inOutList')"></base-btn>
    </div>
  </div>
</template>
<script>
import { bus } from '@/utils/bus'
import {mapActions} from "vuex";
export default {
  data() {
    return {
      type: '',
      requestNo: ''
    }
  },
  created() {
    this.type = this.$route.query.type
    this.requestNo = this.$route.query.requestNo
    if(this.type == 1) {
      //这里不需要修改，页面跳转不影响任何数据更新，避免状态维护错误
     // this.rechargeConfirm()
    }
  },
  methods: {
    ...mapActions({
      rechargeSuccess: 'user/rechargeSuccess',
    }),
    btnClick(path) {
      this.$router.replace({
        path: path,
        query: {
          type: this.$route.query.type,
          backPath: '/profit'
        }
      })
    },
    async rechargeConfirm() {
      await this.rechargeSuccess({requestNo: this.requestNo})
    }
  },
}
</script>
