import request from '@/utils/request'

// 查询支付通道列表
export function listChannel(query) {
  return request({
    url: '/system/channel/list',
    method: 'get',
    params: query
  })
}

// 查询支付通道详细
export function getChannel(channelId) {
  return request({
    url: '/system/channel/' + channelId,
    method: 'get'
  })
}

// 新增支付通道
export function addChannel(data) {
  return request({
    url: '/system/channel',
    method: 'post',
    data: data
  })
}

// 修改支付通道
export function updateChannel(data) {
  return request({
    url: '/system/channel',
    method: 'put',
    data: data
  })
}

// 修改支付通道
export function setProxyChannel(data) {
  return request({
    url: '/system/channel/setProxy',
    method: 'post',
    data: data
  })
}

// 删除支付通道
export function delChannel(channelId) {
  return request({
    url: '/system/channel/' + channelId,
    method: 'delete'
  })
}
