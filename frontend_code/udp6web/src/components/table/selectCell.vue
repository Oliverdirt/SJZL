<template>
  <div class="select-cell">
    <el-popover :value="validateMsg !== ''" trigger="manual">
      <div slot="reference">
        <!-- <span v-if="!editable" @click="handleEditable"
          >{{ editValue }}
          <i class="el-icon-edit"></i>
        </span> -->
        <el-select
          ref="input"
          v-bind="$attrs"
          v-model="editValue"
          @change="handleEditable"
        >
          <el-option
            v-for="item in data"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>
      </div>
      <span style="color: #f5222d">{{ validateMsg }}</span>
    </el-popover>
  </div>
</template>
<script>
export default {
  name: 'select-cell',
  props: {
    value: String,
    type: String,
    data: {
      type: Array,
      default: () => []
    },
    validator: {
      type: Function,
      default: () => null
    }
  },
  data () {
    return {
      editValue: '',
      editable: false,
      validateMsg: ''
    }
  },
  watch: {
    value: {
      immediate: true,
      handler (val) {
        this.editValue = val
      }
    }
  },
  mounted () {
    // this.editValue = this.value;
  },
  methods: {
    handleEditable () {
      if (this.editable && typeof this.validator === 'function') {
        const err = this.validator(this.editValue)
        if (err) {
          this.validateMsg = err
          return
        }
        this.validateMsg = ''
      }
      this.editable = !this.editable
      if (this.editable) {
        this.$nextTick(() => {
          this.$refs.input.focus()
        })
      }
      this.$emit('change', this.editValue)
    }
  }
}
</script>
<style lang="less" scoped>
.select-cell {
  min-height: 20px;
  cursor: pointer;
  span {
    display: inline-block;
    height: 100%;
    width: 100%;
    overflow: hidden;
    text-overflow: ellipsis;
    i {
      display: none;
      transition: 0.3s;
    }
    &:hover i {
      display: inline;
    }
  }
}
</style>
