<template>
  <div class="child">
    <h2>子组件 曹丕</h2>
    <h3>收到父亲-曹操的消息：{{ fromCaocaoMsg }}</h3>
    <h3>收到兄弟-曹植的消息：{{ fromCaozhiMsg }}</h3>
    <el-space>
      <el-input v-model="caopiMsg" style="width: 350px" placeholder="请输入内容" /><br />
      <el-button type="primary" round @click="sendToFather">发给父亲</el-button>
      <el-button type="primary" round @click="sendToBrother">发给兄弟</el-button>
    </el-space>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
const caopiMsg = ref('')
// const fromCaocaoMsg = ref('')
const fromCaozhiMsg = ref('')
// 收到父亲消息
const fromCaocaoMsg = defineModel('fromCaocaoMsg')
// 发给父亲的消息
const toCaocaoMsg = defineModel('toCaocaoMsg')

const sendToFather = () => {
  toCaocaoMsg.value = caopiMsg.value
}
const sendToBrother = () => {}
</script>

<style scoped>
.child {
  display: grid;
  justify-content: center;
  align-items: center;
  background-color: rgb(192, 222, 223);
  height: 400px;
}
</style>
