<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="商户号" prop="channelMerno">
        <el-input
          v-model="queryParams.channelMerno"
          placeholder="请输入商户号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="通道名称" prop="channelName">
        <el-input
          v-model="queryParams.channelName"
          placeholder="请输入通道名称"
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
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:channel:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:channel:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:channel:remove']">删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="channelList" @selection-change="handleSelectionChange">
      <el-table-column type="selection"align="center" />
      <el-table-column label="通道 ID"   align="center" prop="channelId" />
      <el-table-column label="商户号" align="center" prop="channelMerno" />
      <el-table-column label="通道名称" align="center" prop="channelName" />
      <el-table-column label="状态" align="center" prop="status" >
        <template slot-scope="scope">
          <span v-if="scope.row.status == '1'" style="color: #ff9292">停用</span>
          <span v-else-if="scope.row.status == '0'" style="color: #71e2a3">启用</span>
        </template>
      </el-table-column>
      <el-table-column  label="是否代付" width="80" prop="isProxy" >
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.isProxy"
            :active-value="'0'"
            :inactive-value="'1'"
            active-color="#02538C"
            inactive-color="#B9B9B9"
            @change="changeProxy(scope.row)"/>
        </template>
      </el-table-column>
      <el-table-column label="充值手续费" align="center" prop="inFeeRate" />
      <el-table-column label="提现手续费" align="center" prop="outFeeRate" />
      <el-table-column label="提现单笔费用" align="center" prop="singleFee" />
      <el-table-column label="最小充值金额" align="center" prop="inMinAmount">
        <template slot-scope="scope">
          {{ parseFloat(scope.row.inMinAmount)}}
        </template>-->
      </el-table-column>-->
      <el-table-column label="最大充值金额" align="center" prop="inMaxAmount">
        <template slot-scope="scope">
          {{ parseFloat(scope.row.inMaxAmount)}}
        </template>
      </el-table-column>>
      <el-table-column label="最小提现金额" align="center" prop="outMinAmount" >
        <template slot-scope="scope">
          {{ parseFloat(scope.row.outMinAmount)}}
        </template>
      </el-table-column>
      <el-table-column label="充值顺序" align="center" prop="rechargeSort" />
      <el-table-column label="显示名称" align="center" prop="displayName" />
      <el-table-column label="显示logo" align="center" prop="displayLogo" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.displayLogo" :width="50" :height="50"/>
        </template>
      </el-table-column>

      <el-table-column label="余额" align="center" prop="balance" >
        <template slot-scope="scope">
          可用{{((JSON.parse(scope.row.balance).balance)/100).toFixed(2)}}<br>
          冻结{{((JSON.parse(scope.row.balance).freezebalance)/100).toFixed(2)}}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:channel:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:channel:remove']"
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

    <!-- 添加或修改支付通道对话框 -->
    <el-dialog :title="title" :close-on-click-modal="false" :visible.sync="open" width="1000px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="商户号" prop="channelMerno">
              <el-input size="mini" v-model="form.channelMerno" placeholder="请输入商户号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="通道名称" prop="channelName">
              <el-input size="mini" v-model="form.channelName" placeholder="请输入通道名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
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
          <el-col :span="12">
            <el-form-item label="是否代付" prop="isProxy">
              <el-select v-model="form.isProxy" placeholder="是否代付" size="mini">
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
          <el-col :span="12">
            <el-form-item label="充值手续费" prop="inFeeRate">
              <el-input-number size="mini" v-model="form.inFeeRate" controls-position="right" :step="0.01" :min="0" placeholder="请输入充值手续费" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="提现手续费" prop="outFeeRate">
              <el-input-number size="mini" v-model="form.outFeeRate" controls-position="right" :step="0.01" :min="0" placeholder="请输入提现手续费" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="提现单笔费用" prop="singleFee">
              <el-input-number size="mini" v-model="form.singleFee" controls-position="right" :step="1" :min="0" placeholder="提现单笔费用" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="充值排序" prop="rechargeSort">
              <el-input-number size="mini" v-model="form.rechargeSort" controls-position="right" :step="1" :min="0" placeholder="充值排序" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="最小充值金额" prop="inMinAmount">
              <el-input-number size="mini" v-model="form.inMinAmount" controls-position="right" :step="1" :min="0" placeholder="请输入最小充值金额" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="最大充值金额" prop="inMaxAmount">
              <el-input-number size="mini" v-model="form.inMaxAmount" controls-position="right" :step="1" :min="0" placeholder="请输入最大充值金额" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="最小提现金额" prop="outMinAmount">
              <el-input-number size="mini" v-model="form.outMinAmount" controls-position="right" :step="1" :min="0" placeholder="请输入最小提现金额" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="页面显示" prop="displayName">
              <el-input v-model="form.displayName" placeholder="请输入页面显示" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="显示logo" prop="displayLogo">
              <image-upload v-model="form.displayLogo"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="可能出现的营销信息" prop="displayTips">
              <el-input v-model="form.displayTips" placeholder="请输入可能出现的营销信息" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="调用产生json" prop="jsonParam" style="min-height: 150px">
          <el-input size="mini" v-model="form.jsonParam" type="textarea" placeholder="请输入内容" rows="10"/>
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
  import {listChannel, getChannel, delChannel, addChannel, updateChannel, setProxyChannel} from "@/api/system/channel";

  export default {
    name: "Channel",
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
        // 支付通道表格数据
        channelList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          channelMerno: null,
          channelName: null,
          status: null,
          inFeeRate: null,
          outFeeRate: null,
          inMinAmount: null,
          inMaxAmount: null,
          outMinAmount: null,
          jsonParam: null,
          displayName: null,
          displayLogo: null,
          displayTips: null,
          isProxy:null
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          channelMerno: [
            { required: true, message: "商户号不能为空", trigger: "blur" }
          ],
          channelName: [
            { required: true, message: "通道名称不能为空", trigger: "blur" }
          ],
          status: [
            { required: true, message: "状态不能为空", trigger: "change" }
          ],
          inFeeRate: [
            { required: true, message: "充值手续费不能为空", trigger: "blur" }
          ],
          outFeeRate: [
            { required: true, message: "提现手续费不能为空", trigger: "blur" }
          ],
          singleFee: [
            { required: true, message: "提现单笔费用不能为空", trigger: "blur" }
          ],
          rechargeSort: [
            { required: true, message: "充值排序不能为空", trigger: "blur" }
          ],
          inMinAmount: [
            { required: true, message: "最小充值金额不能为空", trigger: "blur" }
          ],
          inMaxAmount: [
            { required: true, message: "最大充值金额不能为空", trigger: "blur" }
          ],
          outMinAmount: [
            { required: true, message: "最小提现金额不能为空", trigger: "blur" }
          ],
          jsonParam: [
            { required: true, message: "调用产生json不能为空", trigger: "blur" }
          ],
        },
        statusOptions: [{
          "label": "启用",
          "value": "0"
        }, {
          "label": "停用",
          "value": "1"
        },
        ]
      };
    },
    created() {
      this.getList();
    },
    methods: {
      /** 查询支付通道列表 */
      getList() {
        this.loading = true;
        listChannel(this.queryParams).then(response => {
          this.channelList = response.rows;
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
          channelId: null,
          channelMerno: null,
          channelName: null,
          status: null,
          inFeeRate: null,
          outFeeRate: null,
          inMinAmount: null,
          inMaxAmount: null,
          outMinAmount: null,
          jsonParam: null,
          createTime: null,
          updateTime: null,
          displayName: null,
          displayLogo: null,
          displayTips: null,
          isProxy:null
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
        this.handleQuery();
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.channelId)
        this.single = selection.length!==1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加支付通道";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const channelId = row.channelId || this.ids
        getChannel(channelId).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改支付通道";
        });
      },

      async changeProxy(op){
        this.loading = true;
        await setProxyChannel(op)
        this.loading = false;
        this.handleQuery()
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            this.form.inMinAmount = this.form.inMinAmount
            this.form.inMaxAmount = this.form.inMaxAmount
            this.form.outMinAmount = this.form.outMinAmount
            if (this.form.channelId != null) {
              updateChannel(this.form).then(response => {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            } else {
              addChannel(this.form).then(response => {
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
        const channelIds = row.channelId || this.ids;
        this.$modal.confirm('是否确认删除支付通道编号为"' + channelIds + '"的数据项？').then(function() {
          return delChannel(channelIds);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {});
      },
      /** 导出按钮操作 */
      handleExport() {
        this.download('system/channel/export', {
          ...this.queryParams
        }, `channel_${new Date().getTime()}.xlsx`)
      }
    }
  };
</script>

<style>

</style>
