<template>
  <div >
<!--    <div style="display: flex;-->
<!--  align-items: center;-->
<!--  justify-content: left;-->
<!-- ">-->
<!--      <el-button  class="button" @click="tab = 'follow'">关注</el-button>-->
<!--      <el-button  class="button"  @click="tab = 'fans'">粉丝</el-button>-->
<!--    </div>-->
<!--&lt;!&ndash;    我关注的&ndash;&gt;-->
<!--    <div class="list" v-if="tab === 'follow'" v-for="item in state.followerOptions" :key="item.id">-->
<!--      <div class="aa">-->
<!--        <span>{{ item.id }}</span>-->
<!--        <span>{{item.followerId}}</span>-->
<!--      </div>-->
<!--    </div>-->
<!--&lt;!&ndash;    粉丝：关注我的&ndash;&gt;-->
<!--    <div class="list" v-else>-->
<!--      <div class="bb"  v-for="item in state.followerOptions" :key="item.id">-->
<!--        <span>{{ item.id }}</span>-->
<!--        <span>{{item.userId}}</span>-->
<!--      </div>-->
<!--    </div>-->
<!--  </div>-->
      <div>
        <el-table  :data="state.tableData" style="width: 100%;margin-top: 20px">
            <el-table-column prop="time" label="关注时间" width="250">
              <template #default="scope">
                <div style="display: flex; align-items: center">
                  <el-icon><timer /></el-icon>
                  <span style="margin-left: 10px">{{scope.row.createTime}}</span>
                </div>
              </template>
            </el-table-column>

          <el-table-column label="头像" width="300">
            <template #default="scope">
              <el-popover effect="light" trigger="hover" placement="top" width="auto">
                <template #default>
                  <div>关注: {{scope.row.followCount}}</div>
                  <div>粉丝:{{scope.row.fansCount?scope.row.fansCount : 0}} </div>
<!--                  <div>他的特别关注:{{scope.row.fansCount?scope.row.fansCount : 0}} </div>-->
                </template>
                <template #reference>
                  <el-avatar :size="40" :src="scope.row.user.avatar" style="margin-top: 10px;cursor: pointer"
                             @click="router.push('/front/info?id=' + scope.row.followerId)" />
                </template>
              </el-popover>
            </template>
          </el-table-column>

            <el-table-column label="名称" width="300">
              <template #default="scope">
                <el-popover effect="light" trigger="hover"  popover: disabled  width="auto">

                  <template #reference>
                            <!-- 名称在这里-->
                    <el-tag>{{ scope.row.user.name ? scope.row.user.name:'' }}</el-tag>
                  </template>
                </el-popover>
              </template>
            </el-table-column>

            <el-table-column label="操作">

              <template #default="scope" style="display: flex">
                <el-popconfirm title="您确定取消关注吗？" @confirm="del(scope.row.id)" v-if="auths.includes('follower.delete')">
                  <template #reference>
                    <el-button type="danger">取消关注</el-button>
                  </template>
                </el-popconfirm>
              </template>
            </el-table-column>

          <el-table-column #default="scope" label="私信">

                  <el-button type="primary" @click="router.push('/front/pm?fid=' + scope.row.followerId)" >私信</el-button>

          </el-table-column>
          </el-table>
      </div>
    <div style="margin: 10px 0">
      <el-pagination
          @current-change="load"
          @size-change="load"
          v-model:current-page="pageNum"
          v-model:page-size="pageSize"
          background
          :page-sizes="[2, 5, 10, 20]"
          layout="total ,sizes, prev, pager, next, jumper"
          :total="total"
      />
    </div>




  </div>



</template>

<script setup >
import {reactive, ref} from 'vue'
import request from "@/utils/request";
import {ElButton, ElMessage} from "element-plus";
import {useUserStore} from "@/stores/user";
import router from "@/router";

const userStore = useUserStore()
const auths =  userStore.getAuths
const name = ref('')
const pageNum = ref(1)
const pageSize = ref(5)
const total = ref(0)
const token = userStore.getBearerToken
const state = reactive({
  tableData: [],
  form: {},
  data:{}
})

const load = () => {
  request.get('/follower/followPage', {params: {
      name: name.value,
      type: 'user',
      pageNum: pageNum.value,
      pageSize: pageSize.value
    }
  }).then(res => {

    state.tableData = res.data.records
    console.log(state.tableData)
    total.value = res.data.total
  })
}
load()  // 调用 load方法拿到后台数据






// //获取用户列表
// state.userOptions = []
// request.get('/user').then(res => {
//   state.userOptions = res.data
// })

// state.followerOptions = []

// 获取关注列表：包括关注数量，粉丝数量，用户信息
// const followerOptions = () => {
//   //这里假设你有一个后端接口可以返回关注列表数据，你需要根据你的实际情况修改
//   request.get('/follower/count').then(res => {
//     if (res.code === '200') {
//       console.log(res)
//       state.followerOptions = res.data
//       ElMessage.success('获取关注列表成功');
//     } else {
//       ElMessage.error('获取关注列表失败');
//     }
//   })
// }
// followerOptions()


const del = (id) => {
  request.delete('/follower/' + id).then(res => {
    if (res.code === '200') {
      ElMessage.success('取消关注成功')
      load()  // 刷新表格数据
    } else {
      ElMessage.error(res.msg)
    }
  })
}



</script>

<style scoped>


</style>
