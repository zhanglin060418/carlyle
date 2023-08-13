<template>
  <div class="task" ref="scrollbox">
    <div class="ban-header">
      <span class="leftImg"><img src="/static/assets/image/logo.png" alt="" class="logo" /></span>
      <h3>carlyle</h3>
      <span class="secondImg"><img src="/static/assets/image/icon_selected_circle@2x.png" alt="" class="logo"/></span>
    </div>
    <div class="ban-box">
      <div class="h-banner" @click="$router.push('/fund')">
        <img src="https://www.carlyle.com/themes/carlyle_2020/logo.svg" alt="" class="logo" />
      </div>
    </div>
    <div class="home-content">
      <div class="text-main">
        <div v-for="item in list" class="text-box" v-if="item.status == 0">
          <img :src="imgBaseUrl + item.coverImage" alt="" />
          <p v-if="$i18n.locale=='zh_CN'" class="ql-snow ql-editor" v-html="item.description" />
          <p v-else class="ql-snow ql-editor" v-html="item.descriptionEn" />
        </div>
        <div class="text-box footer">
          <hr />
          <p>
            © 1987-2023 Carlyle LP. All Rights Reserved.
          </p>
        </div>
      </div>
    </div>
    <van-popup
        v-model="showNotice"
        round
        position="bottom"
        safe-area-inset-bottom
        class="popup-announcement"
        :style="{ height:'60%',width:'96%', background: '#eaeeff',margin: '0% 2% 40% 2% ' }"
    >
      <div class="sell-buy popup-box energy-popup">
        <div class="head">
          <div class="left">
            <img class="noticeImg" src="static/assets/image/logo.png" alt="" />
            <div class="title">
              Notice
            </div>
          </div>
          <div class="right">
            <img class="close" src="static/assets/image/dw/close_circle@3x.png" alt="" @click="closeNotic"/>
          </div>
        </div>
        <div class="content ql-snow" style="font-size:medium">
          <p class="ql-snow ql-editor" v-html="loginAnnouncement.noticeContent"/>
        </div>
      </div>
    </van-popup>
    <CustomerService/>
  </div>
</template>
<script>
// import panicBuy from '../components/panicBuy'
import task from './task'
import proItemCom from './proItem.vue'

import { mapActions, mapGetters } from 'vuex'
import mixinsSerivce from '@/mixins/service'
import {parseTime} from "../../utils/parseTime";
export default {
  mixins: [mixinsSerivce],
  components: {
    // panicBuy,
    task,
    proItemCom,
  },
  data() {
    return {
      sym: '',
      showNotice: false,
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
      loginAnnouncement: null,
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
  watch: {
    '$route'() {
      if (this.$route.name == 'home')
        this.getData()
    }
  },
  created() {
    this.sym = localStorage.getItem('localCurrency') || 'NGN'
    this.getData()
    let token = localStorage.getItem('token') || null
    if(token != null) {
      this.showNoticePop()
    }
  },
  methods: {
    parseTime,
    ...mapActions({
      getHomeIndex: 'user/getHomeIndex',
      getHomeAnnouncementItem: 'user/getHomeAnnouncement'
    }),
    onClick() {
      this.showNotice = false
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
    showNoticePop() {
      if (this.$route.path == '/home' && !sessionStorage.getItem('_t_notic')) {
        this.showNotice = true
        sessionStorage.setItem('_t_notic', 1)
      }
    },
    closeNotic() {
      this.showNotice = false
      sessionStorage.setItem('_t_notic', 2)
      if(sessionStorage.getItem('isFirstLogin') == '1' && sessionStorage.getItem('_t_notic') == 2) {
        this.$dialog
            .confirm({
              title: '',
              message: this.$t('msg.firstLoginSuccess'),
              cancelButtonText: this.$t('sys.cancel'),
              confirmButtonText: this.$t('sys.confirm'),
            })
            .then(() => {
              sessionStorage.setItem('isFirstLogin', '0')
              this.$router.replace({
                path: '/setting',
                query: {
                  type: this.$route.query.type,
                  backPath: '/home'
                }
              })
            })
            .catch(() => {
              // on cancel
            })
      }
    },
    async getData() {
      await this.getHomeIndex().then(res =>{
        this.list = res.data
      })
      await  this.getHomeAnnouncementItem().then(res => {
        this.loginAnnouncement = res.loginAnnouncement
      })
    },
    async getList(start, end, currentTime) {
      console.log(start, end)
      this.commodityQuery(this.param).then(res => {
        let data = res.data.records || []
        this.sourceData = JSON.parse(JSON.stringify(data))
        let _list = data.filter(d => {
          let s = this.getTime(d.startTime)
          console.log('s, start,end,currentTime', s, start, end, currentTime) //&& d.enable
          console.log('isbool=', s >= start && s < end && currentTime >= s)
          return s >= start && s < end && currentTime >= s
        })
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
