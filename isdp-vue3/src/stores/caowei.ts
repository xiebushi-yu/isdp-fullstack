import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useCaoWeiStore = defineStore('caowei', () => {
  const caopiMsg = ref('')
  const caozhiMsg = ref('')
  function writePoem() {
    caozhiMsg.value = '煮豆燃豆萁,豆在釜中泣.本是同根生,相煎何太急.'
  }
  return { caopiMsg, caozhiMsg, writePoem }
})
