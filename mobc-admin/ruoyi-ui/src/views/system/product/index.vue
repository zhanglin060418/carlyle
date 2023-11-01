<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="产品名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="金额类型" prop="fundType">
        <el-select
          v-model="queryParams.fundType"
          placeholder="金额类型"
          size="mini"
          style="width: 120px"
        >
          <el-option
            v-for="(item, index) in fundTypeOptions"
            :key="index"
            :label="item.label"
            :value="item.value"
            :disabled="item.disabled">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="是否显示" prop="isVisible">
        <el-select
          size="mini" v-model="queryParams.isVisible"
          placeholder="是否显示"
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
      <el-form-item label="是否发售" prop="onSale">
        <el-select
          size="mini" v-model="queryParams.onSale"
          placeholder="是否发售"
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

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:product:add']"
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
          v-hasPermi="['system:product:edit']"
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
          v-hasPermi="['system:product:remove']"
        >删除</el-button>
      </el-col>
<!--      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:product:export']"
        >导出</el-button>
      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="productList" @selection-change="handleSelectionChange">
      <el-table-column fixed type="selection" align="center" />
      <el-table-column label="ID" align="center" prop="id" width="50"/>
      <el-table-column label="产品名称" align="center" prop="name">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:product:edit']"
          >{{ scope.row.name }}</el-button>
        </template>
      </el-table-column>
      <el-table-column label="缩略图" align="center" prop="coverImages" width="80">
        <template slot-scope="scope">
          <image-preview :src="scope.row.coverImages" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="标识" align="center" prop="category"/>
      <el-table-column label="分类" align="center" prop="type"/>
      <el-table-column label="最小购买额" align="center" prop="minimumBuy" >
        <template slot-scope="scope">
        {{parseFloat(scope.row.minimumBuy/100)}}
        </template>
      </el-table-column>
      <el-table-column label="日利率" align="center" prop="dailyInterest"/>
      <el-table-column label="周期" align="center" prop="cycle" />
      <el-table-column label="数量" align="center" prop="copies"/>
      <el-table-column label="产品进度" align="center" prop="progress"/>
      <el-table-column label="是否获得抽奖资格" align="center" prop="isDraws" >
        <template slot-scope="scope">
          <span v-if="scope.row.isDraws == '0'" style="color: #71e2a3">是</span>
          <span v-if="scope.row.isDraws == '1'" style="color: #ffba00">否</span>
        </template>
      </el-table-column>
      <el-table-column label="是否获得优惠券" align="center" prop="isVoucher" >
        <template slot-scope="scope">
          <span v-if="scope.row.isVoucher == '0'" style="color: #71e2a3">是</span>
          <span v-if="scope.row.isVoucher == '1'" style="color: #ffba00">否</span>
       </template>
     </el-table-column>
      <el-table-column label="是否开启幸运收入" align="center" prop="hasGroupBuyOption" >
        <template slot-scope="scope">
          <span v-if="scope.row.hasGroupBuyOption == '0'" style="color: #71e2a3">是</span>
          <span v-if="scope.row.hasGroupBuyOption == '1'" style="color: #ffba00">否</span>
        </template>
      </el-table-column>
      <el-table-column label="数字进度条" align="center" prop="showProgressBar">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.showProgressBar"
            active-value="0"
            inactive-value="1"
            @change="handleStatusChange(scope.row, 'showProgressBar')"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="是否显示" align="center" prop="isVisible">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.isVisible"
            active-value="0"
            inactive-value="1"
            @change="handleStatusChange(scope.row, 'isVisible')"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="是否发售" align="center" prop="onSale">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.onSale"
            active-value="0"
            inactive-value="1"
            @change="handleStatusChange(scope.row, 'onSale')"
          ></el-switch>
        </template>
      </el-table-column>
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
            v-hasPermi="['system:product:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:product:remove']"
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
            <el-form-item label="产品名称 (印度)" prop="nameIn">
              <el-input size="mini" v-model="form.nameIn" placeholder="请输入产品名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="产品名称 (英文)" prop="nameEn" >
              <el-input size="mini" v-model="form.nameEn" placeholder="请输入产品名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="产品名称 (俄语)" prop="nameRu" >
              <el-input size="mini" v-model="form.nameRu" placeholder="请输入产品名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="标识" prop="category" >
              <el-input size="mini" v-model="form.category" placeholder="请输入标识"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="分类" prop="type">
              <el-select
                v-model="form.type"
                placeholder="分类"
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
        <el-row >
          <el-col>
            <el-form-item label="缩略图" prop="coverImages">
              <image-upload v-model="form.coverImages"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="奖励产品" prop="rewardProduct">
              <el-select v-model="form.rewardProduct" placeholder="状态" size="mini">
                <el-option
                  v-for="(item, index) in rewardProductList"
                  :key="index"
                  :label="item.label"
                  :value="item.name"
                  :disabled="item.disabled">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="日利率" prop="dailyInterest">
              <el-input-number size="mini" v-model="form.dailyInterest" controls-position="right" :step="0.01" :min="0" placeholder="请输入日利率" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="周期" prop="cycle">
              <el-input-number size="mini" v-model="form.cycle" controls-position="right" :step="1" :min="0" placeholder="请输入周期" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="会员充值最小金额" prop="minimumBuy">
              <el-input-number size="mini" v-model="form.minimumBuy" controls-position="right" :step="1" :min="0" placeholder="请输入会员充值最小金额" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="会员充值最大金额" prop="maximumBuy">
              <el-input-number size="mini" v-model="form.maximumBuy" controls-position="right" :step="1" :min="0" placeholder="请输入会员充值最大金额" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="金额类型" prop="fund_type">
          <el-select v-model="form.fundType" placeholder="金额类型" size="mini">
            <el-option
              v-for="(item, index) in fundTypeOptions"
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
            <el-form-item label="产品进度" prop="progress">
              <el-input-number size="mini" v-model="form.progress" controls-position="right" :step="0.01" :min="0" placeholder="请输入产品进度" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="是否显示数字进度条" prop="showProgressBar">
              <el-radio-group v-model="form.showProgressBar">
                <el-radio
                  v-for="dict in dict.type.sys_normal_disable"
                  :key="dict.value"
                  :label="dict.value"
                >{{dict.label}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        <el-col :span="8">
          <el-form-item label="发售时间" prop="sellingTimestamp">
            <el-date-picker clearable
              v-model="form.sellingTimestamp"
              type="datetime"
              value-format="yyyy-MM-dd hh:mm:ss"
              placeholder="请选择发售时间" >
            </el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="总配额" prop="totalFund">
              <el-input-number size="mini" v-model="form.totalFund" controls-position="right" :step="0.01" :min="0" placeholder="请输入配额" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="剩余额度" prop="currFund">
              <el-input-number size="mini" v-model="form.currFund" controls-position="right" :step="0.01" :min="0" placeholder="请输入剩余配额" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="数量" prop="copies" >
              <el-input-number size="mini" v-model="form.copies" controls-position="right" :step="1" :min="0" placeholder="请输入份数" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="是否显示" prop="isVisible">
              <el-radio-group v-model="form.isVisible">
                <el-radio
                  v-for="dict in dict.type.sys_normal_disable"
                  :key="dict.value"
                  :label="dict.value"
                >{{dict.label}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="是否发售" prop="onSale">
              <el-radio-group v-model="form.onSale">
                <el-radio
                  v-for="dict in dict.type.sys_normal_disable"
                  :key="dict.value"
                  :label="dict.value"
                >{{dict.label}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="状态" prop="status">
              <el-select v-model="form.status" placeholder="状态" size="mini">
                <el-option
                  v-for="(item, index) in statusOptions"
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
            <el-form-item label="是否开启幸运收入" prop="hasGroupBuyOption">
              <el-radio-group v-model="form.hasGroupBuyOption">
                <el-radio
                  v-for="dict in dict.type.sys_normal_disable"
                  :key="dict.value"
                  :label="dict.value"
                >{{dict.label}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="幸运收入开始值" prop="luckyNumberRangeStart">
              <el-input-number size="mini" v-model="form.luckyNumberRangeStart" controls-position="right" :step="1" :min="0" placeholder="请输入幸运收入区间开始值" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="幸运收入终止值" prop="luckyNumberRangeEnd">
              <el-input-number size="mini" v-model="form.luckyNumberRangeEnd" controls-position="right" :step="1" :min="0" placeholder="请输入幸运收入区间终止值" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="是否获得抽奖机会" prop="isDraws">
              <el-radio-group v-model="form.isDraws">
                <el-radio
                  v-for="dict in dict.type.sys_normal_disable"
                  :key="dict.value"
                  :label="dict.value"
                >{{dict.label}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="使用优惠券开始值" prop="voucherStart">
              <el-input-number size="mini" v-model="form.voucherStart" controls-position="right" :step="1" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="使用优惠券终止值" prop="voucherEnd">
              <el-input-number size="mini" v-model="form.voucherEnd" controls-position="right" :step="1" :min="0" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="是否获得优惠券" prop="isVoucher">
              <el-radio-group v-model="form.isVoucher">
                <el-radio
                  v-for="dict in dict.type.sys_normal_disable"
                  :key="dict.value"
                  :label="dict.value"
                >{{dict.label}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="优惠券有效期" prop="voucherCycle">
              <el-input-number size="mini" v-model="form.voucherCycle" controls-position="right" :step="1" :min="0"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="获得优惠券开始值" prop="voucherObtainStart">
              <el-input-number size="mini" v-model="form.voucherObtainStart" controls-position="right" :step="1" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="获得优惠券终止值" prop="voucherObtainEnd">
              <el-input-number size="mini" v-model="form.voucherObtainEnd" controls-position="right" :step="1" :min="0" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="介绍" prop="description">
              <editor v-model="form.description" :min-height="192" placeholder="请输入内容"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="介绍 (英文)" prop="descriptionEn">
              <editor v-model="form.descriptionEn" :min-height="192" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="介绍 (印度语)" prop="descriptionIn">
              <editor v-model="form.descriptionIn" :min-height="192" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
         <el-col :span="12">
            <el-form-item label="介绍 (俄文)" prop="descriptionRu">
              <editor v-model="form.descriptionRu" :min-height="192" placeholder="请输入内容" />
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
import { listProduct, listRewardProduct, getProduct, delProduct, addProduct, updateProduct } from "@/api/system/product";
import {float} from "quill/ui/icons";
import {parseTime} from "../../../utils/ruoyi";

export default {
  name: "Product",
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
      minbuy: 0,
      maxbuy: 0,
      // 总条数
      total: 0,
      // 产品表格数据
      productList: [],
      rewardProductList: [],
      dateRange: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        name: null,
        pageSize: 10,
        createBy: undefined,
        status: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          { required: true, message: "名称不能为空", trigger: "blur" }
        ],
        category: [
          { required: true, message: "标识不能为空", trigger: "blur" }
        ],
        type: [
          { required: true, message: "分类不能为空", trigger: "change" }
        ],
        dailyInterest: [
          { required: true, message: "日利率不能为空", trigger: "blur" }
        ],
        cycle: [
          { required: true, message: "周期不能为空", trigger: "blur" }
        ],
        voucherCycle: [
          { required: true, message: "优惠券有效期不能为空", trigger: "blur" }
        ],
        minimumBuy: [
          { required: true, message: "会员充值最小金额不能为空", trigger: "blur" }
        ],
        maximumBuy: [
          { required: true, message: "会员充值最大金额不能为空", trigger: "blur" }
        ],
        fundType: [
          { required: true, message: "金额类型不能为空", trigger: "change" }
        ],
        progress: [
          { required: true, message: "产品进度不能为空", trigger: "blur" }
        ],
        showProgressBar: [
          { required: true, message: "是否显示数字进度条不能为空", trigger: "blur" }
        ],
        sellingTimestamp: [
          { required: true, message: "发售时间不能为空", trigger: "blur" }
        ],
        totalFund: [
          { required: true, message: "总配额不能为空", trigger: "blur" }
        ],
        currFund: [
          { required: true, message: "剩余额度不能为空", trigger: "blur" }
        ],
        copies: [
          { required: true, message: "份数不能为空", trigger: "blur" }
        ],
        isVisible: [
          { required: true, message: "是否显示不能为空", trigger: "blur" }
        ],
        onSale: [
          { required: true, message: "是否发售不能为空", trigger: "blur" }
        ],
        hasGroupBuyOption: [
          { required: true, message: "是否开启幸运收入不能为空", trigger: "blur" }
        ],
        luckyNumberRangeStart: [
          { required: true, message: "幸运收入开始值不能为空", trigger: "blur" }
        ],
        luckyNumberRangeEnd: [
          { required: true, message: "幸运收入终止值不能为空", trigger: "blur" }
        ],
        isDraws: [
          { required: true, message: "是否获得抽奖机会不能为空", trigger: "blur" }
        ],
        voucherStart: [
          { required: true, message: "使用优惠券开始值不能为空", trigger: "blur" }
        ],
        voucherEnd: [
          { required: true, message: "使用优惠券终止值不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "状态不能为空", trigger: "change" }
        ],
        isVoucher: [
          { required: true, message: "是否获得优惠券不能为空", trigger: "blur" }
        ],
        voucherObtainStart: [
          { required: true, message: "获得优惠券开始值不能为空", trigger: "blur" }
        ],
        voucherObtainEnd: [
          { required: true, message: "获得优惠券终止值不能为空", trigger: "blur" }
        ],
      },
      // Status Options
      statusOptions: [{
        "label": "草稿",
        "value": "草稿"
      }, {
        "label": "发布不开售",
        "value": "发布不开售"
      }, {
        "label": "发布开手中",
        "value": "发布开手中"
      }, {
        "label": "发布停售",
        "value": "发布停售"
      }],
      // Type Options
      typeOptions: [{
        "label": "普通产品",
        "value": "普通产品"
      }, {
        "label": "赠送产品",
        "value": "赠送产品"
      }],
      // FundType Options
      fundTypeOptions: [{
        "label": "定额",
        "value": "定额"
      }, {
        "label": "区间",
        "value": "区间"
      }],
      visibleOptions: [{
        "label": "是",
        "value": "0"
      }, {
        "label": "否",
        "value": "1"
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
      await listProduct(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.productList = response.rows;
        console.log(this.productList)
        this.total = response.total;
        this.loading = false;
      });
      this.productList.forEach(item =>{
        if(item.type == '赠送产品')
          this.rewardProductList.push(item)
      })
      console.log(this.rewardProductList)
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
        nameIn: null,
        nameEn: null,
        nameRu: null,
        coverImages: null,
        category: null,
        type: null,
        rewardProduct: null,
        dailyInterest: null,
        cycle: null,
        minimumBuy: null,
        maximumBuy: null,
        totalFund:null,
        currFund:null,
        fundType: null,
        progress: null,
        showProgressBar: null,
        sellingTimestamp: null,
        totalFund: null,
        currFund: null,
        copies: null,
        isVisible: null,
        onSale: null,
        hasGroupBuyOption: null,
        luckyNumberRangeStart: null,
        luckyNumberRangeEnd: null,
        status: null,
        description: null,
        voucherStart: null,
        voucherEnd: null,
        voucherCycle:null,
        voucherObtainStart: null,
        voucherObtainEnd: null,
        descriptionEn: null,
        descriptionIn: null,
        descriptionRu: null,
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
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加产品";
    },
    /** 修改按钮操作 */
    async handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      await getProduct(id).then(response => {
        this.form = response.data;
        this.form.minimumBuy = this.form.minimumBuy/100
        this.form.maximumBuy = this.form.maximumBuy/100
        this.form.totalFund= this.form.totalFund/100
        this.form.currFund= this.form.currFund/100
      });
      const reward = this.form.rewardProduct
      const curr = this.rewardProductList.filter(item => item.id == reward);
      if(curr!=null&&curr.length>0){
        this.form.rewardProduct = curr[0].name
      }

      this.open = true;
      this.title = "修改产品";
    },
    /** 提交按钮 */
    submitForm: function(){
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.minimumBuy = this.form.minimumBuy * 100
          this.form.maximumBuy = this.form.maximumBuy * 100
          this.form.totalFund = this.form.totalFund * 100
          this.form.currFund = this.form.currFund * 100

          if(this.form.hasGroupBuyOption=='0' && this.form.isVoucher=='0'){
            this.$modal.msg("获得幸运收入和获得优惠券不能同时开启，请检查");
            return;
          }
          if(this.form.hasGroupBuyOption=='0'){
            if(this.form.luckyNumberRangeEnd =='0'||this.form.luckyNumberRangeStart>this.form.luckyNumberRangeEnd){
              this.$modal.msg("幸运区间值异常，请检查");
              return;
            }
          }
          if(this.form.voucherEnd =='0'||this.form.voucherStart>this.form.voucherEnd){
            this.$modal.msg("使用优惠券区间值异常，请检查");
            return;
          }
          if(this.form.isVoucher=='0') {
            if (this.form.voucherObtainEnd == '0' || this.form.voucherObtainStart > this.form.voucherObtainEnd) {
              this.$modal.msg("获得优惠券区间值异常，请检查");
              return;
            }
          }
          const reward = this.form.rewardProduct
          if (reward!=null) {
            const curr = this.rewardProductList.filter(item => item.name == reward)
            this.form.rewardProduct = curr[0].id
          }
          if (this.form.id != null) {
            updateProduct(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addProduct(this.form).then(response => {
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
      this.$modal.confirm('如果您删除该产品，所有包含该产品的操作（用户产品购买日志）将被删除。'+ '是否确认删除产品编号为"' + ids + '"的数据项？').then(function() {
        return delProduct(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    // 角色状态修改
    handleStatusChange(row, str) {
      let text = row[str] === "0" ? "启用" : "停用";
      let field_name = str === "isVisible" ? "是否显示"
        : str === "onSale" ? "是否发售"
        : str === "showProgressBar" ? "是否显示数字进度条" : "是否支持团购";
      this.$modal.confirm('确认要' + text + '-' + row.name +' : '+ field_name + '？').then(function() {
        updateProduct(row);
        return;
      }).then(() => {
        this.$modal.msgSuccess(text + "成功");
      }).catch(function() {
        if (str == 'isVisible')
          row.isVisible = row.isVisible === "0" ? "1" : "0";
        else if (str == 'onSale')
          row.onSale = row.onSale === "0" ? "1" : "0";
        else if (str == 'showProgressBar')
          row.showProgressBar = row.showProgressBar === "0" ? "1" : "0";
        else if(str == 'hasGroupBuyOption')
          row.hasGroupBuyOption = row.hasGroupBuyOption === "0" ? "1" : "0";
      });
    },
/*    /!** 导出按钮操作 *!/
    handleExport() {
      this.download('system/product/export', {
        ...this.queryParams
      }, `product_${new Date().getTime()}.xlsx`)
    }*/
  }

};
</script>
