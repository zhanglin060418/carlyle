<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="用户账户" prop="username">
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
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
      <el-form-item style="margin-left: 20%">
        <span  style="font-size:large;color:#515a6e; font-weight:600">
            合计 : {{parseFloat(totalBalance) / 100 }}
        </span>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:balance:edit']"
        >余额处理</el-button>
      </el-col>

      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          :disabled="single"
          @click="handleProductReward"
          v-hasPermi="['system:balance:productReward']"
        >产品赠送</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="balanceList" @selection-change="handleSelectionChange">
      <el-table-column fixed type="selection" width="55" align="center" />
      <el-table-column label="用户账户" align="center" prop="username"  >
        <template slot-scope="scope">
         {{ scope.row.username }}
        </template>
      </el-table-column>
      <el-table-column label="业务员" align="center"prop="topName" v-if= "$auth.hasPermi('system:user:edit')"/>
      <el-table-column label="经理"  align="center" prop="managerName" v-if= "$auth.hasPermi('system:manager:edit')"/>
      <el-table-column label="代理商"  align="center" prop="agentName" v-if= "$auth.hasPermi('system:agent:edit')"/>
      <el-table-column label="剩余抽奖次数" align="center" prop="drawsNumber"  />
      <el-table-column label="可提现余额" align="center" prop="availableAmt">
        <template slot-scope="scope">
          {{ parseFloat(scope.row.availableAmt) / 100 }}
        </template>
      </el-table-column>
      <el-table-column label="不可提现余额" align="center" prop="lockBalance">
        <template slot-scope="scope">
          {{ parseFloat(scope.row.lockBalance) / 100 }}
        </template>
      </el-table-column>
      <el-table-column label="在途金额（传输中）" align="center" prop="transitAmt">
        <template slot-scope="scope">
          {{ parseFloat(scope.row.transitAmt) / 100 }}
        </template>
      </el-table-column>
      <el-table-column label="返佣余额" align="center" prop="rewardAmt">
        <template slot-scope="scope">
          {{ parseFloat(scope.row.rewardAmt) / 100 }}
        </template>
      </el-table-column>

      <el-table-column label="增值宝余额" align="center" prop="assetBalance">
        <template slot-scope="scope">
          {{ parseFloat(scope.row.assetBalance) / 100 }}
        </template>
      </el-table-column>

      <el-table-column label="总额" align="center" prop="balance">
        <template slot-scope="scope">
          {{ parseFloat(scope.row.balance) / 100 }}
        </template>
      </el-table-column>

      <el-table-column label="最后更新" align="center" prop="updateAt">
        <template slot-scope="scope">
         {{scope.row.updateAt}}
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

    <!-- 添加或修改用户余额对话框 -->
    <el-dialog v-loading="loading"  :title="title" :close-on-click-modal="false" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="用户" prop="username">
          <el-input v-model="form.username" readonly="readonly"/>
        </el-form-item>
        <el-form-item label="当前可用余额" prop="balance">
          <el-input v-model="form.availableAmt"  readonly="readonly" />
        </el-form-item>
        <el-form-item label="账务类型" prop="billType">
          <el-select
            v-model="form.billType"
            placeholder="请输入账务类型"
            clearable
            @keyup.enter.native="handleQuery"
            style="width:100%">
            <el-option
              v-for="(item, index) in billTypeOptions"
              :key="index"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="交易类型" prop="transType">
          <el-select
            v-model="form.transType"
            placeholder="请选择交易类型"
            clearable
            @keyup.enter.native="handleQuery"
            style="width:100%">
            <el-option
              v-for="(item, index) in transTypeOptions"
              :key="index"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="调整金额" prop="amount">
          <el-input v-model="form.amount" type ="number" placeholder="请输入金额" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog v-loading="loading"  :title="title" :close-on-click-modal="false" :visible.sync="productRewardOpen" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="用户" prop="username">
          <el-input v-model="form.username" readonly="readonly"/>
        </el-form-item>
        <el-form-item label="赠送金额" prop="amount">
          <el-input v-model="form.amount" type ="number" placeholder="赠送金额" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitProductRewardForm">确 定</el-button>
        <el-button @click="cancelProductReward">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listBalance, getBalance, delBalance, productReward, updateBalance } from "@/api/system/balance";
import UserInfo from "@/components/UserInfo";

export default {
  name: "Balance",
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
      // 用户余额表格数据
      balanceList: [],
      totalBalance: 0,
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      productRewardOpen: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userName: null,
        topName: null,
        managerName: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        userId: [
          { required: true, message: "用户ID不能为空", trigger: "blur" }
        ],
        amount: [
          { required: true, message: "金额不能为空", trigger: "blur" }
        ],
        billType: [
          { required: true, message: "交易类型不能为空", trigger: "blur" }
        ]
      },
      billTypeOptions: [
        {
          "label": "增加",
          "value": "IN"
        },{
          "label": "减少",
          "value": "OUT"
        }],

      transTypeOptions: [
        {
          "label": "工资",
          "value": "Salary"
        },{
          "label": "补贴",
          "value": "Subsidy"
        },{
          "label": "奖金",
          "value": "Bonus"
        },{
          "label": "线下充值",
          "value": "Offline_Recharge"
        }],
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询用户余额列表 */
    getList() {
      this.loading = true;
      listBalance(this.queryParams).then(response => {
        this.balanceList = response.rows;
        console.log(this.balanceList)
        this.total = response.total;
        this.totalBalance = response.amountCount;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    cancelProductReward(){
      this.productRewardOpen = false;
    },
    // 表单重置
    reset() {
      this.form = {
        userBalanceId: null,
        userId: null,
        availableAmt: null,
        amount:null,
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
      this.resetForm("queryForm");
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.userBalanceId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加用户余额";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const userBalanceId = row.userBalanceId || this.ids
      if(userBalanceId == '') {
        this.$modal.msgWarning('当前用户没有充值过余额');
        return
      }
      getBalance(userBalanceId).then(response => {
        this.form = response.data;
        this.form.availableAmt = this.form.availableAmt/100
        this.open = true;
        this.title = "余额处理";
      });
    },
    handleProductReward(row){
      this.form = {
        userBalanceId: null,
        userId: null,
        amount:null,
      };
      const userBalanceId = row.userBalanceId || this.ids
      if(userBalanceId == '') {
        this.$modal.msgWarning('未获取到Id');
        return
      }
      getBalance(userBalanceId).then(response => {
        this.form = response.data;
        this.productRewardOpen = true;
        this.title = "产品赠送";
      });
    },
    handleGetUser(userId) {
      return UserInfo.open({ userId })
    },
    submitProductRewardForm(){
        if (this.form.userBalanceId != null) {
          if(this.form.amount==null){
            this.$modal.msgWarning('请输入金额');
            return
          }
          this.loading = true
          productReward(this.form).then(response => {
            this.loading = false
            this.$modal.msgSuccess("处理成功");
            this.productRewardOpen = false;
            this.getList();
          });
        }
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.userBalanceId != null) {
            if(this.form.billType==='OUT'){
              if(this.form.amount>this.form.availableAmt){
                this.$modal.msgWarning('可用余额不足');
                return;
              }
            }
            if(this.form.billType==='IN'){
              if(this.form.transType==''||this.form.transType==null){
                this.$modal.msgWarning('请选择交易类型!');
                return;
              }
            }
            this.loading = true
            updateBalance(this.form).then(response => {
              this.loading = false
              this.$modal.msgSuccess("处理成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      if(row.userBalanceId == null) {
        this.$modal.msgWarning('当前用户没有充值过余额');
        return
      }
      const userBalanceIds = row.userBalanceId || this.ids;
      this.$modal.confirm('您确定要初始化用户"' + row.username + '"的余额项吗？').then(function () {
        return delBalance(userBalanceIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/balance/export', {
        ...this.queryParams
      }, `balance_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
