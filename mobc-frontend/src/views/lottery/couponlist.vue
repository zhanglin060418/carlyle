<template>
  <modMain :title="title" class="setting">
    <div class="record-main" v-if="couponList && couponList.length">
        <div slot="content" class="record-list" v-for="item in couponList">
          <div class="h-top" style="background-color: #eaeeff">
            <div class="left-box">
              <p style="font-size:14px;color: #000000;" class="orderNo" >{{ item.nameEn }}</p>
              <p style="font-size:14px;" class="usdt" >{{$t('dw.t233')}}: {{ formatDate(item.endDate)}}</p>
              <p class="time">{{ parseEnDateTime(item.createTime) }}</p>
            </div>
            <div style="font-size:14px;" class="right-state" >
              <span v-if="item.status == 'Completed'" style="color: #71e2a3">Completed</span>
              <span v-else-if="item.status == 'Expired'" style="color: #fc0000">Expired</span>
              <span v-else-if="item.status == 'ToBeUsed'" @click="toBuy" style="color: #ffba00">To Be Used</span>
            </div>
          </div>
        </div>
    </div>
    <no-data v-else></no-data>
  </modMain>
</template>
<script>
  import {mapActions, mapGetters, mapMutations} from 'vuex'
  import {parseEnDateTime} from "../../utils/parseEnDateTime";
  export default {
    data() {
      return {
        sym: '',
        couponList: [],
        title: '',
        total: 0,
      }
    },
    computed: {
      ...mapGetters({
        userInfo: 'user/userInfo',
      }),
    },
    created() {
      this.sym = localStorage.getItem('localCurrency') || 'NGN'
      this.title ='Voucher'
      this.getList()
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
      parseEnDateTime,
      ...mapActions('user', {
        getCouponList: 'getCouponList', // 记录

      }),
      async getList() {
        let res =  await this.getCouponList()
        if (res.code == 200) {
          this.couponList= res.data;
        }
      },
      toBuy(){
        this.$router.push({
          path: '/fund',
        })
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
