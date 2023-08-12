import request from '@/utils/request'

// 查询【请填写功能名称】列表
export function listHistory(query) {
  return request({
    url: '/system/history/list',
    method: 'get',
    params: query
  })
}


export function listCommission(query) {
  return request({
    url: '/system/history/listCommission',
    method: 'get',
    params: query
  })
}

export function listInterest(query) {
  return request({
    url: '/system/history/listInterest',
    method: 'get',
    params: query
  })
}


