<template>
  <div class="service-main">
    <p>
      {{ $t('sa.txt275') }}
    </p>
    <a href="javascript:void(0)" @click="toService()">
      <svg-icon iconClass="whatsapp" width="20" height="20"></svg-icon>
      {{ $t('sa.txt276') }}
    </a>
  </div>
</template>
<script>
import { mapActions } from 'vuex'
export default {
  data() {
    return {
      wathsrls: [],
    }
  },
  created() {
    this.getLink()
  },
  methods: {
    ...mapActions({
      linkquery: 'user/linkquery',
    }),
    getLink() {
      /*----------by 7 ----------
      this.linkquery().then(res => {
        let data = res.data || []
        if (data.length > 0) {
          let waths = data.filter(d => d.type == 'WhatsApp')
          this.wathsrls = waths.map(d => d.link)
        }
      })*/
    },
    toService() {
      let idx = parseInt(Math.random() * this.wathsrls.length, 10)
      let _url = this.wathsrls[idx]
      if (window.webkit) {
        window.webkit.messageHandlers.openBrowser.postMessage(_url)
      } else if (window.appInterface) {
        if (typeof window.appInterface.openBrowser === 'function') {
          window.appInterface.openBrowser(_url)
        } else if (typeof window.appInterface.goToBrowser === 'function') {
          window.appInterface.goToBrowser(_url)
        } else {
          window.location.href = _url
        }
      } else {
        window.location.href = _url
      }
    },
  },
}
</script>
