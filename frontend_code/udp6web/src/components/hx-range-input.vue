<template>
  <div class="hx-range-input" :class="[size, isFocus && 'is-focus']">
    <Input
      v-model="prevVal"
      class="hx-range-input__input"
      :type="type"
      @on-focus="isFocus = true"
      @on-blur="isFocus = false"
      placeholder="请输入"
      :clearable="clearable"
    ></Input>
    <div class="unit">ms</div>
    <div class="division">-</div>
    <Input
      v-model="nextVal"
      class="hx-range-input__input"
      :type="type"
      @on-focus="isFocus = true"
      @on-blur="isFocus = false"
      placeholder="请输入"
      :clearable="clearable"
    ></Input>
    <div class="unit">ms</div>
  </div>
</template>
<script>
export default {
  props: {
    value: {
      type: Array,
      default() {
        return [];
      },
    },
    type: {
      type: String,
      default: "text",
    },
    size: {
      type: String,
      default: "small", // small or large
    },
    clearable: {
      type: Boolean,
      default: false,
    },
  },
  model: {
    prop: "value",
    event: "on-date-change",
  },
  data() {
    return {
      isFocus: false,
    };
  },
  computed: {
    prevVal: {
      get() {
        return this.value[0] || "";
      },
      set(val) {
        this.$emit("on-date-change", [val, this.nextVal]);
      },
    },
    nextVal: {
      get() {
        return this.value[1] || "";
      },
      set(val) {
        this.$emit("on-date-change", [this.prevVal, val]);
      },
    },
  },
  methods: {},
};
</script>
<style lang="less" scoped>
.hx-range-input {
  display: flex;
  width: 230px;
  padding: 0 4px;
  color: #00082e;
  box-sizing: border-box;
  border-radius: 4px;
  //   border: 1px solid#3396ff;
  transition: all 0.2s;
  background: #fff;
  &:hover {
    // border: 1px solid #6b6f84;
  }
  &.is-focus {
    // border: 1px solid #6b6f84;
    // box-shadow: 0 0 0 2px rgba(70, 75, 101, 20%);
  }
  &__input {
    flex: 1;
    /deep/.ivu-input {
      height: 40px;
      border: none;
      box-shadow: none;
      background: transparent;
    }
  }
  .unit,
  .division {
    padding: 6px 4px;
  }
  &.small {
    height: 32px;
    .hx-unit-input__input {
      /deep/.ivu-input {
        height: 30px;
      }
    }
    .unit,
    .division {
      line-height: 18px;
    }
  }
  &.large {
    height: 40px;
    .hx-unit-input__input {
      /deep/.ivu-input {
        height: 38px;
      }
    }
    .unit,
    .division {
      line-height: 26px;
    }
  }
}
</style>
