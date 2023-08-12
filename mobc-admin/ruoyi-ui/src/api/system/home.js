import request from '@/utils/request'

// 查询主页列表
export function listHome(query) {
  return request({
    url: '/system/home/list',
    method: 'get',
    params: query
  })
}

// 查询主页详细
export function getHome(homeId) {
  return request({
    url: '/system/home/' + homeId,
    method: 'get'
  })
}

// 新增主页
export function addHome(data) {
  return request({
    url: '/system/home',
    method: 'post',
    data: data
  })
}

// 修改主页
export function updateHome(data) {
  return request({
    url: '/system/home',
    method: 'put',
    data: data
  })
}

// 删除主页
export function delHome(homeId) {
  return request({
    url: '/system/home/' + homeId,
    method: 'delete'
  })
}
