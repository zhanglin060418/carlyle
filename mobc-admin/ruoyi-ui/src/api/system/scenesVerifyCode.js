import request from '@/utils/request'

// 查询滚屏交易信息列表
export function listScenesVerifyCode(query) {
  return request({
    url: '/system/code/scenesVerifyCodeList',
    method: 'get',
    params: query
  })
}
