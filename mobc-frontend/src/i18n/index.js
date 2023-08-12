import Vue from 'vue'
import VueI18n from 'vue-i18n'

Vue.use(VueI18n)
import zh from './zh.js'
import en from './en.js' // 英语
import id_id from './id.js' // 印尼语
// 注册i18n实例并引入语言文件
// var queryString = item => {
//   var svalue = window.location.search.match(
//     new RegExp('[?&]' + item + '=([^&]*)(&?)', 'i')
//   )
//   return svalue ? svalue[1] : svalue
// }
// let locale = queryString('locale')
// let islocale = window.location.href.indexOf('locale') > -1
// let isZh = window.location.href.indexOf('locale=zh') > -1
// let locale = 'en_us'
// if (islocale) {
//   locale = isZh ? 'zh_cn' : 'en_us'
// } else {
//   locale = localStorage.getItem('locale') == 'zh' ? 'zh_cn' : 'en_us'
// }
let locale = localStorage.getItem('locale') || 'en_US'
let lang = locale.substring(0, 2)
const languageList = [
  'zh_CN',
  'en_US',
  'id_ID',
]
let langIndex = languageList.findIndex(d => d.indexOf(lang) > -1)
if (langIndex > -1) {
  locale = languageList[langIndex]
} else {
  locale = 'en_US'
}
localStorage.setItem('locale', locale)
const i18n = new VueI18n({
  locale: locale,
  messages: {
    en_US: en,
    zh_CN: zh,
    id_ID: id_id,
  },
})
// i18n.setLocaleMessage(locale, locale == 'zh_cn' ? zh : en)
export default i18n
