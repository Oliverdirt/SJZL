<template>
  <div>
    <alert />
    <one-column />
    <two-column />
    <three-column />
  </div>
</template>

<script>
import Alert from './components/alert.vue'
import OneColumn from './components/one-column.vue'
import TwoColumn from './components/two-column.vue'
import ThreeColumn from './components/three-column.vue'
export default {
  components: { OneColumn, TwoColumn, ThreeColumn, Alert }
}
</script>
