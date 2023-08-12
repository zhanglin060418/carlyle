<template>
  <van-popup
    v-model="show"
    class="popup-result topup-info"
    get-container="body"
    v-if="user"
  >
    <!-- <img
      class="poput-close"
      src="static/assets/image/dw/close.png"
      alt=""
      @click.stop="show = false"
    /> -->
    <div class="content">
      <div class="info">
        <h2>{{ $t('dw.t155') }}</h2>
        <div class="user-info">
          <p>{{ $t('payDetail.text2') }}:</p>
          <span>{{ user.name }}</span>
        </div>
        <div class="user-info">
          <p>{{ $t('user.mobile') }}:</p>
          <span>{{ user.mobilePhone }}</span>
        </div>
        <div class="user-info">
          <p>{{ $t('payDetail.text27') }}:</p>
          <span>{{ user.evp }}</span>
        </div>
      </div>
      <div class="popup-btn-group">
        <div class="popup-btn btn-view" @click="edit">
          {{ $t('user.update') }}
        </div>
        <div class="popup-btn" @click="confirm">
          {{ $t('sys.confirm') }}
        </div>
      </div>
    </div>
  </van-popup>
</template>
<script>
export default {
  props: {
    user: {
      type: Object,
      default: null,
    },
  },
  data() {
    return {
      show: false,
    }
  },
  methods: {
    edit() {
      this.show = false
      this.$router.push({
        path: '/recharge-info',
        query: {
          from: 'buy',
        },
      })
    },
    confirm() {
      this.show = false
      this.$emit('sure')
    },
  },
}
</script>
