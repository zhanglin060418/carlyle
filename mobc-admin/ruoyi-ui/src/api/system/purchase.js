import request from '@/utils/request'

// 查询产品支付列表
export function listPurchase(query) {
  return request({
    url: '/system/purchase/list',
    method: 'get',
    params: query
  })
}

export function getTotalPurchase(query) {
  return request({
    url: '/system/purchase/getTotalPurchase',
    method: 'get',
    params: query
  })
}

// 查询产品支付详细
export function getPurchase(id) {
  return request({
    url: '/system/purchase/' + id,
    method: 'get'
  })
}

// 新增产品支付
export function addPurchase(data) {
  return request({
    url: '/system/purchase',
    method: 'post',
    data: data
  })
}

// 修改产品支付
export function updatePurchase(data) {
  return request({
    url: '/system/purchase',
    method: 'put',
    data: data
  })
}

// 删除产品支付
export function delPurchase(id) {
  return request({
    url: '/system/purchase/' + id,
    method: 'delete'
  })
}
