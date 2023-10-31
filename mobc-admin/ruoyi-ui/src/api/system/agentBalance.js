import request from '@/utils/request'

// 查询用户余额列表
export function listAgentBalance(query) {
  return request({
    url: '/system/agentBalance/listDetail',
    method: 'get',
    params: query
  })
}

// 查询用户余额详细
export function getAgentBalance(id) {
  return request({
    url: '/system/agentBalance/' + id,
    method: 'get'
  })
}

// 修改用户余额
export function updateAgentBalance(data) {
  return request({
    url: '/system/agentBalance',
    method: 'put',
    data: data
  })
}

export function getBalanceDetailList(data) {
  return request({
    url: '/system/agentBalance/getBalanceDetailList',
    method: 'post',
    params: data
  })
}

