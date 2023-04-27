<template>
  <div>
    <button   @click.stop="favor"
              class="my_button" style="cursor: pointer" :style="{backgroundColor:bg_color, color: ft_color}"
              @mouseenter="change()" @mouseleave="goback()" >
      {{content}}
    </button>
  </div>
</template>


<script>
export default {
  name: "follow",
  data () {
    return{
      liked:false,
      content:'+关注',
      bg_color:"#fef0f0",
      ft_color:"#f56c6c",
    }
  },
  methods:{
    favor (e) {
      this.liked=!this.liked;
      if(this.liked){
        this.content="已关注";
        this.bg_color="#f56c6c";
        this.ft_color="#fef0f0";
      }

      else{
        this.content="+关注"
        this.bg_color="#fef0f0";
        this.ft_color="#f56c6c";

      }
    },
    change(){
      this.bg_color="#ff9999";
      this.ft_color="#fef0f0";
    },
    goback(){
      if(this.liked){
        this.bg_color="#f56c6c";
        this.ft_color="#fef0f0";
      }
      else{
        this.bg_color="#fef0f0";
        this.ft_color="#f56c6c";
      }
    }
  }
}
</script>


<style scoped>

button{
  outline:none;
}

.my_button{
  color: #f56c6c;
  background: #fef0f0;
  border: #fbc4c4 solid;
  border-radius: 20px;
  padding: 12px 23px;
  text-align: center;
  font-size: 16px;
  -webkit-transform: scale(0.7);
}

</style>

