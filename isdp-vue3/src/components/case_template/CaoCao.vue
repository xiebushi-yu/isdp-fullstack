<template>
  <div class="about">
    <el-row style="height: 400px">
      <el-col align="center" style="line-height: 70px">
        <h1>演示组件通信: 通信方式名称???</h1>
        <h2>父组件 曹操</h2>
        <h3>收到子组件-曹丕的消息：{{ fromCaopiMsg }}</h3>
        <h3>收到子组件-曹植的消息：{{ fromCaozhiMsg }}</h3>
        <el-space>
          <el-input v-model="caocaoMsg" style="width: 350px" placeholder="请输入内容" />
          <el-button type="primary" round @click="sendToCaopi">发给曹丕</el-button>
          <el-button type="primary" round @click="sendToCaozhi">发给曹植</el-button>
        </el-space>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <CaoPi></CaoPi>
      </el-col>
      <el-col :span="12">
        <CaoZhi></CaoZhi>
      </el-col>
    </el-row>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import CaoPi from './caopi.vue'
import CaoZhi from './CaoZhi.vue'
const caocaoMsg = ref('')
const fromCaopiMsg = ref('')
const fromCaozhiMsg = ref('')

const sendToCaopi = () => {}
const sendToCaozhi = () => {}
</script>

<style>
.about {
  line-height: 2;
  background-color: white;
  height: 90vh;
}
</style>
