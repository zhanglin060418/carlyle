<template>
  <div class="base-input" :class="{ click: isClick, error: isError }">
    <div class="input-box">
      <input class="ipt" :type="type" :value="value" :placeholder="placeholder" :name="name" @click="iptClick" @blur="iptBlur" @input="$emit('input', $event.target.value)" />
    </div>
    <div class="class-send">
      <span v-if="countdown != 0">{{ countdown }}s</span>
      <span v-else @click="sendVerify">{{ $t('home.sendcode') }}</span>
    </div>

    <!-- <tencentCaptcha ref="tencentCaptcha" @sendVerify="sendVerify"></tencentCaptcha> -->
  </div>
</template>
<script>
import { mapActions, mapGetters } from 'vuex'
export default {
  name: 'baseSend',
  props: {
    type: {
      type: String,
      default: 'text',
    },
    value: String,
    placeholder: String,
    name: String, //name 名称
    disabled: {
      type: Boolean,
      default: false,
    }, //是否禁用
    isError: {
      type: Boolean,
      default: false,
    },
    sendData: Object, //发送地址
    // error: String   //错误提示
    code: String,
  },
  data() {
    return {
      isClick: false,
      typePwd: '',
      countdown: 0,
      timmer: null,
      params: {
        areaCode: '',
        username: '',
        type: null, //短信为null，邮箱用VERIFY_CODE
      },
      isEmail: false,
    }
  },
  created() {
    if (this.$route.path == '/verification') {
      this.sendVerify()
    }
  },
  mounted() {
    // this.$refs.tencentCaptcha.show()
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
    ...mapActions({
      smsSend: 'user/smsSend',
    }),
    iptClick(e) {
      this.isClick = true
    },
    iptBlur(e) {
      this.isClick = false
    },
    startCountdown() {
      this.countdown = 120
      this.timmer = setInterval(() => {
        this.countdown--
        if (this.countdown == 0) {
          clearInterval(this.timmer)
        }
      }, 1000)
    },
    async sendVerify() {
      if (this.$route.path == '/verification') {
        this.isEmail = this.sendData.type == 'EMAIL'
      } else {
        // if (this.code == '') {
        //   this.errDialog('Kode verifikasi grafis tidak boleh kosong')
        //   return
        // }
        this.isEmail = this.switchType == 'EMAIL'
        // let res = await this.verifyFake(this.code)
        // console.log('图形验证：', res)
        // if (!res.data) {
        //   this.errDialog('Kesalahan input kode verifikasi grafis')
        //   this.$emit('getImgCode')
        //   return
        // }
      }
      this.params.username = this.sendData.username
      if (this.sendData.username == '') {
        this.errDialog(
          this.isEmail ? this.$t('msg.email') : this.$t('msg.mobile')
        )
        return
      }
      if (this.isEmail) {
        this.params.type = 'VERIFY_CODE'
        delete this.params.areaCode
        let isErrorEmail = !this.onFailed({
          name: 'email',
          value: this.params.username,
        })
        if (isErrorEmail) return
      } else {
        this.params.type = null
        this.params.areaCode = this.areaItem.code
        if (
          this.sendData.username.length < 5 ||
          this.sendData.username.length > 20 ||
          !/^[0-9]*$/.test(this.sendData.username)
        ) {
          this.errDialog(this.$t('msg.mobile'))
          return
        }
      }
      this.showDialog('Verification code has been sent')

      this.smsSend(this.params).then(res => {
        console.log('返回短信验证码', res)
        if (res && res.code == 0) {
          this.startCountdown()
        } else {
          this.errDialog(res.msg)
          return
        }
      })
    },
  },
}
</script>
