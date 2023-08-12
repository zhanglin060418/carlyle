<template>
  <div>
    <!-- 暂时不用这个组件，直接换成html使用 -->
  </div>
</template>
<script>

export default {
  mounted() {
    var captcha = new TencentCaptcha(
      '2077734432',
      function (res) {
        /* callback */
        if (res.ticket == '') {
          if (window.webkit) {
            window.webkit.messageHandlers.toBack.postMessage({})
          } else if (window.appInterface) {
            window.appInterface.toBack({})
          }
        } else {
          if (res && res.ret === 0) {
            // 获取票据、随机数并调用App端注入的方法传入票据、随机数，进行后台票据校验
            var result = { randstr: res.randstr, ticket: res.ticket }
            window.jsBridge.getData(JSON.stringify(result))
          }
        }
      },
      {
        needFeedBack: false,
      }
    )
    captcha.show() // 显示验证码
  },
}
</script>
