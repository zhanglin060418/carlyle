<template>
    <modMain title="More" class="setting">
        <div class="main person-more" v-if="list && list.length">
            <div class="person-box" v-for="item in list">
                <!-- <img v-if="item.imgUrl" :src="imgBaseUrl + item.headImg" alt="" />
                <img v-else src="static/assets/image/xapp/icon_home_default2.png" alt="" /> -->
                <div class="info-main">
                    <img
                            v-if="item.userAvatar"
                            class="dw"
                            :src="imgBaseUrl + item.userAvatar"
                            alt=""
                    />
                    <img v-else class="dw" src="static/assets/image/default-aa.png" alt="" />
                </div>
                <div class="info-box">
                    <!--          <p class="name ">ID:{{ item.uid | strAbb }}</p>
                              <p class="profit">${{ $utils.getkStr(item.totalProfit) }}</p>
                              <p>{{ $t('dw.t8') }}</p>-->
                    <p class="name ">ID : {{ item.userName }}</p>
                    <p>{{ $t('btn.t20') }}</p>
                    <p class="profit">₦: {{ parseInt(item.recharge || 0)/100 }}</p>
                    <p>{{ $t('dw.t8') }}</p>
                    <p class="profit">₦: {{ parseInt(item.income || 0)/100 }}</p>
                </div>
            </div>
        </div>
        <no-data v-else></no-data>
        <loadding v-if="isLoading"></loadding>
    </modMain>
</template>
<script>
    import {mapActions, mapGetters} from "vuex";
    import PullList from "../../components/pullList.vue";
    export default {
        components: {PullList},
        data() {
            return {
                title: "More",
                list: [],
                sym: '',
                userId:'',
                isLoading:false,
                teamLevel: '',
            }
        },
        created() {
            let token = localStorage.getItem('token') || null
            if (token == null) {
                this.errDialog(this.$t('msg.loginFirst'))
                return this.$router.push("/login")
            }
            this.teamLevel = this.$route.query.data
            this.userId = localStorage.getItem('userId') || null
            this.sym = localStorage.getItem("localCurrency") || 'NGN'
            this.getList()
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
                incomeOverview: 'incomeOverview',
            }),
            async getList() {
                const user_id = this.userId
                this.isLoading = true
                let response = this.incomeOverview({
                    userId: user_id,
                    teamLevel: this.teamLevel
                }).then(response =>{
                    this.list = response.teamIncomeOverview
                    this.isLoading = false
                })
            }
        }
    }
</script>
