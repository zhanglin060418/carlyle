<template>
  <modMain :title="$t('home.resetPwd')" class="inside-page setting" :isSave="true" @save="submitForm" :activity="!disabled">
    <div class="padTop20">
      <p class="ipt-title">{{ $t('home.phoneNumber') }}</p>
      <div class="flex">
        <base-phone
            v-model.trim="phoneNo"
            type="number"
            :placeholder="$t('home.enterMobile') "
        ></base-phone>
        <div style="width: 120px; height: 40px; border-radius: 12px; border:1px solid #3376c4; margin-left: 8px; display: flex; justify-content: center; align-items: center" >
<!--          <img v-if="clickedPhoneVerify" src="/static/assets/image/phone_verify.png" style="width: 100%; height: 100%" @click="btnVerifyPhone">-->
<!--          <img v-else src="/static/assets/image/phone_verify_disable.png" style="width: 100%; height: 100%">-->
          <span v-if="clickedPhoneVerify" @click="btnVerifyPhone" style="color: #0a364a">Get OTP</span>
          <span v-else >Please enter your OTP</span>
        </div>
      </div>
      <p class="ipt-title">{{ $t('home.verifyCode') }}</p>
      <baseSMS
          type="number"
          v-model.trim="verifyPhone"
          :placeholder="$t('home.phoneVerifyCode')"
          :timer="!clickedPhoneVerify"
      ></baseSMS>
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
import {mapActions, mapGetters, mapMutations} from 'vuex'
export default {
  data() {
    return {
      isLoading: false,
      hasPayPwd: true,
      newPassword: '',
      newPassword2: '',
      areaCode: '',
      phoneNo: '',
      isErrorPwd1: false,
      isErrorPwd2: false,
      sendSmsClicked: false,
      username: '',
      verifyPhone: ''
    }
  },
  computed: {
    ...mapGetters({
      userInfo: 'user/userInfo',
      clickedPhoneVerify: 'getClickedPhoneVerifyEvent'
    }),
    areaItem() {
      return this.$store.getters.getAreaCode()
    },
    disabled() {
      return this.newPassword == '' || this.newPassword2 == '' || this.phoneNo == ''
    },
  },
  created() {
    this.hasPayPwd = this.userInfo.hasPayPwd
    this.setPhoneVerifyClicked(true)
  },
  methods: {
    ...mapActions({
      updatePayPwd: 'user/updatePayPwd',
      setPayPwd: 'user/setPayPwd',
      codeVerify: 'user/codeVerify',
      resetLoginPwd: 'user/resetLoginPwd',
      phoneVerify: 'user/phoneVerify',
      verifyUsername: 'user/verifyUsername',
    }),
    ...mapMutations({
      updateUser: 'user/UPDATE_USER',
      setPhoneVerifyClicked: 'SET_PHONE_VERIFY',
    }),
    async btnVerifyPhone() {
      if (
          this.phoneNo.length < 5 ||
          this.phoneNo.length > 20 ||
          !/^[0-9]*$/.test(this.phoneNo)
      ) {
        this.isErrorMobile = true
        this.errDialog(this.$t('msg.mobile'))
        return
      }
      this.areaCode = this.areaItem.code
      this.isLoading = true
      await this.verifyUsername({
        username: this.areaCode + this.phoneNo,
      }).then(res =>{
        this.isLoading = false
        if(res.code == 200){
            this.sendSmsClicked = true
        }else{
          this.sendSmsClicked = false
          this.errDialog(this.$t('msg.invalidUser'))
          return
        }
      })
      if(this.sendSmsClicked) {
        this.isLoading = true
        await this.codeVerify({
          phoneNo: this.areaCode + this.phoneNo,
          useMethod: 'forgetLoginPwd'
        }).then(res => {
          this.isLoading = false
          if (res.code == 200) {
            this.setPhoneVerifyClicked(false)
          }else{
            this.errDialog(res.msg)
          }
        })
      }
    },
    async submitForm() {
      if(!this.sendSmsClicked) {
        this.errDialog(this.$t('msg.sendVerifyRequest'))
        return
      }
      this.isErrorPwd1 = this.isVaild(this.newPassword)
      this.isErrorPwd2 = this.isVaild(this.newPassword2)
      if (this.isErrorPwd1 || this.isErrorPwd2) return
      if (this.newPassword != this.newPassword2) {
        this.errDialog(this.$t('msg.repwd'))
        return
      }
      this.isLoading = true
      let phoneVerifyRes = await this.phoneVerify({
        phoneNo: this.areaCode + this.phoneNo,
        verifyCode: this.verifyPhone,
        useMethod: 'forgetLoginPwd'
      })
      this.isLoading = false
      if (phoneVerifyRes.code == 500) {
        if(phoneVerifyRes.msg == 'Please click phone verify button and receive the verify code')
          this.errDialog(this.$t('msg.sendVerifyRequest'))
        else if(phoneVerifyRes.msg == 'Phone verify code invalid!')
          this.errDialog(this.$t('msg.invalidVerifyCode'))
        else if (phoneVerifyRes.msg == 'Verify code expired. Please receive verify code!')
          this.errDialog(this.$t('msg.expiredVerifyCode'))
        return
      }
      let postData = {
        phoneNo: this.areaCode + this.phoneNo,
        newPwd: this.newPassword,
      }
      let res = await this.resetLoginPwd(postData)
      if (res && res.code == 200) {
        this.isLoading = false
        this.errDialog(this.$t('sys.successSubmit'))
        this.$router.push('/login')
      } else {
        this.errDialog(this.$t('msg.failedPwdSubmit'))
        return
      }
    },
    isVaild(value) {
      return !this.onFailed({ name: 'password', value: value })
    },
  },
}
</script>
