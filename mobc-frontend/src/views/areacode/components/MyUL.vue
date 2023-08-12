<template>
  <div class="area-ul">
    <label>{{ item.name }}</label>
    <div class="box">
      <MyLI v-for="(val,index) in item.list" :key="val.area" :isCountry="isCountry" :item="val"></MyLI>
    </div>
  </div>
</template>

<script>
import MyLI from "./MyLI";
export default {
  components: {
    MyLI
  },
  props: {
    item: {},
    isCountry: {
      type: Boolean,
      default: false
    }
  }
};
</script>
