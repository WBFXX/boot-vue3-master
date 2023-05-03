<template>
  <div class="common-layout">
    <el-container>
      <el-header style="display: flex">
        <div style="width: 10%">
        <div v-for="(item,index) in state.dynamic">
          <el-avatar v-if="index===0"
            style="height: 80px;width: 80px;top: 20px"
            :src="item.user.avatar"
        />
        </div>

        <div>

        </div>
        </div>
        <div v-for="(item,index) in state.dynamic" style="width: 40%;top: 20px">
          <div v-if="index===0" style="color: #222226;
    font-size: 22px;
    font-weight: 500;
    line-height: 24px;">{{state.fuser.name}}</div>
          <div style="line-height: 40px;font-size: 14px;
    margin-bottom: 10px;
    color: #555666;
    text-align: justify;
    word-break: break-all;" v-if="state.fellow.name && index===0">代表动态:《{{state.fellow.name}}》</div>
          <div style="font-size: 14px;
    margin-bottom: 10px;
    color: #555666;
    text-align: justify;
    line-height: 22px;
    word-break: break-all;" v-if="index===0">地址:{{state.fuser.address}}</div>
        </div>
        <div style="position: absolute;right: 20px;top: 20px">
          <div style="padding-bottom: 15px ; margin-bottom: 20px">
            <span style="float: right">
              <input type="checkbox" v-model="state.fellow" hidden>
              <button @click.stop="followActive" class="my_button"
                      :class="{liked: state.fellow.liked === null ? state.fellow.liked : 'false'}">
                {{state.fellow.liked ? "已关注" : "+ 关注"}}
              </button>
            </span>
          </div>
        </div>


      </el-header>
      <el-container>
        <el-aside>

          <div style="width: 200px; border-right: 1px solid #ccc">

                <el-menu
                    default-active="2"
                    class="el-menu-vertical-demo">
<!--                    @open="handleOpen"-->
<!--                    @close="handleClose"-->


                  <el-menu-item index="2">
                    <el-icon><Menu /></el-icon>
                    <span style="font-size: 16px;margin: auto">他的动态</span>
                  </el-menu-item>
                </el-menu>

          </div>


        </el-aside>
        <el-main>
          <div style="width: 100%; color: #666">
            <el-card>
              <div v-for="item in state.dynamic" :key="item.id" style="border-bottom: 1px solid #ddd; padding: 20px; cursor: pointer"
                   @click="router.push('/front/detail?id=' + item.id)">

                <div style="display: flex">
                  <div style="width: 50%; display: flex">
                    <img v-if="item.user" :src="item.user.avatar" alt="" style="width: 40px; height: 40px; border-radius: 50%">
                    <div style="flex: 1; line-height: 40px; margin-left: 10px; " v-if="item.user" >
                      <span style="font-weight: bold; font-size: 16px">{{ item.user.name }}</span>
                      <span style="margin-left: 10px; font-size: 12px; color: #888">{{ item.time }}</span>
                    </div>
                  </div>





                  <div style="flex: 1;">
                    <!--                <span class="icon" v-if="state.dynamic.liked" @click="followActive">-->
                    <!--                  <button @click.stop="followActive" class="my_button" :class="{liked: item.liked}">-->
                    <!--                    {{ content }}-->
                    <!--                  </button>-->
                    <!--                     <span style="margin-left: 5px; color: red">{{ state.dynamic.praiseCount }}</span>-->
                    <!--                 </span>-->
                    <!--                <span class="icon" v-else @click="praise">-->
                    <!--                     <el-icon style="top: 5px; margin-left: 10px"><img src="../../assets/点赞.svg" alt="" style="width: 20px;"></el-icon>-->
                    <!--                     <span style="margin-left: 5px; color: #888">{{ state.dynamic.praiseCount }}</span>-->
                    <!--                </span>-->


                  </div>
                </div>

                <div style="margin: 10px 0">
                  <div style="color: #000; font-size: 18px; font-weight: bold">{{ item.name }}</div>
                </div>

                <div style="margin: 10px 0">
                  <div style="color: #666; font-size: 14px">{{ item.descr }}</div>
                </div>

                <div style="margin: 10px 0">
                  <div><img :src="item.img " alt="" style="width: 40%; border-radius: 10px"></div>
                </div>

                <div style="display: flex">
                  <div style="width: 50%">
                    <el-tag style="margin-right: 10px" v-for="tag in item.tags" :key="tag"># {{ tag }} </el-tag>
                  </div>
                  <div style="width: 50%; text-align: right; font-size: 16px; color: #888">
                  <span>
                  <el-icon :size="20" style="top: 4px"><View /></el-icon>
                  <span style="margin-left: 3px">{{ item.view }}</span>
                </span>
                    <span style="margin-left: 10px">
                  <el-icon :size="20" style="top: 4px"><Star /></el-icon>
                  <span style="margin-left: 3px">{{ item.collectCount }}</span>
                </span>
                    <span style="margin-left: 10px">
                  <el-icon :size="20" style="top: 4px"><ChatLineSquare /></el-icon>
                  <span style="margin-left: 3px">{{ item.commentCount }}</span>
                </span>

                    <span style="margin-left: 10px" >
                  <el-icon  :size="20" style="top: 4px" ><img style="width: 20px" src="@/assets/点赞.svg" alt=""></el-icon>
                  <span style="margin-left: 3px">{{ item.praiseCount }}</span>
                </span>
                  </div>
                </div>

              </div>
            </el-card>

            <el-card style="margin: 10px 0; background-color: white">
              <el-pagination
                  @current-change="load"
                  @size-change="load"
                  v-model:current-page="pageNum"
                  v-model:page-size="pageSize"
                  background
                  layout="total, prev, pager, next"
                  :total="total"
              />
            </el-card>
          </div>

        </el-main>


      </el-container>

    </el-container>
  </div>


</template>


<script setup>
import {nextTick, onMounted, reactive, ref} from "vue";
// import { Menue } from '@element-plus/icons'
const name = ref('')
const state = reactive({
  fuser:{},
  dynamics: [],
  fellow: {},
  dynamic: {}
})

const pageNum = ref(1)
const pageSize = ref(5)
const total = ref(0)
import {useUserStore} from "@/stores/user";
const userStore = useUserStore()
const user = userStore.getUser
const id = user.id

const iid = router.currentRoute.value.query.id
const load = () => {
  request.get('/dynamic/fel/' + iid).then(res => {
    // 在fellow里加个liked字段吧
    nextTick(() => {
      state.fellow = res.data

    })
  })

  request.get('/user/' + iid).then(res =>{

    state.fuser = res.data
  })

  request.get('/dynamic/page', {
    params: {
      name: name.value,
      pageNum: pageNum.value,
      pageSize: pageSize.value
    }
  }).then(res => {
    state.dynamics = res.data.records
    total.value = res.data.total
  })

  request.get('/dynamic/info/' + iid).then(res => {
    // 在fellow里加个liked字段吧
    state.dynamic = res.data
    console.log(state.dynamic)
  })


}

load()  // 调用 load方法拿到后台数据
const followActive = () => {
  request.post('/follower',{followerId: iid}).then(res => {
    if (res.code === '200'){
      ElMessage.success('操作成功')
      load()
    }else {
      ElMessage.error(res.msg)
    }

  })
}

onMounted(() => {
  load()
})

import router from "@/router";
import request from "@/utils/request";
import {ElMessage} from "element-plus";

</script>

<style scoped>

.el-aside {
  background-color: aliceblue;
  color: #333;
  text-align: center;
  width: 200px;
  /*height: 420px;*/
}

.el-header{
  background-color: aliceblue;
  color: #333;
  border-radius: 5px 5px 0 0;
  height: 150px;
  border-bottom: 1px solid darkgrey;
}

/*主聊天框*/
.el-main {
  background-color: #f4f5f7;
  color: #333;
  /*height: 420px;*/

}
.refresh:hover {
  cursor: pointer;
}
:deep(.el-card__body) {
  padding: 10px !important;
}

.my_button {
  color: #f56c6c;
  background: #fef0f0;
  border: #fbc4c4 solid;
  border-radius: 20px;
  padding: 12px 23px;
  text-align: center;
  font-size: 16px;
  -webkit-transform: scale(0.7);
  cursor: pointer;
}

.my_button:hover {
  background: #ff9999;
}

.my_button.liked {
  color: #fef0f0;
  background: #f56c6c;
}

</style>
