<template>
  <modMain :title="$t('dw.t103')" class=" setting">
    <div class="main mission mis-upload">
      <div class="content video-wrap">
        <div class="mis-box fc">
          <p>1.{{ $t('dw.t104') }}</p>
          <p>
            2.{{ $t('dw.t105') }} {{ videoList.length }} {{ $t('dw.t106') }}
          </p>
        </div>
        <div class="mis-box fc share-content">
          <div class="video-main">
            <!-- <video
              id="media"
              class="vidwo-main"
              poster="static/assets/image/dw/video.png"
              :src="currentVideo ? currentVideo.url : ''"
              width="100%"
              height="100%"
            ></video> -->
            <div
              v-if="currentVideo && !isShowMask && !isPlayAll && !isStart"
              class="no-mask"
              id="playBtn"
              @click="playVideo"
            >
              <van-icon name="play-circle play" />
            </div>
            <div v-if="isShowMask" class="no-mask">
              <p class="t" v-if="isPlayAll">
                {{ $t('dw.t111') }}<br />
                {{ $t('dw.t112') }}
              </p>
              <p class="t" v-else>{{ $t('dw.t113') }}</p>
            </div>
          </div>
        </div>
        <div class="service-app marTop20 yellow">
          <p v-if="!cutDownTime && !isShowMask && !isPlayAll">
            {{ $t('dw.t107') }}
          </p>
          <p v-else-if="isPlayAll">{{ $t('dw.t114') }}</p>
          <van-count-down
            v-else
            class="write"
            :time="cutDownTime"
            @finish="finish"
          />
        </div>
        <div class="mis-box view-num">
          <div class="view-box">
            <p>
              <span>{{ todayCount }}</span
              >/{{ videoList.length }}
            </p>
            <p>{{ $t('dw.t108') }}</p>
          </div>
          <div class="view-box">
            <p>
              <span>{{ totalCount }}</span>
            </p>
            <p>{{ $t('dw.t110') }}</p>
          </div>
        </div>
      </div>
    </div>
  </modMain>
</template>
<script>
import { mapActions } from 'vuex'
export default {
  data() {
    return {
      isStart: false,
      isPlayAll: false,
      defaultId: [], //获取后台已经播放的id
      perMinute: 10, //间隔几分钟播放下一个广告毫秒
      //(1636642336819 - 1636641736819)/(1000*60)
      videoNum: 5, //默认5条视频
      videoList: [],
      showNextPlay: false,
      currentVideo: null,
      isShowMask: false,
      statusList: null,
      isMiss: false,
      todayCount: 0,
      todayTotalCount: 10,
      totalCount: 0,
      ableVideoList: [], //可以继续播放的视频
      cutDownTime: null,
      videoDom: null,
    }
  },
  created() {
    this.getVideo()
  },
  mounted() {
    let vm = this

    this.$nextTick(() => {
      this.videoDom = document.getElementsByTagName('video')[0]
      this.videoDom.addEventListener('ended', function() {
        //视频播放结束，调用接口，获取领奖机会
        vm.endVideoPlay()
        if (vm.currentVideo) {
          vm.isStart = false
          vm.videoDom.load()
        }
      })
    })
  },
  methods: {
    ...mapActions({
      queryAdTask: 'user/queryAdTask', // 查看是否领取状态
      missionCreate: 'user/missionCreate', // 领取奖励
      adqueryList: 'user/adqueryList', // 获取视频链接
    }),
    playVideo() {
      if (this.videoDom) {
        this.videoDom.play()
        this.isStart = true
      }
    },
    getVideo() {
      this.adqueryList({ page: 1, size: 100, enable: true }).then(res => {
        this.videoList = res.data.records || []
        this.getqueryAdTask()
      })
    },
    endVideoPlay() {
      let currentTime = new Date().getTime()
      localStorage.setItem('video_time', currentTime)
      this.paware()
    },
    paware() {
      this.missionCreate({
        finishCount: 1,
      }).then(res => {
        //看完领取
        this.getqueryAdTask()
      })
    },
    getqueryAdTask() {
      if (!this.userInfo || !this.userInfo.username) {
        return
      }
      this.queryAdTask().then(res => {
        let { todayCount, totalCount } = res.data || null
        this.todayCount = todayCount
        this.todayTotalCount = this.videoList.length
        this.totalCount = totalCount
        if (todayCount == 0) {
          //清楚缓存
          localStorage.removeItem('video_time')
          localStorage.removeItem('video_play_id')
        }
        if (this.todayCount < this.todayTotalCount) {
          this.getNextVideo()
        } else {
          // 今日广告观看次数已经完成
          this.playAll()
        }
      })
    },
    playAll() {
      this.isShowMask = true
      this.currentVideo = null
      this.cutDownTime = null
      this.isPlayAll = true
    },
    getNextVideo() {
      let cachCTime = Number(localStorage.getItem('video_time') || 0)
      let isGoon = true
      if (cachCTime > 0) {
        let currentTime = new Date().getTime()
        //计算间隔的分钟数
        let jgMin = (currentTime - cachCTime) / (1000 * 60)
        if (jgMin > this.perMinute) {
          //如果大于10分钟,就自动随机下一个未播放的视频，可以点击播放，
          isGoon = true
          this.isShowMask = false
        } else {
          //否则就倒计时
          isGoon = false
          this.isShowMask = true
          this.showNextPlay = false
          this.cutDownTime =
            this.perMinute * 60 * 1000 - (currentTime - cachCTime) //倒计时时间：10分钟-时间差
        }
      }
      if (!isGoon) return
      let ids = []
      let playingId = localStorage.getItem('video_play_id') || null
      if (playingId) {
        ids = playingId.split(',')
        this.defaultId = ids
      }
      let _list = this.videoList
      if (ids.length > 0) {
        _list = this.videoList.filter(d => !ids.includes(d.id))
      }
      this.ableVideoList = _list
      if (this.ableVideoList.length == 0) {
        // 当前所有视频均已播放完
        this.playAll()
        return
      } else {
        let maxNum = this.ableVideoList.length - 1
        let minNum = 0
        let idx = parseInt(Math.random() * (maxNum - minNum + 1) + minNum, 10)
        console.log('randomNum', idx)
        this.currentVideo = this.ableVideoList[idx]
        let id = this.currentVideo.id
        this.defaultId.push(id)
        localStorage.setItem('video_play_id', this.defaultId.join(','))
      }
    },
    finish() {
      this.showNextPlay = true
      this.isShowMask = false
      this.cutDownTime = null
      this.getqueryAdTask()
    },
  },
  beforeDestroy() {
    if (this.videoDom) {
      this.videoDom.pause()
    }
  },
}
</script>
