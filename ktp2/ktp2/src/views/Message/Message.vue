<template>
  <div class="outer">
    <home-nav ref="homeNav" />

    <!-- 消息主体 -->
    <div class="main">
      <h3><i class="el-icon-s-promotion" /> 消息通知</h3>
      <!-- 显示消息 -->
      <div class="message">
        <el-card class="box-card" v-for="(item, i) in messages" :key="item.msgId" :body-style="{padding: '15px'}">
          <div>{{item.msg}}</div>
          <div>from: {{item.from.name}}</div>
          <div v-if="item.status === 1" style="color: #81ecec">已阅读</div>
          <div v-else><span @click="setCss(item, i)" :style="notReadStyle">未阅读</span></div>
        </el-card>
        
      </div>
    </div>
  </div>
</template>

<script>
import HomeNav from 'components/content/HomeNav'
import {request} from '../../network/request'
import qs from 'qs'

export default {
  name: 'Message',
  components: {
    HomeNav
  },
  data() {
    return {
      messages: [],
      notReadStyle: {
        cursor: 'pointer', 
        borderBottom: '1px solid #e74c3c',
        color: '#e74c3c'
        }
    }
  },
  mounted() {
    // 请求数据
    request({
      url: '/api/message/getMessages',
      method: 'get',
      params: {
        to: this.$store.state.uid
      }
    }).then(res => {
      this.messages = [...res.data.filter(e => e.status === 0), ...res.data.filter(e => e.status === 1)]
    }).catch(err => {
      console.log(err)
    })
  },
  methods: {
    setCss(item, index) {
      this.$refs.homeNav.msgMinis()
      item.status = 1
      this.read(item.msgId)
      // 将已阅读的放到下方
      this.messages = [...this.messages.filter(e => e.status === 0),
                        ...this.messages.filter(e => e.status === 1)]
    },
    read(msgId) {
      console.log(JSON.stringify([msgId]))
      request({
        url: '/api/message/read',
        method: 'post',  
        data: [msgId],
      }).then(res => {console.log(res)})
      .catch(err => {console.log(err)})
    }
  }
}
</script>

<style scoped>
  .outer {
    height: 100%;
    position: relative;
  }

  /* 消息主体 */
  .main {
    position: absolute;
    top: 90px;
    text-align: center;
    width: 100%;
    height: calc(100% - 90px);
    z-index: 20;
  }

  .message {
    margin-top: 20px;
    overflow-y: scroll;
    overflow-x: hidden;
    max-height: 90%;
  }
  /* 滑动槽 */
  .message::-webkit-scrollbar {
    width: 6px;
    height: 6px;
  }
  /* 滑动槽背景 */
  .message::-webkit-scrollbar-track {
    border-radius: 3px;
    background: rgba(255, 255, 255, .6);
  }
  /* 滚动条 */
  .message::-webkit-scrollbar-thumb {
    border-radius: 3px;
    background: rgba(126, 214, 223, .6);
    box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.08);
  }

  .box-card {
    width: 90%;
    text-align: start;
    margin: 0 auto;
  }

  .box-card:not(:last-child) {
    margin-bottom: 10px;
  }

  /* 卡片中的尾部 */
  .box-card div:nth-child(2),
  .box-card div:nth-child(3) {
    font-size: 12px;
    text-align: right;
  }
  .box-card div:nth-child(3) {
    user-select: none;
  }
</style>