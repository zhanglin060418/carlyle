<template>
  <modMain title="FAQ">
    <!-- <howTo></howTo> -->
    <qa></qa>
  </modMain>
</template>
<script>
import howTo from './components/howTo'
import qa from './components/qa'
// import webViewMixins from '@/mixins/webview.js'
export default {
  // mixins: [webViewMixins],
  components: {
    howTo,
    qa,
  },
}
</script>
