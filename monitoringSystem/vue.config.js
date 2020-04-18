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
        target: "http://10.1.100.69:8008",
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