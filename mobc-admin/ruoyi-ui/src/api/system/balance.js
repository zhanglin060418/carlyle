import request from '@/utils/request'

// 查询用户余额列表
export function listBalance(query) {
  return request({
    url: '/system/balance/listDetail',
    method: 'get',
    params: query
  })
}

// 查询用户余额详细
export function getBalance(userBalanceId) {
  return request({
    url: '/system/balance/' + userBalanceId,
    method: 'get'
  })
}

// 新增用户余额
export function addBalance(data) {
  return request({
    url: '/system/balance',
    method: 'post',
    data: data
  })
}

export function productReward(data) {
  return request({
    url: '/system/balance/productReward',
    method: 'post',
    data: data
  })
}

// 修改用户余额
export function updateBalance(data) {
  return request({
    url: '/system/balance',
    method: 'put',
    data: data
  })
}

// 删除用户余额
export function delBalance(userBalanceId) {
  return request({
    url: '/system/balance/' + userBalanceId,
    method: 'delete'
  })
}
