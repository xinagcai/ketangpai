<template>
   <div class="wrapper">
    <work-nav :cname="course.name"></work-nav>
    <div class="info-top">
      <div class="topm">
        <h1>{{course.name}}</h1>
        <div class="box-box">
          <div class="sele">
            <div class="infotextmain">加课码: </div>
            <div class="codetext">{{course.cno}}</div>
          </div>
          <ul>
            <li><div @click="gotoCourseMember">成员: {{course.members + 1}}</div></li>
            <li><div>成绩管理</div></li>
          </ul>
          <div class="coursedatabox" style="float: right">
            <div class="coursedata">
              <ul>
                <li style="display: block">{{tasks.length}}</li>
                <li>发布作业</li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="content">
      <el-button type="primary" @click="deliver(1)" plain>发布个人作业</el-button>
      <el-button type="primary" @click="deliver(2)" plain>发布小组作业</el-button>
    </div>
    <PersonalDeliver
      v-show="showDeliver===1"
      :cno="course.cno"
      @deliver="deliver"
      @send="send"/>
    <GroupDeliver
      v-show="showDeliver===2"
      @deliver="deliver"/>
    <div v-if="loadKey">
      <div v-for="task in tasks" :key="task.tno">
        <div class="homework-box" v-show="!editing[task.tno]">
          <div class="homework-inner">
            <div class="unique-top">
              <div class="work-type">
                <span class="work-type-first-span">个人作业</span>
                <span>{{ date_format(task.createDate) }}</span>
                <span>{{ time_format(task.createDate) }}</span>
                <el-dropdown @command="commandHandler" @click.native="focus=task.tno" trigger="click" style="float: right">
                  <span class="el-dropdown-link">· · ·</span>
                  <el-dropdown-menu slot="dropdown" style="width: 200px">
                    <el-dropdown-item command="edit">编辑</el-dropdown-item>
                    <el-dropdown-item command="saveTo">保存到...</el-dropdown-item>
                    <el-dropdown-item command="delete">删除</el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </div>
            </div>
            <div class="middle">
              <div class="unique-count">
                <span class="score">
                  <a>{{ task.judgeNum }}</a>
                  <a>已批</a>
                </span>
                  <span class="score">
                  <a>{{ task.submitNum - task.judgeNum }}</a>
                  <a>未批</a>
                </span>
                  <span class="score">
                  <a>{{ course.members - task.submitNum }}</a>
                  <a>未交</a>
                </span>
              </div>
              <div class="unique-title">
                <el-link @click="gotoRevise(task)" class="work-name">{{ task.tname }}</el-link>
                <div>
                  <span class="unique-word" v-html="task.description"></span>
                </div>
              </div>
            </div>
            <div class="unique-bottom">
              <span class="work-deadline">
                截止日期：
                <span>{{ date_format(task.endDate) }}</span>
                <span>{{ time_format(task.endDate) }}</span>
              </span>
              <el-link href="#" class="work-commend">0条讨论</el-link>
            </div>
          </div>
        </div>
        <div v-show="editing[task.tno]">
          <PersonalDeliver :task="task" :edit="true" @deliver="editing[task.tno]=false" @send="send"/>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import axios from 'axios';
  import PersonalDeliver from "components/content/PersonalDeliver";
  import WorkNav from 'components/content/WorkNav'
  import GroupDeliver from 'components/content/GroupDeliver'
  import { request } from '../../network/request';

  export default {
    name: "TeacherTask",
    components: {PersonalDeliver, WorkNav, GroupDeliver},
    data() {
      return {
        showDeliver: 0,
        tasks: [],
        loadKey: false,
        editing: [],
        focus: '',
        course: {}
      }
    },
    mounted() {
      request({
        url: '/api/course/getCourseByCno',
        method: 'get',
        params: {
          cno: this.$route.params.cno
        }
      }).then(res => {
        this.course = res.data
        this.send(true)
      }).catch(err => {
        console.log(err)
      })
    },
    methods: {
      gotoCourseMember() {
        this.$router.push(`/coursemember/${this.course.cno}`)
      },
      deliver(index) {
        this.showDeliver !== index ? this.showDeliver = index:this.showDeliver = 0;
      },
      deepClone(orign,target) {
       const tar = target || {};
        const arrStr = "[object Array]";
        for (const prop in orign)
        {
          if (orign.hasOwnProperty(prop))  {
            if (typeof(orign[prop])=='object'&&typeof(orign[prop])!=="undefined") {
              tar[prop]=Object.prototype.toString.call(orign[prop])===arrStr?[]:{};
              deepClone(orign[prop],tar[prop]);
            } else {
              tar[prop] = orign[prop];
            }
          }
        }
        return tar;
      },
      send(restore = false) {
        axios.get('/api/taskList?cno=' + this.course.cno).then(res => {
          console.log(res.data)
          this.tasks = res.data;
          if (restore) {
            this.tasks.forEach(task => {
              this.editing[task.tno] = false;
            })
          }
          this.loadKey = true
          console.log("reloaded")
        })
      },
      date_format(date) {
        return new Date(date).format("yy/MM/dd");
      },
      time_format(date) {
        return new Date(date).format("hh:mm");
      },
      commandHandler(command) {
        if (command === 'edit') {
          // this.editing[this.focus] = true
          let temp = this.deepClone(this.editing)
          temp[this.focus] = true
          this.editing = temp
        } else if (command === 'delete') {
          axios.post('/api/deleteTask?tno='+this.focus).then(res => {
            this.send()
          })
        }
      },
      gotoRevise(task) {
        this.$router.push({path: `/taskmark/${task.cno}/${task.tno}`})
      }
    }
  }
</script>

<style scoped>
  .wrapper {
    width: 83%;
    margin: auto;
    font-size: 14px;
    font-family: 'PingFang SC',tahoma,arial,'helvetica neue','hiragino sans gb','microsoft yahei ui','microsoft yahei',simsun,sans-serif;
  }
  .info-top {
    margin-top: 105px;
    width: 96%;
    height: 200px;
    background-image: linear-gradient(120deg, #a1c4fd 0%, #c2e9fb 100%);
    margin-bottom: 20px;
    padding: 48px 0 0 48px;
    border-radius: 8px;
  }
  .topm>h1 {
    font-size: 36px;
    color: #fff;
    font-weight: 500;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }
  .topm .sele {
    font-size: 15px;
    color: #fff;
    text-align: left;
    height: 24px;
    line-height: 24px;
    background-position: 100px 10px;
    background-size: 14px auto;
    cursor: pointer;
    user-select: none;
    background: rgba(255,255,255,.4);
    border-radius: 2px;
    padding-right: 10px;
    padding-left: 10px;
    display: inline-block;
  }
  .topm .sele+ul li {
    cursor: pointer;
  }
  .infotextmain {
    display: inline-block;
    color: #fff;
    font-size: 12px;
    height: 24px;
    line-height: 24px;
  }
  .topm .box-box {
    height: 35px;
    margin: 20px auto 0;
    color: #fff;
  }
  .topm ul {
    display: inline-block;
  }
  .topm ul li {
    margin-left: 10px;
    position: relative;
    background: rgba(255,255,255,.4);
    text-align: center;
    padding-left: 10px;
    padding-right: 10px;
    display: inline-block;
    height: 24px;
    line-height: 24px;
  }

  .coursedatabox .coursedata {
    margin-right: 30px;
  }

  .coursedatabox .coursedata ul {
    margin-top: -50px;
  }

  .coursedatabox .coursedata ul li:first-of-type {
    font-size: 40px;
    line-height: 48px;
    height: 50px;
  }
  .coursedatabox .coursedata ul li {
    background: none;
  }

  .coursedatabox .coursedata ul li+li {
    font-size: 12px;
    line-height: 16px;
    width: 80px;
  }

  .codetext {
    display: inline-block;
    line-height: 24px;
    height: 24px;
  }

  .homework-box {
    padding: 0 20px 0 40px;
    border: 1px solid #E2E6ED;
    border-radius: 8px;
    background: #FFF;
    text-align: left;
    margin-top: 20px;
  }
  .work-type {
    text-align: left;
    color:#5F6368;
    font-size: 14px;
    padding: 21px 0 10px;
    line-height: 30px;
  }
  .work-type span, .unique-bottom span {
    margin-right: 15px;
  }
  .work-type-first-span {
    background-color: #F1F3F4;
    padding: 3px 5px;
  }
  .unique-title {
    margin-bottom: 20px;
    margin-right: 340px;
  }
  .unique-word {
    font-size: 14px;
    height: 20px;
    vertical-align: middle;
    color: #707070;
  }
  .el-dropdown-link:hover {
    cursor: pointer;
  }
  .unique-count {
    float: right;
    display: flex;
    justify-content: space-between;
    text-align: center;
  }
  .unique-count span a {
    display: block;
    min-width: 80px;
    padding-left: 15px;
    color: #707070;
    padding-right: 15px;
  }
  .unique-count span a:first-child {
    font-size: 30px;
    height: 44px;
    line-height: 44px;
  }
  .unique-count span a:last-child {
    font-size: 14px;
    height: 26px;
    line-height: 26px;
  }
  .unique-bottom {
    color: #A0A0A0;
    font-size: 14px;
    margin-bottom: 20px;
  }
  .work-name {
    font-size: 20px;
    margin-bottom: 5px;
  }
  .work-deadline {
    margin-right: 40px;
    vertical-align: top;
  }

  .homework-box:hover .work-name {
    color: #4D90FE;
  }
</style>
