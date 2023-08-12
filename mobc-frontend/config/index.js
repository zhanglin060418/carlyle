'use strict'
// Template version: 1.3.1
// see http://vuejs-templates.github.io/webpack for documentation.

const path = require('path')

module.exports = {
    dev: {
        // Paths
        assetsSubDirectory: 'static',
        assetsPublicPath: '/',
        proxyTable: {
            '/trade': {
                target: 'https://wwwsuperwind.com/trade', // uat地址
                // target: 'http://192.168.106.197:9100/', // bendi
                changeOrigin: true,
                secure: true,
                pathRewrite: {
                    '^/trade': '',
                },
            },
        },
        // Various Dev Server settings
        host: 'localhost', // can be overwritten by process.env.HOST
        // host: '107.148.29.231', // can be overwritten by process.env.HOST
        port: 8090, // can be overwritten by process.env.PORT, if port is in use, a free one will be determined
        autoOpenBrowser: false,
        errorOverlay: true,
        notifyOnErrors: true,
        poll: false, // https://webpack.js.org/configuration/dev-server/#devserver-watchoptions-

        /**
         * Source Maps
         */

        // https://webpack.js.org/configuration/devtool/#development
        devtool: 'cheap-module-eval-source-map',

        // If you have problems debugging vue-files in devtools,
        // set this to false - it *may* help
        // https://vue-loader.vuejs.org/en/options.html#cachebusting
        cacheBusting: true,

        cssSourceMap: true,
    },

    build: {
        // Template for index.html
        index: path.resolve(__dirname, '../dist/app.html'),
        // Paths
        assetsRoot: path.resolve(__dirname, '../dist'),
        assetsSubDirectory: 'static',
        // assetsPublicPath: '/micro/phone/',
        assetsPublicPath: '/',
        // assetsPublicPath: ' https://superwind-003.s3-accelerate.amazonaws.com/h5/',
        /**
         * Source Maps
         */

        productionSourceMap: false, // true,
        // https://webpack.js.org/configuration/devtool/#production
        devtool: '#source-map',

        // Gzip off by default as many popular static hosts such as
        // Surge or Netlify already gzip all static assets for you.
        // Before setting to `true`, make sure to:
        // npm install --save-dev compression-webpack-plugin
        productionGzip: true,
        productionGzipExtensions: ['js', 'css'],

        // Run the build command with an extra argument to
        // View the bundle analyzer report after build finishes:
        // `npm run build --report`
        // Set to `true` or `false` to always turn it on or off
        bundleAnalyzerReport: process.env.npm_config_report,
    },
    uat: {
        // Template for index.html
        index: path.resolve(__dirname, '../dist/uat/app.html'),
        // Paths
        assetsRoot: path.resolve(__dirname, '../dist/uat'),
        assetsSubDirectory: 'static',
        // assetsPublicPath: '/micro/phone/',
        assetsPublicPath: '/', // 172测试环境英文
        // assetsPublicPath: ' https://superwind-003.s3-accelerate.amazonaws.com/uat/',
        /**
         * Source Maps
         */

        productionSourceMap: true,
        // https://webpack.js.org/configuration/devtool/#production
        devtool: '#source-map',

        // Gzip off by default as many popular static hosts such as
        // Surge or Netlify already gzip all static assets for you.
        // Before setting to `true`, make sure to:
        // npm install --save-dev compression-webpack-plugin
        productionGzip: true,
        productionGzipExtensions: ['js', 'css'],

        // Run the build command with an extra argument to
        // View the bundle analyzer report after build finishes:
        // `npm run build --report`
        // Set to `true` or `false` to always turn it on or off
        bundleAnalyzerReport: process.env.npm_config_report,
    },
}
