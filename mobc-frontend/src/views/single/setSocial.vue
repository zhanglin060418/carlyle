<template>
  <modMain :title="$t('home.setContact')" class="inside-page setting" :isSave="true" @save="submitForm" :activity="!disabled">
    <div class="padTop20">
      <p class="ipt-title">{{ $t('home.emailAddress') }}</p>
      <base-input v-model.trim="emailAddress" :placeholder="$t('home.enterEmail')"></base-input>
      <p class="ipt-title">{{ $t('home.whatsApp') }}</p>
      <base-input v-model.trim="whatsApp" name="newPasswor" :placeholder="$t('home.enterWhatsApp')"></base-input>
      <p class="ipt-title">{{ $t('home.telegram') }}</p>
      <base-input v-model.trim="telegram" name="telegram" :placeholder="$t('home.enterTelegram')"></base-input>
      <p class="ipt-title">{{ $t('sa.txt351') }}</p>
      <base-input disabled v-model.trim="inviteCode" name="inviteCode" v-clipboard:copy="inviteCode" v-clipboard:success="copy"></base-input>
    </div>
    <div class="btn-box btn-fixed">
      <base-btn :disabled="disabled" @btnClick="submitForm" :btnTitle="$t('sys.submit')"></base-btn>
    </div>
  </modMain>
</template>
<script>
import {mapActions, mapGetters, mapMutations} from 'vuex'
export default {
  data() {
    return {
      whatsApp: '',
      telegram: '',
      emailAddress: '',
      inviteCode: ''
    }
  },
  created() {
    this.emailAddress = this.userInfo.email
    this.whatsApp = this.userInfo.whatsApp
    this.telegram = this.userInfo.telegram
    this.inviteCode = this.userInfo.inviteCode
  },
  computed: {
    ...mapGetters({
      userInfo: 'user/userInfo',
    }),
    areaItem() {
      return this.$store.getters.getAreaCode()
    },
    disabled() {
      return this.emailAddress == ''
    },
  },
  methods: {
    ...mapActions({
      setSocial: 'user/setSocial',
    }),
    ...mapMutations({
      updateUser: 'user/UPDATE_USER'
    }),
    copy(val) {
      this.showDialog(this.$t('home.copySuccess'))
    },
    isEmailValid(email) {
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
      return emailRegex.test(email)
    },
    async submitForm() {
      if (!this.isEmailValid(this.emailAddress)) {
        this.errDialog(this.$t('msg.email'))
        return
      }
      const user_id = this.userInfo.user_id
      const postData = {
        userId: user_id,
        email: this.emailAddress,
        whatsApp: this.whatsApp,
        telegram: this.telegram,
      }
      this.setSocial(postData).then(res =>{
        if(res.code == 200) {
          this.errDialog(this.$t('sys.successSubmit'))
          this.updateUser({
            ...this.userInfo,
            email: this.emailAddress,
            email: this.emailAddress,
            whatsApp: this.whatsApp,
            telegram: this.telegram,
          })
          this.$router.push('/setting')
        }
      })
    }
  },
}
</script>
