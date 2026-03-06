<template>
  <div class="about">
    <el-row style="height: 400px">
      <el-col align="center" style="line-height: 70px">
        <h1>演示组件通信: inject</h1>
        <h2>祖父组件 曹操</h2>
        <h3>收到子组件-曹丕的消息：{{ fromCaopiMsg }}</h3>
        <h3>收到孙组件-曹睿的消息：{{ fromCaoruiMsg }}</h3>
        <el-space>
          <el-input v-model="caocaoMsg" style="width: 350px" placeholder="请输入内容" />
          <el-button type="primary" round @click="sendToCaopi">发给曹丕</el-button>
          <el-button type="primary" round @click="sendToCaorui">发给曹睿</el-button>
        </el-space>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <CaoPi></CaoPi>
      </el-col>
    </el-row>
  </div>
</template>

<script lang="ts" setup>
import { ref, provide } from 'vue'
import CaoPi from '@/components/case4/CaoPi.vue'
const caocaoMsg = ref('')
const fromCaopiMsg = ref('')
const fromCaoruiMsg = ref('')

// 为曹丕提供的数据和方法
const toCaopiMsg = ref('')
// 为曹叡提供的数据和方法
const toCaoruiMsg = ref('')

// 为整个曹魏家族提供数据和方法
provide('caowei', {
  // 给曹丕的数据和方法
  caopi: {
    fromCaocaoMsg: toCaopiMsg,
    sendToCaocao: (msg: string) => {
      fromCaopiMsg.value = msg
    },
  },
  // 给曹叡的数据和方法（跨层）
  caorui: {
    fromCaocaoMsg: toCaoruiMsg,
    sendToCaocao: (msg: string) => {
      fromCaoruiMsg.value = msg
    },
  },
})

const sendToCaopi = () => {
  if (caocaoMsg.value.trim()) {
    toCaopiMsg.value = caocaoMsg.value // 更新 provide 的数据
    caocaoMsg.value = ''
  }
}

const sendToCaorui = () => {
  if (caocaoMsg.value.trim()) {
    toCaoruiMsg.value = caocaoMsg.value // 更新 provide 的数据
    console.log('曹操发送给曹睿:', caocaoMsg.value)
    caocaoMsg.value = ''
  }
}
</script>

<style>
.about {
  line-height: 2;
  background-color: white;
  height: 90vh;
}
</style>
