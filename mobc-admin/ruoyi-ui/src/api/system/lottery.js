import request from '@/utils/request'

// 查询产品列表
export function listLottery(query) {
  return request({
    url: '/system/lottery/list',
    method: 'get',
    params: query
  })
}

// 查询产品详细
export function getLottery(id) {
  return request({
    url: '/system/lottery/' + id,
    method: 'get'
  })
}

// 修改产品
export function updateLottery(data) {
  return request({
    url: '/system/lottery',
    method: 'put',
    data: data
  })
}
