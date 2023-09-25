import request from '@/utils/request'

// 查询社区信息列表
export function listMessage(query) {
  return request({
    url: '/system/message/list',
    method: 'get',
    params: query
  })
}

// 查询社区信息详细
export function getMessage(messageId) {
  return request({
    url: '/system/message/' + messageId,
    method: 'get'
  })
}

// 新增社区信息
export function addMessage(data) {
  return request({
    url: '/system/message',
    method: 'post',
    data: data
  })
}

// 修改社区信息
export function updateMessage(data) {
  return request({
    url: '/system/message',
    method: 'put',
    data: data
  })
}

// 删除社区信息
export function delMessage(messageId) {
  return request({
    url: '/system/message/' + messageId,
    method: 'delete'
  })
}
