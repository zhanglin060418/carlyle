<template>
  <div class="bit-nav flex" :class="{ flexbot: $route.path == '/transaction' }">
    <!-- <template v-if="$route.path == '/position' && title != ''">
      <div class="title">
        {{ title }}
      </div>
      <div class="right-filter" v-if="positionType == 1" @click="showFilter">
        <img src="static/assets/image/icon/icon_transactions_screen@2x.png" alt="" />
      </div>
    </template> -->
  </div>
</template>
<script>
import { mapState, mapGetters } from 'vuex'
import { bus } from '@/utils/bus.js'
export default {
  data() {
    return {
      // currentAccount: 0, //0模拟盘 1 实盘
      filtershow: false,
    }
  },
  computed: {
    ...mapState({
      positionType: 'positionType',
    }),
    ...mapGetters({
      userInfo: 'user/userInfo',
      personalMsgKey: 'account/personalMsgKey',
      diskinfo: 'diskinfo',
    }),
    currentAccount() {
      let type = this.diskinfo
      return type == 'actual' ? 1 : 0
    },
  },
  // created() { },
  // mounted() {
  //   bus.$on('closeshowAccount', () => {
  //     this.filtershow = false
  //   })
  // },
  // destroyed() {
  //   bus.$off('closeshowAccount')
  //   bus.$off('showFilter')
  // },
  methods: {
    // showFilter() {
    //   bus.$emit('showFilter')
    // },
  },
}
</script>
