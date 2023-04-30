<script setup>
import {nextTick, onMounted, reactive, ref} from "vue"
import {User, Lock} from '@element-plus/icons-vue'
import request from "@/utils/request";
import {ElMessage} from "element-plus";
import {useUserStore} from "@/stores/user";
import router, {setRoutes} from "@/router";
import SIdentify from '../components/Sidentify.vue';

// 图形验证码
let identifyCodes = "1234567890"
let identifyCode = ref('')
const loginData = reactive({})
const ruleFormRef = ref()
const passwordVis = ref(false)
const passwordForm = reactive({})
const rulePasswordFormRef = ref()
const interval = ref(-1)
const time = ref(0);

const failCount = ref(0)
const randomNum = (min, max) => {
  return Math.floor(Math.random() * (max - min) + min)
}
const makeCode = (o, l) => {
  for (let i = 0; i < l; i++) {
    identifyCode.value += o[randomNum(0, o.length)];
  }
}
const refreshCode = () => {
  identifyCode.value = "";
  makeCode(identifyCodes, 4);
}
// 生成验证码
onMounted(() => {
  identifyCode.value = "";
  makeCode(identifyCodes, 4);
})




const rules = reactive({
  username: [
    {required: true, message: '请输入账号', trigger: 'blur'},
  ],
  password: [
    {required: true, message: '请输入密码', trigger: 'blur'},
    {min: 3, max: 20, message: '密码长度在3-20位之间', trigger: 'blur'},
  ],
})

const passwordRules = reactive({
  email: [
    {required: true, message: '请输入邮箱', trigger: 'blur'},
  ],
  emailCode: [
    {required: true, message: '请输入验证码', trigger: 'blur'},
  ],
})

const loginFront = () => {
  ruleFormRef.value.validate(valid => {
    if (valid) {
      // 失败3次触发验证码
      if (failCount.value >= 3 && loginData.code !== identifyCode.value) {
        ElMessage.warning('验证码错误')
        return
      }
      // 发送表单数据给后台
      request.post('/login', loginData).then(res => {
        if (res.code === '200') {

            router.push('/front') // 前台的首页

          ElMessage.success('登录成功')
          const userStore = useUserStore()
          userStore.setManagerInfo(res.data)
        } else {
          ElMessage.error(res.msg)
          failCount.value ++  // 失败次数加1
        }
      })
    }
  })
}
const login = () => {
  ruleFormRef.value.validate(valid => {
    if (valid) {
      // 失败3次触发验证码
      if (failCount.value >= 3 && loginData.code !== identifyCode.value) {
        ElMessage.warning('验证码错误')
        return
      }
      // 发送表单数据给后台
      request.post('/login', loginData).then(res => {
        if (res.code === '200') {

          router.push('/home') // 后台的首页

          ElMessage.success('登录成功')
          const userStore = useUserStore()
          userStore.setManagerInfo(res.data)
        } else {
          ElMessage.error(res.msg)
          failCount.value ++  // 失败次数加1
        }
      })
    }
  })
}

//点击忘记密码触发，羽雀有记录；
const handleResetPassword = () => {
  passwordVis.value = true
  //触发表单重置,时钟处理
  nextTick(()=>{//下一个时钟
    rulePasswordFormRef.value.resetFields()
  })
}
//发送邮箱验证
const reg = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
const sendEmail = () => {
  if (!reg.test(passwordForm.email)) {
    ElMessage.error("请输入合法的邮箱！")
    return
  }
//倒计时方法
  const times = () => {
    //清空定时器
    if (interval.value > 0) {
      clearInterval(interval.value)
    }
    time.value = 60 //倒计时
    interval.value = setInterval(() => {
      if (time.value > 0) {
        time.value--;
      }
    }, 1000)
  }

  request.get("/email", {
    params: {
      email: passwordForm.email,
      type: 'RESETPASSWORD'
    }
  }).then(res => {
    if (res.code === '200') {
      times();//倒计时
      ElMessage.success('发送成功，有效期5分钟')
    } else {
      ElMessage.error(res.msg)
    }
  })
}
// 调用新接口重置密码
const resetPassword = () => {
  rulePasswordFormRef.value.validate(valid => {
        if (valid) {
          request.post("/password/reset", passwordForm
          ).then(res => {
                console.log(res)
                if (res.code === '200') {
                  ElMessage.success('重置成功，您的密码为:' + res.data)
                  passwordVis.value = false
                } else {
                  ElMessage.error(res.msg)
                }
              }
          )
        }

      }
  )
}
</script>

<template>
  <div style="height: 100vh; overflow: hidden; background-color: aliceblue">
    <div style="width: 100%; background-color: rgba(65, 105, 225,.1);padding: 15px 30px; color: dodgerblue; font-size: 20px; position: absolute">考研交流互助平台</div>
    <div style="display: flex; width: 50%; margin: 150px auto; background-color: white;
      box-shadow: 0 0 10px -2px rgba(30, 144, 255,.5); overflow: hidden; border-radius: 10px">
      <div style="padding:30px">
        <img src="../assets/bg2.png" alt="" style="width: 100%; margin-top: 50px">
      </div>
      <div>
        <div style="width: 400px; background-color: white; padding: 30px 40px;">
          <el-form
              ref="ruleFormRef"
              :model="loginData"
              :rules="rules"
              size="large"
              status-icon
          >
            <div style="text-align: center; color: dodgerblue; font-size: 30px; font-weight: bold; margin-bottom: 30px">
              登 录
            </div>
            <el-form-item prop="username">
              <el-input v-model="loginData.username" placeholder="请输入账号" :prefix-icon="User"/>
            </el-form-item>
            <el-form-item prop="password">
              <el-input v-model="loginData.password" show-password placeholder="请输入密码" :prefix-icon="Lock"/>
            </el-form-item>
            <div style="display: flex; margin: 15px 0" v-if="failCount >= 3">
              <div style="flex: 1">
                <el-input v-model="loginData.code" placeholder="验证码"></el-input>
              </div>
              <div>
                <div @click="refreshCode" style="margin-left: 5px">
                  <SIdentify :identifyCode="identifyCode" />
                </div>
              </div>
            </div>
            <el-form-item>
              <el-button type="primary" style="width: 100%;" @click="loginFront">平台首页登录</el-button>
            </el-form-item>
            <el-form-item>
              <el-button type="success" style="width: 100%" @click="login">后台管理登录</el-button>
            </el-form-item>
          </el-form>
          <div style="text-align: right;width: 100%">
            <el-button style="float: left;font-size: 15px;top: 3px" type="primary" link @click="handleResetPassword" >
              忘记密码?
            </el-button>
            <a style="text-decoration: none; color: dodgerblue"
                                            href="/register">还没有账号？去注册</a>

          </div>



        </div>
        <el-dialog v-model="passwordVis" title="忘记密码？" :close-on-click-modal="false" style="width: 500px ;padding: 0 20px">
          <el-form :model="passwordForm" ref="rulePasswordFormRef" :rules="passwordRules" status-icon label-width="100px">
            <el-form-item label="邮箱" prop="email" style="display: flex;width: 92%">
              <el-input v-model="passwordForm.email" autocomplete="off"/>
            </el-form-item>
            <el-form-item label="验证码" prop="emailCode" >
              <div style="display: flex;width: 90%">
                <el-input style="flex: 1" v-model="passwordForm.emailCode" clearable></el-input>
                <el-button style="width: 100px;margin-left:5px" @click="sendEmail" :disabled="time">点击发送<span
                    v-if="time">({{ time }})</span></el-button>
              </div>
            </el-form-item>
          </el-form>
          <template #footer>
      <span class="dialog-footer">
        <el-button @click="passwordVis = false">取消</el-button>
        <el-button type="primary" @click="resetPassword">
          确认
        </el-button>
      </span>
          </template>
        </el-dialog>

      </div>
    </div>

  </div>
</template>
