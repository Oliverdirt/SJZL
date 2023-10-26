<template>
  <div id="magic">
   <iframe  id="iframe" width="100%" height="100%" :src="$util.baseUrl+'/api/magic?directUrl='+$util.baseUrl+'/magic/web/index.html&token='+this.token" frameborder="0"></iframe>
   <!-- <iframe  id="iframe" width="100%" height="100%" :src="" frameborder="0"></iframe> -->
   <!-- <iframe id="iframe" :src='""' width="100%" height="100%" frameborder="0"> -->
     <!-- <button onclick="test()"></button> -->
   <!-- </iframe> -->
   <!-- <iframe  id="iframe" width="100%" height="100%" :src="$util.baseUrl+'/api/magic?directUrl='+$util.baseUrl+'/magic/web/index.html&token='+this.token" frameborder="0"></iframe> -->
   <!-- <a ref="testa" target="_blank" :href="$util.baseUrl+'/magic/web/index.html'"></a> -->

  </div>

</template>

<script>
import util from '@/libs/util'
export default {
  // created(){
  //   this.token = window.localStorage.getItem('token')
  // },
  // created() {
  //     setTimeout(() => {
  //       var iframe = document.querySelector("#iframe");
  //       this.populateIframe(iframe, [["Authorization",  localStorage.getItem("token")]]);
  //     }, 0);
  //   },
  name: 'Magic',
  data(){
    return{
      token:''
    }
  },
  mounted () {
    this.token = window.localStorage.getItem('token')
    // console.log(this.token);
    // this.$refs.testa.click()
    this.toMagic();
  },
  methods:{
    populateIframe(iframe, headers) {
      var basUrl = util.baseUrl;
      var xhr = new XMLHttpRequest();
      xhr.open("get",basUrl+'/magic/web/index.html');
      xhr.responseType = "blob";
      headers.forEach((header) => {
      xhr.setRequestHeader(header[0], header[1]);
      });
      xhr.onreadystatechange = () => {
      if (xhr.readyState === xhr.DONE) {
      if (xhr.status === 200) {
        iframe.src = URL.createObjectURL(xhr.response);
      }
      }
      };
      xhr.send();
   },
    toMagic(){
      util.http.get('/api/magic?directUrl='+util.baseUrl+'/magic/web/index.html')
    }
  }
}
</script>

<style scoped lang="less">
#magic {
  width: 100%;
  height: 100%;
}
</style>
