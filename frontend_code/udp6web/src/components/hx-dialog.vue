<template>
  <transition name="fade">
    <div
      :class="[
        'hx-dialog-wrapper',
        { normal: '', large: 'large-wrapper' }[type],
      ]"
      v-if="dialogVisible"
    >
      <div
        class="hx-dialog-modal"
        @click="clickModalClose ? handleClose() : () => {}"
      ></div>
      <div class="hx-dialog" :style="{ width: width }">
        <Icon
          class="icon-close"
          type="md-close"
          size="20"
          @click="handleClose"
        />
        <div class="ct-container">
          <div class="ct-container__title">{{ title }}</div>
          <div class="ct-container__content">
            <slot></slot>
          </div>
          <div class="hx-dialog-footer">
            <slot name="footer"></slot>
          </div>
        </div>
      </div>
    </div>
  </transition>
</template>
<script>
export default {
  name: 'HxDialog', // 弹窗
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    // 弹窗类型 normal or large
    type: {
      type: String,
      default: 'normal'
    },
    // 弹窗标题
    title: {
      type: String,
      default: '标题'
    },
    // 弹窗宽度
    width: {
      type: String,
      default: '60%'
    },
    // 是否点击遮罩层关闭弹窗
    clickModalClose: {
      type: Boolean,
      default: true
    }
  },
  model: {
    prop: 'visible',
    event: 'handleVisibleChange'
  },
  computed: {
    dialogVisible: {
      get () {
        return this.visible
      },
      set (val) {
        this.$emit('handleVisibleChange', val)
      }
    }
  },
  methods: {
    handleClose () {
      this.$emit('on-close')
      this.dialogVisible = false
    }
  }
}
</script>
<style lang="less" scoped>
.hx-dialog {
  position: absolute;
  top: calc(50% - 40px);
  left: 50%;
  transform: translate(-50%, -50%);
  background: #fff;
  .icon-close {
    position: absolute;
    top: 20px;
    right: 20px;
    cursor: pointer;
    transition: opacity 0.1s;
    &:hover {
      opacity: 0.8;
    }
    &:active {
      opacity: 0.6;
    }
  }
  &-wrapper {
    position: fixed;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    z-index: 90;
  }
  &-modal {
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.4);
  }

  &-footer {
    display: flex;
    margin-top: 20px;
    justify-content: flex-end;
    /deep/.ivu-btn + .ivu-btn {
      margin-left: 6px;
    }
  }
  .ct-container {
    height: 100%;
    margin: 0;
    &__content {
      flex: 1;
    }
  }
}

.large-wrapper {
  top: 80px;
  .hx-dialog {
    position: absolute;
    top: 0;
    left: auto;
    right: 0;
    transform: translate(0, 0);
    height: 100%;
    background: #fff;
    animation: slideRight 0.3s ease-in-out;
  }
}

@keyframes slideRight {
  from {
    opacity: 0.2;
    transform: translateX(20%);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}
</style>
