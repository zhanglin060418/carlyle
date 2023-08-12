<template>
  <div style="display: flex;
    height: 100%;
    width: 100%;
    align-items: center;
    justify-content: center;    flex-direction: column;">
    <van-button type="primary" @click="btnRegister">test</van-button>
    <p style="margin-top:15px;">start{{ regMsg }}</p>
    <div style="display: flex;
    height: 300px;
    overflow: auto;
    width: 355px;
    flex-wrap: wrap;">
      <span style="display:block;" v-for="name in usernameList">{{ name }}&nbsp;&nbsp;</span>
    </div>
    <h1>formtest</h1>
    <form ref="pixForm" action="https://kehuapay.com/PAYAPI/pay" method="POST">
      <!-- <input type="hidden" name="appid" value="672334124" /><input
        type="hidden"
        name="ordernumber"
        value="1433459446817751040"
      /><input type="hidden" name="paytype" value="BANKCARD" /><input
        type="hidden"
        name="orderamount"
        value="220.00"
      /><input
        type="hidden"
        name="notifyurl"
        value="http://18.141.241.136/trade/payment_callback/progress/payCallback"
      /><input type="hidden" name="bankcode" value="NGR044" /><input
        type="hidden"
        name="sign"
        value="edcd81309e254e121957fa99fd38311c"
      /> -->
      <input type="hidden" name="appid" value="672334124" />
      <input type="hidden" name="ordernumber" value="1433479454125981696" />
      <input type="hidden" name="paytype" value="BANKCARD" />
      <input type="hidden" name="orderamount" value="10000.00" />
      <input type="hidden" name="notifyurl" value="https://m.petsworld.me/trade/payment_callback/progress/payCallback" />
      <input type="hidden" name="bankcode" value="NGR044" />
      <input type="hidden" name="sign" value="3bedb4b7f77d62fdc26c8e887333b711" />
      <van-button type="primary" @click="$refs.pixForm.submit()">submit</van-button>
    </form>
    <van-button type="primary" @click="testCodeRsa()">testCodeRsa</van-button>
  </div>
</template>
<script>
import { mapActions, mapGetters, mapMutations } from 'vuex'

export default {
  data() {
    return {
      isShowLoadding: false,
      isErrorEmail: false,
      isErrorPwd: false,
      isErrorCode: false,
      isShowOrgCode: false,
      isAgree: true,
      form: {
        username: '',
        password: '123456',
        password2: '123456',
        registerType: 'PHONE',
        authorityType: 'CLIENT',
        platform: 'H5',
        areaCode: '91',
        source: 1,
        symbolCode: null, //邀请码
      },
      code: '', //短信验证码
      imgCode: '',
      bannerList: null,
      usernameList: [],
      regMsg: '',
      action: 'https://kehuapay.com/PAYAPI/pay',
      sForm: {
        appid: '',
        ordernumber: '',
        paytype: '',
        orderamount: '',
        notifyurl: '',
        sign: '',
      },
    }
  },

  methods: {
    ...mapActions('user', {
      register: 'register',
      login: 'login',
      verifyUsername: 'verifyUsername',
      codeVerify: 'codeVerify',
      codeRsa: 'codeRsa',
    }),

    btnRegister() {
      for (let index = 0; index < 6; index++) {
        this.getrom()
      }
    },
    getrom() {
      let vm = this
      for (let i = 0; i < 5; i++) {
        ; (function (self, index) {
          let form = {
            username: '',
            password: '123456',
            password2: '123456',
            registerType: 'PHONE',
            authorityType: 'CLIENT',
            platform: 'H5',
            areaCode: '91',
            source: 1,
            symbolCode: '49161247',
          }
          form.username = '222222' + Math.floor(Math.random(0, 100000) * 100000)
          console.log(form.username)
          vm.usernameList.push(form.username)
          if (index == 99) {
            vm.regMsg = 'success'
          }
          self.register(form)
        })(vm, i)
      }
    },
    testCodeRsa() {
      this.codeRsa().then(res => { })
    },
  },
}
</script>
