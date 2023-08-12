<template>
  <modMain :title="$t('user.cpfTitle')" class="noPadding" from="payCpf">
    <div class="personal-data">
      <div class="form-group">
        <van-form>
          <van-field
            v-model="form.name"
            :placeholder="$t('user.enterHere')"
            :label="$t('user.name')"
          >
          </van-field>
          <p class="cpf-txt">
            The same name as the payment account, if not, then it will not be
            pulled up
          </p>
          <van-field
            v-model="form.email"
            :placeholder="$t('user.enterEmail')"
            :label="$t('user.email')"
          >
          </van-field>
          <p class="cpf-txt">
            The same email address as payment
          </p>
          <!-- <van-field
            v-model="form.cpfCode"
            :placeholder="$t('user.enterHere')"
            :label="$t('user.cpf')"
          >
          </van-field>
          <p class="cpf-txt">
            Consistente com as informações do beneficiário da conta PIX
          </p> -->
          <van-field
            v-model="form.mobilePhone"
            :placeholder="$t('user.enterHere')"
            :label="$t('user.mobile')"
          >
          </van-field>
          <p class="cpf-txt">
            The same cellphone number as payment receiver account
          </p>
          <div class="cpf-tips">
            <h2>Instruction:</h2>
            <p class="cpf-txt">
              1. Make sure your filled information is correct.<br />
              2.Contact customer service
              <a href="javascript:void(0)" @click="toService()">WhatsApp</a>
              Can't pay or withdraw from information Incorrect person.
            </p>
          </div>
          <div class="btn-box">
            <base-btn
              :btnTitle="$t('sys.submit')"
              :disabled="disabled"
              @btnClick="onSubmit"
            ></base-btn>
          </div>
        </van-form>
      </div>
    </div>
  </modMain>
</template>
<script>
import { mapActions, mapMutations, mapGetters } from 'vuex'
import mixinsSerivce from '@/mixins/service'
export default {
  mixins: [mixinsSerivce],
  data() {
    return {
      locale: localStorage.getItem('locale') || 'en_US',
      form: {
        name: '',
        // surname: '',
        email: '',
        // cpfCode: '',
        mobilePhone: '',
      },
      wathsrls: [],
    }
  },
  computed: {
    ...mapGetters({
      userInfo: 'user/userInfo',
    }),
    disabled() {
      return (
        this.form.name == '' ||
        this.form.email == '' ||
        this.form.mobilePhone == ''
      )
    },
  },
  created() {
    Object.keys(this.form).forEach(key => {
      this.form[key] = this.userInfo[key]
    })
  },
  methods: {
    cpfClick() {},
    ...mapActions('user', {
      updateUserBasic: 'updateUserBasic',
    }),
    ...mapMutations('user', {
      updateUser: 'UPDATE_USER',
    }),
    onSubmit() {
      //
      this.updateUserBasic(this.form).then(res => {
        if (res && res.code == 0) {
          this.updateUser(this.form)
          // this.$emit('closeCpf')
          this.$router.go(-1)
        } else {
          this.errDialog(res.msg)
        }
      })
    },
  },
}
</script>
