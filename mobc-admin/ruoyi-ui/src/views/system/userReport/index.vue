<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="用户" prop="userName">
        <el-input
          v-model="queryParams.userName"
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
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="userReportList">
      <el-table-column label="用户账户" align="center" prop="userName"/>
      <el-table-column label="推荐人" align="center"prop="parentUsername"/>
      <el-table-column label="业务员" align="center"prop="topName" v-if= "$auth.hasPermi('system:user:edit')"/>
      <el-table-column label="经理"  align="center" prop="managerName" v-if= "$auth.hasPermi('system:manager:edit')"/>
      <el-table-column label="代理商"  align="center" prop="agentName" v-if= "$auth.hasPermi('system:agent:edit')"/>
      <el-table-column label="账户总额" align="center" prop="balance">
        <template slot-scope="scope">
          {{ parseFloat(scope.row.balance)/100}}
        </template>
      </el-table-column>
      <el-table-column label="增值宝" align="center" prop="assetBalance">
        <template slot-scope="scope">
          {{ parseFloat(scope.row.assetBalance)/100}}
        </template>
      </el-table-column>
      <el-table-column label="可用余额" align="center" prop="availableAmt">
        <template slot-scope="scope">
          {{ parseFloat(scope.row.availableAmt)/100}}
        </template>
      </el-table-column>
      <el-table-column label="充值" align="center" prop="rechargeAmt">
        <template slot-scope="scope">
          {{ parseFloat(scope.row.rechargeAmt)/100}}
        </template>
      </el-table-column>
      <el-table-column label="提现" align="center" prop="withdrawAmt">
        <template slot-scope="scope">
          {{ parseFloat(scope.row.withdrawAmt)/100}}
        </template>
      </el-table-column>
      <el-table-column label="佣金" align="center" prop="rewardAmt">
        <template slot-scope="scope">
          {{ parseFloat(scope.row.rewardAmt)/100}}
        </template>
      </el-table-column>
      <el-table-column label="产品总额" align="center" prop="purchaseAmt">
        <template slot-scope="scope">
          {{ parseFloat(scope.row.purchaseAmt)/100}}
        </template>
      </el-table-column>
      <el-table-column label="赠送产品" align="center" prop="rewardProductAmt">
      <template slot-scope="scope">
        {{ parseFloat(scope.row.rewardProductAmt)/100}}
      </template>
    </el-table-column>
      <el-table-column label="未返本金额" align="center" prop="noBackProductAmt">
        <template slot-scope="scope">
          {{ parseFloat(scope.row.noBackProductAmt)/100}}
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
import { listUserReport } from "@/api/system/panUser";
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
      userReportList: [],
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

      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询【请填写功能名称】列表 */
    getList() {
      this.loading = true;
      listUserReport(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.userReportList = response.rows;
        this.total = response.total;
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
