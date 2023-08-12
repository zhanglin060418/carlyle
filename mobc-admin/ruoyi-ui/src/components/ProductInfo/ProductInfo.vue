<template>
  <div id="product-info-dialog">
    <el-dialog :title="title" :visible.sync="open" width="750px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="名称" prop="name" >
              <el-input size="mini" readonly v-model="form.name"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="产品名称 (印度)" prop="nameIn">
              <el-input size="mini" readonly v-model="form.nameIn"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="产品名称 (英文)" prop="nameEn" >
              <el-input size="mini" readonly v-model="form.nameEn"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="产品名称 (俄语)" prop="nameRu" >
              <el-input size="mini" readonly v-model="form.nameRu"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="缩略图" prop="coverImages">
          <image-upload readonly v-model="form.coverImages"/>
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="标识" prop="category" >
              <el-input size="mini" readonly v-model="form.category"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="分类" prop="type">
              <el-input size="mini" readonly v-model="form.type"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="奖励产品" prop="rewardProduct">
              <el-input size="mini" readonly v-model="form.rewardProduct"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="日利率" prop="dailyInterest">
              <el-input size="mini" readonly v-model="form.dailyInterest"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="周期" prop="cycle">
              <el-input size="mini" readonly v-model="form.cycle"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="会员充值最小金额" prop="minimumBuy">
              <el-input size="mini" readonly v-model="form.minimumBuy" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="会员充值最大金额" prop="maximumBuy">
              <el-input size="mini" readonly v-model="form.maximumBuy" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="金额类型" prop="fund_type">
              <el-input size="mini" readonly v-model="form.fundType"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="产品进度" prop="progress">
              <el-input size="mini" readonly v-model="form.progress" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="是否显示数字进度条" prop="showProgressBar">
              <el-input size="mini" readonly v-model="form.showProgressBar ? '启用' : '停用'" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="发售时间" prop="sellingTimestamp">
          <el-input size="mini" readonly v-model="form.sellingTimestamp" />
        </el-form-item>
        <el-row>
          <el-col :span="8">
            <el-form-item label="配额" prop="totalFund">
              <el-input size="mini" readonly v-model="form.totalFund" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="份数" prop="copies" >
              <el-input size="mini" readonly v-model="form.copies" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="是否显示" prop="isVisible">
                <el-input size="mini" readonly v-model="form.isVisible ? '启用' : '停用'" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="是否发售" prop="onSale">
              <el-input size="mini" readonly v-model="form.onSale ? '启用' : '停用'" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="是否支持团购" prop="hasGroupBuyOption">
              <el-input size="mini" readonly v-model="form.hasGroupBuyOption ? '启用' : '停用'" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="幸运收入区间开始值" prop="luckyNumberRangeStart">
              <el-input size="mini" readonly v-model="form.luckyNumberRangeStart" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="幸运收入区间终止值" prop="luckyNumberRangeEnd">
              <el-input size="mini" readonly v-model="form.luckyNumberRangeEnd" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="状态" prop="status">
          <el-input size="mini" readonly v-model="form.status" />
        </el-form-item>
        <el-form-item label="介绍" prop="description">
          <editor readOnly v-model="form.description" :min-height="192" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="介绍 (英文)" prop="descriptionEn">
          <editor readOnly v-model="form.descriptionEn" :min-height="192" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="介绍 (印度语)" prop="descriptionIn">
          <editor readOnly v-model="form.descriptionIn" :min-height="192" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="介绍 (俄文)" prop="descriptionRu">
          <editor readOnly v-model="form.descriptionRu" :min-height="192" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="$emit('close'), open = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  dicts: ['sys_normal_disable'],
  name: "ProductInfo",
  props: {
    form: {
      type: Object,
      default: () => ({})
    },
  },
  data: () => ({
    open: true,
    title: "",
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
    }
  })
}
</script>
