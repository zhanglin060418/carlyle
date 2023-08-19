import {mapActions, mapGetters, mapMutations} from 'vuex'

export default {
  data() {
    return {
      isFirstLogin: false,
      firstForm: {},
      isShowLoadding: false,
      isErrorMobile: false,
      isErrorEmail: false,
      isErrorPwd: false,
      isErrorCode: false,
      isShowOrgCode: false,
      isAgree: true,
      form: {
        username: '',
        password: '',
        password2: '',
        areaCode: '',
        inviteCode: this.$route.query.c || '',
        defaultLang: '',
        country:'',
        uuid:''
      },
      code: '', //短信验证码
      imgCode: '',
      verifyPhone: '',
      bannerList: null,
      registerbBonusMin: 0,
      registerbBonusMan: 0
    }
  },
  computed: {
    ...mapGetters({
      checkType: 'getCheckType', // 注册 or 登录
      switchType: 'getSwitchType', // 邮箱 or 手机
      clickedPhoneVerify: 'getClickedPhoneVerifyEvent'
    }),
    areaItem() {
      return this.$store.getters.getAreaCode()
    },
    disabled() {
      let disabled =
        this.form.username == '' ||
        this.form.password == '' ||
        this.form.password2 == '' ||
        this.form.inviteCode == '' ||
        this.isAgree == false
      return disabled || this.imgCode == ''
    },
    loginDisabled() {
      return this.form.username == '' || this.form.password == ''
    },
    isEmail() {
      return this.switchType === 'EMAIL'
    },
  },
  watch: {
    checkType(newval, oldval) {
      if (this.checkType) {
        this.form.username = ''
        this.form.password = ''
      } else {
        let rememberInput = document.getElementsByName("remember")[0];
        let userName = localStorage.getItem('logo_username');
        let password = localStorage.getItem('password');
        if(userName != null && password != null){
          this.form.username = userName;
          this.form.password = password;
          rememberInput.checked = true;
        }else{
          rememberInput.checked = false;
        }

      }
    },
  },
  methods: {
    ...mapActions('user', {
      register: 'register',
      login: 'login',
      verifyUsername: 'verifyUsername',
      codeVerify: 'codeVerify',
      getUserData: 'getUserData',
      phoneVerify: 'phoneVerify',
      registerBonusValue: 'getRegisterBonusValue'
      // verifyBroker: 'verifyBroker',
    }),
    ...mapMutations({
      setCheckType: 'SET_PLAT_TYPE',
      setPhoneVerifyClicked: 'SET_PHONE_VERIFY',
      setSwitchType: 'SET_SWITCH_TYPE',
      updateUser: 'user/UPDATE_USER'
    }),
    async getRegisterBonusValues() {
      await this.registerBonusValue().then(res => {
        this.registerbBonusMin = res.registerbBonusMin
        this.registerbBonusMan = res.registerbBonusMan
      })
    },
    getImgCode() {
      this.$refs.imgCode.getCodeImg()
    },
    async btnVerifyPhone() {
      if (
        this.form.username.length < 5 ||
        this.form.username.length > 20 ||
        !/^[0-9]*$/.test(this.form.username)
      ) {
        this.isErrorMobile = true
        this.errDialog(this.$t('msg.mobile'))
        return
      }
      let res = await this.verifyUsername({
        username: this.areaItem.code + this.form.username,
      })
      if (res.code == 200) {
        this.setCheckType(false)
        return this.errDialog(this.$t('msg.existUser'))
      }
      else {
        this.codeVerify({
          phoneNo: this.areaItem.code + this.form.username,
          useMethod: 'register'
        }).then(res => {
          if (res.code == 200) {
            this.setPhoneVerifyClicked(false)
          }else{
            this.errDialog(res.msg)
          }
        })
      }
    },
    async btnRegister() {
      this.form.registerType = this.switchType
      this.form.areaCode = this.isEmail ? null : this.areaItem.code
      if (this.isEmail) {
        this.isErrorEmail = !this.onFailed({
          name: 'email',
          value: this.form.username,
        })
        if (this.isErrorEmail) return
      } else {
        if (this.form.username.length < 5 ||this.form.username.length > 20 ||!/^[0-9]*$/.test(this.form.username) ) {
          this.isErrorMobile = true
          this.errDialog(this.$t('msg.mobile'))
          return
        }
      }
      if (this.form.password != this.form.password2) {
        this.errDialog(this.$t('msg.repwd'))
        return
      }
      if( this.form.password.length < 5 ||this.form.password.length > 20){
        this.errDialog(this.$t('msg.password'))
        return
      }
      if (!this.isEmail) {
        let fz = this.form.username.substring(0, 1)
        if (fz == 0) {
          this.form.username = this.form.username.substr(1)
        }
      }
      //验证是否有注册
      let res = await this.verifyUsername({
        username: this.form.areaCode + this.form.username,
      })

      if (res.code == 200) {
        this.setCheckType(false)
        this.$router.push('/login')
        return this.errDialog(this.$t('msg.existUser'))
      }

      let phoneVerifyRes = await this.phoneVerify({
        phoneNo: this.form.areaCode + this.form.username,
        verifyCode: this.verifyPhone,
        useMethod: 'register'
      })

      if (phoneVerifyRes.code == 500) {
        if(phoneVerifyRes.msg == 'Please click phone verify button and receive the verify code')
          this.errDialog(this.$t('msg.sendVerifyRequest'))
        else if(phoneVerifyRes.msg == 'Phone verify code invalid!')
          this.errDialog(this.$t('msg.invalidVerifyCode'))
        else if (phoneVerifyRes.msg == 'Verify code expired. Please receive verify code!')
          this.errDialog(this.$t('msg.expiredVerifyCode'))
        return
      }
      await this._register()
    },
    _register() {
      this.form.defaultLang = localStorage.getItem('locale_name') || null
      this.form.country = this.areaItem.nameCN
      const formData = {
        code: this.imgCode,
        confirmPassword: this.form.password2,
        password: this.form.password,
        username: this.form.areaCode + this.form.username,
        inviteCode: this.form.inviteCode,
        country: this.form.country,
        defaultLang: this.form.defaultLang,
        uuid: sessionStorage.getItem('sessionId') || ''
      }
      this.isShowLoadding = true
      this.register(formData).then(res => {
        this.isShowLoadding = false
        if (res.code == 200) {
          localStorage.setItem('logo_username', this.form.username)
          localStorage.removeItem('password')
          this.imgCode = '';
          sessionStorage.setItem('isFirstLogin', '1')
          this.firstForm = {
            password: formData.password,
            username: formData.username,
          }
          this.errDialog(this.$t('dialog.regSuccess'))
          this.$router.push('/login');
          console.log(this.isFirstLogin);
          this.setCheckType(false)
        } else if(res.code == 500 && res.msg == '验证码已失效') {
          this.errDialog(this.$t('home.imgExpireMsg'))
        } else if(res.code == 500 && res.msg == '验证码错误') {
          this.errDialog(this.$t('home.imgErrMsg'))
        }else {
          this.errDialog(res.msg)
        }
        this.getImgCode()
        return
      })
    },
    async btnLogin() {
      let isEmail = this.form.username.indexOf('@') > -1
      this.setSwitchType(isEmail ? 'EMAIL' : 'PHONE')
      if (isEmail) {
        this.form.registerType = 'EMAIL'
        this.isErrorEmail = !this.onFailed({
          name: 'email',
          value: this.form.username,
        })
        if (this.isErrorEmail) return
      } else {
        this.form.registerType = 'PHONE'
        if (
          this.form.username.length < 5 ||
          this.form.username.length > 20 ||
          !/^[0-9]*$/.test(this.form.username)
        ) {
          this.isErrorMobile = true
          this.errDialog(this.$t('msg.mobile'))
          return
        }
      }
      this.form.areaCode = isEmail ? null : this.areaItem.code
      this.toLogin()
    },
    async toLogin(postData = null) {
      this.isShowLoadding = true
      let obj = postData || this.form

      const formData = {
        code: this.imgCode,
        password: this.form.password,
        username: this.form.areaCode + this.form.username,
        uuid: sessionStorage.getItem('sessionId') || '',
      }

      let rememberInput = document.getElementsByName("remember")[0];
      if(rememberInput.checked){
        localStorage.setItem('logo_username', this.form.username);
        localStorage.setItem('password', this.form.password);
      }else{
        localStorage.removeItem('logo_username');
        localStorage.removeItem('password');
      }

      let res = await this.login(formData)

      let checkUser = await this.verifyUsername({
        username: this.form.areaCode + this.form.username,
      })
      if (checkUser.code != 200) {
        this.isShowLoadding = false
        return this.errDialog(this.$t('msg.notregistered'))
      }
      this.isShowLoadding = false
      if (res.token) {
        localStorage.setItem('token', res.token)
        let userData = await this.getUserData()
        localStorage.setItem('payment_password', userData.hasPayPwd)
        this.updateUser(userData)
        localStorage.setItem('userId', userData.user_id)
        this.$router.replace({
          path: '/',
        })
      } else if(res.code == 500 && res.msg == '验证码已失效') {
        this.errDialog(this.$t('home.imgExpireMsg'))
      } else if(res.code == 500 && res.msg == '验证码错误') {
        this.errDialog(this.$t('home.imgErrMsg'))
      } else if(res.code == 500 && res.msg.toString().includes('停用')) {
        this.errDialog("Sorry, your account has been disabled, Please contact our service..")
      }else {
        this.errDialog(this.$t('msg.loginErr'))
      }
      this.getImgCode()
      return
    },
    //打开注册协议
    openXy(type) {
      this.$router.push({
        path: '/xieyi',
        query: {
          type,
        },
      })
    },
  },
  beforeDestroy() {
    this.isShowLoadding = false
  },
}
