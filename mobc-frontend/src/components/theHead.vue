<template>
  <div class="top-nav">
    <template v-for="item in topNavList">
      <router-link v-if="isRouteLink" :to="item.path" :class="{'active':$route.path.toLowerCase()==item.path}">{{item.name}}</router-link>
      <a href="javascript:void(0)" v-else @click="itemClick(item)">{{item.name}}</a>
    </template>
  </div>
</template>
<script>
export default {
  name: 'theHead',
  props: {
    topNavList: {
      type: Array,
      default: () => {
        return [
          {
            name: 'Quotation',
            path: '/quotation',
            icon: {
            },
          },
          {
            name: 'Company',
            path: '/company',
            icon: {
            },
          },
          {
            name: 'Technique',
            path: '/technique',
            icon: {
            },
          },

          {
            name: 'Operation',
            path: '/operation',
            icon: {
            },
          },
        ]
      }
    },
    isRouteLink: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      // topNavList: [
      //   {
      //     name: 'Company',
      //     path: '/company',
      //     icon: {
      //     },
      //   },
      //   {
      //     name: 'Technique',
      //     path: '/technique',
      //     icon: {
      //     },
      //   },
      //   {
      //     name: 'Solar energy',
      //     path: '/solar',
      //     icon: {
      //     },
      //   },
      //   {
      //     name: 'Operation',
      //     path: '/operation',
      //     icon: {
      //     },
      //   },
      // ]
    };
  },
  created() {
  },
  mounted() {
  },
  methods: {
    itemClick(item) {
      this.$emit('onClick', item)
    }
  }
}
</script>
