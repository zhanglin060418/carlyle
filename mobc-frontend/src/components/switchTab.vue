<template>
  <div class="mobile-mail" :class="checkType ? 'tab1' : 'tab2'">
    <span :class="{ active: checkType }" @click="checkClick(1)">{{
      tabText[0]
    }}</span>
    <span :class="{ active: !checkType }" @click="checkClick(2)">
      {{ tabText[1] }}</span
    >
  </div>
</template>
<script>
import { mapGetters, mapMutations } from 'vuex'
export default {
  props: {
    tabs: {
      type: Array,
    },
    isNew: {
      type: Boolean,
      default: true,
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
      if (this.isNew) {
        this.$router.replace({
          path: type == 1 ? '/register' : '/login',
        })
      }
    },
  },
}
</script>
