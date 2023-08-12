import request from '@/utils/request'

// 查询【请填写功能名称】列表
export function listCode(query) {
  return request({
    url: '/system/code/list',
    method: 'get',
    params: query
  })
}

// 查询【请填写功能名称】详细
export function getCode(phoneNo) {
  return request({
    url: '/system/code/' + phoneNo,
    method: 'get'
  })
}

// 新增【请填写功能名称】
export function addCode(data) {
  return request({
    url: '/system/code',
    method: 'post',
    data: data
  })
}

// 修改【请填写功能名称】
export function updateCode(data) {
  return request({
    url: '/system/code',
    method: 'put',
    data: data
  })
}

// 删除【请填写功能名称】
export function delCode(phoneNo) {
  return request({
    url: '/system/code/' + phoneNo,
    method: 'delete'
  })
}
