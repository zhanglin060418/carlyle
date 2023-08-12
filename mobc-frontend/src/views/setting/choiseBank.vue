<template>
  <modMain title="Choose bank" class="noPadding setting">
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
        <MyUl v-for="(item,index) in searchList"
          :key="index"
          :item="item"
        ></MyUl>
      </div>
    </div>
  </modMain>
</template>
<script>
import { mapActions, mapGetters } from 'vuex'
import MyUl from './components/MyUL'
import { jump } from "@/assets/js/commonJs/scrollTop";
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
      id: null,
      name: null,
      description: null,
      list: [],
      searchList: []
    }
  },
  created() {
    let type = this.$route.query.type || 'withdraw'
    this.getBankList(type)
  },
  methods: {
    ...mapActions({
      bankList: 'bankCard/bankList',
    }),
    getBankList(type = 'withdraw') {
      this.bankList({ type: type }).then(res => {
        this.list = res.data
        this.searchList = this.list
        // this.comAreaData = this.combData()
      })
    },
    iptClick(val) {
      if(val != '') {
        let obj = [];
        let count = 0;
        for (let i = 0; i < this.list.length; i++) {
          let item = this.list[i]
          if (val) {
            let d = String(val).toLowerCase()
            if (item.name.toLowerCase().includes(d)) {
              obj[count++] = item
            }
          }
        }
        this.searchList = obj
        return
      }
      this.searchList = this.list
    },
    //-------- by 7 ------------
    /*combData(val = '') {
      let obj = {
        // hot: {
        //   name: 'Hot',
        //   list: [],
        // },
      }
      // let hot = ['id']
      console.log(val)
      for (let i = 0; i < this.list.length; i++) {
        let item = this.list[i]
        let str = item.name
        let first = str.substr(0, 1)
        // let nameCN = countriesName[i] // 中文的国家名称
        let phoneCodes = '' // item.length > 4 ? item[4][0] : ''

        if (val) {
          let d = String(val)
          let isCludes = item.name.indexOf(d) > -1
          console.log(isCludes)
          if (isCludes) {
            let first = item.name.substr(0, 1)
            if (!obj[first]) {
              obj[first] = {
                name: first,
                list: [],
              }
            }
            obj[first].list.push({
              label: item.name,
              id: item.id,
              code: item.code,
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
            label: item.name,
            id: item.id,

            code: item.code,
          })
        }
        // for (let j = 0; j < hot.length; j++) {
        //   if (hot[j] == item[1]) {
        //     obj.hot.list.push({
        //       label: item[0],
        //       area: item[1],
        //       code: item[2] + phoneCodes,
        //     })
        //   }
        // }
      }
      return obj
    },*/


    jump(key) {
      this.$vux.toast.show({
        time: 1000,
        text: key,
        position: "top",
        type: "text"
      });
      if (key == "热门") {
        key = "hot";
      }
      jump(`.${key}`);
    }
  },
}
</script>
