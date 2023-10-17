<template>
  <el-table
    :data="messageTable"
    style="width: 93%;"
    :stripe="true"
    size="medium "
    :show-header="false"
    border
    :row-style="rowStyle"
    >
  <el-table-column prop="date" >
  </el-table-column>
  <el-table-column prop="name">
  </el-table-column>
    </el-table>
</template>
<script>
export default {
  props: {
    messageTable: {
      type: Array,
      require: true
    }
  },
  watch: {
  
  },
  data () {
    return {
     
    }
  },
  computed: {
  
  },
  mounted(){
    console.log(this.messageTable)
  },
  methods: {
    // 自定义列背景色
    rowStyle ({
      row,
      rowIndex
    }) {
      if (rowIndex === 0) {
        return 'background:#f3f6fc;'
      } else {
        return 'background:#ffffff;'
      }
    }
  }
}
</script>
