<template>
  <div>
    <alert />
    <table-header />
    <form-table />
    <Page :total="100" style="text-align:center;margin-top:50px" show-total show-elevator/>
  </div>
</template>

<script>
import Alert from './components/alert.vue'
import TableHeader from './components/table-header.vue'
import FormTable from './components/table.vue'
export default {
  components: { TableHeader, FormTable, Alert }
}
</script>
