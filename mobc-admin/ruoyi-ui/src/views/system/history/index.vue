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
      <el-form-item label="业务员" prop="topName" v-hasPermi="['system:user:edit']">
        <el-input
          v-model="queryParams.topName"
          placeholder="请输入业务员账号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="经理" prop="managerName" v-hasPermi="['system:manager:edit']">
        <el-input
          v-model="queryParams.managerName"
          placeholder="请输入经理账号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="代理商" prop="agentName" v-hasPermi="['system:agent:edit']">
        <el-input
          size="mini" v-model="queryParams.agentName"
          placeholder="请输入代理商账号"
          clearable
          @keyup.enter.native="handleQuery"
        />
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
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="交易时间">
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
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>

      <el-form-item style="margin-left: 20%">
        <span  style="font-size:large;color:#515a6e; font-weight:600">
            合计 : {{parseFloat(amountCount) / 100 }}
        </span>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="historyList" @selection-change="handleSelectionChange">
      <el-table-column label="用户账户" align="center" prop="username"/>
      <el-table-column label="业务员" align="center"prop="topName" v-if= "$auth.hasPermi('system:user:edit')"/>
      <el-table-column label="经理"  align="center" prop="managerName" v-if= "$auth.hasPermi('system:manager:edit')"/>
      <el-table-column label="代理商"  align="center" prop="agentName" v-if= "$auth.hasPermi('system:agent:edit')"/>
      <el-table-column label="交易类型" align="center" prop="transactionType" >
        <template slot-scope="scope">
          <span v-if="scope.row.transactionType == 'Recharge'">充值</span>
          <span v-else-if="scope.row.transactionType == 'Withdraw'">提现</span>
          <span v-else-if="scope.row.transactionType == 'Treasure_Reward'">增值宝利息</span>
          <span v-else-if="scope.row.transactionType == 'Product_Daily_Interest'">产品日收益</span>
          <span v-else-if="scope.row.transactionType == 'Principal_Return'">本金返还</span>
          <span v-else-if="scope.row.transactionType == 'Buy_Product_Balance'">余额购买产品</span>
          <span v-else-if="scope.row.transactionType == 'Child_First_Purchase_Reward'">下级首单奖励</span>
          <span v-else-if="scope.row.transactionType == 'Reward_Product'">赠送产品</span>
          <span v-else-if="scope.row.transactionType == 'Commission_A_Reward'">A级用户返佣</span>
          <span v-else-if="scope.row.transactionType == 'Commission_B_Reward'">B级用户返佣</span>
          <span v-else-if="scope.row.transactionType == 'Commission_C_Reward'">C级用户返佣</span>
          <span v-else-if="scope.row.transactionType == 'Treasure_Transfer_In'">增值宝转入</span>
          <span v-else-if="scope.row.transactionType == 'Treasure_Transfer_Out'">增值宝转出</span>
          <span v-else-if="scope.row.transactionType == 'Salary_Subsidy_Bonus'">工资/补贴/奖金</span>
          <span v-else-if="scope.row.transactionType == 'Register_Reward'">注册奖励</span>
          <span v-else-if="scope.row.transactionType == 'Manual_Adjustment'">手工调账</span>
          <span v-else-if="scope.row.transactionType == 'SignIn_Reward'">签到奖励</span>
          <span v-else-if="scope.row.transactionType == 'Manual_Reward_Product'">手工赠送产品</span>
        </template>
      </el-table-column>
      <el-table-column label="交易前可用余额" align="center" prop="amountBefore">
        <template slot-scope="scope">
          {{ parseFloat(scope.row.amountBefore)/100 || 0}}
        </template>
      </el-table-column>
      <el-table-column label="交易金额" align="center" prop="amount">
        <template slot-scope="scope">
          {{ parseFloat(scope.row.amount)/100 || 0}}
        </template>
      </el-table-column>
      <el-table-column label="交易后可用余额" align="center" prop="amountAfter">
        <template slot-scope="scope">
          {{ parseFloat(scope.row.amountAfter)/100 || 0}}
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="交易时间" align="center" prop="transactionDate">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.transactionDate) }}</span>
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
import { listHistory } from "@/api/system/history";
import UserInfo from "@/components/UserInfo";

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
      historyList: [],
      amountCount: 0,
      dateRange: [],
      // 弹出层标题
      title: "交易记录",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userId: null,
        username: null,
        transactionDate: null,
        transactionType: null,
        amount: 0,
        amountBefore: 0,
        amountAfter: 0
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        userId: [
          { required: true, message: "用户ID不能为空", trigger: "blur" }
        ],
        transactionDate: [
          { required: true, message: "交易时间不能为空", trigger: "blur" }
        ],

      },
      transTypeOptions: [
      {
        "label": "充值",
        "value": "Recharge"
      },{
        "label": "提现",
        "value": "Withdraw"
      }, {
        "label": "增值宝利息",
        "value": "Treasure_Reward"
      },{
          "label": "产品日收益",
          "value": "Product_Daily_Interest"
        },{
          "label": "本金返还",
          "value": "Principal_Return"
        }, {
          "label": "余额购买产品",
          "value": "Buy_Product_Balance"
        },{
          "label": "下级首单奖励",
          "value": "Child_First_Purchase_Reward"
        }, {
          "label": "赠送产品",
          "value": "Reward_Product"
        },{
          "label": "A级用户返佣",
          "value": "Commission_A_Reward"
        },{
          "label": "B级用户返佣",
          "value": "Commission_B_Reward"
        }, {
          "label": "C级用户返佣",
          "value": "Commission_C_Reward"
        },{
          "label": "增值宝转入",
          "value": "Treasure_Transfer_In"
        },{
          "label": "增值宝转出",
          "value": "Treasure_Transfer_Out"
        }, {
          "label": "注册奖励",
          "value": "Register_Reward"
        }, {
          "label": "工资/补贴/奖金",
          "value": "Salary_Subsidy_Bonus"
        },{
          "label": "手工调账",
          "value": "Manual_Adjustment"
        },{
          "label": "签到奖励",
          "value": "SignIn_Reward"
        },{
          "label": "手工赠送产品",
          "value": "Manual_Reward_Product"
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
      listHistory(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.historyList = response.rows;
        this.total = response.total;
        this.amountCount = response.amountCount;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        transactionHistoryId: null,
        userId: null,
        transactionDate: null,
        transactionType: null,
        amount: 0,
        remark: 0
      };
      this.resetForm("form");
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
    }
  }
};
</script>
