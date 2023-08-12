<template>
  <modMain :title="$t('payDetail.text16')" class="setting">
    <div class="bankcard-v">
      <template v-if="bankcards.length == 0">
        <div class="content">
           <img
            @click="$router.push('/addcard')"
            src="static/assets/image/icon/icon-addbank.png"
            alt=""
          />
          <div class="add-bank-card" @click="$router.push('/addcard')">
            <!-- <img src="static/assets/image/dw/add-bank.png" alt="" /> -->
            <p class="title">
              <!-- <img src="static/assets/image/dw/jia.png" alt="" /> -->
              {{ $t('payDetail.text17') }}
            </p>
            <p class="tips">{{ $t('payDetail.text43') }}</p>
          </div>
        </div>
      </template>
      <template v-else>
        <div class="content top-20">
          <div class="bank-list" v-for="item in bankcards">
            <div class="card">
              <p class="bank-name">{{ item.bankName }}</p>
              <van-icon name="edit edit-i" @click="updateBank(item)" />
<!--              <van-icon name="delete-o delete" @click="deleteBank(item)" />-->
              <p class="card-value">
                {{ item.cardNo.toString().substring(0, 4) }} **** ***
                {{
                  item.cardNo.toString().substring(
                      item.cardNo.toString().length - 4,
                      item.cardNo.toString().length
                  )
                }}
              </p>
            </div>
          </div>
          <div class="add-btn-box" @click="$router.push('/addcard')">
            <!-- <img src="static/assets/image/dw/jia.png" alt="" /> -->
            {{ $t('payDetail.text17') }}
            <!-- <div class="left">
              <img src="static/assets/image/icon/icon-addbank.png" alt="" />
              <span>{{ $t('payDetail.text17') }}</span>
            </div>
            <div class="right">
              <img src="static/assets/image/icon/icon_me_more@2x.png" alt="" />
            </div> -->
          </div>
        </div>
      </template>
    </div>
    <loadding v-if="showLoadding" />
  </modMain>
</template>
<script>
import {mapActions, mapGetters} from 'vuex'
export default {
  data() {
    return {
      showLoadding: false,
      bankcards: [],
    }
  },
  computed: {
    ...mapGetters({
      userInfo: 'user/userInfo',
      isLogin: 'user/isLogin',
    }),
  },
  created() {
    this.showLoadding = true
    this._getBankCard()
  },
  methods: {
    ...mapActions({
      getBankCard: 'bankCard/getBankCard',
      deleteBankCard: 'bankCard/deleteBankCard',
    }),
    updateBank(item) {
      localStorage.setItem(
          'bankInfo',
          JSON.stringify({
            name: item.name,
            mobile: item.mobile,
            bankName: item.bank.name,
            cardNo: item.cardNo,
          })
      )
      this.$router.push({
        path: '/addcard',
        query: { card: JSON.stringify(item.id) },
      })
    },
    _getBankCard() {
      const user_id = this.userInfo.user_id
      this.getBankCard({userId: user_id}).then(res => {
        if (res.code == 200) {
          this.bankcards = res.data
        }
        this.showLoadding = false
      })
    },
    deleteBank(item) {
      this.$dialog
        .confirm({
          title: '',
          message: this.$t('msg.deleteCard'),
          cancelButtonText: this.$t('sys.cancel'),
          confirmButtonText: this.$t('sys.confirm'),
        })
        .then(() => {
          this.deleteBankCard({cardId: item.id}).then(res => {
            this._getBankCard()
          })
        })
        .catch(() => {
          // on cancel
        })
    },
  },
}
</script>
