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
            <li><div @click="gotoCourseMember">同学: {{course.members}}</div></li>
            <li><div>成绩</div></li>
          </ul>
        </div>
      </div>
    </div>

    <div v-if="loadKey">
      <div v-for="task in tasks" :key="task.tno">
        <div class="homework-box">
          <div class="homework-inner">
            <div class="unique-top">
              <div class="work-type">
                <span class="work-type-first-span">个人作业</span>
                <span>{{ date_format(task.createDate) }}</span>
                <span>{{ time_format(task.createDate) }}</span>
              </div>
            </div>
            <div class="middle">
              <div class="unique-count">
                <span class="score">
                  <el-button @click="gotoTask(task)" type="primary">提交作业</el-button>
                </span>
              </div>
              <div class="unique-title">
                <el-link @click="gotoTask(task)" class="work-name">{{ task.tname }}</el-link>
                <div>
                  <span class="unique-word" v-html="task.description"></span>
                  <div class="annex-boxes" v-if="task.annex !== '' && task.annex !== null">
                    <div
                      v-for="item in annex_format(task.annex)"
                      class="annex-box"
                      @click="download_annex(item.name)">
                      <img :src="getIconSource(item.name.substring(10))" :alt="item.name.substring(10)" height="64px" width="64px"/>
                      <div :align="'center'" class="annex-label">{{ item.name.substring(10) }}</div>
                      <el-link href="javascript:;" class="annex-download" type="primary" :underline="false">下载</el-link>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="unique-bottom">
              <span class="work-deadline">
                <i class="el-icon-time"></i>截止日期：
                <span>{{ date_format(task.endDate) }}</span>
                <span>{{ time_format(task.endDate) }}</span>
              </span>
              <el-link href="#" class="work-commend">0条讨论</el-link>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import WorkNav from 'components/content/WorkNav'
import { request } from '../../network/request'
import axios from 'axios'

export default {
  name: 'StudentTask',
  components: {
    WorkNav
  },
  data() {
    return {
      course: {},
      tasks: [],
      loadKey: true
    }
  },
  mounted() {
    const taskConfig = request({
      url: '/api/taskList',
      method: 'get',
      params: {
        cno: this.$route.params.cno
      }
    })

    const courseConfig = request({
        url: '/api/course/getCourseByCno',
        method: 'get',
        params: {
          cno: this.$route.params.cno
        }
      })

      axios.all([courseConfig, taskConfig])
      .then(axios.spread((res1, res2) => {
        this.course = res1.data
        this.tasks = res2.data
      }))
  },
  methods: {
    date_format(date) {
      return new Date(date).format("yy/MM/dd");
    },
    time_format(date) {
      return new Date(date).format("hh:mm");
    },
    gotoTask(task) {
      this.$router.push({path: `/studenttaskcommit/${task.cno}/${task.tno}`})
    },
    gotoCourseMember() {
      this.$router.push(`/coursemember/${this.course.cno}`)
    },
    annex_format(annex) {
      if (annex === null) {
        return []
      }
      var temp = annex.split('?')
      var annex_temp = []
      temp.forEach(item => {
        annex_temp.push({
          name: item,
        })
      })
      return annex_temp
    },
    getIconSource(fileName) {
      let temp = fileName.split('.')
      try {
        return require('../../assets/fileicons/' + temp[temp.length - 1] + '.png')
      }catch (e) {
        return require('../../assets/fileicons/txt.png')
      }
    },
    download_annex(fileName) {
      let anchor = document.createElement("a");
      anchor.setAttribute("href", "/static/annex/" + fileName);
      anchor.setAttribute("download", fileName.substring(10));
      anchor.click();
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
    padding-top: 105px;
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
    margin-bottom: 30px;
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

  .annex-boxes {
    height: 100px;
  }
  .annex-box {
    float: left;
    text-align: center;
    height: 100px;
    width: 90px;
  }
  .annex-box:hover {
    cursor: pointer;
  }
  .annex-label {
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
  }
  .annex-download {
    display: none;
  }
  .annex-box:hover .annex-download {
    display: block;
  }
</style>