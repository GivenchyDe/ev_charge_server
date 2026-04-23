import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useUserStore = defineStore('user', () => {
  const adminInfo = ref(JSON.parse(localStorage.getItem('admin_info') || 'null'))
  const token = ref(localStorage.getItem('admin_token') || '')

  const setAdminInfo = (info) => {
    adminInfo.value = info
    localStorage.setItem('admin_info', JSON.stringify(info))
  }

  const setToken = (t) => {
    token.value = t
    localStorage.setItem('admin_token', t)
  }

  const logout = () => {
    adminInfo.value = null
    token.value = ''
    localStorage.removeItem('admin_info')
    localStorage.removeItem('admin_token')
  }

  return { adminInfo, token, setAdminInfo, setToken, logout }
})
