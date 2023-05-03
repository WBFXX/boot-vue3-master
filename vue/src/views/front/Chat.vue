<!-- Chat.vue -->
<template>
  <div style="width: 70%;height: 100%;margin: auto;">
    <el-container>
<!--      <el-aside width="250px">-->

<!--        <div>-->
<!--          <el-row style="height:60px;background-color: white">-->
<!--            <div class="myinfo">-->
<!--              <el-avatar src="http://img.52z.com/upload/news/image/20180111/20180111085521_86389.jpg"></el-avatar>-->
<!--              <span>在风中飞翔~</span>-->
<!--            </div>-->

<!--          </el-row>-->
<!--          <div style="display: flex;background-color: white">-->
<!--            <el-row style="height:40px;width: 80%;">-->
<!--              <el-input v-model="keyword"-->
<!--                        :data="tableData.filter(data => !keyword || data.name.toLowerCase().includes(keyword.toLowerCase()))"-->
<!--                        placeholder="搜索好友">-->
<!--                <el-button slot="append">-->

<!--                </el-button>-->
<!--              </el-input>-->

<!--            </el-row>-->
<!--            <el-button style="width: 20%;height: 40px">-->
<!--              <el-icon>-->
<!--                <Search/>-->
<!--              </el-icon>-->
<!--            </el-button>-->

<!--          </div>-->

<!--          <el-row style="height:390px">-->
<!--            <el-table :data="tableData" stripe style="width: 100%;cursor: pointer;" :show-header='false' >-->
<!--              <el-table-column >-->
<!--                <template #default="scope">-->
<!--                  <el-avatar :src="scope.row.img"></el-avatar>-->
<!--                  <span style="position: relative;top: 40%;">{{scope.row.name }}</span>-->
<!--                </template>-->
<!--              </el-table-column>-->
<!--            </el-table>-->


<!--          </el-row>-->
<!--        </div>-->

<!--      </el-aside>-->
      <el-container>
        <el-header style="border-radius: 10px 10px 0 0">
          <div style="top: 20px;color: white;font-size: 16px ;outline-width: 10px">
            考研交流室
          </div>

        </el-header>
          <div ref="divRef" class="el-main1" style="overflow-y: scroll;">
            <div  v-for="item in messages" :key="item.id">
              <div style="display: flex; margin: 20px 0;" v-if="user.uid !== item.uid">
                <el-popover
                    placement="top-start"
                    :width="100"
                    trigger="hover"
                >
                  <template #reference>
                    <img :src="item.avatar" alt="" style="width: 30px; height: 30px; border-radius: 50%; margin-left: 10px">
                  </template>
                  <div style="line-height: 20px;">
                    <div style="font-size: 16px">{{ item.username }}</div>
                    <div style="font-size: 12px">{{ item.sign }}</div>
                  </div>
                </el-popover>
                <!--          <div style="width: 50px; line-height: 30px; margin-left: 5px; color: #888; overflow: hidden; font-size: 14px">{{ item.username }}</div>-->
                <div style="line-height: 30px; background-color: white; padding: 0 10px;margin-left: 10px;
                 width:fit-content; border-radius: 10px">{{ item.text }}</div>
              </div>

              <div style="display: flex; justify-content: flex-end; margin: 20px 0 20px 0;" v-else>
                <div style="line-height: 30px; background-color: #80B9F2; padding: 0 10px;
                margin-right: 10px;
                width:fit-content; border-radius: 10px;">{{ item.text }}</div>

                <el-popover
                    placement="top-start"
                    :width="100"
                    trigger="hover"
                >
                  <template #reference>
                    <img :src="item.avatar" alt="" style="width: 30px; height: 30px; border-radius: 50%; margin-right: 10px">
                  </template>
                  <div style="line-height: 20px">
                    <div style="font-size: 16px">{{ item.username }}</div>
                    <div style="font-size: 12px;">{{ item.sign }}</div>
                  </div>
                </el-popover>
              </div>
            </div>
          </div>

        <el-footer style="border-radius:  0 0 10px 10px;border-top: 1px solid darkgray">
          <div style="display: flex;margin-left: 20px;top: 10px;width: 95%;height: 80px;">
            <V3Emoji default-select="recent" :recent="true" :options-name="optionsName" :keep="true"  :textArea="true" size="mid" v-model="text" />
            <div style="text-align: right;margin-left: 20px;top: 45px;border-radius: 10px">
              <el-button @click="send" type="primary">
                发送
              </el-button>
            </div>
          </div>
        </el-footer>
      </el-container>
    </el-container>
  </div>
</template>


<script setup>
import {nextTick, onMounted, reactive, ref} from "vue";
import V3Emoji from 'vue3-emoji'
import 'vue3-emoji/dist/style.css'
import {useUserStore} from "@/stores/user";
import request from "@/utils/request";
import { ElRow, ElInput, ElButton } from 'element-plus'


const messages = ref([])

const userStore = useUserStore()
const user = userStore.getUser

const text = ref('')  // 聊天输入的内容
const divRef = ref()   // 聊天框的引用

// 页面滚动到最新位置的函数
const scrollBottom = () => {
  nextTick(() => {   // 等到页面元素出来之后再去滚动
    divRef.value.scrollTop = divRef.value.scrollHeight
  })
}


// 页面加载完成触发此函数
onMounted(() => {
  request.get("/im/init/10").then(res => {
    messages.value = res.data

    scrollBottom()
  })
})

const client = new WebSocket(`ws://localhost:9090/imserver/${user.uid}`)
// 发送消息触发滚动条滚动
const send = () => {
  if (client) {
    client.send(text.value)
  }
  text.value = ''  // 清空文本框
}

const optionsName = {
  'Smileys & Emotion': '笑脸&表情',
  'Food & Drink': '食物&饮料',
  'Animals & Nature': '动物&自然',
  'Travel & Places': '旅行&地点',
  'People & Body': '人物&身体',
  Objects: '物品',
  Symbols: '符号',
  Flags: '旗帜',
  Activities: '活动'
}

client.onopen = () => {
  console.log('open')
}
client.onclose = () => {  // 页面刷新的时候和后台websocket服务关闭的时候
  console.log('close')
}
client.onmessage = (msg) => {
  if (msg.data) {
    let json = JSON.parse(msg.data)
    if (json.uid && json.text) {  // 聊天消息
      messages.value.push(json)
      scrollBottom()  // 滚动页面到最底部
    }
  }
}

// const tableData =  [
//   {
//     name: '王小虎',
//     img: 'https://p0.ssl.qhimgs1.com/sdr/400__/t0197ad9742a3e63410.jpg'
//   }, {
//     name: '郑泷',
//     img: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
//   }, {
//     name: '小蛮',
//     img: 'http://gss0.baidu.com/-fo3dSag_xI4khGko9WTAnF6hhy/zhidao/pic/item/30adcbef76094b36ba49777aa5cc7cd98c109d49.jpg'
//   }, {
//     name: '张云',
//     img: 'http://img.52z.com/upload/news/image/20180111/20180111085521_86389.jpg'
//   }
// ]
//
// const keyword =  ''
//
// const state = reactive({
//   tableData: [
//       {
//     name: '王小虎',
//     img: 'https://p0.ssl.qhimgs1.com/sdr/400__/t0197ad9742a3e63410.jpg'
//   }, {
//     name: '郑泷',
//     img: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
//   }, {
//     name: '小蛮',
//     img: 'http://gss0.baidu.com/-fo3dSag_xI4khGko9WTAnF6hhy/zhidao/pic/item/30adcbef76094b36ba49777aa5cc7cd98c109d49.jpg'
//   }, {
//     name: '张云',
//     img: 'http://img.52z.com/upload/news/image/20180111/20180111085521_86389.jpg'
//   }
// ],
//       info: {
//         /* 记录登录状态 */
//         isLogin:false,
//         /* 我的信息 */
//         myInfo:{
//           img:'',
//           name:'',
//         },
//         /* 别人的信息（特指聊天对象） */
//         userInfo:{
//           img:'',
//           name:'',
//         },
//         /* 用户列表 */
//         userList:[],
//         /* 聊天记录 */
//         chatMessageList:[],
//       },
//       mutations: {},
//       actions: {},
//       modules: {}
// }
// )




</script>

<style scoped>

.el-container {
  height: 100%;
}
/*考研聊天室*/
.el-header{
  background-color: #409EFF;
  color: #333;
  text-align: center;
  height: 60px;
  border-bottom: 1px solid darkgrey;
}
/*输入框*/
.el-footer {
  background-color: #f4f5f7;
  color: #333;
  text-align: center;
  height: 120px;
}

.el-aside {
  background-color: #d3dce6;
  color: #333;
  text-align: center;
  height: 600px;
}
/*主聊天框*/
.el-main1 {
  background-color: #f4f5f7;
  color: #333;
  text-align: center;
  height: 420px;

}

.el-menu {
  background-color: #d3dce6;
}

.myinfo{

    text-align: left;
    vertical-align: middle;
    margin-top: 10px;
    margin-left: 10px;
  }
.myinfo span{
  text-align: left;
  vertical-align: middle;
}

</style>
