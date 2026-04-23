import request from '@/utils/request.js'

export const getUserList = (page, limit) => {
  return request.get('/user/list', { params: { page, limit } })
}

export const getUserById = (id) => {
  return request.get(`/user/${id}`)
}

export const updateUser = (data) => {
  return request.post('/user/update', data)
}

export const updateUserStatus = (id, status) => {
  return request.get('/user/updateStatus', { params: { id, status } })
}

export const deleteUser = (id) => {
  return request.get('/user/delete', { params: { id } })
}
