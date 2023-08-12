<template>
  <div class="share-land share-b">
    <!-- <div class="boxs top">
      <h1><i>Register for Rp324</i></h1>
      <h2>You Don’t have to pay for it</h2>
    </div> -->
    <div class="boxs box1" @click="toSignIn">
      <img src="static/assets/image/xapp/land/hongbao@2x.png" alt="" />
      <div class="learn-more" @click="toSignIn">Limited time today</div>
    </div>
    <div class="boxs box2" @click="toSignIn">
      <img src="static/assets/image/xapp/land/anniu-1@2x.png" alt="" />
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
    comments,
  },
}
</script>
