<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="用户" prop="username">
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
      <el-form-item label="奖品类型" prop="type">
        <el-select
          size="mini"
          v-model="queryParams.type"
          placeholder="奖品类型"
          clearable
          @keyup.enter.native="handleQuery"
        >
          <el-option
            v-for="(item, index) in typeOptions"
            :key="index"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
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
    </el-form>

    <el-table v-loading="loading" :data="drawsList" @selection-change="handleSelectionChange">
      <el-table-column label="用户账户" align="center" prop="userName"/>
      <el-table-column label="业务员" align="center"prop="topName" v-if= "$auth.hasPermi('system:user:edit')"/>
      <el-table-column label="经理"  align="center" prop="managerName" v-if= "$auth.hasPermi('system:manager:edit')"/>
      <el-table-column label="代理商"  align="center" prop="agentName" v-if= "$auth.hasPermi('system:agent:edit')"/>
      <el-table-column label="名称" align="center" prop="name"/>
      <el-table-column label="奖品类型" align="center" prop="type" >
        <template slot-scope="scope">
          <span v-if="scope.row.type == 'Cash'">现金</span>
          <span v-else-if="scope.row.type == 'Voucher'">优惠券</span>
          <span v-else-if="scope.row.type == 'Virtual'">虚拟物品</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" >
        <template slot-scope="scope">
          <span v-if="scope.row.status == 'Completed'" style="color: #71e2a3">已完成</span>
          <span v-else-if="scope.row.status == 'Expired'" style="color: #fc0000">过期</span>
          <span v-else-if="scope.row.status == 'ToBeUsed'" style="color: #ffba00">待使用</span>
        </template>
      </el-table-column>
      <el-table-column label="面值" align="center" prop="amountBefore">
        <template slot-scope="scope">
          {{ parseFloat(scope.row.amount)/100 || 0}}
        </template>
      </el-table-column>
      <el-table-column label="有效期" align="center" prop="endDate"/>
      <el-table-column label="创建时间" align="center" prop="createTime">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="updateTime">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime) }}</span>
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
import { getDrawsDetailList } from "@/api/system/draws";
import UserInfo from "@/components/UserInfo";

export default {
  name: "draws",
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
      drawsList: [],
      dateRange: [],
      // 弹出层标题
      title: "抽奖记录",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userId: null,
        username: null,
        status: null,
        type: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      typeOptions: [
      {
        "label": "现金",
        "value": "Cash"
      },{
        "label": "优惠券",
        "value": "Voucher"
      }, {
        "label": "虚拟物品",
        "value": "Virtual"
      }],
      statusOptions: [
        {
          "label": "已完成",
          "value": "Completed"
        },{
          "label": "过期",
          "value": "Expired"
        }, {
          "label": "待使用",
          "value": "ToBeUsed"
        }],
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询【请填写功能名称】列表 */
    getList() {
      this.loading = true;
      getDrawsDetailList(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.drawsList = response.rows;
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
        userId: null,
        amount: 0,
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
