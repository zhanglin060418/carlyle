<template>
  <div class="mine">
    <div class="user-wrap info-list" v-if="messageList && messageList.length > 0">
      <template v-for="(item, index) in messageList">
        <div class="message-box">
          <div class="info-cell" >
            <template v-if="!item.headImg">
              <img src="static/assets/image/logo.png" alt="" />
            </template>
            <template v-else>
              <img :src="imgBaseUrl + item.headImg" alt="" />
            </template>
            <div class="info">
              <div class="username">
                <span> {{ item.userName.substring(0, 3) }} **** {{ item.userName.substring(item.userName.length-3, item.userName.length) }}</span>
              </div>
              <span class="time">{{ parseEnDateTime(item.createTime)  }}</span>
            </div>
          </div>
          <van-divider  :style="{padding: '0px',margin:'0px'}"/>
          <div class="info-content">
            <div class="text-main">
              <div class="text-box">
                <template v-if="item.content!=''">
                  <p class="ql-snow ql-editor">{{ item.content }}</p>
                </template>
                <template v-if="item.coverImage!=''">
                  <viewer :images="image" >
                    <img :src="imgBaseUrl + item.coverImage" alt="" />
                  </viewer>
                </template>
              </div>
            </div>
          </div>
          <div class="article_likes" @click="dolikes(index)">
            <template v-if="!item.likesbefore">
              <img src="static/assets/image/icon/likes-after.png" alt="" />
            </template>
            <template v-else>
              <img src="static/assets/image/icon/likes-before.png" alt="" />
            </template>
            <span class="likes" v-bind:class="[item.likesbefore ? 'gray':'pink']">{{item.likesNum}}</span>
          </div>
        </div>
      </template>
      <p v-show="showMoreInfo" class="load-more" @click="loadMore()">Load more</p>
      <p v-show="showAllInfo" class="load-all">Loading......</p>
    </div>
    <no-data v-else></no-data>
    <loadding v-if="isLoading"></loadding>
    <ReleaseInfo/>
  </div>
</template>
<script>
  import {parseEnDateTime} from "../../utils/parseEnDateTime";
  import { mapActions, mapGetters, mapMutations } from 'vuex'
  import mixinsSerivce from '@/mixins/service'
  export default {
    mixins: [mixinsSerivce],
    created() {
      let token = localStorage.getItem('token') || null
      if(token == null) {
        this.errDialog(this.$t('msg.loginFirst'))
        return this.$router.push("/login")
      }
      this.userId = localStorage.getItem('userId') || null
      this.getList()

    },
    data() {
      return {
        sym:'',
        currencyShape: '',
        userId:'',
        onClickControl: false,
        currentPage: 0,
        messageList:[],
        pageSize: 5,
        showMoreInfo:false,
        showAllInfo:false,
        isLoading: false,
      }
    },
    computed: {
      ...mapGetters({
        userInfo: 'user/userInfo',
        userAccount: 'user/userAccount',
      }),
    },
    watch: {
      '$route'() {
        if (this.$route.name == 'profit') {
          this.getList()
        }
      },
      userInfo: {
        deep: true,
        immediate: true,
        handler: function (val) {
          if(!val.user_id)
            return;
          this.getList()
        }
      }
    },
    methods: {
      parseEnDateTime,
      ...mapActions({
        getMessageList: 'user/getMessageList',
        createLikes: 'user/createLikes'
      }),
      ...mapMutations('user', {
        quitLogin: 'quitLogin',
      }),
      dolikes(index){
        if(this.onClickControl == false) {
          this.onClickControl = true
          this.isLoading = true
          const user_id = this.userId;
          console.log(this.messageList[index])
          this.messageList[index].likesNum = this.messageList[index].likesbefore ? parseInt(this.messageList[index].likesNum) + 1 : parseInt(this.messageList[index].likesNum) - 1
          this.messageList[index].likesbefore = !this.messageList[index].likesbefore;
          const formData = {
            userId: user_id,
            messageId: this.messageList[index].messageId,
            likesbefore: this.messageList[index].likesbefore
          }
          this.createLikes(formData).then(res => {
            this.onClickControl = false
            if (res.code == 200) {
              this.isLoading = false
            } else {
              this.isLoading = false
              this.errDialog(res.msg)
            }
          })
        }
       },
      async getList() {
        this.currentPage = 0;
        const user_id = this.userId;
        let res = await this.getMessageList({
          userId:user_id,
          currentPage:0,
          pageSize:this.pageSize
        })
        this.messageList = res.data;
        this.showMoreInfo = true;
        if(res.data.length<this.pageSize){
          this.showAllInfo = true;
          this.showMoreInfo = false;
        }
      },
      loadMore(){
        this.isLoading = true;
        const user_id = this.userId;
        const startPage = (this.currentPage + 1) * this.pageSize;
        this.getMessageList({userId:user_id, currentPage: startPage, pageSize:this.pageSize}).then(response => {
          if(response.code==200){
            if(response.data.length>0){
              for (let item of response.data) {
                this.messageList.push(item);
              }
              this.currentPage++;
            }
            if(response.data.length<this.pageSize){
              this.showAllInfo = true;
              this.showMoreInfo = false;
            }
          }
          this.isLoading = false;
        })
      },

    },
  }
</script>
<style>
  .load-more {
    width: 100%;
    display: flex;
    justify-content: center;
    height: 20px;
    margin-top: 5px;
    font-size: 14px;
    color:  #3376c4;
    cursor: pointer;
  }

  .load-all{
    width: 100%;
    display: flex;
    justify-content: center;
    height: 20px;
    margin-top: 5px;
    font-size: 14px;
    color:  #9fa3aa;
    cursor: pointer;
  }
</style>
