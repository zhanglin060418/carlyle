<template>
  <modMain title="Recharge">
    <div style="padding-bottom: 30px">
      <iframe
        v-if="src"
        :src="src"
        style="width: 100%; height: 1100px"
        name="iframe_a"
        frameborder="no"
        border="0"
        marginwidth="0"
        marginheight="0"
        allowtransparency="yes"
      ></iframe>
    </div>
  </modMain>
</template>
<script>
export default {
  data() {
    return {
      src: '',
    }
  },
  mounted() {
    this.src = this.$route.query.src
  },
}
</script>
