<script setup>
import {ref, onMounted} from "vue";
import axios from "axios";
const message = ref("");

onMounted(async () => {
  const response = await  axios.get("http://localhost:8080/api/hello");
  message.value = response.data;
})

</script>

<template>
  <h1>{{message}}</h1>
  <br>
  <h1>Ledear the`m bưởi</h1>
  <img src="/picture/ledear.jpg" alt="">
</template>

