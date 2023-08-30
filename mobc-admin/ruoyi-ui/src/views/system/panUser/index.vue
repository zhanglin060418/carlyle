<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col>
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
          <el-form-item label="用户编号" prop="userId">
            <el-input
              size="mini" v-model="queryParams.userId"
              placeholder="请输入用户编号"
              clearable
              @keyup.enter.native="handleQuery"/>
          </el-form-item>
          <el-form-item label="用户账户" prop="userName">
            <el-input
              size="mini" v-model="queryParams.userName"
              placeholder="请输入用户账户"
              clearable
              @keyup.enter.native="handleQuery"/>
          </el-form-item>
          <el-form-item label="业务员" prop="topName" v-hasPermi="['system:user:edit']">
            <el-input
              v-model="queryParams.topName"
              placeholder="请输入业务员账号"
              clearable
              @keyup.enter.native="handleQuery"/>
          </el-form-item>
          <el-form-item label="经理" prop="managerName" v-hasPermi="['system:manager:edit']">
            <el-input
              v-model="queryParams.managerName"
              placeholder="请输入经理账号"
              clearable
              @keyup.enter.native="handleQuery"/>
          </el-form-item>
          <el-form-item label="代理商" prop="agentName" v-hasPermi="['system:agent:edit']">
            <el-input
              size="mini" v-model="queryParams.agentName"
              placeholder="请输入代理商账号"
              clearable
              @keyup.enter.native="handleQuery"/>
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select
              size="mini" v-model="queryParams.status"
              placeholder="用户状态"
              clearable
              style="width: 240px">
              <el-option
                v-for="dict in dict.type.sys_normal_disable"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"/>
            </el-select>
          </el-form-item>
          <el-form-item label="登陆IP" prop="loginIp" >
            <el-input
              size="mini" v-model="queryParams.loginIp"
              placeholder="请输入登陆IP"
              clearable
              @keyup.enter.native="handleQuery"/>
          </el-form-item>
          <el-form-item label="创建时间">
            <el-date-picker
              size="mini" v-model="dateRange"
              style="width: 240px"
              value-format="yyyy-MM-dd"
              type="daterange"
              range-separator="-"
              start-placeholder="开始日期"
              end-placeholder="结束日期"/>
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
              v-hasPermi="['system:panUser:add']"
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
              v-hasPermi="['system:panUser:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="warning"
              plain
              icon="el-icon-download"
              size="mini"
              @click="handleExport"
              v-hasPermi="['system:panUser:export']">导出</el-button>
          </el-col>
          <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="userList" @selection-change="handleSelectionChange"
                  @row-dblclick="rowDblClick"
                  @sort-change="changeTableSort"
                  :default-sort = "{prop: 'createTime', order: 'descending'}">
          <el-table-column fixed type="selection" width="50" align="center" />
          <el-table-column label="编号" align="center" key="userId" prop="userId" v-if="columns[0].visible" />
          <el-table-column label="用户账号" align="center" key="userName" v-if="columns[1].visible" >
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                @click="callUserTram(scope.row)">{{ scope.row.userName }}</el-button>
            </template>
          </el-table-column>
          <el-table-column label="推荐人" align="center"prop="parentUsername"/>
          <el-table-column label="业务员" align="center"prop="topName" v-if= "$auth.hasPermi('system:user:edit')"/>
          <el-table-column label="经理"  align="center" prop="managerName" v-if= "$auth.hasPermi('system:manager:edit')"/>
          <el-table-column label="代理商"  align="center" prop="agentName" v-if= "$auth.hasPermi('system:agent:edit')"/>
          <el-table-column label="最后登录IP" align="center" prop="loginIp" />
          <el-table-column label="最后登录时间" align="center" prop="loginDate" :sortable="'custom'"/>
          <el-table-column label="邀请码" align="center" key="inviteCode" prop="inviteCode"/>

          <el-table-column label="状态" align="center" key="status" >
            <template slot-scope="scope">
              <el-switch
                size="mini" v-model="scope.row.status"
                active-value="0"
                inactive-value="1"
                @change="handleStatusChange(scope.row)"
              ></el-switch>
            </template>
          </el-table-column>
          <el-table-column label="是否可提现" align="center" key="isWithdarw">
            <template slot-scope="scope">
              <el-switch
                size="mini" v-model="scope.row.isWithdarw"
                active-value="0"
                inactive-value="1"
                @change="handleIsWithdarwChange(scope.row)"
              ></el-switch>
            </template>
          </el-table-column>
          <el-table-column label="是否返息" align="center" key="isRebate">
            <template slot-scope="scope">
              <el-switch
                size="mini" v-model="scope.row.isRebate"
                active-value="0"
                inactive-value="1"
                @change="handleIsRebateChange(scope.row)"
              ></el-switch>
            </template>
          </el-table-column>
          <el-table-column label="邀请码是否可用" align="center" key="isInviteCode">
            <template slot-scope="scope">
              <el-switch
                size="mini" v-model="scope.row.isInviteCode"
                active-value="0"
                inactive-value="1"
                @change="handleIsInviteCodeChange(scope.row)"
              ></el-switch>
            </template>
          </el-table-column>

          <el-table-column label="创建时间" align="center" prop="createTime" :sortable="'custom'"/>
          <el-table-column
            label="操作"
            align="center"
            width="160"
            class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['system:panUser:edit']">修改</el-button>

              <el-dropdown size="mini" @command="(command) => handleCommand(command, scope.row)" v-hasPermi="['system:panUser:resetPwd']">
                <el-button size="mini" type="text" icon="el-icon-d-arrow-right">更多</el-button>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item command="handleResetPwd" icon="el-icon-key"
                    v-hasPermi="['system:panUser:resetPwd']">重置密码</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
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
      </el-col>
    </el-row>

    <el-dialog :visible.sync="dialogVisible"  title="团队信息" width="70%">
      <child-component ref="child" ></child-component>
    </el-dialog>

    <!-- 添加或修改用户配置对话框 -->
    <el-dialog :title="title" :close-on-click-modal="false" :visible.sync="open" width="600px" append-to-body>
      <el-form v-loading="loadingForm" ref="form"  :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="用户账号"  prop="userName">
              <el-input size="mini" placeholder="输入234开头的用户账号" v-model=" form.userName" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.userId == undefined" label="登录密码" prop="password">
              <el-input size="mini" v-model="form.password" placeholder="请输入密码" type="password" maxlength="20" show-password/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item v-if="form.userId == undefined" label="邀请码" prop="inviteCode">
          <el-input size="mini" placeholder="邀请人邀请码" v-model="form.inviteCode"/>
        </el-form-item>
        <el-form-item label="email" prop="email">
          <el-input size="mini" v-model=" form.email"/>
        </el-form-item>
        <el-form-item label="WhatsApp" prop="whatsApp">
          <el-input size="mini" v-model=" form.whatsApp"/>
        </el-form-item>
        <el-form-item label="Telegram" prop="telegram">
          <el-input size="mini" v-model=" form.telegram"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 用户导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <div class="el-upload__tip" slot="tip">
            <el-checkbox size="mini" v-model="upload.updateSupport" /> 是否更新已经存在的用户数据
          </div>
          <span>仅允许导入xls、xlsx格式文件。</span>
          <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;" @click="importTemplate">下载模板</el-link>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listUser, getUser, addUser, updateUser, resetUserPwd, changeUserStatus,changeUserIsWithdarw,changeUserIsRebate,changeUserIsInviteCode } from "@/api/system/panUser";
import { getToken } from "@/utils/auth";
import data from "@/views/system/dict/data.vue";
import ChildComponent from './userTeamInfo.vue'

export default {
  name: "User",
  components: {
    ChildComponent
  },
  computed: {
    data() {
      return data
    }
  },
  dicts: ['sys_normal_disable', 'sys_user_sex'],
  data() {
    return {
      // 遮罩层
      loading: true,
      loadingForm: false,
      currentComponent: null,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      dialogVisible: false,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 用户表格数据
      userList: null,
      // 弹出层标题
      title: "",
      // 部门树选项
      deptOptions: undefined,
      // 是否显示弹出层
      open: false,
      // 部门名称
      deptName: undefined,
      // 默认密码
      initPassword: undefined,
      // 日期范围
      dateRange: [],
      // 岗位选项
      postOptions: [],
      // 角色选项
      roleOptions: [],
      // 表单参数
      form: {},
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 用户导入参数
      upload: {
        // 是否显示弹出层（用户导入）
        open: false,
        // 弹出层标题（用户导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的用户数据
        updateSupport: 0,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/system/panUser/importData"
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userName: null,
        topName: null,
        managerName: null,
        status: undefined,
        propName:null,
        sortType:null
      },
      // 列信息
      columns: [
        { key: 0, label: `用户编号`, visible: true },
        { key: 1, label: `用户名称`, visible: true },
        { key: 2, label: `用户昵称`, visible: true },
        { key: 3, label: `部门`, visible: true },
        { key: 4, label: `手机号码`, visible: true },
        { key: 5, label: `状态`, visible: true },
        { key: 6, label: `创建时间`, visible: true }
      ],
      // 表单校验
      rules: {
        userName: [
          { required: true, message: "用户名称不能为空", trigger: "blur" },
          { min: 10, max: 20, message: '用户名称长度必须介于 10 和 20 之间', trigger: 'blur' }
        ],
        inviteCode:[
          { required: true, message: "邀请人邀请码不能为空", trigger: "blur" },
        ],
        password: [
          { required: true, message: "用户密码不能为空", trigger: "blur" },
          { min: 5, max: 20, message: '用户密码长度必须介于 5 和 20 之间', trigger: 'blur' }
        ],
        email: [
          {
            type: "email",
            message: "请输入正确的邮箱地址",
            trigger: ["blur", "change"]
          }
        ]

      },
      statusOptions: [{
        "label": "启用",
        "value": "0"
      }, {
        "label": "停用",
        "value": "1"
      }],
    };
  },
  watch: {
    // 根据名称筛选部门树
    deptName(val) {
      this.$refs.tree.filter(val);
    }
  },
  created() {
    this.getList();
    // this.getDeptTree();
    this.getConfigKey("sys.user.initPassword").then(response => {
      this.initPassword = response.msg;
    });
  },
  methods: {
    /** 查询用户列表 */
    getList() {
      this.loading = true;
      listUser(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.userList = response.rows;
          this.total = response.total;
          this.loading = false;
        }
      );
    },
    //排序触发事件
    changeTableSort({prop ,order}) {
      this.queryParams.propName = prop;
      this.queryParams.sortType = order;
      this.loading = true;
      listUser(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.userList = response.rows;
        this.total = response.total;
        this.loading = false;
      }
      );
    },

    callUserTram(row){
      this.dialogVisible =true;
      this.currentComponent = 'ChildComponent';
      this.$nextTick(() => {
        this.$refs.child.getUserTram(row)
      })
    },

    handleStatusChange(row) {
      let text = row.status === "0" ? "启用" : "停用";
      this.$modal.confirm('确认要"' + text + '""' + row.userName + '"用户吗？').then(function() {
        return changeUserStatus(row.userId, row.status);
      }).then(() => {
        this.$modal.msgSuccess(text + "成功");
      }).catch(function() {
        row.status = row.status === "0" ? "1" : "0";
      });
    },
    handleIsWithdarwChange(row) {
      let text = row.isWithdarw === "0" ? "开启" : "关闭";
      this.$modal.confirm('确认要"' + text + '""' + row.userName + '"提现功能吗？').then(function() {
        return changeUserIsWithdarw(row.userId, row.isWithdarw);
      }).then(() => {
        this.$modal.msgSuccess(text + "成功");
      }).catch(function() {
        row.isWithdarw = row.isWithdarw === "0" ? "1" : "0";
      });
    },
    handleIsRebateChange(row) {
      let text = row.isRebate === "0" ? "开启" : "关闭";
      this.$modal.confirm('确认要"' + text + '""' + row.userName + '"返息权限吗？').then(function() {
        return changeUserIsRebate(row.userId, row.isRebate);
      }).then(() => {
        this.$modal.msgSuccess(text + "成功");
      }).catch(function() {
        row.isRebate = row.isRebate === "0" ? "1" : "0";
      });
    },
    handleIsInviteCodeChange(row) {
      let text = row.isInviteCode === "0" ? "开启" : "关闭";
      this.$modal.confirm('确认要"' + text + '""' + row.userName + '"邀请码权限吗？').then(function() {
        return changeUserIsInviteCode(row.userId, row.isInviteCode);
      }).then(() => {
        this.$modal.msgSuccess(text + "成功");
      }).catch(function() {
        row.isInviteCode = row.isInviteCode === "0" ? "1" : "0";
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
        userId: undefined,
        deptId: undefined,
        userName: undefined,
        userType: undefined,
        nickName: undefined,
        password: undefined,
        phonenumber: undefined,
        inviteCode: undefined,
        email: undefined,
        sex: undefined,
        status: undefined,
        remark: undefined,
        postIds: [],
        roleIds: [],
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
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.userId);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    // 更多操作触发
    handleCommand(command, row) {
      switch (command) {
        case "handleResetPwd":
          this.handleResetPwd(row);
          break;
        case "handleAuthRole":
          this.handleAuthRole(row);
          break;
        default:
          break;
      }
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加用户";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const userId = row.userId || this.ids;
      getUser(userId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改用户";
        this.form.password = "";
      });
    },
    /** 重置密码按钮操作 */
    handleResetPwd(row) {
      this.$prompt('请输入"' + row.userName + '"的新密码', "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        closeOnClickModal: false,
        inputPattern: /^.{5,20}$/,
        inputErrorMessage: "用户密码长度必须介于 5 和 20 之间"
      }).then(({ value }) => {
          resetUserPwd(row.userId, value).then(response => {
            this.$modal.msgSuccess("修改成功，新密码是：" + value);
          });
        }).catch(() => {});
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.loading = true;
          if (this.form.userId != undefined) {
            updateUser(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.loading = false;
              this.getList();
            });
          }else {
            addUser(this.form).then(response => {
              if(response.code==200){
                this.$modal.msgSuccess("新增成功");
                this.open = false;
                this.loading = false;
                this.getList();
              }else{
                this.errDialog(response.msg)
              }
            });
          }
        }
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/user/export', {
        ...this.queryParams
      }, `user_${new Date().getTime()}.xlsx`)
    },
    handleExport() {
      this.download('system/panUser/export', {
        ...this.queryParams
      }, `UserInfo_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
