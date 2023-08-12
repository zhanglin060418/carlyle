<template>
  <modMain :title="$t('user.setting')" class="setting ">
    <div class="form-group">
      <van-form>
        <!-- <van-cell  title="Head portrait" @click="bankCards" is-link /> -->
        <template v-if="userInfo.username">
          <div class="head-avatar">
            <label>{{ $t('sa.txt277') }}</label>
            <van-uploader :after-read="afterRead" accept="image/*" :max-count="1">
              <div class="avatar">
                <template v-if="!userInfo || !userInfo.headImg">
                  <img src="static/assets/image/default-aa.png" alt="" />
                </template>
                <template v-else>
                  <img :src="imgBaseUrl + userInfo.headImg" alt="" />
                </template>
                <van-icon name="arrow van-cell__right-icon" />
              </div>
            </van-uploader>
          </div>
        </template>

        <van-cell :title="$t('payDetail.text16')" @click="bankCards" is-link />
        <van-cell :title="$t('sa.txt284')" @click="$router.push('/reset')" is-link />
        <van-cell :title="$t('sa.txt2844')" @click="$router.push('/resetPayPwd')" is-link />
        <van-cell :title="$t('sa.txt285')" @click="$router.push('/setSocial')" is-link />
        <van-cell :title="$t('dw.t164')" @click="tologout" is-link />
<!--         <van-cell
          title="设置支付密码"
          @click="$router.push('/setPayPwd')"
          is-link
        />-->
<!--         <van-cell-->
<!--          :title="$t('home.lang')"-->
<!--          :value="locale_name"-->
<!--          @click="$router.push('/language')"-->
<!--          is-link-->
<!--        />-->
<!--        <van-cell-->
<!--            :title="$t('home.currency')"-->
<!--            :value="locale_currency"-->
<!--            @click="$router.push('/currency')"-->
<!--            is-link-->
<!--        />-->
      </van-form>
      <div v-if="userInfo && userInfo.username" class="btn-box btn-fixed">
        <base-btn :btnTitle="$t('sys.logOut')" :disabled="false" @btnClick="btnQuitClick"></base-btn>
      </div>
    </div>
    <result-popup ref="showResult" :isShowCancel="true" @sure="sureClick()">
      <template slot="content">
        <div class="info">
          <p class="txt">{{ $t('sys.sureSignout') }}</p>
        </div>
      </template>
    </result-popup>
    <loadding v-if="isLoading"></loadding>
  </modMain>
</template>
<script>
import { mapGetters, mapActions, mapMutations } from 'vuex'
import client from '@/utils/http'

export default {
  data() {
    return {
      params: {
        dialog: false,
        sounds: true,
      },
      isLoading: false,
      showPage: false,
      utc: JSON.parse(localStorage.getItem('utc') || null),
      locale_name: localStorage.getItem('locale_name'),
      locale_currency: localStorage.getItem('localeCurrency_name') || 'Nigerian Naira',
    }
  },
  computed: {
    ...mapGetters({
      userInfo: 'user/userInfo',
      isLogin: 'user/isLogin',
      // getcurrentGame: 'trade/getcurrentGame',
    }),
  },
  created() {
    // if (!this.locale_name) {
    //   this.getLocaleName()
    // }
    // if (localStorage.getItem('token')) {
    //   this._userconfigquery()
    // }
  },
  mounted() {
    // console.log('用户信息', this.userInfo)
  },
  methods: {
    ...mapActions('user', {
      logout: 'logout',
      setuserconfig: 'setuserconfig',
      userconfigquery: 'userconfigquery',
      updateUserBasic: 'updateUserBasic',
    }),
    ...mapMutations('user', {
      updateUser: 'UPDATE_USER',
      quitLogin: 'quitLogin',
    }),
    getLocaleName() {
      this.querylist().then(res => {
        let languageList = res.data || []
        let locale = localStorage.getItem('locale') || 'en_US'
        let langObj = languageList.find(d => d.locale == locale)
        this.locale_name = langObj.englishName
      })
    },
    bankCards() {
      this.$router.push('/bankcard')
    },
    async afterRead(files) {
      let vm = this
      this.$toast.loading({
        message: this.$t('sys.uploadding'),
        duration: 0,
        forbidClick: true,
      })
      let file = files.file

      console.log('上传file。。。。。。。。。。', file)
      let fileTypes = ['image/jpeg', 'image/pjpeg', 'image/png']
      if (!fileTypes.includes(file.type)) {
        this.errDialog(this.$t('sys.imgErr'))
        return
      }
      if (file.size.toFixed(2) > 1024 * 1024 * 6) {
        this.errDialog(this.$t('sys.uploadSize'))
        return
      }
      console.log('开始上传。。。。。。。。。。', file)
      client
        .postFormData('/system/user/profile/avatar', {
          avatarfile: file,
        })
        .then(res => {
          console.log('上传返回信息。。。。。。。。。。', res)
          if(res.code == 200) {
            setTimeout(() => {
              vm.updateUser({
                ...this.userInfo,
                headImg: res.imgUrl,
              })
              this.errDialog(this.$t('msg.uploadSuccess'))
            }, 1000);
          }
          else this.errDialog(this.$t('msg.failedOperation'))
          /*//更新用户头像信息
          if (res.code == 0) {
            vm.updateUserBasic({
              userId: this.userInfo.id,
              headImg: res.data,
            }).then(response => {
              if (response.code == 0) {
                vm.updateUser({
                  headImg: res.data,
                })
              }
            })
          } else {
            this.errDialog(res.msg)
          }*/
        })
        /*.catch(err => { })
        .finally(res => {
          this.$toast.clear()
        })*/
    },
    btnQuitClick() {
      // this.$dialog
      //   .confirm({
      //     title: '',
      //     message: this.$t('sys.sureSignout'),
      //     cancelButtonText: this.$t('sys.cancel'),
      //     confirmButtonText: this.$t('sys.confirm'),
      //   })
      //   .then(() => {
      //     this.logout()
      //     localStorage.removeItem('token')
      //     localStorage.removeItem('USER_INFO')
      //     localStorage.removeItem('limit_time')
      //     localStorage.removeItem('video_time')
      //     localStorage.removeItem('video_play_id')
      //     this.quitLogin()
      //     window.location.href = '/'
      //   })
      //   .catch(() => {
      //   })
      this.$refs.showResult.show = true
    },
    sureClick() {
      this.logout()
      localStorage.removeItem('token')
      localStorage.removeItem('USER_INFO')
      localStorage.removeItem('limit_time')
      localStorage.removeItem('video_time')
      localStorage.removeItem('video_play_id')
      this.quitLogin()
      this.$router.push('/home')
      window.location.href = '/'
    },
    tologout() {
      this.$dialog
              .confirm({
                title: '',
                message: this.$t('sys.sureSignout'),
                cancelButtonText: this.$t('sys.cancel'),
                confirmButtonText: this.$t('sys.confirm'),
              })
              .then(() => {
                this.logout()
                localStorage.removeItem('token')
                localStorage.removeItem('USER_INFO')
                sessionStorage.clear()
                this.quitLogin()
                // this.$router.push('/')
                window.location.href = '/'
              })
              .catch(() => {
                // on cancel
              })
    },
  },
}
</script>
