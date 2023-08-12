<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
    </el-form>

    <el-table v-loading="loading" :data="purchaseList" @selection-change="handleSelectionChange">
      <el-table-column label="产品名称" align="center" prop="productName" />
      <el-table-column label="金额" align="center" prop="amount" />
      <el-table-column label="数量" align="center" prop="cycle" />
      <el-table-column label="总金额" align="center" prop="totalInterest"/>
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
import { getTotalPurchase } from "@/api/system/purchase";
import UserInfo from "@/components/UserInfo/index.js";

export default {
  name: "Purchase",
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
      dateRange: [],
      // 产品支付表格数据
      purchaseList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      purchaseOpen: false,
      userOpen: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10
      },
      // 表单参数
      form: {},
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询产品支付列表 */
    getList() {
      this.loading = true;
      getTotalPurchase(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.purchaseList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    }
  }
};
</script>
