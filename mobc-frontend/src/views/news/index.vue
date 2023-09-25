<template>
  <modMain :title="$t('home.news')" :leftArrow="true"  class="pad0">

    <div class="news">
      <van-pull-refresh
              v-model="isDownLoading"
              @refresh="onDownRefresh"
              :pullingText="pullingText"
              :loosingText="loosingText"
              :loadingText="loadingText"
      >
        <van-list
                v-if="list && list.length > 0"
                v-model="isUpLoading"
                :finished="upFinished"
                :finished-text="$t('sys.nomore')"
                :immediate-check="false"
                offset="20"
                :loadingText="loadingText"
        >
          <div class="news-list">
            <ul>
              <li v-for="d in list" @click="toDetail(d)">
                <div class="l-img"><i class="iconfont icon-icnews"></i></div>
                <div class="right line05">
                  <div class="desc">
                    <p class="title">{{ d.noticeTitle }}</p>
                    <p class="time">{{ parseEnDateTime(d.createTime)  }}</p>
                  </div>
                  <div class="r-arrow">
                    <van-icon name="arrow"/>
                  </div>
                </div>
              </li>
            </ul>
          </div>
        </van-list>
        <no-data
                v-else-if="!isShowLoadding && (!list || list.length == 0)"
        ></no-data>
      </van-pull-refresh>
    </div>
  </modMain>


</template>

<script>
  import {parseEnDateTime} from "../../utils/parseEnDateTime";
  import {mapGetters, mapActions} from 'vuex'
  import mixinsSerivce from '@/mixins/service'

  export default {
    mixins: [mixinsSerivce],
    data() {
      return {
        isUpLoading: false, // 上拉加载更多
        upFinished: false, //上拉加载完毕
        isDownLoading: false, // 下拉刷新
        isShowLoadding: false,
        pullingText: this.$t('sys.pullingText'),
        loosingText: this.$t('sys.loosingText'),
        loadingText: this.$t('sys.uploaddingText'),
        list: [],
        params: {
          page: 1,
          size: 10,
        },
        total: 0,
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
      if (token == null) {
        this.errDialog(this.$t('msg.loginFirst'))
        return this.$router.push("/login")
      }
      this.getData()
    },
    methods: {
      parseEnDateTime,
      ...mapActions({
        queryStatementPage: 'user/queryStatementPage',
        getNoticeNews: 'user/getNoticeNews'
      }),
      toDetail(data) {
        this.$router.push({
          path: '/newsdetail',
          query: {
            data: JSON.stringify(data),
          },
        })
      },
      async getData() {
        this.isShowLoadding = true
        let res = await this.getNoticeNews()
        this.list = res.list
        this.isShowLoadding = false
        this.isDownLoading = false
        this.isUpLoading = false
      },
      loadFinished(rows) {
        this.upFinished = false
        // this.isLoading = true
        if (rows == null || rows.length === 0) {
          // 加载结束
          this.upFinished = true
          this.isDownLoading = false
          this.isUpLoading = false
          this.isLoading = false
          return
        }
        if (rows.length < this.params.size) {
          // 最后一页不足size条的情况
          this.upFinished = true
        }
        // 处理数据
        if (this.params.page === 1) {
          this.list = rows
        } else {
          this.list = this.list.concat(rows)
        }
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
        this.params.page = 1
        setTimeout(() => {
          this.incomeData = []
          this.upFinished = false // 不写这句会导致你上拉到底过后在下拉刷新将不能触发下拉加载事件
          this.isDownLoading = false
          this.isUpLoading = false
          this.getData()
        }, 1000)
      },
    },
  }
</script>
