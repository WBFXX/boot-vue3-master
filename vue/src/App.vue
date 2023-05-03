<script setup>
import { RouterView } from 'vue-router'
import router from "@/router";
</script>

<template>
<!--  <RouterView :key="router.currentRoute.value.fullPath + Math.random()" />-->
  <router-view :key="$route.fullPath"></router-view>
</template>

