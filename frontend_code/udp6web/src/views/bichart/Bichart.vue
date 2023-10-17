<template>
  <div id="bibb">
    <iframe src="/ureport/designer" width="100%" height="100%"  frameborder="0"></iframe>
<!--    <Embed :src="baseUrl+'/ureport/designer'" width="100%" height="100%"/>-->
    <!--    <object data="http://10.1.90.188:9000/ureport/designer" width="100%" height="100%" type="text/html">-->
    <!--      Alternative Content-->
    <!--    </object>-->
  </div>
</template>

<script>
export default {
  name: 'Bichart'
}
</script>

<style scoped>
#bibb {
  width: 100%;
  height: 100%;
}
</style>
