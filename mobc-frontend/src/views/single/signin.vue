<template>
  <div class="noPadding">
    <div class="login-main">
      <div class="banner share-signin">
        <!-- <img class="logo" src="static/assets/image/dw/logo.png" alt="" /> -->
        <h2>Just Sign Up And Get Rp100,000</h2>
        <p>In Free Investments Instantly</p>
      </div>
      <switch-tab :isNew="false" />
      <base-phone
        v-model.trim="form.username"
        v-if="switchType == 'PHONE' && checkType"
        type="number"
        :placeholder="
          checkType ? $t('home.enterMobile') : $t('home.enterEmail')
        "
      ></base-phone>
      <base-input
        v-model.trim="form.username"
        v-show="!checkType"
        type="text"
        :placeholder="$t('home.enterUserName')"
      ></base-input>
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
      </template>
      <div class="btn-wrap">
        <template v-if="checkType">
          <base-btn
            :btnTitle="$t('home.register')"
            :disabled="disabled"
            @btnClick="btnRegister"
          ></base-btn>
          <switch-login />
        </template>
        <template v-else>
          <base-btn
            :btnTitle="$t('home.login')"
            :disabled="loginDisabled"
            @btnClick="btnLogin"
          ></base-btn>
        </template>
        <template v-if="checkType">
          <div class="register-agree">
            <span
              class="agree-icon"
              :class="{ tongyi: isAgree }"
              @click="isAgree = !isAgree"
            ></span>
            <p class="agree-txt">
              {{ $t('home.year18') }}
              <span @click="openXy(1)">{{ $t('home.tersCond') }}</span>
            </p>
          </div>
        </template>
        <template v-else>
          <div class="forgot">
            <p>
              {{ $t('home.forgot') }}
              <span @click="$router.push('/reset')">{{
                $t('home.reset')
              }}</span>
            </p>
          </div>
        </template>
        <group-service ref="groupService"></group-service>
      </div>
      <!-- <fb-google v-if="$route.query.facebook" /> -->
      <download />
    </div>
    <loadding v-if="isShowLoadding"></loadding>
  </div>
</template>
<script>
import { mapActions, mapGetters, mapMutations } from 'vuex'
import regLoginMixins from './regLogin'
export default {
  mixins: [regLoginMixins],
  created() {
    this.setCheckType(true)
    this.setSwitchType('PHONE')
  },
}
</script>
