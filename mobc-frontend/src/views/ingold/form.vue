<template>
  <div ref="postForm">
    <div v-if="isHtml" v-html="getFormHtml">
    </div>
  </div>
</template>
<script>
import { mapGetters } from 'vuex'
export default {
  data() {
    return {
      isHtml: ''
    }
  },
  computed: {
    ...mapGetters({
      getFormHtml: 'getFormHtml'
    })
  },
  created() {
    this.isHtml = this.$route.query.html
  },
  mounted() {
    this.$nextTick(() => {
      this.isHtml && this.getFormHtml && document.getElementById('pixForm').submit();
    })
  }
}
</script>
