module.exports = {
    devServer: {
        proxy: {
            '/': {
                target: 'http://localhost:8081',
                ws: true,
                changeOrigin: true
            }
        }
    }
}