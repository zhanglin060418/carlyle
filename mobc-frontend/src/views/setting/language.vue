<template>
  <modMain :title="$t('home.lang')">
    <div class="language">
      <div class="h10"></div>
      <template v-for="item in languageList">
        <div class="lang" @click="itemClick(item)">
          <p>{{ item.englishName }}</p>
          <i v-show="locale == item.locale" class="icon-ok"></i>
        </div>
      </template>
      <!-- <div class="lang" @click="itemClick('en')">
        <p>{{ $t('home.en') }}</p>
        <i v-show="locale == 'en'" class="icon-ok"></i>
      </div>
      <div class="lang" @click="itemClick('zh')">
        <p>{{ $t('home.zh') }}</p>
        <i v-show="locale == 'zh'" class="icon-ok"></i>
      </div> -->
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
      languageList: [],
    }
  },
  created() {
    this.locale = localStorage.getItem('locale') || 'en_US'
    this.getList()
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
      // window.location.reload()
      setTimeout(() => {
        this.showLoadding = false
      }, 1000)
    },
  },
}
</script>
