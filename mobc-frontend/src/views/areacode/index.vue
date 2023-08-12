<template>
  <modMain :title="title" class="noPadding">
    <country></country>
  </modMain>
</template>
<script>
import country from './country'
export default {
  components: {
    country,
  },
  data() {
    return {
      title: '',
    }
  },
  created() {
    let { from } = this.$route.query
    this.title = this.$t('home.enterCountry')
  },
}
</script>
