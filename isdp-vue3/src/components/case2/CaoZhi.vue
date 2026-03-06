<template>
  <div class="child">
    <h2>子组件 曹植</h2>
    <h3>收到父亲-曹操的消息：{{ fromCaocaoMsg }}</h3>
    <h3>收到兄弟-曹丕的消息：{{ fromCaopiMsg }}</h3>
    <el-space>
      <el-input v-model="caozhiMsg" style="width: 350px" placeholder="请输入内容" /><br />
      <el-button type="primary" round @click="sendToFather">发给父亲</el-button>
      <el-button type="primary" round @click="sendToBrother">发给兄弟</el-button>
    </el-space>
  </div>
</template>

<script lang="ts" setup>
// import { ref } from 'vue'
// const caozhiMsg = ref('')
// const fromCaocaoMsg = ref('')
// const fromCaopiMsg = ref('')

// const sendToFather = () => {}

// const sendToBrother = () => {}

import { ref } from 'vue'
const caozhiMsg = ref('')
// const fromCaocaoMsg = ref('')
const fromCaopiMsg = ref('')
// 收到父亲消息
const fromCaocaoMsg = defineModel('fromCaocaoMsg')
// 发给父亲的消息
const toCaocaoMsg = defineModel('toCaocaoMsg')

const sendToFather = () => {
  toCaocaoMsg.value = caozhiMsg.value
}
const sendToBrother = () => {}
</script>

<style scoped>
.child {
  display: grid;
  justify-content: center;
  align-items: center;
  background-color: rgb(237, 225, 195);
  height: 400px;
}
</style>
