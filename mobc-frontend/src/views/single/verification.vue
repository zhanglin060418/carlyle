<template>
  <modMain :title="$t('home.verification')" class="inside-page noPadding">
    <div class="verif-main">
      <p>
        {{ $t('home.verifications') }}
      </p>
      <p class="send-code">{{ $t('home.sendTo') }}{{ txtPhone }}</p>
      <base-send
        type="text"
        :sendData="form"
        v-model="code"
        :placeholder="$t('home.verCode')"
        ref="sendPhone"
        :isError="isErrorCode"
      ></base-send>
      <base-btn
        :disabled="disabled"
        @btnClick="onSubmit"
        :btnTitle="$t('sys.submit')"
      ></base-btn>
    </div>
  </modMain>
</template>
<script>
import { mapGetters, mapActions, mapMutations } from 'vuex'

export default {
  data() {
    return {
      code: '',
      form: null,
      isErrorCode: false,
    }
  },
  computed: {
    disabled() {
      return this.code == '' || this.code.length < 4
    },
    // ...mapGetters({
    //   getRegisterLuckyMoney: 'system/getRegisterLuckyMoney',
    // }),
    txtPhone() {
      let { data } = this.$route.query
      this.form = JSON.parse(data)
      let vildTxt = this.form.username
      // this.form.type == 'EMAIL' ? this.form.email : this.form.phone
      if (vildTxt && vildTxt.indexOf('@') == -1) {
        let regx = /(\d{3})(\d{4})(\d{4})/
        let strLen = vildTxt.length
        if (strLen == 11) {
          regx = /(\d{3})(\d{4})(\d{4})/
        } else if (strLen == 10) {
          regx = /(\d{3})(\d{4})(\d{3})/
        } else if (strLen == 9) {
          regx = /(\d{3})(\d{3})(\d{3})/
        } else if (strLen == 8) {
          regx = /(\d{3})(\d{3})(\d{2})/
        } else if (strLen == 7) {
          regx = /(\d{2})(\d{2})(\d{3})/
        } else if (strLen == 6) {
          regx = /(\d{2})(\d{2})(\d{2})/
        } else if (strLen == 5) {
          regx = /(\d{2})(\d{1})(\d{2})/
        }
        let str = vildTxt
          .match(regx)
          .slice(1)
          .reduce(function(value, item, index) {
            return index === 1 ? value + '****' : value + item
          })
        return str
      } else {
        let txtArr = vildTxt.split('@')
        let str = txtArr[0].length > 4 ? txtArr[0].substring(0, 4) : txtArr[0]
        return str + '****@' + txtArr[1]
      }
    },
  },
  methods: {
    ...mapActions('user', {
      register: 'register',
      login: 'login',
      codeVerify: 'codeVerify',
    }),
    ///注册提交验证码
    onSubmit() {
      // this.form.username =
      //   this.form.type == 'EMAIL' ? this.form.email : this.form.phone
      this.form.verifyCode = this.code
      if (this.code.length != 6) {
        this.errDialog(this.$t('msg.verifycode'))
        this.isErrorCode = true
        return
      }
      if (this.form.isfrom && this.form.isfrom == 'reset') {
        // "newPassword": "string",
        // "username": "string",
        // "verifyCode": "string
        let params = {
          mobilePhone: this.form.username,
          // this.form.type == 'EMAIL' ? this.form.email : this.form.phone,
          code: this.form.verifyCode,
        }
        this.codeVerify(params).then(res => {
          console.log('校验是否成功', res)
          if (res && res.code == 0) {
            this.$router.push({
              path: '/updateLoginPwd',
              query: {
                verifyCode: this.form.verifyCode,
                username: params.mobilePhone,
              },
            })
          } else {
            this.errDialog(res.msg)
          }
        })
      } else if (this.form.isfrom && this.form.isfrom == 'verfication2') {
        let params = {
          mobilePhone:
            this.form.type == 'EMAIL' ? this.form.email : this.form.phone,
          code: this.form.verifyCode,
        }
        this.codeVerify(params).then(res => {
          console.log('校验是否成功', res)
          if (res && res.code == 0) {
            this.$router.push('/verfication2')
          } else {
            this.errDialog(res.msg)
          }
        })
      } else {
        this.register(this.form).then(res => {
          if (res.code == 0) {
            this.showDialog(this.$t('dialog.regSuccess'))
            let loginform = {
              platform: 'H5',
              username: this.form.username,
              password: this.form.password,
              areaCode: this.form.areaCode,
              authorityType: 'CLIENT',
            }
            this.login(loginform).then(logres => {
              if (logres.code === 0) {
                localStorage.removeItem('KYCDATA')
                localStorage.removeItem('password')
                localStorage.removeItem('reg_username')
                localStorage.setItem('username', this.form.username)
                this.$nextTick(() => {
                  this.$router.push({ path: '/' })
                })
              }
            })
          } else {
            this.errDialog(res.msg)
          }
        })
      }
    },
  },
}
</script>
