<template>
  <div class="tab-head">
    <!-- :style="{'top':offsetTop+'px'}" -->
    <div class="top-tabs">
      <van-tabs
        v-model="active"
        :class="{
          tab3: tabs.length > 2 && tabs.length <= 3,
          tab4: tabs.length > 3,
        }"
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
</template>
<script>
import { mapMutations, mapState } from 'vuex'
var safeAreaInsets = require('safe-area-insets')
export default {
  props: {
    // offetTop: {
    //   type: Number,
    //   default: 44
    // },
    tabIndex: {
      type: Number,
      default: 0,
    },
    tabs: {
      type: Array,
      default: [],
    },
  },
  data() {
    return {
      active: 0,
      offsetTop: 24,
    }
  },
  watch: {
    tabIndex: {
      handler() {
        this.active = this.tabIndex
      },
      immediate: true,
    },
  },
  computed: {
    ...mapState(['positionType', 'couponType']),
  },
  created() {
    // this.$emit('itemClick', this.active)
    this.offsetTop = this.offsetTop + safeAreaInsets.top
  },
  methods: {
    ...mapMutations({
      setPositionType: 'setPositionType',
      setCouponType: 'setCouponType',
    }),
    itemClick(data) {
      // let { path } = this.$route
      // if (path == '/position') {
      //   this.setPositionType(data)
      // } else {
      //   this.setCouponType(0)
      // }
      this.$emit('itemClick', data)
    },
  },
}
</script>
