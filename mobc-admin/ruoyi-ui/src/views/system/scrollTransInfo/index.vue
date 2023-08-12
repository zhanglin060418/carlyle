<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="用户账户" prop="userName">
        <el-input
          v-model="queryParams.userName"
          placeholder="请输入用户账户"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="交易类型" prop="transType">
        <el-select
          v-model="queryParams.transType"
          placeholder="请输入交易类型"
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

      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:scrollTransInfo:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:scrollTransInfo:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:scrollTransInfo:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:scrollTransInfo:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="scrollTransInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="交易编号" align="center" prop="transId" />
      <el-table-column label="用户账户" align="center" prop="userName" />
      <el-table-column label="交易类型" align="center" prop="transType" >
        <template slot-scope="scope">
          <span v-if="scope.row.transType == 'Recharge'" >充值</span>
          <span v-if="scope.row.transType == 'Withdraw'" >提现</span>
          <span v-else-if="scope.row.transType == 'Purchase'" >购买产品</span>
        </template>
      </el-table-column>
      <el-table-column label="交易金额" align="center" prop="amount" />
      <el-table-column label="创建时间" align="center" prop="createTime" />
      <el-table-column label="更新时间" align="center" prop="updateTime" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:scrollTransInfo:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:scrollTransInfo:remove']"
          >删除</el-button>
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

    <!-- 添加或修改滚屏交易信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户账户" prop="userName">
          <el-input v-model="form.userName" placeholder="请输入用户账户" />
        </el-form-item>
        <el-form-item label="交易类型" prop="transType">
          <el-select
            size="mini"
            v-model="form.transType"
            placeholder="交易类型"
          >
            <el-option
              v-for="(item, index) in transTypeOptions"
              :key="index"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="交易金额" prop="amount">
          <el-input v-model="form.amount" placeholder="请输入交易金额" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
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
import { listScrollTransInfo, getScrollTransInfo, delScrollTransInfo, addScrollTransInfo, updateScrollTransInfo } from "@/api/system/scrollTransInfo";

export default {
  name: "ScrollTransInfo",
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
      // 滚屏交易信息表格数据
      scrollTransInfoList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userName: null,
        transType: null,
        amount: null,
        createTime: null,
        updateTime: null,
        remark: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      },
      transTypeOptions: [
        {
          "label": "充值",
          "value": "Recharge"
        },{
          "label": "提现",
          "value": "Withdraw"
        }, {
          "label": "购买产品",
          "value": "Purchase"
        }],
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询滚屏交易信息列表 */
    getList() {
      this.loading = true;
      listScrollTransInfo(this.queryParams).then(response => {
        this.scrollTransInfoList = response.rows;
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
        transId: null,
        userName: null,
        transType: null,
        amount: null,
        createTime: null,
        updateTime: null,
        remark: null
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
      this.ids = selection.map(item => item.transId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加滚屏交易信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const transId = row.transId || this.ids
      getScrollTransInfo(transId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改滚屏交易信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.transId != null) {
            updateScrollTransInfo(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addScrollTransInfo(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const transIds = row.transId || this.ids;
      this.$modal.confirm('是否确认删除滚屏交易信息编号为"' + transIds + '"的数据项？').then(function() {
        return delScrollTransInfo(transIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/scrollTransInfo/export', {
        ...this.queryParams
      }, `scrollTransInfo_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
