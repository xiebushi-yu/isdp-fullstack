<template>
  <div class="grandchild">
    <h2>孙组件 曹睿</h2>
    <h3>收到祖父-曹操的消息：{{ fromCaocaoMsg }}</h3>
    <h3>收到父亲-曹丕的消息：{{ fromCaopiMsg }}</h3>
    <el-space>
      <el-input v-model="caoruiMsg" style="width: 350px" placeholder="请输入内容" /><br />
      <el-button type="primary" round @click="sendToGrandfather">发给祖父</el-button>
      <el-button type="primary" round @click="sendToFather">发给父亲</el-button>
    </el-space>
  </div>
</template>

<script lang="ts" setup>
import { ref, inject, watch } from 'vue'

// 定义注入的键类型
interface CaoWeiInjection {
  caopi: {
    fromCaopiMsg: string
    sendToCaopi: (msg: string) => void
  }
  caorui: {
    fromCaocaoMsg: string
    sendToCaocao: (msg: string) => void
  }
}

// 注入数据和方法
const { caopi, caorui } = inject<CaoWeiInjection>('caowei')!

const caoruiMsg = ref('')
const fromCaocaoMsg = ref('')
const fromCaopiMsg = ref('')

// 关键：监听祖父曹操的消息（跨层）
watch(
  () => caorui.fromCaocaoMsg,
  (newVal) => {
    if (newVal) {
      fromCaocaoMsg.value = newVal
      console.log('曹睿收到祖父消息:', newVal)
      // 显示后清空消息，准备接收下一条
      setTimeout(() => {
        caorui.fromCaocaoMsg = ''
      }, 100)
    }
  },
)

// 关键：监听父亲曹丕的消息
watch(
  () => caopi.fromCaopiMsg,
  (newVal) => {
    if (newVal) {
      fromCaopiMsg.value = newVal
      console.log('曹睿收到父亲消息:', newVal)
      // 显示后清空消息，准备接收下一条
      setTimeout(() => {
        caopi.fromCaopiMsg = ''
      }, 100)
    }
  },
)

const sendToFather = () => {
  if (caoruiMsg.value.trim()) {
    caopi.sendToCaopi(`${caoruiMsg.value}`)
    caoruiMsg.value = ''
  }
}

const sendToGrandfather = () => {
  if (caoruiMsg.value.trim()) {
    caorui.sendToCaocao(`${caoruiMsg.value}`)
    caoruiMsg.value = ''
  }
}
</script>

<style scoped>
.grandchild {
  display: grid;
  justify-content: center;
  align-items: center;
  background-color: rgb(215, 237, 195);
  width: 600px;
  height: 300px;
  border: 2px solid #67c23a;
  border-radius: 8px;
  margin: 10px;
}
</style>
