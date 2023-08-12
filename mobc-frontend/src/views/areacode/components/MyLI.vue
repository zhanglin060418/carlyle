<template>
  <div class="area-li line05" @click="codeClick($event)">
    <div class="area-logo">
      <span class="iti-flag" :class="item.area"></span>
      <span>{{item.label}}</span>
    </div>
    <span v-if="!isCountry">+{{ item.code }}</span>
    <span v-if="isCountry&&item.code==currentCode" class="icon-ok"></span>
  </div>
</template>

<script>
export default {
  props: {
    item: {
      default: () => {
        return {
          label: "China",
          code: 86
        };
      }
    },
    isCountry: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      currentCode: '',
    }
  },
  created() {
    this.getCountryCode()
  },
  methods: {
    codeClick(e) {
      let strItem = JSON.stringify(this.item)
      if (!this.isCountry) {
        localStorage.setItem("areaItem", strItem);
        this.$router.go(-1);
      } else {
        localStorage.setItem("COUNTRY", strItem);
        let all = document.querySelectorAll('.icon-ok');
        if (all) {
          all.forEach(dom => {
            dom.remove()
          })
        }
        this.$nextTick(() => {
          let dom = e.target || e.srcElement
          let span = document.createElement("span");
          span.className = 'icon-ok'
          dom.appendChild(span)
        })
      }
    },
    getCountryCode() {
      let item = localStorage.getItem("COUNTRY") || null
      if (item) {
        item = JSON.parse(item)
        this.currentCode = item.code
      }
    }
  }
};
</script>
