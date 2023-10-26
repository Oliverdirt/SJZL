<template>
  <Select v-model="sourceId" placeholder="选择kafka环境" @on-change="selectkafka" style="margin-bottom: 5px;width: 100%">
    <Option v-for="item in sources" :key="item.name" :label="item.name" :value="item.id"></Option>
  </Select>
</template>

<script>
import {
  getAllSourceInfo
} from '@/api/kafka/kafka'
export default {
  name: 'kafkaSelect',
  data () {
    return {
      sourceId: null,
      sources: []
    }
  },
  created () {
    this.getAllSource()
  },
  methods: {
    getAllSource () {
      getAllSourceInfo()
      // this.$http.post('/api/kafka/getSource')
        .then((response) => {
          this.sources = response.data
        }).catch((error) => {
          this.$message.error('查询所有kafka环境失败')
        })
    },
    selectkafka () {
      if (this.sourceId != null && this.sourceId != '') {
        this.$emit('kafkaChange', this.sourceId)
      }
    }
  }
}
</script>

<style scoped>

</style>
