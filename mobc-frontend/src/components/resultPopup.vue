<template>
  <van-popup v-model="show" class="popup-result" get-container="body">
    <!-- <img
      class="poput-close"
      src="static/assets/image/dw/close.png"
      alt=""
      @click.stop="show = false"
    /> -->
    <div class="content">
      <slot name="content"></slot>
      <div class="info" v-if="pData">
        <!-- <img
          src="static/assets/image/dw/success_icon.png"
          class="s-icon"
          alt=""
          v-if="pData.type == 1 || pData.type == 4"
        /> -->
        <p class="txt" v-html="pData.contentTxt"></p>
      </div>
      <div
        v-if="!isShowCancel"
        class="popup-btn"
        :class="{ 'btn-view': pData && pData.type == 1 }"
        @click="popBtnClick(pData ? pData.type : -1)"
      >
        {{ pData ? pData.btnTxt : $t('sys.confirm') }}
      </div>
      <div v-else class="popup-btn-group">
        <div class="popup-btn btn-view" @click="show = false">
          {{ $t('sys.cancel') }}
        </div>
        <div class="popup-btn" @click="quitout">
          {{ $t('sys.confirm') }}
        </div>
      </div>
    </div>
  </van-popup>
</template>
<script>
export default {
  props: {
    showResult: {
      type: Boolean,
      defalut: false,
    },
    pData: {
      type: Object,
      default: null,
    },
    isShowCancel: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      show: false,
    }
  },
  watch: {
    showResult: {
      handler() {
        this.show = this.showResult
      },
      immediate: true,
    },
  },
  methods: {
    popBtnClick(type) {
      if (type == 2) {
      } else if (type == 0) {
        this.$router.push({
          path: '/energy',
          query: {
            type: 1,
          },
        })
      } else if (type == 1) {
        this.$router.push({
          path: '/product',
          query: {
            type: 1,
          },
        })
      } else if (type == 3) {
        //查看能量兑换记录
        this.$router.push({
          path: '/inOutList',
          query: {
            type: 2,
          },
        })
      }
      this.show = false
    },
    quitout() {
      this.show = false
      this.$emit('sure')
    },
  },
}
</script>
