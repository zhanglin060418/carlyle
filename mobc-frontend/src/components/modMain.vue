<template>
  <div class="single-main">
    <div class="single-nav" :class="{ line05: showLine }" v-if="!isFromPhone">
      <van-nav-bar
        safe-area-inset-top
        fixed
        :title="title"
        :left-arrow="leftArrow"
        @click-left="onClickLeft"
      >
        <!-- <template #left>
          <div class="left-back" v-if="leftArrow">
            <img src="static/assets/image/icon/icon_back@2x.png" alt="">
            <span>{{ $t('sys.back') }}</span>
          </div>
        </template> -->
        <template #right>
          <i
            v-if="isShowRightIcon"
            class="icon i-tanchuang_cancel"
            @click="onClickRight"
          ></i>
          <slot name="title"></slot>
          <slot name="right"></slot>
          <div class="top-fiexd" v-if="from == 'assets'">
            <topTabs :tabs="tabs" @itemClick="itemClick"></topTabs>
          </div>
          <!-- <div v-if="isSave" class="right-btn" :class="{ activity: activity }" @click="rightBtnClick">
            {{ $t('sys.save') }}
          </div> -->
<!--          <div class="right-language">-->
<!--            <div class="check-lang">-->
<!--              <button-->
<!--                class="btn btn-outline-warning dropdown-toggle">-->
<!--&lt;!&ndash;                @click.stop="handerClickLang"&ndash;&gt;-->

<!--&lt;!&ndash;                <template v-if="locale == 'en_US'">&ndash;&gt;-->
<!--&lt;!&ndash;                  <img src="static/assets/image/bcf/flag_ng.png" alt="" />&ndash;&gt;-->
<!--&lt;!&ndash;                </template>&ndash;&gt;-->
<!--&lt;!&ndash;                <template v-if="locale == 'hi_IN'">&ndash;&gt;-->
<!--&lt;!&ndash;                  <img src="static/assets/image/bcf/lang_in.jpg" alt="" />&ndash;&gt;-->
<!--&lt;!&ndash;                </template>&ndash;&gt;-->
<!--&lt;!&ndash;                <template v-if="locale == 'zh_CN'">&ndash;&gt;-->
<!--&lt;!&ndash;                  <img src="static/assets/image/bcf/lang_cn.png" alt="" />&ndash;&gt;-->
<!--&lt;!&ndash;                </template>&ndash;&gt;-->
<!--              </button>-->
<!--            </div>-->
<!--&lt;!&ndash;            <div class="dropdown-menu" v-show="isShowBtn">&ndash;&gt;-->
<!--&lt;!&ndash;              <div class="lang-box" @click="setLang('en_US')">&ndash;&gt;-->
<!--&lt;!&ndash;                <img src="static/assets/image/bcf/lang_en.jpg" alt="" /> EN&ndash;&gt;-->
<!--&lt;!&ndash;              </div>&ndash;&gt;-->
<!--&lt;!&ndash;&lt;!&ndash;              <div class="lang-box" @click="setLang('hi_IN')">&ndash;&gt;&ndash;&gt;-->
<!--&lt;!&ndash;&lt;!&ndash;                <img src="static/assets/image/bcf/lang_in.jpg" alt="" /> हिन्दी&ndash;&gt;&ndash;&gt;-->
<!--&lt;!&ndash;&lt;!&ndash;              </div>&ndash;&gt;&ndash;&gt;-->
<!--&lt;!&ndash;              <div class="lang-box" @click="setLang('zh_CN')">&ndash;&gt;-->
<!--&lt;!&ndash;                <img src="static/assets/image/bcf/lang_cn.png" alt="" /> 中文&ndash;&gt;-->
<!--&lt;!&ndash;              </div>&ndash;&gt;-->
<!--&lt;!&ndash;            </div>&ndash;&gt;-->
<!--          </div>-->
        </template>
      </van-nav-bar>
      <!-- <template v-if="tabs&&tabs.length>0">
        <top-tabs :tabs="tabs" @itemClick="itemClick" :offetTop="44"></top-tabs>
      </template> -->
      <h2 class="page-title" v-if="!title && pageTitle">{{ pageTitle }}</h2>
    </div>
    <div class="single-content">
      <slot></slot>
    </div>
  </div>
</template>
<script>
import { bus } from '@/utils/bus'
export default {
  props: {
    active: {
      type: Number,
      default: 0,
    },
    title: {
      type: String,
      default: '',
    },
    pageTitle: {
      type: String,
      default: '',
    },
    leftArrow: {
      type: Boolean,
      default: true,
    },
    isShowRightIcon: {
      type: Boolean,
      default: true,
    },
    isAddBank: {
      type: Boolean,
      default: false,
    },
    showLogin: {
      type: Boolean,
      default: false,
    },
    showRegister: {
      type: Boolean,
      default: false,
    },
    showLine: {
      type: Boolean,
      default: false,
    },
    from: {
      type: String,
      default: '',
    },
    isSave: {
      type: Boolean,
      default: false,
    },
    activity: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      isFromPhone: false,
      isShowBtn: false,
      tabs: [
        {
          title: '投资',
          index: 0,
        },
        {
          title: '我的资产',
          index: 1,
        },
      ],
      locale: 'en_US',
      languageList: [
        {
          englishName: 'हिन्दी',
          locale: 'hi_IN',
        },
        {
          englishName: 'English',
          locale: 'en_US',
        },
        {
          englishName: '中文',
          locale: 'zh_CN',
        },
      ],
    }
  },
  watch: {
    isShowBtn() {
      if (!this.isShowBtn) {
        document.removeEventListener('click', this.hideLangSet)
        console.log('移出事件')
      }
    },
  },
  beforeMount() {
    // let phone = this.$route.query.phone || sessionStorage.getItem('t_app')
    // this.isFromPhone = !!phone
    // if (this.isFromPhone && window.webkit) {
    //   window.webkit.messageHandlers.setNavTitle.postMessage(this.title)
    // } else if (this.isFromPhone && window.appInterface) {
    //   window.appInterface.setNavTitle(this.title)
    // }
  },
  created() {
    const lang = localStorage.getItem("locale")
    if(lang)
      this.setLang(lang)
  },
  methods: {
    setLang(lang) {
      let item = this.languageList.find(d => d.locale == lang)
      this.locale = item.locale
      localStorage.setItem('locale', item.locale)
      localStorage.setItem('locale_name', item.englishName)
      this.$i18n.locale = item.locale
    },
    handerClickLang() {
      this.isShowBtn = true
      document.addEventListener('click', this.hideLangSet, false)
    },
    hideLangSet() {
      this.isShowBtn = false
    },
    itemClick(idx) {
      this.$emit('tabClick', idx)
    },
    rightBtnClick() {
      if (this.from == 'inMoney') {
        bus.$emit('showRechargeInfo')
      }
      if (this.activity) {
        this.$emit('save')
      }
    },
    onClickLeft() {
      // if (this.from == 'inMoney') {
      //   bus.$emit('showRechargeInfo')
      // }
      // if (this.from == 'coupon') {
      //   bus.$emit('closeCoupon')
      //   return
      // }
      // if (this.from == 'payCpf') {
      //   bus.$emit('closeCpf')
      //   return
      // }
      if (this.leftArrow) {
        let backPath = this.$route.query.backPath || ''
        let path = this.$route.path //verfication2
        if (path == '/product') {
          this.$router.push('fund')
        }
        else if (backPath != '') {
          this.$router.push(backPath)
        } else if (path == '/setting') {
          this.$router.push('/profit')
        } else if (path == '/login' || path == '/register') {
          this.$router.push('/')
        } else {
          this.$router.back(-1)
        }
      }
    },
    onClickRight() {
      this.$router.go(-1)
    },
  },
}
</script>
