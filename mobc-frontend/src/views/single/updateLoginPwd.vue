<template>
  <modMain :title="$t('home.resetPwd')" class="inside-page setting" :isSave="true" @save="submitForm" :activity="!disabled">
    <div class="padTop20">
      <template v-if="!$route.query.verifyCode">
        <p class="ipt-title">{{ $t('home.oldPwd') }}</p>
        <base-input type="password" v-model.trim="rawPassword" :isError="isErrorOldPwd" :placeholder="$t('home.enterOldPwd')"></base-input>
      </template>
      <p class="ipt-title">{{ $t('home.newPwd') }}</p>
      <base-input type="password" v-model.trim="newPassword" :isError="isErrorPwd1" name="newPasswor" :placeholder="$t('home.enternewPwd')"></base-input>
      <p class="ipt-title">{{ $t('home.confirmPwd') }}</p>
      <base-input type="password" v-model.trim="newPassword2" :isError="isErrorPwd1" name="newPassword2" :placeholder="$t('home.confirmNewPwd')"></base-input>
    </div>
    <div class="btn-box btn-fixed">
      <base-btn :disabled="disabled" @btnClick="submitForm" :btnTitle="$t('sys.submit')"></base-btn>
    </div>
    <loadding v-if="isLoading"></loadding>
  </modMain>
</template>
<script>
import { mapActions, mapGetters } from 'vuex'
export default {
  data() {
    return {
      isLoading: false,
      newPassword: '',
      newPassword2: '',
      rawPassword: '',
      isErrorOldPwd: false,
      isErrorPwd1: false,
      isErrorPwd2: false,
      username: '',
    }
  },
  computed: {
    ...mapGetters({
      userInfo: 'user/userInfo',
    }),
    areaItem() {
      return this.$store.getters.getAreaCode()
    },
    disabled() {
      if (!this.$route.query.verifyCode) {
        return (
          this.newPassword == '' ||
          this.rawPassword == '' ||
          this.newPassword2 == ''
        )
      }
      return this.newPassword == '' || this.newPassword2 == ''
    },
  },
  methods: {
    ...mapActions({
      updateLoginPwd: 'user/updateLoginPwd',
    }),
    async submitForm() {
      this.isErrorPwd1 = this.isVaild(this.newPassword)
      this.isErrorPwd2 = this.isVaild(this.newPassword2)
      if (this.isErrorPwd1 || this.isErrorPwd2) return
      if (this.newPassword != this.newPassword2) {
        this.errDialog(this.$t('msg.repwd'))
        return
      }
      let postData = {
        oldPwd: this.rawPassword,
        newPwd: this.newPassword,
      }
      this.isLoading = true
      let res = await this.updateLoginPwd(postData)
      this.isLoading = false
      if (res && res.code == 200) {
        this.errDialog(this.$t('sys.successSubmit'))
        this.$router.replace('/setting')
      } else {
        this.errDialog(this.$t('msg.failedPwdSubmit'))
      }
    },
    isVaild(value) {
      return !this.onFailed({ name: 'password', value: value })
    },
  },
}
</script>
