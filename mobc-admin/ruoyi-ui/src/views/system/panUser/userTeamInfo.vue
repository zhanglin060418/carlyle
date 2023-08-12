<template>

  <div class="app-container" v-loading="loading">
        <table >
          <thead>
          <tr>
            <th>用户账号</th>
            <th>个人充值</th>
            <th>个人提现</th>
            <th>个人购买基金</th>
            <th>团队充值</th>
            <th>团队提现</th>
            <th>团队购买基金</th>
            <th>团队购买人数</th>
            <th>团队分佣</th>
            <th>团队人数</th>
          </tr>
          </thead>
          <tbody>
          <tr>
            <td>
              <button class="el-button el-button--text el-button--mini"
                       @click="getUserTramChinld(teamUserInfo.userId)">{{ teamUserInfo.userName }}</button>
              <template v-if="teamUserInfo.parentName"> >><button class="el-button el-button--text el-button--mini"
                      @click="getUserTramChinld(teamUserInfo.parentId)"> {{ teamUserInfo.parentName }}</button></template></td>
            <td>{{ parseFloat(teamUserInfo.rechargeCount) / 100 }}</td>
            <td>{{ parseFloat(teamUserInfo.withdrawCount) / 100 }}</td>
            <td>{{ parseFloat(teamUserInfo.purchaseCount) / 100 }}</td>
            <td>{{ parseFloat(teamUserInfo.totalRechargeCount) / 100 }}</td>
            <td>{{ parseFloat(teamUserInfo.totalWithdrawCount) / 100 }}</td>
            <td> {{ parseFloat(teamUserInfo.totalPurchaseCount) / 100 }}</td>
            <td> {{ teamUserInfo.totalTeamBuyCount }}</td>
            <td> {{ parseFloat(teamUserInfo.totalTeamReward) / 100 }}</td>
            <td> {{ teamUserInfo.totalPeopleCount  }}</td>
          </tr>
          <!-- 表格数据行 -->
          <tr v-for="item in userTeamList" :key="item.userId"  >
            <td>
              <button class="el-button el-button--text el-button--mini"
                @click="getUserTramChinld(item.userId)"
              >{{ item.userName }}</button>
            </td>
            <td>{{ parseFloat(item.rechargeCount) / 100 }}</td>
            <td>{{ parseFloat(item.withdrawCount) / 100 }}</td>
            <td>{{ parseFloat(item.purchaseCount) / 100 }}</td>
            <td>{{ parseFloat(item.totalRechargeCount) / 100 }}</td>
            <td>{{ parseFloat(item.totalWithdrawCount) / 100 }}</td>
            <td> {{ parseFloat(item.totalPurchaseCount) / 100 }}</td>
            <td> {{ item.totalTeamBuyCount }}</td>
            <td> {{ parseFloat(item.totalTeamReward) / 100 }}</td>
            <td> {{ item.totalPeopleCount  }}</td>
          </tr>
          </tbody>
        </table>
      <p v-show="showMoreInfo" class="load-more" @click="loadMore(teamUserInfo.userId)">加载更多</p>
      <p v-show="showAllInfo" class="load-all">已加载全部</p>
  </div>
</template>

<script>
  import { getUserTeamList } from "@/api/system/panUser";
  export default {
    name: "ChildComponent",
    data () {
      return {
        loading: false,
        showMoreInfo:false,
        showAllInfo:false,
        userTeamList: [],
        teamUserInfo:null,
        teamTotal: 0,
        currentPage: 0,
        pageSize: 10,
        teamQueryParams: {
          pageNum: 1,
          pageSize: 10,
          userId: null
        },
      }
    },
    methods: {
      getUserTram(row) {
        const userId = row.userId || this.ids;
        this.loading = true;
        this.currentPage = 0;
        getUserTeamList({userId: userId,currentPage:0,pageSize:this.pageSize}).then(response => {
          if(response.code==200){
            this.showMoreInfo = true;
            this.showAllInfo = false;
            this.userTeamList = response.userTeamList;
            this.teamUserInfo = response.teamUserInfo;
            if(this.userTeamList.length<this.pageSize){
              this.showMoreInfo = false
              this.showAllInfo = true;
            }
            this.loading = false;

          }else{
            this.errDialog(response.msg)
          }

          })
      },
      getUserTramChinld(userId) {
        this.loading = true;
        this.currentPage = 0;
        getUserTeamList({userId: userId,currentPage:0,pageSize:this.pageSize}).then(response => {
          if(response.code==200){
            this.showMoreInfo = true;
            this.showAllInfo = false;
            this.userTeamList = response.userTeamList;
            this.teamUserInfo = response.teamUserInfo;
            if(this.userTeamList.length<this.pageSize){
              this.showMoreInfo = false
              this.showAllInfo = true;
            }
            this.loading = false;
          }else{
            this.errDialog(response.msg)
          }
        })
      },
      loadMore(userId) {
        this.loading = true;
        const start = (this.currentPage + 1) * this.pageSize;
        getUserTeamList({userId: userId,currentPage:start,pageSize:this.pageSize}).then(response => {
          if(response.code==200){
            this.teamUserInfo = response.teamUserInfo;
            if(response.userTeamList.length>0){
              for (let item of response.userTeamList) {
                this.userTeamList.push(item);
              }
              this.currentPage++;
            }
            if(response.userTeamList<this.pageSize){
              this.showMoreInfo = false;
              this.showAllInfo = true;
            }
            this.loading = false;
          }else{
            this.errDialog(response.msg)
          }
        })
      },
    }
  };
</script>
<style>
  .load-more {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 50px;
    font-size: 14px;
    color: #1890ff;
    cursor: pointer;
  }

  .load-all{
    display: flex;
    justify-content: center;
    align-items: center;
    height: 50px;
    font-size: 14px;
    color:  #f45e1e;
    cursor: pointer;
  }
  .load-more:hover {
    color: #71e2a3;
  }
  table{
    width: 100%;
    border-collapse: collapse;
  }

  table caption{
    font-size: 2em;
    font-weight: bold;
    margin: 1em 0;
  }

  th,td{
    border: 1px solid #999;
    text-align: center;
    padding: 10px 0;
  }

  table thead tr{
    background-color: #f8f8f9;
    color: #515a6e;
    height: 40px;
    font-size: 15px;
    border-right: 1px solid #dfe6ec;
  }
  table tbody tr{
    height: 40px;
  }
  table tbody tr:nth-child(odd){
    background-color: #eee;
  }

  table tbody tr:hover{
    background-color: #ccc;
  }

  table tbody tr td:first-child{
    color: #1890ff;
  }

  table tfoot tr td{
    text-align: right;
    padding-right: 15px;
  }

  .el-button--mini{
    padding: 7px 5px;
    font-size: 15px;
    border-radius: 3px;
  }

</style>
