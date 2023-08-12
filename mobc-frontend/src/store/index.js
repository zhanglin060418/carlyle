import Vue from 'vue'
import Vuex from 'vuex'
import SocketAPI from '@/utils/http.js'
import ModUser from './mod-user.js'
import ModOrder from './mod-order.js'
import ModBackCard from './mod-bankcard.js'

const debug = process.env.NODE_ENV !== 'production'

Vue.use(Vuex)
export default new Vuex.Store({
  state: {
    isLogin: false,
    theme: 'dark', //dark or light
    imgData: {
      sessionId: '',
      data: null,
    },
    currentMenuId: 1,
    UserInfo: null,
    navType: 1,
    recordType: 0,
    positionType: 0,
    couponType: 0,
    diskinfo: 'simulate',
    imgBaseUrl: '', //图片前缀
    checkType: true, //true注册 false登录
    clickedPhoneVerify: false,
    isShowBlance: true, //金额是否显示
    switchType: 'PHONE', // EMAIL邮箱 or PHONE手机
    hasNewObj: {
      hasNewLuckyMoney: false,
      hasNewNotice: false,
    },
    payMethods: null, //当前选择支付方式
    areaCode: {
      label: 'Nigeria',
      area: 'ng',
      code: '234',
      nameCN: '尼日利亚',
    },
    formHtml: '',
  },
  modules: {
    user: ModUser(SocketAPI),
    order: ModOrder(SocketAPI),
    bankCard: ModBackCard(SocketAPI),
  },
  strict: debug,
  getters: {
    getFormHtml: state => {
      return state.formHtml
    },
    getCheckType: state => {
      return state.checkType
    },
    getClickedPhoneVerifyEvent: state => {
      return state.clickedPhoneVerify
    },
    getSwitchType: state => {
      return state.switchType
    },
    imageData: state => {
      return state.imgData.data
    },
    imgSessionId: state => {
      return state.imgData.sessionId
    },
    diskinfo: state => {
      return state.diskinfo
    },
    getNavType: state => {
      let navType = localStorage.getItem('NAVTYPE')
      if (navType) {
        return Number(navType)
      }
      return state.navType
    },
    getCssSkin: state => () => {
      //getter如果不执行 改成方法
      let _cssSkin = localStorage.getItem('t_theme')
      if (_cssSkin) {
        return _cssSkin
      }
      return state.theme
    },
    imgBaseUrl: state => {

      var protocol = location.protocol; // 获取协议，例如："http:" 或 "https:"
      var hostname = location.hostname; // 获取域名，例如："www.example.com"
     var currUrl =  protocol+"//"+hostname
      var port = location.port;
      //兼容下测试环境带端口的访问
      if(port!=''){
        currUrl =  protocol+"//"+hostname+":"+port;
      }
      //如果dev测试环境另外算
      if(process.env.NODE_ENV === 'development'){
        return process.env.NODE_ENV_URL;//这个写死了挺不好的，但是先这样用着先
      }else{
        return currUrl+"/prod-api"
      }
    },
    getShowDot: state => () => {
      return state.hasNewObj
    },
    currentPaymethod: state => () => {
      return state.payMethods
    },
    getAreaCode: state => () => {
      let item = localStorage.getItem('areaItem') || null
      return item ? JSON.parse(item) : state.areaCode
    },
  },
  actions: {
    /**mobilephone
     * 获取图形验证码
     * @param {*} param0
     * @param {*} arg
     */
    codeImage({ commit }, arg) {
      SocketAPI.get('/code/image', {}).then(res => {
        commit('SET_IMG_DATA', res)
      })
    },
    /**
     * 查询图片服务器地址
     */
    getUrlImage({ commit }, arg) {
      return SocketAPI.get('/upload/path', {}).then(res => {
        console.log(res)
        commit('SET_IMG_URL', res.data || '')
        return res
      })
    },
    /**
     * 获取系统手续费
     */
    getQueryFee: () => {
      return SocketAPI.get('/systemConfig/query/fee')
    },
    // 排行榜
    incomeRank: ({ commit }, args) => {
      return SocketAPI.get('/incomeRank/queryList', args)
    },
    systemTime: ({ commit }, args) => {
      return SocketAPI.get('/system/time', args)
    },
  },
  mutations: {
    setCurrentPaymethod(state, data) {
      state.payMethods = data
    },
    setHtml(state, strHtml) {
      state.formHtml = strHtml
    },
    SET_ISSHOWDOT(state, data) {
      state.hasNewObj = Object.assign(state.hasNewObj, data)
    },
    SET_PLAT_TYPE(state, type) {
      state.checkType = type
    },
    SET_PHONE_VERIFY(state, type) {
      state.clickedPhoneVerify = type
    },
    SET_SWITCH_TYPE(state, type) {
      state.switchType = type
    },
    SET_CSSSKIN(state, data) {
      state.theme = data
      localStorage.setItem('t_theme', data)
    },
    SET_USERINFO(state, data) {
      state.UserInfo = data
    },
    SET_CURRENT_ID(state, data) {
      state.currentMenuId = data
    },
    SET_TYPE(state, type) {
      state.type = type
    },
    // 退出登录
    SET_USERINFO_CLEAR(state, data) {
      // state.UserInfo = null
      state.currentMenuId = 1
      // storageHelper.clearCache()
    },
    SET_IMG_DATA(state, res) {
      console.log(res)

      if (res.image) {
        state.imgData.data = res.image
      }
      if (res.sessionId) {
        state.imgData.sessionId = res.sessionId
      }
    },
    SET_FOOT_NAVTYPE(state, data) {
      localStorage.setItem('NAVTYPE', data)
      state.navType = data
    },
    SET_RECORD_TYPE(state, type) {
      state.recordType = type
    },
    //设置持仓默认选中状态
    setPositionType(state, type) {
      state.positionType = type
    },
    //设置优惠卷默认选中状态
    setCouponType(state, type) {
      state.couponType = type
    },
    SET_IMG_URL(state, url) {
      state.imgBaseUrl = url
      localStorage.setItem('imgBaseUrl', url)
    },
    DISKINFO(state, diskinfo) {
      state.diskinfo = diskinfo
    },
    SET_BLANCE_STATE(state, isShow) {
      state.isShowBlance = isShow
    },
  },
})
