<template>
  <div class="help-container help-one">
    <div class="title">How to top up?</div>
    <div class="help-main">
      <p>
        Super Wind adalah aplikasi game yang menghasilkan pendapatan harian
        dengan menonton iklan. Anda dapat memperoleh penghasilan dengan menonton
        iklan setiap hari.
      </p>
      <div class="help-video">
        <div class="small-title">Tonton video tutorialnya:</div>
        <!-- <video
          src=" https://superwind-003.s3-accelerate.amazonaws.com/video/topup.mp4"
          width="375"
          poster="static/assets/image/dw/video.png"
          controls
        ></video> -->
      </div>
      <div class="text-box">
        <p class="h-tip">Langkah pertama:</p>
        <p>
          Klik di bagian bawah menu-""Mine""
        </p>
        <p class="h-tip">Tahap kedua:</p>
        <p>
          Klik "Top up"
        </p>
        <div class="img-box">
          <!-- <img src="static/assets/image/dw/land/help1.jpg" alt="" /> -->
        </div>
        <p class="h-tip">Langkah ketiga:</p>
        <p>
          Pilih metode isi ulang, pilih jumlah isi ulang dan klik "Pembayaran"
        </p>

        <div class="img-box">
          <!-- <img src="static/assets/image/dw/land/help2.jpg" alt="" /> -->
        </div>
        <p class="h-tip">Langkah keempat:</p>
        <p>
          Setelah memilih bank dan memasukkan informasi yang benar untuk
          memastikan bahwa isi ulang berhasil
        </p>
        <div class="img-box">
          <!-- <img src="static/assets/image/dw/land/help3.jpg" alt="" /> -->
        </div>
      </div>
    </div>
  </div>
</template>

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
    .help-video {
      video {
        width: 100%;
      }
    }
    margin-top: 15px;
    p {
      font-size: 15px;
      line-height: 19px;

      color: #6c717d;
    }
    .small-title,
    .h-tip {
      margin-top: 20px;
      font-size: 20px;
      line-height: 26px;
      color: #1a1f2d;
    }
    .text-box {
      p {
        margin-bottom: 10px;
      }
    }
    .img-box {
      margin-bottom: 20px;
      margin-top: 10px;
    }
  }
}
</style>
