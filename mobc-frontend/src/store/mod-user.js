// const url = '/simulate' //实盘
const pubKey = `MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCrkvC44r0IsCiTFljQn49xgIAeuvl3YAlihI89FbtY7ip9kVQQnROSZ1Wje+RB8hlh1YDcgsPmpb7eVulwdbOH12NejAw1Cnv220x4oEgNlIG9wpWWD6DnH8AXNWx0yzQASburTin98Smzrhso3nGoPhD+1gtOPRFggRoZqsKhkwIDAQAB`
export default client => ({
  namespaced: true,
  state: {
    CheckMem: [],
    isLogin: false,
    user: {
      user_id: null,
      username: null,
      inviteCode: null,
      headImg: null,
      email: null,
      whatsApp: null,
      telegram: null,
      clerkWhatsApp: null,
      clerkTelegram: null,
      hasPayPwd: null
    },
    withdrawDetail: null,
    kycData: null, //临时保存数据
    Account: null,
    moreData: null,
    currentitem: null,
  },
  actions: {
    listProduct() {
      return client.get('/system/product/getProductList')
    },
    getPurchaseNews({ commit }, params) {
      return client.get('/system/purchase/getPurchaseNews', params)
    },
    getNoticeNews() {
      return client.get('/system/notice/listActive')
    },
    getNoticeNewsByQa() {
      return client.get('/system/notice/listActiveByQa')
    },
    addToPurchase({ commit }, params) {
      return client.post('/system/purchase', params)
    },
    getRegisterBonusValue() {
      return client.get('/registerBonusValue')
    },
    getCaptchaQrcode({ commit }, params) {
      return client.get('/system/user/captchaQrcode', params)
    },
    transferInAsset({ commit }, params) {
      return client.post('/system/asset/transferIn', params)
    },
    transferOutAsset({ commit }, params) {
      return client.post('/system/asset/transferOut', params)
    },
    rewardTransferOut({ commit }, params) {
      return client.post('/system/team/rewardTransferOut', params)
    },
    getUserBalance({ commit }, arg) {
      return client.get('/system/balance/getUserBalance', arg)
    },
    recharge({ commit }, params) {
      return client.post('/system/recharge/createRecharge', params)
    },
    insertMessage({ commit }, params) {
      return client.post('/system/message/createMessage', params)
    },
    createLikes({ commit }, params) {
      return client.post('/system/message/createLikes', params)
    },
    rechargeSuccess({ commit }, params) {
      return client.get('/system/recharge/rechargeSuccess', params)
    },
    setSocial({ commit }, arg) {
      return client.post('system/user/setSocial', arg)
    },
    paymentList({ commit }, arg) {
      return client.get('/system/recharge/getRechargeHistory', arg)
    },
    verifyPayPassword({ commit }, arg) {
      return client.get('/system/user/verifyPayPassword', arg)
    },
    getPurchaseHistory({ commit }, arg) {
      return client.get('/system/purchase/getPurchaseHistory', arg)
    },
    getPurchaseCount({ commit }, arg) {
      return client.get('/system/purchase/getPurchaseCount', arg)
    },
    getPurchaseInterestList({ commit }, arg) {
      return client.get('/system/purchase/getPurchaseInterestList', arg)
    },

    getMessageList({ commit }, arg) {
      return client.get('/system/message/getMessageList', arg)
    },



    getPurchaseListForBuy() {
      return client.get('/system/purchase/getPurchaseListForBuy')
    },

    addSignRecord({ commit }, arg) {
      return client.post('/system/signRecord/addSignRecord', arg)
    },
    addLotteryMove({ commit }, arg) {
      return client.post('/system/lottery/addLotteryMove', arg)
    },
    getSignRecordListByUser({ commit }, arg) {
      return client.get('/system/signRecord/getSignRecordListByUser', arg)
    },
    getMaxSignRecordByUser({ commit }, arg) {
      return client.get('/system/signRecord/getMaxSignRecordByUser', arg)
    },

    getDailySignInAmount(){
      return client.get('/system/signRecord/getDailySignInAmount')
    },
    checkWithdrawStatus({ commit }, arg) {
      return client.get('/system/withdraw/checkWithdrawStatus', arg)
    },
    getAsset(){
      return client.get('/system/asset/getAsset')
    },
    // /withdraw_order/place_otc_order
    withdraw({ commit }, arg) {
      return client.post('/system/withdraw', arg)
    },
    withdrawList({ commit }, arg) {
      return client.get('/system/withdraw/getWithdrawHistory', arg)
    },

    getDrawsList({ commit }, arg) {
      return client.get('/system/draws/getDrawsList', arg)
    },
    getTotalTeam({ commit }, arg) {
      return client.get('/system/user/getTotalTeam', arg)
    },
    getTreatmentText() {
      return client.get('/system/treatment/getList')
    },
    getDailyIncome({ commit }, arg) {
      return client.get('/system/user/getDailyIncome', arg)
    },
    overView({ commit }, arg) {
      return client.get('/system/team/overview', arg)
    },
    incomeOverview({ commit }, arg) {
      return client.get('/system/team/incomeOverview', arg)
    },
    rewardHistory({ commit }, arg) {
      return client.get('/system/team/rewardDetail', arg)
    },
    getTransactionHistoryToday({ commit }, arg) {
      return client.get('/system/history/today', arg)
    },
    getTransactionHistoryYesterday({ commit }, arg) {
      return client.get('/system/history/yesterday', arg)
    },
    getTransactionHistoryLastWeek({ commit }, arg) {
      return client.get('/system/history/lastweek', arg)
    },
    register({ commit }, params) {
      return client.post('register', params)
    },
    verifyUsername({ commit }, arg) {
      return client.get('/system/user/verifyUsername', arg)
    },
    login({ commit }, arg) {
      return client.post('/login', arg).then(res => {
        if (res.token) {
          localStorage.setItem('token', JSON.stringify(res.token || {}))
        }
        return res
      })
    },
    getUserData() {
      return client.get('getUserData')
    },
    getPaymentRules() {
      return client.get('/system/channel/getPaymentRules')
    },
    // 根据旧密码修改登录密码
    updateLoginPwd({ commit }, arg) {
      // ;/user/adeptu / password / login1
      return client.put('/system/user/resetMyPwd', arg)
    },
    setPayPwd({ commit }, arg) {
      return client.post('/system/user/setPayPwd', arg)
    },
    resetLoginPwd({ commit }, arg) {
      return client.put('/system/user/resetUserPwd', arg)
    },
    getHomeIndex() {
      return client.get('/system/home/getHomeList')
    },
    getLotteryList() {
      return client.get('/system/lottery/getLotteryList')
    },
    getHomeAnnouncement() {
      return client.get('/system/notice/getHomeAnnouncement')
    },
    helpImagePage({ commit, state }, arg) {
      return client.get('/helpImage/page', arg).then(res => {
        return res
      })
    },
    userconfigquery({ commit }, arg) {
      return client.get('/user/custom/action/query', arg).then(res => {
        return res
      })
    },
    // account/ queryAccountList
    queryAccountList({ commit }, args = {}) {
      return client.get('/account/queryAccount', args).then(res => {
        commit('SET_ACCOUNT', res.data)
        return res
      })
    },
    // /user/upload/headImg 没有用，暂用http请求
    uploadheadImg({ commit }, arg) {
      return client.post('/upload/img', arg)
    },

    ///入金配置列表
    queryRechargeConfig({ commit }, arg) {
      return client.get('/payment_passageway/queryRechargeConfig', arg)
    },

    ///通道列表
    queryDisplayList({ commit }, arg) {
      return client.get('/payment_passageway/queryDisplayList', arg)
    },
    Pay({ commit }, arg) {
      console.log(arg)
      return client.post('/thirdparty/payment/pay', arg)
    },
    place_order({ commit }, arg) {
      console.log(arg)
      return client.post2('/payment_order/place_order', arg)
    },
    //出金
    Usdt_apply({ commit }, arg) {
      console.log(arg)
      return client.post('/withdraw_otc/usdt_apply', arg)
    },
    //充值列表
    //查询用户累计充值金额
    paymentTotal({ commit }, arg) {
      console.log(arg)
      return client.get('/payment_order/user/total', arg)
    },
    //提币记录

    withdrawConfig({ commit }, arg) {
      return client.get('/withdraw_order/query/withdrawConfig', arg)
    },
    //入金汇率
    currencyquery({ commit }, arg) {
      return client.get('/currency/query', arg)
    },

    burselist({ commit }, arg) {
      return client.request('/user/burse/list', arg)
    },
    ///出金费率
    fee_coin_order({ commit }, arg) {
      return client.get('/withdraw_order/fee_coin_order', arg)
    },
    // /systemConfig/query/fee
    /**
     * 获取费率
     * @param {*} param0
     * @param {*} arg
     */
    queryFee({ commit }, arg) {
      return client.get('/systemConfig/query/fee', arg)
    },
    applyBurse({ commit }, arg) {
      return client.post('/withdraw_order/place_coin_order', arg)
    },
    // amazon/upload
    amazonUpload({ commit, state }, param) {
      return client.post('/upload/s3', param)
    },

    whatsApp({ commit, state }, param) {
      return client
        .get('/whatsApp/queryList', {
          page: 1,
          size: 10,
          enable: true,
        })
        .then(res => {
          let { records } = res.data
          let obj = null
          let url = ''
          if (records && records.length > 0) {
            obj = records[0]
            url = obj.url || 'https://chat.whatsapp.com/DYsecJVdCn03zGOAM1xd4q'
          } else {
            url = 'https://chat.whatsapp.com/DYsecJVdCn03zGOAM1xd4q'
          }
          localStorage.setItem('WATHS_APP_URL', url)
          return res
        })
    },
    codeImg({ commit, state }, param) {
      return client.get('/captchaImage', param)
    },
    smsSend({ commit, state }, param) {
      let encryptor = new JSEncrypt() // 创建加密对象实例
      //之前ssl生成的公钥，复制的时候要小心不要有空格
      encryptor.setPublicKey(pubKey) //设置公钥
      let rsaData = encryptor.encrypt('ST_PT_WIND_PRO_RSA_SECURITY') // 对内容进行加密
      param.rsaData = rsaData
      console.log(rsaData)
      return client.get('/code/sms/send', param)
    },
    // /code/image/verify
    verifyCode({ commit, state }, param) {
      return client.get('/code/image/verify', param)
    },
    verifyFakeCode({ commit, state }, param) {
      return client.get('/code/image/fake/verify', param)
    },
    codeRsa({ commit }, arg) {
      let encryptor = new JSEncrypt() // 创建加密对象实例
      //之前ssl生成的公钥，复制的时候要小心不要有空格

      encryptor.setPublicKey(pubKey) //设置公钥
      let rsaPassWord = encryptor.encrypt('ST_PT_WIND_PRO_RSA_SECURITY') // 对内容进行加密
      console.log(rsaPassWord)
      return
    },
    logout({ commit }, arg) {
      return client.get('/user/logout').then(res => {
        commit('quitLogin')
        return res
      })
    },
    verifyBroker({ commit }, arg) {
      return client.post('/user/verify/broker', arg)
    },
    // updateUserBasic({ commit }, arg) {
    //   return client.post('/user/upset/kyc', arg)
    // },
    // /user/upload/headImg 没有用，暂用http请求
    //根据验证码修改登录密码
    updateLoginPwd2({ commit }, arg) {
      return client.post('/user/update/password/login2', arg)
    },
    codeVerify({ commit }, args) {
      return client.get('/system/product/send', args)
    },
    phoneVerify({ commit }, args) {
      return client.get('/system/product/verify', args)
    },
    // user/queryUserTreeNode    团队列表
    queryUserTreeNode({ commit }, arg) {
      return client.get('/user/queryTree', arg).then(res => {
        return res
      })
    },
    // user/queryIncome   用户收益
    queryIncome({ commit }, arg) {
      return client.get('/user/queryIncome', arg).then(res => {
        return res
      })
    },
    // user/queryRank  我的排名
    queryRank({ commit }, arg) {
      return client.get('/user/queryRank', arg).then(res => {
        return res
      })
    },
    queryLucky({ commit }, arg) {
      return client.get('/lucky/queryLuckyDrawCommodity', arg).then(res => {
        return res
      })
    },
    luckyDraw({ commit }, arg) {
      return client.get('/lucky/luckyDraw', arg).then(res => {
        return res
      })
    },
    // /account/queryStatementPage
    queryStatementPage({ commit }, arg) {
      return client.post('/account/query', arg).then(res => {
        return res
      })
    },

    linkquery({ commit }, arg) {
      return client.get('/client/caller/links/query', arg).then(res => {
        // ----------by 7-------
        // return res
        return
      })
    },
    userMission({ commit }, arg) {
      return client.get('/userMission/queryList', arg).then(res => {
        return res
      })
    },
    award({ commit }, arg) {
      return client.post('/userMission/award', arg).then(res => {
        return res
      })
    },
    awardStatus({ commit }, arg) {
      return client.get('/userMission/awardStatus', arg).then(res => {
        return res
      })
    },
    awardQueryCount({ commit }, arg) {
      return client.get('/userMission/queryCount', arg).then(res => {
        return res
      })
    },
    getluckyDraw({ commit }, arg) {
      return client.get('/lucky/luckyDraw', arg).then(res => {
        return res
      })
    },
    // /sunshine/youtube
    sunshineYoutube({ commit }, arg) {
      return client.post('/sunshine/youtube', arg).then(res => {
        return res
      })
    },
    loginToken({ commit }, arg) {
      return client.post('/google/login', arg).then(res => {
        if (res.code == 0) {
          if (!res.data) return { code: -1, msg: 'error.' }
          let { token, user } = res.data
          if (token) localStorage.setItem('token', token)
          commit('mutQuery', user)
        }
        return res
      })
    },
    //
    queryUserCallerLinks({ commit }, arg) {
      return client
        .get('/client/caller/links/queryUserCallerLinks', arg)
        .then(res => {
          return res
        })
    },
    // /whats_app/queryWhatsAppPage
    queryWhatsAppPage({ commit }, arg) {
      return client.get('/whats_app/queryWhatsAppPage', arg).then(res => {
        return res
      })
    },
    whatsappQueryCount({ commit }, arg) {
      return client.get('/whats_app/queryCount', arg).then(res => {
        return res
      })
    },
    whatsappAward({ commit }, arg) {
      return client.post('/whats_app/audit', arg).then(res => {
        return res
      })
    },
    //碎片相关
    userFragment({ commit }, arg) {
      return client.get('/userFragment/queryList', arg).then(res => {
        return res
      })
    },
    templatequeryList({ commit }, arg) {
      return client.get('/fragment/template/queryList', arg).then(res => {
        return res
      })
    },
    queryRecordList({ commit }, arg) {
      return client.get('/userFragment/queryRecordList', arg).then(res => {
        return res
      })
    },
    giveAway({ commit }, arg) {
      return client.post('/userFragment/giveAway', arg).then(res => {
        return res
      })
    },
    composite({ commit }, arg) {
      return client.post('/userFragment/composite', arg).then(res => {
        return res
      })
    },
    missionCreate({ commit }, arg) {
      return client.post('/userMission/create', arg).then(res => {
        return res
      })
    },
    queryAdTask({ commit }, arg) {
      return client.get('/userMission/queryAdTask', arg).then(res => {
        return res
      })
    },
    adqueryList({ commit }, arg) {
      return client.get('/ad/template/queryList', arg).then(res => {
        return res
      })
    },

  },
  getters: {
    userInfo: state => state.user,
    isLogin: state => {
      const token = localStorage.getItem('token')
      if(token)
        state.isLogin = true
      if(state.user.user_id == undefined)
        state.isLogin = false
      return state.isLogin
    },
    userAccount: state => {
      return state.Account
    },
    /*isKycVerfic: state => {
      let user = state.user
      let kycData = {
        name: '',
        surname: '',
        sex: 1,
        birthday: '',
        country: '',
        city: '',
        postalCode: '',
        address: '',
      }
      Object.keys(kycData).forEach(key => {
        kycData[key] = user[key]
      })
      let arr = Object.keys(kycData).filter(
        v => !kycData[v] && typeof kycData[v] !== 'number'
      )
      return arr <= 0 //true 代表认证过了
    },
    kycCacheData: state => {
      return state.kycData
    },*/
    getMoreData: state => {
      return state.moreData
    },
    currentPaymethod: state => {
      return state.currentitem
    },
  },

  mutations: {
    updateLucky(state, data) {
      if (state.user && state.user.luckyDraws > 0) {
        state.user.luckyDraws = state.user.luckyDraws - 1
        localStorage.setItem('USER_INFO', JSON.stringify(state.user || {}))
      }
    },
    setIsLogin(state, isLogin) {
      state.isLogin = isLogin
    },
    SET_ACCOUNT(state, data) {
      if (data) {
        state.Account = data
      }
    },
    setMoreData(state, data) {
      state.moreData = data
    },
    mutQuery(state, userInfo) {
      // let user = new User()
      // user.merge(userInfo)
      state.user = userInfo
      console.log('userInfo查询的数据', userInfo)
      localStorage.setItem('USER_INFO', JSON.stringify(userInfo || {}))
      state.isLogin = true
    },
    setCurrentPaymethod(state, data) {
      state.currentitem = data
    },
    UPDATE_USER(state, data) {
      const { user_id, username, inviteCode, headImg, email , whatsApp, telegram, clerkWhatsApp, clerkTelegram, hasPayPwd } = data;
      state.user = { user_id, username, inviteCode, headImg, email , whatsApp, telegram, clerkWhatsApp, clerkTelegram, hasPayPwd };
      if(!user_id)
        state.isLogin = false
      else
        state.isLogin = true
      // let _user = localStorage.getItem('USER_INFO') || null
      // if (!_user) return
      // _user = JSON.parse(_user)
      // Object.keys(data).forEach(key => {
      //   _user[key] = data[key]
      // })
      // state.user = _user
      // localStorage.setItem('USER_INFO', JSON.stringify(_user || {}))
    },
    UPDATE_KYC(state, data) {
      let kycData = {
        name: '',
        surname: '',
        sex: 1,
        birthday: '',
        country: '',
        city: '',
        postalCode: '',
        address: '',
      }
      let obj = JSON.parse(localStorage.getItem('KYCDATA') || null)
      if (obj) {
        kycData = Object.assign(obj, data)
      } else {
        Object.keys(data).forEach(key => {
          kycData[key] = data[key] || ''
        })
      }
      state.kycData = kycData
      localStorage.setItem('KYCDATA', JSON.stringify(kycData))
    },
    clearKycData(state) {
      let kycData = {
        name: '',
        surname: '',
        sex: 1,
        birthday: '',
        country: '',
        city: '',
        postalCode: '',
        address: '',
      }
      localStorage.removeItem('KYCDATA')
      state.kycData = kycData
    },
    setRealAuth(state) {
      state.user.realAuth = true
    },
    setfundPasswdBlank(state) {
      state.user.password = true
    },
    SET_WITHDRAW_DETAIL(state, data) {
      state.withdrawDetail = data
    },
    quitLogin(state) {
      state.user = {
        user_id: null,
        username: null,
        inviteCode: null,
        headImg: null,
        email: null,
        whatsApp: null,
        telegram: null,
        hasPayPwd: null,
        clerkWhatsApp: null,
        clerkTelegram: null,
      }
      state.Account = null
    },
  },
})
