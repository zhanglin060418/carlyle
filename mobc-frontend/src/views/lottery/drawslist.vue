<template>
  <modMain :title="title" class="setting">
    <div class="record-main" v-if="drawsList && drawsList.length">
        <div slot="content" class="record-list" v-for="item in drawsList">
          <div class="h-top">
            <div class="left-box">
              <p class="orderNo" style="font-size:16px; color: #333;">{{ item.nameEn }}</p>
              <p v-if="item.type =='Voucher'" class="usdt"> Expired: {{ item.endDate}}</p>
              <p class="time">{{ parseEnDateTime(item.createTime) }}</p>
            </div>
            <div class="right-state">
              {{ stateText(item.status) }}
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
    },
  }
</script>
