<template>
  <modMain title="404" class="noPadding setting">
    <div class="error-404">
      <h1>Error 404</h1>
      <h2>We can't find the page that you are looking for</h2>
      <van-button plain hairline type="info" @click="$router.replace('/')">Back to Home</van-button>
    </div>
  </modMain>
</template>
<style lang="less" scoped>
.error-404 {
  display: flex;
  // justify-content: center;
  flex-direction: column;
  padding: 0 16px;
  height: 100vh;

  h1 {
    padding: 40px 0;
    color: #fff;
  }
  h2 {
    margin: 20px 0;
    color: #fff;
  }
}
</style>
