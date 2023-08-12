<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="修改前卡号" prop="oldCardNo">
        <el-input
          v-model="queryParams.oldCardNo"
          placeholder="修改前卡号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="修改后卡号" prop="newCardNo">
        <el-input
          v-model="queryParams.newCardNo"
          placeholder="修改后卡号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="cardList" @selection-change="handleSelectionChange">
      <el-table-column label="ID" align="center" prop="id"/>
      <el-table-column label="用户账户" align="center" prop="userName" >
        <template slot-scope="scope">
          {{ scope.row.userName.substring(0, 3) }} ***** {{ scope.row.userName.substring(scope.row.userName.length-4, scope.row.userName.length) }}
        </template>
      </el-table-column>
      <el-table-column label="卡名" align="center" prop="name" />
      <el-table-column label="银行名称" align="center" prop="bankName" />
      <el-table-column label="修改前卡号" align="center" prop="oldCardNo"/>
      <el-table-column label="修改后卡号" align="center" prop="newCardNo"/>
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
import { listCardRecord } from "@/api/system/card";
import UserInfo from "@/components/UserInfo";

export default {
  name: "cardRecord",
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
      // 用户银行卡表格数据
      cardList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        newCardNo: null,
        oldCardNo: null,
        userName: null,
        topName: null,
        managerName: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        cardId: [
          { required: true, message: "用户ID不能为空", trigger: "blur" }
        ],
        name: [
          { required: true, message: "卡名不能为空", trigger: "blur" }
        ],
        bankCode: [
          { required: true, message: "用户ID不能为空", trigger: "blur" }
        ],
        cardNo: [
          { required: true, message: "帐户不能为空", trigger: "blur" }
        ],
        mobile: [
          { required: true, message: "手机不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询用户银行卡列表 */
    getList() {
      this.loading = true;
      listCardRecord(this.queryParams).then(response => {
        this.cardList = response.rows;
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
        userId: null,
        name: null,
        bankName: null,
        bankCode: null,
        cardNo: null,
        mobile: null
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

    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加用户银行卡";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getCard(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改用户银行卡";
      });
    },
    handleGetUser(userId) {
      return UserInfo.open({ userId })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateCard(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addCard(this.form).then(response => {
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
      const ids = row.id || this.ids;
      this.$modal.confirm('删除卡将删除与卡相关的所有活动（用户取款日志）。 '+ '确定要删除卡号为"'+ids+'"的数据项吗？').then(function() {
        return delCard(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/card/export', {
        ...this.queryParams
      }, `card_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
