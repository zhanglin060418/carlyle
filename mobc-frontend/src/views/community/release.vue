<template>
    <modMain :title="$route.meta.title" class="product">
        <van-form>
            <div class="info-content">
                <div class="text-main">
                    <div class="text-box">
                        <textarea v-model="content" id="inputMoney" class="ql-snow ql-editor"  rows="3" maxlength="200"  placeholder="thoughts at this moment ..." />
                        <van-divider  :style="{padding: '10px',margin:'0px'}"/>
                        <div class="upload-btn">
                            <van-uploader  accept="image/*" :max-count="1" :before-read="beforeUpload">
                                    <template >
                                        <img :src="options.img" style="max-width: 320px;height: auto; object-fit: cover;" alt="" />
                                    </template>
                            </van-uploader>
                        </div>
                    </div>
                </div>
            </div>
        </van-form>
        <div class="inver-btn" @click="afterRead">Release</div>
        <loadding v-if="isLoading"></loadding>
    </modMain>
</template>

<script>
    import { mapActions, mapGetters,mapMutations } from "vuex";
    import client from '@/utils/http'
    export default {
        data() {
            return {
                sym: '',
                userId: '',
                content: '',
                isLoading: false,
                tempFile:'',
                options: {
                    img: 'static/assets/image/addimg.png', //裁剪图片的地址
                    autoCrop: true, // 是否默认生成截图框
                    autoCropWidth: 200, // 默认生成截图框宽度
                    autoCropHeight: 200, // 默认生成截图框高度
                    fixedBox: true,// 固定截图框大小 不允许改变
                    outputType:"png"
                },
                previews: {},
            }
        },
        created() {
            let token = localStorage.getItem('token') || null
            if (token == null) {
                this.errDialog(this.$t('msg.loginFirst'))
                return this.$router.push("/login")
            }
            this.userId = localStorage.getItem('userId') || null
            this.guangbiao()
            this.sym = localStorage.getItem("localCurrency") || 'NGN'
            if (this.sym == 'NGN')
                this.currencyShape = '₦'
            else this.currencyShape = '¥'

        },
        computed: {
            ...mapGetters({
                userInfo: 'user/userInfo',
            }),
        },
        methods: {
            ...mapActions('user', {
                insertMessage: 'insertMessage',
            }),
            ...mapMutations('user', {
                updateUser: 'UPDATE_USER',
            }),
            async guangbiao(){
                setTimeout(function (){
                    let onFocus = document.querySelector('#inputMoney');
                    onFocus.focus()
                },1000)
            },

            // 上传预处理
            beforeUpload(file) {
                let fileTypes = ['image/jpeg', 'image/pjpeg', 'image/png']
                if (!fileTypes.includes(file.type)) {
                    this.errDialog(this.$t('sys.imgErr'))
                    return
                }
                if (file.size.toFixed(2) > 1024 * 1024 * 6) {
                    this.errDialog(this.$t('sys.uploadSize'))
                    return
                }
                this.tempFile = file;
                const reader = new FileReader();
                reader.readAsDataURL(file);
                reader.onload = () => {
                    this.options.img = reader.result;
                }
            },

            async afterRead() {
                let file = this.tempFile;
                if(this.content.trim()==''&&file==''){
                    return;
                }else if(file==''&&this.content.trim()!=''){
                    const formData = {
                        userId:this.userId,
                        content:this.content
                    }
                    this.insertMessage(formData).then(res =>{
                        if(res.code == 200) {
                            this.errDialog(this.$t('msg.messageSuccess'))
                            this.$router.push({
                                path: '/community',
                            })
                        }
                    })
                }else{
                    this.$toast.loading({
                        message: this.$t('sys.loadding'),
                        duration: 0,
                        forbidClick: true,
                    })
                    console.log('上传file。。。。。。。。。。', file)
                    let fileTypes = ['image/jpeg', 'image/pjpeg', 'image/png']
                    if (!fileTypes.includes(file.type)) {
                        this.errDialog(this.$t('sys.imgErr'))
                        return
                    }
                    if (file.size.toFixed(2) > 1024 * 1024 * 6) {
                        this.errDialog(this.$t('sys.uploadSize'))
                        return
                    }
                    console.log('开始上传。。。。。。。。。。', file)
                    client.postFormData('/system/user/profile/community', {
                        communityfile: file,
                        })
                        .then(res => {
                            console.log('上传返回信息。。。。。。。。。。', res)
                            if(res.code == 200) {
                                setTimeout(() => {
                                    const formData = {
                                        userId:this.userId,
                                        content:this.content,
                                        coverImage: res.imgUrl,
                                    }
                                    this.insertMessage(formData).then(res =>{
                                        if(res.code == 200) {
                                            this.errDialog(this.$t('msg.messageSuccess'))
                                            this.$router.push({
                                                path: '/community',
                                            })
                                        }
                                    })

                                }, 1000);
                            }
                            else this.errDialog(this.$t('msg.failedOperation'))

                        })
                }
            },
        }
    }
</script>
