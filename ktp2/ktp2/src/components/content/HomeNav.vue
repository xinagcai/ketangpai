<template>
  <navigation class="homenav">
    <template v-slot:left><i class="lstyle" @click="gotoHome"></i></template>
    <template v-slot:right>
      <div class="rstyle">
        <div>
          <el-badge :value="msgNum" :hidden="hiddenOrNot" class="item">
            <i style="font-size: 25px" class="el-icon-bell" @click="gotoMessage" />
          </el-badge>
        </div>
        <div>
          <el-popover
            placement="bottom"
            trigger="click"
            content="这是一段内容">
            <!-- 弹出框 -->
            <div class="nav-btn">
              <div @click="gotoProfile"><i class="setting" />个人设置</div>
              <div @click="logout"><i class="exit" />退出账户</div>
            </div>
            <!-- 点击效果 -->
            <div class="avatar" slot="reference"><img :src="avatar" /></div>
          </el-popover>
        </div>
      </div>
    </template>
  </navigation>
    
    
</template>

<script>
  import Navigation from 'components/common/Navigation'
  import {request} from "../../network/request"
  import {IS_LOGIN, CHANGE_UID} from "../../store/mutation-type"

  export default {
    name: 'HomeNav',
    components: {
      Navigation
    },
    data() {
      return {
        avatar: '',
        msgNum: 0,
        hiddenOrNot: false
      }
    },
    mounted() {
      // 加载头像
      this.avatar = require('../../assets/avatar/' + this.$store.state.avatar)

      // 查询消息数量
      request({
        url: '/api/message/getNotReadCount',
        method: 'get',
        params: {
          to: this.$store.state.uid
        }
      }).then(res => {
        this.msgNum = res.data
        this.hiddenOrNot = this.msgNum === 0
      }).catch(err => {
        console.log(err)
      })
    },
    methods: {
      logout() {
        if (this.$route.path === '/home') {
          this.$emit('clear-style')
        }

        this.$store.commit(CHANGE_UID, '')
        this.$store.commit(IS_LOGIN, '')

        // 删除cookie
        this.$cookies.remove("user")

        this.$router.push('/')
        
      },
      msgMinis() {
        this.msgNum--
        if (this.msgNum === 0) {
          this.hiddenOrNot = true
        } else {
          this.hiddenOrNot = false
        }
      },
      gotoMessage() {
        if (this.$route.path !== '/message') {
          if (this.$route.path === '/home') {
            this.$emit('clear-style')
          }

          this.$router.push('/message')
        }
      },
      gotoProfile() {
        if (this.$route.path !== '/profile') {
          if (this.$route.path === '/home') {
            this.$emit('clear-style')
          }

          this.$router.push('/profile')
        }
      },
      gotoHome() {
        if (this.$route.path !== '/home') {
          this.$router.push('/home')
        }
      }
    }
  }
</script>

<style scoped>
  .homenav {
    box-shadow: 0 0 10px #ccc;
    background: #fff;
    position: fixed;
    z-index: 50;
  }

  .avatar>img {
    width: 30px;
    height: 30px;
    background-repeat: no-repeat;
    background-position: center;
    border-radius: 50%;
  }

  .lstyle {
    display: inline-block;
    vertical-align: middle;
    background: url('../../assets/img/logo.png') no-repeat 0/96px;
    cursor: pointer;
    width: 96px;
    height: 24px;
  }

  .rstyle {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    width: 85px;
  }

  .avatar {
    border: 0;
    border-radius: 50%;
    width: 30px;
    height: 30px;
    /* background: #000; */
    cursor: pointer;
  }

  .el-icon-bell {
    cursor: pointer;
  }

  /* 弹出框 */
  .nav-btn>div {
    cursor: pointer;
    width: 100%;
    padding-left: 10px;
    text-align: left;
    user-select: none;
    font-size: 14px;
    height: 40px;
    line-height: 40px;
  }
  .nav-btn>div:hover {
    background: #f2f2f2;
  }
  .setting {
    display: inline-block;
    vertical-align: middle;
    background: url('../../assets/img/Setting.png') no-repeat 0/14px;
    width: 30px;
    height: 40px;
  }
  .exit {
    display: inline-block;
    vertical-align: middle;
    background: url('../../assets/img/exit.png') no-repeat 0/14px;
    width: 30px;
    height: 40px;
  }

  /* 消息提示 */
  .item {
    margin-top: 10px;
    margin-right: 40px;
  }
</style>