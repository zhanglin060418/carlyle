<template>
  <div class="my-btn-box" :class="{ 'login-service': isfrom }">
    <div class="my-btn tg" @click="toService('tggroup')">
      <img src="static/assets/image/xapp/xins/icon_logo_08.png" alt="" />
      <p>{{ $t('dw.t165') }}</p>
    </div>
    <!-- <div class="my-btn tg" @click="toService('tgSiaran')">
      <img src="static/assets/image/dw/telegram.png" alt="" />
      <p>TG CHANNEL</p>
    </div> -->
    <div class="my-btn whats" @click="toService('whats')">
      <!-- <img src="static/assets/image/dw/btn_whats.png" alt="" /> -->
      <img src="static/assets/image/xapp/xins/icon_logo_07.png" alt="" />
      <p>{{ $t('dw.t166') }}</p>
    </div>
  </div>
</template>

<script>
import mixinsSerivce from '@/mixins/service'
export default {
  name: 'groupService',
  props: ['isfrom'],
  mixins: [mixinsSerivce],
}
</script>
