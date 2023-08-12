<template>
  <div
    class="base-input bottom-margin"
    :class="{ click: isClick, error: isError }"
  >
    <div class="input-box">
      <input
        class="ipt"
        :type="type"
        :value="value"
        :placeholder="placeholder"
        :name="name"
        @click="iptClick"
        @blur="iptBlur"
        @input="$emit('input', $event.target.value)"
      />
    </div>
    <div class="class-currency">
      <span>USDT</span>
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
    value: Number,
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
    // error: String   //错误提示
  },
  computed: {
    areaItem() {
      let item = localStorage.getItem('areaItem') || null
      return item ? JSON.parse(item) : { area: 'ng', code: '234' }
    },
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
