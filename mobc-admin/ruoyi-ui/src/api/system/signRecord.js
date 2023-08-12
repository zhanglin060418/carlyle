import request from '@/utils/request'

// 查询签到记录列表
export function listSignRecord(query) {
  return request({
    url: '/system/signRecord/list',
    method: 'get',
    params: query
  })
}

