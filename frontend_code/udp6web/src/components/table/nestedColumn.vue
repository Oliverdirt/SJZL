<template>
  <el-table-column
    :label="data.label"
    :align="data.align || 'left'"
    v-bind="data"
  >
    <template v-for="info in data.columns">
      <nested-column :key="info.name" :data="info" v-if="info.nested" />
      <el-table-column
        v-else
        :key="info.name"
        :prop="info.name"
        :label="info.label"
        :width="info.width"
        :align="info.align || 'left'"
        v-bind="info"
      >
        <template slot-scope="scope">
          <render-column
            v-if="info.render"
            :render="info.render"
            :scope="scope"
          />
          <span v-else>{{ scope.row[info.name] || "-" }}</span>
        </template>
      </el-table-column>
    </template>
  </el-table-column>
</template>
<script>
import RenderColumn from './renderColumn.vue'
/**
 * 多层嵌套表格
 */
export default {
  name: 'nested-column',
  props: {
    // data: {
    //   type: Array,
    //   default: () => [],
    // },
    data: {
      type: Object,
      default: () => {}
    }
  },
  components: {
    RenderColumn
  }
}
</script>

<style></style>
