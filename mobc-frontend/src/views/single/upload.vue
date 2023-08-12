<template>
  <modMain :title="$t('sa.txt232')" class=" setting">
    <div class="main mission mis-upload">
      <div class="content">
        <div class="mis-box fc">
          <p class="tips">
            1.Setelah {{ type  == 'RECHARGE' ? 'Top-up' : 'withdrawal' }} adalah
            berhasil, ambil tangkapan layar untuk membagikan rekaman<br /><br />
            2.Unggah catatan berbagi, klik untuk menerima bonus setelah manual
            tinjauan
          </p>
        </div>
        <div class="mis-box upload-box flex-c">
          <div class="upload-btn share-img">
            <van-uploader :after-read="afterRead" accept="image/*" :max-count="1">
              <img v-if="sunshineImg" class="get" v-lazy="imgBaseUrl + sunshineImg" alt="" />
              <img v-else src="static/assets/image/xapp/xins/upload.png" alt="" />
            </van-uploader>
          </div>
          <p>Klik untuk mengunggah gambar</p>
        </div>
        <div class="mis-box fc share-content">
          <!-- <div class="left">
            <div class="left-box">
              <p>
                1: Share withdrawal records in the TG group
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
            <img src="static/assets/image/xapp/recharge-task.jpg" alt="" />
          </div> -->
          <h2>{{ $t('dw.t99') }}</h2>
          <!-- <div class="step step1">
            <img src="static/assets/image/puzz/step1.png" alt="" />
            <p>1.{{ $t('dw.t100') }} .</p>
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
            *Please upload the correct picture according to the rules, otherwise
            you won’t get the bonus
          </p>
        </div>
        <div class="service-app" @click="toService('tggroup')">
          <img src="static/assets/image/xapp/xins/tg.png" alt="" />
          <p>{{ $t('sa.txt77') }} {{ $t('sa.txt177') }}</p>
        </div>
      </div>
    </div>
  </modMain>
</template>
<script>
import mixinsSerivce from '@/mixins/service'
import client from '@/utils/http'
export default {
  mixins: [mixinsSerivce],
  data() {
    return {
      sunshineImg: null,
      type: 'RECHARGE',
    }
  },
  created() {
    this.type = this.$route.query.type || 'RECHARGE'
    let key = this.type == 'RECHARGE' ? 'sun_shine_img6' : 'sun_shine_img7'
    this.sunshineImg = localStorage.getItem(key) || null
  },
  methods: {
    async afterRead(files) {
      this.$toast.loading({
        message: this.$t('sys.uploadding'),
        duration: 0,
        forbidClick: true,
      })
      let file = files.file
      let folderName = 'rewith' + this.$utils.getCurrentTimeStr('')
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
          folder: folderName,
          type: this.type,
        })
        .then(res => {
          console.log('上传返回信息。。。。。。。。。。', res)
          if (res.code == 0) {
            this.sunshineImg = res.data
            let key =
              this.type == 'RECHARGE' ? 'sun_shine_img6' : 'sun_shine_img7'
            localStorage.setItem(key, res.data)

            this.showDialog(this.$t('sa.txt301'))
          } else {
            this.errDialog(res.msg)
          }
        })
        .catch(err => { })
        .finally(res => {
          // this.$toast.clear()
        })
    },
  },
}
</script>
