<template>
  <modMain :title="$t('home.lang')" class="setting">
    <div class="language">
      <div class="h10"></div>
      <template v-for="item in currencyList">
        <div class="lang" @click="itemClick(item)">
          <p>{{ item.currencyName }}</p>
          <i v-show="localeCurrency == item.localeCurrency" class="icon-ok"></i>
        </div>
      </template>
    </div>
    <loadding v-if="showLoadding" />
  </modMain>
</template>
<script>
import { mapActions } from 'vuex'
export default {
  data() {
    return {
      localeCurrency: 'NGN',
      showLoadding: false,
      currencyList: [
        {
          currencyName: 'Nigerian Naira',
          localeCurrency: 'NGN',
        },
/*        {
          currencyName: 'Chinese Yuan',
          localeCurrency: 'Yuan',
        },*/
      ],
    }
  },
  created() {
    this.localeCurrency = localStorage.getItem('localeCurrency') || 'NGN'
    // this.getList()
  },
  methods: {
    ...mapActions({
      querylist: 'system/currencyquery',
    }),
    getList() {
      this.querylist().then(res => {
        console.log('语言列表', res)
        this.currencyList = res.data || []
      })
    },
    itemClick(item) {
      this.showLoadding = true
      this.locale = item.locale
      localStorage.setItem('localeCurrency', item.localeCurrency)
      localStorage.setItem('localeCurrency_name', item.currencyName)
      this.sym = item.localeCurrency
      setTimeout(() => {
        this.showLoadding = false
        window.location.reload()
        // window.location.href = '/'
      }, 1000)
    },
  },
}
</script>
