<template>
  <modMain>
    <div class="news-detail">
      <h2 class="title">
        {{
          item.noticeTitle
        }}
      </h2>
      <div class="post-time">{{item.createTime}}</div>
      <div class="ql-snow">
        <div class="ql-snow ql-editor" v-html="item.noticeContent" />
        <p><br /></p>
      </div>
    </div>
  </modMain>
</template>
<script>
export default {
  data() {
    return {
      item: null,
      detail: null,
    }
  },
  created() {
    this.item = JSON.parse(this.$route.query.data)
  }
}
</script>

<style>
.ql-syntax {
  display: flex;
  background-color: #23241f;
  color: #f8f8f2;
  overflow: visible;
  width: 100%;
}

</style>
