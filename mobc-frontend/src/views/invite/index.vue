<template>
  <modMain title="Invite friends" class="noPadding invite">
    <div class="invitation">
      <div class="box">
        <h3>{{ $t('sa.txt347') }}</h3>
        <div class="code-wrap">
          <div class="code">
            <div ref="qrCodeUrl"></div>
            <p>Tautan undangan</p>
          </div>
          <div class="link">
            <p>{{ $t('sa.txt349') }}<br />
              {{ $t('sa.txt350') }}
            </p>
            <b>{{ $t('sa.txt351') }}：{{ userInfo.inviteCode }}</b>
          </div>
          <i></i>
          <b></b>
          <span></span>
          <em></em>
        </div>
        <p class="line"></p>
        <div class="btn" @click="showShare = true">
          {{ $t('sa.txt352') }}
        </div>
      </div>
<!--//      <div class="box2">-->
<!--//        <h3>{{ $t('sa.txt353') }}</h3>-->
<!--//        <div>-->
<!--//          <p>-->
<!--            🔥🔥🔥<br />{{ $t('sa.txt354') }}-->
<!--          </p>-->
<!--          <br />-->
<!--          <p>-->
<!--            {{ $t('sa.txt355') }}<br />-->
<!--            {{ $t('sa.txt356') }}<br />-->
<!--            {{ $t('sa.txt357') }}<br />-->
<!--            {{ $t('sa.txt358') }}<br />-->
<!--            {{ $t('sa.txt359') }}<br />-->
<!--            {{ $t('sa.txt360') }}<br />-->
<!--            {{ $t('sa.txt361') }}<br />-->
<!--            {{ $t('sa.txt362') }}<br />-->
<!--            {{ $t('sa.txt363') }}<br />-->
<!--            {{ $t('sa.txt364') }}<br /><br />-->

<!--          </p>-->
<!--          <p>-->
<!--            🔥🔥🔥<br />{{ $t('sa.txt365') }}-->
<!--          </p>-->
<!--          <br />-->
<!--          <p>-->
<!--            {{ $t('sa.txt366') }}<br />-->
<!--            {{ $t('sa.txt367') }}<br /><br />-->
<!--            {{ $t('sa.txt368') }}<br />-->
<!--            {{ $t('sa.txt369') }}<br /><br />-->
<!--          </p>-->
<!--        </div>-->
<!--      </div>-->
    </div>
    <sharePopup
      :show="showShare"
      @close="close"
      :link="link"
      :code="userInfo.symbolCode"
    >
    </sharePopup>
  </modMain>
</template>
<script>
import QRCode from 'qrcodejs2'
import { mapGetters } from 'vuex'
export default {
  data() {
    return {
      showShare: false,
      link: '',
    }
  },
  computed: {
    ...mapGetters({
      userInfo: 'user/userInfo',
    }),
  },
  created() {
    this.link = window.config.domain + '#/register?c=' + this.userInfo.inviteCode
  },
  mounted() {
    this.createCodeImg()
  },
  methods: {
    createCodeImg() {
      new QRCode(this.$refs.qrCodeUrl, {
        text: this.link, // 需要转换为二维码的内容
        width: 109,
        height: 113,
        colorDark: '#000000',
        colorLight: '#ffffff',
        correctLevel: QRCode.CorrectLevel.H,
      })
    },
    copy(val) {
      this.showDialog(this.$t('home.copySuccess'))
    },
    close() {
      this.showShare = false
    },
  },
}
</script>
