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
      [process.env.VUE_APP_FTP_API]: {
        target: "http://localhost:8008",
        changeOrigin: true,
        ws: true,
        pathRewrite: {
          ["^" + process.env.VUE_APP_FTP_API]: ""
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