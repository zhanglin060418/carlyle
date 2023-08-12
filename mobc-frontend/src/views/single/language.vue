<template>
  <modMain :title="$t('home.lang')" class="setting">
    <div class="language">
      <div class="h10"></div>
      <template v-for="item in languageList">
        <div class="lang" @click="itemClick(item)">
          <p>{{ item.englishName }}</p>
          <i v-show="locale == item.locale" class="icon-ok"></i>
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
      locale: 'en_US',
      showLoadding: false,
      languageList: [
/*        {
          englishName: 'Indonesia',
          locale: 'id_ID',
        },*/
        {
          englishName: 'English',
          locale: 'en_US',
        },
        // {
        //   englishName: '中文',
        //   locale: 'zh_CN',
        // },
      ],
    }
  },
  created() {
    this.locale = localStorage.getItem('locale') || 'en_US'
    // this.getList()
  },
  methods: {
    ...mapActions({
      querylist: 'system/languagequery',
    }),
    getList() {
      this.querylist().then(res => {
        console.log('语言列表', res)
        this.languageList = res.data || []
      })
    },
    itemClick(item) {
      this.showLoadding = true
      this.locale = item.locale
      localStorage.setItem('locale', item.locale)
      localStorage.setItem('locale_name', item.englishName)
      this.$i18n.locale = item.locale
      setTimeout(() => {
        this.showLoadding = false
        window.location.reload()
        // window.location.href = '/'
      }, 1000)
    },
  },
}
</script>
