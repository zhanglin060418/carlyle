import { mapActions, mapGetters, mapState } from 'vuex'
import { bus } from '@/utils/bus'
const mySellBuyMixins = {
    data() {
        return {
            showSuccess: false,
            inputPrice: null,
            sellData: {
                orderId: null,
                price: null,
            },
            limitItem: null, //到点抢购对象
            setTimmer: null,
        }
    },
    computed: {
        ...mapGetters({
            userInfo: 'user/userInfo',
            limitData: 'order/limitData',
            myLimitIds: 'order/myLimitIds',
        }),
    },
    mounted() {
        if (this.userInfo && this.userInfo.username) {
            this.setTimmer = setInterval(() => {
                !this.show && this.getLimitItem() //定时获取抢购对象
            }, 5000)
        } else {
            this.setTimmer && clearInterval(this.setTimmer)
        }
    },
    methods: {
        ...mapActions({
            sell: 'order/sell',
            buy: 'order/buy',
        }),
        sellBuyClick(item) {
            if (item.type == 0) {
                //出售
                let _price = Number(this.sellData.price)
                if (!_price) {
                    this.errDialog(this.$t('dw.t147'))
                    return
                }
                if (_price < item.guidePrice1 || _price > item.guidePrice2) {
                    this.errDialog(this.$t('dw.t148'))
                    return
                }
                this.sellData.orderId = item.id
                this.sell(this.sellData).then(res => {
                    if (res.code == 0) {
                        this.showSuccess = true
                        this.toRefreshMarket()
                    } else {
                        this.errDialog(res.msg)
                    }
                })
            } else if (item.type == 1) {
                //购买
                this.buy({ orderId: item.id }).then(res => {
                    if (res.code == 0) {
                        this.showSell = false
                        this.toRefreshMarket()
                        bus.$emit('openDialog', 1)
                    } else if (res.code == 3002) {
                        this.showSell = false
                        this.toRefreshMarket()
                        bus.$emit('openDialog')
                    } else {
                        this.errDialog(res.msg)
                    }
                })
            }
        },
        toRefreshMarket() {
            bus.$emit('refreshList')
        },
        toGoMarket() {
            this.showSell = false
            if (this.$route.path != '/market') this.$router.push('/market')
        },
        getLimitItem() {
            if (
                (this.$route.path == '/login' ||
                    this.$route.path == '/register' ||
                    this.$route.path == '/helpfile') &&
                !sessionStorage.getItem('_t_notic_num')
            ) {
                return
            }
            console.log('limitData=', this.limitData)
            console.log('myLimitIds=', this.myLimitIds)
            if (this.limitData && this.limitData.length > 0) {
                //获取缓存的抢购对象时间
                let data = this.limitData
                if (data.length == 0) return
                let cachCurrentTime = Number(localStorage.getItem('limit_time') || 0)
                if (cachCurrentTime > 0) {
                    // 如果跨天，清楚缓存
                    let isSameDay =
                        new Date(cachCurrentTime).toDateString() ===
                        new Date().toDateString()
                    if (!isSameDay) {
                        localStorage.removeItem('limit_time')
                        cachCurrentTime = 0
                    }
                }
                // 先过滤已经抢购过的商品
                if (this.myLimitIds && this.myLimitIds.length > 0) {
                    data = data.filter(d => !this.myLimitIds.includes(String(d.id)))
                }
                let getTime = this.$utils.getTime
                let currentTime = getTime()
                let type = 0
                if (currentTime >= getTime('8:00') && currentTime < getTime('12:00')) {
                    type = 0
                } else if (
                    currentTime >= getTime('12:00') &&
                    currentTime < getTime('17:00')
                ) {
                    type = 1
                } else if (
                    currentTime >= getTime('17:00') &&
                    currentTime <= getTime('21:00')
                ) {
                    type = 2
                }
                let start = type == 0 ? '8:00' : type == 1 ? '12:00' : '17:00'
                let end = type == 0 ? '12:00' : type == 1 ? '17:00' : '21:00'
                start = getTime(start)
                end = getTime(end)
                let _list = data.filter(d => {
                    let s = getTime(d.startTime)
                    return s >= start && s < end && d.enable
                })
                if (_list.length) {
                    // 如果有缓存，则排除当前缓存的对象
                    // 过滤
                    _list = _list.filter(d => {
                        let s = getTime(d.startTime)
                        let e = getTime(d.endTime)
                        console.log('s=', s)
                        console.log('cachCurrentTime=', cachCurrentTime)
                        let isok = currentTime > s && currentTime < e
                        if (cachCurrentTime > 0) {
                            return isok && s > cachCurrentTime
                        }
                        return isok
                    })
                    console.log('抢购列表', _list)
                    let item = _list.length ? _list[0] : null
                    console.log('抢购对象', item)
                    if (item) {
                        this.limitItem = item
                        this.$set(this.limitItem, 'type', 1)
                        console.log('缓存时间', getTime(this.limitItem.startTime))
                        localStorage.setItem(
                            'limit_time',
                            getTime(this.limitItem.startTime)
                        )
                        this.show = true
                    }
                }
            }
        },
        //预计收入
        getRevenue(item) {
            let { amount, returnRate, expireTime } = item
            let income = this.$utils.accMul(Number(amount), returnRate) // 每日收入
            let hIncom = income / 24 //每小时收入
            let dTime = new Date()
            let expTime = new Date(expireTime)
            let dObj = this.$utils.diffTime(dTime, expTime)
            let hours = dObj.days * 24 + dObj.hours // 截至到期 多少个小时
            return this.$utils.getkStr(
                this.$utils.accAdd(amount, this.$utils.accMul(hIncom, hours, 0))
            )
        },
    },
}

export default mySellBuyMixins