<template>
  <div class="key-box">
    <div class="key-num">
      <div class="key-info" v-for="(i, index) in 3" :key="index">
        <div @click="$emit('btnClick', 1 + (i - 1) * 3)">
          {{ 1 + (i - 1) * 3 }}
        </div>
        <div @click="$emit('btnClick', 2 + (i - 1) * 3)">
          {{ 2 + (i - 1) * 3 }}
        </div>
        <div @click="$emit('btnClick', 3 + (i - 1) * 3)">
          {{ 3 + (i - 1) * 3 }}
        </div>
      </div>
      <div class="key-info">
        <div @click="$emit('btnClick', '.')">.</div>
        <div @click="$emit('btnClick', 0)">0</div>
        <div @click="$emit('removeClick')">
          <i class="icon-keydel"></i>
        </div>
      </div>
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
