export default {
  name: "table-column-custom-render",
  functional: true,
  props: {
    row: Object,
    column: Object,
    render: Function
  },
  render(h, ctx) {
    const params = {
      row: ctx.props.row,
      column: ctx.props.column,
    }

    return ctx.props.render(h, params)
  }
}
