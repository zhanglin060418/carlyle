import request from '@/utils/request'

// 查询用户列表
export function listUser(query) {
  return request({
    url: '/system/panUser/listDetail',
    method: 'get',
    params: query
  })
}

// 查询用户列表
export function listUserReport(query) {
  return request({
    url: '/system/panUser/listUserReport',
    method: 'get',
    params: query
  })
}


export function getUserTeamList(data) {
  return request({
    url: '/system/panUser/getUserTeamInfo',
    method: 'post',
    params: data
  })
}

// 查询用户详细
export function getUser(id) {
  return request({
    url: '/system/panUser/' + id,
    method: 'get'
  })
}


// 修改用户
export function updateUser(data) {
  return request({
    url: '/system/panUser',
    method: 'put',
    data: data
  })
}

// 用户密码重置
export function resetUserPwd(userId, password) {
  const data = {
    userId,
    password
  }
  return request({
    url: '/system/panUser/resetPwd',
    method: 'put',
    data: data
  })
}

// 用户状态修改
export function changeUserStatus(userId, status) {
  const data = {
    userId,
    status
  }
  return request({
    url: '/system/panUser/changeStatus',
    method: 'put',
    data: data
  })
}

export function changeUserIsInviteCode(userId, isInviteCode) {
  const data = {
    userId,
    isInviteCode
  }
  return request({
    url: '/system/panUser/changeIsInviteCode',
    method: 'put',
    data: data
  })
}
export function changeUserIsWithdarw(userId, isWithdarw) {
  const data = {
    userId,
    isWithdarw
  }
  return request({
    url: '/system/panUser/changeIsWithdarw',
    method: 'put',
    data: data
  })
}
export function changeUserIsRebate(userId, isRebate) {
  const data = {
    userId,
    isRebate
  }
  return request({
    url: '/system/panUser/changeIsRebate',
    method: 'put',
    data: data
  })
}




