<template>
  <div class="area-ul" @click="codeClick($event)">
<!--    <span class="icon-ok" :class="{unchecked: item.id == currentId}"></span>-->
    <img v-if="item.img != null" class="logo" src="https://placehold.jp/150x150.png" alt="logo"/>
    <img v-else src="static/assets/image/bank.png" style="width: 36px; height:36px;" alt=""/>
    <div class="detail">
      <p class="name">{{ item.name }}</p>
      <p class="code">{{ item.code }}</p>
    </div>
<!--    <i class="icon-arrow" :class="{ down: item.open }"></i>-->
  </div>
</template>

<script>
import MyLI from "./MyLI";
export default {
  components: {
    MyLI
  },
  data() {
    return {
      currentId: '',
    }
  },
  props: {
    item: {},
  },
  methods: {
    codeClick(e) {
      localStorage.setItem('bankName', this.item.name)
      localStorage.setItem('bankCode', this.item.code)
      this.$router.go(-1)
      this.$nextTick(() => {
        let dom = e.target || e.srcElement
        let span = document.createElement('span')
        span.className = 'icon-ok'
        dom.appendChild(span)
      })
    },
  }
};
</script>
