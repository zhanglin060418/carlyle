<template>
  <div class="base-btn">
    <button :class="{'no-round':!round}" v-if="btnTitle" :disabled="disabled" @click="$emit('btnClick')">{{btnTitle}}</button>
  </div>
</template>
<script>
export default {
  name: 'baseInput',
  props: {
    btnTitle: String,     //button标题
    disabled: {
      type: Boolean,
      default: true
    },  //是否禁用
    round: {
      type: Boolean,
      default: true
    },
  }
}
</script>
