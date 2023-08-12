<template>
  <div class="app-container">
    <div class="main" v-loading="loading">
      <div class="main-content" style="display: block">
        <div>
          <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"  label-width="68px">
            <el-form-item label="日期">
              <el-date-picker
                size="mini" v-model="dateRange"
                style="width: 240px"
                value-format="yyyy-MM-dd"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
              ></el-date-picker>
            </el-form-item>

            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            </el-form-item>

          </el-form>

        </div>
        <div class="dash-card-group" >
          <div class="dash-card-box">
            <div class="demo-color-box" style="background: rgb(64, 158, 255)">
              <span>用户总数</span>
            </div>
            <div class="dash-card-value">
              <span>{{ totalPeople }}</span>
            </div>
          </div>
          <div class="dash-card-box">
            <div class="demo-color-box" style="background: rgb(64, 158, 255)">
              <span>用户总余额</span>
            </div>
            <div class="dash-card-value">
              <span>{{ parseFloat(totalBalance)/100 }}</span>
            </div>
          </div>

          <div class="dash-card-box">
            <div class="demo-color-box" style="background: rgb(64, 158, 255)">
              <span>今日活跃用户数</span>
            </div>
            <div class="dash-card-value">
              <span>{{ todayActiveUsers }}</span>
            </div>
          </div>

          <div class="dash-card-box">
            <div class="demo-color-box" style="background: rgb(64, 158, 255)">
              <span>今日注册用户</span>
            </div>
            <div class="dash-card-value">
              <span>{{ todayRegisterUsers }}</span>
            </div>
          </div>

          <div class="dash-card-box">
            <div class="demo-color-box" style="background: rgb(64, 158, 255)">
              <span>代理商可提现余额</span>
            </div>
            <div class="dash-card-value">
              <span>{{ parseFloat(agentBalance/100).toFixed(2)  }}</span>
            </div>
          </div>
        </div>

        <div class="dash-card-group">
          <div class="dash-card-box">
            <div class="demo-color-box" style="background: rgb(103, 194, 58);">
              <span>总充值用户</span>
            </div>
            <div class="dash-card-value">
              <span>{{ totalRechargeUsers }}</span>
            </div>
          </div>

          <div class="dash-card-box">
            <div class="demo-color-box" style="background: rgb(103, 194, 58);">
              <span>总充值订单</span>
            </div>
            <div class="dash-card-value">
              <span>{{ totalRechargeCount }}</span>
            </div>
          </div>

          <div class="dash-card-box">
            <div class="demo-color-box" style="background: rgb(103, 194, 58);">
              <span>总充值金额</span>
            </div>
            <div class="dash-card-value">
              <span>{{ parseFloat(totalRechargeAmt)/100 }}</span>
            </div>
          </div>

          <div class="dash-card-box">
            <div class="demo-color-box" style="background: rgb(103, 194, 58);">
              <span>今日充值用户</span>
            </div>
            <div class="dash-card-value">
              <span>{{ todayRechargeUsers }}</span>
            </div>
          </div>

          <div class="dash-card-box">
            <div class="demo-color-box" style="background: rgb(103, 194, 58);">
              <span>今日充值订单</span>
            </div>
            <div class="dash-card-value">
              <span>{{ todayRechargeCount }}</span>
            </div>
          </div>

          <div class="dash-card-box">
            <div class="demo-color-box" style="background: rgb(103, 194, 58);">
              <span>今日充值金额</span>
            </div>
            <div class="dash-card-value">
              <span>{{ parseFloat(todayRechargeAmt)/100 }}</span>
            </div>
          </div>

          <div class="dash-card-box">
            <div class="demo-color-box" style="background: rgb(103, 194, 58);">
              <span>今日首充用户</span>
            </div>
            <div class="dash-card-value">
              <span>{{ todayFristRechargeUsers }}</span>
            </div>
          </div>
        </div>
        <div class="dash-card-group">
          <div class="dash-card-box">
            <div class="demo-color-box" style="background: rgb(103, 194, 58);">
              <span>总提现用户</span>
            </div>
            <div class="dash-card-value">
              <span>{{ totalWithdrawUsers }}</span>
            </div>
          </div>

          <div class="dash-card-box">
            <div class="demo-color-box" style="background: rgb(103, 194, 58);">
              <span>总提现订单</span>
            </div>
            <div class="dash-card-value">
              <span>{{ totalWithdrawCount}}</span>
            </div>
          </div>

          <div class="dash-card-box">
            <div class="demo-color-box" style="background: rgb(103, 194, 58);">
              <span>总提现金额</span>
            </div>
            <div class="dash-card-value">
              <span>{{ parseFloat(totalWithdrawAmt)/100 }}</span>
            </div>
          </div>

          <div class="dash-card-box">
            <div class="demo-color-box" style="background: rgb(103, 194, 58);">
              <span>今日提现用户</span>
            </div>
            <div class="dash-card-value">
              <span>{{ todayWithdrawUsers }}</span>
            </div>
          </div>

          <div class="dash-card-box">
            <div class="demo-color-box" style="background: rgb(103, 194, 58);">
              <span>今日提现订单</span>
            </div>
            <div class="dash-card-value">
              <span>{{ todayWithdrawCount }}</span>
            </div>
         </div>

          <div class="dash-card-box">
            <div class="demo-color-box" style="background: rgb(103, 194, 58);">
              <span>今日提现金额</span>
            </div>
            <div class="dash-card-value">
              <span>{{ parseFloat(todayWithdrawAmt)/100 }}</span>
            </div>
          </div>
        </div>

        <div class="dash-card-group">
          <div class="dash-card-box">
            <div class="demo-color-box" style="background: rgb(199,17,55)">
              <span>总订单用户</span>
            </div>
            <div class="dash-card-value">
              <span>{{ totalOrderUsers }}</span>
            </div>
          </div>

          <div class="dash-card-box">
            <div class="demo-color-box" style="background: rgb(199,17,55)">
              <span>总订单</span>
            </div>
            <div class="dash-card-value">
              <span>{{ totalOrderCount }}</span>
            </div>
          </div>

          <div class="dash-card-box">
            <div class="demo-color-box" style="background: rgb(199,17,55)">
              <span>总订单金额</span>
            </div>
            <div class="dash-card-value">
              <span>{{ parseFloat(totalOrderAmt)/100 }}</span>
            </div>
          </div>

          <div class="dash-card-box">
            <div class="demo-color-box" style="background: rgb(199,17,55)">
              <span>总订单返利</span>
            </div>
            <div class="dash-card-value">
              <span>{{ parseFloat(totalInterestAmt)/100 }}</span>
            </div>
          </div>

          <div class="dash-card-box">
            <div class="demo-color-box" style="background: rgb(199,17,55)">
              <span>总订单返本</span>
            </div>
            <div class="dash-card-value">
              <span>{{ parseFloat(totalPrincipalAmt)/100 }}</span>
            </div>
          </div>
        </div>

        <div class="dash-card-group">
          <div class="dash-card-box">
            <div class="demo-color-box" style="background: rgb(199,17,55)">
              <span>今日订单用户</span>
            </div>
            <div class="dash-card-value">
              <span>{{ todayOrderUsers }}</span>
            </div>
          </div>

          <div class="dash-card-box">
            <div class="demo-color-box" style="background: rgb(199,17,55)">
              <span>今日订单</span>
            </div>
            <div class="dash-card-value">
              <span>{{ todayOrderCount }}</span>
            </div>
          </div>

          <div class="dash-card-box">
            <div class="demo-color-box" style="background: rgb(199,17,55)">
              <span>今日订单金额</span>
            </div>
            <div class="dash-card-value">
              <span>{{ parseFloat(todayOrderAmt)/100 }}</span>
            </div>
          </div>

          <div class="dash-card-box">
            <div class="demo-color-box" style="background: rgb(199,17,55)">
              <span>今日订单返利</span>
            </div>
            <div class="dash-card-value">
              <span>{{ parseFloat(todayInterestAmt)/100 }}</span>
            </div>
          </div>

          <div class="dash-card-box">
            <div class="demo-color-box" style="background: rgb(199,17,55)">
              <span>今日订单返本</span>
            </div>
            <div class="dash-card-value">
              <span>{{ parseFloat(todayPrincipalAmt)/100 }}</span>
            </div>
          </div>
        </div>

        <div class="dash-card-group">
          <div class="dash-card-box">
            <div class="demo-color-box" style="background: rgb(224, 33, 187)">
              <span>赠送订单</span>
            </div>
            <div class="dash-card-value">
              <span>{{ totalRewardProductCount }}</span>
            </div>
          </div>

          <div class="dash-card-box">
            <div class="demo-color-box" style="background: rgb(224, 33, 187)">
              <span>赠送订单金额</span>
            </div>
            <div class="dash-card-value">
              <span>{{ parseFloat(totalRewardProductAmt)/100 }}</span>
            </div>
          </div>

          <div class="dash-card-box">
            <div class="demo-color-box" style="background: rgb(224, 33, 187)">
              <span>手工加款金额</span>
            </div>
            <div class="dash-card-value">
              <span>{{ parseFloat(totalSalarySubsidyBonusAmt)/100 }}</span>
            </div>
          </div>

          <div class="dash-card-box">
            <div class="demo-color-box" style="background: rgb(224, 33, 187)">
              <span>手工减款金额</span>
            </div>
            <div class="dash-card-value">
              <span>{{ parseFloat(totalManualAdjustmentAmt)/100  }}</span>
            </div>
          </div>

          <div class="dash-card-box">
            <div class="demo-color-box" style="background: rgb(224, 33, 187)">
              <span>注册奖励金额</span>
            </div>
            <div class="dash-card-value">
              <span>{{ parseFloat(totalRegisterRewardAmt)/100 }}</span>
            </div>
          </div>

          <div class="dash-card-box">
            <div class="demo-color-box" style="background: rgb(224, 33, 187)">
              <span>首单奖励金额</span>
            </div>
            <div class="dash-card-value">
              <span>{{ parseFloat(totalFirstPurchaseRewardAmt)/100 }}</span>
            </div>
          </div>
        </div>

        <div class="dash-card-group">
          <div class="dash-card-box">
            <div class="demo-color-box" style="background: rgb(13, 217, 189)">
              <span>总返佣金额</span>
            </div>
            <div class="dash-card-value">
              <span>{{ parseFloat(totalRewardAmt)/100 }}</span>
            </div>
          </div>
          <div class="dash-card-box">
            <div class="demo-color-box" style="background: rgb(13, 217, 189)">
              <span>今日返佣金额</span>
            </div>
            <div class="dash-card-value">
              <span>{{ parseFloat(todayRewardAmt)/100 }}</span>
            </div>
          </div>
          <div class="dash-card-box">
            <div class="demo-color-box" style="background: rgb(13, 217, 189)">
              <span>总签到奖励</span>
            </div>
            <div class="dash-card-value">
              <span>{{ parseFloat(totalSignInRewardAmt)/100 }}</span>
            </div>
          </div>
          <div class="dash-card-box">
            <div class="demo-color-box" style="background: rgb(13, 217, 189)">
              <span>今日签到奖励</span>
            </div>
            <div class="dash-card-value">
              <span>{{ parseFloat(todaySignInRewardAmt)/100 }}</span>
            </div>
          </div>
        </div>

        <div class="dash-card-group">
          <div class="dash-card-box">
            <div class="demo-color-box" style="background: rgb(230, 162, 60)">
              <span>增值宝总余额</span>
            </div>
            <div class="dash-card-value">
              <span>{{ parseFloat(assetTotalBalance)/100 }}</span>
            </div>
          </div>
          <div class="dash-card-box">
            <div class="demo-color-box" style="background: rgb(230, 162, 60)">
              <span>增值宝总利息</span>
            </div>
            <div class="dash-card-value">
              <span>{{ parseFloat(totalTreasureRewardAmt)/100 }}</span>
            </div>
          </div>
          <div class="dash-card-box">
            <div class="demo-color-box" style="background: rgb(230, 162, 60)">
              <span>增值宝今日利息</span>
            </div>
            <div class="dash-card-value">
              <span>{{ parseFloat(todayTreasureRewardAmt)/100 }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

</template>

<script>

import { list } from "@/api/monitor/operlog";
import { getTotalDataByFrist ,getTotalDataSecond,getTodayData,getTotalData} from "@/api/login";
import {parseTime} from "../utils/ruoyi";

export default {
  name: 'Index',
  dicts: ['sys_oper_type', 'sys_common_status'],
  data() {
    let now = new Date();
    let sevenHoursAgo = now.getTime();
    const domainHost = location.hostname;
    if (domainHost.includes('mgt.ifund')||domainHost.includes('mgt.exfdngn')||domainHost.includes('mgt.fdngn')||domainHost.includes('mgt.bainbc')){
      sevenHoursAgo = now.getTime() - 7 * 60 * 60 * 1000;
    }
    let date = new Date(sevenHoursAgo);
    let year = date.getFullYear();
    let month = String(date.getMonth() + 1).padStart(2, '0');
    let day = String(date.getDate()).padStart(2, '0');
    const todayInfo = `${year}-${month}-${day}`;

    return {
      dateRange: [todayInfo,todayInfo],
      showSearch: true,
      loading: false,
      total: 0,
      list: [],
      queryParams: {
      },
      totalRechargeCount:0,
      totalOrderCount:0,
      totalWithdrawCount:0,
      totalRewardProductCount:0,
      todayRechargeCount:0,
      todayOrderCount:0,
      todayWithdrawCount:0,
      todayRechargeUsers:0,
      todayOrderUsers:0,
      todayWithdrawUsers:0,
      totalRechargeUsers:0,
      totalOrderUsers:0,
      totalWithdrawUsers:0,
      totalPeople:0,
      totalBalance:0,
      assetTotalBalance:0,
      totalRechargeAmt:0,
      totalWithdrawAmt:0,
      totalOrderAmt:0,
      totalRewardAmt:0,
      totalInterestAmt:0,
      totalPrincipalAmt:0,
      totalRewardProductAmt:0,
      totalRegisterRewardAmt:0,
      totalSalarySubsidyBonusAmt:0,
      totalManualAdjustmentAmt:0,
      totalTreasureRewardAmt:0,
      totalFirstPurchaseRewardAmt:0,
      todayRechargeAmt:0,
      todayWithdrawAmt:0,
      todayOrderAmt:0,
      todayRewardAmt:0,
      todayInterestAmt:0,
      todayPrincipalAmt:0,
      todayTreasureRewardAmt:0,
      todayRegisterUsers:0,
      todayActiveUsers:0,
      todayFristRechargeUsers:0,
      todaySignInRewardAmt:0,
      totalSignInRewardAmt:0,
      agentBalance:0,
    }
  },
  created() {
    this.getData();
  },
  methods: {
    parseTime,
    formatDate(val) {
      const date = new Date(val)
      const options = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric', time: 'long' };
      return date.toLocaleDateString(undefined, options) + " " + date.toLocaleTimeString(undefined)
    },
    handleQuery() {
      this.getData();
    },
    getList() {
      this.loading = true;
      list(this.addDateRange(this.queryParams, this.dateRange)).then( response => {
          this.list = response.rows;
          this.total = response.total;
          this.loading = false;
        }
      );
    },
    getData() {
      this.loading = true;
      getTodayData(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.todayRechargeCount=response.todayRechargeCount
        this.todayOrderCount=response.todayOrderCount
        this.todayWithdrawCount=response.todayWithdrawCount
        this.todayRechargeUsers=response.todayRechargeUsers
        this.todayOrderUsers=response.todayOrderUsers
        this.todayWithdrawUsers=response.todayWithdrawUsers
        this.todayRechargeAmt=response.todayRechargeAmt
        this.todayWithdrawAmt=response.todayWithdrawAmt
        this.todayOrderAmt=response.todayOrderAmt
        this.todayRewardAmt=response.todayRewardAmt
        this.todayInterestAmt=response.todayInterestAmt
        this.todayPrincipalAmt=response.todayPrincipalAmt
        this.todayTreasureRewardAmt=response.todayTreasureRewardAmt
        this.todayRegisterUsers=response.todayRegisterUsers
        this.todayActiveUsers=response.todayActiveUsers
        this.todayFristRechargeUsers=response.todayFristRechargeUsers
        this.todaySignInRewardAmt=response.todaySignInRewardAmt
        this.loading = false;
      });
      getTotalData(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.totalPeople=response.totalPeople
        this.totalBalance=response.totalBalance
        this.assetTotalBalance=response.assetTotalBalance

      });
      getTotalDataByFrist(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.totalRechargeAmt=response.totalRechargeAmt
        this.totalWithdrawAmt=response.totalWithdrawAmt
        this.totalOrderAmt=response.totalOrderAmt
        this.totalRewardAmt=response.totalRewardAmt
        this.totalInterestAmt=response.totalInterestAmt
        this.totalPrincipalAmt=response.totalPrincipalAmt
        this.totalRewardProductAmt=response.totalRewardProductAmt
        this.totalRegisterRewardAmt=response.totalRegisterRewardAmt
        this.totalSalarySubsidyBonusAmt=response.totalSalarySubsidyBonusAmt
        this.totalManualAdjustmentAmt=response.totalManualAdjustmentAmt
        this.totalTreasureRewardAmt=response.totalTreasureRewardAmt
        this.totalFirstPurchaseRewardAmt=response.totalFirstPurchaseRewardAmt
        this.totalSignInRewardAmt=response.totalSignInRewardAmt
        this.agentBalance=response.agentBalance

      });

      getTotalDataSecond(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.totalRechargeCount=response.totalRechargeCount
        this.totalOrderCount=response.totalOrderCount
        this.totalWithdrawCount=response.totalWithdrawCount
        this.totalRewardProductCount=response.totalRewardProductCount
        this.totalRechargeUsers=response.totalRechargeUsers
        this.totalOrderUsers=response.totalOrderUsers
        this.totalWithdrawUsers=response.totalWithdrawUsers
      });
    }
  }
}
</script>

<style lang="scss" scoped>
.main {
  display: flex;
  .main-content {
    flex-grow: 1;
  }
  .recent-actions {
    min-width: 350px;
  }
}
@media screen and (max-width: 640px) {
  .main {
    flex-direction: column;
  }
}
.dashboard-editor-container {
  padding: 32px;
  background-color: rgb(240, 242, 245);
  position: relative;

  .chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 32px;
  }
}

.demo-color-box {
  width: 200px;
  position: relative;
  height: 30px;
  boxSizing: border-box;
  color: var(--el-color-white);
  fontSize: 14px;
}

.dash-card-group {
  display: flex;
  flex-wrap: wrap;
  justify-items: center;
}

.dash-card-box {
  width: 200px;
  border-radius: 10px;
  margin: 10px;
  color: #fff;
  font-size: 14px;
  line-height: 2;
  text-align: center;
}

.dash-card-value {
  height: 30px;
  background: #ecf5ff;
  color: #409EFF;
}

.el-card {
  border: 1px solid #EBEEF5;
  background-color: #FFF;
  color: #303133;
  -webkit-transition: .3s;
  transition: .3s;
  border-radius: 4px;
  overflow: hidden;
}

@media (max-width: 1024px) {
  .chart-wrapper {
    padding: 8px;
  }
}
</style>
