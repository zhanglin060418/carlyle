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
        <div class="send_message" @click="gotoRelease">
            <!--<div @click="kfLogoClick">-->
           <div>
                <img src="static/assets/image/xapp/add.png" alt=""/>
            </div>
        </div>

    </div>
</template>

<script>
    export default {
        name: 'ReleaseInfo',
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
            gotoRelease(){
                this.$router.push('release')
            }
        }
    }
</script>
