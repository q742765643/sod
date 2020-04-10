const path = require("path");
module.exports = {
  publicPath: "./",
  outputDir: "dist",
  assetsDir: "static",
  runtimeCompiler: true,
  devServer: {
    host: "localhost",
    port: 9090,
    proxy: {
      "/app": {
        target: "http://192.168.2.04:8080",
        changeOrigin: true,
        ws: true,
        pathRewrite: {
          "^/app": ""
        }
      }
    }
  },
  configureWebpack: () => ({
    resolve: {
      alias: {
        "@": path.resolve("src")
      }
    }
  })
};