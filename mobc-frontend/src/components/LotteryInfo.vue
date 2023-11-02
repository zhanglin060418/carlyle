<template>
    <div>
        <div class="send_message" @click="gotoLottery">
           <div>
                <img src="static/assets/image/wind/icon/icon-lottery.png" alt=""/>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: 'LotteryInfo',
        data() {
            return {
            };
        },
        created() {
        },
        mounted() {
        },
        methods: {
            gotoLottery(){
                this.$router.push('lottery')
            }
        }
    }
</script>
