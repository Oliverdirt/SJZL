<script>
export default {
  name: 'render-column',
  props: {
    render: {
      type: Function,
      default: () => () => {}
    },
    scope: {
      type: Object,
      default: () => {}
    }
  },
  render (h) {
    const { row, column, $index } = this.scope
    return this.render(h, row, column, $index)
  }
}
</script>
