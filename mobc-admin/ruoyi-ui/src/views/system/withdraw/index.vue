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
      <el-form-item label="业务员" prop="topName">
        <el-input
          size="mini" v-model="queryParams.topName"
          placeholder="请输入业务员账号"
          clearable
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="经理" prop="managerName" v-hasPermi="['system:manager:edit']">
        <el-input
          size="mini" v-model="queryParams.managerName"
          placeholder="请输入经理账号"
          clearable
          style="width: 240px"
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
      <el-form-item label="收款账号" prop="cardNo">
        <el-input
          v-model="queryParams.cardNo"
          placeholder="收款账号"
          clearable
          @keyup.enter.native="handleQuery"
        />
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
          type="success"
          plain
          size="mini"
          :disabled="disabled"
          @click="handleUpdateBatch('Approve')"
          v-hasPermi="['system:withdraw:postBatchWithdraw']"
          v-loading.fullscreen.lock="fullscreenLoading"
        >批量审核通过</el-button>
      </el-col>

      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:withdraw:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

<!--    </el-row>-->
    <el-table v-loading="loading" :data="withdrawList" @selection-change="handleSelectionChange">
      <el-table-column fixed type="selection"  width="50" align="center" :selectable="selectable" />
      <el-table-column label="订单号" align="center" prop="requestNo" />
      <el-table-column label="通道订单号" align="center" prop="orderNo" />
      <el-table-column label="用户" align="center" prop="userName"/>
      <el-table-column label="业务员" align="center"prop="topName" v-if= "$auth.hasPermi('system:user:edit')"/>
      <el-table-column label="经理"  align="center" prop="managerName" v-if= "$auth.hasPermi('system:manager:edit')"/>
      <el-table-column label="代理商"  align="center" prop="agentName" v-if= "$auth.hasPermi('system:agent:edit')"/>
      <el-table-column label="提现金额" align="center" >
        <template slot-scope="scope">
          {{ parseFloat(scope.row.amount) / 100+ parseFloat(scope.row.fee) / 100 }}
        </template>
      </el-table-column>
      <el-table-column label="到账金额" align="center" prop="amount">
        <template slot-scope="scope">
          {{ parseFloat(scope.row.amount) / 100 }}
        </template>
      </el-table-column>
      <el-table-column label="手续费" align="center" prop="fee">
        <template slot-scope="scope">
          {{ parseFloat(scope.row.fee) / 100 }}
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" >
        <template slot-scope="scope">
          <span v-if="scope.row.status == 'Pending'" style="color: #093bf6">待审核</span>
          <span v-if="scope.row.status == 'In progress'" style="color: #ffba00">出款中</span>
          <span v-else-if="scope.row.status == 'Completed'" style="color: #71e2a3">已完成</span>
          <span v-else-if="scope.row.status == 'Failed'" style="color: #fc0000">异常</span>
          <span v-else-if="scope.row.status == 'Declined'" style="color: #fe5900">审核不通过</span>
        </template>
      </el-table-column>
      <el-table-column label="银行名" align="center" prop="bankName" />
      <el-table-column label="收款用户名" align="center" prop="cardName" />
      <el-table-column label="收款号码" align="center" prop="cardNo">
      <template slot-scope="scope">
        <span v-if="scope.row.cardNo.length != 10" style="color: #fc0000">{{scope.row.cardNo}}</span>
        <span v-else-if="scope.row.cardNo.length == 10" >{{scope.row.cardNo}}</span>
      </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" />
      <el-table-column label="更新时间" align="center" prop="orderTime" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:withdraw:query']"
          >操作</el-button>
          <el-button
            v-if="scope.row.status == 'Pending'"
            size="mini"
            type="text"
            icon="el-icon-money"
            @click="handleStatus(scope.row,'Approve')"
            v-hasPermi="['system:withdraw:changeStatus']"
          >批准</el-button>

          <el-button
            v-if="scope.row.status == 'Failed'"
            size="mini"
            type="text"
            icon="el-icon-money"
            @click="handleStatus(scope.row,'Retry')"
            v-hasPermi="['system:withdraw:changeStatus']"
          >重试</el-button>
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

    <!-- 添加或修改提款对话框 -->
    <el-dialog :title="title" :close-on-click-modal="false" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12" >
            <el-form-item label="用户" prop="userName">
              <el-input size="mini" v-model="form.userName" readonly="readonly" placeholder="请输入用户ID" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="创建时间"  readonly="readonly" prop="createTime">
              <span>{{ parseTime(form.createTime) }}</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="提现总额" >
              <el-input size="mini" v-model="form.amount+form.fee"  readonly="readonly" placeholder="提现总额" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select
                size="mini"
                v-model="form.status"
                placeholder="状态"
                :disabled="!form.visible"
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
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="到账金额" prop="amount">
              <el-input size="mini" v-model="form.amount"  readonly="readonly" placeholder="到账金额" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
          <el-form-item label="手续费" prop="fee">
            <el-input size="mini" v-model="form.fee" readonly="readonly" placeholder="手续费" />
          </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="银行名称" prop="bankName">
              <el-input size="mini" v-model="form.bankName" readonly="readonly" placeholder="银行名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="银行代码" prop="bankCode">
              <el-input size="mini" v-model="form.bankCode" readonly="readonly" placeholder="银行代码" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="收款账户名" prop="cardName">
              <el-input size="mini" v-model="form.cardName"  readonly="readonly" placeholder="收款账户名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="收款账号" prop="cardNo">
              <el-input size="mini" v-model="form.cardNo" readonly="readonly" placeholder="收款账号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="银行返回" prop="respDesc">
              <el-input size="mini" readonly="readonly" v-model="form.respDesc" type="textarea" rows="3"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="备注" prop="remark">
              <el-input size="mini" v-model="form.remark" type="textarea" rows="3"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import {
  listWithdraw,
  getWithdraw,
  postBatchWithdraw,
  delWithdraw,
  addWithdraw,
  updateWithdraw,
  handleChangeStatus,
  handleChangeStatusRetry
} from "@/api/system/withdraw";
import UserInfo from "@/components/UserInfo";
import CardInfo from "@/components/CardInfo";
import {changeUserStatus} from "@/api/system/user";
import {parseTime} from "../../../utils/ruoyi";

export default {
  name: "Withdraw",
  data() {
    return {
      // 遮罩层
      loading: false,
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
      // 提款表格数据
      withdrawList: [],
      amountCount: 0,
      dateRange: [],
      // 弹出层标题
      title: "",
      fullscreenLoading:false,
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        bankName: null,
        orderNo: null,
        requestNo: null,
        status: null,
        cardNo:null,
        userName:null,
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
        cardId: [
          { required: true, message: "银行卡ID不能为空", trigger: "blur" }
        ],
        amount: [
          { required: true, message: "代付金额不能为空", trigger: "blur" }
        ],
      },
      statusOptions: [{
        "label": "待审核",
        "value": "Pending"
      }, {
        "label": "已完成",
        "value": "Completed"
      }, {
        "label": "出款中",
        "value": "In progress"
      }, {
        "label" : "审核不通过",
        "value" : "Declined"
      }, {
        "label": "异常",
        "value": "Failed"
      }],
    };
  },
  created() {
    this.getList();
  },
  methods: {
    parseTime,
    /** 查询提款列表 */
    getList() {
      this.loading = true;
      listWithdraw(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.withdrawList = response.rows;
        this.total = response.total;
        this.amountCount = response.amountCount;
        this.loading = false;
      });
    },
    handleUpdateBatch(str) {
      this.fullscreenLoading = true;
      var toAprproveids = this.ids;
      if (str == 'Approve') {
        if(this.ids==false||this.ids.length == 0){
          this.$modal.msg("请选择数据");
          return;
        }else{
            this.$modal.confirm('您确定要批准这些提款申请吗？').then(function () {
              return postBatchWithdraw(toAprproveids)
          }).then(() => {
            this.fullscreenLoading = false;
            this.getList();
            this.$modal.msgSuccess("成功");
          }).catch(() => {
              this.fullscreenLoading = false;
          });
        }
      }
      return
    },
    //复选框禁用
    selectable(row,rowIndex) {
      if(row.status == 'Pending') {
        return true;
      }else{
        return false;
      }
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        withdrawId: null,
        userId: null,
        cardId: null,
        createTime: null,
        amount: null,
        fee:null,
        bankCode: null,
        beneficiaryName: null,
        bankName: null,
        cardNo: null,
        beneficiaryNo: null,
        respDesc: null,
        remark: null,
        beneficiaryMobile: null,
        beneficiaryEmail: null
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
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.withdrawId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加提款";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const withdrawId = row.withdrawId || this.ids
      getWithdraw(withdrawId).then(response => {
        this.form = response.data;
        this.form.amount = this.form.amount/100
        this.form.fee = this.form.fee/100
        if(this.form.status == 'Pending' || this.form.status == 'Failed') {
          this.form.visible = true
        }
        else this.form.visible = false
        this.open = true;
        this.title = "修改提款";
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
          if (this.form.withdrawId != null) {
            updateWithdraw(this.form).then(response => {
              if(response.code == 200){
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              }else{
                this.$modal.msgWarning(response.msg)
              }

            });
          }
        }
      });
    },
    handleStatus(row, str) {
      if (str == 'Approve') {
        this.$modal.confirm('您确定要批准这项提款申请吗？').then(function () {
          return handleChangeStatus(row)
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("成功");
        }).catch(() => {
        });
        return
      } else {
        this.$modal.confirm('您确定要重试提款吗？').then(function () {
          return handleChangeStatusRetry(row)
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
      this.download('system/withdraw/export', {
        ...this.queryParams
      }, `withdraw_${new Date().getTime()}.xlsx`)
    },
    formatDate(val) {
      if (val == null)
        return
      const date = new Date(val)
      return date.toLocaleDateString("en-CA") + ' ' + date.toLocaleTimeString("en-US", {hour12: false})
    },
  }
};
</script>
