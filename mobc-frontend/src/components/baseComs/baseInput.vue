<template>
  <div class="base-input" :class="{ click: isClick, error: isError }">
    <div class="input-box">
      <input
        class="ipt"
        :type="typePwd"
        :value="value"
        :placeholder="placeholder"
        :name="name"
        :disabled="disabled"
        @click="iptClick"
        @blur="iptBlur"
        @input="$emit('input', $event.target.value)"
      />
    </div>
    <div v-if="type == 'password'" class="action">
      <img
        v-if="typePwd == 'password'"
        @click="typePwd = 'text'"
        src="static/assets/image/icon/icon_fx_gb@2x.png"
        alt=""
      />
      <img
        v-else
        @click="typePwd = 'password'"
        src="static/assets/image/icon/icon_fx_kq@2x.png"
        alt=""
      />
    </div>
  </div>
</template>
<script>
export default {
  name: 'baseInput',
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
    // error: String   //错误提示
  },
  data() {
    return {
      isClick: false,
      isShowPwd: true,
      typePwd: '',
    }
  },
  created() {
    this.typePwd = this.type
  },
  methods: {
    iptClick(e) {
      this.isClick = true
    },
    iptBlur(e) {
      this.isClick = false
    },
  },
}
</script>
