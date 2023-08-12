import axios from 'axios'
import router from '../router'
import store from '../store'
import { Toast } from 'vant'
import qs from 'qs'

//  var requesting = []
//  var limitTime = 2000 //請求間隔時間
//  var requestingId = ''
//  var time = null;
let baseURl =
  process.env.NODE_ENV === 'production'
    ? '/prod-api'
      : process.env.NODE_ENV === 'uat'
    ? process.env.NODE_ENV_URL
    : 'http://localhost:8087/'
axios.defaults.timeout = 15000 //請求過期時間。
axios.defaults.baseURL = baseURl

// 添加请求拦截器
axios.interceptors.request.use(function(config) {
  config.headers['locale'] = localStorage.getItem('locale') || 'en_US'
  let token = localStorage.getItem('token')
  if (token) {
    config.headers['Authorization'] = token
  }
  return config
})
// const toLogo = () => {
//   if (window.webkit) {
//     window.webkit.messageHandlers.toLogin.postMessage({})
//     return
//   } else if (window.appInterface) {
//     window.appInterface.toLogin({})
//     return
//   } else {
//     Toast({
//       message:
//         'Your account login invalid，and will be switched to demo account.',
//       duration: 1000,
//       forbidClick: true,
//     })
//     router.replace({
//       path: '/login',
//       query: {
//         redirect: router.currentRoute.fullPath,
//       },
//     })
//     return
//   }
// }

const removeCache = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('userId')
  localStorage.removeItem('USER_INFO')
  localStorage.removeItem('limit_time')
  localStorage.removeItem('video_time')
  localStorage.removeItem('video_play_id')
  localStorage.removeItem('payment_password')
  sessionStorage.clear()
  store.commit('user/quitLogin')
}
/**
 * 添加响应拦截器，统一处理服务器响应和异常
 */
axios.interceptors.response.use(
  async response => {
    let code = response.data.code
    if(code === 401) {
      console.log(response)
      if (!response.config.headers["x-refresh"]) {
        const refresh_token = localStorage.getItem('refresh_token')
        const {access_token} = await axios.post("/refresh_token", {refresh_token}, {
          headers: {
            "x-refresh": true,
          }
        });
        if (access_token) {
          const resp = await axios({
            ...response.config,
            headers: {...response.config.headers, Authorization: access_token, "x-refresh": true}
          });
          if (resp.data.code !== 401) {
            return resp;
          }
        }
      }
      removeCache()
      let currentRoute = router.currentRoute.fullPath
      if (currentRoute != '/home') {
        Toast({
          // message: '登录过期，请重新登录',
          message: 'Login expired, please login again',
          duration: 3000,
          forbidClick: true,
        })
        router.replace({
          path: '/login',
        })
      }
      return Promise.reject('Invalid session, or session expired, please log in again.')
    }
    else {
      return response
    }
  },
  error => {
    switch (error.response.code) {
      // 401: 未登录
      // 未登录则跳转登录页面，并携带当前页面的路径
      // 在登录成功后返回当前页面，这一步需要在登录页操作。
      case 401:
        removeCache()
        let currentRoute = router.currentRoute.fullPath
        if (currentRoute != '/home') {
          router.replace({
            path: '/login',
          })
        }
        break
      // 403 token过期
      // 登录过期对用户进行提示
      // 清除本地token和清空vuex中token对象
      // 跳转登录页面
      case 403:
        removeCache()
        Toast({
          // message: '登录过期，请重新登录',
          message: 'Login expired, please login again',
          duration: 1000,
          forbidClick: true,
        })

        // 跳转登录页面，并将要浏览的页面fullPath传过去，登录成功后跳转需要访问的页面
        // setTimeout(() => {
        //   toLogo()
        // }, 1000)
        break

      // 404请求不存在
      case 404:
        Toast({
          message: 'No network request',
          duration: 1500,
          forbidClick: true,
        })
        break
      // 其他错误，直接抛出错误提示
      default:
        Toast({
          message: error.response.data.message || 'Network anomaly',
          duration: 1500,
          forbidClick: true,
        })
    }

    return Promise.reject(error)
  }
)

export default {
  /**
   * get方法，对应get请求
   * @param {String} url [请求的url地址]
   * @param {Object} params [请求时携带的参数]
   */
  get(url, params, isArray = false) {
    return new Promise((resolve, reject) => {
      axios
        .get(url, {
          params: params,
          paramsSerializer: params => {
            return qs.stringify(params, { indices: false })
          },
        })
        .then(res => {
          resolve(res.data)
        })
        .catch(err => {
          reject(err)
        })
    })
  },
  /**
   * post方法，对应post请求
   * @param {String} url [请求的url地址]
   * @param {Object} params [请求时携带的参数]
   */
  post(url, params) {
    return new Promise((resolve, reject) => {
      axios
        .post(url, params)
        .then(res => {
          resolve(res.data)
        })
        .catch(err => {
          reject(err)
        })
    })
  },
  put(url, params) {
    return new Promise((resolve, reject) => {
      axios
        .put(url, params)
        .then(res => {
          resolve(res.data)
        })
        .catch(err => {
          reject(err)
        })
    })
  },
  delete(url, params, isArray = false) {
    return new Promise((resolve, reject) => {
      axios
        .delete(url, {
          params: params,
          paramsSerializer: params => {
            return qs.stringify(params, { indices: false })
          },
        })
        .then(res => {
          resolve(res.data)
        })
        .catch(err => {
          reject(err)
        })
    })
  },
  post2(url, params) {
    return new Promise((resolve, reject) => {
      axios
        .post(url, params, {
          timeout: 5000 * 3,
        })
        .then(res => {
          resolve(res.data)
        })
        .catch(err => {
          reject(err)
        })
    })
  },
  /**
   * postFormData方法，对应post请求，用来提交文件+数据
   * @param {String} url [请求的url地址]
   * @param {Object} params [请求时携带的参数]
   */
  postFormData(url, params) {
    return new Promise((resolve, reject) => {
      axios({
        headers: {
          'Content-Type': 'multipart/form-data', //
          Authorization: localStorage.getItem('token'),
        },
        transformRequest: [
          function(data) {
            // 在请求之前对data传参进行格式转换
            const formData = new FormData()
            Object.keys(data).forEach(key => {
              formData.append(key, data[key])
            })
            return formData
          },
        ],
        url,
        method: 'post',
        data: params,
      })
        .then(res => {
          resolve(res.data)
        })
        .catch(err => {
          reject(err)
        })
    })
  },
}
