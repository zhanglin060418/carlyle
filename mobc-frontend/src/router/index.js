import Vue from 'vue'
import VueRouter from 'vue-router'

// 为了首屏加载快，所以首页不使用懒加载
import Layout from '../views/layout'
import i18n from '../i18n'

Vue.use(VueRouter)

const routes = [{
            path: '/',
            name: 'app',
            component: Layout,
            redirect: '/home',
            meta: {
                title: 'home',
            },
            children: [{
                    path: '/home',
                    name: 'home',
                    component: () =>
                        import ('@/views/home'),
                    meta: {
                        title: 'Carlyle',
                        keepAlive: false,
                        scrollToTop: true,
                    },
                },
                {
                    path: '/News',
                    name: 'news',
                    component: () =>
                        import ('@/views/news'),
                    meta: {
                        title: 'News',
                    },
                },
                {
                    path: '/fund',
                    name: 'fund',
                    component: () =>
                        import ('@/views/fund'),
                    meta: {
                        title: 'Fund',
                        keepAlive: false,
                    },
                },
                {
                    path: '/team',
                    name: 'team',
                    component: () =>
                        import ('@/views/team'),
                    meta: {
                        title: 'Team',
                    },
                },
                {
                    path: '/profit',
                    name: 'profit',
                    component: () =>
                        import ('@/views/profit'),
                    meta: {
                        title: 'User',
                        keepAlive: false,
                    },
                },
            ],
        },
        {
            path: '/newsdetail',
            name: 'newsdetail',
            component: () =>
                import ('@/views/news/detail'),
            meta: {
                title: 'newsdetail',
            },
        },
        {
            path: '/funddetail',
            name: 'funddetail',
            component: () =>
                import ('@/views/fund/detail'),
            meta: {
                title: 'Fund detail',
                assetstitle: 'Treasure detail',
            },
        },
        {
          path: '/product',
          name: 'purchaseDetail',
          component: () =>
            import ('@/views/fund/purchaseDetail'),
          meta: {
            title: 'Purchase detail',
            type: '1'
          },
        },
        {
            path: '/incomelist',
            name: 'incomelist',
            component: () =>
              import ('@/views/fund/incomelist'),
            meta: {
              title: 'Income Detail',
              type: '1'
            },
          },
        {
          path: '/confirmpurchase',
          name: 'confirmpurchase',
          component: () =>
            import ('@/views/fund/purchase'),
          meta: {
            title: 'Confirm Transfer In',
            withdrawtitle: 'Confirm Transfer Out'
          },
        },
        {
            path: '/team',
            name: 'team',
            component: () =>
                import ('@/views/team'),
            meta: {
                title: i18n.tc('sa.txt9'),
                keepAlive: false,
            },
        },
        {
            path: '/more',
            name: 'more',
            component: () =>
                import ('@/views/team/more'),
            meta: {
                title: i18n.tc('user.more'),
                keepAlive: true,
            },
        },
      {
          path: '/detail',
          name: 'detail',
          component: () =>
            import ('@/views/team/detail'),
          meta: {
            title: "detail",
          },
      },
        {
            path: '/login',
            name: 'login',
            component: () =>
                import ('@/views/single/register-login'),
            meta: {
                title: i18n.tc('user.login'),
                pathName: 'login',
            },
        },
        {
            path: '/register',
            name: 'register',
            component: () =>
                import ('@/views/single/register-login'),
            meta: {
                title: i18n.tc('user.register'),
                pathName: 'register',
            },
        },
        // {
        //   path: '/signin',
        //   name: 'signin',
        //   component: () => import('@/views/single/signin'),
        //   meta: {
        //     title: i18n.tc('user.register'),
        //     pathName: 'register',
        //   },
        // },
        {
            path: '/invite',
            name: 'invite',
            component: () =>
                import ('@/views/invite'),
            meta: {
                title: 'invite',
                keepAlive: true,
                scrollToTop: true,
            },
        },
        {
            path: '/signin-success',
            name: 'signin-success',
            component: () =>
                import ('@/views/single/signin-success'),
            meta: {
                title: i18n.tc('user.register'),
                pathName: 'register',
            },
        },
        {
            path: '/servicecenter',
            name: 'servicecenter',
            component: () =>
                import ('@/views/single/servicecenter'),
            meta: {
                title: '',
            },
        },
        {
            path: '/verification',
            name: 'verification',
            component: () =>
                import ('@/views/single/verification'),
            meta: {
                title: 'Verification',
            },
        },
        {
            path: '/updateLoginPwd',
            name: 'updateLoginPwd',
            component: () =>
                import ('@/views/single/updateLoginPwd'),
            meta: {
                title: 'Change password', //i18n.tc('user.updateLoginPwd')
            },
        },
        {
            path: '/areacode',
            name: 'areacode',
            component: () =>
                import ('@/views/areacode'),
            meta: {
                title: 'Personal data',
            },
        },
        {
            path: '/reset',
            name: 'reset',
            // component: () => import('@/views/single/reset'),
            component: () =>
                import ('@/views/single/updateLoginPwd'),
            meta: {
                title: 'Reset Password',
            },
        },
        {
          path: '/forgetLoginPwd',
          component: () =>
            import ('@/views/single/forgetLoginPwd'),
          meta: {
            title: 'Reset Password',
          },
        },
        {
          path: '/resetPayPwd',
          name: 'resetPayPwd',
          // component: () => import('@/views/single/reset'),
          component: () =>
            import ('@/views/single/updatePayPwd'),
          meta: {
            title: 'Reset Payment Password',
          },
        },
        {
          path: '/setSocial',
          name: 'setSocial',
          // component: () => import('@/views/single/reset'),
          component: () =>
            import ('@/views/single/setSocial'),
          meta: {
            title: 'Set Social',
          },
        },
        {
            path: '/energy',
            name: 'energy',
            component: () =>
                import ('@/views/energy'),
            meta: {
                title: '',
                keepAlive: false,
            },
        },

        {
            path: '/language',
            name: 'language',
            component: () =>
                import ('@/views/single/language'),
            meta: {
                title: 'Language',
            },
        },
        {
          path: '/currency',
          name: 'currency',
          component: () =>
            import ('@/views/single/currency'),
          meta: {
            title: 'Currency',
          },
        },
        {
            path: '/shareUpload2',
            name: 'misUpload',
            component: () =>
                import ('@/views/single/upload'),
            meta: {
                title: 'Sharing activities',
            },
        },
        {
            path: '/viewAd',
            name: 'viewAd',
            component: () =>
                import ('@/views/single/viewAd'),
            meta: {
                title: 'viewAd',
            },
        },
        {
            path: '/shareUpload',
            name: 'shareUpload',
            component: () =>
                import ('@/views/single/uploadShare'),
            meta: {
                title: 'Sharing activities',
            },
        },
        {
            path: '/uploadWhats',
            name: 'uploadWhats',
            component: () =>
                import ('@/views/single/uploadWhats'),
            meta: {
                title: 'Sharing activities',
            },
        },
        // {
        //     path: '/paymethods',
        //     name: 'paymethods',
        //     component: () =>
        //         import ('@/views/ingold/methods'),
        //     meta: {
        //         title: i18n.tc('deposit.chooseMethod'),
        //     },
        // },
        // {
        //     path: '/inMoney',
        //     name: 'inMoney',
        //     component: () =>
        //         import ('@/views/ingold/inMoney'),
        //     meta: {
        //         // title: '',
        //         toPath: 'InMoney',
        //         keepAlive: true,
        //     },
        // },

        // {
        //     path: '/withdrawal',
        //     name: 'withdrawal',
        //     component: () =>
        //         import ('@/views/ingold/withdrawal'),
        //     meta: {
        //         // title:'',
        //         toPath: 'withdrawal',
        //         // keepAlive: true,
        //     },
        // },
        {
            path: '/inOutList',
            name: '出入金记录',
            component: () =>
                import ('@/views/ingold/inOutList'),
            meta: {
                title: '',
            },
        },
        {
            path: '/inoutDetail',
            name: 'inoutDetail',
            component: () =>
                import ('@/views/ingold/details'),
            meta: {
                title: 'inoutDetail',
            },
        },

        {
            path: '/result-vue',
            name: 'result-vue',
            methods: ['get', 'post'],
            component: () =>
                import ('@/views/ingold/result-vue'),
            meta: {
                title: 'result-vue',
            },
        },

        {
            path: '/result-success',
            name: 'result-success',
            methods: ['get', 'post'],
            component: () =>
                import ('@/views/ingold/result-success'),
            meta: {
                title: 'result-success',
            },
        },
        {
            path: '/xieyi',
            name: 'xieyi',
            component: () =>
                import ('@/views/single/xieyi'),
            meta: {
                title: i18n.tc('user.serviceAgreement'),
            },
        },
        {
            path: '/cpf',
            name: 'cpf',
            component: () =>
                import ('@/views/ingold/cpf'),
            meta: {
                title: '',
            },
        },
        {
            path: '/form',
            name: 'form',
            component: () =>
                import ('@/views/ingold/form'),
            meta: {
                title: '',
            },
        },
        {
            path: '/sharePage',
            name: '落地页分享',
            component: () =>
                import ('@/views/share'),
            meta: {
                title: i18n.tc('sa.txt131'),
            },
        },
        // {
        //     path: '/share-a',
        //     name: 'sharea',
        //     component: () =>
        //         import ('@/views/share/share-a'),
        //     meta: {
        //         title: '',
        //     },
        // },
        // {
        //     path: '/share-b',
        //     name: 'shareb',
        //     component: () =>
        //         import ('@/views/share/share-b'),
        //     meta: {
        //         title: '',
        //     },
        // },
        // {
        //   path: '/share-c',
        //   name: 'shareac',
        //   component: () => import('@/views/share/share-c'),
        //   meta: {
        //     title: '',
        //   },
        // },
        // {
        //     path: '/share-d',
        //     name: 'shared',
        //     component: () =>
        //         import ('@/views/share/share-d'),
        //     meta: {
        //         title: '',
        //     },
        // },
        // {
        //   path: '/sharePage',
        //   name: '落地页分享',
        //   component: () => import('@/views/share'),
        //   meta: {
        //     title: i18n.tc('sa.txt131'),
        //   },
        // },
        {
            path: '/payDetail',
            name: '卡片信息支付',
            component: () =>
                import ('@/views/ingold/payDetail'),
            meta: {
                title: '支付',
            },
        },
        {
            path: '/payiframe',
            name: '跳转支付页面',
            component: () =>
                import ('@/views/ingold/payiframe'),
            meta: {
                title: '支付',
            },
        },
        {
            path: '/helpcenter',
            name: 'howToPlay',
            component: () =>
                import ('@/views/my/howToPlay'),
            meta: {
                title: 'How To Play',
            },
        },
        {
            path: '/bill',
            name: 'bill',
            component: () =>
                import ('@/views/bill'),
            meta: {
                title: '账单流水',
            },
        },
        {
            path: '/help',
            name: 'help',
            component: () =>
                import ('@/views/my/help'),
            meta: {
                title: '',
            },
        },
        {
            path: '/setting',
            name: '系统设置',
            component: () =>
                import ('@/views/setting'),
            meta: {
                title: i18n.tc('user.setting'),
            },
        },
    {
        path: '/check',
        name: '签到',
        component: () =>
            import ('@/views/check'),
        meta: {
            title: '',
        },
    },
        {
            path: '/setPayPwd',
            name: '设置支付密码',
            component: () =>
                import ('@/views/setting/setPayPwd'),
            meta: {
                title: '设置支付密码',
            },
        },
        {
            path: '/addcard',
            name: 'addcard',
            component: () =>
                import ('@/views/setting/addBCard'),
            meta: {
                title: 'addBCard',
            },
        },
        {
            path: '/choiseBank',
            name: 'choiseBank',
            component: () =>
                import ('@/views/setting/choiseBank'),
            meta: {
                title: 'Pilih Bank',
            },
        },
        {
            path: '/bankcard',
            name: 'bankCards',
            component: () =>
                import ('@/views/setting/bankCards'),
            meta: {
                title: 'bankCards',
            },
        },
        // 无权限页面
        {
            path: '/no-permission',
            name: 'NoPermission',
            component: () =>
                import ('@/views/error-page/no-permission'),
            meta: {
                title: '访问无权限',
            },
        },
        // 404 页面路由
        {
            path: '*',
            name: 'NotFound',
            component: () =>
                import ('@/views/error-page/404'),
            meta: {
                title: '页面走丢了',
            },
        },
        // {
        //     path: '/captcha',
        //     name: 'captcha',
        //     component: () =>
        //         import ('@/views/single/captcha'),
        //     meta: {
        //         title: '滑块验证',
        //     },
        // },
        // {
        //     path: '/testregister',
        //     name: 'testregister',
        //     component: () =>
        //         import ('@/views/single/testregister'),
        //     meta: {
        //         title: '测试注册',
        //     },
        // },
        {
            path: '/recharge-info',
            name: 'rechargeinfo',
            component: () =>
                import ('@/views/energy/recharge-info'),
            meta: {
                title: '',
            },
        },
        {
            path: '/helpfile',
            name: 'helpfile',
            component: () =>
                import ('@/views/single/helpFile'),
            meta: {
                title: 'helpfile',
            },
        },
    ]
    // 解决报错
const originalPush = VueRouter.prototype.push
const originalReplace = VueRouter.prototype.replace
    // push
VueRouter.prototype.push = function push(location, onResolve, onReject) {
        if (onResolve || onReject)
            return originalPush.call(this, location, onResolve, onReject)
        return originalPush.call(this, location).catch(err => err)
    }
    // replace
VueRouter.prototype.replace = function push(location, onResolve, onReject) {
    if (onResolve || onReject)
        return originalReplace.call(this, location, onResolve, onReject)
    return originalReplace.call(this, location).catch(err => err)
}

// const originalPush = VueRouter.prototype.push
// VueRouter.prototype.push = function push(location) {
//     return originalPush.call(this, location).catch(err => err)
// }
const env = process.env.NODE_ENV
const router = new VueRouter({
    // mode: env == 'production' || env == 'uat' ? 'history' : '',
    base: env == 'production' || env == 'uat' ? '/' : '',
    routes,
    // 页面滚动行为
    scrollBehavior(to, from, savedPosition) {
        if (savedPosition) {
            // savedPosition is only available for popstate navigations.
            return savedPosition
        }
        const position = {}
            // new navigation.
            // scroll to anchor by returning the selector
        if (to.hash) {
            position.selector = to.hash
        }
        // check if any matched route config has meta that requires scrolling to top
        if (to.matched.some(m => m.meta.scrollToTop)) {
            // cords will be used if no selector is provided,
            // or if the selector didn't match any element.
            position.x = 0
            position.y = 0
        }
        // if the returned position is falsy or an empty object,
        // will retain current scroll position.
        return position
    },
})
router.beforeEach((to, from, next) => {
    document.title = process.env.NODE_ENV_TITLE; // to.meta.title
        // 没有token时候,无法跳转其他页面.
    const Token = localStorage.getItem('token')
        // let loginUrls = ['/deposit', '/withdrawal']
        // if (loginUrls.includes(to.path) && !Token) {
        //   next('/login')
        // }
    next()
})
export default router
