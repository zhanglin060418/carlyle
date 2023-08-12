<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="用户" prop="username">
        <el-input
          v-model="queryParams.username"
          placeholder="请输入用户"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户级别" prop="level">
        <el-select
          size="mini"
          v-model="queryParams.level"
          placeholder="交易类型"
          clearable
          @keyup.enter.native="handleQuery"
        >
          <el-option
            v-for="(item, index) in levelOptions"
            :key="index"
            :label="item.label"
            :value="item.value"
            :disabled="item.disabled">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="交易类型" prop="transactionType">
        <el-select
          size="mini"
          v-model="queryParams.transactionType"
          placeholder="交易类型"
          clearable
          @keyup.enter.native="handleQuery"
        >
          <el-option
            v-for="(item, index) in transTypeOptions"
            :key="index"
            :label="item.label"
            :value="item.value"
            :disabled="item.disabled">
          </el-option>
        </el-select>
      </el-form-item>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:team:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="clerkTeamTransList">
      <el-table-column label="用户" align="center" prop="username"/>
      <el-table-column label="订单数" align="center" prop="transCount"/>
      <el-table-column label="金额" align="center" prop="amount">
        <template slot-scope="scope">
          {{ scope.row.amount}}
        </template>
      </el-table-column>
      <el-table-column label="交易类型" align="center" prop="transactionType" >
        <template slot-scope="scope">
          <span v-if="scope.row.transactionType == 'Recharge'">充值</span>
          <span v-else-if="scope.row.transactionType == 'Buy_Product_Balance'">购买产品</span>
          <span v-else-if="scope.row.transactionType == 'Withdraw'">提现</span>
          <span v-else-if="scope.row.transactionType == 'Register_Reward'">注册奖励</span>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<script>
import { getClerkTeamTransInfo } from "@/api/system/teamRecharge";

export default {
  name: "History",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 【请填写功能名称】表格数据
      clerkTeamTransList: [],
      dateRange: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userName: null
      },
      // 表单参数
      form: {}
      ,
      transTypeOptions: [
        {
          "label": "充值",
          "value": "Recharge"
        },
        {
          "label": "购买产品",
          "value": "Buy_Product_Balance"
        },
        {
          "label": "提现",
          "value": "Withdraw"
        },
          {
          "label": "注册",
          "value": "Register_Reward"
        }],
      levelOptions: [
        {
          "label": "A级用户",
          "value": "A"
        },
        {
          "label": "B及用户",
          "value": "B"
        },
        {
          "label": "C级用户",
          "value": "C"
        }],
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询【请填写功能名称】列表 */
    getList() {
      this.loading = true;
      getClerkTeamTransInfo(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.clerkTeamTransList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
    },
    handleExport() {
      this.download('/system/team/export', {
        ...this.queryParams
      }, `TeamTransInfo_${new Date().getTime()}.xlsx`)
    }
  },
};
</script>
