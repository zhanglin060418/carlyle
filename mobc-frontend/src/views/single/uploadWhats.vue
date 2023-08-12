<template>
  <modMain :title="$t('sa.txt232')" class=" setting">
    <div class="main mission  mis-upload ">
      <div class="content whats-wrap">
        <!-- <div class="mis-box fc">
          <p class="tips">
            1.Share to whatsapp group (group size is greater than 30).<br /><br />
            2.Take screenshots and share records, then upload screenshots to get
            rewards.<br /><br />
            3.Participate in the task once an hour, complete once to receive
            R0.3.
          </p>
        </div> -->
        <div class="mis-box upload-box flex-c">
          <p class="tips">
            1.Bagikan ke
            {{ type == 'TELEGRAM' ? 'Telegram Group' : 'WhatsApp Group' }}
            (ukuran grup lebih besar dari 30).<br /><br />
            2.Ambil tangkapan layar dan bagikan catatan, lalu unggah tangkapan
            layar untuk mendapatkan imbalan.<br /><br />
            3. Berpartisipasi dalam tugas satu jam sekali, selesaikan sekali
            untuk menerima Rp1000.
          </p>
          <hr />
          <div class="btns-list">
            <div class="w-list" v-for="(item, idx) in fileList" :key="idx">
              <div class="upload-btn share-img">
                <van-uploader
                  :after-read="afterRead"
                  accept="image/*"
                  :max-count="1"
                  :name="idx + 1"
                >
                  <img
                    v-if="item.imgUrl"
                    class="get"
                    v-lazy="imgBaseUrl + item.imgUrl"
                  />
                  <img
                    v-else
                    src="static/assets/image/xapp/xins/upload.png"
                    alt=""
                  />
                </van-uploader>
                <div
                  class="mask"
                  v-if="idx > 0 && (item.start || item.isWaiting)"
                >
                  <template v-if="item.start && time">
                    <van-count-down
                      :time="time"
                      class="yellow"
                      format="HH:mm:ss"
                      @finish="finish"
                    >
                    </van-count-down>
                  </template>
                  <p v-if="!item.start && item.isWaiting">
                    WAITING
                  </p>
                </div>
                <div class="mask" v-if="item.status == 'SUCCESS'">
                  <img src="static/assets/image/xapp/xins/icon_ok.png" alt="" />
                </div>
              </div>
              <p class="bouns">Bonus:+{{ sym }}1000</p>
            </div>
          </div>
          <p>Klik untuk mengunggah gambar</p>
          <p class="red" style="text-align:left;padding:0 5px" v-if="isFailed">
            Gambar {{ faildedList.join(',') }} tidak memenuhi persyaratan,
            silakan bagikan ke grup WA yang berbeda.
          </p>
        </div>
        <div class="mis-box fc share-content">
          <h2>{{ $t('dw.t99') }}</h2>
          <!-- <div class="step step1">
            <img src="static/assets/image/puzz/step1.png" alt="" />
            <p>
              1.{{ $t('dw.t100') }}
              {{ type == 'TELEGRAM' ? 'Telegram Group' : 'WhatsApp Group' }}.
            </p>
          </div>
          <div class="step step2">
            <img src="static/assets/image/puzz/step2.png" alt="" />
            <p>2.{{ $t('dw.t101') }}</p>
          </div>
          <div class="step step3">
            <img src="static/assets/image/puzz/step3.png" alt="" />
            <p>3.{{ $t('dw.t102') }}</p>
          </div> -->
          <p class="tip">
            *Harap unggah gambar yang benar sesuai aturan, jika tidak Anda tidak
            akan mendapatkan bonus
          </p>
          <!-- <div class="share-m" v-if="type == 'WHATSAPP'">
            <img src="static/assets/image/dw/whatapp.jpg" alt="" />
          </div> -->
          <!-- <div class="left">
            <div class="left-box">
              <p>
                1: Share to whatsapp group
              </p>
              <img src="static/assets/image/xapp/xins/down-arrow.png" alt="" />
            </div>
            <div class="left-box">
              <p>
                2: Save sharing records
              </p>
              <img src="static/assets/image/xapp/xins/down-arrow.png" alt="" />
            </div>
            <div class="left-box">
              <p>
                3: Upload
              </p>
              <img src="static/assets/image/xapp/xins/down-arrow.png" alt="" />
            </div>
            <div class="left-box">
              <p>
                4:Please wait for manual review
              </p>
            </div>
          </div>
          <div class="share-m">
            <p>For example</p>
            <img src="static/assets/image/xapp/whats-app-task.jpg" alt="" />
          </div>
          <p class="tip">
            *Please follow the rules to upload the correct whatsapp group
            picture, otherwise You won't get bonus
          </p> -->
        </div>
      </div>
    </div>
    <div class="inver-btn">
      <template v-if="type == 'TELEGRAM'">
        <div
          class="service-app whatsapp"
          data-sharer="telegram"
          :data-title="shareTitle"
          :data-url="shareLink"
        >
          <img src="static/assets/image/xapp/xins/tg.png" alt="" />
          <p>{{ $t('sa.txt77') }}</p>
        </div>
      </template>
      <template v-else>
        <div
          class="service-app whatsapp"
          data-sharer="whatsapp"
          :data-title="shareTitle"
          :data-url="shareLink"
        >
          <img src="static/assets/image/xapp/xins/whatsapp.png" alt="" />
          <p>{{ $t('sa.txt78') }}</p>
        </div>
      </template>
    </div>
  </modMain>
</template>
<script>
import mixinsSerivce from '@/mixins/service'
import client from '@/utils/http'
import { mapActions, mapGetters } from 'vuex'
export default {
  mixins: [mixinsSerivce],
  data() {
    return {
      sunshineImg: null,
      updateTime: '2021-09-18 23:04:20',
      time: null,
      showMask: true,
      fileList: [],
      beforeTime: '',
      hashtag: window.config.domain,
      isStarCount: false,
      registerbBonusMin: 0,
      registerbBonusMan: 0
    }
  },
  computed: {
    ...mapGetters({
      userInfo: 'user/userInfo',
    }),
    shareLink() {
      return window.config.domain + 's.html?c=' + this.userInfo.symbolCode
    },
    shareTitle() {
      return this.$t('dw.t158')+this.registerbBonusMin+'-'+this.registerbBonusMan+this.$t('dw.t1581')
    },
    faildedList() {
      return this.fileList.filter(d => d.status == 'FAILED').map(d => d.type)
    },
    isFailed() {
      return this.faildedList.length > 0
    },
    type() {
      return this.$route.query.type
    },
  },
  created() {
    this.getWhatsImg()
    this.getRegisterBonusValues()
  },
  mounted() {
    this.$nextTick(() => {
      window.Sharer.init()
    })
  },
  methods: {
    ...mapActions({
      systemTime: 'systemTime',
      queryWhatsAppPage: 'user/queryWhatsAppPage',
      registerBonusValue: 'user/getRegisterBonusValue'
    }),
    finish(time) {
      let imgObj = this.fileList.find(p => p.start)
      if (imgObj) {
        this.$set(imgObj, 'start', false)
      }
    },
    // 获取缓存图片
    getCachImg(idx) {
      let imgObj = this.fileList.find(p => p.sort == String(idx + 1))
      if (imgObj) {
        return imgObj
      }
      return null
    },
    async getRegisterBonusValues() {
      await this.registerBonusValue().then(res => {
        this.registerbBonusMin = res.registerbBonusMin
        this.registerbBonusMan = res.registerbBonusMan
      })
    },
    getWhatsImg() {
      this.queryWhatsAppPage({
        userId: this.userInfo.id,
        page: 1,
        size: 100,
        dateTime: this.getDateStr(new Date()),
        type: this.type,
      }).then(res => {
        let data = res.data.records || []
        if (data.length > 0) {
          this.fileList = data.sort(function(a, b) {
            return a.sort - b.sort // -1 升序排列
          })
        }
        this.fileList = this.fomatFileList()
        this.getCountTime()
      })
    },
    // 处理数据
    fomatFileList() {
      let newList = JSON.parse(JSON.stringify(this.fileList))
      let arr = []
      for (let i = 1; i < 5; i++) {
        let obj = newList[i - 1]
        if (!obj) {
          let emptyObj = {
            gmtCreate: '',
            id: '',
            imgUrl: '',
            localDate: '',
            status: 'PENDING',
            sort: String(i),
            type: this.type,
            isUpload: false,
            start: false,
            isWaiting: true,
          }
          if (!this.isStarCount) {
            emptyObj.start = true
            emptyObj.isWaiting = false
            this.isStarCount = true
          }
          arr.push(emptyObj)
        } else {
          this.beforeTime = obj.gmtCreate
          obj.start = false
          obj.isWaiting = false
          obj.isUpload = true
          arr.push(obj)
        }
      }
      return arr
    },
    // 获取倒计时时间戳
    getCountTime() {
      this.systemTime().then(res => {
        let sysTime = res.data || new Date().getTime()
        this.time = this.countDownTime(sysTime)
      })
    },
    getDateStr(timetamp, em = '-') {
      const add0 = m => {
        return m < 10 ? '0' + m : m
      }
      let date = timetamp ? new Date(timetamp) : new Date()
      let m = date.getMonth() + 1
      let d = date.getDate()
      return date.getFullYear() + em + add0(m) + em + add0(d)
    },
    /**
     * 获取倒计时
     * sysTime 服务器时间戳
     */
    countDownTime(sysTime) {
      if (this.fileList.length == 0 || !this.beforeTime) return 0
      sysTime = new Date(sysTime).getTime()
      let timestamp = new Date(this.beforeTime).getTime() //倒计时开始时间
      let h2 = 1 * 60 * 60 * 1000 //1hours
      let tnt = timestamp + h2 // 往后推几个小时
      let bTime = this.getDateStr(timestamp)
      let nTime = this.getDateStr(tnt)
      if (bTime != nTime) {
        //如果跨天
        let time12 = new Date(bTime + ' ' + '23:59:59').getTime()
        return time12 - sysTime
      } else {
        return tnt - sysTime
      }
    },
    async afterRead(files, detail) {
      let fileName = detail.name
      let folderName =
        (this.type == 'TELEGRAM' ? 'tggrouptask' : 'wagrouptask') +
        this.getDateStr(null, '')
      // let sunType = 'wsapp_shine_img_' + fileName
      this.$toast.loading({
        message: this.$t('sys.uploadding'),
        duration: 0,
        forbidClick: true,
      })
      let file = files.file
      console.log('上传file。。。。。。。。。。', file)
      let fileTypes = ['image/jpeg', 'image/pjpeg', 'image/png']
      if (!fileTypes.includes(file.type)) {
        this.errDialog(this.$t('sys.imgErr'))
        return
      }
      if (file.size.toFixed(2) > 1024 * 1024 * 6) {
        this.errDialog(this.$t('sys.uploadSize'))
        return
      }
      console.log('开始上传。。。。。。。。。。', file)
      client
        .postFormData('/upload/whatsapp', {
          file: file,
          folder: folderName,
          sort: String(fileName),
          type: this.type,
        })
        .then(res => {
          if (res.code == 0) {
            this.isStarCount = false
            this.showDialog(this.$t('sa.txt301'))
            this.getWhatsImg()
          } else {
            this.errDialog(res.msg)
          }
        })
        .catch(err => {})
        .finally(res => {
          // this.$toast.clear()
        })
    },
    // toService() {
    //   let _url = 'https://t.me/joinchat/gQ3I4pXaSUViZDVl'
    //   if (window.webkit) {
    //     window.webkit.messageHandlers.openBrowser.postMessage(_url)
    //   } else if (window.appInterface) {
    //     if (typeof window.appInterface.openBrowser === 'function') {
    //       window.appInterface.openBrowser(_url)
    //     } else if (typeof window.appInterface.goToBrowser === 'function') {
    //       window.appInterface.goToBrowser(_url)
    //     } else {
    //       window.location.href = _url
    //     }
    //   } else {
    //     window.location.href = _url
    //   }
    // },
  },
}
</script>
