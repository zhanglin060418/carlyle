<template>
  <modMain class="noPadding" :title="$t('sa.txt282')">
    <div class="help-content">
      <helpOne v-if="type == 1"></helpOne>
      <helpTwo v-if="type == 2"></helpTwo>
      <helpThree v-if="type == 3"></helpThree>
      <helpFour v-if="type == 4"></helpFour>
    </div>
  </modMain>
</template>
<script>
import helpOne from './components/help-one.vue'
import helpTwo from './components/help-two.vue'
import helpThree from './components/help-three.vue'
import helpFour from './components/help-four.vue'

export default {
  components: {
    helpOne,
    helpTwo,
    helpThree,
    helpFour,
  },
  data() {
    return {
      type: 1,
    }
  },
  created() {
    this.type = this.$route.query.type || 1
  },
}
</script>
<style lang="less">
.video-main {
  display: flex;
  justify-content: center;
  padding: 0 16px;
}
</style>
