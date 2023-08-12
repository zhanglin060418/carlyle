<template>
  <div class="qa-main">
    <!-- <div class="qa">{{ $t('user.qa') }}</div> -->
    <div class="content">
      <div class="list-box" v-for="(item, idx) in problemData">
        <div class="title" @click="openProblem(item)">
          <p :class="{ ell: !item.isOpen, large: !item.isOpen }">
            {{ idx + 1 }}、{{ lang == 'en_US' ? item.titleEn : item.titleId }}
          </p>
          <div class="right">
            <i class="right-arrow" :class="{ down: !item.isOpen }"></i>
          </div>
        </div>
        <div class="info" v-show="!item.isOpen" v-html="lang == 'en_US' ? item.contentEn : item.contentId"></div>
      </div>
    </div>
  </div>
</template>

<script>
import { problemData } from './qaData.js'
export default {
  data() {
    return {
      problemData,
      params: {
        page: 1,
        size: 200,
      },
      lang: localStorage.getItem('locale') || 'en_US',
    }
  },
  methods: {
    getData() {
      // this.helpProblemPage(this.params).then(res => {
      //   console.log('常见问答', res)
      //   if (res && res.code == 0) {
      //     let data = res.data ? res.data.records : []
      //     data.forEach(d => d.isOpen = false)
      //     this.problemData = data
      //   }
      // })
    },
    openProblem(item) {
      item.isOpen = !item.isOpen
    },
  },
}
</script>
