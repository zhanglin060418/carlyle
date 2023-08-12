<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="业务员" prop="userName">
        <el-input
          v-model="queryParams.userName"
          placeholder="请输入用户"
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

    <el-table v-loading="loading" :data="clerkRechargeList">
      <el-table-column label="业务员" align="center" prop="userName"/>
      <el-table-column label="经理"  align="center" prop="managerName" v-if= "$auth.hasPermi('system:manager:edit')"/>
      <el-table-column label="代理商"  align="center" prop="agentName" v-if= "$auth.hasPermi('system:agent:edit')"/>
      <el-table-column label="A级用户数" align="center" prop="childCount"/>
      <el-table-column label="A级充值人数" align="center" prop="childRechargeCount"/>
      <el-table-column label="A级充值金额" align="center" prop="childRechargeAmt">
        <template slot-scope="scope">
          {{ parseFloat(scope.row.childRechargeAmt)/100}}
        </template>
      </el-table-column>
      <el-table-column label="B级用户数" align="center" prop="grandCount"/>
      <el-table-column label="B级充值人数" align="center" prop="grandRechargeCount"/>
      <el-table-column label="B级充值金额" align="center" prop="grandRechargeAmt">
        <template slot-scope="scope">
          {{ parseFloat(scope.row.grandRechargeAmt)/100}}
        </template>
      </el-table-column>
      <el-table-column label="C级用户数" align="center" prop="greatGrandCount"/>
      <el-table-column label="C级充值人数" align="center" prop="greatGrandRechargeCount"/>
      <el-table-column label="C级充值金额" align="center" prop="greatGrandRechargeAmt">
        <template slot-scope="scope">
          {{ parseFloat(scope.row.greatGrandRechargeAmt)/100}}
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
import { rechargeListByClerk } from "@/api/system/teamRecharge";
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
      clerkRechargeList: [],
      dateRange: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userId: null,
        userName: null
      },
      // 表单参数
      form: {}
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询【请填写功能名称】列表 */
    getList() {
      this.loading = true;
      rechargeListByClerk(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.clerkRechargeList = response.rows;
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
    }
  }
};
</script>
