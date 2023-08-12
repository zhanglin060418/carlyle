<template>
  <div class="right-bottom">
    <!-- <div class="box suipian" @click="$router.push('/puzzles')">
      <img src="static/assets/image/lucky/pintu.png" alt="" class="zp" />
      <p class="btn">{{ $t('dw.t85') }}</p>
    </div> -->
    <div class="box dazhuanpan" @click="$router.push('/drawLucky')">
      <!-- <van-icon name="clear" class="close" @click.stop="showDzp = false" /> -->
      <img src="static/assets/image/lucky/lk_zp.png" alt="" class="zp" />
      <p class="btn">{{ $t('dw.t86') }}</p>
      <!-- <img src="static/assets/image/lucky/bt.png" alt="" class="bt" />
      <img src="static/assets/image/lucky/jx.png" alt="" class="jx" /> -->
    </div>
  </div>
</template>
