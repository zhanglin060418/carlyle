<template xmlns:v-clipboard="http://www.w3.org/1999/xhtml">
    <div>

        <div slot="content">
            <div class="show-invite-content">
                <div class="copylink">
                    <p>{{ copyLink }}</p>
                </div>
                <div class="copy-btn" @click="onCopyLink">
                    Copy
                </div>
            </div>
        </div>
        <div style="text-align: center;" >
            <img :src="userQrcode"  alt="" />
            <p>UID:{{userId}}</p>
        </div>
    </div>
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
    this.getCaptchaQrcodes()
  },
  data() {
    return {
      active: 0,
      showPopup: false,
      userId: 0,
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
      this.userId = this.userInfo.user_id;
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
