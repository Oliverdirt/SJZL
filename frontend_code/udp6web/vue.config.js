const CompressionPlugin = require('compression-webpack-plugin')
const BiyiMsConfigPlugin = require('biyi-ms-config-plugin')

const indexHtmlJs = require('./index.html.js')
const path = require('path')
const IS_PROD = process.env.NODE_ENV === 'production'
let buildProdFlag = false
const baseUrl = process.env.NODE_ENV === 'development' ? 'http://localhost:9000' : 'http://udp6hxtest.devops.com'

function resolve(dir) {
  return path.join(__dirname, dir)
}

module.exports = {
  publicPath: './',
  css: {
    loaderOptions: {
      scss: {
        /* 自动引入全局scss文件 */
        additionalData:
          (content, loaderContext) => {
            const {resourcePath} = loaderContext
            if (resourcePath.endsWith('global.scss')) return content
            return `@import "./src/styles/global.scss"; ${content}`
          }
      }
    }
  },
  lintOnSave: false,
  devServer: {
    host: '0.0.0.0',
    port: '8080',
    open: false,
    client: {
      overlay: false
    },
    proxy: {
      [process.env.VUE_APP_BASE_API]: {
        target: baseUrl,
        changeOrigin: true,
        pathRewrite: {
          ['^' + process.env.VUE_APP_BASE_API]: ''
        }
      },
      '/ureport': {
        target: baseUrl,
        ws: false,
        changeOrigin: true,
        pathRewrite: {
          '^/ureport': '/ureport'
        }
      }
    }
  },
  chainWebpack: config => {
    config.entry.app = ['babel-polyfill', './src/main.js']
    /* 配置svg图标自动加载 begin */
    config.module
      .rule('svg')
      .exclude.add(resolve('./src/views/lowcode/vformPro/icons'))
      .end()
    config.module
      .rule('icons')
      .test(/\.svg$/)
      .include.add(resolve('./src/views/lowcode/vformPro/icons'))
      .end()
      .use('svg-sprite-loader')
      .loader('svg-sprite-loader')
      .options({
        symbolId: 'icon-[name]'
      })
    /* 配置svg图标自动加载 end */
    // 开启js、css压缩，生成gz压缩文件
    if (process.env.NODE_ENV === 'production') {
      config.plugin('compressionPlugin')
        .use(new CompressionPlugin({
          test: /\.js$|\.html$|\.css$|\.svg$/, // 匹配文件名
          threshold: 102400, // 对超过100k的数据压缩
          deleteOriginalAssets: false // 不删除源文件
        }))
      config.plugin('BiyiMsConfigPlugin').use(new BiyiMsConfigPlugin([
        {
          fileName: 'micro-service-config.js',
          path: 'public/configs',
          content: ''
        },
        {
          fileName: 'index.html',
          path: 'public',
          content: indexHtmlJs // 如需添加第三方js文件请在./index.html.js中添加
        }
      ]))

    }
  },
  productionSourceMap: false,
  configureWebpack: (config) => {
    config.devtool = IS_PROD ? false : 'source-map';
    config.output.libraryExport = 'default'  /* 解决import UMD打包文件时, 组件install方法执行报错的问题！！ */
 
    
    if (IS_PROD && buildProdFlag) { /* 仅生产环境使用 */
      /* CDN打包，需要修改index.html加入CDN资源 */
      config.externals = {
        'vue': 'Vue',
        'element-ui': 'ELEMENT',
        //'quill': 'Quill',
      }
    }

    config.module.rules.push({
      test: /\.xml$/,
      use: [{
        loader: 'xml-loader', // 解析xml文件
        options: {
          prefix: false
        }
      }]
    })
  },
  transpileDependencies: [
    'biyi-admin',
    'biyi-captcha',
    'biyi-store',
    'biyi-dic',
    'sockjs-client'
  ]
}
