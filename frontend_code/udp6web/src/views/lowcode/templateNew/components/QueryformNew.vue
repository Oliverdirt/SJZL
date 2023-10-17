<template>
  <div id="queryForm">
    <el-form size="small" :inline="true" :model="formInline" class="demo-form-inline">
      <el-form-item
        v-for="(item,index) in searchJson"
        :key="index + 'hbw_'"
        :label="item.fieldComment+'：'"
      >
        <!--        文本框  -->
        <el-input v-if="item.controlType === '0'" placeholder="请输入" v-model="formInline[item.fieldName]"></el-input>
        <!--        下拉框 -->
        <el-select filterable v-if="item.controlType === '1'" v-model="formInline[item.fieldName]" placeholder="请选择">
          <el-option
            v-for="(option,index) in item.options"
            :label="option.label"
            :value="option.value"
            :key="index+'opt'"
          >
          </el-option>
        </el-select>
        <el-select multiple filterable v-if="item.controlType === '2'" v-model="formInline[item.fieldName]"
                   placeholder="请选择">
          <el-option
            v-for="(option,index) in item.options"
            :label="option.label"
            :value="option.value"
            :key="index+'opt'"
          >
          </el-option>
        </el-select>
        <!--        时间范围（HH:mm:ss） -->
        <el-time-picker
          is-range
          value-format="HH-mm-ss"
          v-if="item.controlType === '3'"
          arrow-control
          v-model="formInline[item.fieldName]"
          range-separator="至"
          start-placeholder="开始时间"
          end-placeholder="结束时间"
          placeholder="选择时间范围">
        </el-time-picker>
        <!--        日期范围 （yyyy-MM-dd HH:mm:ss）  -->
        <el-date-picker
          value-format="yyyy-MM-dd HH-mm-ss"
          v-if="item.controlType === '4'"
          v-model="formInline[item.fieldName]"
          type="datetimerange"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        >
        </el-date-picker>
        <!--        日期范围 （yyyy-MM-dd）  -->
        <el-date-picker
          v-if="item.controlType === '5'"
          v-model="formInline[item.fieldName]"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item :key="item" v-for="item in 3-(searchJson.length + 1)%3"></el-form-item>
      <el-form-item>
        <el-button @click="clickQueryBtn" type="primary" icon="el-icon-search">查询</el-button>
        <el-button @click="onReset" icon="el-icon-s-tools">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  name: 'Queryform',
  props: {
    searchJson: {
      type: Array,
      default: () => {
        return []
      }
    }
  },
  data () {
    return {
      formInline: {}
    }
  },
  methods: {
    getFormData () {
      return this.formInline
    },
    clickQueryBtn () {
      this.$emit('clickQueryBtn')
    },
    onReset () { // 重置
      for (let item in this.formInline) {
        if (Array.isArray(this.formInline[item])) { // 是数组
          this.formInline[item] = []
        } else {
          this.formInline[item] = ''
        }
      }
      this.clickQueryBtn()
    }
  }
}
</script>

<style lang="less">
#queryForm {
  .el-form {
    padding: 25px 0 15px;
    width: 100%;
    height: 100%;
    display: flex;
    align-items: center;
    flex-wrap: wrap;
    .el-form-item {
      margin: 0;
      width: 33%;
      box-sizing: border-box;
      padding: 0 10px 10px 0;
      display: flex;
      align-items: center;

      .el-form-item__label {
        flex: 1;
        text-overflow: ellipsis;
        overflow: hidden;
        white-space: nowrap;
        flex-shrink: 0;
      }

      .el-form-item__content {
        flex: 3;

        .el-select {
          width: 100%;
        }

        .el-input {
          width: 100%;
        }

        .el-date-editor {
          width: 100%;
        }
      }
    }
    .el-form-item:last-child {
      .el-form-item__content {
        display: flex;
        justify-content: flex-end;
      }
    }
  }
}
</style>
