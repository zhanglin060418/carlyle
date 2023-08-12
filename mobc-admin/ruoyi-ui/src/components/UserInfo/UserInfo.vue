<template>
  <div id="user-info-dialog">
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="用户账号" prop="userName">
              <el-input readonly size="mini" v-model=" form.userName" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="用户邮箱" prop="email">
              <el-input readonly size="mini" v-model=" form.email"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邀请码" prop="inviteCode">
              <el-input readonly size="mini" v-model=" form.inviteCode" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="顶级ID" prop="topId">
              <el-input readonly size="mini" v-model=" form.topUsername" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="上级ID" prop="parentId">
              <el-input readonly size="mini" v-model=" form.parentUsername" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="上上级ID" prop="grandId">
              <el-input readonly size="mini" v-model=" form.grandUsername" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="上上上级ID" prop="greatGrandId">
              <el-input readonly size="mini" v-model=" form.greatGrandUsername" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="WhatsApp" prop="whatsApp">
          <el-input readonly size="mini" v-model=" form.whatsApp"/>
        </el-form-item>
        <el-form-item label="Telegram" prop="telegram">
          <el-input readonly size="mini" v-model=" form.telegram"/>
        </el-form-item>
        <el-form-item label="Child List" prop="childList">
          <el-input size="mini"type="textarea" readonly v-model="form.childList"/>
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
  name: "UserInfo",
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
        {required: true, message: "用户ID不能为空", trigger: "blur"}
      ],
      product: [
        {required: true, message: "产品ID不能为空", trigger: "blur"}
      ],
      cycle: [
        {required: true, message: "周期不能为空", trigger: "blur"}
      ],
      dailyInterest: [
        {required: true, message: "日利率不能为空", trigger: "blur"}
      ],
      totalInterest: [
        {required: true, message: "总利率不能为空", trigger: "blur"}
      ],
      amount: [
        {required: true, message: "金额不能为空", trigger: "blur"}
      ],
      payBack: [
        {required: true, message: "是否返息不能为空", trigger: "blur"}
      ],
      status: [
        {required: true, message: "状态不能为空", trigger: "change"}
      ],
      endAt: [
        {required: true, message: "结算时间不能为空", trigger: "blur"}
      ],
      createdAt: [
        {required: true, message: "创建时间不能为空", trigger: "blur"}
      ]
    }
  })
}
</script>
