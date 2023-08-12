<template>
  <div class="share-main">
    <div class="lucky-info">
      <div class="learn-more" @click="toRegIn">Belajarlah lagi</div>
    </div>
    <div class="main">
      <div class="userinfo-container">
        <h3>Mendaftar Untuk Memberi <span>Hadiah</span></h3>

        <div class="userinfo-value">
          <basePhone
            v-model.trim="form.username"
            v-if="switchType == 'PHONE'"
            :placeholder="$t('home.enterMobile')"
          ></basePhone>
          <base-input
            v-model.trim="form.username"
            v-if="switchType == 'EMAIL'"
            :isError="isErrorEmail"
            :placeholder="$t('home.enterEmail')"
          ></base-input>
          <base-input
            v-model.trim="form.password"
            :isError="isErrorPwd"
            type="password"
            :placeholder="$t('home.enterPwd')"
          ></base-input>
          <base-input
            v-model.trim="form.password2"
            :isError="isErrorPwd"
            type="password"
            :placeholder="$t('home.confirmPwd')"
          ></base-input>
          <!-- <base-code
            ref="imgCode"
            v-model="imgCode"
            :isError="isErrorCode"
          ></base-code> -->
          <baseSend
            type="text"
            :sendData="form"
            v-model="code"
            :placeholder="$t('home.verCode')"
            ref="sendPhone"
            :isError="isErrorCode"
          ></baseSend>
        </div>
        <!-- <switch-login /> -->
        <div class="reg-btn" @click="toSignIn">Pendaftaran</div>
      </div>
      <group-service ref="groupService"></group-service>
      <p class="desc">Mendaftar untuk hadiah yang bagus</p>
      <div class="profit-container">
        <div class="profit-info">
          <div class="time">Hanya hari ini</div>
          <div class="profit-box">
            <div class="profit-avatar">
              <img src="static/assets/image/xapp/land/vector.png" alt="" />
            </div>
            <div class="profit-money">
              <p>R 100K</p>
              <p>Daftar untuk memberi</p>
            </div>
            <div class="profit-btn" @click="toRegIn">Menerima</div>
          </div>
        </div>
      </div>
      <p class="desc">Komentar dari semua orang</p>
      <comments></comments>
    </div>

    <div class="free-reg" @click="toRegIn">
      Pendaftaran gratis
    </div>
  </div>
</template>
<script>
import { mapActions, mapGetters, mapMutations } from 'vuex'
import comments from './components/comments'

export default {
  components: {
    comments,
  },
  data() {
    return {
      isErrorPwd: false,
      isErrorCode: false,
      isAgree: true,
      isShowLoadding: false,
      form: {
        username: '',
        password: '',
        password2: '',
        registerType: 'PHONE',
        authorityType: 'CLIENT',
        platform: 'H5',
        areaCode: '91',
        verifyCode: '',
        source: 1,
        symbolCode: null, //邀请码
      },
      code: '', //短信验证码
      // imgCode: '',
      wathsrls: [],
      isErrorEmail: null,
    }
  },
  computed: {
    ...mapGetters({
      switchType: 'getSwitchType',
    }),
    areaItem() {
      return this.$store.getters.getAreaCode()
    },
  },
  methods: {
    ...mapActions('user', {
      register: 'register',
      login: 'login',
      verifyUsername: 'verifyUsername',
      codeVerify: 'codeVerify',
    }),
    // getImgCode() {
    //   this.$refs.imgCode.getCodeImg()
    // },
    toRegIn() {
      this.$router.push('/register')
    },
    // toSignIn() {},
    async toSignIn() {
      // if (this.imgCode == '') {
      //   this.errDialog('Kode verifikasi grafis tidak boleh kosong')
      //   return
      // }
      this.form.areaCode = this.areaItem.code
      this.form.registerCountryCode = this.areaItem.area
      if (this.switchType == 'PHONE') {
        let fz = this.form.username.substring(0, 1)
        if (fz == 0) {
          this.form.username = this.form.username.substr(1)
        }
        if (
          this.form.username.length < 5 ||
          !/^[0-9]*$/.test(this.form.username)
        ) {
          this.errDialog(this.$t('msg.mobile'))
          return
        }
      }
      if (this.switchType == 'EMAIL') {
        this.isErrorEmail = !this.onFailed({
          name: 'email',
          value: this.form.username,
        })
        if (this.isErrorEmail) return
      }
      if (this.form.password != this.form.password2) {
        this.errDialog(this.$t('msg.repwd'))
        return
      }
      if (this.code == '') {
        this.errDialog(this.$t('msg.verifycode'))
        this.isErrorCode = true
        return
      }
      // this.isErrorPwd = !this.onFailed({
      //   name: 'password',
      //   value: this.form.password,
      // })
      // if (this.isErrorPwd) return

      //验证是否有注册
      let res = await this.verifyUsername({
        username: this.form.username,
      })
      let isok = false
      if (res.code == 0) {
        if (res.data) {
          isok = false
          this.errDialog(this.$t('msg.login'))
          return
        } else {
          isok = true
        }
      } else {
        isok = false
        this.errDialog(res.msg || this.$t('msg.systemerr'))
        return
      }
      let isCodeVerify = false
      this.form.registerType = this.switchType
      let params = {
        mobilePhone: this.form.username,
        code: this.code,
      }
      let rs = await this.codeVerify(params)
      if (rs && rs.code == 0) {
        isCodeVerify = true
      } else {
        isCodeVerify = false
        this.errDialog(rs.msg)
      }
      if (isok && isCodeVerify) {
        await this._register()
      }
    },
    _register() {
      this.isShowLoadding = true
      this.form.verifyCode = this.code
      // this.form.sessionId = sessionStorage.getItem('sessionId') || ''
      // this.form.verifyImageCode = this.imgCode
      this.form.symbolCode = localStorage.getItem('u_symbolCode') || null
      this.form.verifyCode = this.code
      this.register(this.form).then(res => {
        this.isShowLoadding = false
        if (res.code == 0) {
          localStorage.setItem('logo_username', this.form.username)
          this.showDialog(this.$t('dialog.regSuccess'))
          this.onLogin()
        } else {
          this.errDialog(res.msg)
        }
      })
    },
    onLogin() {
      this.$router.push('/signin-success')
    },
  },
}
</script>
<style lang="less" scoped>
.share-main {
  width: 100%;
  background: url('../../assets/image/xapp/land/bg1.jpg') no-repeat;
  background-size: cover;
  padding-bottom: 60px;
  p {
    &.green {
      color: #fff;
    }
  }
  .lucky-info {
    width: 100%;
    height: 499px;
    background: url('../../assets/image/xapp/land/lucky.png') no-repeat;
    background-size: cover;
    position: relative;
    .learn-more {
      position: absolute;
      display: flex;
      align-items: center;
      justify-content: center;
      width: 210px;
      height: 49px;
      left: 78px;
      bottom: 60px;
      background: linear-gradient(0deg, #efa95f 0%, #fcf1c0 100%);
      box-shadow: -0.0267043px 1.54063px 4.10836px #a52a28,
        inset 0px 4.24089px 4.24089px #fff1d3;
      border-radius: 50px;
      font-size: 20px;
      line-height: 26px;
      color: #72490b;
      animation: heartbeat2 1.6s infinite;
      z-index: 9;
    }
  }
  .main {
    padding: 0 30px;
    margin-top: 25px;
    .userinfo-container {
      background: rgba(255, 255, 255, 0.2);
      box-shadow: 0px 4px 16px rgba(33, 36, 75, 0.16),
        inset 0px 0px 40px rgba(173, 170, 255, 0.25);
      border-radius: 10px;
      padding: 19px 10px 12px 10px;
      h3 {
        font-size: 18px;
        line-height: 26px;
        text-align: center;
        color: #fbf6d9;
        margin-bottom: 10px;
        span {
        }
      }
      .userinfo-value {
        /deep/ .base-input {
          border-radius: 4px;
          margin-bottom: 10px;
        }
      }
      .server-link {
        color: #fff;
        margin-top: 10px;
        text-align: left;
        text-decoration: underline;
        span {
          color: #fad657;
        }
      }
      .reg-btn {
        width: 100%;
        height: 51px;
        line-height: 51px;
        background: linear-gradient(0deg, #efa95f 0%, #fcf1c0 100%);
        box-shadow: -0.0267043px 1.54063px 4.10836px #284ba5,
          inset 0px 4.24089px 4.24089px #fff1d3;
        font-size: 21px;
        color: #72490b;
        text-align: center;
        border-radius: 30px;
        margin-top: 15px;
      }
    }

    .desc {
      font-size: 18px;
      margin-top: 25px;
      color: #fbf6d9;
      text-align: center;
      position: relative;
      padding-bottom: 10px;
      &::after {
        content: '';
        position: absolute;
        width: 30px;
        height: 16px;
        left: 50%;
        margin-left: -15px;
        bottom: -20px;
        background: url('../../assets/image/xapp/land/row.png') no-repeat;
        background-size: contain;
      }
    }
    .profit-container {
      margin-top: 40px;
      .profit-info {
        .time {
          color: #72490b;
          font-size: 12px;
          font-weight: 500;
          background: linear-gradient(180deg, #fad657 0%, #fca227 100%);
          width: 90px;
          text-align: center;
          line-height: 18px;
        }
        .profit-box {
          display: flex;
          background: rgba(255, 255, 255, 0.2);
          box-shadow: 0px 4px 16px rgba(33, 36, 75, 0.16),
            inset 0px 0px 40px rgba(173, 170, 255, 0.25);
          border-radius: 40px;
          padding: 16px 13px;
          border-top-left-radius: 0;
          align-items: center;
          .profit-avatar {
            flex: 0 0 34px;
            width: 34px;
            height: 34px;
            img {
              width: 34px;
            }
          }
          .profit-money {
            margin: 0 7px;
            p {
              font-weight: 500;
              font-size: 16px;
              color: #fff;
              margin-bottom: 0;
            }
          }
          .profit-btn {
            flex: 0 0 96px;
            width: 96px;
            line-height: 38px;
            text-align: center;
            background: linear-gradient(180deg, #fad657 0%, #fca227 100%);
            border-radius: 60px;
            font-weight: 500;
            font-size: 16px;
            color: #72490b;
          }
        }
      }
    }
  }

  .free-reg {
    width: 355px;
    height: 48px;
    position: fixed;
    left: 10px;
    bottom: 20px;
    bottom: calc(20px + constant(safe-area-inset-bottom));
    bottom: calc(20px + env(safe-area-inset-bottom));
    display: flex;
    align-items: center;
    justify-content: center;
    background: linear-gradient(180deg, #fad657 0%, #fca227 100%);
    box-shadow: inset 0px -4px 0px #ab3f57;
    border-radius: 50px;
    font-size: 22px;
    // line-height: 27px;
    color: #82001c;
    z-index: 2;
    animation: heartbeat2 1.5s infinite;
  }
}
</style>
