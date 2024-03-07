<template>
  <div class="hx-datePicker">
    <DatePicker
      :value="dateValue"
      :type="type"
      :options="options"
      :placeholder="placeholder"
      :size="size"
      :readonly="readonly"
      v-bind="$attrs"
      @on-change="handleDatePicker"
    />
  </div>
</template>
<script>
import emitter from "element-ui/src/mixins/emitter";
/**
 * view-design datepicker 套用element form验证规则
 */
export default {
  name: "hx-date-picker",
  mixins: [emitter],
  inheritAttrs: false,
  props: {
    type: String,
    value: [Date, String, Array],
    placeholder: {
      type: String,
      default: "选择时间"
    },
    size: String,
    readonly: Boolean,
    options: {
      type: Object,
      default: () => {}
    }
  },
  model: {
    prop: "value",
    event: "change"
  },
  data() {
    return {
      dateValue: ""
    };
  },
  mounted() {
    this.dateValue = this.value;
  },
  watch: {
    value(newVal) {
      this.handleDatePicker(newVal);
      this.dispatch("ElFormItem", "el.form.change", [newVal]);
      if (newVal !== this.dateValue) {
        this.dateValue = newVal;
      }
    }
  },
  methods: {
    handleDatePicker(value, date) {
      this.$emit("change", value);
    }
  }
};
</script>
<style lang="less" scoped>
.hx-datePicker {
  /deep/.ivu-date-picker {
    width: 100%;
    .ivu-input {
      height: 34px;
      line-height: 34px;
      font-size: inherit;
    }
    .ivu-input-suffix i {
      line-height: 34px;
    }
  }
}
</style>
