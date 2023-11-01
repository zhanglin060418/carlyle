<template>
  <modMain :title="title" class="setting">
    <div class="record-main" v-if="drawsList && drawsList.length">
        <div slot="content" class="record-list" v-for="item in drawsList">
          <div class="h-top">
            <div class="left-box">
              <p style="font-size:14px;color: #000000;" class="orderNo" >{{ item.nameEn }}</p>
              <p v-if="item.type =='Voucher'" style="font-size:14px;" class="usdt" > Expired: {{ formatDate(item.endDate)}}</p>
              <p class="time">{{ parseEnDateTime(item.createTime) }}</p>
            </div>
            <div style="font-size:14px;" class="right-state" >
              <span v-if="item.status == 'Completed'" style="color: #71e2a3">Completed</span>
              <span v-else-if="item.status == 'Expired'" style="color: #fc0000">Expired</span>
              <span v-else-if="item.status == 'ToBeUsed'" style="color: #ffba00">To Be Used</span>
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
        drawsList: [],
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
      this.title ='Draws View'
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
        getDrawsList: 'getDrawsList', // 记录

      }),
      stateText(status) {
        switch (status) {
          case 'Expired':
            return 'Expired'
          case 'Completed':
            return 'Completed'
          case 'ToBeUsed':
            return 'To Be Used'
        }
      },
      async getList() {
        const user_id = this.userInfo.user_id
        let res =  await this.getDrawsList({
          userId: user_id
        })
        if (res.code == 200) {
          this.drawsList = res.data
        }
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
