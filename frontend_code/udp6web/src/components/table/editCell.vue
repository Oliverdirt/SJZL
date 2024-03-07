<template>
  <div class="edit-cell">
    <el-popover :value="validateMsg !== ''" trigger="manual">
      <div slot="reference">
        <span v-if="!editable" @click="handleEditable"
          >{{ editValue }}
          <i :class="['el-icon-edit', hideIcon ? '' : 'show-icon']"></i>
        </span>
        <el-input
          ref="input"
          :type="type"
          :rows="rows"
          v-bind="$attrs"
          autofocus
          v-else
          v-model="editValue"
          @change="handleEditable"
          @blur="handleEditable"
        />
      </div>
      <span style="color: #f5222d">{{ validateMsg }}</span>
    </el-popover>
  </div>
</template>
<script>
export default {
  name: 'edit-cell',
  props: {
    value: String,
    type: String,
    rows: {
      type: Number,
      default: 3
    },
    validator: {
      type: Function,
      default: () => null
    },
    // 编辑图标是否长显
    hideIcon: {
      type: Boolean,
      default: true
    }
  },
  data () {
    return {
      editValue: '',
      editable: false,
      validateMsg: ''
    }
  },
  mounted () {
    // this.editValue = this.value;
  },
  watch: {
    value: {
      immediate: true,
      handler (val) {
        this.editValue = val
      }
    },
    editable () {
      this.editValue = this.value
    }
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
.edit-cell {
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
      &.show-icon {
        display: inline;
      }
    }
    &:hover i {
      display: inline;
    }
  }
}
</style>
