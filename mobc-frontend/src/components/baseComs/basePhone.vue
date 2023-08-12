<template>
  <div class="base-input" :class="{ click: isClick, error: isError }">
    <div class="left-quhao" @click="$router.push('/areacode')">
      <div class="areacode rfont" v-if="areaItem">
        <span class="iti-flag" :class="areaItem.area"> </span>
        <span> +{{ areaItem.code }} </span>
      </div>
      <i class="icons-down"></i>
    </div>
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
