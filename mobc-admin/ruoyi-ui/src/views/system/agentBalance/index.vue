<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:agentBalance:edit']"
        >预付款处理</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>


    <el-table v-loading="loading" :data="balanceList" @selection-change="handleSelectionChange">
      <el-table-column fixed type="selection" width="55" align="center" />
      <el-table-column label="代理商账号" align="center" prop="agentName"  >
        <template slot-scope="scope">
         {{ scope.row.agentName }}
        </template>
      </el-table-column>

      <el-table-column label="预付款金额" align="center" prop="prechargeAmt">
        <template slot-scope="scope">
          {{ parseFloat(scope.row.prechargeAmt/100).toFixed(2)}}
        </template>
      </el-table-column>

       <el-table-column label="可提现余额" align="center" prop="availableAmt">
        <template slot-scope="scope">
          {{ parseFloat(scope.row.availableAmt/100).toFixed(2)}}
        </template>
      </el-table-column>
<!--      <el-table-column label="在途金额（传输中）" align="center" prop="transitAmt">-->
<!--        <template slot-scope="scope">-->
<!--          {{ parseFloat(scope.row.transitAmt) / 100 }}-->
<!--        </template>-->
<!--      </el-table-column>-->

<!--      <el-table-column label="总额" align="center" prop="balance">-->
<!--        <template slot-scope="scope">-->
<!--          {{ parseFloat(scope.row.balance) / 100 }}-->
<!--        </template>-->
<!--      </el-table-column>-->

      <el-table-column label="最后更新时间" align="center" prop="updateAt">
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
    <el-dialog v-loading="loading" :title="title" :close-on-click-modal="false" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="150px">
        <el-form-item label="用户" prop="username">
          <el-input v-model="form.agentName" readonly="readonly"/>
        </el-form-item>
        <el-form-item label="当前预付款金额" prop="balance">
          <el-input v-model="form.prechargeAmt"  readonly="readonly" />
        </el-form-item>
        <el-form-item label="新增金额" prop="amount">
          <el-input v-model="form.amount" type ="number" placeholder="请输入金额" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listAgentBalance, getAgentBalance, updateAgentBalance } from "@/api/system/agentBalance";
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
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        agentId: [
          { required: true, message: "用户ID不能为空", trigger: "blur" }
        ],
        amount: [
          { required: true, message: "金额不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询用户余额列表 */
    getList() {
      this.loading = true;
      listAgentBalance(this.queryParams).then(response => {
        this.balanceList = response.rows;
        console.log(this.balanceList)
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
        id: null,
        agentId: null,
        prechargeAmt: null,
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
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      if(id == '') {
        this.$modal.msgWarning('请勾选数据');
        return
      }
      getAgentBalance(id).then(response => {
        this.form = response.data;
        this.form.prechargeAmt = this.form.prechargeAmt/100
        this.open = true;
        this.title = "预付款处理";
      });
    },
    handleGetUser(agentId) {
      return UserInfo.open({ agentId })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            this.loading = true
            updateAgentBalance(this.form).then(response => {
              this.loading = false
              this.$modal.msgSuccess("处理成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    }
  }
};
</script>
