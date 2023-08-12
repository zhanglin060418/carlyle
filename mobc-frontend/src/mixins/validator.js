import { mapActions, mapGetters } from 'vuex'
const myValidMixins = {
  data() {
    return {
      emailPattern: /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/, // 验证邮箱
      pwdPattern: /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,12}$/, // 6-12位登录密码
      payPwdPattern: /^\d{6}$/, //支付密码，6位纯数字
      idCardPattern: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/, //验证身份证
      bankCardPattern: '',
      offetTop: 44, //顶部安全距离
      lang: 'en',
      imgCode: '', //图片验证码
    }
  },
  computed: {
    ...mapGetters({
      userInfo: 'user/userInfo',
      imgBaseUrl: 'imgBaseUrl',
    }),
    isId() {
      let lang = localStorage.getItem('locale') || 'en_US'
      return lang == 'en_US'
    },
  },
  created() {
    this.lang = localStorage.getItem('locale') || 'en_US'
  },
  methods: {
    ...mapActions({
      verifyCode: 'user/verifyCode',
      verifyFakeCode: 'user/verifyFakeCode',
    }),
    // 校验函数返回 true 表示校验通过，false 表示不通过
    validator(val) {
      return /1\d{10}/.test(val)
    },
    onFailed(errorInfo) {
      // console.log('failed', errorInfo)
      const { name, value } = errorInfo
      let errMsg = ''
      switch (name) {
        case 'email':
          if (!this.emailPattern.test(value)) {
            // '请输入有效的电子邮箱地址'
            errMsg = this.$t('msg.email')
          }
          break
        case 'password':
          // errMsg = value === '' ? '密码' : '6-12位数字和字母组成的密码'
          // errMsg =
          //   value === '' ? this.$t('form.password') : this.$t('form.legalPwd')
          // if (!this.pwdPattern.test(value)) {
          //   errMsg = this.$t('msg.password') //'密码为 6-12 位数字和字母组合'
          // }
          break
        case 'payPwdPattern':
          // errMsg =
          //   value === '' ? this.$t('form.payPwd') : this.$t('form.legalPayPwd')
          if (!this.payPwdPattern.test(value)) {
            errMsg = this.$t('msg.paypwd') // '请输入6位纯数字的密码'
          }
          break
        case 'idCardPattern':
          // errMsg = value === '' ? '身份证号码' : '有效的身份证号码'
          errMsg =
            value === '' ? this.$t('form.idNumber') : this.$t('form.validID')
          break
        case 'bankCardPattern':
          // errMsg = value === '' ? '银行卡号' : '有效的银行卡号'
          errMsg =
            value === ''
              ? this.$t('form.bankCardNo')
              : this.$t('form.validCardNo')
          break
      }
      if (errMsg != '') {
        this.errDialog(errMsg)
        return false
      }
      return true
    },
    errDialog(message) {
      this.$toast({
        message: message || 'Anomali jaringan',
        position: 'middle',
      })
    },
    showDialog(message) {
      this.$toast({ message, position: 'middle' })
    },
    // 图形验证码检测
    verify(value) {
      let params = {
        sessionId: sessionStorage.getItem('sessionId') || '',
        code: value,
      }
      return this.verifyCode(params)
    },
    verifyFake(value) {
      let params = {
        sessionId: sessionStorage.getItem('sessionId') || '',
        code: value,
      }
      return this.verifyFakeCode(params)
    },
    toShare() {
      if (this.userInfo && this.userInfo.username) {
        this.$router.push('/invite')
      } else {
        this.$router.push('/login')
      }
    },
    randomNum(minNum, maxNum) {
      switch (arguments.length) {
        case 1:
          return parseInt(Math.random() * minNum + 1, 10)
        case 2:
          return parseInt(Math.random() * (maxNum - minNum + 1) + minNum, 10)
        default:
          return 0
      }
    },
    //本金+每小时收益金额*剩余小时数*50%）--（本金+每小时收益金额*剩余小时数*75%）
    getGuidePrice(item) {
      let { amount, returnRate, expireTime } = item
      let income = this.$utils.accMul(amount, returnRate)
      let hIncom = income / 24
      let dTime = new Date()
      let expTime = new Date(expireTime)
      let dObj = this.$utils.diffTime(dTime, expTime)
      let hours = dObj.days * 24 + dObj.hours
      let sPrice = this.$utils.accAdd(amount, hIncom * hours * 0.5)
      let ePrice = this.$utils.accAdd(amount, hIncom * hours * 0.75)
      return {
        startPrice: this.$utils.getkStr(sPrice + 2, 0),
        endPrice: this.$utils.getkStr(ePrice, 0),
        guidePrice1: sPrice + 2,
        guidePrice2: ePrice,
      }
    },
  },
}
export default myValidMixins
