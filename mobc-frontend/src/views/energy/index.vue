<template>
  <mod-main :title="title" class="energy setting">
    <div class="history-box" slot="right" @click="viewHistory">
      {{ type == 1 ? $t('bill.deposithistory') : $t('bill.withdrawhistory') }}
    </div>
    <buy v-if="type == 1" ref="buyPage" />
    <sell v-else />
  </mod-main>
</template>
<script>
import Buy from './buy.vue'
import Sell from './sell.vue'
export default {
  components: {
    Buy,
    Sell,
  },
  computed: {
    type() {
      return this.$route.query.type || 1
    },
    title() {
      return this.type == 1 ? this.$t('sa.txt137') : this.$t('sa.txt138')
    },
  },

  beforeRouteLeave(to, from, next) {
    // ...
    if (to.name == 'rechargeinfo' && this.type == 1) {
      from.meta.keepAlive = true
    }
    next()
  },
  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (vm.type == 1 && vm.$refs.buyPage.$refs.InfoRef) {
        vm.$refs.buyPage.$refs.InfoRef.show = false
      }
    })
  },
  methods: {
    viewHistory() {
      this.$router.push({
        path: '/inOutList',
        query: {
          type: this.type,
        },
      })
    },
  },
}
</script>
