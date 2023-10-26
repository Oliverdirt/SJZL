<template>
  <div class="app-container">
    <!-- 操作工作栏 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleExportHtml">导出 HTML</el-button>
        <el-button type="success" icon="el-icon-plus" size="mini" @click="handleExportWord">导出 Word</el-button>
        <el-button type="warning" icon="el-icon-plus" size="mini" @click="handleExportMarkdown">导出 Markdown</el-button>
      </el-col>
    </el-row>

    <!-- 展示文档 -->
    <div v-loading="loading" :style="'height:'+ height">
      <i-frame :src="src" />
    </div>
  </div>
</template>
<script>
import iFrame from './iFrame/index'
import {
  exportHtml,
  exportWord,
  exportMarkdown
} from '@/api/admin/db-doc'
export default {
  name: 'DBDoc',
  components: { iFrame },
  data () {
    return {
      height: document.documentElement.clientHeight - 94.5 + 'px;',
      loading: true,
      src: undefined
    }
  },
  mounted: function () {
    setTimeout(() => {
      this.loading = false
    }, 230)
    const that = this
    window.onresize = function temp () {
      that.height = document.documentElement.clientHeight - 94.5 + 'px;'
    }
  },
  created () {
    // 加载 Html，进行预览
    exportHtml()
      .then(res => {
        let blob = new Blob([res.data], { type: 'text/html' })
        console.log('asdasdasdasdsa', window.URL.createObjectURL(blob))
        this.src = window.URL.createObjectURL(blob)
        console.log('asdasdasdasdsa', this.src)
      }).catch(function (error) {
        this.$Notice.error({
          title: '操作失败'
        })
        console.log(error)
      })
  },
  methods: {
    /** 处理导出 HTML */
    handleExportHtml () {
      exportHtml()
        .then(res => {
          this.$download.html(res.data, '数据库文档.html')
        })
    },
    /** 处理导出 Word */
    handleExportWord () {
      exportWord()
        .then(res => {
          this.$download.word(res.data, '数据库文档.doc')
        })
    },
    /** 处理导出 Markdown */
    handleExportMarkdown () {
      exportMarkdown()
        .then(res => {
          this.$download.markdown(res.data, '数据库文档.md')
        })
    }
  }
}
</script>
