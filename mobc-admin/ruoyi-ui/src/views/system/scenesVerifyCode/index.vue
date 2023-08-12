<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="用户账户" prop="phoneNo">
        <el-input
          v-model="queryParams.phoneNo"
          placeholder="请输入手机号码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="验证码" prop="verifyCode">
        <el-input
          v-model="queryParams.verifyCode"
          placeholder="请输入验证码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select
          v-model="queryParams.status"
          placeholder="请输入状态"
          clearable
          @keyup.enter.native="handleQuery"
        >
          <el-option
            v-for="(item, index) in statusOptions"
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

    <el-table v-loading="loading" :data="scenesVerifyCodeList" @selection-change="handleSelectionChange">
      <el-table-column label="手机号码" align="center" prop="phoneNo" />
      <el-table-column label="验证码" align="center" prop="verifyCode" />
      <el-table-column label="发送时间" align="center" prop="sendTime" />
      <el-table-column label="状态" align="center" prop="status" >
        <template slot-scope="scope">
          <span v-if="scope.row.status == 'Pending'" style="color: #ffba00">待使用</span>
          <span v-if="scope.row.status == 'Success'" style="color: #71e2a3">成功</span>
          <span v-else-if="scope.row.status == 'Failed'" style="color: #fc0000">失败</span>
        </template>
      </el-table-column>
      <el-table-column label="使用场景" align="center" prop="useScenes" >
        <template slot-scope="scope">
          <span v-if="scope.row.useScenes == 'register'" >注册</span>
          <span v-if="scope.row.useScenes == 'updatePayPwd'" >修改支付密码</span>
          <span v-else-if="scope.row.useScenes == 'forgetLoginPwd'" >修改登陆密码</span>
        </template>
      </el-table-column>
      <el-table-column label="使用时间" align="center" prop="useTime" />
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"/>
  </div>
</template>

<script>
import { listScenesVerifyCode } from "@/api/system/scenesVerifyCode";

export default {
  name: "ScenesVerifyCode",
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
      scenesVerifyCodeList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      },
      statusOptions: [
        {
          "label": "待使用",
          "value": "Pending"
        },{
          "label": "成功",
          "value": "Success"
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
    /** 查询滚屏交易信息列表 */
    getList() {
      this.loading = true;
      listScenesVerifyCode(this.queryParams).then(response => {
        this.scenesVerifyCodeList = response.rows;
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
      this.resetForm("queryForm");
    },
  }
};
</script>
