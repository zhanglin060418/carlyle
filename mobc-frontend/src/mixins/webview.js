export default {
  data() {
    return {}
  },
  beforeCreate() {
    //处理移动端交互代码
    // token=
    // &locale=zh
    // &theme=dark
    // &phone=65
    let token = this.$route.query.token
    if (token) {
      localStorage.setItem('token', token)
    }
    // 多语言 zh or en
    let locale = this.$route.query.locale
    if (locale) {
      localStorage.setItem('locale', locale)
    }
    //判断是否是移动端打开
    let phone = this.$route.query.phone
    if (phone) {
      sessionStorage.setItem('t_app', phone)
    }
  },
  mounted() {
    //主题 dark or light
    let theme = this.$route.query.theme
    if (theme) {
      localStorage.setItem('t_theme', theme)
      document.body.className = 'g-' + theme
    }
  },
}
