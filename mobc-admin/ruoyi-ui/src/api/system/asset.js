import request from '@/utils/request'

// 查询增值宝列表
export function listAsset(query) {
  return request({
    url: '/system/asset/list',
    method: 'get',
    params: query
  })
}

// 查询增值宝详细
export function getAsset(userAssetId) {
  return request({
    url: '/system/asset/' + userAssetId,
    method: 'get'
  })
}

