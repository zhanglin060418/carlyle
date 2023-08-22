<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="用戶账号" prop="userName">
        <el-input
          v-model="queryParams.userName"
          placeholder="请输入用戶账号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="订单号" prop="requestNo">
        <el-input
          v-model="queryParams.requestNo"
          placeholder="请输入订单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="通道订单号" prop="orderNo">
        <el-input
          v-model="queryParams.orderNo"
          placeholder="请输入通道订单号"
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
      <el-form-item label="状态" prop="status">
        <el-select
          size="mini"
          v-model="queryParams.status"
          placeholder="状态"
          clearable
          @keyup.enter.native="handleQuery"
        >
          <el-option
            v-for="(item, index) in statusOptions"
            :key="index"
            :label="item.label"
            :value="item.value"
            :disabled="item.disabled">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间">
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

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:recharge:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="rechargeList" @selection-change="handleSelectionChange">
      <el-table-column label="订单号" align="center" prop="requestNo" />
      <el-table-column label="通道订单号" align="center" prop="orderNo" />
      <el-table-column label="用户账户" align="center" prop="userName"/>
      <el-table-column label="业务员" align="center"prop="topName" v-if= "$auth.hasPermi('system:user:edit')"/>
      <el-table-column label="经理"  align="center" prop="managerName" v-if= "$auth.hasPermi('system:manager:edit')"/>
      <el-table-column label="代理商"  align="center" prop="agentName" v-if= "$auth.hasPermi('system:agent:edit')"/>
      <el-table-column label="充值金额" align="center" prop="amount">
        <template slot-scope="scope">
          {{ parseFloat(scope.row.amount) / 100 }}
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" >
        <template slot-scope="scope">
          <span v-if="scope.row.status == 'Pending'" style="color: #ffba00">待支付</span>
          <span v-else-if="scope.row.status == 'Completed'" style="color: #71e2a3">已完成</span>
          <span v-else>待办的</span>
        </template>
      </el-table-column>

      <el-table-column label="创建时间" align="center" prop="createTime" />
      <el-table-column label="更新时间" align="center" prop="orderDate"/> >
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:recharge:query']"
          >查看</el-button>

          <el-button
            v-if="environment=='TEST'&&scope.row.status!='Completed'"
            size="mini"
            type="text"
            icon="el-icon-money"
            @click="handleStatus(scope.row,'Approve')"
            v-hasPermi="['system:recharge:query']"
          >处理</el-button>
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

    <!-- 添加或修改充值对话框 -->
    <el-dialog :title="title" :close-on-click-modal="false" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col span="12">
            <el-form-item label="用户编号" prop="userName">
              <el-input size="mini" v-model="form.userName" placeholder="用户编号" />
            </el-form-item>
          </el-col>
          <el-col span="12">
            <el-form-item label="充值金额" prop="amount">
              <el-input size="mini" v-model="form.amount/100" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
        </el-row>
        <!--<el-form-item label="卡片ID" prop="cardId">
          <el-input size="mini" v-model="form.cardId" placeholder="请输入卡片ID" />
        </el-form-item>-->
        <el-form-item label="订单号" prop="orderNo">
          <el-input size="mini" v-model="form.orderNo" placeholder="请输入订单号" />
        </el-form-item>
        <el-form-item label="请求流水号" prop="requestNo">
          <el-input size="mini" v-model="form.requestNo" placeholder="请输入请求流水号" />
        </el-form-item>
        <el-form-item label="支付信息网址" prop="payInfoUrl">
          <el-input size="mini" v-model="form.payInfoUrl" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="创建时间" prop="createTime">
          <el-input size="mini" v-model="form.createTime"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listRecharge, getRecharge, delRecharge, addRecharge, updateRecharge,handleChangeStatus } from "@/api/system/recharge";
import UserInfo from "@/components/UserInfo/index.js";
import CardInfo from "@/components/CardInfo/index.js";

export default {
  name: "Recharge",
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
      amountCount: 0,
      environment:'',
      // 充值表格数据
      rechargeList: [],
      dateRange: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        status: null,
        orderNo: null,
        requestNo: null,
        userName: null,
        topName: null,
        managerName: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        userId: [
          { required: true, message: "用戶ID不能为空", trigger: "blur" }
        ],
        amount: [
          { required: true, message: "充值金额不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "$comment不能为空", trigger: "change" }
        ],
        cardId: [
          { required: true, message: "$comment不能为空", trigger: "blur" }
        ],
      },
      statusOptions: [
        {
          "label": "已完成",
          "value": "Completed"
        },{
        "label": "待支付",
        "value": "Pending"
      }, {
        "label": "失败",
        "value": "Failed"
      }],
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询充值列表 */
    getList() {
      this.loading = true;
      listRecharge(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.rechargeList = response.rows;
        this.total = response.total;
        this.amountCount = response.amountCount;
        this.environment = response.environment;
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
        rechargeId: null,
        userId: null,
        amount: null,
        status: null,
        createTime: null,
        cardId: null,
        orderNo: null,
        payInfoUrl: null,
        requestNo: null,
        orderDate: null
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
    },

    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加充值";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const rechargeId = row.rechargeId || this.ids
      getRecharge(rechargeId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "查看充值";
      });
    },
    handleGetUser(userId) {
      return UserInfo.open({ userId })
    },
    handleGetCard(cardId) {
      return CardInfo.open({ cardId })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.rechargeId != null) {
            updateRecharge(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addRecharge(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },


    handleStatus(row, str) {
      if (str == 'Approve') {
        this.$modal.confirm('您确定要完成这笔充值吗？').then(function () {
          return handleChangeStatus(row)
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("成功");
        }).catch(() => {
        });
        return
      }
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/recharge/export', {
        ...this.queryParams
      }, `recharge_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
