<template>
  <div class="wrapper" v-if="this.renderKey">
    <task-commit-nav 
      :cname="course.name" 
      :tno="task.tno"
      :cno="course.cno"
      @back="back" />
    <div class="task-info">
      <div class="task-head">
        <div class="task-title">
          <span>{{ task.tname }}</span>
        </div>
        <div class="task-word">
          <span v-html="task.description"></span>
        </div>
      </div>
      <div class="task-props">
        <span>截止日期：{{ date_format(task.endDate) }} {{time_format(task.endDate)}}</span>
        <span>个人作业</span>
      </div>
    </div>
    <div class="submit">
      <el-button style="width: 102px;" type="primary" :disabled="isDisabled" plain @click="submit">{{grade.score == -1 ? '提交' : '更新提交'}}</el-button>
      <span style="float: right">{{ grade.score == -1 ? "未完成":"已提交" }}</span>
    </div>
    <div class="work-text">
      <span>答：</span>
      <el-input
        style="width: 97%;float: right"
        type="textarea"
        :autosize="{ minRows: 5 }"
        placeholder="点击编辑作业内容（仅老师可看）..."
        @input="canDisabled"
        v-model="answer">
      </el-input>
    </div>
  </div>
</template>

<script>
  import axios from 'axios'
  import TaskCommitNav from 'components/content/TaskCommitNav'
  import {request} from '../../network/request'
  export default {
    name: "TaskCommit",
    components: {
      TaskCommitNav
    },
    data() {
      return {
        course: {},
        task: {},
        grade: {},
        renderKey: false,
        end_date: '',
        answer: '',
        isDisabled: true,
      }
    },
    mounted() {
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
          tno: this.$route.params.tno,
          cno: this.$route.params.cno
        }
      })

      const gradeConfig = request({
        url: '/api/grade/getGrade',
        method: 'get',
        params: {
          uid: this.$store.state.uid,
          tno: this.$route.params.tno
        }
      })

      axios.all([courseConfig, taskConfig, gradeConfig])
      .then(axios.spread((res1, res2, res3) => {
        this.course = res1.data
        this.task = res2.data[0]
        this.grade = res3.data
        this.renderKey = true
        this.answer = this.grade.answer
        this.isDisabled = this.answer == ''
      })).catch(err => {console.log(err)})
    },
    methods: {
      submit() {
        request({
          url: '/api/grade/submit',
          method: 'put',
          params: {
            uid: this.$store.state.uid,
            tno: this.task.tno,
            answer: this.answer
          }
        }).then(res => {
          if (res.data) {
            this.$router.push(`/studenttask/${this.course.cno}`)
            this.$message.success('提交成功')
          } else {
            this.$message.error('提交失败，请重试')
          }
        }).catch(err => {consol.log(err)})
      },
      canDisabled() {
        this.isDisabled = this.answer == ''
      },
      date_format(date) {
      return new Date(date).format("yy/MM/dd");
      },
      time_format(date) {
        return new Date(date).format("hh:mm");
      },
      back() {
        this.$router.push(`/studenttask/${this.course.cno}`)
      }
    },
    
  }
</script>

<style scoped>
  .wrapper {
    width: 83%;
    margin: auto;
    font-size: 14px;
    font-family: 'PingFang SC',tahoma,arial,'helvetica neue','hiragino sans gb','microsoft yahei ui','microsoft yahei',simsun,sans-serif;
  }
  .task-info {
    padding-top: 100px;
  }
  .submit {
    margin-top: 40px;
  }
  .task-title {
    font-size: 18px;
  }
  .task-word {
    font-size: 12px;
    margin-top: 14px;
    color: #5F6368;
  }
  .task-props {
    margin-top: 30px;
  }
  .task-props span{
    background-color: #F1F3F4;
    font-size: 12px;
    color: #5F6368;
    padding: 3px 5px;
    margin-right: 10px;
  }
  .work-text {
    margin-top: 20px;
  }
</style>
