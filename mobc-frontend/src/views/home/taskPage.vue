<template>
  <!-- <mod-main title="Harian pada" class="task-main"> -->
  <div class="task-main">
    <!-- <h1 class="p-title">Penyegaran tugas <br />harian pada 0:00</h1> -->
    <task />
  </div>
  <!-- </mod-main> -->
</template>
<script>
import task from './task'
export default {
  components: {
    task,
  },
  data() {
    return {}
  },
}
</script>
