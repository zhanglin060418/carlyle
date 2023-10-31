import request from '@/utils/request'

// 登录方法
export function login(username, password, code, uuid) {
  const data = {
    username,
    password,
    code,
    uuid
  }
  return request({
    url: '/sysLogin',
    headers: {
      isToken: false
    },
    method: 'post',
    data: data
  })
}

// 注册方法
export function register(data) {
  return request({
    url: '/register',
    headers: {
      isToken: false
    },
    method: 'post',
    data: data
  })
}

// 获取用户详细信息
export function getInfo() {
  return request({
    url: '/getInfo',
    method: 'get'
  })
}



export function getTodayData(query) {
  return request({
    url: 'system/user/getTodayData',
    method: 'get',
    params: query
  })
}
export function gettodayDataByFrist(query) {
  return request({
    url: 'system/user/gettodayDataByFrist',
    method: 'get',
    params: query
  })
}
export function getTodayDataSecond(query) {
  return request({
    url: 'system/user/getTodayDataSecond',
    method: 'get',
    params: query
  })
}

export function getTodayDataThird(query) {
  return request({
    url: 'system/user/getTodayDataThird',
    method: 'get',
    params: query
  })
}

export function getTodayDataFour(query) {
  return request({
    url: 'system/user/getTodayDataFour',
    method: 'get',
    params: query
  })
}

export function getTodayDataFive(query) {
  return request({
    url: 'system/user/getTodayDataFive',
    method: 'get',
    params: query
  })
}

export function getTotalData(query) {
  return request({
    url: 'system/user/getTotalData',
    method: 'get',
    params: query
  })
}
getTotalData
export function getTotalDataByFrist(query) {
  return request({
    url: 'system/user/getTotalDataByFrist',
    method: 'get',
    params: query
  })
}
export function getTotalDataSecond(query) {
  return request({
    url: 'system/user/getTotalDataSecond',
    method: 'get',
    params: query
  })
}
export function getTotalDataThird(query) {
  return request({
    url: 'system/user/getTotalDataThird',
    method: 'get',
    params: query
  })
}
export function getAgentBalance(query) {
  return request({
    url: 'system/user/getAgentBalance',
    method: 'get',
    params: query
  })
}


// 退出方法
export function logout() {
  return request({
    url: '/logout',
    method: 'post'
  })
}

// 获取验证码
export function getCodeImg() {
  return request({
    url: '/captchaImage',
    headers: {
      isToken: false
    },
    method: 'get',
    timeout: 20000
  })
}
