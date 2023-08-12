<template>
  <div class="task" ref="scrollbox">
    <!-- <dw-head /> -->
    <div class="h-banner">
      <img
        @click="$router.push('/fund')"
        src="static/assets/image/bcf/10016.jpg"
        alt=""
      />
    </div>
    <!-- <div class="panic-buy">
      <panic-buy
        from="home"
        :type="type"
        :item="list[0]"
        @callback="refresh"
      ></panic-buy>
    </div> -->
    <div class="tab-list">
      <div class="m-box" @click="toGo('/energy', '1')">
        <img src="static/assets/image/wind/icon/icon-r.png" alt="" />
        <p>{{ $t('sa.txt69') }}</p>
      </div>
      <div class="m-box" @click="toGo('/energy', '2')">
        <img src="static/assets/image/wind/icon/icon-w.png" alt="" />
        <p>{{ $t('sa.txt70') }}</p>
      </div>
      <div
        class="m-box "
        v-if="userInfo && userInfo.username"
        @click="$router.push('/invite')"
      >
        <img class="img-money" src="static/assets/image/wind/money-gif.gif" alt="" />
        <p>{{ $t('dw.t22') }}</p>
      </div>
      <div class="m-box" v-else @click="$router.replace('/login')">
        <img class="img-money" src="static/assets/image/wind/money-gif.gif" alt="" />
        <p>Sign in</p>
      </div>
    </div>
    <proItemCom v-if="proItem" :item="proItem" :list="proList" />
    <task v-if="$route.path == '/home'" />
    <div class="kf">
      <div @click="toService('tggroup')">
        <img src="static/assets/image/xapp/logo-tg.png" alt="" />
      </div>
      <div @click="toService('whats')">
        <img src="static/assets/image/xapp/logo-wa.png" alt="" />
      </div>
    </div>
    <!-- <lucky-draw /> -->
    <van-popup
      v-model="showNotic"
      @closed="closeNotic"
      class="popup-result limit-rule notic"
    >
      <!-- <img
        class="poput-close"
        src="static/assets/image/dw/close.png"
        alt=""
        @click="showNotic = false"
      /> -->
      <div class="content">
        <!-- <img class="abs hg" src="static/assets/image/hg.png" alt="" /> -->
        <img class="abs nbot" src="static/assets/image/n_bot.png" alt="" />
        <!-- <div class="poput-rule">
          <h2>{{ $t('dw.t150') }}</h2>
          <template v-for="(d, i) in $t('dw.t151')">
            <p :key="i" class="nh" v-html="i + 1 + '.' + d"></p>
          </template>
        </div> -->
        <div class="intive-box">
          <div class="left">
            <img src="static/assets/image/task/intive.png" alt="" />
          </div>
          <div class="right">
            <template>
              <p>Undang teman untuk mendapatkan lebih banyak pendapatan</p>
              <div class="intive-btn" @click="onClick('INVITE')">
                mengundang
              </div>
              <p class="red">Setiap undangan nyata, Bonus+{{ sym }}500</p>
            </template>
          </div>
        </div>
      </div>
    </van-popup>
  </div>
</template>
<script>
// import panicBuy from '../components/panicBuy'
import task from './task'
import proItemCom from './proItem.vue'
import { mapActions, mapGetters } from 'vuex'
import mixinsSerivce from '@/mixins/service'
export default {
  mixins: [mixinsSerivce],
  components: {
    // panicBuy,
    task,
    proItemCom,
  },
  data() {
    return {
      showNotic: false,
      param: {
        page: 1,
        size: 50,
        type: 'LIMIT', //普通商品
      },
      list: [],
      type: 0,
      sourceData: null,
      proList: [],
      proItem: null,
      oldScrollTop: 0,
    }
  },
  computed: {
    ...mapGetters({
      userInfo: 'user/userInfo',
    }),
  },
  // beforeRouteEnter(to, from, next) {
  //   next(vm => {
  //     vm.refresh()
  //     vm.getData()
  //   })
  // },
  created() {
    if (this.$route.path == '/home') {
      // this.refresh()
      this.getData()
      this.getProList()
    }
  },
  mounted() {
    this.showNoticPop()
  },
  methods: {
    ...mapActions({
      getUser: 'user/query',
      queryAccountList: 'user/queryAccountList',
      commodityQuery: 'order/commodityQuery',
      queryLimitPurchased: 'order/queryLimitPurchased',
    }),
    onClick() {
      this.showNotic = false
      this.$router.push({
        path: 'invite',
      })
    },
    getProList() {
      this.commodityQuery({
        page: 1,
        size: 50,
        type: 'NORMAL',
      }).then(res => {
        let data = res.data.records || []
        this.proList = data.filter(
          d => d.enable && d.actualPrice != 100000 && d.id != 1
        )
        this.proItem = data.filter(d => d.enable && d.id != 1)[0]
      })
    },
    showNoticPop() {
      if (this.$route.path == '/home' && !sessionStorage.getItem('_t_notic')) {
        this.showNotic = true
        sessionStorage.setItem('_t_notic', 1)
      }
    },
    closeNotic() {
      sessionStorage.setItem('_t_notic_num', 1)
    },
    async getData() {
      await this.getUser()
      await this.queryAccountList()
    },
    async getList(start, end, currentTime) {
      console.log(start, end)
      this.commodityQuery(this.param).then(res => {
        let data = res.data.records || []
        this.sourceData = JSON.parse(JSON.stringify(data))
        console.log('sourceData', data)
        let _list = data.filter(d => {
          let s = this.getTime(d.startTime)
          console.log('s, start,end,currentTime', s, start, end, currentTime) //&& d.enable
          console.log('isbool=', s >= start && s < end && currentTime >= s)
          return s >= start && s < end && currentTime >= s
        })
        console.log('11111', _list)
        let row = _list.sort((a, b) => {
          return b.limitVolume - a.limitVolume
        })
        this.getBuyQuery(row)
      })
    },
    isLogin() {
      if (!this.userInfo || !this.userInfo.username) {
        this.$router.push('/login')
        return false
      }
      return true
    },
    toGo(path, type) {
      if (this.isLogin()) {
        if (type) {
          this.$router.push({
            path: path,
            query: {
              type: type,
            },
          })
        } else {
          this.$router.push(path)
        }
      }
    },
    async getBuyQuery(list) {
      console.log('listlistlistlist', list)
      let _list = list
      let ids = list.map(d => d.id)
      if (ids.length && this.userInfo && this.userInfo.username) {
        let res = await this.queryLimitPurchased({ commodityIdList: ids })
        if (res.code == 0) {
          let pids = res.data || []
          console.log('pids=', pids)
          if (pids.length > 0) {
            _list = list.filter(d => !pids.includes(String(d.id)))
          }
        }
      }
      if (_list.length == 0) {
        _list = this.sourceData
      }
      _list = _list.filter(d => {
        let cTime = this.getTime()
        let s = this.getTime(d.startTime)
        let e = this.getTime(d.endTime)
        console.log('cTime,s', cTime, s)
        console.log('cTime,s', cTime <= s)
        return cTime >= s && cTime < e
      })
      if (_list.length == 0) {
        _list = this.sourceData
      }
      this.list = _list
      console.log('结果22==', this.list)
    },
    getTime(hours = null) {
      let d = new Date()
      let year = d.getFullYear()
      let month = d.getMonth() + 1
      let date = d.getDate()
      let getDate = year + '/' + month + '/' + date
      let h = d.getHours() > 9 ? d.getHours() : '0' + d.getHours
      let min = d.getMinutes() > 9 ? d.getMinutes() : '0' + d.getMinutes()
      let currentTime = hours
        ? getDate + ' ' + hours
        : getDate + ' ' + h + ':' + min
      return new Date(currentTime).getTime()
    },
    // 根据当前时间获取一个抢购宠物
    refresh() {
      let type = 0
      let currentTime = this.getTime()
      console.log('currentTime==', currentTime)
      if (
        currentTime >= this.getTime('8:00') &&
        currentTime < this.getTime('12:00')
      ) {
        type = 0
      } else if (
        currentTime >= this.getTime('12:00') &&
        currentTime < this.getTime('17:00')
      ) {
        type = 1
      } else if (
        currentTime >= this.getTime('17:00') &&
        currentTime <= this.getTime('21:00')
      ) {
        type = 2
      }
      this.type = type
      let start = type == 0 ? '8:00' : type == 1 ? '12:00' : '17:00'
      let end = type == 0 ? '12:00' : type == 1 ? '17:00' : '21:00'
      this.getList(this.getTime(start), this.getTime(end), currentTime)
    },
  },
}
</script>
