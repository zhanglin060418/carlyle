export default client => ({
    namespaced: true,
    state: {
        backCardList: [],
    },
    actions: {
        // GET /bank_card/query/user
        getBankCard({ commit }, arg) {
            return client.get('/system/card/getUserCard', arg).then(res => {
                commit('SET_BANK_LIST', res.data)
                return res
            })
        },
        // /bank_card/bind
        // 绑卡
        addBankCard({ commit }, arg) {
            return client.post('/system/card', arg).then(res => {
                // commit('ADD_BANK_Card_LIST', res.data)
                return res
            })
        },
        // /bindcard/update
        updateBankCard({ commit }, arg) {
            return client.put('/system/card', arg)
        },
        deleteBankCard({ commit }, arg) {
            return client.delete('/system/card/deleteBankCard', arg)
        },
        bankList({ commit }, arg) {
            return client.get('/system/bank/getBankList', arg).then(res => {
                commit('SET_BANK_LIST', res.data)
                return res
            })
        },
    },

    getters: {
        isBindBank(state) {
            return state.backCardList ? state.backCardList.length > 0 : false
        },
        getBankList(state) {
            return state.backCardList
        },
    },
    mutations: {
        SET_BANK_LIST(state, data) {
            state.backCardList = data
        },
        // ADD_BANK_LIST(state, data) {
        //     if (data) {
        //         state.backCardList = [data, ...state.backCardList]
        //     }
        // },
    },
})