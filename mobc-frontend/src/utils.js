export default {
    add0(m) {
        return m < 10 ? '0' + m : m
    },
    ///时间戳 转化-4时区时间戳
    serverTime(time) {
        //参数i为时区值数字，比如北京为东八区则输进8,西5输入-5
        // if (typeof i !== 'number') return;
        var d = new Date(time)
            //得到1970年一月一日到现在的秒数
        var len = d.getTime()
            //本地时间与GMT时间的时间偏移差
        var offset = d.getTimezoneOffset() * 60000
            //得到现在的格林尼治时间
        var utcTime = len + offset
        return utcTime + 3600000 * 7
    },
    ///本地时间转服务器时间
    localToServer() {
        var d = new Date()
            //得到1970年一月一日到现在的秒数
        var len = d.getTime()
            //本地时间与GMT时间的时间偏移差
        var offset = d.getTimezoneOffset() * 60000
            //得到现在的格林尼治时间
        var utcTime = len + offset
        return utcTime + 3600000 * -2.5
    },
    ///本地时间转服务器时间
    toLocalTime(time) {
        // const toDate = new Date("2020-09-10 06:26:59");
        var d = this.formattime(time)
        var localTime = time
        var localOffset = d.getTimezoneOffset() * 60000 //getTimezoneOffset()返回是以分钟为单位，需要转化成ms
        var utc = localTime + localOffset
        var offset = 7 //以韩国时间为例，东9区
        var korean = utc + 3600000 * offset
        var nd = new Date(korean)
        let YY = nd.getFullYear() + '年'
        let MM = nd.getMonth() + 1 + '月'
        let dd = nd.getDate() + '日  '
        let hh = nd.getHours() + ':'
        let mm = nd.getMinutes() + ':'
        let ss = nd.getSeconds()
        return YY + '-' + MM + '-' + dd + ' ' + hh + ':' + mm + ':' + ss
    },
    ///
    formattime(shijianchuo, flag = false) {
        let add0 = this.add0
            //shijianchuo是整数，否则要parseInt转换
        var time = new Date(Number(shijianchuo))
        var y = time.getFullYear()
        var m = time.getMonth() + 1
        var d = time.getDate()
        var h = time.getHours()
        var mm = time.getMinutes()
        var s = time.getSeconds()
        if (s == 59) {
            return (
                add0(y) +
                '-' +
                add0(m) +
                '-' +
                add0(d) +
                ' ' +
                add0(h) +
                ':' +
                add0(flag ? mm + 1 : mm) +
                ':' +
                add0(flag ? 0 : s)
            )
        }
        return (
            add0(y) +
            '-' +
            add0(m) +
            '-' +
            add0(d) +
            ' ' +
            add0(h) +
            ':' +
            add0(mm) +
            ':' +
            add0(flag ? s + 1 : s)
        )
    },

    formattimestr(shijianchuo, flag, isHours = true) {
        let add0 = this.add0
            //shijianchuo是整数，否则要parseInt转换
        var time = new Date(this.serverTime(shijianchuo))
        var y = time.getFullYear()
        var m = time.getMonth() + 1
        var d = time.getDate()
        var h = time.getHours()
        var mm = time.getMinutes()
        var s = time.getSeconds()
        if (s == 59) {
            return (
                add0(y) +
                '-' +
                add0(m) +
                '-' +
                add0(d) +
                ' ' +
                add0(h) +
                ':' +
                add0(flag ? mm + 1 : mm) +
                ':' +
                add0(flag ? 0 : s)
            )
        }
        if (isHours) {
            return (
                add0(y) +
                '-' +
                add0(m) +
                '-' +
                add0(d) +
                ' ' +
                add0(h) +
                ':' +
                add0(mm) +
                ':' +
                add0(flag ? s + 1 : s)
            )
        } else {
            return add0(y) + '-' + add0(m) + '-' + add0(d)
        }
    },

    getPstr(val) {
        val = Math.floor(val)
        let strVal = String(val)
        if (strVal.length >= 5) {
            const reg = /(?=(\B)(\d{3})+$)/g
            return strVal.replace(reg, '.')
        }
        return val
    },
    getJt(val) {
        val = Math.floor(val)
        let len = String(val).length
        if (len > 6) {
            let sm = String(val / 1000000)
            let re = /([0-9]+\.[0-9]{1})[0-9]*/
            sm = sm.replace(re, '$1')
            return sm + 'JT'
        } else if (len >= 4 && len <= 6) {
            let sm = String(val / 1000)
            let re = /([0-9]+\.[0-9]{1})[0-9]*/
            sm = sm.replace(re, '$1')
            return sm + 'K'
        } else {
            return val
        }
        // val = Math.floor(val)
        // let strVal = String(val)
        // if (strVal.length >= 5) {
        //   const reg = /(?=(\B)(\d{3})+$)/g
        //   return strVal.replace(reg, ',')
        // }
        // return val
    },
    getkStr(val, n = 0) {
        // val = Math.floor(val)
        // let len = String(val).length
        // if (len > 6) {
        //     let sm = String(val / 1000000)
        //     let re = /([0-9]+\.[0-9]{1})[0-9]*/
        //     sm = sm.replace(re, '$1')
        //     return sm + 'JT'
        // } else if (len >= 4 && len <= 6) {
        //     let sm = String(val / 1000)
        //     let re = /([0-9]+\.[0-9]{1})[0-9]*/
        //     sm = sm.replace(re, '$1')
        //     return sm + 'K'
        // } else {
        //     return val
        // }

        let strVal = String(val)
        let idx = strVal.indexOf('.')
        let ispoint = idx > 0
        strVal = String(
            n == 0 ?
            Math.floor(val) :
            ispoint ?
            strVal.substring(0, idx + n + 1) :
            Math.floor(val)
        )
        let f = '',
            l = '',
            newVal = strVal
        if (ispoint) {
            f = strVal.split('.')[0]
            l = strVal.split('.')[1]
            newVal = f
        }
        if (newVal.length >= 5) {
            const reg = /(?=(\B)(\d{3})+$)/g
            let v = newVal.replace(reg, ',')
            if (l) {
                return v + '.' + l
            }
            return v
        } else {
            newVal = strVal
        }
        return newVal
    },
    openWindow(url, targetType = '_blank', id = 'open', download = false) {
        // 如果存在则删除
        if (document.getElementById(id)) {
            document.body.removeChild(document.getElementById(id))
        }
        const a = document.createElement('a')
        a.setAttribute('href', url)
        if (download) {
            a.setAttribute('download', url)
        }
        a.setAttribute('target', targetType)
        a.setAttribute('id', id)
        document.body.appendChild(a)
        a.click()
    },
    /**
     ** 加法函数，用来得到精确的加法结果
     ** 说明：javascript的加法结果会有误差，在两个浮点数相加的时候会比较明显。这个函数返回较为精确的加法结果。
     ** 调用：accAdd(arg1,arg2)
     ** 返回值：arg1加上arg2的精确结果
     **/
    accAdd(arg1, arg2) {
        var r1, r2, m, c
        try {
            r1 = arg1.toString().split('.')[1].length
        } catch (e) {
            r1 = 0
        }
        try {
            r2 = arg2.toString().split('.')[1].length
        } catch (e) {
            r2 = 0
        }
        c = Math.abs(r1 - r2)
        m = Math.pow(10, Math.max(r1, r2))
        if (c > 0) {
            var cm = Math.pow(10, c)
            if (r1 > r2) {
                arg1 = Number(arg1.toString().replace('.', ''))
                arg2 = Number(arg2.toString().replace('.', '')) * cm
            } else {
                arg1 = Number(arg1.toString().replace('.', '')) * cm
                arg2 = Number(arg2.toString().replace('.', ''))
            }
        } else {
            arg1 = Number(arg1.toString().replace('.', ''))
            arg2 = Number(arg2.toString().replace('.', ''))
        }
        return (arg1 + arg2) / m
    },
    /**
     ** 减法函数，用来得到精确的减法结果
     ** 说明：javascript的减法结果会有误差，在两个浮点数相减的时候会比较明显。这个函数返回较为精确的减法结果。
     ** 调用：accSub(arg1,arg2)
     ** 返回值：arg1加上arg2的精确结果
     **/
    accSub(arg1, arg2) {
        var r1, r2, m, n
        try {
            r1 = arg1.toString().split('.')[1].length
        } catch (e) {
            r1 = 0
        }
        try {
            r2 = arg2.toString().split('.')[1].length
        } catch (e) {
            r2 = 0
        }
        m = Math.pow(10, Math.max(r1, r2)) //last modify by deeka //动态控制精度长度
        n = r1 >= r2 ? r1 : r2
        return ((arg1 * m - arg2 * m) / m).toFixed(n)
    },
    /**
     ** 乘法函数，用来得到精确的乘法结果
     ** 说明：javascript的乘法结果会有误差，在两个浮点数相乘的时候会比较明显。这个函数返回较为精确的乘法结果。
     ** 调用：accMul(arg1,arg2)
     ** 返回值：arg1乘以 arg2的精确结果
     **/
    accMul(arg1, arg2, n = 2) {
        var m = 0,
            s1 = arg1.toString(),
            s2 = arg2.toString()
        try {
            m += s1.split('.')[1].length
        } catch (e) {}
        try {
            m += s2.split('.')[1].length
        } catch (e) {}
        let val =
            (Number(s1.replace('.', '')) * Number(s2.replace('.', ''))) /
            Math.pow(10, m)

        let strVal = String(val)
        let idx = strVal.indexOf('.')
        let ispoint = idx > 0
        strVal = String(
            n == 0 ?
            Math.floor(val) :
            ispoint ?
            strVal.substring(0, idx + n + 1) :
            Math.floor(val)
        )
        return strVal
    },
    getCurrentTimeStr(em = '-') {
        let add0 = this.add0
        let date = new Date()
        let m = date.getMonth() + 1
        let d = date.getDate()
        return date.getFullYear() + em + add0(m) + em + add0(d)
    },
    //时间差计算
    /*
     * startDate==>开始时间
     * endDate==>结束时间
     * 事例：diffTime(data.createTime,new Date())
     *
     * */
    diffTime(startDate, endDate) {
        var diff = endDate - startDate //.getTime();//时间差的毫秒数

        //计算出相差天数
        var days = Math.floor(diff / (24 * 3600 * 1000))

        //计算出小时数
        var leave1 = diff % (24 * 3600 * 1000) //计算天数后剩余的毫秒数
        var hours = Math.floor(leave1 / (3600 * 1000))
            //计算相差分钟数
        var leave2 = leave1 % (3600 * 1000) //计算小时数后剩余的毫秒数
        var minutes = Math.floor(leave2 / (60 * 1000))

        //计算相差秒数
        var leave3 = leave2 % (60 * 1000) //计算分钟数后剩余的毫秒数
        var seconds = Math.round(leave3 / 1000)

        // var returnStr = seconds + '秒前'
        // if (minutes > 0) {
        //     returnStr = minutes + '分钟前' //+ returnStr;
        // }
        // if (hours > 0) {
        //     returnStr = hours + '小时前' // + returnStr;
        // }
        // if (days > 0) {
        //     returnStr = days + '天前' //+ returnStr;
        // }
        return { days, hours, minutes, seconds }
    },
    changetime(value) {
        var secondTime = parseInt(value) // 秒
        var minuteTime = 0 // 分
        var hourTime = 0 // 小时
        if (secondTime > 60) {
            //如果秒数大于60，将秒数转换成整数
            //获取分钟，除以60取整数，得到整数分钟
            minuteTime = parseInt(secondTime / 60)
                //获取秒数，秒数取佘，得到整数秒数
            secondTime = parseInt(secondTime % 60)
                //如果分钟大于60，将分钟转换成小时
            if (minuteTime > 60) {
                //获取小时，获取分钟除以60，得到整数小时
                hourTime = parseInt(minuteTime / 60)
                    //获取小时后取佘的分，获取分钟除以60取佘的分
                minuteTime = parseInt(minuteTime % 60)
            }
        }
        var time = '' + parseInt(secondTime) + ''

        if (minuteTime > 0) {
            time = '' + parseInt(minuteTime) + ':' + time
        }
        if (hourTime > 0) {
            time = '' + parseInt(hourTime) + ':' + time
        }
        return time
    },
    getTime(hours = null) {
        let d = new Date()
        let year = d.getFullYear()
        let month = d.getMonth() + 1
        let date = d.getDate()
        let getDate = year + '-' + month + '-' + date
        let h = d.getHours() > 9 ? d.getHours() : '0' + d.getHours()
        let min = d.getMinutes() > 9 ? d.getMinutes() : '0' + d.getMinutes()
        let currentTime = hours ?
            getDate + ' ' + hours :
            getDate + ' ' + h + ':' + min
            //处理ios时间问题
        if (typeof currentTime == 'string' && currentTime.indexOf('-') != -1) {
            currentTime = currentTime.replace(/-/g, '/')
        }
        return new Date(currentTime).getTime()
    },
    union(arr) {
        if (!Array.isArray(arr)) {
            console.log('type error!')
            return
        }
        var array = []
        for (var i = 0; i < arr.length; i++) {
            if (!array.includes(arr[i])) {
                //includes 检测数组是否有某个值
                array.push(arr[i])
            }
        }
        return array
    },
}
