<template>
    <div>
        <div class="comment-div">
            <Card>
                <p slot="title">
                    意见及批注
                </p>
                <div class="comment-textarea">

                    <Input v-model="comment" type="textarea" :autosize="{minRows: 3,maxRows: 3}"
                           placeholder="请输入意见和批注"></Input>
                </div>
            </Card>
        </div>
    </div>

</template>

<script>
import comment from './eventComment'
export default {
  name: 'ProcessBottom',
  data () {
    return {
      comment: ''
    }
  },
  watch: {
    comment (val) {
      comment.$emit('commentChange', val)
    }
  }
}
</script>

<style scoped>
    .ivu-card {
        background: #f8f8f9;
    }

    .comment-div {
        width: calc(100% - 220px);
        position: fixed;
        bottom: 10px;
        z-index: 100;
    }

    .comment-textarea {
        width: 100%;
        padding: 0 10px;
    }
</style>
