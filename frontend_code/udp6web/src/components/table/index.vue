<template>
  <div class="hx-table">
    <el-row :gutter="8">
      <el-col :span="24" class="header-tools" v-if="$slots.header">
        <slot name="header"></slot>
      </el-col>
      <el-col :span="24">
        <el-table
          v-if="dicLoaded"
          ref="hx-table"
          :header-cell-style="{ background: '#f8f8f9' }"
          :data="tableOptions.data"
          :row-key="tableOptions.rowKey"
          :border="border"
          row-class-name="hx-table-row"
          v-on="$listeners"
          v-bind="$attrs"
        >
          <template v-if="tableOptions.expand">
            <el-table-column type="expand" :fixed="true" :width="pxToVw(50)">
              <template slot-scope="scope">
                <render-column
                  :render="tableOptions.renderExpandTable"
                  :scope="scope"
                />
              </template>
            </el-table-column>
          </template>
          <template v-for="item in transformPxToVw(tableOptions.columns)">
            <el-table-column
              v-if="item.type === 'selection'"
              :key="item.name || 'selection'"
              :align="item.align || 'center'"
              type="selection"
              v-bind="item"
            />
            <nested-column
              :key="item.name"
              :data="item"
              v-else-if="item.nested"
            />
            <el-table-column
              v-else
              :key="item.name"
              :prop="item.name"
              :label="item.label"
              :width="item.width"
              :align="item.align || 'left'"
              v-bind="item"
            >
              <template slot-scope="scope">
                <!-- 优先展示render -->
                <render-column
                  v-if="item.render"
                  :render="item.render"
                  :scope="scope"
                />
                <!-- 否则展示数据 数据判定是否字典 -->
                <span v-else>
                  <span v-if="item.dicCode">{{
                    returnDicLabel(item.dicCode, scope.row[item.name])
                  }}</span>
                  <span v-else>{{ scope.row[item.name] | formatValue }}</span>
                </span>
              </template>
            </el-table-column>
          </template>
        </el-table>
      </el-col>
      <el-col :span="24" v-if="showPagination" class="pagination-box">
        <el-pagination
          background
          :total="pageOptions.total"
          :page-size="pageOptions.pageSize"
          :page-sizes="[10, 15, 20, 30]"
          @size-change="handlePageChange({ pageSize: $event })"
          @current-change="handlePageChange({ pageNum: $event })"
          :current-page="pageOptions.pageNum"
          :popper-class="pageOptions.popperClass"
          layout="total, slot, prev, pager, next, sizes, jumper"
        >
          <span class="el-pagination__total" key="totalSize"
            >共&nbsp;{{ totalSize }}&nbsp;页</span
          >
        </el-pagination>
      </el-col>
    </el-row>
    <Spin fix v-show="loading" />
  </div>
</template>
<script>
import RenderColumn from "./renderColumn.vue";
//
import nestedColumn from "./nestedColumn.vue";
import json from "highlight.js/lib/languages/json";

export default {
  name: "hx-table",
  props: {
    tableOptions: {
      type: Object,
      default: () => {},
    },
    loading: Boolean,
    showPagination: {
      type: Boolean,
      default: true,
    },
    pageOptions: {
      type: Object,
      default: () => ({
        total: 0,
        pageSize: 0,
      }),
    },
    border: {
      type: Boolean,
      default: () => false,
    },
  },
  filters: {
    formatValue(val) {
      if (val === "" || val === null || val === undefined) {
        return "-";
      }
      return val;
    },
  },
  components: {
    RenderColumn,
    nestedColumn,
  },
  computed: {
    isShowPage() {
      const { total, pageSize } = this.pageOptions;
      return total > pageSize;
    },
    totalSize() {
      if (
        this.pageOptions &&
        this.pageOptions.total &&
        this.pageOptions.pageSize
      ) {
        return Math.ceil(this.pageOptions.total / this.pageOptions.pageSize);
      } else {
        return 0;
      }
    },
  },
  // watch: {
  //   "tableOptions.columns": {
  //     deep: true,
  //     handler() {
  //       // 列表项变化，更新一下表格
  //       this.$refs.table.doLayout();
  //     }
  //   }
  // },
  data() {
    return {
      dicLabelList: {},
      dicCodeList: [],
      dicLoaded: false,
    };
  },
  created() {
    this.setDicCodeList();
  },
  methods: {
    /**
     * 分页切换
     */
    handlePageChange(params) {
      this.$emit("change", params);
    },
    setDicCodeList() {
      this.dicCodeList = this.tableOptions.columns.filter((res) => {
        return res.dicCode;
      });
      // 多promise启动字典获取
      this.getDicList();
    },
    /**
     * 转换列表项中的width 设置
     */
    transformPxToVw(data) {
      let arr = [];

      if (!data && !Array.isArray(data)) {
        return arr;
      }
      for (let i = 0, len = data.length; i < len; i++) {
        let item = data[i];
        // 字级嵌套,表头嵌套
        if (item.nested) {
          let columns = this.transformPxToVw(item.columns);
          arr.push({
            ...item,
            columns,
          });
          continue;
        }
        // 没有设置width，则不处理
        if (!item.width && !item.minWidth && !item["min-width"]) {
          arr.push({ ...item });
          continue;
        }
        let width = this.pxToVw(item.width);
        let minWidth = this.pxToVw(item.minWidth || item["min-width"]);
        arr.push({
          ...item,
          width,
          minWidth,
        });
      }
      return arr;
    },
    pxToVw($px) {
      $px = Number($px);
      if (isNaN($px)) {
        return;
      }
      return this.$px2Vw($px);
    },
    async ajaxDicList(dicCode) {
      const dicList = await this.$store.dispatch("dictionary/getDict", dicCode);
      this.dicLabelList[dicCode] = dicList;
    },
    getDicLabel(dicCode) {
      this.$store.dispatch("dictionary/getDict", dicCode).then((res) => {
        this.dicLabelList[dicCode] = res;
        console.info("dicLabelList", this.dicLabelList);
        // 更新数据
        this.$nextTick(() => {
          this.$refs["hx-table"].doLayout();
        });
      });
    },
    returnDicLabel(dicCode, value) {
      let dicList = this.dicLabelList[dicCode];
      if (dicList) {
        const itemArr = dicList.filter((item) => {
          return item.itemCode == value;
        });
        if (itemArr.length > 0) {
          return itemArr[0].itemValue;
        } else {
          return "-";
        }
      } else {
        return "";
      }
    },
    getDicList() {
      if (this.dicCodeList.length > 0) {
        const promises = [];
        this.dicCodeList.forEach((res) => {
          let promise = this.$store.dispatch("dictionary/getDict", res.dicCode);
          promises.push(promise);
        });
        Promise.all(promises)
          .then((posts) => {
            posts.forEach((res, index) => {
              this.dicLabelList[this.dicCodeList[index].dicCode] = res;
            });
            this.dicLoaded = true;
          })
          .catch(function (reason) {
            this.dicLoaded = true;
          });
      } else {
        this.dicLoaded = true;
      }
    },
  },
};
</script>
<style lang="less" scoped>
@primary-color: #326fe9;
@warning-color: #ff9900;
@error-color: #ed4014;

@center-color: #448ef7;
@header-color: rgba(0, 0, 0, 0.02);

.hx-table {
  position: relative;
  .header-tools {
    margin-bottom: 8px;
  }
  /deep/.pagination-box {
    // text-align: right;
    // margin: 8px 0;
    margin-top: 20px;
    text-align: center;
    .el-pagination.is-background .el-pager li {
      border-radius: 4px;
      border: 1px solid #888aa6;
      color: #1c1c28;
      background-color: #fff;
      font-weight: normal;
    }

    .el-pagination.is-background .el-pager li:not(.disabled):hover {
      border-color: @center-color;
      color: @center-color;
    }
    .el-input__inner:hover {
      border-color: @center-color;
    }
    .el-pagination.is-background .el-pager li:not(.disabled).active {
      background-color: @center-color;
      border-color: @center-color;
      color: #fff;
    }
    .el-pagination.is-background .el-pager li.more {
      border: none;
    }
  }
  /deep/ .el-table thead {
    // color: #000;
    // background-color: #fff;
    .el-table__cell {
      background: @center-color !important;
      font-weight: 500;
      font-size: 14px;
      padding: 0;
      color: #fff;
      font-family: PingFangSC-Medium, PingFang SC;
      height: 40px;
      line-height: 40px;
      text-align: left;
    }
  }
  /deep/ .el-table tbody {
    .el-table__cell {
      color: #333;
      font-size: 14px;
      font-weight: 400;
      font-family: PingFang SC-Regular, PingFang SC;
      &:first-child {
        border-left: 0px solid #ebeef5;
      }
      &:last-child {
        border-right: 0px solid #ebeef5;
      }
    }
  }
  /deep/.table-operate {
    .el-button + .el-button {
      margin-left: 8px;
    }
    .el-button {
      background: transparent;
      padding: 5px 12px;
      border: none;
      &.el-button--primary {
        // border: 1px solid @primary-color;

        // border-radius: 19px;
        // background-color: #fff;
        color: @primary-color;
        // &:hover {
        //   background-color: @primary-color;
        //   color: #fff;
        // }
      }
      &.el-button--danger {
        // border: 1px solid @error-color;
        // padding: 5px 12px;
        // border-radius: 19px;
        // background-color: #fff;
        color: @error-color;
        // &:hover {
        //   background-color: @error-color;
        //   color: #fff;
        // }
      }
      &.el-button--warning {
        // border: 1px solid @warning-color;
        // padding: 5px 12px;
        // border-radius: 19px;
        // background-color: #fff;
        // color: @warning-color;
        color: @primary-color;
        // &:hover {
        //   background-color: @warning-color;
        //   color: #fff;
        // }
      }
    }
  }
  /deep/.hx-table-row {
    &:hover > td.el-table__cell {
      background-color: rgba(240, 247, 255, 1);
    }
  }
  // /deep/ .el-table__body-wrapper {
  //   &::-webkit-scrollbar {
  //     width: 10px;
  //     height: 1px;
  //   }
  //   &::-webkit-scrollbar-thumb {
  //     border-radius: 0.10417vw;
  //     box-shadow: inset 0 0 0.26042vw rgba(0, 0, 0, 0.2);
  //     background: #dcdee2;
  //   }
  // }
}
</style>
