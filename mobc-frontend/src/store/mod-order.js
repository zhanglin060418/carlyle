export default client => ({
    namespaced: true,
    state: {
        limitData: null,
        myLimitIds: null,
    },
    actions: {
        commodityQuery({ commit }, arg) {
            return client.get('/commodity/query', arg).then(res => {
                if (res.code == 0) {
                    if (arg.type == 'LIMIT') commit('setLimitData', res.data.records)
                }
                return res
            })
        },
        orderPlace({ commit }, arg) {
            return client.post('/order/place', arg).then(res => {
                return res
            })
        },
        //立即抢购
        limitPlace({ commit }, arg) {
            return client.post('/order/limitPlace', arg).then(res => {
                return res
            })
        },
        // 查询已经抢购过的商品
        queryLimitPurchased({ commit }, arg) {
            return client.get('/order/queryLimitPurchased', arg, true).then(res => {
                if (res.code == 0) {
                    commit('setLimitIds', res.data)
                }
                return res
            })
        },
        orderqueryPage({ commit }, arg) {
            return client.get('/order/queryPage', arg).then(res => {
                return res
            })
        },
        // 我的宠物
        myOrder({ commit }, arg) {
            return client.get('/order/queryPage', arg).then(res => {
                return res
            })
        },
        //交易记录 BUY, EXPIRE, REVOKE, SELLED, SELLING
        recordqueryPage({ commit }, arg) {
            return client.get('/record/queryPage', arg).then(res => {
                return res
            })
        },
        //我要买
        queryBuyCachePage({ commit }, arg) {
            return client.get('/order/queryBuyCachePage', arg).then(res => {
                return res
            })
        },
        // 我的动物和正在交易
        queryStatusCachePage({ commit }, arg) {
            return client.get('/order/queryStatusCachePage', arg).then(res => {
                return res
            })
        },
        buy({ commit }, arg) {
            return client.post('/order/buy', arg).then(res => {
                return res
            })
        },
        sell({ commit }, arg) {
            return client.post('/order/sell', arg).then(res => {
                return res
            })
        },
        revoke({ commit }, arg) {
            return client.post('/order/revoke', arg).then(res => {
                return res
            })
        },
        queryTotalUserIncome({ commit }, arg) {
            return client.get('/order/queryTotalUserIncome', arg).then(res => {
                return res
            })
        },
        // /order/queryOrderTotalByUserId
        queryOrderTotalByUserId({ commit }, arg) {
            return client.get('/order/queryOrderTotalByUserId', arg).then(res => {
                return res
            })
        },
        // /order/orderSettlement
        orderSettlement({ commit }, arg) {
            return client.get('/order/orderSettlement', arg).then(res => {
                return res
            })
        },
        purchaseGoods({ commit }, arg) {
            return client.get('/purchaseGoods/client/queryList', arg).then(res => {
                return res
            })
        },
        createUserInfo({ commit }, arg) {
            return client.post('/purchaseGoods/createUserInfo', arg).then(res => {
                return res
            })
        },
        purchaseGoodsBuy({ commit }, arg) {
            return client.post('/purchaseGoods/buy', arg).then(res => {
                return res
            })
        },
        puruploadImage({ commit }, arg) {
            return client.post('/purchaseGoods/uploadImage', arg).then(res => {
                return res
            })
        },
        clientQuery({ commit }, arg) {
            return client.get('/paymentImage/clientQuery', arg).then(res => {
                return res
            })
        },
        // order/queryOrderCount
        queryOrderCount({ commit }, arg) {
            return client.get('/order/queryOrderCount', arg).then(res => {
                return res
            })
        },
    },
    getters: {
        isLogin: state => {
            return state.isLogin
        },
        limitData: state => {
            return state.limitData
        },
        myLimitIds: state => {
            return state.myLimitIds
        },
    },
    mutations: {
        setIsLogin(state, isLogin) {
            state.isLogin = isLogin
        },
        setLimitData(state, data) {
            state.limitData = data
        },
        setLimitIds(state, data) {
            state.myLimitIds = data
        },
    },
})