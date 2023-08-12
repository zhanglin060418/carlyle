<template>
  <div class="share-land share-c">
    <div class="boxs box1" @click="toRegIn">
      <img src="static/assets/image/dw/land/ld3.png" alt="" />
      <div class="learn-more" @click="toRegIn">Limited time collection</div>
    </div>
    <div class="main userinfo-container">
      <img class="icon-star" src="static/assets/image/dw/land/star.png" alt="" />
      <h3>Register to get funds</h3>

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
      <switch-login />
      <div class="reg-btn" @click="toSignIn">register</div>
    </div>
    <div class="content-c">
      <group-service ref="groupService"></group-service>
    </div>
    <div class="boxs box2" @click="toRegIn" style="margin-top:10px">
      <img src="static/assets/image/dw/land/ld4.png" alt="" />
    </div>
    <!-- <div class="content-c">
      <comments />
    </div> -->
    <div class="free-reg" @click="toRegIn">
      Free registration
    </div>
  </div>
</template>
<script>
import comments from './components/comments'
import comMixins from './comMixins'
export default {
  mixins: [comMixins],
  components: {
    comments,
  },
}
</script>
<style lang="less" scoped>
.userinfo-container {
  width: 360px;
  height: 336px;
  margin: 0 auto;
  background: linear-gradient(0deg, #094cf7 0%, #5bcaf9 99%);
  box-shadow: 0px 4px 4px rgba(0, 0, 0, 0.6);
  border-radius: 8px;
  padding: 19px 19px 10px;
  position: relative;
  .icon-star {
    width: 44px;
    height: 42px;
    position: absolute;
    top: 5px;
    right: 10px;
  }
  > h3 {
    font-size: 20px;
    line-height: 26px;
    text-align: center;
    letter-spacing: -0.045em;
    text-transform: capitalize;
    color: #fbf6d9;
    text-align: center;
    margin-bottom: 10px;
  }
  .base-input {
    margin-bottom: 10px;
    border-radius: 4px;
  }
  .reg-btn {
    width: 100%;
    height: 50px;
    background: linear-gradient(180deg, #ffb13d 0%, #f45e1e 100%);
    box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3),
      inset 0px -2px 4px rgba(255, 169, 39, 0.25);

    font-size: 21.2871px;
    line-height: 27px;
    text-align: center;
    letter-spacing: -0.045em;
    text-transform: capitalize;
    color: #fff;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 50px;
    margin-top: 10px;
  }
}
.content-c {
  padding: 0 16px;
}
</style>
