let proxyObj = {};
const CompressionPlugin = require("compression-webpack-plugin");
proxyObj['/'] = {
    ws: false,
    target: 'http://localhost:8001/',
    changeOrigin: true,
    pathRewrite: {
        '^/': ''
    }
}
module.exports = {
	publicPath: './',
    // publicPath:"http://localhost:8001/api/service",
    devServer: {
        host: 'localhost',
        port: 8060,

        proxy: proxyObj
    },
    configureWebpack: config => {
        if (process.env.NODE_ENV === 'production') {
            return {
                plugins: [
                    new CompressionPlugin({
                        test: /\.js$|\.html$|\.css/,
                        threshold: 1024,
                        deleteOriginalAssets: false
                    })
                ]
            }
        }
    }
}
