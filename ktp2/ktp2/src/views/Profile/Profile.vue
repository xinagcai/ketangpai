<template>
  <div class="outer">
    <home-nav />
    <!-- 上部分 -->
    <div class="layer-top">
      <div>
        <img :src="avatar" :alt="user.name" />
        <el-upload
          class="avatar-uploader"
          :data="{uid: $store.state.uid}"
          action="/api/file/upload"
          :on-success="uploadSuccess"
          :on-error="uploadError"
          :show-file-list="false"
          :before-upload="beforeAvatarUpload">
          <div>更改头像</div>
        </el-upload>
      </div>
      <h1>{{user.name}}</h1>
    </div>
    <div class="layer-bottom">
      <div>
        <div>
          <h3>基本资料</h3>
          <ul>
            <li>
              <p>姓名：</p>
              <div class="edit-box">
                <span v-if="!editBasic">{{user.name}}</span>
                <input v-else type="text" name="name" v-model="username" placeholder="请输入姓名" />
              </div>
              <div v-if="!editBasic" @click="changeEdit(1)">编辑资料</div>
              <div v-else @click="changeEdit(1)">放弃编辑</div>
            </li>
            <li>
              <p>学校：</p>
              <div class="edit-box">
                <span v-if="!editBasic">{{showSchool}}</span>
                <input v-else type="text" name="school" v-model="school" placeholder="请输入学校" />
              </div>
            </li>
          </ul>
          <div v-if="editBasic" class="save-btn" @click="save(1)">保存</div>
        </div>
        <div>
          <h3>身份角色</h3>
          <ul>
            <li>
              <p>角色：</p>
              <div class="edit-box">
                <span v-if="!editPermission">{{showPermission}}</span>
                <div v-else>
                  <el-radio v-model="permission" :label="1">学生</el-radio>
                  <el-radio v-model="permission" :label="2">教师</el-radio>
                </div>
              </div>
              <div v-if="!editPermission" @click="changeEdit(2)">变更身份</div>
              <div v-else @click="changeEdit(2)">放弃变更</div>
            </li>
          </ul>
          <div v-if="editPermission" class="save-btn" @click="save(2)">保存</div>
        </div>
        <div>
          <h3>账号设置</h3>
          <ul>
            <li>
              <p v-if="!editUser">账号：</p>
              <p v-else>新密码：</p>
              <div class="edit-box">
                <span v-if="!editUser">{{user.account}}</span>
                <input v-else type="password" name="password" v-model="pwd" placeholder="请输入新密码" />
              </div>
              <div v-if="!editUser" @click="changeEdit(3)">修改</div>
              <div v-else @click="changeEdit(3)">放弃修改</div>
            </li>
            <li>
              <p v-if="!editUser">密码：</p>
              <p v-else>确认密码：</p>
              <div class="edit-box">
                <span v-if="!editUser">******</span>
                <input v-else type="password" name="passwordAgain" v-model="pwdAgain" placeholder="请再次输入密码" @focus="pwdChange(2)" @blur="pwdChange(1)" />
                <b>两次密码不一致</b>
              </div>
            </li>
          </ul>
          <div v-if="editUser" class="save-btn" @click="save(3)">保存</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import HomeNav from 'components/content/HomeNav'
import {request} from 'network/request'
import axios from 'axios'
import {USER_AVATAR} from "../../store/mutation-type"

export default {
  name: 'Profile',
  components: {
    HomeNav
  },
  data() {
    return {
      avatar: '',
      user: {},
      schools: [],
      school: '',
      username: '',
      permission: '',
      pwd: '',
      pwdAgain: '',
      editBasic: false,
      editPermission: false,
      editUser: false
    }
  },
  computed: {
    showSchool() {
      return this.user.school === undefined ? '' : this.user.school.sclName
    },
    showPermission() {
      return this.user.permission === undefined ? '' : this.user.permission.pname
    }
  },
  mounted() {
    this.avatar = require('../../assets/avatar/' + this.$store.state.avatar)
    // 获取个人信息
    const profileInfo = request({
      url: '/api/findEntityByUid',
      method: 'get',
      params: {
        uid: this.$store.state.uid
      }
    })
    // 获取学校信息
    const sclInfo = request({
      url: '/api/selectSchool',
      method: 'get'
    })

    // 当两个请求都完成是再处理
    axios.all([profileInfo, sclInfo])
    .then(axios.spread((res1, res2) => {
      this.user = res1.data
      this.schools = res2.data
      this.username = this.user.name
      this.school = this.user.school.sclName
      this.permission = this.user.permission.pid
    })).catch(err => {
      console.log(err)
    })
  },
  methods: {
    changeEdit(index) {
      let flag = ''

      if (index === 1) {
        this.editBasic = !this.editBasic
        flag = this.editBasic
      } else if (index === 2) {
        this.editPermission = !this.editPermission
        flag = this.editPermission
      } else if (index === 3) {
        this.editUser = !this.editUser
        flag = this.editUser
      }
      
      // 设置部分css样式
      // 基础资料部分
      if (!flag) {
        document.querySelector(`.layer-bottom>div>div:nth-child(${index})>ul>li:first-child>div:last-child`).style.color = "#818181"
        document.querySelector(`.layer-bottom>div>div:nth-child(${index})>ul>li:first-child>div:last-child`).style.border = "1px solid #c8c8c8"
        document.querySelector(`.layer-bottom>div>div:nth-child(${index})>ul>li:first-child>div:last-child`).onmouseover = () => {
          document.querySelector(`.layer-bottom>div>div:nth-child(${index})>ul>li:first-child>div:last-child`).style.color = "#2d2d2d"
          document.querySelector(`.layer-bottom>div>div:nth-child(${index})>ul>li:first-child>div:last-child`).style.background = "#e6e6e6"
        }
        document.querySelector(`.layer-bottom>div>div:nth-child(${index})>ul>li:first-child>div:last-child`).onmouseout = () => {
          document.querySelector(`.layer-bottom>div>div:nth-child(${index})>ul>li:first-child>div:last-child`).style.color = "#818181"
          document.querySelector(`.layer-bottom>div>div:nth-child(${index})>ul>li:first-child>div:last-child`).style.background = "none"
        }
        
        if (index === 3) {
          document.querySelector('.layer-bottom>div>div>ul>li:nth-child(2)>div:nth-child(2)>b').style.display = "none"
        }

        // 未修改则将只还原为原本的值
        this.permission = this.user.permission.pid
        this.school = this.user.school.sclName
        this.username = this.user.name
      } else {
        document.querySelector(`.layer-bottom>div>div:nth-child(${index})>ul>li:first-child>div:last-child`).style.color = "#4d90fe"
        document.querySelector(`.layer-bottom>div>div:nth-child(${index})>ul>li:first-child>div:last-child`).style.border = "1px solid #4d90fe"
        document.querySelector(`.layer-bottom>div>div:nth-child(${index})>ul>li:first-child>div:last-child`).onmouseover = () => {
          document.querySelector(`.layer-bottom>div>div:nth-child(${index})>ul>li:first-child>div:last-child`).style.color = "#fff"
          document.querySelector(`.layer-bottom>div>div:nth-child(${index})>ul>li:first-child>div:last-child`).style.background = "#4d90fe"
        }
        document.querySelector(`.layer-bottom>div>div:nth-child(${index})>ul>li:first-child>div:last-child`).onmouseout = () => {
          document.querySelector(`.layer-bottom>div>div:nth-child(${index})>ul>li:first-child>div:last-child`).style.color = "#4d90fe"
          document.querySelector(`.layer-bottom>div>div:nth-child(${index})>ul>li:first-child>div:last-child`).style.background = "none"
        }
      }
    },
    pwdChange(flag) {
      // 1是blur，2是focus
      if (flag === 1) {
        if (this.pwd !== this.pwdAgain) {
          document.querySelector('.layer-bottom>div>div>ul>li:nth-child(2)>div:nth-child(2)>b').style.display = "inline-block"
        }
      } else {
        document.querySelector('.layer-bottom>div>div>ul>li:nth-child(2)>div:nth-child(2)>b').style.display = "none"
      }
    },
    // 头像上传
    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/jpeg';
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPG) {
        this.$message.error('上传头像图片只能是 JPG 格式!');
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!');
      }
      return isJPG && isLt2M;
    },
    uploadSuccess(res) {
      // 头像上传成功，重新渲染用户的头像
      if (res !== false) {
        this.$message.success('上传成功')
        this.$store.commit(USER_AVATAR, res)
        this.avatar = res
      } else {
        this.$message.error('上传失败')
      }
      
    },
    uploadError(res) {
      this.$message.error('上传失败')
    },
    save(status) {
      if (status === 1) {
        // 修改基本信息->姓名、学校
        const nowSchool = this.schools.filter(e => e.sclName === this.school)
        if (nowSchool.length !== 0) {
          this.user.school = nowSchool[0]
          this.user.name = this.username
          this.user.pid = this.user.permission.pid
          this.user.sclID = this.user.school.sclID

          // 修改
          request({
            url: '/api/updateUserInfo',
            method: 'put',
            params: {
              name: this.user.name,
              sclID: this.user.school.sclID,
              userId: this.user.userId
            }
          }).then(res => {
            this.$message.success('基本信息修改成功')
          }).catch(err => {
            this.$message.success('基本信息修改失败')
          })
        } else {
          this.school = this.user.school.sclName
          this.$message.error('基本信息修改失败')
        }
        
        this.changeEdit(1)
      } else if (status === 2) {
        if (this.user.permission.pid === 2) {
          this.$message.warning('教师角色已是会员，无法修改')
        } else {
          // 修改角色信息->角色
          this.user.pid = this.permission
          this.user.sclID = this.user.school.sclID
          
          if (this.permission === 1) {
            this.user.permission = {pid: this.permission, pname: '学生'}
          } else {
            this.user.permission = {pid: this.permission, pname: '教师'}
          }
          
          // 修改
          request({
            url: '/api/updateUserInfo',
            method: 'put',
            params: {
              pid: this.user.pid,
              userId: this.user.userId
            }
          }).then(res => {
            this.$message.success('角色信息修改成功')
          }).catch(err => {
            this.$message.success('角色信息修改失败')
          })
        }
        

        this.changeEdit(2)
      } else {
        // 修改密码
        if (this.pwd !== this.pwdAgain) {
          this.$message.error('请检验输入是否合法')
        } else {
          this.user.pid = this.permission
          this.user.sclID = this.user.school.sclID
          this.user.password = this.pwd

          // 修改
          request({
            url: '/api/updateUserInfo',
            method: 'put',
            params: {
              password: this.user.password,
              userId: this.user.userId
            }
          }).then(res => {
            this.$message.success('密码修改成功')
          }).catch(err => {
            this.$message.success('密码修改失败')
          })
        }

        this.changeEdit(3)
      }
    }
  }
}
</script>

<style scoped>
  .outer {
    height: 100%;
    position: relative;
  }
  
  /* 上部分 */
  .layer-top {
    height: 200px;
    margin-top: 65px;
    border-bottom: 1px solid #C8C8C8;
    text-align: center;
    margin-bottom: 40px;
    overflow: hidden;
  }
  .layer-top>div:first-child {
    margin: 45px auto 0;
    width: 73px;
  }
  .layer-top>div:first-child:hover>.avatar-uploader {
    opacity: 1;
  }
  .layer-top>div:first-child>img {
    height: 73px;
    width: 73px;
    margin: 0 auto;
    border-radius: 50%;
  }
  .layer-top>h1 {
    padding: 43px 0 10px;
    font-size: 28px;
    font-weight: 700;
    margin-top: -30px;
    line-height: 34px;
    height: 34px;
  }

  /* 下部分 */
  .layer-bottom {
    border: 1px solid #c8c8c8;
    margin-top: 40px;
    margin-bottom: 60px;
    padding-bottom: 60px;
    background: #fff;
    font-size: 14px;
  }
  .layer-bottom>div {
    padding: 20px 120px 0;
  }
  .layer-bottom>div>div {
    margin-top: 20px;
  }
  .layer-bottom>div>div>h3 {
    font-size: 14px;
    color: #333;
    border-bottom: 1px solid #DCDCDC;
    padding-bottom: 20px;
    line-height: 1;
  }
  .layer-bottom>div>div>ul {
    padding-top: 10px;
  }
  .layer-bottom>div>div>ul>li {
    height: 32px;
    margin-top: 10px;
  }
  .layer-bottom>div>div>ul>li>p {
    float: left;
    min-width: 75px;
    line-height: 32px;
    color: #969696;
  }
  li {
    list-style: none;
  }

  .layer-bottom>div>div>ul>li>div:nth-child(2)>span {
    display: inline-block;
    color: #595959;
    line-height: 32px;
    height: 32px;
  }

  .layer-bottom>div>div>ul>li>div:nth-child(2)>input {
    width: 172px;
    height: 30px;
    line-height: 30px\9;
    border: 1px solid #c8c8c8;
    color: #595959;
    padding: 0 14px;
    border-radius: 3px;
    font-size: 14px;
    outline: none;
  }
  .layer-bottom>div>div>ul>li>div:nth-child(2)>input:focus {
    border: 1px solid #4d90fe;
  }
  .layer-bottom>div>div>ul>li:nth-child(2)>div:nth-child(2)>b {
    display: none;
    line-height: 32px;
    color: red;
    font-size: 12px;
    margin-left: 20px;
    font-weight: 400;
  }

  .layer-bottom>div>div>ul>li:first-child>div:last-child {
    float: right;
    min-width: 56px;
    height: 22px;
    text-align: center;
    line-height: 22px;
    font-size: 12px;
    border-radius: 3px;
    margin-top: 3px;
    padding-left: 5px;
    padding-right: 5px;
    cursor: pointer;
    user-select: none;
    color: #818181;
    border: 1px solid #c8c8c8;
    transition: .3s;
  }

  .layer-bottom>div>div:nth-child(1)>ul>li:first-child>div:last-child:hover,
  .layer-bottom>div>div:nth-child(2)>ul>li:first-child>div:last-child:hover,
  .layer-bottom>div>div:nth-child(3)>ul>li:first-child>div:last-child:hover {
    color: #2d2d2d;
    background: #e6e6e6;
  }

  .edit-box {
    float: left;
    position: relative;
  }
  .save-btn {
    display: block;
    margin-left: 75px;
    width: 56px;
    height: 26px;
    border-radius: 3px;
    background: #4d90fe;
    color: #FFF;
    text-align: center;
    line-height: 26px;
    margin-top: 10px;
    cursor: pointer;
    user-select: none;
  }

  /* 上传头像 */
  .avatar-uploader {
    position: absolute;
    top: 45px;
    color: #FFF;
    background: rgba(0,0,0,.3);
    transition: .5s ease;
    font-size: 14px;
    border-radius: 50%;
    opacity: 0;
  }
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    overflow: hidden;
  }
  .el-upload>div {
    height: 73px;
    width: 73px;
    border-radius: 50%;
    line-height: 73px;
    text-align: center;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 73px;
    height: 73px;
    border-radius: 50%;
    line-height: 73px;
    text-align: center;
  }
</style>