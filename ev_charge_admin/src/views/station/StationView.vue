<script setup>
import { ref, onMounted } from 'vue'
import { getStationList, addStation, updateStation, deleteStation } from '@/api/station.js'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const list = ref([])
const total = ref(0)
const page = ref(1)
const limit = ref(10)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const form = ref({})
const formRef = ref(null)

const rules = {
  name: [{ required: true, message: '请输入网点名称', trigger: 'blur' }],
  address: [{ required: true, message: '请输入地址', trigger: 'blur' }],
}

const fetchList = async () => {
  loading.value = true
  try {
    const res = await getStationList(page.value, limit.value)
    list.value = res.data?.records || []
    total.value = res.data?.total || 0
  } finally { loading.value = false }
}

const handleAdd = () => {
  dialogTitle.value = '新增网点'
  form.value = { status: 1 }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑网点'
  form.value = { ...row }
  dialogVisible.value = true
}

const handleSubmit = async () => {
  await formRef.value.validate()
  const api = form.value.id ? updateStation : addStation
  const res = await api(form.value)
  if (res.success) {
    ElMessage.success(dialogTitle.value + '成功')
    dialogVisible.value = false
    fetchList()
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定删除该网点吗？', '提示', { type: 'warning' }).then(async () => {
    const res = await deleteStation(row.id)
    if (res.success) { ElMessage.success('删除成功'); fetchList() }
  })
}

onMounted(fetchList)
</script>

<template>
  <div class="page-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>网点管理</span>
          <el-button type="primary" @click="handleAdd">新增网点</el-button>
        </div>
      </template>
      <el-table :data="list" v-loading="loading" border stripe>
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="name" label="网点名称" width="150" />
        <el-table-column prop="address" label="地址" show-overflow-tooltip />
        <el-table-column prop="phone" label="电话" width="130" />
        <el-table-column prop="status" label="状态" width="90">
          <template #default="{ row }">
            <el-tag :type="['warning','success','info'][row.status] || 'info'" size="small">
              {{ ['维护中','正常运营','已停用'][row.status] || '未知' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination class="pagination" v-model:current-page="page" v-model:page-size="limit" :total="total" layout="total, prev, pager, next" @current-change="fetchList" />
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="名称" prop="name"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="地址" prop="address"><el-input v-model="form.address" /></el-form-item>
        <el-form-item label="经度"><el-input v-model="form.longitude" /></el-form-item>
        <el-form-item label="纬度"><el-input v-model="form.latitude" /></el-form-item>
        <el-form-item label="电话"><el-input v-model="form.phone" /></el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status"><el-option label="维护中" :value="0" /><el-option label="正常运营" :value="1" /><el-option label="已停用" :value="2" /></el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.page-container { padding: 0; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
.pagination { margin-top: 16px; justify-content: flex-end; }
</style>
