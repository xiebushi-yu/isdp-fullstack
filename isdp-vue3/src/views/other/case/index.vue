<template>
  <div>
    <CaoCao></CaoCao>
  </div>
</template>

<script lang="ts" setup>
/**
 * template: 模版，含1个父组件CaoCao.vue和2个子组件CaoPi.vue和CaoZhi.vue
 * case1: props+emit实现父子、子父通信。
 * case2: v-model实现父子、子父通信。
 * case3: pinia实现兄弟组件通信。
 */
import CaoCao from '@/components/case2/CaoCao.vue'
</script>

<style scoped></style>
