<template>
  <div class="base-input" :class="{'click':isClick,error:isError}" @click="toArea">
    <div class="input-box">
      <input class="ipt" :placeholder="$t('home.enterCountry')" readonly="readonly">
    </div>
    <div class="counrty-name">
      <span>{{areaItem.label}}</span>
      <i class="right-arrow"></i>
    </div>
  </div>
</template>
<script>
export default {
  name: 'baseCountry',
  props: {
    type: {
      type: String,
      default: "text"
    },
    readonly: {
      type: Boolean,
      default: false
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
    }
    // error: String   //错误提示
  },
  computed: {
    areaItem() {
      return this.$store.getters.getAreaCode()
    }
  },
  data() {
    return {
      isClick: false,
      isShowPwd: true,
      typePwd: ''
    }
  },
  created() {
    this.typePwd = this.type
  },
  methods: {
    toArea() {
      if (!this.readonly) {
        this.$router.push({
          path: '/areacode',
          query: {
            from: 'enterCountry'
          }
        })
      }
    },
    iptClick(e) {
      this.isClick = true
    },
    iptBlur(e) {
      this.isClick = false
    }
  }
}
</script>
