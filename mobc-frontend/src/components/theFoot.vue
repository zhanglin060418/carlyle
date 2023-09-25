<template>
  <van-tabbar v-model="active" active-color="#0a364a ">
    <template v-for="item in footNavList">
      <!-- :icon="item.icon" -->
      <van-tabbar-item :to="item.path">
        <template #icon="props">
          <!-- <img :src="props.active ? item.icon.inactive : item.icon.active" />
          <span>{{ item.name }}</span> -->
          <!-- <svg-icon :iconClass="item.svg" width="26" height="26"></svg-icon> -->
          <i class="iconfont" :class="item.icon"></i>
        </template>
        <!-- {{ item.name }} -->
      </van-tabbar-item>
    </template>
  </van-tabbar>
</template>
<script>
export default {
  name: 'theFoot',
  computed: {
    theme() {
      return this.$store.getters.getCssSkin()
    },
    isShowDot() {
      let hasNewObj = this.$store.getters.getShowDot()
      if (hasNewObj) {
        return hasNewObj.hasNewLuckyMoney || hasNewObj.hasNewNotice
      } else {
        return true
      }
    },
  },

  data() {
    return {
      active: 0,
      footNavList: [
        {
          name: 'Home', //首页
          path: '/home',
          icon: 'icon-home',
          // icon: {
          //   active: require('@/assets/image/tab/task1.png'),
          //   inactive: require('@/assets/image/tab/task1_h.png'),
          // },
        },
        {
          name: 'Community', //任务
          path: '/community',
          icon: 'icon-team',
          // icon: {
          //   active: require('@/assets/image/tab/task3.png'),
          //   inactive: require('@/assets/image/tab/task3_h.png'),
          // },
        },
        {
          name: 'Fund', //我的设备
          path: '/fund',
          icon: 'icon-24gl-podium',
          // icon: {
          //   active: require('@/assets/image/tab/task2.png'),
          //   inactive: require('@/assets/image/tab/task2_h.png'),
          // },
        },

        {
          name: 'Tim', //团队
          path: '/team',
          icon: 'icon-git-merge',
          // icon: {
          //   active: require('@/assets/image/tab/task4.png'),
          //   inactive: require('@/assets/image/tab/task4_h.png'),
          // },
        },
        {
          name: 'My', //我的
          path: '/profit',
          icon: 'icon-person_circle',
          // icon: {
          //   active: require('@/assets/image/tab/task5.png'),
          //   inactive: require('@/assets/image/tab/task5_h.png'),
          // },
        },
      ],
    }
  },
  watch: {
    $route: {
      handler() {
        let { path } = this.$route
        if (path == '/home'||path == '/news') {
          this.active = 0
        } else if (path == '/community') {
          this.active = 1
        } else if (path == '/fund') {
          this.active = 2
        } else if (path == '/team') {
          this.active = 3
        } else {
          this.active = 4
        }
      },
      immediate: true,
    },
  },
}
</script>
