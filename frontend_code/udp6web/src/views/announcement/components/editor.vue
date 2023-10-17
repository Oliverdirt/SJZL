<template>
  <div style="border: 1px solid #ccc">
    <Toolbar
      style="border-bottom: 1px solid #ccc"
      :editor="editor"
      :defaultConfig="toolbarConfig"
      :mode="mode"
    />
    <Editor
      style="height: 301px; overflow-y: hidden"
      v-model="textcontent.noticeContent"
      :defaultConfig="editorConfig"
      :mode="mode"
      @onCreated="onCreated"
    />
  </div>
</template>
<script>
import Vue from 'vue'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'

export default Vue.extend({
  props: {
    textcontent: {
      type: Object,
      require: true
    },
    flagFn:{
      type: String,
      require: true
    }
  },
  components: { Editor, Toolbar },
  data () {
    return {
      editor: null,
      html: '<p>新版本内容</p>',
      toolbarConfig: {
        excludeKeys:["uploadVideo","emotion","insertVideo","insertImage","group-video"]
      },
      editorConfig: { placeholder: '请输入内容...',
      MENU_CONF: {
					uploadImage: {
            customUpload: this.uploadImg,
					},
				},

    },
      mode: 'default' // or 'simple'
    }
  },

  methods: {

    onCreated (editor) {
      this.editor = Object.seal(editor) // 一定要用 Object.seal() ，否则会报错
    },
    enableFn(){
      console.log(1,'222')
      this.editor.enable()
       console.log(1,'222')
    },

 uploadImg(file, insertFn){
      let imgData = new FormData();
			imgData.append('file', file);
      this.$http.post('/api/system/uploadImage',imgData).then((response)=>{
        console.log(response,'qqq')
        insertFn(response.data);
      })
    },


  },
  mounted () {
    // console.log(this.textcontent,'textcontent1')
    console.log(this.flagFn,'flagFn')

    // 模拟 ajax 请求，异步渲染编辑器
    setTimeout(() => {
      this.html = '<p>新版本内容</p>'
      this.flagFn == 1 ? this.editor.disable() : this.enableFn()
    }, 1500)
  },
  beforeDestroy () {
    const editor = this.editor
    if (editor == null) return
    editor.destroy() // 组件销毁时，及时销毁编辑器
  }
})
</script>

<style src="@wangeditor/editor/dist/css/style.css">
</style>
