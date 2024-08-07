const { template } = require('@babel/core')
const { plugins } = require('@babel/preset-env/lib/plugins-compat-data')
const HtmlwebPackPlugin = require('html-webpack-plugin')
const path = require('path')

module.exports = {
    entry: './index.js',
    mode: 'development',
    output: {
        path: path.resolve(__dirname, './dist'),
        filename: 'index_bundle.js',
    },
    target: 'web',
    devServer: {
        port: '5000',
        static: {
            directory: path.join(__dirname, 'public')
        },
        open: true,
        hot: true,
        liveReload: true
    },

    resolve: {
        extensions: ['.js', '.jsx', '.json']
    },
    module: {
        rules: [{
            test: /\.(js|jsx)$/,
            exclude: /node_modules/,
            use: 'babel-loader'
        }]
    },
    plugins: [
        new HtmlwebPackPlugin({
            template: path.join(__dirname, 'public', 'index.html')
        })
    ]
}