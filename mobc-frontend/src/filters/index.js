/**
 * 保留两位，如果是整数就取整
 */
const fomatFloat = num => {
  let n = 2 //保留几位
  var f = parseFloat(num)
  if (isNaN(f)) {
    return false
  }
  f = Math.round(num * Math.pow(10, n)) / Math.pow(10, n) // n 幂
  return f
}
// 保留两位，如果是整数也取两位
const fomatFloat2 = num => {
  let n = 2 //保留几位
  let f = parseFloat(num)
  if (isNaN(f)) {
    return false
  }
  f = Math.round(num * Math.pow(10, n)) / Math.pow(10, n) // n 幂
  let s = f.toString()
  let rs = s.indexOf('.')
  //判定如果是整数，增加小数点再补0
  if (rs < 0) {
    rs = s.length
    s += '.'
  }
  while (s.length <= rs + n) {
    s += '0'
  }
  return s
}
const dateFormat = (date, fmt) => {
  if (date == null || date == '' || date == undefined) return null
  try {
    //解决ios手机时间格式化NAN问题
    if (typeof date == 'string' && date.indexOf('-') != -1) {
      date = date.replace(/-/g, '/')
    }
    date = new Date(date)
  } catch (e) {
    date = date
  }
  fmt = fmt ? fmt : 'yyyy-MM-dd'
  var o = {
    'M+': date.getMonth() + 1, //月份
    'd+': date.getDate(), //日
    'H+': date.getHours(), //小时
    'm+': date.getMinutes(), //分
    's+': date.getSeconds(), //秒
    'q+': Math.floor((date.getMonth() + 3) / 3), //季度
    S: date.getMilliseconds(), //毫秒
  }
  if (/(y+)/.test(fmt))
    fmt = fmt.replace(
      RegExp.$1,
      (date.getFullYear() + '').substr(4 - RegExp.$1.length)
    )
  for (var k in o)
    if (new RegExp('(' + k + ')').test(fmt))
      fmt = fmt.replace(
        RegExp.$1,
        RegExp.$1.length == 1 ? o[k] : ('00' + o[k]).substr(('' + o[k]).length)
      )
  return fmt
}
const formatTime = (date, fmt = 'yyyy-MM-dd') => {
  return dateFormat(date, fmt)
}
/**
 * 保留小数位，默认两位
 * @param {*} num
 * @param {*} n
 */
const toFixedStr = (num, n = 2) => {
  num = Number(num) || 0
  num = num.toString()
  let result = ''
  if (n == 0) {
    return num.split('.')[0]
  }
  let zeroResult = n => {
    let zero = ''
    for (let i = 0; i < n; i++) {
      zero += '0'
    }
    return zero
  }
  if (num % 1 == 0) {
    //整数
    result = num + '.' + zeroResult(n)
  } else {
    //小数
    let num1 = num.split('.')
    if (num1.length > 0 && num1[1].length < n) {
      result = num1[0] + '.' + num1[1] + zeroResult(n - num1[1].length)
    } else {
      result = num1[0] + '.' + num1[1].substring(0, n)
    }
  }
  return result
}
const strAbb = (str, mark = '****') => {
  if (!str) return ''
  return str.substring(0, 2) + mark + str.substring(str.length - 3, str.length)
}
export { fomatFloat, fomatFloat2, dateFormat, formatTime, toFixedStr, strAbb }
