<template>
  <modMain :title="$t('sa.txt232')" class=" share-up-main setting">
    <div class="main mission mis-upload">
      <div class="content">
        <div class="mis-box fc">
          <p class="tips" v-if="type == 'JOIN_TG_GROUP'">
            1：Bergabunglah dengan grup TG, ambil tangkapan layar dan rekam
            setelah bergabung<br /><br />
            2：Unggah record untuk menerima hadiah: Rp6000, setiap pengguna
            dibatasi 1 kali
          </p>
          <p class="tips" v-else-if="type == 'YOUTUBE'">
            1. Buat video dan publikasikan ke YouTube. <br /><br />
            2. Persyaratan video: Perlu menjelaskan fungsi Wind Pro.<br /><br />
            3: Kirim tautan video YouTube <br /><br />
            Tip: Jika Anda tidak tahu cara membuat video, silakan berkonsultasi
            Pelanggan mendapatkan layanan video lengkap"
          </p>
          <p class="tips" v-else-if="type == 'TELEGRAM' || type == 'WHATS_APP'">
            <!-- 1. Anggota grup harus lebih dari 30 <br /><br />
            2. Pengenalan Super Wind App dirilis dalam grup. <br /><br />
            3. Nama grup disatukan sebagai "Petsworld-Your ID" <br /><br />
            4. Unggah tangkapan layar, menunggu ulasan. -->
          </p>
          <p class="tips" v-else-if="type == 'FACE_BOOK' || type == 'TWITTER'">
            1. Bagikan ke {{ type == 'FACE_BOOK' ? 'Facebook' : 'Twitter' }},
            kontennya berisi tautan undangan<br /><br />
            2. Unggah tangkapan layar, menunggu ulasan.
          </p>
          <p class="tips" v-else>
            1. Setelah berhasil membagikan ke {{ title }}, ambil tangkapan layar
            Berbagi catatan<br /><br />
            2. Unggah tangkapan layar dan terima hadiah setelah disetujui
          </p>
        </div>
        <template v-if="type != 'YOUTUBE'">
          <div class="mis-box upload-box flex-c">
            <div class="upload-btn share-img">
              <van-uploader
                :after-read="afterRead"
                accept="image/*"
                :max-count="1"
              >
                <img
                  class="get"
                  v-if="sunshineImg"
                  v-lazy="imgBaseUrl + sunshineImg"
                  alt=""
                />
                <img v-else src="static/assets/image/xapp/xins/upload2.png" alt="" />
              </van-uploader>
            </div>
            <p>Klik untuk mengunggah gambar</p>
          </div>
          <div class="mis-box fc share-content">
            <!-- <div class="left">
              <div class="left-box">
                <p v-if="type == 'JOIN_TG_GROUP'">
                  1: Join Telegram group
                </p>
                <p v-else>1: Share to {{ title }}</p>
                <img src="static/assets/image/xapp/xins/down-arrow.png" alt="" />
              </div>
              <div class="left-box">
                <p v-if="type == 'JOIN_TG_GROUP'">
                  2: Screenshot the group record
                </p>
                <p v-else>
                  2:Take a screenshot of the sharing record
                </p>
                <img src="static/assets/image/xapp/xins/down-arrow.png" alt="" />
              </div>
              <div class="left-box">
                <p v-if="type == 'JOIN_TG_GROUP'">
                  3: Upload
                </p>
                <p v-else>3: Upload</p>
                <img src="static/assets/image/xapp/xins/down-arrow.png" alt="" />
              </div>
              <div class="left-box">
                <p v-if="type == 'JOIN_TG_GROUP'">
                  4: Please wait for manual review
                </p>
                <p v-else>4:Get the reward after review</p>
              </div>
            </div>
            <div class="share-m">
              <p>For example</p>
              <img
                v-if="type == 'FACE_BOOK'"
                src="static/assets/image/xapp/facebook.jpg"
                alt=""
              />
              <img
                v-if="type == 'TWITTER'"
                src="static/assets/image/xapp/twitter.jpg"
                alt=""
              />
              <img
                v-if="type == 'WHATS_APP'"
                src="static/assets/image/xapp/whats-app-task.jpg"
                alt=""
              />
              <img
                v-if="type == 'JOIN_TG_GROUP'"
                src="static/assets/image/xapp/tggroup-s.jpg"
                alt=""
              />
            </div> -->
            <h2>{{ $t('dw.t99') }}</h2>
            <!-- <div class="step step1">
              <img src="static/assets/image/puzz/step1.png" alt="" />
              <p>1.{{ $t('dw.t100') }} {{ title }}</p>
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
              *Harap unggah gambar yang benar sesuai aturan, jika tidak Anda
              tidak akan mendapatkan bonus
            </p>
            <!-- <div class="share-m" v-if="type == 'JOIN_TG_GROUP'">
              <img src="static/assets/image/dw/tg-group.jpg" alt="" />
            </div> -->
          </div>
        </template>
        <template v-else>
          <div class="mis-box fc">
            <div class="youtube">Silakan masukkan tautan video YouTube</div>
            <baseInput
              type="text"
              v-model="url"
              placeholder="Enter YouTube video link"
            ></baseInput>

            <div class="service-app marTop20" @click="saveYoutube">
              <p>{{ $t('sys.submit') }}</p>
            </div>
          </div>

          <div class="mis-box fc video">
            <div class="video-tip">Sebagai contoh</div>
            <div class="video-main">
              <!-- <video
                src=" https://superwind-003.s3-accelerate.amazonaws.com/video/youtubeexper.mp4"
                poster="static/assets/image/dw/video.png"
                width="100%"
                height="100%"
                controls
              ></video> -->
            </div>
          </div>
        </template>
      </div>
    </div>

    <div class="inver-btn">
      <div
        v-if="type == 'JOIN_TG_GROUP'"
        class="service-app"
        @click="toService('tggroup')"
      >
        <img src="static/assets/image/xapp/xins/tg.png" alt="" />
        <p>{{ $t('sa.txt77') }} {{ $t('sa.txt177') }}</p>
      </div>

      <div v-else class="btn" @click="share()">Share</div>
    </div>
    <sharePopup :show="showShare" @close="close" :code="userInfo.symbolCode">
    </sharePopup>
  </modMain>
</template>
<script>
import client from '@/utils/http'
import mixinsSerivce from '@/mixins/service'
import { mapActions, mapGetters } from 'vuex'
export default {
  mixins: [mixinsSerivce],
  data() {
    return {
      url: '',
      sunshineImg: null,
      showShare: false,
      link: '',
    }
  },
  computed: {
    ...mapGetters({
      userInfo: 'user/userInfo',
    }),
    title() {
      let t = 'Facebook'
      if (this.type == 'FACE_BOOK') {
        t = 'Facebook'
      } else if (this.type == 'TWITTER') {
        t = 'Twitter'
      } else if (this.type == 'WHATS_APP') {
        t = 'WhatsApp group'
      } else if (this.type == 'TELEGRAM') {
        t = 'Telegram group'
      } else if (this.type == 'JOIN_TG_GROUP') {
        t = 'Telegram group'
      }
      return t
    },
  },
  created() {
    this.sunshineImg = localStorage.getItem('sun_shine_img')
    this.type = this.$route.query.type || ''
    if (this.type == 'YOUTUBE') {
      this.url = localStorage.getItem('sun_shine_img2') || ''
    } else if (this.type == 'FACE_BOOK') {
      this.sunshineImg = localStorage.getItem('sun_shine_img1')
    } else if (this.type == 'TWITTER') {
      this.sunshineImg = localStorage.getItem('sun_shine_img3')
    } else if (this.type == 'WHATS_APP') {
      this.sunshineImg = localStorage.getItem('sun_shine_img4')
    } else if (this.type == 'TELEGRAM') {
      this.sunshineImg = localStorage.getItem('sun_shine_img8')
    } else if (this.type == 'JOIN_TG_GROUP') {
      this.sunshineImg = localStorage.getItem('sun_shine_img5')
    }
  },
  methods: {
    ...mapActions({
      sunshineYoutube: 'user/sunshineYoutube',
    }),
    async afterRead(files) {
      this.$toast.loading({
        message: this.$t('sys.uploadding'),
        duration: 0,
        forbidClick: true,
      })
      let file = files.file
      let folderName = 'fbtwtg' + this.$utils.getCurrentTimeStr('')
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
        .postFormData('/upload/sunshine', {
          file: file,
          type: this.type,
          folder: folderName,
        })
        .then(res => {
          console.log('上传返回信息。。。。。。。。。。', res)
          if (res.code == 0) {
            this.sunshineImg = res.data
            if (this.type == 'FACE_BOOK') {
              localStorage.setItem('sun_shine_img1', this.sunshineImg)
            } else if (this.type == 'YOUTUBE') {
              localStorage.setItem('sun_shine_img2', this.sunshineImg)
            } else if (this.type == 'TWITTER') {
              localStorage.setItem('sun_shine_img3', this.sunshineImg)
            } else if (this.type == 'WHATS_APP') {
              localStorage.setItem('sun_shine_img4', this.sunshineImg)
            } else if (this.type == 'TELEGRAM') {
              localStorage.setItem('sun_shine_img8', this.sunshineImg)
            } else if (this.type == 'JOIN_TG_GROUP') {
              localStorage.setItem('sun_shine_img5', this.sunshineImg)
            }
            this.showDialog(this.$t('sa.txt301'))
          } else {
            this.errDialog(res.msg)
          }
        })
        .catch(err => {})
        .finally(res => {
          // this.$toast.clear()
        })
    },
    share() {
      this.showShare = true
      // let _ver = Number(localStorage.getItem('app_version') || 0)
      // if (_ver >= 103) {
      //   this.showShare = true
      // } else {
      //   if (window.appInterface) {
      //     this.shareApp()
      //   } else {
      //     this.showShare = true
      //   }
      // }
    },
    // shareApp() {
    //   let _content = `${this.$t('dw.t158')}：${this.link}`
    //   if (window.webkit) {
    //     window.webkit.messageHandlers.shareSocial.postMessage(_content)
    //   } else if (window.appInterface) {
    //     window.appInterface.shareSocial(_content)
    //   } else {
    //     console.log(_content)
    //     this.$copyText(_content).then(
    //       e => {
    //         this.showDialog(
    //           'The sharing link has been copied successfully, you can go to paste and share'
    //         )
    //       },
    //       function(e) {
    //         console.log(e)
    //       }
    //     )
    //   }
    // },

    close() {
      this.showShare = false
    },
    saveYoutube() {
      let isUrl = /^https?:\/\/([a-zA-Z0-9]+\.)+[a-zA-Z0-9]+/.test(this.url)

      if (!isUrl) {
        this.errDialog('Please enter a valid and correct YouTuBe link')
        return
      }
      this.sunshineYoutube({ url: this.url }).then(res => {
        if (res.code == 0) {
          localStorage.setItem('sun_shine_img2', this.url)
          this.showDialog(this.$t('sa.txt302'))
        }
      })
    },
  },
}
</script>
