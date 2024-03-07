<template>
  <div class="hx-date-picker">
    <div
      :class="['hx-date-picker__item', type === '0' && 'selected']"
      @click="clickItem('0')"
    >
      <div class="cell">1小时</div>
    </div>
    <div
      :class="['hx-date-picker__item', type === '1' && 'selected']"
      @click="clickItem('1')"
    >
      <div class="cell">近3天</div>
    </div>
    <div
      :class="['hx-date-picker__item', type === '2' && 'selected']"
      @click="clickItem('2')"
    >
      <div class="cell">近7天</div>
    </div>
    <div
      :class="['hx-date-picker__item', type === '3' && 'selected']"
      @click="clickItem('3')"
    >
      <el-date-picker
        v-model="dataValue"
        type="datetimerange"
        style="width: 300px"
        placeholder="自定义时间范围"
        :clearable="false"
      ></el-date-picker>
    </div>
  </div>
</template>
<script>
export default {
  name: "HxDatePicker", // 时间范围选择器
  props: {
    value: {
      type: Array,
      default() {
        return [];
      },
    },
    timeType: {
      type: String,
      default: "", // 0：近1小时；1：近三天；2：近七天； 3：自定义
    },
  },
  model: {
    prop: "value",
    event: "on-date-change",
  },
  computed: {
    dataValue: {
      get() {
        return this.value;
      },
      set(val) {
        this.$emit("on-date-change", val);
      },
    },
    type: {
      get() {
        return this.timeType;
      },
      set(val) {
        this.$emit("on-type-change", val);
      },
    },
  },
  methods: {
    clickItem(e) {
      if (e === this.type) {
        this.type = "";
        this.dataValue = [];
      } else {
        this.type = e;
        const dateFun = {
          0: this.getYesterday(),
          1: this.getThreeDay(),
          2: this.getWeek(),
        }[e];
        this.dataValue = dateFun || this.dataValue;
      }
    },
    getYesterday() {
      const end = new Date();
      const start = new Date();
      start.setTime(start.getTime() - 3600 * 1000 * 24);
      return [start, end];
    },
    getThreeDay() {
      const end = new Date();
      const start = new Date();
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 3);
      return [start, end];
    },
    getWeek() {
      const end = new Date();
      const start = new Date();
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
      return [start, end];
    },
  },
};
</script>
<style lang="less" scoped>
.hx-date-picker {
  display: flex;
  &__item {
    min-width: 70px;
    height: 32px;
    margin-left: -1px;
    border: 1px solid #dfe3eb;
    text-align: center;
    color: #999;
    user-select: none;
    cursor: pointer;
    box-sizing: border-box;
    // transition: all 0.1s ease;
    .cell {
      line-height: 1;
      padding: 8px 10px;
    }
    &:hover {
      opacity: 0.8;
    }
    &:active {
      opacity: 0.6;
    }
    &:first-child {
      margin-left: 0;
      border-radius: 4px 0px 0px 4px;
    }
    &:last-child {
      border-radius: 0px 4px 4px 0px;
    }
    &.selected {
      color: #666;
      //   border: 1px solid #464b65;
      outline: 0;
      //   -webkit-box-shadow: 0 0 0 2px rgba(70, 75, 101, 20%);
      //   box-shadow: 0 0 0 2px rgba(70, 75, 101, 20%);
    }
    /deep/.el-input__inner {
      border: none;
      box-shadow: none;
      height: 32px !important;
    }
  }
}
</style>
