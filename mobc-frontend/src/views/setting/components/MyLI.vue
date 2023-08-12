<template>
  <div class="area-li line05" @click="codeClick($event)">
    <div class="area-logo">
      <span v-if="!isCountry" class="iti-flag" :class="item.area"></span>
      <span>{{ item.description }}</span>
    </div>
    <span v-if="!isCountry">+{{ item.code }}</span>
    <span v-if="item.id == currentId" class="icon-ok"></span>
  </div>
</template>

<script>
export default {
  props: {
    item: {
      default: () => {
        return {
          label: 'China',
          code: 86,
        }
      },
    },
    isCountry: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      currentId: '',
    }
  },
  created() {
    this.getCountryCode()
  },
  methods: {
    codeClick(e) {
      let bankCode = JSON.stringify(this.item)
      localStorage.setItem('bankCode', bankCode)
      localStorage.setItem('bankId', this.item.id)
      this.$router.go(-1)
      this.$nextTick(() => {
        let dom = e.target || e.srcElement
        let span = document.createElement('span')
        span.className = 'icon-ok'
        dom.appendChild(span)
      })
    },
    getCountryCode() {
      let item = localStorage.getItem('bankId') || null
      if (item) {
        item = JSON.parse(item)
        this.currentId = item
      }
    },
  },
}
</script>
