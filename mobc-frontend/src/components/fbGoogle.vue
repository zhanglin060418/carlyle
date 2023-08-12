<template>
  <div class="login-google">
    <!-- <p class="or-other">{{ $t('home.socialAcc') }}</p>
    <div class="login-other ">
      <g-signin-button
        :params="googleSignInParams"
        @success="onSignInSuccess"
        @error="onSignInError"
      >
        <div class="other-btn google">Google</div>
      </g-signin-button>

      <fb-signin-button
        :params="fbSignInParams"
        @success="onSignInSuccessFb"
        @error="onSignInErrorFb"
      >
        <div class="other-btn facebook">Facebook</div>
      </fb-signin-button>
    </div> -->
  </div>
</template>

<script>
import { mapActions } from 'vuex'
export default {
  name: 'XAppFbgoogle',

  data() {
    return {
      googleSignInParams: {
        client_id:
          '708031290659-m0v2n1stj4hocstcgmjauapo44ke6gv3.apps.googleusercontent.com',
      },
      fbSignInParams: {
        scope: 'public_profile,email',
        return_scopes: true,
        auto_logout_link: 'true',
      },
    }
  },

  mounted() { },

  methods: {
    ...mapActions({
      loginToken: 'user/loginToken',
    }),
    onSignInSuccess(googleUser) {
      const info = googleUser.getBasicProfile() //获取信息
      const name = info.getName() // 姓名
      const id = info.getId() // id
      const givenName = info.getGivenName() // 名
      const familyName = info.getFamilyName() // 姓
      const imageUrl = info.getImageUrl() // imageUrl
      const email = info.getEmail() // email
      console.log(info, name, id, givenName, familyName, imageUrl, email)

      this.loginToken({
        token: googleUser.Zb.id_token,
        source: 2,
      }).then(res => {
        this.callBackSuccess(res)
      })
    },
    onSignInError(error) {
      console.log(error)
    },
    onSignInSuccessFb(response) {
      let vm = this
      console.log(response) //返回第三方的登录信息 tolen等
      // FB.api('/me?fields=name,first_name,last_name,email', function(response) {
      //   let data = JSON.stringify(response)
      //   console.log('facebook', data)
      //   // "email":"rhemcqacordd@outlook.com","id":"130035665951708"
      //   // vm.
      // })
      this.loginToken({
        token: response.authResponse.accessToken,
        source: 3,
      }).then(res => {
        this.callBackSuccess(res)
      })
    },
    onSignInErrorFb(error) {
      console.log(error)
    },
    callBackSuccess(res) {
      if (res.code === 0) {
        this.$router.replace({
          path: '/',
        })
      } else if (res.code === 1017) {
        this.errDialog(res.msg)
      } else {
        this.errDialog(res.msg)
      }
    },
  },
}
</script>
