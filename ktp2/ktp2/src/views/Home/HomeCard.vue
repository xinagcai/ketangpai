<template>
  <div class="card">
    <div class="title">
      <!-- 课程名称 -->
      <div><span @click="gotoTask">{{course.name}}</span></div>
      <!-- 加课吗和学期 -->
      <div>
        <div>加课码: {{course.cno}}</div>
        <div>
          <div>{{course.year}}</div>
          <div>{{showSemester}}</div>
        </div>
      </div>
    </div>

    <div class="homework">
      <div>近期作业</div>
      <div>无</div>
    </div>

    <div class="bottom">
      <div>成员{{memberNum}}人</div>
      <div @click.stop="more" class="prop-parent">
        更多
        <!-- 弹窗 -->
        <div class="prop" v-if="course.admin.userId == this.$store.state.uid">
          <div @click="editCourse">编辑</div>
          <div @click="deleteCourse">删除</div>
          <div @click="archiveCourse">归档</div>
        </div>
        <div class="prop" v-else>
          <div @click="deleteCourseMember">退课</div>
        </div>
      </div>   
    </div>
  </div>  
</template>

<script>
import {request} from '../../network/request'

export default {
  name: 'HomeCard',
  props: {
    course: {
      type: Object,
      required: true
    },
    index: {
      type: Number,
      required: true
    }
  },
  data() {
    return {
      works: [],
      memberNum: 0,
      addEvent: (e) => {
          if (!this.$el.contains(e.target)) {
          document.querySelector(`.prop${this.index+1}`).style.display = "none"
        }
      }
    }
  },
  computed: {
    showSemester() {
      return this.course.semester === "1" ? '第一学期' : '第二学期'
    }
  },
  mounted() {
    document.querySelector("div.prop-parent").setAttribute("class", `prop-parent${this.index+1}`)
    document.querySelector(`.prop-parent${this.index+1}>.prop`).setAttribute("class", `prop prop${this.index+1}`)

    // 事件监听器
    window.addEventListener('click', this.addEvent, false)

    // 请求成员数量，只包含学生
    request({
      url: '/api/course/getMembers',
      method: 'get',
      params: {
        cno: this.course.cno
      }
    }).then(res => {
      this.memberNum = res.data
    }).catch(err => {
      console.log(err)
    })
  },
  methods: {
    more() {
      document.querySelector(`.prop${this.index+1}`).style.display = "block"
    },
    editCourse() {
      this.$emit('edit-course', this.course)
    },
    deleteCourse() {
      this.$emit('delete-course', this.course)
    },
    archiveCourse() {
      this.$emit('archive-course', this.course)
    },
    recoverStyle() {
      // 还原事件，避免前端找不到事件而报差错

      // 事件监听器
      window.removeEventListener('click', this.addEvent, false)

      // 还原
      document.querySelector(`div.prop${this.index+1}`).setAttribute("class", `prop`)
      document.querySelector(`div.prop-parent${this.index+1}`).setAttribute("class", `prop-parent`)
    },
    deleteCourseMember() {
      this.$emit('drop-out', this.course)
    },
    gotoTask() {
      this.$emit('goto-task', this.course)
    }
  }
}
</script>

<style scoped>
  .card {
    width: 270px;
    background: #fff;
    border: 1px solid #e2e6ed;
    border-radius: 8px;
    position: relative;
  }

  /* 标题部分 */
  .title {
    width: 100%;
    height: 95px;
    padding: 18px 0 14px 0;
    background: #48dbfb;
    border-radius: 8px 8px 0 0;
    display: flex;
    flex-direction: column;
    color: #fff;
  }
  .title>div:first-child {
    margin-left: 18px;
    margin-right: 48px;
    font-size: 20px;
    
  }
  .title>div:first-child>span {
    border-bottom: 1px solid rgba(255, 255, 255, 0);
    cursor: pointer;
  }
  .title>div:first-child>span:hover {
    border-bottom: 1px solid rgba(255, 255, 255, 1);
  }
  .title>div:last-child {
    font-family: PingFang-SC-Medium;
    text-align: center;
    margin-left: 18px;
    margin-right: 18px;
    padding-left: 2px;
    padding-right: 4px;
    margin-top: 13px;
  }
  .title>div:last-child>div:first-child {
    float: left;
    margin-top: 40px;
    font-size: 14px;
  }
  .title>div:last-child>div:last-child {
    margin-top: 30px;
    float: right;
    color: rgba(255, 255, 255, .8);
    font-size: 12px;
  }

  /* 近期作业显示情况 */
  .homework {
    margin-top: 5px;
    padding: 0 16px;
    height: 105px;
    border-bottom: 1px solid rgba(226, 230, 237, 1);
  }
  .homework>div {
    height: 30px;
    line-height: 30px;
    font-size: 14px;
  }
  .homework>div:first-child {
    font-weight: 500;
    color: rgba(31,32,35,.7);
  }

  .bottom {
    height: 35px;
    line-height: 35px;
    font-size: 12px;
  }
  .bottom>div:first-child {
    float: left;
    margin-left: 20px;
    cursor: pointer;
  }
  .bottom>div:last-child {
    position: relative;
    float: right;
    margin-right: 10px;
    cursor: pointer;
    color: #0abde3;
  }

  .prop {
    width: 128px;
    position: absolute;
    background: #fff;
    box-shadow: 1px 2px 3px rgba(0,0,0,.2);
    right: 10px;
    bottom: 30px;
    padding: 5px 0;
    display: none;
  }
  .prop>div {
    width: 100%;
    height: 30px;
    line-height: 30px;
    text-align: center;
  }
  .prop>div:hover {
    background: rgba(0,0,0,.1);
  }
</style>