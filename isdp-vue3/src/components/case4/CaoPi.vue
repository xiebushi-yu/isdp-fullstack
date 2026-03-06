<template>
  <div class="child">
    <h2>父组件 曹丕</h2>
    <h3>收到父亲-曹操的消息：{{ fromCaocaoMsg }}</h3>
    <h3>收到儿子-曹睿的消息：{{ fromCaoruiMsg }}</h3>
    <el-space>
      <el-input v-model="caopiMsg" style="width: 350px" placeholder="请输入内容" /><br />
      <el-button type="primary" round @click="sendToFather">发给父亲</el-button>
      <el-button type="primary" round @click="sendToSon">发给儿子</el-button>
    </el-space>
    <CaoRui />
  </div>
</template>

<script lang="ts" setup>
import { ref, inject, provide, watch } from 'vue'
import CaoRui from './CaoRui.vue'

// 注入祖父曹操的数据和方法
interface CaoWeiInjection {
  caopi: {
    fromCaocaoMsg: string
    sendToCaocao: (msg: string) => void
  }
  caorui: {
    fromCaocaoMsg: string
    sendToCaocao: (msg: string) => void
  }
}

const { caopi, caorui } = inject<CaoWeiInjection>('caowei')!

const caopiMsg = ref('')
const fromCaocaoMsg = ref('')
const fromCaoruiMsg = ref('')

// 为儿子曹叡提供的数据
const toCaoruiMsg = ref('')

// 关键：监听父亲曹操的消息
watch(
  () => caopi.fromCaocaoMsg,
  (newVal) => {
    if (newVal) {
      fromCaocaoMsg.value = newVal
      console.log('曹丕收到父亲消息:', newVal)
      // 显示后清空消息，准备接收下一条
      setTimeout(() => {
        caopi.fromCaocaoMsg = ''
      }, 100)
    }
  },
)

// 为儿子曹叡提供数据和方法
provide('caowei', {
  // 曹丕自己的数据和方法
  caopi: {
    fromCaopiMsg: fromCaoruiMsg,
    sendToCaopi: (msg: string) => {
      fromCaoruiMsg.value = msg
    },
  },
  // 继承祖父给曹叡的数据和方法（跨层传递）
  caorui: caorui,
})

const sendToFather = () => {
  if (caopiMsg.value.trim()) {
    caopi.sendToCaocao(caopiMsg.value)
    caopiMsg.value = ''
  }
}

const sendToSon = () => {
  if (caopiMsg.value.trim()) {
    toCaoruiMsg.value = caopiMsg.value // 更新 provide 的数据
    caopiMsg.value = ''
  }
}
</script>

<style scoped>
.child {
  display: grid;
  justify-content: center;
  align-items: center;
  background-color: rgb(192, 222, 223);
  height: 600px;
  border: 2px solid #409eff;
  border-radius: 8px;
  margin: 10px;
}
</style>
