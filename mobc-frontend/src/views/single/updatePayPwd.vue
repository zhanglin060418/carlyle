<template>
  <modMain :title="hasPayPwd ? $t('home.resetPayPwd') : $t('home.setPayPwd')" class="inside-page setting" :isSave="true" @save="submitForm" :activity="!disabled">
    <div class="padTop20">
      <p v-if="hasPayPwd == 'true'" class="ipt-title">{{ $t('home.phoneNumber') }}</p>
      <div v-if="hasPayPwd == 'true'" class="flex">
<!--        <base-phone-->
<!--            v-model.trim="phoneNo"-->
<!--            type="number"-->
<!--            :placeholder="$t('home.enterMobile') "-->
<!--        ></base-phone>-->
        <base-input disabled v-model.trim="phoneNo" name="inviteCode"></base-input>
        <div style="width: 50px; height: 40px; border-radius: 12px; border:1px solid #3376c4; margin-left: 8px; display: flex; justify-content: center; align-items: center" >
          <img v-if="clickedPhoneVerify" src="/static/assets/image/phone_verify.png" style="width: 100%; height: 100%" @click="btnVerifyPhone">
          <img v-else src="/static/assets/image/phone_verify_disable.png" style="width: 100%; height: 100%">
        </div>
      </div>
      <p v-if="hasPayPwd == 'true'"  class="ipt-title">{{ $t('home.verifyCode') }}</p>
      <baseSMS
          v-if="hasPayPwd == 'true'"
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
      isErrorOldPwd: false,
      isErrorPwd1: false,
      isErrorPwd2: false,
      username: '',
      phoneNo: '',
      sendSmsClicked: false,
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
      if (!this.$route.query.verifyCode) {
        return (
            this.newPassword == '' ||
            this.newPassword2 == ''
        )
      }
      return this.newPassword == '' || this.newPassword2 == ''
    },
  },
  created() {
    this.phoneNo = this.userInfo.username
    this.hasPayPwd = localStorage.getItem('payment_password')
    this.setPhoneVerifyClicked(true)
  },
  methods: {
    ...mapActions({
      updatePayPwd: 'user/updatePayPwd',
      setPayPwd: 'user/setPayPwd',
      verifyUsername: 'user/verifyUsername',
      codeVerify: 'user/codeVerify',
      phoneVerify: 'user/phoneVerify',
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
        username: this.phoneNo,
      }).then(res =>{
        this.isLoading = false
        if(res.code == 500) {
          this.errDialog(this.$t('msg.invalidUser'))
          return
        }
        this.sendSmsClicked = true
      })
      if(this.sendSmsClicked) {
        this.isLoading = true
        await this.codeVerify({
            phoneNo: this.phoneNo,
            useMethod: 'updatePayPwd'
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
      if(this.hasPayPwd == 'true') {
        if(!this.sendSmsClicked) {
          this.errDialog(this.$t('msg.sendVerifyRequest'))
          return
        }
      }
      this.isErrorPwd1 = this.isVaild(this.newPassword)
      this.isErrorPwd2 = this.isVaild(this.newPassword2)
      if (this.isErrorPwd1 || this.isErrorPwd2) return
      if (this.newPassword != this.newPassword2) {
        this.errDialog(this.$t('msg.repwd'))
        return
      }
      if(this.hasPayPwd == 'true') {
        let phoneVerifyRes = await this.phoneVerify({
          phoneNo: this.phoneNo,
          verifyCode: this.verifyPhone,
          useMethod: 'updatePayPwd'
        })
        this.isLoading = false
        if (phoneVerifyRes.code == 500) {
          if (phoneVerifyRes.msg == 'Please click phone verify button and receive the verify code')
            this.errDialog(this.$t('msg.sendVerifyRequest'))
          else if (phoneVerifyRes.msg == 'Phone verify code invalid!')
            this.errDialog(this.$t('msg.invalidVerifyCode'))
          else if (phoneVerifyRes.msg == 'Verify code expired. Please receive verify code!')
            this.errDialog(this.$t('msg.expiredVerifyCode'))
          return
        }
        this.isLoading = true
        const user_id = this.userInfo.user_id
        let res = await this.setPayPwd({
          userId: user_id,
          password: this.newPassword
        })
        if (res && res.code == 200) {
          this.isLoading = false
          this.errDialog(this.$t('sys.successSubmit'))
          localStorage.setItem('payment_password', 'true')
          this.$router.push('/profit')
        } else {
          this.errDialog(this.$t('msg.failedPwdSubmit'))
        }
      }
      else {
        this.isLoading = true
        this.isLoading = false
        const user_id = this.userInfo.user_id
        this.isLoading = true
        let res = await this.setPayPwd({
          userId: user_id,
          password: this.newPassword
        })
        if(res.code == 200) {
          this.isLoading = true
          this.errDialog(this.$t('sys.successSubmit'))
          localStorage.setItem('payment_password', 'true')
          this.updateUser({
            ...this.userInfo,
            hasPayPwd: true
          })
          let backPath = this.$route.query.backPath || ''
          if (backPath != '') {
            this.$router.push(backPath)
          }
          else
            this.$router.push('/profit')
        }
      }
    },
    isVaild(value) {
      return !this.onFailed({ name: 'password', value: value })
    },
  },
}
</script>
