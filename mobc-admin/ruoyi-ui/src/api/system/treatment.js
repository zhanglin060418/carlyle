import request from '@/utils/request'

// 查询主页列表
export function listTreatment(query) {
  return request({
    url: '/system/treatment/list',
    method: 'get',
    params: query
  })
}

// 查询主页详细
export function getTreatment(id) {
  return request({
    url: '/system/treatment/' + id,
    method: 'get'
  })
}

// 新增主页
export function addTreatment(data) {
  return request({
    url: '/system/treatment',
    method: 'post',
    data: data
  })
}

// 修改主页
export function updateTreatment(data) {
  return request({
    url: '/system/treatment',
    method: 'put',
    data: data
  })
}

// 删除主页
export function delTreatment(id) {
  return request({
    url: '/system/treatment/' + id,
    method: 'delete'
  })
}
