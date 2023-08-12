<template>
  <div class="mobile-mail2" :class="checkType ? 'tab1' : 'tab2'">
    <base-btn :btnTitle="$t('home.jumpLogin')" v-if="checkType" :disabled="false" @btnClick="checkClick(2)"></base-btn>
    <base-btn :btnTitle="$t('home.registration')" v-if="!checkType" :disabled="false" @btnClick="checkClick(1)"></base-btn>
  </div>
</template>
<script>
import { mapGetters, mapMutations } from 'vuex'
export default {
  props: {
    tabs: {
      type: Array,
    },
  },
  computed: {
    ...mapGetters({
      checkType: 'getCheckType',
    }),
    tabText() {
      if (this.tabs) {
        return this.tabs
      }
      return [this.$t('home.registration'), this.$t('home.login')]
    },
  },
  methods: {
    ...mapMutations({
      setCheckType: 'SET_PLAT_TYPE',
    }),
    checkClick(type) {
      this.setCheckType(type == 1)
      this.$router.replace({
        path: type == 1 ? '/register' : '/login',
      })
    },
  },
}
</script>
