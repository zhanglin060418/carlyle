<template>
  <div class="base-input" :class="{ error: isError }">
    <div class="input-box">
      <input
          class="ipt"
          :value="value"
          :placeholder="placeholder"
          :name="name"
          :disabled="disabled"
          @input="$emit('input', $event.target.value)"
      />
    </div>
    <vue-countdown v-if="timer" :time="60000" @end="onCountdownEnd" v-slot="{ totalSeconds }" style="color: gray; margin-left: 3px; margin-right: 3px" >{{ $t('status.resend')}}({{ totalSeconds }} s)</vue-countdown>
<!--    <div v-if="type == 'password'" class="action">-->
<!--      <img-->
<!--          v-if="typePwd == 'password'"-->
<!--          @click="typePwd = 'text'"-->
<!--          src="static/assets/image/icon/icon_fx_gb@2x.png"-->
<!--          alt=""-->
<!--      />-->
<!--      <img-->
<!--          v-else-->
<!--          @click="typePwd = 'password'"-->
<!--          src="static/assets/image/icon/icon_fx_kq@2x.png"-->
<!--          alt=""-->
<!--      />-->
<!--    </div>-->
  </div>
</template>
<script>
import {mapMutations} from "vuex";

export default {
  name: 'baseSMS',
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
    isSend: {
      type: Boolean,
      default: false,
    },
    timer: {
      type: Boolean,
      default: false
    }
    // error: String   //错误提示
  },
  data() {
    return {
    }
  },
  methods: {
    ...mapMutations({
      setPhoneVerifyClicked: 'SET_PHONE_VERIFY',
    }),
    startCountdown: function () {
      this.setPhoneVerifyClicked(false)
    },
    onCountdownEnd: function () {
      this.setPhoneVerifyClicked(true)
    },
  },
}
</script>
