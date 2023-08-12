import request from '@/utils/request'

export function rechargeList(query) {
  return request({
    url: '/system/team/rechargeList',
    method: 'get',
    params: query
  })
}

export function rechargeListByClerk(query) {
  return request({
    url: '/system/team/rechargeListByClerk',
    method: 'get',
    params: query
  })
}

export function getUserTeamTrans(data) {
  return request({
    url: '/system/team/getUserTeamTrans',
    method: 'post',
    params: data
  })
}


export function getClerkTeamTransInfo(query) {
  return request({
    url: '/system/team/getClerkTeamTransInfo',
    method: 'get',
    params: query
  })
}




