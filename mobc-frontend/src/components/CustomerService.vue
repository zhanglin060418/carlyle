<template>
    <div>
       <!-- <van-popup
                v-model="showServicePopup"
                round
                position="bottom"
                safe-area-inset-bottom
                class="popup-announcement"
                :style="{ height:'80%',width:'96%', background: '#fff6ef',margin: '0% 2% 20% 2% ' }"
        >
            <div class="sell-buy popup-box energy-popup">
                <div class="head">
                    <div class="left">
                        <img class="noticeImg" src="static/favicon.png" alt=""/>
                        <div class="title">
                            Services Online
                        </div>
                    </div>
                    <div class="right">
                        <img class="close" src="static/assets/image/dw/close_circle@3x.png" alt=""
                             @click="closeService"/>
                    </div>
                </div>
                <div class="content ql-snow" style="font-size:medium">
                    <iframe  :src="serviceUrl" style="width: 100%;height: 100%"></iframe>
                </div>
            </div>
        </van-popup>-->
        <div class="kf">
            <!--<div @click="kfLogoClick">-->
           <div @click="toService('whats')">
                <img src="static/assets/image/xapp/kf.png" alt=""/>
            </div>
        </div>

    </div>
</template>

<script>
import mixinsSerivce from '@/mixins/service'
    export default {
        name: 'CustomerService',
        mixins: [mixinsSerivce],
        props: {},
        data() {
            return {
                serviceUrl: '',
                showServicePopup: false,
            };
        },
        created() {
        },
        mounted() {
        },
        methods: {
            kfLogoClick() {
                const domainHost = window.location.host;
                this.showServicePopup = true;
                if (domainHost.includes('www.exfdngn')){
                    this.serviceUrl = "https://tawk.to/chat/6489ce6594cf5d49dc5dac4c/1h2t4sft2";
                }else if(domainHost.includes('www.fdngn')){
                    this.serviceUrl = "https://tawk.to/chat/6489cc76cc26a871b02284ab/1h2t4dcjh";
                }else if(domainHost.includes('www.bainbc')){
                    this.serviceUrl = "https://tawk.to/chat/648a0b1ecc26a871b0229042/1h2tjmscd";
                }else if(domainHost.includes('www.ifund')){
                    this.serviceUrl = "https://tawk.to/chat/6489b225cc26a871b0227da3/1h2stvoqj";
                }else{
                    this.serviceUrl = "https://tawk.to/chat/648a0b1ecc26a871b0229042/1h2tjmscd";
                }
            },
            closeService() {
                this.showServicePopup = false;
            }

        }
    }
</script>
