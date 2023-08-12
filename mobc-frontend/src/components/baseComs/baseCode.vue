<template>
  <div class="base-input" :class="{'click':isClick,error:isError}">
    <div class="input-box">
      <input class="ipt" :type="type" :value="value" :placeholder="$t('home.enterImgCode')" :name="name" @click="iptClick" @blur="iptBlur" @input="$emit('input',$event.target.value)">
    </div>
    <div class="class-code">
      <img @click="getCodeImg" :src="image" width="100px" alt="">
    </div>
  </div>
</template>
<script>
import { mapActions } from 'vuex'
export default {
  name: 'baseCode',
  props: {
    type: {
      type: String,
      default: "text"
    },
    value: String,
    placeholder: String,
    name: String,     //name 名称
    disabled: {
      type: Boolean,
      default: false
    },  //是否禁用
    isError: {
      type: Boolean,
      default: false
    },
    sendData: Object //发送地址
    // error: String   //错误提示
  },
  data() {
    return {
      isClick: false,
      image: ''
    }
  },
  created() {
    this.getCodeImg()
  },
  mounted() {
  },
  methods: {
    ...mapActions({
      codeImg: 'user/codeImg'
    }),
    iptClick(e) {
      this.isClick = true
    },
    iptBlur(e) {
      this.isClick = false
    },
    getCodeImg() {
      this.codeImg().then(res => {
        this.image = 'data:image/png;base64,' + res.img
        sessionStorage.setItem('sessionId', res.uuid)
      })
    },
  }
}
</script>
