<template>
  <!-- <div class="login-tab">
    <div
      class="btn"
      @click.stop="tabClick(1)"
      :class="{ active: type == 'EMAIL' }"
    >
      {{ $t('home.email') }}
    </div>
    <div
      class="btn"
      @click.stop="tabClick(0)"
      :class="{ active: type == 'PHONE' }"
    >
      {{ $t('home.mobile') }}
    </div>
  </div> -->
  <p class="contact green" style="text-align:center" @click="switchToPhone">
    {{
      switchType == 'PHONE' ? $t('home.emailRegistration') : $t('home.phoneRegistration')
    }}
  </p>
</template>

<script>
import { mapGetters, mapMutations } from 'vuex'

export default {
  name: 'switchlogin',
  data() {
    return {
      type: 'PHONE',
    }
  },
  computed: {
    ...mapGetters({
      switchType: 'getSwitchType',
    }),
  },
  created() {
    this.type = this.switchType
  },
  methods: {
    ...mapMutations({
      setSwitchType: 'SET_SWITCH_TYPE',
    }),
    tabClick(tab) {
      this.type = tab == 0 ? 'PHONE' : 'EMAIL'
      this.setSwitchType(this.type)
    },
    switchToPhone() {
      this.setSwitchType(this.switchType == 'PHONE' ? 'EMAIL' : 'PHONE')
    },
  },
}
</script>
