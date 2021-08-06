<template>
  <div class="outer">
    <task-commit-nav :cname="course.name" @back="back" />
    <div>
      <div class="content">
        <ul>
          <div slot="header" class="clearfix">
            <div>
              <span style="float: left"><h2>作业: {{task.tname}}</h2></span>
            </div>
          </div>
          <div class="text item" style=" color: #A0A0A0;">
            课程: {{course.name}}
          </div>
          <el-divider></el-divider>
          <div class="comment-box">
          <span>
            <div><img :src="avatar" /></div>
          </span>
            <el-input
              type="textarea"
              :rows="5"
              style="position: relative;left: 90px; width:680px;padding-right: 300px"
              placeholder="添加评论"
              v-model="textarea">
            </el-input>
            <div style="width: 100% ;height: 50px">
              <el-button @click="createComment()" :disabled="textarea==''" style="float: right;margin-right: 20px;margin-top: 20px" type="primary">确定</el-button>
            </div>
          </div>
          <el-divider ></el-divider>
          <li v-for="item in comments">
            <div>
              <div><img :src="require('../../assets/avatar/' + item.user.avatar)" /></div>
              <div class="text item">{{item.user.name}} {{item.user.userId}}<span style="color: #a9a9a9;padding-left: 10px">{{date_format(item.cmDate)}} {{time_format(item.cmDate)}}</span>
              </div>
            </div>
            <div class="text item">{{item.comment}}</div>
            <el-divider></el-divider>
          </li>
        </ul>
      </div>
    </div>

  </div>

</template>

<script>
  import TaskCommitNav from 'components/content/TaskCommitNav'
  import {request} from '../../network/request'
  import axios from 'axios'

  export default {
    name: 'Discuss',
    components: {
      TaskCommitNav
    },
    data() {
      return {
        textarea: "",
        task: {},
        // 评论
        comments: [],
        course: {},
        avatar:  require('../../assets/avatar/' + this.$store.state.avatar)
      }
    },
    mounted() {
      const commentConfig = request({
        url: '/api/task/getComments',
        method: 'get',
        params: {
          tno: this.$route.params.tno
        }
      })
      const courseConfig = request({
        url: '/api/course/getCourseByCno',
        method: 'get',
        params: {
          cno: this.$route.params.cno
        }
      })
      const taskConfig = request({
        url: '/api/taskList',
        method: 'get',
        params: {
          cno: this.$route.params.cno,
          tno: this.$route.params.tno
        }
      })

      // 只有三个请求都发送完成后，才会有返回
      axios.all([commentConfig, courseConfig, taskConfig])
        .then(axios.spread((res1, res2, res3) => {
          this.course = res2.data
          this.task = res3.data[0]
          this.comments = res1.data
        })).catch(err => {console.log(err)})
    },
    methods:{
      createComment(){
        let item = {
          task: {tno: this.$route.params.tno},
          user: {userId: this.$store.state.uid},
          comment: this.textarea,
        }

        request({
          url: '/api/task/comment',
          method: 'post',
          data: item
        }).then(res => {
          // 插入成功，重新请求
          if (res.data) {
            request({
              url: '/api/task/getComments',
              method: 'get',
              params: {
                tno: this.$route.params.tno
              }
            }).then(res => {
              this.comments = res.data
              this.textarea=""
            }).catch(err => {
              console.log(err)
            })
          }
        }).catch(err => {console.log(err)})
      },
      date_format(date) {
        return new Date(date).format("yy/MM/dd");
      },
      time_format(date) {
        return new Date(date).format("hh:mm");
      },
      back() {
        if (this.$store.state.permission == '2') {
          this.$router.push(`/teachertask/${this.$route.params.cno}`)
        } else {
          this.$router.push(`/studenttask/${this.$route.params.cno}`)
        }

      }
    }
  }
</script>

<style scoped>
  .outer {
    width: 100%;
  }

  .outer>div {
    width: 95%;
  }

  .content {
    padding-top: 100px;
    width: 810px;
    margin-left: auto;
    margin-right: auto;
  }

  .text {
    font-size: 14px;
  }

  .item {
    margin-bottom: 18px;
    padding-left: 90px;
  }

  .clearfix:before,
  .clearfix:after {
    display: table;
    content: "";
  }


  .clearfix:after {
    clear: both
  }

  .clearfix {
    margin-top: 30px;
    margin-bottom: 18px;
    padding: 0 20px 10px 30px;
  }
  .comment-box img{
    width: 37px;
    height: 37px;
    position: relative;
    float: left;
    top:40px;
    margin-right: 18px;
    margin-top: 10px;
  }

  li {
    list-style: none;
  }

  img {
    width: 37px;
    height: 37px;
    position: relative;
    float: left;
    margin-left: 38px;
    margin-top: -5px;
    border-radius: 50%;
  }


  h2 {
    padding-top: 10px;
    float: left;
    height: 50px;
    line-height: 50px;
    font-size: 20px;
    font-weight: bold;
    width: 100%;
  }
</style>
