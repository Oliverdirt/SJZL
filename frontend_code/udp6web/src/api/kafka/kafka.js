import Util from '@/libs/util'

// 获取kafka元信息
export function getKafkaAddress () {
  return Util.http({
    url: `/api/kafka/getIp`,
    method: 'post'
  })
}

// 查询所有kafka资源信息
export function getAllSourceInfo () {
  return Util.http({
    url: `/api/kafka/getSource`,
    method: 'post'
  })
}

// /api/kafka/getSource
// 根据条件获取资源信息
export function getSourceInfoByParams (params) {
  return Util.http({
    url: `/api/kafka/getSource`,
    method: 'get',
    params
  })
}

// 通过id删除kafka资源信息
export function delSourceById (id) {
  return Util.http({
    url: `/api/kafka/deleteSource/${id}`,
    method: 'post'
  })
}

// 新增kafka资源信息
export function addSource (data) {
  return Util.http({
    url: `/api/kafka/add`,
    method: 'post',
    data
  })
}

// /api/kafka/produce
// 发送信息
export function produceMsg (data) {
  return Util.http({
    url: `/api/kafka/produce`,
    method: 'post',
    data
  })
}

// /api/kafka/createTopic
// 创建topic
export function createTopic (data) {
  return Util.http({
    url: `/api/kafka/createTopic`,
    method: 'post',
    data
  })
}

// /api/kafka/cluster/info
export function getClusterInfo (data) {
  return Util.http({
    url: `/api/kafka/cluster/info`,
    method: 'post',
    data
  })
}

// /api/kafka/deleteTopic
export function deleteTopicByData (data) {
  return Util.http({
    url: `/api/kafka/deleteTopic`,
    method: 'post',
    data
  })
}

// /api/topicForms/export
export function exportTopicByData (responseType) {
  return Util.http({
    url: `/api/topicForms/export`,
    method: 'post',
    responseType

  })
}

// /api/kafka/group/detail
// 查询消费组详情
export function getGroupDetail (data) {
  return Util.http({
    url: `/api/kafka/group/detail`,
    method: 'post',
    data

  })
}

// /api/kafka/group/all
// 查询消费组
export function getGroupByData (data) {
  return Util.http({
    url: `/api/kafka/group/all`,
    method: 'post',
    data

  })
}

// /api/kafka/group/delete
export function delGroupByData (data) {
  return Util.http({
    url: `/api/kafka/group/delete`,
    method: 'post',
    data

  })
}

// /api/kafka/getAllSourceAuth
export function getAllSourceAuth () {
  return Util.http({
    url: `/api/kafka/getAllSourceAuth`,
    method: 'get'

  })
}

// /api/kafka/group/search
export function searchGroupByData (data) {
  return Util.http({
    url: `/api/kafka/group/search`,
    method: 'post',
    data

  })
}

// /api/topicForms/export
// 导出
export function exportGroupByData (responseType) {
  return Util.http({
    url: `/api/topicForms/export`,
    method: 'post',
    responseType

  })
}

// /api/kafka/auth
// 授权
export function operationAuthByData (data) {
  return Util.http({
    url: `/api/kafka/auth`,
    method: 'post',
    data

  })
}

// /api/kafka/getTopics
export function getTopicsByData (data) {
  return Util.http({
    url: `/api/kafka/getTopics`,
    method: 'post',
    data

  })
}

// /api/kafka/searchTopic
export function searchTopicByData (data) {
  return Util.http({
    url: `/api/kafka/searchTopic`,
    method: 'post',
    data

  })
}

// /api/kafka/getGroupsByTopic
export function getGroupsByTopicByData (data) {
  return Util.http({
    url: `/api/kafka/getGroupsByTopic`,
    method: 'post',
    data

  })
}

// /api/kafka/getTopicDetail
export function getTopicDetailByData (data) {
  return Util.http({
    url: `/api/kafka/getTopicDetail`,
    method: 'post',
    data

  })
}
