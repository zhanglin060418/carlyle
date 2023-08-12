<template>
  <div class="area-aside">
    <ul>
      <li v-for="val in list" :key="val" @click="$emit('click',val)">{{val}}</li>
    </ul>
  </div>
</template>


<script>
export default {
  props: {
    list: {}
  }
};
</script>
