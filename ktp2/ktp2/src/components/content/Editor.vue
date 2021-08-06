<template>
  <div>
    <div ref="toolbar" class="toolbar"></div>
    <div ref="editor" class="text" style="text-align:left;max-height: 200px;"></div>
  </div>
</template>

<script>
  import E from 'wangeditor'
  export default {
    name: 'editor',
    props: {
      description: {
        type:String,
        default: '',
      }
    },
    data () {
      return {
        editorContent: this.description
      }
    },
    mounted() {
      var editor = new E(this.$refs.toolbar,this.$refs.editor)
      editor.customConfig.onchange = (html) => {
        this.editorContent = html
        this.$emit('description', this.editorContent)
      }
      editor.create()
      editor.txt.html(this.description)
    }
  }
</script>

<style scoped>
  .toolbar {
    border: 1px solid #ccc;
  }
  .text {
    border: 1px solid #ccc;
    height: 400px;
  }
</style>
