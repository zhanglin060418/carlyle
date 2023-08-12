<template>
  <div class="help-container help-one">
    <div class="title">Share link</div>
    <div class="help-main">
      <div class="small-title">Langkah pertama:</div>
      <p>
        Klik pada "Invite" Undang di aplikasi dan salin tautan untuk dikirim ke
        teman Anda
      </p>
      <div class="small-title">Tahap kedua:</div>
      <p>
        Beritahu temanmu untuk mendaftar untuk mendapatkan puzzle kelinci
        senilai Rp100000
      </p>
      <div class="small-title">Langkah ketiga:</div>
      <p>
        Teman mendaftar untuk aplikasi melalui tautan dan Anda mendapatkan Rp400
      </p>
      <div class="img-box">
        <!-- <img src="static/assets/image/dw/land/help7.jpg" alt="" /> -->
      </div>

      <rule class="help-four-rule" />
    </div>
  </div>
</template>
<script>
import rule from '../../team/rule.vue'
export default {
  components: {
    rule,
  },
}
</script>
<style lang="less" scoped>
.help-container {
  padding: 15px 16px;
  background: #fff;
  min-height: 100%;
  .title {
    font-size: 22px;
    line-height: 28px;

    color: #1a1f2d;
    padding-bottom: 15px;
    border-bottom: 1px solid #f2f2f2;
  }
}
.help-one {
  .help-main {
    margin-top: 15px;
    p {
      font-size: 15px;
      line-height: 19px;

      color: #6c717d;
    }
    .small-title {
      margin-top: 20px;
      font-size: 20px;
      line-height: 26px;
      color: #1a1f2d;
      margin-bottom: 10px;
    }
    .text-box {
      p {
        // margin-bottom: 10px;
        &:nth-of-type(1) {
          margin-bottom: 10px;
        }
      }
    }
    .img-box {
      margin: 20px 0;
    }
  }
}
</style>
