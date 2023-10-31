<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="类型" prop="type">
        <el-select
          v-model="queryParams.type"
          placeholder="类型"
          size="mini"
          style="width: 120px"
        >
          <el-option
            v-for="(item, index) in typeOptions"
            :key="index"
            :label="item.label"
            :value="item.value"
            :disabled="item.disabled">
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
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:lottery:edit']"
        >修改</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="lotteryList" @selection-change="handleSelectionChange">
      <el-table-column fixed type="selection" align="center" />
      <el-table-column label="ID" align="center" prop="id" width="50"/>
      <el-table-column label="名称" align="center" prop="name">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:lottery:edit']"
          >{{ scope.row.name }}</el-button>
        </template>
      </el-table-column>
      <el-table-column label="缩略图" align="center" prop="coverImages" width="80">
        <template slot-scope="scope">
          <image-preview :src="scope.row.coverImages" :width="50" :height="50"/>
        </template>
      </el-table-column>

      <el-table-column label="类型" align="center" prop="type">
        <template slot-scope="scope">
          <span v-if="scope.row.type == 'Cash'">现金</span>
          <span v-else-if="scope.row.type == 'Voucher'">优惠卷</span>
          <span v-else-if="scope.row.type == 'Virtual'">虚拟物品</span>
        </template>
      </el-table-column>
      <el-table-column label="面值" align="center" prop="minimumBuy" >
        <template slot-scope="scope">
        {{parseFloat(scope.row.amount/100)}}
        </template>
      </el-table-column>
      <el-table-column label="概率" align="center" prop="probability"/>
      <el-table-column label="标识" align="center" prop="category"/>
      <el-table-column label="有效期" align="center" prop="cycle" />
      <el-table-column label="创建时间" align="center" prop="createdTime" >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="updatedTime" >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:lottery:edit']"
          >修改</el-button>
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

    <!-- 添加或修改产品对话框 -->
    <el-dialog :title="title" :close-on-click-modal="false" :visible.sync="open" width="1200px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="148px">
        <el-row>
          <el-col :span="8">
            <el-form-item label="名称" prop="name" >
              <el-input size="mini" v-model="form.name" placeholder="请输入名称" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="产品名称 (英文)" prop="nameEn" >
              <el-input size="mini" v-model="form.nameEn" placeholder="请输入产品名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="类型" prop="type">
              <el-select
                v-model="form.type"
                placeholder="类型"
                size="mini"
                style="width: max-content"
              >
                <el-option
                  v-for="(item, index) in typeOptions"
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
          <el-col :span="8">
            <el-form-item label="面值" prop="amount">
              <el-input-number size="mini" v-model="form.amount" controls-position="right" :step="1" :min="0" placeholder="请输入会员充值最小金额" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="概率" prop="probability">
              <el-input-number size="mini" v-model="form.probability" controls-position="right" :step="0.01" :min="0" placeholder="请输入概率" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="有效期" prop="cycle">
              <el-input-number size="mini" v-model="form.cycle" controls-position="right" :step="1" :min="0" placeholder="请输入有效期" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row >
          <el-col>
            <el-form-item label="缩略图" prop="coverImages">
              <image-upload v-model="form.coverImages"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="标识" prop="category" >
              <el-input-number size="mini" v-model="form.category" controls-position="right" :step="1" :min="0" placeholder="请输入标识" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="介绍" prop="description">
              <editor v-model="form.description" :min-height="192" placeholder="请输入内容"/>
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
import { listLottery, getLottery, updateLottery } from "@/api/system/lottery";
import {float} from "quill/ui/icons";
import {parseTime} from "../../../utils/ruoyi";

export default {
  name: "Lottery",
  computed: {
    float() {
      return float
    }
  },
  dicts: ['sys_normal_disable'],
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
      // 产品表格数据
      lotteryList: [],
      dateRange: [],
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
        name: [
          { required: true, message: "名称不能为空", trigger: "blur" }
        ],
        nameEn: [
          { required: true, message: "英文名称不能为空", trigger: "blur" }
        ],
        category: [
          { required: true, message: "标识不能为空", trigger: "blur" }
        ],
        type: [
          { required: true, message: "类型不能为空", trigger: "change" }
        ],
        probability: [
          { required: true, message: "概率不能为空", trigger: "blur" }
        ],
        cycle: [
          { required: true, message: "有效期不能为空", trigger: "blur" }
        ],
        amount: [
          { required: true, message: "面值不能为空", trigger: "blur" }
        ],
      },
      // Type Options
      typeOptions: [{
        "label": "现金",
        "value": "Cash"
      }, {
        "label": "优惠卷",
        "value": "Voucher"
      }, {
        "label": "虚拟物品",
        "value": "Virtual"
      }],
    };
  },
  created() {
    this.getList();
  },
  methods: {
    parseTime,
    /** 查询产品列表 */
    async getList() {
      this.loading = true;
      await listLottery(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.lotteryList = response.rows;
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
        name: null,
        nameEn: null,
        coverImages: null,
        category: null,
        type: null,
        probability: null,
        cycle: null,
        amount: null,
        description: null,
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
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 修改按钮操作 */
    async handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      await getLottery(id).then(response => {
        this.form = response.data;
        this.form.amount = this.form.amount/100
      });
      this.open = true;
      this.title = "修改产品";
    },
    /** 提交按钮 */
    submitForm: function(){
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.amount = this.form.amount * 100
          if (this.form.id != null) {
            updateLottery(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
  }

};
</script>
