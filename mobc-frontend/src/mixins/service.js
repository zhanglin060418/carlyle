import { mapActions, mapGetters } from 'vuex'
const myValidMixins = {
    data() {
        return {
            wathsrls: [],
            tgUrls: [],
            wathsrlIds: [],
        }
    },
    computed: {
        ...mapGetters({
            userInfo: 'user/userInfo',
        }),
    },
    created() {
        this.getLink()
    },
    methods: {
        ...mapActions({
            linkquery: 'user/linkquery',
            queryUserCallerLinks: 'user/queryUserCallerLinks',
        }),
        isLogin() {
            if (!this.userInfo || !this.userInfo.username) {
                this.$router.push('/login');
                return false
            }
            return true
        },
        getLink() {
            /*-----------------by 7 -------------
            this.linkquery().then(res => {
                let data = res.data || []
                if (data.length > 0) {
                    let tgs = data.filter(d => d.type == 'Telegram')
                    let waths = data.filter(d => d.type == 'WhatsApp')
                    this.tgUrls = tgs.map(d => d.link)
                    this.wathsrls = waths.map(d => d.link)
                    this.wathsrlIds = waths.map(d => d.id)
                }
            })*/
        },
        async toService(type) {
            let _url = 'https://t.me/+qjWLk9N-uQ5lOGE1';
           // let idx = 0
            if (type == 'tggroup') {
                if(this.userInfo.clerkTelegram) {
                    _url = 'https://t.me/' + this.userInfo.clerkTelegram;
                }else if(this.userInfo.telegram) {
                    _url = 'https://t.me/' + this.userInfo.telegram;
                }
                else _url = 'https://t.me/+qjWLk9N-uQ5lOGE1';
            } else if (type == 'tgSiaran') {
                _url = 'https://t.me/+qjWLk9N-uQ5lOGE1';
            } else if (type == 'whats') {
                // idx = parseInt(Math.random() * this.wathsrls.length, 10)
                if(this.userInfo.clerkWhatsApp) {
                    _url = 'https://wa.me/+' + this.userInfo.clerkWhatsApp;
                }else if(this.userInfo.whatsApp) {
                    _url = 'https://wa.me/+' + this.userInfo.whatsApp;
                }else{
                    _url = 'https://wa.me/+'+this.userInfo.username;
                }
                // _url = this.wathsrls[idx]
                    // 根据用户id缓存常用客服
                    // if (this.isLogin()) {
                    //   let link_Key = this.userInfo.id + '_WHATAPP'
                    //   _url = localStorage.getItem(link_Key) || null
                    //   if (!_url) {
                    //     _url = await this.getUserService(this.wathsrlIds[idx])
                    //   }
                    // }
            }
            if (type == 'tgtask') {
                if (this.isLogin()) {
                    _url = 'https://t.me/+qjWLk9N-uQ5lOGE1';
                } else {
                    return
                }
            }
            if (!_url){
                return
            }
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
        async getUserService(linkID) {
            if (!linkID) return ''
            return await this.queryUserCallerLinks({
                    callerLinksId: linkID,
                })
                .then(res => {
                    let url = res.data || ''
                    let link_Key = this.userInfo.id + '_WHATAPP'
                    localStorage.setItem(link_Key, url)
                    return url
                })
                .catch(e => {
                    console.log(e)
                    return ''
                })
        },
    },
}

export default myValidMixins
