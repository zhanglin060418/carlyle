import request from '@/utils/request'

// 查询提款列表
export function listWithdraw(query) {
  return request({
    url: '/system/withdraw/list',
    method: 'get',
    params: query
  })
}

// 查询提款详细
export function getWithdraw(withdrawId) {
  return request({
    url: '/system/withdraw/' + withdrawId,
    method: 'get'
  })
}


export function postBatchWithdraw(withdrawIds) {
  return request({
    url: '/system/withdraw/postBatchWithdraw',
    method: 'post',
    data: {ids:withdrawIds}
  })
}


export function handleChangeStatus(data) {
  return request({
    url: '/system/withdraw/changeStatus',
    method: 'put',
    data: data
  })
}

export function handleChangeStatusRetry(data) {
  return request({
    url: '/system/withdraw/changeStatusRetry',
    method: 'put',
    data: data
  })
}

// 新增提款
export function addWithdraw(data) {
  return request({
    url: '/system/withdraw',
    method: 'post',
    data: data
  })
}

// 修改提款
export function updateWithdraw(data) {
  return request({
    url: '/system/withdraw',
    method: 'put',
    data: data
  })
}

// 删除提款
export function delWithdraw(withdrawId) {
  return request({
    url: '/system/withdraw/' + withdrawId,
    method: 'delete'
  })
}
