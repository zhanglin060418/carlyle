<template>
  <van-pull-refresh
    v-model="isDownLoading"
    @refresh="onDownRefresh"
    style="min-height:90vh;"
    :pullingText="pullingText"
    :loosingText="loosingText"
    :loadingText="loadingText"
    :disabled="disabled"
  >
    <van-list
      v-if="list && list.length > 0 && !isLoading"
      v-model="isUpLoading"
      :finished="upFinished"
      :immediate-check="false"
      offset="30"
      @load="onLoadList"
    >
      <slot name="content"></slot>
      <slot name="finished" v-if="list.length > params.size && upFinished">
        <p class="no-more">{{ $t('sys.nomore') }}</p>
      </slot>
    </van-list>
    <loadding v-else-if="isLoading" />
    <no-data v-else-if="!isLoading && list && list.length == 0"></no-data>
  </van-pull-refresh>
</template>
<script>
// import jsondata from "@/assets/json/xialadata.json";
export default {
  props: {
    params: {
      type: Object,
      default: {
        page: 1,
        size: 15,
      },
    },
    total: {
      type: Number,
      default: 0,
    },
    disabled: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      // jsondata,
      isLoading: true,
      isUpLoading: false, // 上拉加载更多
      upFinished: false, //上拉加载完毕
      isDownLoading: false, // 下拉刷新
      pullingText: this.$t('sys.pullingText'),
      loosingText: this.$t('sys.loosingText'),
      loadingText: this.$t('sys.uploaddingText'),
      list: [],
      // palyoption: {
      //   loop: true,
      //   autoplay: true,
      //   speed: 1,
      //   width: '120px',
      // },
    }
  },
  methods: {
    loadFinished(rows) {
      this.upFinished = false
      if (rows == null || rows.length === 0) {
        // 加载结束
        this.upFinished = true
        this.isDownLoading = false
        this.isUpLoading = false
        this.isLoading = false
        this.list = []
        this.$emit('setListData', [])
        return
      }
      if (rows.length < this.params.size) {
        // 最后一页不足10条的情况
        this.upFinished = true
      }
      // 处理数据
      if (this.params.page === 1) {
        this.list = rows
      } else {
        this.list = this.list.concat(rows)
      }
      this.$emit('setListData', this.list)
      //如果列表数据条数>=总条数，不再触发滚动加载
      if (this.list.length >= this.total) {
        this.upFinished = true
      }
      this.isDownLoading = false
      this.isUpLoading = false
      this.isLoading = false
    },
    // 下拉刷新
    onDownRefresh() {
      this.$emit('setPage', false)
      setTimeout(() => {
        this.list = []
        this.upFinished = false // 不写这句会导致你上拉到底过后在下拉刷新将不能触发下拉加载事件
        this.isDownLoading = true
        this.isUpLoading = true
        this.$emit('getList') // 加载数据
      }, 1000)
    },
    // 上拉加载请求方法
    onLoadList() {
      this.$emit('setPage')
      console.log('加载更多')
      setTimeout(() => {
        this.$emit('getList')
      }, 1000)
    },
  },
}
</script>
