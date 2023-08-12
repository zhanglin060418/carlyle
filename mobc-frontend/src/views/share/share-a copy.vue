<template>
  <div class="share-land share-a">
    <div class="boxs box1">
      <img src="static/assets/image/xapp/land/dingbu-banner@2x.png" alt="">
      <div class="learn-more" @click="toSignIn">Learn More</div>
    </div>
    <div class="boxs box2" @click="toSignIn">
      <img src="static/assets/image/xapp/land/group1.png" alt="">
    </div>
    <comments />
    <div class="free-reg" @click="toSignIn">
      Free registration
    </div>
  </div>
</template>
<script>
import comments from './components/comments'
import comMixins from './comMixins'
export default {
  mixins: [comMixins],
  components: {
    comments
  },
}
</script>
