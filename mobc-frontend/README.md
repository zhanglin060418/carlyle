# app-h5

移动端交互 h5 页面输出轻应用。

## 跳转链接：

https://m.thewinners.cc/redirect.html?router=ingold&token=&theme=dark&locale=en&phone=55 ####参数说明

> - router:跳转对应的页面（路由）比如：上面法币充值页面 router=ingold
> - theme:主题（dark/light）
> - locale:语言（zh/en）
> - token:登录 token
> - phone:标识是 app 内嵌 h5 （此参数也可以不用）

#### p：不需要获取用户信息的不用传 token

- 现有链接地址：m.bigwinner.live 是线上地址 m.thewinners.cc 测试地址

1. [规则页面](https://m.thewinners.cc/redirect.html?router=rule&theme=dark&locale=en&phone=55):https://m.thewinners.cc/redirect.html?router=rule&theme=dark&locale=en&phone=55

2. [协议页面](https://m.thewinners.cc/redirect.html?router=xieyi&theme=dark&locale=en&phone=55):https://m.thewinners.cc/redirect.html?router=xieyi&theme=dark&locale=en&phone=55

3. [帮助中心](https://m.thewinners.cc/redirect.html?router=helpcenter&theme=dark&locale=en&phone=55):https://m.thewinners.cc/redirect.html?router=helpcenter&theme=dark&locale=en&phone=55

4. [会员等级](https://m.thewinners.cc/redirect.html?router=level&theme=dark&locale=en&token=&phone=55):https://m.thewinners.cc/redirect.html?router=level&theme=dark&locale=en&phone=55& **token=**

5. [充提币](https://m.thewinners.cc/redirect.html?router=ingold&theme=dark&locale=en&token=&phone=55):https://m.thewinners.cc/redirect.html?router=ingold&theme=dark&locale=en&phone=55& **token=**

`// 设置导航栏头部内容 if (window.webkit) { window.webkit.messageHandlers.setNavTitle.postMessage('') } else if (window.appInterface) { window.appInterface.setNavTitle('') }`
