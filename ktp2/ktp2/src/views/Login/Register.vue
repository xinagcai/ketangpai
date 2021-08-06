<template>
  <div class="outer">
    <div>
      <div v-if="status === 1" class="register-home">
        <div>注册账号</div>
        <div>请选择您在日常教学中的实际身份</div>
        <div>
          <div @click="status = 3">我是老师/助教</div>
          <div @click="status = 2">我是学生</div>
        </div>
        <div class="login"><span @click="login">已有账号？</span><span @click="login">去登录</span></div>
      </div>
      <div v-else-if="status === 2" class="register-student">
        <div><div><i class="el-icon-back" @click="changeStatus" /></div><div>学生注册</div></div>
        <div>
          <div><input type="text" placeholder="账号" v-model="account" @blur="verifyAccountExist" @focus="focusBorder(1)" /><div></div></div>
          <div><input type="password" placeholder="密码" v-model="pwd" @focus="focusBorder(2)" @blur="notNullContent(2, $event)" /><div>密码为必填项目</div></div>
          <div><input type="password" placeholder="再次输入密码" v-model="pwdAgain" @blur="verifyPwd" @focus="focusBorder(3)" /><div></div></div>
          <div><input type="text" placeholder="姓名" v-model="name" @focus="focusBorder(4)" @blur="notNullContent(4, $event)" /><div>姓名为必填项目</div></div>
          <div><input type="text" placeholder="学号" v-model="identityNo" @focus="focusBorder(5)" @blur="notNullContent(5, $event)" /><div>学号为必填项目</div></div>
          <div>
            <el-select
              v-model="school"
              filterable
              remote
              reserve-keyword
              placeholder="学校"
              :remote-method="remoteMethod"
              :loading="loading">
              <el-option
                 v-for="item in options"
                :key="item.sclID"
                :label="item.sclName"
                :value="item">
              </el-option>
            </el-select>
          </div>
        </div>
        <div>
        <slide-verify :l="42"
            :r="10"
            :w="310"
            :h="155"
            :imgs="imgs"
            slider-text="向右滑动"
            @success="onSuccess"
            @fail="onFail"
            ></slide-verify>
        </div>
        <div><div class="for-student" @click="register">注册为学生</div></div>
        <div class="login"><span @click="login">已有账号？</span><span @click="login">去登录</span></div>
      </div>
      <div v-else class="register-teacher">
        <div><div><i class="el-icon-back" @click="changeStatus" /></div><div>老师/助教注册</div></div>
        <div>
          <div><input type="text" placeholder="账号" v-model="account" @blur="verifyAccountExist" @focus="focusBorder(1)" /><div></div></div>
          <div><input type="password" placeholder="密码" v-model="pwd" @focus="focusBorder(2)" @blur="notNullContent(2, $event)" /><div>密码为必填项目</div></div>
          <div><input type="password" placeholder="再次输入密码" v-model="pwdAgain" @blur="verifyPwd" @focus="focusBorder(3)" /><div></div></div>
          <div><input type="text" placeholder="姓名" v-model="name" @focus="focusBorder(4)" @blur="notNullContent(4, $event)" /><div>姓名为必填项目</div></div>
          <div>
            <el-select
              v-model="school"
              filterable
              remote
              reserve-keyword
              placeholder="学校"
              :remote-method="remoteMethod"
              :loading="loading">
              <el-option
                 v-for="item in options"
                :key="item.sclID"
                :label="item.sclName"
                :value="item">
              </el-option>
            </el-select>
          </div>
        </div>
        <div>
          <slide-verify :l="42"
            :r="10"
            :w="310"
            :h="155"
            :imgs="imgs"
            slider-text="向右滑动"
            @success="onSuccess"
            @fail="onFail"
            >
          </slide-verify>
        </div>
        <div><div class="for-teacher" @click="register">注册为老师/助教</div></div>
        <div class="login"><span @click="login">已有账号？</span><span @click="login">去登录</span></div>
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
import {request} from '../../network/request.js'
import {IS_LOGIN, USER_AVATAR, CHANGE_UID, PERMISSION} from "../../store/mutation-type"
import qs from 'qs'

export default {
  name: 'Register',
  data() {
    return {
      status: 1,
      account: '',
      pwd: '',
      pwdAgain: '',
      name: '',
      identityNo: '',
      school: {},
      schools: [],
      options: [],
      loading: false,
      imgs: [verify1, verify2, verify3, verify4, verify5, verify6, verify7],
      verify: false,
      accountExist: false
    }
  },
  mounted() {
    request({
      url: '/api/selectSchool',
      method: 'get'
    }).then(res => {
      this.schools = res.data
    }).catch(err => {
      console.log(err)
    })
  },
  methods: {
    login() {
      this.$router.push('/')
    },
    remoteMethod(query) {
      if (query !== '') {
        this.loading = true;
        setTimeout(() => {
          this.loading = false;
          this.options = this.schools.filter(item => {
            return item.sclName.indexOf(query) > -1;
          });
        }, 200);
      } else {
        this.options = [];
      }
    },
    onSuccess() {
      this.verify = true
    },
    onFail() {
      this.verify = false
    },
    changeStatus() {
      if (this.status === 2) {
        document.querySelector(".register-student>div:nth-child(2)>div:first-child>div").style.display = "none"
        document.querySelector(".register-student>div:nth-child(2)>div:nth-child(3)>div").style.display = "none"
      } else if (this.status === 3) {
        document.querySelector(".register-teacher>div:nth-child(2)>div:first-child>div").style.display = "none"
        document.querySelector(".register-teacher>div:nth-child(2)>div:nth-child(3)>div").style.display = "none"
      }

      this.account = ''
      this.pwd = ''
      this.pwdAgain = ''
      this.name = ''
      this.identityNo = ''
      this.school = {}
      this.options = []
      this.status = 1
      this.accountExist = false
      this.verify = false
    },
    verifyAccountExist() {
      request({
        url: '/api/verify',
        method: 'get',
        params: {
          account: this.account,
          password: '',
          func: 'register'
        }
      }).then(res => {
        if (!res.data) {
          this.accountExist = true
          if (this.status === 2) {
            document.querySelector(".register-student>div:nth-child(2)>div:first-child>div").style.display = "block"
            document.querySelector(".register-student>div:nth-child(2)>div:first-child>div").innerHTML = "该用户已存在"
            document.querySelector(".register-student>div:nth-child(2)>div:first-child>input").style.borderColor = "red"
          } else if (this.status === 3) {
            document.querySelector(".register-teacher>div:nth-child(2)>div:first-child>div").style.display = "block"
            document.querySelector(".register-teacher>div:nth-child(2)>div:first-child>div").innerHTML = "该用户已存在"
            document.querySelector(".register-teacher>div:nth-child(2)>div:first-child>input").style.borderColor = "red"
          }
        } else {
          this.accountExist = false
          if (this.status === 2) {
            document.querySelector(".register-student>div:nth-child(2)>div:first-child>div").style.display = "none"
            document.querySelector(".register-student>div:nth-child(2)>div:first-child>input").style.borderColor = "rgba(226, 230, 237, 1)"
          } else if (this.status === 3) {
            document.querySelector(".register-teacher>div:nth-child(2)>div:first-child>div").style.display = "none"
            document.querySelector(".register-teacher>div:nth-child(2)>div:first-child>input").style.borderColor = "rgba(226, 230, 237, 1)"
          }
        }
      })
    },
    verifyPwd() {
      if (this.pwd !== this.pwdAgain) {
        if (this.status === 2) {
          document.querySelector(".register-student>div:nth-child(2)>div:nth-child(3)>div").style.display = "block"
          document.querySelector(".register-student>div:nth-child(2)>div:nth-child(3)>div").innerHTML = "两次密码不相同"
          document.querySelector(".register-student>div:nth-child(2)>div:nth-child(3)>input").style.borderColor = "red"
        } else if (this.status === 3) {
          document.querySelector(".register-teacher>div:nth-child(2)>div:nth-child(3)>div").style.display = "block"
          document.querySelector(".register-teacher>div:nth-child(2)>div:nth-child(3)>div").innerHTML = "两次密码不相同"
          document.querySelector(".register-teacher>div:nth-child(2)>div:nth-child(3)>input").style.borderColor = "red"
        }
      } else {
        if (this.status === 2) {
          document.querySelector(".register-student>div:nth-child(2)>div:nth-child(3)>div").style.display = "none"
          document.querySelector(".register-student>div:nth-child(2)>div:nth-child(3)>input").style.borderColor = "rgba(226, 230, 237, 1)"
        } else if (this.status === 3) {
          document.querySelector(".register-teacher>div:nth-child(2)>div:nth-child(3)>div").style.display = "none"
          document.querySelector(".register-teacher>div:nth-child(2)>div:nth-child(3)>input").style.borderColor = "rgba(226, 230, 237, 1)"
        }
      }

    },
    focusBorder(index) {
      if (this.status === 3) {
        document.querySelector(`.register-teacher>div:nth-child(2)>div:nth-child(${index})>input`).style.borderColor = "rgba(50, 186, 240, 1)"
        document.querySelector(`.register-teacher>div:nth-child(2)>div:nth-child(${index})>input`).style.transition = ".3s"
      } else if (this.status === 2) {
        document.querySelector(`.register-student>div:nth-child(2)>div:nth-child(${index})>input`).style.borderColor = "rgba(50, 186, 240, 1)"
        document.querySelector(`.register-student>div:nth-child(2)>div:nth-child(${index})>input`).style.transition = ".3s"
      }
    },
    notNullContent(index, e) {
      if (this.status === 2) {
        if (e.target.value === '') {
          document.querySelector(`.register-student>div:nth-child(2)>div:nth-child(${index})>div`).style.display = "block"
          document.querySelector(`.register-student>div:nth-child(2)>div:nth-child(${index})>input`).style.borderColor = "red"
          document.querySelector(`.register-student>div:nth-child(2)>div:nth-child(${index})>input`).style.transition = ".3s"
        } else {
          document.querySelector(`.register-student>div:nth-child(2)>div:nth-child(${index})>div`).style.display = "none"
          document.querySelector(`.register-student>div:nth-child(2)>div:nth-child(${index})>input`).style.borderColor = "rgba(226, 230, 237, 1)"
          document.querySelector(`.register-student>div:nth-child(2)>div:nth-child(${index})>input`).style.transition = ".3s"
        }
      } else if (this.status === 3) {
        if (e.target.value === '') {
          document.querySelector(`.register-teacher>div:nth-child(2)>div:nth-child(${index})>div`).style.display = "block"
          document.querySelector(`.register-teacher>div:nth-child(2)>div:nth-child(${index})>input`).style.borderColor = "red"
          document.querySelector(`.register-teacher>div:nth-child(2)>div:nth-child(${index})>input`).style.transition = ".3s"
        } else {
          document.querySelector(`.register-teacher>div:nth-child(2)>div:nth-child(${index})>div`).style.display = "none"
          document.querySelector(`.register-teacher>div:nth-child(2)>div:nth-child(${index})>input`).style.borderColor = "rgba(226, 230, 237, 1)"
          document.querySelector(`.register-teacher>div:nth-child(2)>div:nth-child(${index})>input`).style.transition = ".3s"
        }
      }
    },
    register() {
      if (this.verify && !this.accountExist && this.account !== '' && this.pwd !== '' && this.pwd === this.pwdAgain && this.school.sclID !== undefined) {
        if (this.status === 2) {
          // 学生注册
          if (this.identityNo !== '') {
            request({
              url: '/api/register',
              method: 'post',
              data: {
                account: this.account,
                password: this.pwd,
                name: this.name,
                permission: {pid: 1, pname: '学生'},
                school: this.school,
                identifyNo: this.identityNo
              }

            }).then(res => {
              this.$message({
                message: '注册成功，1秒后跳转到首页',
                type: 'success'
              });

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
              }).catch(err => {
                console.log(err)
              })
              // 延迟1秒跳转
              setTimeout(() => {
                this.$router.replace('/home')
              }, 1000)

            }).catch(err => {
              this.$message({
                message: '注册失败，请重试',
                type: 'error'
              });
              this.changeStatus()
            })

          } else {
            this.$message({
              message: '请检查填入信息是否合法',
              type: 'warning'
            });
          }
        } else if (this.status === 3) {
          //教师注册
          request({
            url: '/api/register',
            method: 'post',
            data: {
              account: this.account,
              password: this.pwd,
              name: this.name,
              permission: {pid: 2, pname: '教师'},
              school: this.school
            }
          }).then(res => {
            this.$store.commit(IS_LOGIN, this.account)
            this.$message({
              message: '注册成功，1秒后跳转到首页',
              type: 'success'
            });

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
              this.$store.commit(CHANGE_NAME, res.data.name)
              this.$store.commit(PERMISSION, res.data.permission.pid)
            }).catch(err => {
              console.log(err)
            })

            // 延迟1秒跳转
            setTimeout(() => {
              this.$router.replace('/home')
            }, 1000)

          }).catch(err => {
            this.$message({
              message: '注册失败，请重试',
              type: 'error'
            });

            this.changeStatus()
          })
        }
      } else {
        this.$message({
          message: '请检查填入信息是否合法',
          type: 'warning'
        });
      }

    }
  }
}
</script>

<style scoped>
  .outer {
    height: 100%;
    display: flex;
    justify-content: center;
  }

  .outer>div {
    width: 480px;
    height: 100%;
  }

  .register-home, .register-student, .register-teacher {
    margin-top: 50px;
    width: 100%;
    background: #fff;
    box-shadow: 0 3px 5px rgba(0, 0, 0, .2);
    border-radius: 3px;
    position: relative;
  }

  /* status = 1 */
  .register-home>div:first-child {
    font-size: 26px;
    font-family: PingFangSC-Medium;
    font-weight: 500;
    color: rgba(59, 61, 69, 1);
    padding-top: 53px;
    padding-left: 30px;
    padding-right: 30px;
  }
  .register-home>div:nth-child(2) {
    font-size: 14px;
    color: #707070;
    margin-top: 20px;
    padding-left: 30px;
    padding-right: 30px;
  }

  .register-home>div:nth-child(3) {
    font-size: 16px;
    font-family: 'PingFang SC', tahoma, arial, 'helvetica neue','hiragino sans gb','microsoft yahei ui','microsoft yahei',simsun,sans-serif;
    margin-top: 60px;
    padding-left: 30px;
    padding-right: 30px;
  }
  .register-home>div:nth-child(3)>div {
    height: 64px;
    width: 420px;
    color: #fff;
    text-align: center;
    line-height: 65px;
    border-radius: 3px;
    background: #32baf0;
    cursor: pointer;
    user-select: none;
  }

  .register-home>div:nth-child(3)>div:first-child {
    margin-bottom: 70px;
  }

  .login {
    text-align: right;
    font-size: 14px;
    cursor: pointer;
    user-select: none;
    padding: 15px 30px 15px 0;
  }
  .login span:first-child {
    color: #AFB1B3;
  }
  .login span:last-child {
    color: #32BAF0;
  }

  /* status = 2 */
  .register-student>div:first-child,
  .register-teacher>div:first-child {
    display: flex;
    flex-direction: row;
    font-size: 32px;
    padding-left: 30px;
    padding-top: 30px;
  }

  .register-student>div:nth-child(2),
  .register-teacher>div:nth-child(2) {
    margin-top: 25px;
    padding-left: 30px;
    padding-right: 30px;
  }

  .register-student>div:nth-child(2)>div:first-child,
  .register-teacher>div:nth-child(2)>div:first-child,
  .register-student>div:nth-child(2)>div:nth-child(2),
  .register-teacher>div:nth-child(2)>div:nth-child(2),
  .register-student>div:nth-child(2)>div:nth-child(3),
  .register-teacher>div:nth-child(2)>div:nth-child(3),
  .register-student>div:nth-child(2)>div:nth-child(4),
  .register-teacher>div:nth-child(2)>div:nth-child(4),
  .register-student>div:nth-child(2)>div:nth-child(5) {
    position: relative;
  }
  .register-student>div:nth-child(2)>div:first-child>div,
  .register-teacher>div:nth-child(2)>div:first-child>div,
  .register-student>div:nth-child(2)>div:nth-child(2)>div,
  .register-teacher>div:nth-child(2)>div:nth-child(2)>div,
  .register-student>div:nth-child(2)>div:nth-child(3)>div,
  .register-teacher>div:nth-child(2)>div:nth-child(3)>div,
  .register-student>div:nth-child(2)>div:nth-child(4)>div,
  .register-teacher>div:nth-child(2)>div:nth-child(4)>div,
  .register-student>div:nth-child(2)>div:nth-child(5)>div {
    position: absolute;
    display: none;
    color: red;
    font-size: 12px;
    top: 50px;
    left: 15px;
  }

  input {
    outline: none;
    font-size: 16px;
    padding-left: 12px;
    height: 45px;
    width: 406px;
    border: rgba(226,230,237,1) 1px solid;
    border-radius: 4px;
    margin-bottom: 24px;
  }
  input:focus {
    border: rgba(50, 186, 240, 1) 1px solid;
    transition: .3s;
  }

  .el-select {
    width: 420px;
    height: 45px;
    margin-bottom: 24px;
  }

  .el-icon-back {
    cursor: pointer;
    margin-right: 22px;
  }

  .register-student>div:nth-child(3),
  .register-teacher>div:nth-child(3) {
    padding-left: 85px;
    margin-bottom: 30px;
  }

  .register-teacher>div:nth-child(4),
  .register-student>div:nth-child(4) {
    padding-left: 30px;
  }

  .for-teacher, .for-student {
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
  .for-student:hover, .for-teacher:hover {
    transition: .2s;
    background: #74b9ff;
  }

</style>
