<template>
  <div id="user-info-dialog">
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="用户名称" prop="mobile">
          <el-input readonly size="mini" v-model="form.mobile" placeholder="请输入用户名称" />
        </el-form-item>
        <el-form-item label="卡名" prop="name">
          <el-input readonly size="mini" v-model="form.name" placeholder="请输入卡名" />
        </el-form-item>
        <el-form-item label="银行名称" prop="bank.name">
          <el-input readonly size="mini" v-model="form.bank.name" placeholder="银行代码" />
        </el-form-item>
        <el-form-item label="银行代码" prop="bankCode">
          <el-input readonly size="mini" v-model="form.bankCode" placeholder="银行代码" />
        </el-form-item>
        <el-form-item label="帐户" prop="cardNo">
          <el-input readonly size="mini" v-model="form.cardNo" placeholder="请输入帐户" />
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
  name: "CardInfo",
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
      cardId: [
        { required: true, message: "用户ID不能为空", trigger: "blur" }
      ],
      name: [
        { required: true, message: "卡名不能为空", trigger: "blur" }
      ],
      bankCode: [
        { required: true, message: "用户ID不能为空", trigger: "blur" }
      ],
      cardNo: [
        { required: true, message: "帐户不能为空", trigger: "blur" }
      ],
      mobile: [
        { required: true, message: "手机不能为空", trigger: "blur" }
      ]
    }
  })
}
</script>
