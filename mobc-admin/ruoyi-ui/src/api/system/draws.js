import request from '@/utils/request'

export function getDrawsDetailList(query) {
  return request({
    url: '/system/draws/getDrawsDetailList',
    method: 'get',
    params: query
  })
}
