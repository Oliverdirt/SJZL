<template>
    <Modal
            v-model="modalVisible" title="查看流程图" width="900" class-name="vertical-center-modal" footer-hide>
        <div style="height: 60vh;overflow: auto;background-color: #f2f2f2">
            <div style="padding-top: 40px;" v-html="svg"></div>
        </div>

    </Modal>
</template>

<script>
import util from '@/libs/util.js'
export default {
  name: 'FlowDiagram',
  data () {
    return {
      modalVisible: false,
      marked: false,
      dispose: false,

      process: null,
      elements: null,

      svg: null,
      processInstanceId: null,
      processDefinitionId: null,
      processDefinitionKey: null,

      style: {
        blank: {
          color: 'white',
          backgroundColor: '#9e9e9e'
        },
        history: {
          color: 'red',
          backgroundColor: '#FAFAFA'
        },
        active: {
          color: '#5DEA5D',
          backgroundColor: '#FAFAFA'
        }
      }
    }
  },
  watch: {
    svg: function (value) {
      if (!this.marked || value === null) return

      if (!this.dispose) {
        let t_svg = value
        // 处理svg
        let idx = value.indexOf('<defs>') + 6
        let s1 = value.substring(0, idx)
        let s2 = value.substring(idx)
        let marked = '<marker id="marked" viewBox="0 0 20 20" refX="11" refY="10" markerWidth="10" markerHeight="10" orient="auto"><path d="M 1 5 L 11 10 L 1 15 Z" style="fill: red; stroke-width: 1px; stroke-linecap: round; stroke-dasharray: 10000, 1; stroke: red;"/></marker>'
        t_svg = s1 + marked + s2

        // 获取marked元素
        if (this.marked) {
          this.getElements()
        }
        this.defaultRender()
        console.log(this.svg)
      }
    },
    processInstanceId: function (value) {
      if (value !== null) {
        this.marked = true
        this.getProcessByProcessInstanceId()
      }
    },
    processDefinitionId: function (value) {
      if (value !== null) {
        this.process = this.getProcessByProcessDefinitionId()
      }
    },
    processDefinitionKey: function (value) {
      if (value !== null) {
        this.getProcessByProcessDefinitionKey()
      }
    },
    process: function (value) {
      if (value !== null) {
        this.svg = value.svg
      }
    },
    elements: function (elements) {
      if (!this.marked) return
      if (elements == null || elements.length === 0) return

      let history = this.style.history
      let active = this.style.active

      for (let i = 0; i < elements.length; i++) {
        let ele = elements[i]
        let _ele = $('[data-element-id=' + ele.key + ']')
        let _visual = _ele.find('g.djs-visual')
        switch (ele.elementType) {
          case 'StartEvent':
          case 'EndEvent':
            _visual.find('circle').css('stroke', history.color)
            _visual.find('circle').css('fill', history.backgroundColor)
            $('[data-element-id=' + ele.key + '_label]').find('g.djs-visual text').css('fill', history.color)
            break
          case 'UserTask':
          case 'ServiceTask':
            let ct = history
            if (ele.active) {
              ct = active
            }
            _visual.find('rect').css('stroke', ct.color)
            _visual.find('rect').css('fill', ct.backgroundColor)
            _visual.find('text').css('fill', ct.color)
            _visual.find('path').css('stroke', ct.color)
            break
          case 'SequenceFlow':
            _visual.find('path').css('stroke', history.color)
            _visual.find('path').css('marker-end', 'url("#marked")')
            $('[data-element-id=' + ele.key + '_label]').find('g.djs-visual text').css('fill', history.color)
            break
          case 'ExclusiveGateway':
          case 'ParallelGateway':
            _visual.find('polygon').css('stroke', history.color)
            _visual.find('path').css('fill', history.color)
            _visual.find('path').css('stroke', history.color)
            $('[data-element-id=' + ele.key + '_label]').find('g.djs-visual text').css('fill', history.color)
            break
          default:
            break
        }
      }
    }
  },
  methods: {

    defaultRender () {
      $('[data-element-id] g.djs-visual rect').css('fill', this.style.blank.color)
      $('[data-element-id] g.djs-visual circle').css('fill', this.style.blank.color)

      $('[data-element-id] g.djs-visual rect').css('stroke', this.style.blank.backgroundColor)
      $('[data-element-id] g.djs-visual circle').css('stroke', this.style.blank.backgroundColor)
      $('[data-element-id] g.djs-visual path').css('stroke', this.style.blank.backgroundColor)
      $('[data-element-id] g.djs-visual polygon').css('stroke', this.style.blank.backgroundColor)

      let paths = $('[data-element-id] g.djs-visual path')
      for (let i = 0; i < paths.length; i++) {
        if ($(paths[i]).siblings('polygon').length > 0) {
          $(paths[i]).css('fill', this.style.blank.backgroundColor)
        }
      }

      $('marker:gt(0) path').css({
        fill: this.style.blank.backgroundColor,
        stroke: this.style.blank.backgroundColor
      })
    },
    getProcessByProcessInstanceId: function () {
      if (this.processInstanceId === null) return null
      let This = this
      util.http.get('/api/activiti/query/getByProcessInstanceId/' + this.processInstanceId).then(response => {
        This.process = response.data
      })
    },
    getProcessByProcessDefinitionKey: function () {
      if (this.processDefinitionKey == null) return null
      let This = this
      util.http.get('/api/activiti/bpmn/getMaxVersionByProcessDefinitionKey/' + this.processDefinitionKey).then(response => {
        This.process = response.data
      })
    },
    getProcessByProcessDefinitionId: function () {
      let This = this
      util.http.get('/api/activiti/bpmn/getByProcessDefinitionId/' + this.processDefinitionId).then(response => {
        This.process = response.data
      })
    },
    getElements: function () {
      let _this = this
    }
  }
}
</script>

<style lang="less">
    .vertical-center-modal {
        display: flex;
        align-items: center;
        justify-content: center;

        .ivu-modal {
            top: 0;
        }
    }
</style>
