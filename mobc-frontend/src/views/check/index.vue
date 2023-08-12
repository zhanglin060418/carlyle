<template>
    <modMain :title="$t('user.checkin')" class="setting ">
    <div class="test-page">
        <div class="top">
            <div class="button" v-if="!isSigned" @click="toSignIn"><i class="calendar-icon"></i>
                <div class="toSign">
                    <span>Sign in</span>
                </div>
            </div>
            <div class="button" v-if="isSigned"><i class="calendar-icon"></i><div>Signed in</div></div>
            <p v-show="showSignInfo">You have signed in for {{day}} consecutive days!</p>
        </div>
        <div class="content">
            <ul class="month bottom-line">
                <li class="arrow" @click="pickPre(currentYear,currentMonth)"><van-icon name="arrow-left"/> last month</li>
                <li class="year-month"><span>{{ currentYear }}-{{ currentMonth }}</span></li>
                <li class="arrow" @click="pickNext(currentYear,currentMonth)">next month <van-icon name="arrow"/></li>
            </ul>
            <ul class="weekdays">
                <li>Sun</li>
                <li>Mon</li>
                <li>Tue</li>
                <li>Wed</li>
                <li>Thu</li>
                <li>Fri</li>
                <li>Sat</li>
            </ul>
            <ul class="days bottom-line">
                <li  v-for="day in days">
                    <!--本月已签到日期-->
                    <span v-if="day.isSign && day.day.getMonth()+1 === currentMonth" class="cli"><img :src="doneCheck">{{ day.day.getDate() }}</span>
                    <!--本月未签到日期-->
                    <span v-if="!day.isSign && day.day.getMonth()+1 === currentMonth" class="cli">{{ day.day.getDate() }}</span>
                </li>
            </ul>
        </div>
        <div class="role">
            <div class="role-title">Check in rules</div>
            <div class="role-content"><p>You can get <span style="color: #0a364a">{{dailySignInAmount}}</span>  - <span style="color: #0a364a">{{dailySignInAmountMax}}</span>  naira every day for sign in, invite your friends to sign in</p></div>
        </div>
    </div>
     <loadding v-if="isLoading"></loadding>
    </modMain>
</template>
<script>
    import {  mapGetters,mapActions } from 'vuex'
    import { Cell, CellGroup, Field, Popup, Button, Icon } from 'vant';
    export default {
        components: {
            [Cell.name]: Cell,
            [CellGroup.name]: CellGroup,
            [Field.name]: Field,
            [Popup.name]: Popup,
            [Button.name]: Button,
            [Icon.name]: Icon
        },
        data() {
            return {
                isLoading:false,
                showSignInfo:false,
                currentDay: 1, // 当前天
                currentMonth: 1, // 当前月
                currentYear: 2023,
                currentWeek: 1, // 一号所在的星期
                days: [], // 当月所有天数
                userId: 0,
                signedDate: [], // 当月签到日期
                num: 0,
                day: 0,
                isSigned: false,
                dailySignInAmount:0,
                dailySignInAmountMax:0,
                doneCheck:require('@/assets/image/calendar-sign-icon.png'),
            };
        },
        created() {
            this.getDailySignInAmt();
            this.userId = this.$route.query.data;
            var today = new Date();
            this.currentYear = today.getFullYear();
            this.currentMonth = today.getMonth() + 1;
            this.currentDay = today.getDate();
            this.getMaxSignByUser(this.currentYear,this.currentMonth,this.currentDay );
            this.getSign(this.currentYear,this.currentMonth );
        },
        computed: {
            ...mapGetters({
                userInfo: 'user/userInfo',
            }),
        },
        methods: {
            ...mapActions('user', {
                getSignRecordListByUser:'getSignRecordListByUser',
                getMaxSignRecordByUser:'getMaxSignRecordByUser',
                getDailySignInAmount:'getDailySignInAmount',
                addSignRecord:'addSignRecord',
            }),
            getDailySignInAmt(){
                this.getDailySignInAmount().then(response => {
                    this.dailySignInAmount = response.dailySignInAmount;
                    this.dailySignInAmountMax = response.dailySignInAmountMax;
                })
            },
            getMaxSignByUser(currYear,currMonth,currDay){
                this.isLoading = true;
                console.log(currDay)
                this.getMaxSignRecordByUser({userId: this.userId}).then(response => {
                    if(response.code==200){
                        if(response.data!= null){
                            console.log(response.data.signDay+""+currDay)
                          if(response.data.signYear===currYear&&
                              response.data.signMonth===currMonth&&
                              response.data.signDay===currDay){
                              this.day = response.data.continuousDay;
                              this.isSigned = true;
                              this.showSignInfo = true;
                          }
                        }
                    }
                    this.isLoading = false;
                })
            },
            async getSign(currYear,currMonth) {
                this.isLoading = true;
                let response =  await this.getSignRecordListByUser({
                    userId: this.userId,
                    signYear:currYear,
                    signMonth:currMonth
                })
                if(response.data.length>0){
                    for (let item of response.data) {
                        this.signedDate.push(item.signDay);
                    }
                }
                this.initData(this.formatDate(currYear, currMonth, 1));
                this.isLoading = false;
            },
            initData(curDate) {
                let date;
                if (curDate) { // 切换日期
                    date = new Date(curDate);
                } else {
                    var now = new Date();
                    var d = new Date(this.formatDate(now.getFullYear(), now.getMonth() + 1, 1));
                    d.setDate(35);
                    date = new Date(this.formatDate(d.getFullYear(), d.getMonth(), 1));
                }
                this.currentDay = date.getDate(); // 今日日期 几号
                this.currentYear = date.getFullYear(); // 当前年份
                this.currentMonth = date.getMonth() + 1; // 当前月份
                this.currentWeek = date.getDay(); // 0,1...6 星期
                const str = this.formatDate(this.currentYear, this.currentMonth, this.currentDay); // 2020-01-01
                this.days.length = 0;
                // 如果今天是周日，放在第一行第7个位置，前面6个 这里默认显示一周，如果需要显示一个月，则第二个循环为 i<= 35- this.currentWeek
                for (var i = this.currentWeek; i > 0; i--) {
                    const d = new Date(str);
                    d.setDate(d.getDate() - i);
                    var dayobject = {};
                    dayobject.day = d;
                    this.days.push(dayobject); // 将日期放入data 中的days数组 供页面渲染使用
                }
                // 其他周 // 设置天数为35天,周日设置在第一位，循环从36开始
                this.num = 0;
                for (var j = 0; j <= 36 - this.currentWeek; j++) {
                    const d = new Date(str);
                    d.setDate(d.getDate() + j);
                    const dddd = d.getDate();
                    if (dddd === 1) {this.num++;}
                    if (this.num === 2) {return;}
                    const dayobject = { day: d, isSign: this.isVerDate(dddd) };
                    this.days.push(dayobject);
                }
            },
             // 判断该日期是否有签到
            isVerDate(val) {
                return this.signedDate.includes(val);
            },
            //上一月
            pickPre(year, month) {
                const d = new Date(this.formatDate(year, month, 1));
                d.setDate(0);
                this.signedDate = [];
                this.getSign(d.getFullYear(),d.getMonth() + 1)
                this.initData(this.formatDate(d.getFullYear(), d.getMonth() + 1, 1));
            },
            //下一月
            pickNext(year, month) {
                const d = new Date(this.formatDate(year, month, 1));
                d.setDate(35);
                this.signedDate = [];
                this.getSign(d.getFullYear(),d.getMonth() + 1)
                this.initData(this.formatDate(d.getFullYear(), d.getMonth() + 1, 1));
            },
            formatDate(year, month, day) {
                month < 10 && (month = '0' + month);
                day < 10 && (day = '0' + day);
                const data = year + '-' + month + '-' + day;
                return data;
            },
            /**
             * 点击签到
             * @param index
             */
            toSignIn() {
                const signCurrDate = new Date();
                const curYear = signCurrDate.getFullYear();
                const curMonth = signCurrDate.getMonth() + 1;
                const curDay = signCurrDate.getDate();
                this.isLoading = true;
                this.addSignRecord({
                    userId: this.userId,
                    signYear:curYear,
                    signMonth:curMonth,
                    signDay:curDay,
                }).then(res => {
                    if (res.code == 200) {
                        this.getMaxSignByUser(curYear,curMonth,curDay);
                        this.getSign(curYear,curMonth);
                        this.isLoading = false;
                        this.showDialog('Sign in successfully');
                    } else {
                        this.isLoading = false;
                        this.errDialog(res.msg)
                    }
                })
            }
        }
    };
</script>
<style lang="less" scoped>
    @import '../../assets/css/checkin.less';
</style>

