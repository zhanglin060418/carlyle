<template>
  <modMain :title="$t('home.rule')" class="noPadding">
    <div class="help-content" v-if="imgList && imgList.length > 0">
      <template v-for="url in imgList">
        <img :src="imgBaseUrl + url" alt="" />
      </template>
    </div>
  </modMain>
</template>
<script>
import { mapActions, mapGetters } from 'vuex'
export default {
  data() {
    return {
      imgList: [],
    }
  },
  created() {
    let item = localStorage.getItem('GAMERULE')
    if (item) {
      item = JSON.parse(localStorage.getItem('GAMERULE'))
      let imgs = this.lang == 'zh' ? item.contentImg : item.contentImgEn
      this.imgList = imgs.split(',')
    } else {
      this.getRule()
    }
  },
  computed: {
    ...mapGetters({
      getcurrentGame: 'trade/getcurrentGame',
    }),
  },
  methods: {
    ...mapActions({
      getHelp: 'system/helpImagePage',
    }),
    getRule() {
      let gameSymbol =
        this.$route.query.gameCode || this.getcurrentGame.gameSymbol
      let params = {
        gameCode: gameSymbol,
        symbol: 'Game',
        page: 1,
        size: 2,
      }
      let lang = this.$route.query.locale
        ? this.$route.query.locale
        : localStorage.getItem('locale') || 'en_US'
      this.getHelp(params).then(res => {
        if (res && res.code == 0) {
          let data = res.data.records
          if (data && data.length > 0) {
            let item = data[0]
            let imgs = lang == 'en' ? item.contentImgEn : item.contentImg
            this.imgList = imgs.split(',')
          }
        }
      })
    },
  },
  beforeDestroy() {
    localStorage.removeItem('GAMERULE')
  },
}
</script>
