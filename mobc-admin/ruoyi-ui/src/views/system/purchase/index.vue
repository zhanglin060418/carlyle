<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="用户账号" prop="userName">
        <el-input
          v-model="queryParams.userName"
          placeholder="请输入用户账号"
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
      <el-form-item label="产品名称" prop="productName">
        <el-input
          v-model="queryParams.productName"
          placeholder="请输入产品名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否返本" prop="payBack">
        <el-select
          size="mini" v-model="queryParams.payBack"
          placeholder="是否返本"
          clearable
          style="width: 120px"
        >
          <el-option
            v-for="(item, index) in visibleOptions"
            :key="index"
            :label="item.label"
            :value="item.value"
            :disabled="item.disabled">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="结算开始日期" prop="beginDate">
        <el-date-picker clearable
                        v-model="queryParams.beginDate"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="收益开始日期" >
        </el-date-picker>
      </el-form-item>

      <el-form-item label="结算到期日期" prop="endDate">
        <el-date-picker clearable
                        v-model="queryParams.endDate"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="收益到期日期" >
        </el-date-picker>
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
          v-hasPermi="['system:purchase:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="purchaseList" @selection-change="handleSelectionChange">
      <el-table-column label="编号" align="center" prop="orderNo" />
      <el-table-column label="用户账户" align="center" prop="userName"/>
      <el-table-column label="业务员" align="center"prop="topName" v-if= "$auth.hasPermi('system:user:edit')"/>
      <el-table-column label="经理"  align="center" prop="managerName" v-if= "$auth.hasPermi('system:manager:edit')"/>
      <el-table-column label="代理商"  align="center" prop="agentName" v-if= "$auth.hasPermi('system:agent:edit')"/>
      <el-table-column label="产品名称" align="center" prop="productName" />
      <el-table-column label="周期" align="center" prop="cycle" />
      <el-table-column label="日利率" align="center" prop="dailyInterest" />
      <el-table-column label="已获收益" align="center" prop="totalInterest">
        <template slot-scope="scope">
          <span>{{ parseFloat(scope.row.totalInterest/100).toFixed(2) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="金额" align="center" prop="amount" >
        <template slot-scope="scope">
          {{ parseFloat(scope.row.amount) / 100 }}
        </template>
      </el-table-column>
      <el-table-column label="结算开始时间" align="center" prop="beginDate"/>
      <el-table-column label="到期时间" align="center" prop="endDate" />
      <el-table-column label="是否返本" align="center" prop="payBack" >
        <template slot-scope="scope">
          <span v-if="scope.row.payBack == '1'" style="color: #ffba00">是</span>
          <span v-else-if="scope.row.payBack == '0'" style="color: #71e2a3">否</span>
          <span v-else></span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime"/>
      <el-table-column label="更新时间" align="center" prop="endAt"/>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改产品支付对话框 -->
    <el-dialog :title="title" :close-on-click-modal="false" :visible.sync="purchaseOpen" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col span="12">
            <el-form-item label="用户" size="mini" prop="buyer">
              <el-input v-model="form.userName" placeholder="请输入用户ID" />
            </el-form-item>
          </el-col>
          <el-col span="12">
            <el-form-item label="产品" size="mini" prop="product">
              <el-input v-model="form.productName" placeholder="请输入产品ID" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="周期" size="mini" prop="cycle">
              <el-input v-model="form.cycle" placeholder="请输入周期" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="日利率" size="mini" prop="dailyInterest">
              <el-input v-model="form.dailyInterest" placeholder="请输入日利率" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="已获收益" size="mini" prop="totalInterest">
              <el-input v-model="form.totalInterest/100" placeholder="请输入总利率" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="金额" size="mini" prop="amount">
              <el-input v-model="form.amount/100" placeholder="请输入金额" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="结算开始时间" size="mini"  prop="beginDate">
              <el-input v-model="form.beginDate" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结算到期时间" size="mini" prop="endDate">
              <el-input v-model="form.endDate"  />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="title" :visible.sync="userOpen" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="用户账号" prop="userName">
          <spen>{{ form.userName }}</spen>
        </el-form-item>
        <el-form-item label="用户邮箱" prop="email">
          <spen>{{ form.email }}</spen>
        </el-form-item>
        <el-form-item label="邀请码" prop="inviteCode">
          <spen>{{ form.inviteCode }}</spen>
        </el-form-item>
        <el-form-item label="顶级ID" prop="topId">
          <spen>{{ form.topId }}</spen>
        </el-form-item>
        <el-form-item label="上级ID" prop="parentId">
          <spen>{{ form.parentId }}</spen>
        </el-form-item>
        <el-form-item label="上上级ID" prop="grandId">
          <spen>{{ form.grandId }}</spen>
        </el-form-item>
        <el-form-item label="上上上级ID" prop="greatGrandId">
          <spen>{{ form.greatGrandId }}</spen>
        </el-form-item>
        <el-form-item label="WhatsApp" prop="whatsApp">
          <spen>{{ form.whatsApp }}</spen>
        </el-form-item>
        <el-form-item label="Telegram" prop="telegram">
          <spen>{{ form.telegram }}</spen>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listPurchase, getPurchase, delPurchase, addPurchase, updatePurchase } from "@/api/system/purchase";
import UserInfo from "@/components/UserInfo/index.js";
import ProductInfo from "@/components/ProductInfo/index.js";

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
      amountCount: 0,
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      purchaseOpen: false,
      userOpen: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userName: null,
        topName: null,
        managerName: null,
        productName: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        buyer: [
          { required: true, message: "用户ID不能为空", trigger: "blur" }
        ],
        product: [
          { required: true, message: "产品ID不能为空", trigger: "blur" }
        ],
        cycle: [
          { required: true, message: "周期不能为空", trigger: "blur" }
        ],
        dailyInterest: [
          { required: true, message: "日利率不能为空", trigger: "blur" }
        ],
        totalInterest: [
          { required: true, message: "总利率不能为空", trigger: "blur" }
        ],
        amount: [
          { required: true, message: "金额不能为空", trigger: "blur" }
        ],
        payBack: [
          { required: true, message: "是否返息不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "状态不能为空", trigger: "change" }
        ],
        endAt: [
          { required: true, message: "结算时间不能为空", trigger: "blur" }
        ],
        createdAt: [
          { required: true, message: "创建时间不能为空", trigger: "blur" }
        ]
      },
      visibleOptions: [{
        "label": "是",
        "value": "1"
      }, {
        "label": "否",
        "value": "0"
      }],
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询产品支付列表 */
    getList() {
      this.loading = true;
      listPurchase(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.purchaseList = response.rows;
        this.total = response.total;
        this.amountCount = response.amountCount;
        this.loading = false;
      });
    },
    handleGetUser(userId) {
      return UserInfo.open({ userId })
    },
    handleGetProduct(productId) {
      return ProductInfo.open({ productId })
    },
    // 取消按钮
    cancel() {
      this.purchaseOpen = false;
      this.userOpen = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        buyer: null,
        product: null,
        cycle: null,
        dailyInterest: null,
        totalInterest: null,
        amount: null,
        payBack: null,
        status: null,
        endAt: null,
        createdAt: null
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
      this.purchaseOpen = true;
      this.title = "添加产品支付";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getPurchase(id).then(response => {
        this.form = response.data;
        this.purchaseOpen = true;
        this.title = "修改产品支付";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updatePurchase(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.purchaseOpen = false;
              this.getList();
            });
          } else {
            addPurchase(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.purchaseOpen = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除产品支付编号为"' + ids + '"的数据项？').then(function() {
        return delPurchase(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/purchase/export', {
        ...this.queryParams
      }, `purchase_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
