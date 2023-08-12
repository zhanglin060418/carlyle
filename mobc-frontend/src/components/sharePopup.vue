<template xmlns:v-clipboard="http://www.w3.org/1999/xhtml">
  <van-popup v-model="currentShow" round position="bottom" @open="open" @close="close" @click-overlay="close" safe-area-inset-bottom class="popup-sell-buy" ref="sharePopup">
    <div class="sell-buy popup-box">
      <!-- <div v-if="showheader" class="popup-header">
        <p v-if="title">{{ title }}</p>
        <slot name="title"></slot>
      </div> -->
      <div class="head">
        <div class="title">
          {{ $t('dw.t22') }}
        </div>

        <div class="qrcodeCss">
          <img :src="userQrcode" width="180px" high="180px" alt="">
        </div>
      </div>
      <div class="popup-content" slot="content">
        <div class="share-content">
          <div class="copylink">
            <p>{{ copyLink }}</p>
          </div>
    <!--      <div class="copy-btn" v-clipboard:copy="copyLink" v-clipboard:success="onCopy" v-clipboard:error="onError">
            Copy
          </div>-->
          <div class="copy-btn" @click="onCopyLink">
            Copy
          </div>
          <div class="share-logo">
            <p class="bawah">{{ $t('sa.txt315') }}</p>
            <div class="logos-share" ref="logoShare">
              <div class="s-logo" data-sharer="whatsapp" :data-title="shareTitle" :data-url="shareLink">
                <img src="static/assets/image/xapp/logo-wa.png" alt="" />
                <p>WhatsApp</p>
              </div>
              <div class="s-logo" data-sharer="telegram" :data-title="shareTitle" :data-url="shareLink">
                <img src="static/assets/image/xapp/logo-tg.png" alt="" />
                <p>Telegram</p>
              </div>
              <div class="s-logo" data-sharer="facebook" :data-hashtag="hashtag" :data-title="shareTitle" :data-url="shareLink">
                <img src="static/assets/image/xapp/logo-fb.png" alt="" />
                <p>Facebook</p>
              </div>
              <div class="s-logo" data-sharer="twitter" :data-hashtag="hashtag" :data-title="shareTitle" :data-url="shareLink">
                <img src="static/assets/image/xapp/logo-tt.png" alt="" />
                <p>Twitter</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </van-popup>
</template>

<script>
import {mapActions, mapGetters} from "vuex";

export default {
  props: {
    show: {
      type: Boolean,
      default: false,
    },
    showheader: {
      type: Boolean,
      default: true,
    },
    closeable: {
      type: Boolean,
      default: false,
    },
    height: {
      type: String,
      default: '70%',
    },
    iconName: {
      type: String,
      default: 'zhutu',
    },
    title: {
      type: String,
      default: 'Share link',
    },
    link: {
      type: String,
      default: '',
    },
    code: {
      type: String,
      default: '',
    },
  },
  created() {
    this.getRegisterBonusValues(),
    this.getCaptchaQrcodes()
  },
  data() {
    return {
      active: 0,
      showPopup: false,
      currentShow: false,
      hashtag: window.config.domain,
      inviteCode: '',
      userQrcode: '',
      registerbBonusMin: 0,
      registerbBonusMan: 0
    }
  },
  computed: {
    ...mapGetters({
      userInfo: 'user/userInfo',
    }),

    copyLink() {
      //this.$copyText(window.config.domain + '#/register?c=' + this.userInfo.inviteCode, this.$refs.sharePopup);
      return window.config.domain + '#/register?c=' + this.userInfo.inviteCode
      // return this.userInfo.inviteCode
    },
    shareLink() {
      return window.config.domain + '#/register?c=' + this.userInfo.inviteCode
    },
    shareTitle() {
      return this.$t('dw.t158')+this.registerbBonusMin+'-'+this.registerbBonusMan+this.$t('dw.t1581')
    },
  },
  watch: {
    show() {
      this.currentShow = this.show
    }
  },
  methods: {
    ...mapActions({
      registerBonusValue: 'user/getRegisterBonusValue',
      getCaptchaQrcode:'user/getCaptchaQrcode'
    }),
    onCopy() {
      this.showDialog(this.$t('home.copySuccess'))
    },
    onError(){
      this.showDialog(this.$t('home.copyError'))
    },

    onCopyLink(){
      var input = document.createElement('input');
      input.value = window.config.domain + '#/register?c=' + this.userInfo.inviteCode;
      document.body.appendChild(input);
      input.select();
      document.execCommand('copy');
      this.showDialog(this.$t('home.copySuccess'));
      document.body.removeChild(input);
    },
    async getRegisterBonusValues() {
      await this.registerBonusValue().then(res => {
        this.registerbBonusMin = res.registerbBonusMin
        this.registerbBonusMan = res.registerbBonusMan
      })
    },
    async getCaptchaQrcodes() {
      const domain = window.config.domain
      let res = await this.getCaptchaQrcode({
        domain :domain
      })
      this.userQrcode ='data:image/jpg;base64,' +  res.userQrcode
    },

    open() {
      this.$nextTick(() => {
        window.Sharer.init()
      })
    },
    close() {
      this.$emit('close')
    },
  },
}
</script>
