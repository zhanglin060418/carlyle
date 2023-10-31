<template>
  <div class="app-container" v-loading="loading">
    <table>
      <thead>
      <tr>
        <th>代理商账号</th>
        <th>处理前金额</th>
        <th>金额</th>
        <th>处理后金额</th>
        <th>业务类型</th>
        <th>处理时间</th>
      </tr>
      </thead>
      <tbody>
      <!-- 表格数据行 -->
      <tr v-for="item in detailList">
        <td>{{ item.agentName }}</td>
        <td>{{ parseFloat(item.amountBefore) / 100 }}</td>
        <td>{{ parseFloat(item.amount) / 100 }}</td>
        <td>{{ parseFloat(item.amountAfter) / 100 }}</td>
        <td>
          <span v-if="item.transType== 'Prepaid_Recharge'" >预付款充值</span>
          <span v-else-if="item.transType == 'Manual_Adjustment'">手工调账</span>
        </td>
        <td>{{ item.transactionDate}}</td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
  import { getBalanceDetailList } from "@/api/system/agentBalance";
  export default {
    name: "ChildComponent",
    data () {
      return {
        loading: false,
        detailList: [],
      }
    },
    methods: {
      getAgentBalanceDetail(row) {
        const agentId = row.agentId
        getBalanceDetailList({agentId: agentId}).then(response => {
          if(response.code==200){
            this.detailList = response.detailList;
          }else{
            this.errDialog(response.msg)
          }
        })
      },
    }
  };
</script>
<style>
  table{
    width: 100%;
    border-collapse: collapse;
  }

  table caption{
    font-size: 2em;
    font-weight: bold;
    margin: 1em 0;
  }

  th,td{
    border: 1px solid #999;
    text-align: center;
    padding: 10px 0;
  }

  table thead tr{
    background-color: #f8f8f9;
    color: #515a6e;
    height: 40px;
    font-size: 15px;
    border-right: 1px solid #dfe6ec;
  }
  table tbody tr{
    height: 40px;
  }
  table tbody tr:nth-child(odd){
    background-color: #eee;
  }

  table tbody tr:hover{
    background-color: #ccc;
  }

  table tfoot tr td{
    text-align: right;
    padding-right: 15px;
  }

</style>
