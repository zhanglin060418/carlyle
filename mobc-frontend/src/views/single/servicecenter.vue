<template>
  <modMain title="Customer Service" class="">
    <div class="service-list">
      <div class="service-cell line05" @click="toService('tggroup')">
        <img src="static/assets/image/xapp/logo-tg.png" alt="" />
        <p>Join Telegram Group</p>
      </div>
      <div class="service-cell line05" @click="toService('whats')">
        <img src="static/assets/image/xapp/logo-wa.png" alt="" />
        <p>WhatsApp Service</p>
      </div>
    </div>
  </modMain>
</template>
<script>
import mixinsSerivce from '@/mixins/service'
export default {
  mixins: [mixinsSerivce],
}
</script>
