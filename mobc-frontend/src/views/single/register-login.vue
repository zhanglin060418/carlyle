<template>
  <modMain :title="!checkType ? 'Login' : 'Register'" class="noPadding setting">
    <div class="login-main">
      <div class="banner">
<!--        <img class="logo" src="static/assets/image/bcf/logo.png" alt="" />-->

        <h1 style="font-family:Fortescue Display,serif; letter-spacing:0.1rem; color:#0a364a;font-weight: 800">CARLYLE</h1>
        <!-- <van-swipe
          class="my-swipe"
          :autoplay="5000"
          :indicator-color="cssSkin == 'dark' ? 'white' : '#303235'"
        >
          <template v-for="item in bannerList">
            <van-swipe-item @click="swiperClick(item)"
              ><img class="ban-img" :src="item.imgUrl" alt=""
            /></van-swipe-item>
          </template>
        </van-swipe> -->
      </div>
      <div  v-if="checkType" class="registration-bonus">
        {{ $t('dw.t223') }}{{ registerbBonusMin }}-{{ registerbBonusMan }}{{ $t('dw.t224') }}
      </div>
       <switch-tab />
      <div class="flex" style="margin-bottom: 8px; margin-top: 8px">
        <base-phone
            v-model.trim="form.username"
            v-if="switchType == 'PHONE'"
            type="number"
            :placeholder="$t('home.enterMobile') "
        ></base-phone>
        <div v-if="checkType" style="width: 50px; height: 40px; border-radius: 12px; border:1px solid #3376c4; margin-left: 8px; display: flex; justify-content: center; align-items: center" >
          <img v-if="clickedPhoneVerify" src="/static/assets/image/phone_verify.png" style="width: 100%; height: 100%" @click="btnVerifyPhone">
          <img v-else src="/static/assets/image/phone_verify_disable.png" style="width: 100%; height: 100%">
        </div>
      </div>
      <baseSMS
          type="number"
          v-if="checkType"
          v-model.trim="verifyPhone"
          :placeholder="$t('home.phoneVerifyCode')"
          :timer="!clickedPhoneVerify"
      ></baseSMS>
      <base-input
        v-model.trim="form.username"
        type="text"
        v-show="switchType == 'EMAIL' && checkType"
        :isError="isErrorEmail"
        :placeholder="$t('home.enterEmail')"
      ></base-input>

      <base-input
        v-model.trim="form.password"
        :isError="isErrorPwd"
        type="password"
        :placeholder="$t('home.enterPwd')"
      ></base-input>
      <template v-if="checkType">
        <base-input
          v-model.trim="form.password2"
          :isError="isErrorPwd"
          type="password"
          :placeholder="$t('home.confirmPwd')"
          @blur="alert(22)"
        ></base-input>
        <!-- <baseSend type="text" :sendData="form" v-model="code" :placeholder="$t('home.verCode')" ref="sendPhone" :isError="isErrorCode"></baseSend> -->
      </template>
      <template v-if="checkType">
        <base-input
            v-model.trim="form.inviteCode"
            :readonly="true"
            :placeholder="$t('home.enterInviteCode')"
        ></base-input>
      </template>
      <base-code
          ref="imgCode"
          v-model="imgCode"
          :isError="isErrorCode"
      ></base-code>
      <!-- <base-country></base-country> -->
      <!-- <template v-if="checkType">
        <base-code ref="imgCode" v-model="imgCode" :isError="isErrorCode"></base-code>
      </template> -->
      <template v-if="!checkType">
      <div class="register-agree">
          <input class="remember-icon" type="checkbox" name="remember" value="1">
          <p class="agree-txt">
              Remember me
          </p>
      </div>
        <div class="forgot">
          <p>
            {{ $t('home.forgot') }}
            <span @click="$router.push('/forgetLoginPwd')">{{ $t('home.reset') }}</span>
          </p>
        </div>
      </template>
      <template v-if="checkType">
        <div class="register-agree">
          <span class="agree-icon" :class="{ tongyi: isAgree }" @click="isAgree = !isAgree"></span>
          <p class="agree-txt">
            {{ $t('home.year18') }}
            <span @click="openXy(1)">{{ $t('home.tersCond') }}</span>
          </p>
        </div>
      </template>
      <div class="btn-wrap">
        <template v-if="checkType">
          <base-btn
            :btnTitle="$t('home.register')"
            :disabled="disabled"
            @btnClick="btnRegister"
          ></base-btn>
          <!-- <switch-login /> -->
        </template>
        <template v-else>
          <base-btn
            :btnTitle="$t('home.login')"
            :disabled="loginDisabled"
            @btnClick="btnLogin"
          ></base-btn>
        </template>

        <!-- <template v-else>
          <div class="forgot">
            <p>
              {{ $t('home.forgot') }}
              <span @click="$router.push('/reset')">{{
                $t('home.reset')
              }}</span>
            </p>
          </div>
        </template> -->
        <switch-tab2 />
<!--        <switch-login v-if="checkType" />-->
        <!-- <group-service ref="groupService" :isfrom="'login-service'"></group-service> -->
      </div>
      <!-- <fb-google /> -->
      <!-- <download /> -->
      <!-- <groupService /> -->
    </div>
    <loadding v-if="isShowLoadding"></loadding>
  </modMain>
</template>
<script>
import baseSMS from "../../components/baseComs/baseSMS.vue";
import regLoginMixins from './regLogin'
export default {
  mixins: [regLoginMixins],
  data() {
    return {
      inputType: 'text',
    }
  },

  created() {
    let pathName = this.$route.meta.pathName
    let type = pathName && pathName == 'register'
    this.setCheckType(type)
    this.setPhoneVerifyClicked(true)
    let password = localStorage.getItem('password') || ''
    let username = localStorage.getItem('logo_username') || ''
    this.form.username = username
    this.form.password = password
    this.getRegisterBonusValues()
  },

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (from.path == '/verification' && from.query.data) {
        let data = JSON.parse(from.query.data)
        vm.form.username = data.username
        vm.form.password = data.password
      }
    })
  },
  beforeRouteLeave(to, from, next) {
    // ...
    if (to.path == '/areacode' || to.path == '/reset') {
      localStorage.setItem('logo_username', this.form.username)
      localStorage.setItem('password', this.form.password)
    }
    next()
  },
}
</script>
