<template>
  <div class="hx-unit-input" :class="[size, isFocus && 'is-focus']">
    <Input
      v-model="inputValue"
      class="hx-unit-input__input"
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
      type: String,
      default: "",
    },
    type: {
      type: String,
      default: "text",
    },
    size: {
      type: String,
      default: "large", // small or large
    },
    clearable: {
      type: Boolean,
      default: false,
    },
  },
  model: {
    prop: "value",
    event: "on-change",
  },
  data() {
    return {
      isFocus: false,
    };
  },
  computed: {
    inputValue: {
      get() {
        return this.value;
      },
      set(val) {
        this.$emit("on-change", val);
      },
    },
  },
  methods: {},
};
</script>
<style lang="less" scoped>
.hx-unit-input {
  display: flex;
  width: 260px;
  padding: 0 4px;
  color: #00082e;
  box-sizing: border-box;
  border-radius: 4px;
  border: 1px solid#c5c8ce;
  transition: all 0.2s;
  background: #fff;
  &:hover {
    border: 1px solid #57a3f3;
  }
  &.is-focus {
    border: 1px solid #57a3f3;
    // box-shadow: 0 0 0 2px rgba(70, 75, 101, 20%);
  }
  &__input {
    flex: 1;
    /deep/.ivu-input {
      border: none;
      box-shadow: none;
      background: transparent;
    }
  }
  .unit {
    padding: 6px 4px;
  }
  &.small {
    height: 32px;
    .hx-unit-input__input {
      /deep/.ivu-input {
        height: 30px;
      }
    }
    .unit {
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
    .unit {
      line-height: 26px;
    }
  }
}
</style>
