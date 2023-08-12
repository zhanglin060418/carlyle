<template>
  <div class="help-container help-one">
    <div class="title">How to withdraw money?</div>
    <div class="help-main">
      <div class="text-box">
        <p class="small-title">Langkah pertama:</p>
        <p>
          Klik di bagian bawah menu - "Mine", masukkan "withdrawal"
        </p>
      </div>
      <div class="img-box">
        <!-- <img src="static/assets/image/dw/land/helpw1.jpg" alt="" /> -->
      </div>

      <p class="small-title">Tahap kedua:</p>
      <p>
        Pilih metode pembayaran "Bank" dan ikat kartu bank Anda
      </p>
      <div class="img-box">
        <!-- <img src="static/assets/image/dw/land/helpw2.jpg" alt="" /> -->
      </div>

      <p class="small-title">Langkah ketiga:</p>
      <p>
        Isi informasi penerima pembayaran dan pastikan itu benar, jika tidak,
        Anda tidak akan menerima transfer
      </p>
      <div class="img-box">
        <!-- <img src="static/assets/image/dw/land/helpw3.jpg" alt="" /> -->
      </div>
      <p class="small-title">Langkah keempat:</p>
      <p>
        Masukkan jumlah penarikan, pilih kartu bank Anda, kirim dan tunggu
        transfer bank
      </p>
      <div class="img-box">
        <!-- <img src="static/assets/image/dw/land/helpw4.jpg" alt="" /> -->
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
    margin-top: 15px;
    p {
      font-size: 15px;
      line-height: 19px;

      color: #6c717d;
    }
    .small-title {
      margin-bottom: 10px;
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
