<template>
  <div>
    <button @click.stop="favor" class="my_button" style="cursor: pointer"
            :style="{backgroundColor:bg_color, color: ft_color}"
            @mouseenter="change" @mouseleave="goback">
      {{ content }}
    </button>
  </div>
</template>


<script setup>

import {ref} from "vue";



    const liked = ref(false);
    const content = ref('+关注');
    const bg_color = ref("#fef0f0");
    const ft_color = ref("#f56c6c");



    const favor = () => {
      liked.value = !liked.value;
      if (liked.value) {
        content.value = "已关注";
        bg_color.value = "#f56c6c";
        ft_color.value = "#fef0f0";
      } else {
        content.value = "+关注"
        bg_color.value = "#fef0f0";
        ft_color.value = "#f56c6c";

      }
    };


    const change = () => {
      bg_color.value = "#ff9999";
      ft_color.value = "#fef0f0";
    };

    const goback = () => {
      if (liked.value) {
        bg_color.value = "#f56c6c";
        ft_color.value = "#fef0f0";
      } else {
        bg_color.value = "#fef0f0";
        ft_color.value = "#f56c6c";
      }
    };



</script>

<style scoped>

button {
  outline: none;
}

.my_button {
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
