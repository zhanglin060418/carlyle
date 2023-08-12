<template>
  <modMain :title="$t('sa.txt35')" class="noPadding">
    <div class="main lucky">
      <!-- <div class="guang guang1"></div>
    <div class="guang guang2"></div> -->
      <div class="luck-img">
        <img class="xz" src="static/assets/image/lucky/xz.png" alt="" />
        <img
          class="lucky-draw"
          src="static/assets/image/lucky/lucky-top.png"
          alt=""
        />
      </div>
      <div class="top-main">
        <div class="top-new">
          <i class="icon-laba"></i>
          <!-- <div class="n-swiper" ref="swiper">
          <p v-for="item in news"><a src="javascript:void(0)">{{item.title}}</a></p>
        </div> -->
          <van-swipe
            class="company-swiper"
            ref="companySwiper"
            style="height:40px;"
            :autoplay="3000"
            indicator-color="white"
            :show-indicators="false"
            vertical
          >
            <template v-for="item in news">
              <van-swipe-item>
                <a src="javascript:void(0)">{{ item.title }}</a>
              </van-swipe-item>
            </template>
          </van-swipe>
        </div>
      </div>
      <!-- 大转盘抽奖 -->
      <div class="wheel" style="position:relative;z-index:1;">
        <div class="draw-main">
          <div class="fag">
            <img v-show="showFg" src="static/assets/image/lucky/fg1.png" alt="" />
            <img v-show="!showFg" src="static/assets/image/lucky/fg2.png" alt="" />
          </div>
          <div class="draw-content">
            <div class="tip">
              <p>Each lottery consumes one lottery count</p>
            </div>
            <div class="luck-map">
              <LuckyGrid
                ref="LuckDraw"
                width="331px"
                height="242px"
                :prizes="prizes"
                :buttons="buttons"
                :blocks="blocks"
                :default-config="defaultConfig"
                :default-style="defaultStyle"
                :active-style="activeStyle"
                @start="startCallBack"
                @end="endCallBack"
              />
            </div>
          </div>
        </div>
      </div>
      <div class="draw-btn" @click="startCallBack">
        {{
          luckyDraws > 0
            ? luckyDraws + $t('dw.t134')
            : $t('dw.t135') + sym + '2000'
        }}
      </div>
      <div class="tips">
        <h3>{{ $t('dw.t146') }}</h3>
        <template v-for="(d, i) in $t('dw.t145')">
          <p :key="i">{{ d }}</p>
        </template>
        <p>{{ $t('dw.t137') }}:</p>
        <div class="tables">
          <table>
            <tr>
              <td>Arowana2/Monkey4</td>
              <td>：30.00%</td>
              <td>Cobra3</td>
              <td>：20.00%</td>
            </tr>
            <tr>
              <td>Cobra4</td>
              <td>：23.00%</td>
              <td>Arowana5</td>
              <td>：4.99%</td>
            </tr>
            <tr>
              <td>Arowana3</td>
              <td>：5.00%</td>
              <td>Arowana4</td>
              <td>：15.00%</td>
            </tr>
            <tr>
              <td>Cobra5</td>
              <td>：2.00%</td>
              <td>Arowana6</td>
              <td>：0.01%</td>
            </tr>
          </table>
        </div>
      </div>
    </div>
    <van-popup
      v-model="showPrize"
      class="dw-popup"
      :close-on-click-overlay="false"
    >
      <div class="head">{{ $t('dw.t138') }}</div>
      <!-- <img
        class="poput-close"
        src="static/assets/image/dw/close.png"
        alt=""
        @click="showPrize = false"
      /> -->
      <div class="popup-main prize-success">
        <div class="qg-popup">
          <div class="content">
            <div class="infos">
              <div class="neirong" v-if="prizeObj">
                {{ $t('dw.t136') }} {{ prizeObj.name
                }}<img :src="imgBaseUrl + prizeObj.shredImgUrl" alt="" />X1
              </div>
              <div class="popup-btn" @click="showPrize = false">
                {{ $t('sys.ok') }}
              </div>
            </div>
          </div>
        </div>
      </div>
    </van-popup>
  </modMain>
</template>
<script>
import { mapActions, mapGetters, mapMutations } from 'vuex'
export default {
  data() {
    return {
      showPrize: false,
      news: [],
      showFg: false,
      prizes: [],
      defaultStyle: {
        borderRadius: 7,
        fontColor: '#3B3B43',
        fontSize: '12px',
        fontWeight: '500',
        textAlign: 'center',
        background: '#FFFADE',
        // shadow: '0 5 0 #E17BB9',
      },
      blocks: [
        { padding: '4px', background: '#100574', borderRadius: 7 },
        { padding: '0px', background: '#100574', borderRadius: 7 },
        { padding: '0px', background: '#100574', borderRadius: 7 },
      ],
      defaultConfig: {
        gutter: 3,
        speed: 10,
        accelerationTime: 1000,
        decelerationTime: 1000,
      },
      activeStyle: {
        background: 'linear-gradient(270deg, #FF9504, #E23323)',
        shadow: '0 5 0 #AD2B20',
        fonts: {
          fontColor: '#ffffff',
        },
      },
      buttons: [
        {
          x: 1,
          y: 1,
          // background: '#EA536E',
          // background: '#ea4f1b',
          background: 'linear-gradient(180deg, #FF9504, #E23323)',
          shadow: '0 5 0 #AD2B20',
          fonts: [
            {
              text: 'Start',
              fontColor: '#ffffff',
              top: '25%',
              fontSize: '30px',
            },
          ],
        },
      ],
      prize: [],
      resultIndex: 0,
      newIndex: 0,
      luckyNum: 0,
      timmer: null,
      timmer2: null,
      isStart: false,
      prizeList: [],
      prizeObj: null,
      prizeId: '',
    }
  },
  computed: {
    ...mapGetters({
      userInfo: 'user/userInfo',
    }),
    luckyDraws() {
      return this.userInfo && this.userInfo.luckyDraws
        ? this.userInfo.luckyDraws
        : 0
    },
  },
  // activated() {
  //   if (this.userInfo && this.userInfo.username) {
  //     this.$store.dispatch('user/query')
  //   }
  // },
  created() {
    this.getUser()
  },
  mounted() {
    this.timmer2 = setInterval(() => {
      this.showFg = !this.showFg
    }, 200)

    this.getPrizeList()
  },
  // beforeRouteEnter(to, from, next) {
  //   if (window.appInterface) {
  //     window.appInterface.setStatusColor('#1b2789')
  //   }
  //   next()
  // },
  // beforeRouteLeave(to, from, next) {
  //   if (window.appInterface) {
  //     window.appInterface.setStatusColor('#ffffff')
  //   }
  //   next()
  // },
  methods: {
    ...mapActions({
      getUser: 'user/query',
      luckyDraw: 'user/luckyDraw',
      queryLucky: 'user/queryLucky',
      fragmentList: 'user/templatequeryList',
    }),
    ...mapMutations({
      updateLucky: 'user/updateLucky',
    }),
    getPrizeList() {
      this.prize = []
      // let prizesData = []
      // this.queryLucky().then(res => {
      //   this.prize = []
      //   if (res.code == 0) {
      //     let data = res.data || []
      //     data.forEach(d => {
      //       let { currency, amount, number, unit } = d
      //       this.prize.push(amount)
      //       let nameTxt = `${currency} ${number}${unit}`
      //       if (d.amount == 4000000) {
      //         prizesData.push({ name: nameTxt })
      //       } else {
      //         prizesData.push({
      //           name: nameTxt,
      //           img: require('@/assets/image/lucky/zs.png'),
      //         })
      //       }
      //     })
      //   }
      // })
      let prizesData = []
      const prizes = []
      this.fragmentList({ page: 1, size: 9, luckyDraw: 1 }).then(res => {
        let row = res.data.records || []
        this.prizeList = row
        if (row.length) {
          this.news = this.getbroadData()
        }
        if (row.length) {
          let arr = row.filter(
            d => d.id == '1453623844672681241' || d.id == '1453623844672681234'
          )
          let arr2 = row.filter(
            d => d.id != '1453623844672681241' && d.id != '1453623844672681234'
          )
          console.log(arr, arr2)
          let name = ''
          let img1 = ''
          let ids = ''
          let imgArr = []
          arr.forEach((d, i) => {
            // name.push(d.name)
            // img1.push(d.nashredImgUrlme)
            name = name ? name + '\nor ' + d.name : d.name
            img1 = d.shredImgUrl
            ids = ids ? ids + '|' + d.id : d.id
            imgArr.push(d.shredImgUrl)
          })
          this.prize.push(ids)
          prizesData.push({
            name: name,
            background: '#FFEBF7',
            shadow: '0 5 0 #E17BB9',
            img: img1,
            imgArr: imgArr,
          })
          arr2.forEach((d, i) => {
            // d.shredImgUrl = this.imgBaseUrl + d.shredImgUrl
            let obj = {
              name: d.name,
              background: i % 2 != 0 ? '#DCEAFF' : '#FFEBF7',
              shadow: i % 2 != 0 ? '0 5 0 #6897ED' : '0 5 0 #E17BB9',
              img: d.shredImgUrl,
            }
            this.prize.push(d.id)
            prizesData.push(obj)
          })

          // 模拟接口请求奖品列表
          setTimeout(() => {
            // const prizesData = [
            //   { name: 'R1', background: '#FFEBF7', shadow: '0 5 0 #E17BB9' },
            //   { name: 'R10', background: '#DCEAFF', shadow: '0 5 0 #6897ED' },
            //   {
            //     name: 'R500',
            //     background: '#FFEBF7',
            //     shadow: '0 5 0 #E17BB9',
            //     img: require('@/assets/image/lucky/zs.png'),
            //   },
            //   { name: 'R2', background: '#DCEAFF', shadow: '0 5 0 #6897ED' },
            //   { name: 'R50', background: '#FFEBF7', shadow: '0 5 0 #E17BB9' },
            //   { name: 'R5', background: '#DCEAFF', shadow: '0 5 0 #6897ED' },
            //   { name: 'R20', background: '#FFEBF7', shadow: '0 5 0 #E17BB9' },
            //   { name: 'R200', background: '#DCEAFF', shadow: '0 5 0 #6897ED' },
            // ]
            console.log('prizesData=', prizesData)
            this.luckyNum = 1
            let axis = [
              [0, 0],
              [1, 0],
              [2, 0],
              [2, 1],
              [2, 2],
              [1, 2],
              [0, 2],
              [0, 1],
            ]
            for (let i = 0; i < 8; i++) {
              let item = prizesData[i]
              let pobj = {
                index: i,
                x: axis[i][0],
                y: axis[i][1],
                background: item.background,
                shadow: item.shadow,
                fonts: [
                  {
                    text: item.name,
                    top: '30%',
                    wordWrap: false,
                  },
                ],
                // imgs: [{ src: item.img, width: '55%', top: '8%' }]
              }
              if (item.img) {
                pobj.fonts[0].top = '60%'
                // pobj.fonts[0].fontColor = '#EA536E'
                pobj.imgs = [
                  {
                    src: this.imgBaseUrl + item.img,
                    width: '26px',
                    height: '30px',
                    top: '10%',
                  },
                ]
              }
              if (item.imgArr && item.imgArr.length > 1) {
                pobj.imgs = [
                  {
                    src: require('@/assets/image/puzz/zuhe.png'),
                    width: '76px',
                    height: '30px',
                    top: '10%',
                  },
                ]
                // item.imgArr.forEach((d, i) => {
                //   pobj.imgs.push({
                //     src: d,
                //     width: '26px',
                //     height: '30px',
                //     top: i == 0 ? '10%' : '30%',
                //   })
                // })
              }
              prizes.push(pobj)
            }
            this.prizes = prizes
          }, 0)
        }
      })
    },
    startCallBack() {
      if (this.isStart) return
      this.isStart = true
      this.prizeObj = null
      if (this.userInfo && this.userInfo.username) {
        this.luckyDraw().then(res => {
          if (res.code == 0) {
            this.prizeId = res.data
            let index = this.prize.findIndex(d => d.indexOf(res.data) > -1)
            this.resultIndex = index
            this.$refs.LuckDraw.play()
            this.stop()
          } else {
            this.errDialog(res.msg)
          }
        })
      } else {
        this.$router.push('/login')
      }
    },
    randomNum(minNum, maxNum) {
      switch (arguments.length) {
        case 1:
          return parseInt(Math.random() * minNum + 1, 10)
          break
        case 2:
          return parseInt(Math.random() * (maxNum - minNum + 1) + minNum, 10)
          break
        default:
          return 0
          break
      }
    },
    getbroadData(n = 20) {
      let arr = []
      for (let i = 0; i < n; i++) {
        arr.push(this.buildObj())
      }
      return arr
    },
    buildObj() {
      let random = () => {
        let demo = ''
        //这里取的是4位，所以for循环4次
        for (let i = 0; i < 9; i++) {
          //设置随机数范围,这设置为0 ~ 9
          let a = Math.floor(Math.random() * 9)
          //拼接字符串
          demo += a
        }
        return '****' + demo.substring(7, 9)
      }
      let n = this.randomNum(1, 50) // 1-50随机数
      let pre = [12, 14, 15, 16, 21, 23, 25, 31, 43, 85]
      let idx = this.randomNum(0, 9)
      // 12（CTBC电话公司）、14（巴西电信）、15（TELEFÔNICA）、16（CETERP）、21（ENBRATEL）、23（INTELIG）、25（GVT）、31（TELEMAR）、43（SERCOMTEL）、85（VÉSPER）
      let prizeId = this.randomNum(0, 7)
      // let price = this.prize[prizeId]
      let pObj = this.prizeList[prizeId]
      let name = pre[idx] + random()
      let lang = localStorage.getItem('locale') || 'en_US'
      // 用户88***888   邀请了6个好友,领取了3R$
      // 用户88***888   下级收益提成xxR$
      // let idxs = Math.floor(Math.random(0,2))
      let obj = {
        title: this.$t('sa.txt477') + ` ${name} draws to get ${pObj.name}`,
      }
      return obj
    },
    endCallBack(data) {
      console.log('抽奖结果', data)
      //根据抽奖id获取抽奖结果
      let obj = this.prizeList.find(d => d.id == this.prizeId)
      obj.shredImgUrl = obj.shredImgUrl
      this.prizeObj = obj
      this.showPrize = true
      this.$refs.LuckDraw.stop(-1)
    },
    stop() {
      setTimeout(() => {
        // this.resultIndex = Math.random() * 8 >> 0
        // console.log(this.resultIndex)
        this.$refs.LuckDraw.stop(this.resultIndex)
        this.getUser()
        this.isStart = false
      }, 1000)
    },
  },
  beforeDestroy() {
    this.timmer && clearInterval(this.timmer)
    this.isStart = false
  },
}
</script>
