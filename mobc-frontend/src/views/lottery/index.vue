<template>
    <modMain :title="$t('user.lottery')" class="setting ">
        <div class="lottery-page">
            <div class="gameBox">

                <div class="start">
                    <p v-if='!isDraws' class="count" @click="move"></p>
                    <p v-if='isDraws' class="toDraws" @click="shuax"></p>
                </div>
                <ul class="spoi">
                    <li v-for="(item, i) in lotteryList"
                        :key="i"
                        :class="['item' + (i + 1), { active: currentIndex == i }]">
                        <div class="desc">
                            <viewer :images="image" >
                                <img :src="imgBaseUrl + item.coverImages" alt="" />
                            </viewer>
                        </div>
                    </li>
                </ul>
            </div>
            <div class="my-btn-box"  style="margin-top: 30px; padding-left:20px;padding-right: 20px;" >
                <div class="my-btn tg">
                    <p style="font-size: 16px">Remaining:{{ number_of_draws}}</p>
                </div>

                <div class="my-btn whats"  @click="toDetail()">
                    <p style="font-size: 16px">Get records</p>
                </div>
            </div>

            <div class="desc" style="background: #eaeeff;">
                <div class="ql-snow" >
                    <p class="ql-editor" v-html="drawsText.descriptionEn" />
                </div>
            </div>
        </div>
        <van-popup
                v-model="showCash"
                round
                position="bottom"
                safe-area-inset-bottom
                class="popup-lucky"
                :style="{ height:'14.2rem',width:'76%',margin: '0% 12% 20% 12% ' }">
            <div class="sell-lucky" @click="closeCash">
                <div class="content-money">
                    <p class="money">{{currencyShape}} {{cashAounmt/100}}</p>
                </div>
            </div>
        </van-popup>
        <van-popup
                v-model="showDraws"
                round
                position="bottom"
                safe-area-inset-bottom
                class="popup-draws"
                :style="{ height:'9.3rem',width:'76%',margin: '0% 12% 45% 12% ' }">
            <div class="sell-draws" @click="closeDraws">
                <div class="content-money">
                    <p class="money">{{currencyShape}} {{drawsAmount/100}}</p>
                </div>
            </div>
        </van-popup>
        <loadding v-if="isLoading"></loadding>
    </modMain>
</template>
<script>
    import {  mapGetters,mapActions } from 'vuex'
    export default {
        data() {
            return {
                isLoading:false,
                userId:0,
                isDraws:false,
                showCash:false,
                drawsAmount:0,
                cashAounmt:0,
                showDraws: false,
                drawsText: '',
                number_of_draws: 0, //限制每天抽奖次数，接口返回
                prize_data: null, //中奖信息
                lotteryList: [],
                speed: 150, // 初始转动速度
                currentIndex: 0, // 当前转动到哪个位置，第一次起点位置0,对应页面item1位置
                count: 8, // 总共有多少个位置
                times: 0, // 转动跑格子次数,
                cycle: 40, // 转动基本次数：即至少需要转动多少次再进入抽奖环节
                timer: 0, // 转动定时器
                prize: 0, // 中奖位置，接口返回
            };
        },
        created() {
            let token = localStorage.getItem('token') || null
            if(token == null) {
                this.errDialog(this.$t('msg.loginFirst'))
                return this.$router.push("/login")
            }
            this.getList();
            this.getDrawsText()

            this.sym = localStorage.getItem('localeCurrency') || 'NGN'
            if(this.sym == 'NGN')
                this.currencyShape = '₦'
            else this.currencyShape = '¥'

        },
        watch: {
            '$route'() {
                    this.getList();
                    this.getDrawsText()
                }
            },
        computed: {
            ...mapGetters({
                userInfo: 'user/userInfo',
            }),
        },
        methods: {
            ...mapActions('user', {
                getLotteryList:'getLotteryList',
                addLotteryMove:'addLotteryMove',
                getTeamTreatmentText: 'getTreatmentText'
            }),

            async getList() {
                await this.getLotteryList().then(res =>{
                    this.lotteryList = res.lotteryList;
                    this.number_of_draws = res.data.drawsNumber;
                })
            },
            async getDrawsText() {
                await this.getTeamTreatmentText().then(res => {
                    if(res.data.length>0){
                        for (let item of res.data) {
                            if(item.remark.includes('Draws')) {
                                this.drawsText = item;
                            }

                        }
                    }
                })
            },

            toDetail() {
                this.$router.push({
                    path: '/drawsList',
                })
             },
            move() {
                if (this.number_of_draws == 0) {
                    this.errDialog("Lottery opportunities have been exhausted")
                } else if (this.times != 0) {
                    this.errDialog("please do not click repeatedly");
                } else {
                    this.isLoading = true;
                    const user_id = this.userInfo.user_id;
                    //执行抽奖
                    this.addLotteryMove({userId: user_id,}).then(res => {
                        if (res.code == 200) {
                            this.isLoading = false;
                            this.number_of_draws = this.number_of_draws - 1;
                            this.isDraws = true;
                            this.prize_data = res.data;
                            this.speed = 150;
                            this.prize =  res.data.category;
                            this.startRoll();
                        } else {
                            this.isLoading = false;
                            this.errDialog(res.msg)
                        }
                    })

                }
            },
            // 开始转动
            startRoll() {
                this.times += 1; // 转动次数
                this.oneRoll(); // 转动过程调用的每一次转动方法，这里是第一次调用初始化
                this.usePrize();
            },

            // 每一次转动
            oneRoll() {
                let index = this.currentIndex; // 当前转动到哪个位置
                const count = 8; // 总共有多少个位置
                index += 1;
                if (index > count - 1) {
                    index = 0;
                }
                this.currentIndex = index;
            },

            usePrize() {
                // 如果当前转动次数达到要求 && 目前转到的位置是中奖位置
                if (this.times > this.cycle && this.prize === this.currentIndex) {
                    clearTimeout(this.timer); // 清除转动定时器
                    this.times = 0; // 转动跑格子次数初始化为0，可以开始下次抽奖
                    if(this.prize_data!=null){
                        if(this.prize_data.type=='Voucher'){
                            this.drawsAmount = this.prize_data.amount;
                            this.showDraws = true;
                        }else if(this.prize_data.type=='Cash'){
                            this.cashAounmt = this.prize_data.amount;
                            this.showCash = true;
                        }
                    }
                } else {
                    if (this.times < this.cycle - 20) {
                        this.speed -= 4; // 加快转动速度
                    } else {
                        this.speed += 5; // 抽奖即将结束，放慢转动速度
                    }
                    this.timer = setTimeout(this.startRoll, this.speed); //开始转动
                }
            },

            closeCash(){
                this.showCash = false;
            },
            closeDraws(){
                this.showDraws = false;
            },
            shuax(){
                this.getList();
                this.isDraws=false;
            }
        },
    };
</script>
<style lang="less" scoped>
    @import '../../assets/css/lottery.less';
</style>

