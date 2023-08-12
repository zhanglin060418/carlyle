<template>
  <div class="area-main">
    <div class="search-area" :class="{ iscountry: isCountry }">
      <div class="search-box">
        <i class="search-icon"></i>
        <input
          type="text"
          placeholder="Search"
          @input="iptClick($event.target.value)"
        />
      </div>
    </div>
    <div class="area-country">
      <MyUl
        v-for="(itme, key) in comAreaData"
        :class="key"
        :item="itme"
        :isCountry="isCountry"
        :key="key"
      ></MyUl>
    </div>
  </div>
</template>
<script>
// import { mapActions, mapGetters } from "vuex";
import areaCode from '@/assets/js/commonJs/areaCode'
import { countriesName } from '@/assets/js/commonJs/data-cn'
import MyUl from './components/MyUL'
// import { jump } from "@/assets/js/commonJs/scrollTop";
export default {
  props: {
    isCountry: {
      type: Boolean,
      default: false,
    },
  },
  components: {
    MyUl,
  },
  data() {
    return {
      areaCode: areaCode,
      comAreaData: null,
    }
  },
  computed: {
    cptAsideList() {
      let list = []
      let obj = this.areaCode
      list = Object.keys(obj)
      list[0] = 'Hot'
      return list
    },
  },
  created() {
    this.comAreaData = this.combData()
  },
  methods: {
    iptClick(value) {
      if (value == '') {
        this.areaCode = areaCode
        this.comAreaData = this.combData()
      } else {
        this.searchData(value.toLowerCase())
      }
    },
    searchData(val) {
      // this.areaCode = areaCode.filter(d => d[0].toLowerCase().indexOf(val) > -1 || d[2].indexOf(val) > -1)
      // debugger
      this.comAreaData = this.combData(val)
    },
    combData(val = '') {
      let obj = {
        hot: {
          name: 'Hot',
          list: [],
        },
      }
      let hot = ['id']

      for (let i = 0; i < this.areaCode.length; i++) {
        let item = this.areaCode[i]
        let str = item[0]
        let first = str.substr(0, 1)
        let nameCN = countriesName[i] // 中文的国家名称
        let phoneCodes = '' // item.length > 4 ? item[4][0] : ''

        if (val) {
          let d = String(val)
          let isCludes =
            item[0].toLowerCase().indexOf(d) > -1 ||
            item[1].indexOf(d) > -1 ||
            item[2].indexOf(d) > -1 ||
            nameCN.indexOf(d) > -1

          if (isCludes) {
            let first = item[0].substr(0, 1)
            if (!obj[first]) {
              obj[first] = {
                name: first,
                list: [],
              }
            }
            obj[first].list.push({
              label: item[0],
              area: item[1],
              code: item[2] + phoneCodes,
              nameCN: nameCN,
            })
          }
        } else {
          if (!obj[first]) {
            obj[first] = {
              name: first,
              list: [],
            }
          }
          obj[first].list.push({
            label: item[0],
            area: item[1],
            code: item[2] + phoneCodes,
            nameCN: nameCN,
          })
        }
        for (let j = 0; j < hot.length; j++) {
          if (hot[j] == item[1]) {
            obj.hot.list.push({
              label: item[0],
              area: item[1],
              code: item[2] + phoneCodes,
              nameCN: nameCN,
            })
          }
        }
      }
      return obj
    },
    // jump(key) {
    //   this.$vux.toast.show({
    //     time: 1000,
    //     text: key,
    //     position: "top",
    //     type: "text"
    //   });
    //   if (key == "热门") {
    //     key = "hot";
    //   }
    //   jump(`.${key}`);
    // }
  },
}
</script>
