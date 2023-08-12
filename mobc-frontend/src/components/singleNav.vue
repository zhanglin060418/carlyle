<template>
  <div class="single-nav">
    <van-nav-bar fixed :title="title" :left-arrow="leftArrow" @click-left="onClickLeft">
      <template #left>
        <div class="left-back">
          <img src="static/assets/image/icon/icon_back@2x.png" alt="">
          <span>Back</span>
        </div>
      </template>
      <template #right>
        <i v-if="isShowRightIcon" class="icon i-tanchuang_cancel" @click="onClickRight"></i>
        <div v-if="isAddBank" class="add-bank" @click="$router.push('/addBank')">
          <!-- 添加 -->
          {{ $t('sys.add') }}
        </div>
      </template>
    </van-nav-bar>
    <h2 class="page-title" v-if="!title">{{ $route.meta.title }}</h2>
  </div>
</template>
<script>
export default {
  name: 'singleNav',
  props: {
    title: {
      type: String,
      default: '',
    },
    leftArrow: {
      type: Boolean,
      default: true,
    },
    isShowRightIcon: {
      type: Boolean,
      default: true,
    },
    isAddBank: {
      type: Boolean,
      default: false,
    },
  },
  methods: {
    onClickLeft() {
      if (this.leftArrow) {
        this.$router.go(-1)
      }
    },
    onClickRight() {
      this.$router.go(-1)
    }
  },
}
</script>
