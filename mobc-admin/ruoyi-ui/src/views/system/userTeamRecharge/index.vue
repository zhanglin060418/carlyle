<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="用户" prop="username">
        <el-input
          v-model="queryParams.userName"
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
    </el-form>

    <el-table v-loading="loading" :data="userRechargeList">
      <el-table-column label="用户账号" align="center" key="userName"  >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            @click="callUserTeamTrans(scope.row)">{{ scope.row.userName }}</el-button>
        </template>
      </el-table-column>
      <el-table-column label="业务员" align="center"prop="topName" v-if= "$auth.hasPermi('system:user:edit')"/>
      <el-table-column label="经理"  align="center" prop="managerName" v-if= "$auth.hasPermi('system:manager:edit')"/>
      <el-table-column label="代理商"  align="center" prop="agentName" v-if= "$auth.hasPermi('system:agent:edit')"/>
      <el-table-column label="A级用户数" align="center" prop="childCount"/>
      <el-table-column label="B级用户数" align="center" prop="grandCount"/>
      <el-table-column label="C级用户数" align="center" prop="greatGrandCount"/>
    </el-table>
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <el-dialog :title="title" :close-on-click-modal="false" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="200px">
        <el-row>
          <el-col :span="12" >
            <el-form-item label="A级充值人数：">
              <span>{{ form.childRechargeCount }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="A级充值金额：">
              <span>{{ parseFloat(form.childRechargeAmt) / 100 }}</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12" >
            <el-form-item label="A级购买人数：">
              <span>{{ form.childPurcherCount }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="A级购买金额：">
              <span>{{ parseFloat(form.childPurcherAmt) / 100 }}</span>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12" >
            <el-form-item label="B级充值人数：">
              <span>{{ form.grandRechargeCount }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="B级充值金额：">
              <span>{{ parseFloat(form.grandRechargeAmt) / 100 }}</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12" >
            <el-form-item label="B级购买人数：">
              <span>{{ form.grandPurcherCount }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="B级购买金额：">
              <span>{{ parseFloat(form.grandPurcherAmt) / 100 }}</span>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12" >
            <el-form-item label="C级充值人数：">
              <span>{{ form.greatGrandRechargeCount }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="C级充值金额：">
              <span>{{ parseFloat(form.greatGrandRechargeAmt) / 100 }}</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12" >
            <el-form-item label="C级购买人数：">
              <span>{{ form.greatGrandPurcherCount }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="C级购买金额：">
              <span>{{ parseFloat(form.greatGrandPurcherAmt) / 100 }}</span>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import { rechargeList,getUserTeamTrans } from "@/api/system/teamRecharge";
import UserInfo from "@/components/UserInfo";
export default {
  name: "History",
  data() {
    return {
      // 遮罩层
      loading: true,
      open: false,
      title: "",
      form: {},
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
      userRechargeList: [],
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userId: null,
        userName: null
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询【请填写功能名称】列表 */
    getList() {
      this.loading = true;
      rechargeList(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.userRechargeList = response.rows;
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
    },
    callUserTeamTrans(row) {
      const userId = row.userId;
      this.loading = true;
      getUserTeamTrans({userId: userId}).then(response => {
        if(response.code == 200){
          this.form = response.data;
          this.open = true;
          this.title = "团队交易数据";
          this.loading = false;
        }else{
          this.errDialog(response.msg)
        }
      })
    }
  }
};
</script>


