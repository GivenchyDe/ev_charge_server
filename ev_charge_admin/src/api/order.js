import request from '@/utils/request.js'

export const getOrderList = (page, limit) => request.get('/chargingOrder/list', { params: { page, limit } })
export const getOrderById = (id) => request.get(`/chargingOrder/${id}`)
export const deleteOrder = (id) => request.get('/chargingOrder/delete', { params: { id } })
