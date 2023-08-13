<template>
  <div class="qa-main">
    <!-- <div class="qa">{{ $t('user.qa') }}</div> -->
    <div class="content">
      <div class="list-box" v-for="(item, idx) in problemData">
        <div class="title" @click="openProblem(item)">
          <p :class="{ ell: item.status==1, large: item.status==1 }">
            {{ idx + 1 }}.{{ item.noticeTitle}}
          </p>
          <div class="right">
            <i class="right-arrow" :class="{ down: item.status==1 }"></i>
          </div>
        </div>
        <div class="info" v-show="item.status==1" v-html="item.noticeContent"></div>
      </div>
    </div>
    <loadding v-if="isLoading"></loadding>
  </div>
</template>

<script>
// import { problemData } from './qaData.js'
import { mapGetters, mapActions } from 'vuex'
import mixinsSerivce from '@/mixins/service'
export default {
  mixins: [mixinsSerivce],
  data() {
    return {
      isOpen:false,
      isLoading:false,
      problemData: [],

    }
  },
  watch: {
    '$route'() {
      if (this.$route.name == 'news')
        this.getData()
    }
  },
  created() {
    let token = localStorage.getItem('token') || null
    if(token == null) {
      this.errDialog(this.$t('msg.loginFirst'))
      return this.$router.push("/login")
    }
    this.getData()
  },
  methods: {
    ...mapActions({
      getNoticeNewsByQa: 'user/getNoticeNewsByQa'
    }),
    async getData() {
      this.isLoading = true;
      let res = await this.getNoticeNewsByQa()
      this.problemData = res.list
      this.isLoading = false;
    },
    openProblem(item) {
      if(item.status==0){
        item.status = 1
      }else{
        item.status = 0
      }
    },
  },
}
</script>
