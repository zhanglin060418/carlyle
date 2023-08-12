<template>
  <modMain :title="$t('user.userRule')" class="setting xieyi">
    <xieyiZh v-if="$i18n.locale=='zh_CN'"/>
    <xieyiEn v-else/>
  </modMain>
</template>
<script>
import xieyiEn from './xieyi_en'
import xieyiZh from './xieyi_zh'
import webViewMixins from '@/mixins/webview.js'
export default {
  mixins: [webViewMixins],
  components: {
    xieyiEn,
    xieyiZh
  },
  data() {
    return {
      locale: localStorage.getItem('locale'),
    }
  },
}
</script>
<style lang="less">
.xieyi {
  color: #000000;
  .title {
    font-size: 14px;
  }
  p {
    line-height: 24px;
    font-size: 12px;
    &.title {
      margin-top: 15px;
    }
  }
  h2 {
    &.title {
      margin-top : 10px;
      text-align: center;
      line-height: 30px;
      font-size: 18px;
    }
  }
  .indet {
    text-indent: 2em;
  }
}
</style>
