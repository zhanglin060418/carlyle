// import { mapGetters } from 'vuex'

import { mapActions, mapGetters, mapMutations } from 'vuex'
import comments from './components/comments'

export default {
    components: {
        comments,
    },
    data() {
        return {
            isErrorPwd: false,
            isErrorCode: false,
            isAgree: true,
            isShowLoadding: false,
            form: {
                username: '',
                password: '',
                password2: '',
                registerType: 'PHONE',
                authorityType: 'CLIENT',
                platform: 'H5',
                areaCode: '91',
                verifyCode: '',
                source: 1,
                symbolCode: null, //邀请码
            },
            code: '', //短信验证码
            // imgCode: '',
            wathsrls: [],
            isErrorEmail: null,
        }
    },
    computed: {
        ...mapGetters({
            switchType: 'getSwitchType',
        }),
        areaItem() {
            return this.$store.getters.getAreaCode()
        },
    },
    methods: {
        ...mapActions('user', {
            register: 'register',
            login: 'login',
            verifyUsername: 'verifyUsername',
            codeVerify: 'codeVerify',
        }),
        getImgCode() {
          this.$refs.imgCode.getCodeImg()
        },
        toRegIn() {
            this.$router.push('/register')
        },
        async toSignIn() {
            // if (this.imgCode == '') {
            //   this.errDialog('Kode verifikasi grafis tidak boleh kosong')
            //   return
            // }
            this.form.areaCode = this.areaItem.code
            this.form.registerCountryCode = this.areaItem.area
            if (this.switchType == 'PHONE') {
                let fz = this.form.username.substring(0, 1)
                if (fz == 0) {
                    this.form.username = this.form.username.substr(1)
                }
                if (
                    this.form.username.length < 5 ||
                    !/^[0-9]*$/.test(this.form.username)
                ) {
                    this.errDialog(this.$t('msg.mobile'))
                    return
                }
            }
            if (this.switchType == 'EMAIL') {
                this.isErrorEmail = !this.onFailed({
                    name: 'email',
                    value: this.form.username,
                })
                if (this.isErrorEmail) return
            }
            if (this.form.password != this.form.password2) {
                this.errDialog(this.$t('msg.repwd'))
                return
            }
            if (this.code == '') {
                this.errDialog(this.$t('msg.verifycode'))
                this.isErrorCode = true
                return
            }
            // this.isErrorPwd = !this.onFailed({
            //   name: 'password',
            //   value: this.form.password,
            // })
            // if (this.isErrorPwd) return

            //验证是否有注册
            let res = await this.verifyUsername({
                username: this.form.username,
            })
            let isok = false
            if (res.code == 0) {
                if (res.data) {
                    isok = false
                    this.errDialog(this.$t('msg.login'))
                    return
                } else {
                    isok = true
                }
            } else {
                isok = false
                this.errDialog(res.msg || this.$t('msg.systemerr'))
                return
            }
            let isCodeVerify = false
            this.form.registerType = this.switchType
            let params = {
                mobilePhone: this.form.username,
                code: this.code,
            }
            let rs = await this.codeVerify(params)
            if (rs && rs.code == 0) {
                isCodeVerify = true
            } else {
                isCodeVerify = false
                this.errDialog(rs.msg)
            }
            if (isok && isCodeVerify) {
                await this._register()
            }
        },
        _register() {
            this.isShowLoadding = true
            this.form.verifyCode = this.code
                // this.form.sessionId = sessionStorage.getItem('sessionId') || ''
                // this.form.verifyImageCode = this.imgCode
            this.form.symbolCode = localStorage.getItem('u_symbolCode') || null
            this.form.verifyCode = this.code
            this.register(this.form).then(res => {
                this.isShowLoadding = false
                if (res.code == 0) {
                    localStorage.setItem('logo_username', this.form.username)
                    this.showDialog(this.$t('dialog.regSuccess'))
                    this.onLogin()
                } else {
                    this.errDialog(res.msg)
                }
            })
        },
        onLogin() {
            this.$router.push('/signin-success')
        },
    },
}