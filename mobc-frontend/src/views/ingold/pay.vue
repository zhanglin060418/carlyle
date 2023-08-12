<template>
  <div class="pay-page">
    <single-nav title="入金" :isShowRightIcon="false"></single-nav>
    <div class="main">
      <iframe scrolling="yes" id="main" name="main" frameborder="0" :src="url" style="width:100%;" :style="{height: oHeight + 'px'}"></iframe>
    </div>
  </div>
</template>
<script>

export default {
  data() {
    return {
      oHeight: 0,
      url: ""
    };
  },
  mounted() {
    this.url = sessionStorage.getItem("payurl");
    this.oHeight = document.body.clientHeight;
  },
  methods: {}
};
</script>
<style lang="less" scoped>
.pay-page {
  background: #fff;
  height: 100%;
}
</style>
