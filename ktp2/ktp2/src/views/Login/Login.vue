<template>
  <div class="outer">
    <div>
      <div>账号登录</div>
      <div>
        <div><input type="text" placeholder="请输入账号" v-model="account" /></div>
        <div><input type="password" placeholder="请输入密码" v-model="pwd" /></div>
        <div><el-checkbox v-model="checked">下次自动登录</el-checkbox></div>
        <div><div class="login-btn" @click="test">登录</div></div>
        <div class="register"><span @click="register">还没有账号？</span><span @click="register">去注册</span></div>
      </div>

      <div>
        <slide-verify ref="slideblock" :l="42"
            :r="10"
            :w="310"
            :h="155"
            :imgs="imgs"
            slider-text="向右滑动"
            @success="onSuccess"
            @fail="onFail"
            ></slide-verify>
      </div>
      
    </div>
  </div>  
</template>

<script>
import verify1 from 'assets/img/verify1.jpg'
import verify2 from 'assets/img/verify2.jpeg'
import verify3 from 'assets/img/verify3.jpg'
import verify4 from 'assets/img/verify4.jpg'
import verify5 from 'assets/img/verify5.jpg'
import verify6 from 'assets/img/verify6.jpg'
import verify7 from 'assets/img/verify7.jpg'
import {request} from 'network/request'
import {IS_LOGIN, CHANGE_UID, USER_AVATAR, PERMISSION, CHANGE_NAME} from "../../store/mutation-type"

export default {
  name: 'Login',
  data() {
    return {
      account: '',
      pwd: '',
      checked: false,
      imgs: [verify1, verify2, verify3, verify4, verify5, verify6, verify7]
    }
  },
  methods: {
    register() {
      this.$router.push("/register")
    },
    onSuccess(){
      // 登录请求
      request({
        url: '/api/verify',
        method: 'get',
        params: {
          password: this.pwd,
          account: this.account,
          func: 'login'
        }

      }).then(res => {
        if (res.data) {
          this.$store.commit(IS_LOGIN, this.account)
          // 请求用户的完整信息，保存到全局中-->uid
          request({
            url: '/api/findOneEntity',
            method: 'get',
            params: {
              account: this.account
            }
          }).then(res => {
            this.$store.commit(CHANGE_UID, res.data.userId)
            this.$store.commit(USER_AVATAR, res.data.avatar)
            this.$store.commit(PERMISSION, res.data.permission.pid)
            this.$store.commit(CHANGE_NAME, res.data.name)

            // 若点了自动登录，将存放一部分数据到cookie中
            if (this.checked) {
              this.$cookies.set('user', {
                uid: res.data.userId,
                avatar: res.data.avatar,
                permission: res.data.permission.pid,
                name: res.data.name,
                account: res.data.account
              })
            }

            // 提示消息
            this.$message({
              message: '登录成功',
              type: 'success'
            })

            setTimeout(() => {
              document.querySelector(".outer>div>div:nth-child(3)").style.display = 'none'
              this.$router.push('/home')
            }, 1000)
          }).catch(err => {
            console.log(err)
          })
        }
        else {
          this.$message({
            message: '用户名和密码不匹配，请重试',
            type: 'error'
          })
          setTimeout(() => {
            document.querySelector(".outer>div>div:nth-child(3)").style.display = 'none'
            this.pwd = ''
            this.$refs.slideblock.reset()
          }, 500)

        }
      }).catch(err => {
        console.log(err)
      })
    },
    onFail(){
      // 验证失败
      this.$message({
        message: '验证失败，请重试',
        type: 'error'
      })
    },
    test() {
      document.querySelector(".outer>div>div:nth-child(3)").style.display = 'block'
    }
  }
}
</script>

<style scoped>
  .outer {
    height: 100%;
    display: flex;
    justify-content: center;
    align-items:center;
  }

  .outer>div {
    width: 480px;
    background-color: #fff;
    box-shadow: 0 3px 5px rgba(0, 0, 0, .2);
    position: relative;
  }

  .outer>div>div:first-child {
    font-size: 26px;
    font-family: PingFangSC-Medium;
    font-weight: 500;
    color: rgba(59, 61, 69, 1);
    padding-top: 53px;
    padding-bottom: 58px;
    padding-left: 30px;
  }

  .outer>div>div:nth-child(2) {
    padding-left: 30px;
    padding-right: 30px;
    padding-bottom: 60px;
  }

  .outer>div>div:nth-child(3) {
    position: absolute;
    top: 180px;
    left: 85px;
    display: none;
  }

  input {
    outline: none;
    font-size: 16px;
    padding-left: 12px;
    height: 64px;
    width: 406px;
    border: rgba(226,230,237,1) 1px solid;
    border-radius: 4px;
    margin-bottom: 24px;
  }
  input:focus {
    border: rgba(50, 186, 240, 1) 1px solid;
    transition: .3s;
  }

  .el-checkbox {
    padding-top: 20px;
    padding-bottom: 20px;
    margin-bottom: 25px;
  }

  .login-btn {
    height: 50px;
    width: 420px;
    font-size: 16px;
    color: #fff;
    text-align: center;
    line-height: 50px;
    border-radius: 3px;
    background: #32baf0;
    cursor: pointer;
    user-select: none;
  }

  .login-btn:hover {
    transition: .2s;
    background: #74b9ff;
  }

  .register {
    text-align: right;
    font-size: 14px;
    cursor: pointer;
    user-select: none;
    padding: 15px 0 15px 0;
  }
  .register span:first-child {
    color: #AFB1B3;
  }
  .register span:last-child {
    color: #32BAF0;
  }
</style>