<template>
  <van-popup
    v-model="currentShow"
    round
    position="bottom"
    @close="close"
    :style="{ height: height }"
    @click-overlay="close"
    safe-area-inset-bottom
  >
    <div class="popup-box">
      <div v-if="showheader" class="popup-header">
        <p v-if="title">{{ title }}</p>
        <!-- <i class="icon i-tanchuang_cancel" @click="show=false"></i> -->
        <slot name="title"></slot>
      </div>
      <div class="popup-content">
        <slot></slot>
      </div>
    </div>
  </van-popup>
</template>

<script>
export default {
  props: {
    show: {
      type: Boolean,
      default: false,
    },
    showheader: {
      type: Boolean,
      default: true,
    },
    closeable: {
      type: Boolean,
      default: false,
    },
    height: {
      type: String,
      default: '70%',
    },
    iconName: {
      type: String,
      default: 'zhutu',
    },
    title: {
      type: String,
      default: '',
    },
  },
  data() {
    return {
      active: 0,
      showPopup: false,
      currentShow: false,
    }
  },
  watch: {
    show() {
      this.currentShow = this.show
    },
  },
  methods: {
    close() {
      this.$emit('close')
    },
  },
}
</script>
