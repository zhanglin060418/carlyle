import request from '@/utils/request'

// 查询滚屏交易信息列表
export function listScrollTransInfo(query) {
  return request({
    url: '/system/scrollTransInfo/list',
    method: 'get',
    params: query
  })
}

// 查询滚屏交易信息详细
export function getScrollTransInfo(transId) {
  return request({
    url: '/system/scrollTransInfo/' + transId,
    method: 'get'
  })
}

// 新增滚屏交易信息
export function addScrollTransInfo(data) {
  return request({
    url: '/system/scrollTransInfo',
    method: 'post',
    data: data
  })
}

// 修改滚屏交易信息
export function updateScrollTransInfo(data) {
  return request({
    url: '/system/scrollTransInfo',
    method: 'put',
    data: data
  })
}

// 删除滚屏交易信息
export function delScrollTransInfo(transId) {
  return request({
    url: '/system/scrollTransInfo/' + transId,
    method: 'delete'
  })
}
