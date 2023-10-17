<template>
  <div class="panel-tab__content">
    <el-form size="mini" label-width="90px" @submit.native.prevent>
      <el-form-item label="比例或票数" v-if="!subProcessFalse">
        <el-input v-model="scheme"></el-input>
      </el-form-item>
      <el-form-item label="多实例策略" v-if="!subProcessFalse">
        <el-select v-model="loopInstanceName" clearable @change="changeInstanceName">
          <el-option v-for="item in loopInstanceNameList" :key="item.value" :label="item.label" :value="item.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="回路特性">
        <el-select v-model="loopCharacteristics" @change="changeLoopCharacteristicsType">
          <!--bpmn:MultiInstanceLoopCharacteristics-->
          <el-option label="并行多重事件" value="ParallelMultiInstance" />
          <el-option label="时序多重事件" value="SequentialMultiInstance" />
          <!--bpmn:StandardLoopCharacteristics-->
          <el-option label="循环事件" value="StandardLoop" />
          <el-option label="无" value="Null" />
        </el-select>
      </el-form-item>
      <template v-if="loopCharacteristics === 'ParallelMultiInstance' || loopCharacteristics === 'SequentialMultiInstance'">
        <el-form-item label="循环基数" key="loopCardinality">
          <el-input v-model="loopInstanceForm.loopCardinality" clearable @change="updateLoopCardinality" />
        </el-form-item>
        <el-form-item label="集合" key="collection">
          <el-input clearable v-model="loopInstanceForm.collection" @change="updateLoopBase" :disabled="!!loopInstanceName" />
        </el-form-item>
        <el-form-item label="元素变量" key="elementVariable">
          <el-input v-model="loopInstanceForm.elementVariable" clearable @change="updateLoopBase" />
        </el-form-item>
        <el-form-item label="完成条件" key="completionCondition">
          <el-input clearable v-model="loopInstanceForm.completionCondition" @change="updateLoopCondition" :disabled="!!loopInstanceName" />
        </el-form-item>
        <el-form-item label="异步状态" key="async">
          <el-checkbox v-model="loopInstanceForm.asyncBefore" label="异步前" @change="updateLoopAsync('asyncBefore')" />
          <el-checkbox v-model="loopInstanceForm.asyncAfter" label="异步后" @change="updateLoopAsync('asyncAfter')" />
          <el-checkbox
            v-model="loopInstanceForm.exclusive"
            v-if="loopInstanceForm.asyncAfter || loopInstanceForm.asyncBefore"
            label="排除"
            @change="updateLoopAsync('exclusive')"
          />
        </el-form-item>
        <el-form-item label="重试周期" prop="timeCycle" v-if="loopInstanceForm.asyncAfter || loopInstanceForm.asyncBefore" key="timeCycle">
          <el-input v-model="loopInstanceForm.timeCycle" clearable @change="updateLoopTimeCycle" />
        </el-form-item>
      </template>
    </el-form>
  </div>
</template>

<script>
export default {
  name: "ElementMultiInstance",
  props: {
    businessObject: Object,
    type: String,
    loopInstanceNameList: Array
  },
  inject: {
    prefix: "prefix"
  },
  data() {
    return {
      subProcessFalse: false,
      loopInstanceName: '',
      scheme:this.scheme,
      loopCharacteristics: "",
      //默认配置，用来覆盖原始不存在的选项，避免报错
      defaultLoopInstanceForm: {
        completionCondition: "",
        loopCardinality: "",
        extensionElements: [],
        asyncAfter: false,
        asyncBefore: false,
        exclusive: false
      },
      loopInstanceForm: {}
    };
  },
  watch: {
    businessObject: {
      immediate: true,
      handler(val) {
        if(val.$type === 'bpmn:SubProcess'){
          this.subProcessFalse = true
        }
        this.bpmnElement = window.bpmnInstances.bpmnElement;
        this.getElementLoop(val);
      }
    },
    scheme: {
      handler:function(val){
        let params = {
          // 多实例的id
          taskDefinitionKey: window.bpmnInstances.bpmnElement.id,
          scheme: parseInt(this.scheme)
        }
        this.$http.post('/api/flow/multi/updateMulti',params).then((res) => {
          console.log("修改成功")
        })
      }
    }
  },
  mounted() {
    this.$http.get(`/api/flow/multi/selectMultiByKey?taskKey=${window.bpmnInstances.bpmnElement.id}`).then((res) => {
      this.loopInstanceName = res.data.result?.taskMulti
      this.scheme = res.data.result?.scheme
          }).catch(() => {
            this.$Message.error('查询失败')
          })
  },
  methods: {
    getElementLoop(businessObject) {
      if (!businessObject.loopCharacteristics) {
        this.loopCharacteristics = "Null";
        this.loopInstanceForm = {};
        return;
      }
      if (businessObject.loopCharacteristics.$type === "bpmn:StandardLoopCharacteristics") {
        this.loopCharacteristics = "StandardLoop";
        this.loopInstanceForm = {};
        return;
      }
      if (businessObject.loopCharacteristics.isSequential) {
        this.loopCharacteristics = "SequentialMultiInstance";
      } else {
        this.loopCharacteristics = "ParallelMultiInstance";
      }
      // 合并配置
      this.loopInstanceForm = {
        ...this.defaultLoopInstanceForm,
        ...businessObject.loopCharacteristics,
        completionCondition: businessObject.loopCharacteristics?.completionCondition?.body ?? '',
        collection: businessObject.loopCharacteristics?.collection ?? '',
        elementVariable: businessObject.loopCharacteristics?.elementVariable ?? '',
        loopCardinality: businessObject.loopCharacteristics?.loopCardinality?.body ?? ""
      };

      if ((!this.loopInstanceForm.collection) && this.loopInstanceName) {
        this.loopInstanceForm.collection = '${multiInstanceService.pourAssigneeCollection(execution)}'
      }

      if ((!this.loopInstanceForm.completionCondition) && this.loopInstanceName) {
        this.loopInstanceForm.completionCondition = '${multiInstanceService.hasComplete(execution)}'
        this.updateLoopCondition(this.loopInstanceForm.completionCondition)
      }

      if ((!window.bpmnInstances.bpmnElement.businessObject.loopCharacteristics.collection) && this.loopInstanceName ) {
        window.bpmnInstances.bpmnElement.businessObject.loopCharacteristics.collection = '${multiInstanceService.pourAssigneeCollection(execution)}'
      }
      // 保留当前元素 businessObject 上的 loopCharacteristics 实例
      this.multiLoopInstance = window.bpmnInstances.bpmnElement.businessObject.loopCharacteristics;
      // 更新表单
      if (
        businessObject.loopCharacteristics.extensionElements &&
        businessObject.loopCharacteristics.extensionElements.values &&
        businessObject.loopCharacteristics.extensionElements.values.length
      ) {
        this.$set(this.loopInstanceForm, "timeCycle", businessObject.loopCharacteristics.extensionElements.values[0].body);
      }
    },
    changeInstanceName(name) {
      // 这里从无到有,少了一个赋值,可以手动调用回路特性的change事件
      if (this.loopCharacteristics) {
        this.changeLoopCharacteristicsType(this.loopCharacteristics)
      }
      if (name) { // name不为空,调用post接口
        let params = {
          // 多实例的id
          taskDefinitionKey: window.bpmnInstances.bpmnElement.id,
          taskMulti: name,
          scheme: parseInt(this.scheme)
        }
        this.$http.post('/api/flow/multi/updateMulti',params).then((res) => {
            this.$Message.success('保存成功')
          }).catch(() => {
            this.$Message.error('保存失败')
          })
      } else { // name为空,调用删除接口,同时清空集合和完成条件的值
        if (this.loopInstanceForm.collection) {
          this.loopInstanceForm.collection = ''
      }

      if (this.loopInstanceForm.completionCondition) {
        this.loopInstanceForm.completionCondition = ''
        this.updateLoopCondition(this.loopInstanceForm.completionCondition)
      }

      if (window.bpmnInstances.bpmnElement.businessObject?.loopCharacteristics?.collection) {
        window.bpmnInstances.bpmnElement.businessObject.loopCharacteristics.collection = ''
        }

        this.$http.get(`/api/flow/multi/deleteMultiByKey?taskKey=${window.bpmnInstances.bpmnElement.id}`).then((res) => {
            this.$Message.success('修改成功')
          }).catch(() => {
            this.$Message.error('修改失败')
          })
      }
    },

    changeLoopCharacteristicsType(type) {
      // this.loopInstanceForm = { ...this.defaultLoopInstanceForm }; // 切换类型取消原表单配置
      // 取消多实例配置
      if (type === "Null") {
        window.bpmnInstances.modeling.updateProperties(this.bpmnElement, { loopCharacteristics: null });
        return;
      }
      // 配置循环
      if (type === "StandardLoop") {
        const loopCharacteristicsObject = window.bpmnInstances.moddle.create("bpmn:StandardLoopCharacteristics");
        window.bpmnInstances.modeling.updateProperties(this.bpmnElement, {
          loopCharacteristics: loopCharacteristicsObject
        });
        this.multiLoopInstance = null;
        return;
      }
      // 时序
      if (type === "SequentialMultiInstance") {
        this.multiLoopInstance = window.bpmnInstances.moddle.create("bpmn:MultiInstanceLoopCharacteristics", {
          isSequential: true
        });
      } else {
        this.multiLoopInstance = window.bpmnInstances.moddle.create("bpmn:MultiInstanceLoopCharacteristics");
      }
      window.bpmnInstances.modeling.updateProperties(this.bpmnElement, {
        loopCharacteristics: this.multiLoopInstance
      });
    },
    // 循环基数
    updateLoopCardinality(cardinality) {
      let loopCardinality = null;
      if (cardinality && cardinality.length) {
        loopCardinality = window.bpmnInstances.moddle.create("bpmn:FormalExpression", { body: cardinality });
      }
      window.bpmnInstances.modeling.updateModdleProperties(this.bpmnElement, this.multiLoopInstance, {
        loopCardinality
      });
    },
    // 完成条件
    updateLoopCondition(condition) {
      let completionCondition = null;
      if (condition && condition.length) {
        completionCondition = window.bpmnInstances.moddle.create("bpmn:FormalExpression", { body: condition });
      }
      window.bpmnInstances.modeling.updateModdleProperties(this.bpmnElement, this.multiLoopInstance, {
        completionCondition
      });
    },
    // 重试周期
    updateLoopTimeCycle(timeCycle) {
      const extensionElements = window.bpmnInstances.moddle.create("bpmn:ExtensionElements", {
        values: [
          window.bpmnInstances.moddle.create(`${this.prefix}:FailedJobRetryTimeCycle`, {
            body: timeCycle
          })
        ]
      });
      window.bpmnInstances.modeling.updateModdleProperties(this.bpmnElement, this.multiLoopInstance, {
        extensionElements
      });
    },
    // 直接更新的基础信息
    updateLoopBase() {
      window.bpmnInstances.modeling.updateModdleProperties(this.bpmnElement, this.multiLoopInstance, {
        collection: this.loopInstanceForm.collection || null,
        elementVariable: this.loopInstanceForm.elementVariable || null
      });
    },
    // 各异步状态
    updateLoopAsync(key) {
      const { asyncBefore, asyncAfter } = this.loopInstanceForm;
      let asyncAttr = Object.create(null);
      if (!asyncBefore && !asyncAfter) {
        this.$set(this.loopInstanceForm, "exclusive", false);
        asyncAttr = { asyncBefore: false, asyncAfter: false, exclusive: false, extensionElements: null };
      } else {
        asyncAttr[key] = this.loopInstanceForm[key];
      }
      window.bpmnInstances.modeling.updateModdleProperties(this.bpmnElement, this.multiLoopInstance, asyncAttr);
    }
  },
  beforeDestroy() {
    this.multiLoopInstance = null;
    this.bpmnElement = null;
  }
};
</script>
