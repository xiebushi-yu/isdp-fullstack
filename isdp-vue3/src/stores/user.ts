import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { LoginUser } from '@/types'
import { login } from '@/api/user'

export const useUserStore = defineStore(
  'user',
  () => {
    const token = ref('')
    const userName = ref('')
    function userLogin(userInfo: LoginUser) {
      return new Promise<void>((resolve, reject) => {
        login(userInfo)
          .then((response) => {
            userName.value = response.data.userName
            token.value = response.data.token
            resolve()
          })
          .catch((error) => {
            reject(error)
          })
      })
    }
    return { token, userName, userLogin }
  },
  {
    persist: true,
  },
)
