import request from '@/utils/request'

// 查询用户银行卡列表
export function listCard(query) {
  return request({
    url: '/system/card/list',
    method: 'get',
    params: query
  })
}


export function listCardRecord(query) {
  return request({
    url: '/system/card/cardRecordList',
    method: 'get',
    params: query
  })
}

// 查询用户银行卡详细
export function getCard(cardId) {
  return request({
    url: '/system/card/' + cardId,
    method: 'get'
  })
}


// 新增用户银行卡
export function addCard(data) {
  return request({
    url: '/system/card',
    method: 'post',
    data: data
  })
}

// 修改用户银行卡
export function updateCard(data) {
  return request({
    url: '/system/card',
    method: 'put',
    data: data
  })
}

// 删除用户银行卡
export function delCard(cardId) {
  return request({
    url: '/system/card/' + cardId,
    method: 'delete'
  })
}
