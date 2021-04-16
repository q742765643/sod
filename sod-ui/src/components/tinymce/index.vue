<template>
  <section id="tinymce-editor">
Tinymce Editor
  </section>
</template>

<script>
  import './tinymce.min';
  import './themes/silver/theme.min'
  import './langs/zh_CN'

  export default {
    components: {
    },

    data() {
      return {}
    },
    mounted() {
      tinymce.init({
        selector: '#tinymce-editor',
        language: 'zh_CN',
        menubar: 'bar1 bar2',
        menu:{
          bar1: {title: '菜单栏1', items:'copy paste'},
          bar2: {title: '菜单栏2', items:'cut'},
        },
        toolbar: 'undo redo'
      })
    },
  }
</script>

<style lang="less" scoped>
@import url("./skins/ui/oxide/skin.min.css");
</style>
