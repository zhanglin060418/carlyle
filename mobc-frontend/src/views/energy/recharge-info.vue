<template>
  <modMain :title="$t('dw.t153')" class="setting">
    <div class="addcard-v">
      <div class="form-group">
        <van-form>
          <van-field
            v-model.trim="form.name"
            :placeholder="$t('user.enterHere')"
            :label="$t('payDetail.text2')"
          >
          </van-field>
          <p class="desc cpf-txt">
            {{ $t('payDetail.text44') }}
          </p>
          <div class="h20"></div>
          <van-field
            v-model.trim="form.mobilePhone"
            :placeholder="$t('user.enterHere')"
            :label="$t('user.mobile')"
          >
          </van-field>
          <p class="desc cpf-txt">
            {{ $t('payDetail.text47') }}
          </p>
          <div class="h20"></div>
          <van-field
            v-model.trim="form.evp"
            readonly
            is-link
            :placeholder="$t('user.pleaseChoose')"
            @click="focusCode"
            :label="$t('payDetail.text27')"
          >
          </van-field>
          <p class="desc cpf-txt">
            {{ $t('payDetail.text45') }}
          </p>
          <div class="btn-box">
            <base-btn
              :btnTitle="$t('sys.submit')"
              :disabled="false"
              @btnClick="btnClick"
            ></base-btn>
          </div>
        </van-form>
      </div>
    </div>
  </modMain>
</template>
<script>
import { mapGetters, mapMutations, mapActions } from 'vuex'
import moment from 'moment'

export default {
  data() {
    return {
      form: {
        name: '',
        mobilePhone: '',
        cpfCode: '', //银行code
        evp: '', //银行名称
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
        let obj = JSON.parse(localStorage.getItem('BANK_INFO'))
        vm.form.name = obj.name
        vm.form.mobilePhone = obj.mobilePhone
      }
    })
  },
  beforeRouteLeave(to, from, next) {
    if (to.path == '/choiseBank') {
      localStorage.setItem(
        'BANK_INFO',
        JSON.stringify({
          mobilePhone: this.form.mobilePhone,
          name: this.form.name,
        })
      )
    } else {
      localStorage.removeItem('BANK_INFO')
    }
    next()
  },
  created() {
    // if (this.$route.query.card) {
    //   let card = JSON.parse(this.$route.query.card)
    //   this.form.id = card.id
    //   this.form.mobilePhone = card.mobilePhone
    //   this.form.name = card.name
    //   this.form.bankCardId = card.bankCardId
    //   this.form.bankName = card.bankName
    //   this.form.bankCode = card.bankCode
    // }
    if (this.userInfo) {
      let { name, mobilePhone, cpfCode, evp } = this.userInfo
      this.form.name = name
      this.form.mobilePhone = mobilePhone
      this.form.cpfCode = cpfCode
      this.form.evp = evp
    }
    let bankCode = localStorage.getItem('bankCode') || null
    if (bankCode) {
      let item = JSON.parse(bankCode)
      this.form.cpfCode = item.code //充值银行信息
      this.form.evp = item.label
    }
  },
  methods: {
    ...mapActions({
      updateBank: 'bankCard/updateBank',
    }),
    ...mapActions('user', {
      updateUserBasic: 'updateUserBasic',
    }),
    ...mapMutations('user', {
      updateUser: 'UPDATE_USER',
    }),
    focusCode() {
      this.$router.push({
        path: '/choiseBank',
        query: {
          type: 'payment',
        },
      })
    },
    confirmDate(value) {
      this.form.birthday = moment(value).format('MM-DD-yyyy')
      this.hideBirthdayPopup()
    },
    btnClick() {
      if (this.isSaved) return
      this.isSaved = true
      if (this.form.name == '') {
        this.errDialog(this.$t('payDetail.text2') + this.$t('dw.t154'))
        this.isSaved = false
        return
      }
      if (this.form.mobilePhone == '') {
        this.errDialog(this.$t('user.mobile') + this.$t('dw.t154'))
        this.isSaved = false
        return
      }
      if (this.form.cpfCode == '') {
        this.errDialog(this.$t('payDetail.text27') + this.$t('dw.t154'))
        this.isSaved = false
        return
      }
      this.update()
    },

    async update() {
      await this.updateUserBasic(this.form).then(res => {
        if (res && res.code == 0) {
          this.successBack(res)
          this.updateUser(this.form)
        } else {
          this.errDialog(res.msg)
        }
      })
    },
    successBack(res) {
      if (res && res.code == 0) {
        localStorage.removeItem('addCard')
        localStorage.removeItem('bankCode')
        localStorage.removeItem('bankId')
        this.isSaved = false
        let { from } = this.$route.query
        if (from && 'buy') {
          this.$router.push({
            path: '/energy',
            query: {
              type: 1,
            },
          })
        }
      } else {
        this.isSaved = false
        this.errDialog(res.msg)
      }
    },
  },
  beforeDestroy() {
    this.isSaved = false
  },
}
</script>
