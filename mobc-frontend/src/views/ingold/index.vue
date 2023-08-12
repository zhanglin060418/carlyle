<template>
  <div class="ingold-main" :class="{ lenovo: isLenovo }">
    <div class="flexd-head">
      <div class="tab-nav-head">
        <div class="top-nav-tabs">
          <van-tabs
            v-model="type"
            :class="{ tab3: tabs.length > 2 }"
            @click="itemClick"
          >
            <template v-for="(item, idx) in tabs">
              <van-tab :title="item.title" :key="idx">
                <slot name="content"></slot>
              </van-tab>
            </template>
          </van-tabs>
        </div>
      </div>
    </div>
    <div class="lenovo-height" v-if="isLenovo"></div>
    <div class="ingold-content">
      <paymethods v-if="type == 0"></paymethods>
      <Deposit v-else></Deposit>
    </div>
  </div>
</template>
<script>
import Deposit from './deposit'
// import InMoney from './inMoney'
import paymethods from './methods'
export default {
  components: {
    Deposit,
    paymethods,
  },
  data() {
    return {
      type: 0,
      tabs: [
        {
          title: '购买USDT',
        },
        {
          title: '钱包充币',
        },
      ],
    }
  },
  computed: {
    isLenovo() {
      return localStorage.getItem('plat') || null
    },
  },
  created() {
    this.$store.dispatch('user/query')
    // this.$store.dispatch('user/whatsApp')
  },
  methods: {
    itemClick(type) {
      this.type = type
      if (window.webkit) {
        window.webkit.messageHandlers.changeDeposit.postMessage({ type })
      } else if (window.appInterface) {
        window.appInterface.changeDeposit(type)
      }
    },
  },
}
</script>
