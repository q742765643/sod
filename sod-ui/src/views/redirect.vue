<script>
export default {
  created() {
    let url = this.$route.query;
    let str = "";
    for (let i in url) {
      str += url[i];
    }
    if (str == "/unCheckBox") {
      this.$router.push({
        name: "首页",
        params: { pageFlag: "unCheckBox" }
      });
      return;
    }
    const { params, query } = this.$route;
    const { path } = params;
    this.$router.replace({ path: "/" + path, query });
  },
  render: function(h) {
    return h(); // avoid warning message
  }
};
</script>
