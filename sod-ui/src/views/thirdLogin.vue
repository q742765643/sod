<template>
</template>
<script>

export default {
  name: "ThirdLogin",
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect;
      },
      immediate: true
    }
  },
  created() {
    const token = this.$route.params && this.$route.params.token;
    console.log(token)
    this.$store
      .dispatch("ThirdLogin", token)
      .then(() => {
        this.loading = false;
        this.$router.push({ path: this.redirect || "/" });
      })
      .catch(() => {
        this.loading = false;
      });

  }
}
</script>

