<template>
  <modMain :title="$route.query.card ? $t('payDetail.text28') : $t('payDetail.text26')" class="setting">
    <div class="addcard-v">
      <div class="form-group">
        <van-form>
          <div class="h20"></div>
          <van-field v-model.trim="form.mobile" readonly :placeholder="$t('user.enterHere')" :label="$t('user.mobile')">
          </van-field>
          <p class="desc cpf-txt">
            {{ $t('payDetail.text47') }}
          </p>

          <div class="h20"></div>
          <van-field v-model.trim="form.name" :placeholder="$t('user.enterHere')" :label="$t('payDetail.text2')">
          </van-field>
          <p class="desc cpf-txt">
            {{ $t('payDetail.text44') }}
          </p>

          <div class="h20"></div>
          <van-field v-model.trim="form.bankName" readonly is-link :placeholder="$t('user.pleaseChoose')" @click="focusCode" :label="$t('payDetail.text27')">
          </van-field>
          <p class="desc cpf-txt">
            {{ $t('payDetail.text45') }}
          </p>

          <div class="h20"></div>
          <van-field v-model.trim="form.cardNo" type="number" :placeholder="$t('user.enterHere')" :label="$t('payDetail.text25')" >
          </van-field>
          <p class="desc cpf-txt red">* {{ $t('payDetail.text46') }}</p>
          <p class="bagbg-txt">{{ $t('payDetail.text51') }}</p>

          <div class="btn-box">
            <base-btn :btnTitle="$t('sys.submit')" :disabled="false" @btnClick="btnClick"></base-btn>
          </div>
        </van-form>
      </div>
    </div>

  </modMain>
</template>
<script>
import { mapGetters, mapMutations, mapActions } from 'vuex'

export default {
  data() {
    return {
      isShowBirthday: false,
      minDate: new Date(1900, 0, 1),
      maxDate: new Date(2099, 10, 1),
      currentDate: new Date(),
      form: {
        id: null,
        name: '',
        // surname: '',
        // cpfCode: '',
        mobile: '',
        bankName: '',
        bankCode: '',
        cardNo: ''
        // receivingCode: '', // IFSC code
        // birthday: '',
      },
      isSaved: false,
    }
  },
  computed: {
    ...mapGetters('user', {
      userInfo: 'userInfo',
    }),
  },
  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (from.path == '/choiseBank') {
        let obj = JSON.parse(localStorage.getItem('bankInfo'))
        vm.form.name = obj.name
        vm.form.mobile = obj.mobile
        vm.form.bankName = localStorage.getItem("bankName")
        vm.form.bankCode = localStorage.getItem("bankCode")
      }
    })
  },
  beforeRouteLeave(to, from, next) {
    if (to.path == '/choiseBank') {
      localStorage.setItem(
        'bankInfo',
        JSON.stringify({
          name: this.form.name,
          mobile: this.form.mobile,
          cardNo: this.form.cardNo,
        })
      )
    } else {
      localStorage.removeItem('bankInfo')
    }
    next()
  },
  created() {
    if (this.$route.query.card) {
      let cardId = JSON.parse(this.$route.query.card)
      this.form.id = cardId
    }
    const user_name = this.userInfo.username
    console.log()
    this.form.mobile = user_name
    let bankInfo = localStorage.getItem('bankInfo') || null
    if (bankInfo) {
      let item = JSON.parse(bankInfo)
      this.form.name = item.name
      this.form.cardNo = item.cardNo
      this.form.bankName = item.bankName
      this.form.bankCode = item.bankCode
    }
  },
  methods: {
    ...mapActions({
      addBankcard: 'bankCard/addBankCard',
      getBankCard: 'bankCard/getBankCard',
      updateBankCard: 'bankCard/updateBankCard',
    }),

    focusCode() {
      // let addCard = JSON.stringify(this.form)
      // localStorage.setItem('addCard', addCard)
      this.$router.push('/choiseBank')
    },
    //-------- by 7------
    // hideBirthdayPopup() {
    //   this.isShowBirthday = false
    // },
    // confirmDate(value) {
    //   this.form.birthday = moment(value).format('MM-DD-yyyy')
    //   this.hideBirthdayPopup()
    // },
    btnClick() {
      if (this.isSaved) return
      this.isSaved = true
      if (this.form.name == '') {
        this.errDialog('Name cannot be empty.')
        this.isSaved = false
        return
      }
      if(this.form.name.length>50){
        this.errDialog('The name length cannot be greater than 50 characters')
        this.isSaved = false
        return
      }
      if (this.form.bankName == '') {
        this.errDialog('Bank name cannot be empty.')
        this.isSaved = false
        return
      }
      if (this.form.cardNo == '') {
        this.errDialog('Bank account cannot be empty.')
        this.isSaved = false
        return
      }
      if(this.form.cardNo.length>12||10>this.form.cardNo.length){
        this.errDialog('Bank account length must be between 10 and 12 characters')
        this.isSaved = false
        return
      }
      if (this.form.mobile == '') {
        this.errDialog('Phone number can not be empty.')
        this.isSaved = false
        return
      }
      if (this.form.id) {
        this.update()
      } else {
        this.addNew()
      }
    },
    addNew() {
      const user_id = this.userInfo.user_id
      const postForm = {
        userId: user_id,
        name: this.form.name,
        bankCode: this.form.bankCode,
        cardNo: this.form.cardNo,
        mobile: this.form.mobile,
      }
      this.addBankcard(postForm).then(res => {
        this.successBack(res)
      })
    },

    async update() {
      const user_id = this.userInfo.user_id
      let postForm = {
        id: this.form.id,
        userId: user_id,
        name: this.form.name,
        bankCode: this.form.bankCode,
        cardNo: this.form.cardNo,
        mobile: this.form.mobile,
      }
      this.errDialog("Please contact customer service to modify the bank card!")
      /*await this.updateBankCard(postForm).then(res => {
          this.successBack(res)
      })*/
    },

    successBack(res) {
      if (res && res.code == 200) {
        localStorage.removeItem('addCard')
        localStorage.removeItem('cardNo')
        localStorage.removeItem('id')

        this.isSaved = false
        this.showDialog(this.$t('sys.saved'))
        let { from } = this.$route.query
        if (from && from == 'sell') {
          this.$router.push({
            path: '/energy',
            query: {
              type: 2,
            },
          })
        }
        else if(from == 'buy'){
          this.$router.push({
            path: '/energy',
            query: {
              type: 1,
            },
          })
        } else {
          this.$router.push({
            path: '/bankcard',
            query: {
              backPath: '/profit',
            },
          })
        }
      } else {
        this.isSaved = false
        if(res.msg == 'number already exists')
          this.errDialog(res.msg)
        else
          this.errDialog(res.msg)
      }
    },
  },
  beforeDestroy() {
    this.isSaved = false
  },
}
</script>
