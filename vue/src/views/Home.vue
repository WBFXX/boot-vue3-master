<script setup>
import {reactive} from "vue";
import request from "@/utils/request";

const state = reactive({
  notice: []
})

const load = () => {
  request.get("/notice").then(res => {
    state.notice = res.data
  })
}
load()
</script>

<template>
  <div>
    <el-row :gutter="10">
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div>
              <span>考研交流互助平台</span>
            </div>
          </template>
          <div>
            <div style="margin: 10px 0">
              近年来，考研报名人数呈现明显的增长趋势，而考研备考本身是一项高强度、高难度的任务，需要考生投入大量时间和精力。
              同时，考研人群对各种考研资讯和学习资料的需求也越来越大。很多考生在备考过程中会遇到各种问题，如备考方法不当、学习计划安排不合理、
              考研心态失衡，这些问题往往需要经验丰富的老考生或者专业的导师提供指导和帮助。由于考研基数越来越大，人们便以信息技术为基础
              ，开发出了学习交流平台来服务有需要的人群。


            </div>
            <div style="margin: 10px 0">
              然而，随着学生选择考研趋势的逐步发展，原有学习交流平台在对考研人群的针对性、实时性、并发和效率上的弊端越来越凸现出来。
              用户对信息访问便捷性和速度要求的不断提高，信息量的不断增大，使得人们对考研交流互助平台的要求也越来越高。
              因此，设计并实现一套先进、可靠、易用的考研交流互助平台非常必要，它将帮助人们更加便捷地获取各种考研信息，并提供交流互动的功能。
            </div>

            <div  style="margin: 10px 0">
              <a href="http://localhost:7000/front/home">平台首页地址</a>
            </div>
            <div style="font-weight: bold">当前最新版本:  <b style="color: dodgerblue">v2.0</b></div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div>
              <span>技术栈</span>
            </div>
          </template>
          <div style="display: flex">
            <div style="flex: 1; padding: 0 20px; line-height: 30px">
              <h3 style="font-weight: bold">后端</h3>
              <div>SpringBoot2</div>
              <div>Mybatis-Plus</div>
              <div>Sa-Token</div>
              <div>JWT</div>
              <div>Mysql</div>
              <div>...</div>
            </div>
            <div style="flex: 1; padding: 0 20px;  line-height: 30px">
              <h3 style="font-weight: bold">前端</h3>
              <div>Vue3</div>
              <div>Pinia</div>
              <div>Element-Plus</div>
              <div>Axios</div>
              <div>WangEditor</div>
              <div>...</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-divider />

    <div>
      <el-card style="width: 50%;  margin: 10px 0">
        <template #header>
          <div>
            <span>公告列表</span>
          </div>
        </template>
        <el-collapse accordion>
          <el-collapse-item v-for="(item,index) in state.notice" :key="item.id" :name="'' + index">
            <template #title>
              <span style="font-size: 20px;">{{ item.name }}</span>
              <span style="margin-left: 10px">{{ item.createTime }}</span>
            </template>
            <div v-html="item.content"></div>
          </el-collapse-item>
        </el-collapse>
      </el-card>
    </div>
  </div>
</template>
