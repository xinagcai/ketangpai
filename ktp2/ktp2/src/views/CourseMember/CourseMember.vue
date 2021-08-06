<template>
  <div class="outer">
    <course-member-nav :cname="cname" :cno="$route.params.cno" @back="back" />

    <div class="content">
      <div class="lstyle">
        <div>
          <ul>
            <li :class="{active:isActive===1}" @click="change(1)">教学团队（{{teaCount}}）</li>
            <li :class="{active:isActive===2}" @click="change(2)" style="border-radius: 0%;">全部学生（学生{{stuCount}}）</li>
          </ul>
        </div>
      </div>
      <div class="rstyle">
        <div class="members">
          <div class="tittle">
            <span :class="{hide:isActive!=1}">教学团队（老师{{teaCount}}）</span>
            <span :class="{hide:isActive!=2}">全部学生（学生{{stuCount}}）</span>
          </div>
          <div class="tittle" v-if="$store.state.permission == '2'" :class="{hide:isActive!=2}" style="background-color: white;">
            <span>
              <el-checkbox @change="selectAll()" style="margin-right: 30px;" v-model="checked">全选 已选（{{multipleSelection.length}}）人
              </el-checkbox>
              <el-button type="primary" :disabled="multipleSelection.length == 0" @click=" delAll">删除成员</el-button>
            </span>
          </div>
          <ul :class="{hide:isActive!=1}">
            <li v-for="(item,index) in teachers">
              <p class="tname">
                <span style="margin-left: 24px;margin-top: 20px;" :title="item.name">{{item.name}}</span>
              </p>
              <p class="mail" title="item.userId">{{item.userId}}</p>
              <span style="line-height: 60px;">（{{item.identifyNo}}）</span>
            </li>
          </ul>
          <ul :class="{hide:isActive!=2}">
            <li class="stu-list" v-for="(item,index) in students">
              <el-checkbox v-if="$store.state.permission == '2'" v-model="checkStatus[index]" style="line-height: 55px;margin-right: 30px;"
                           @change.stop.native="mutiplySelectionMethod(item, index)">
              </el-checkbox>
              <p class="sname">
                <span style="margin-left: 24px;margin-top: 20px;" :title="item.name">{{item.name}}</span>
              </p>
              <p style="margin-left: 50px;margin-top: 20px;" :title="item.userId">{{item.userId}}</p>
              <p class="mail" title="item.identifyNo">{{item.identifyNo}}</p>
              <p v-if="$store.state.permission == '2'" @click="delHandle(index)" class="delete" style="line-height: 60px;color:#32BAF0;">删除</p>
            </li>


          </ul>
          <!-- 删除提示框 -->
          <el-dialog title="提示" :visible.sync="delVisible" width="300px" center>
            <div class="del-dialog-cnt">删除不可恢复，是否确定删除？</div>
            <span slot="footer" class="dialog-footer">
              <el-button @click="cancle">取 消</el-button>
              <el-button type="primary" @click="deleteRow">确 定</el-button>
            </span>
          </el-dialog>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import {request} from '../../network/request'
  import CourseMemberNav from 'components/content/CourseMemberNav'
  import axios from 'axios'


  export default {
    name: "CourseMember",
    components: {
      CourseMemberNav
    },
    data() {
      return {
        teachers: [],
        students: [],
        stuCount: 0,
        teaCount: 0,
        isActive: 1,
        checked: false,
        delVisible: false, //删除框状态
        delarr: [], //删除数据
        multipleSelection: [], //多选数据
        checkStatus: [],
        cname: ''
      };
    },
    mounted() {
      const courseConfig = request({
        url: '/api/course/getCourseByCno',
        method: 'get',
        params: {
          cno: this.$route.params.cno
        }
      })

      const userConfig = request({
        url: '/api/userList',
        method: 'get',
        params: {
          cno: this.$route.params.cno
        }
      })

      axios.all([courseConfig, userConfig])
      .then(axios.spread((res1, res2) => {
        this.cname = res1.data.name

        this.students = res2.data.filter(e => e.permission.pid == '1')
        this.teachers = res2.data.filter(e => e.permission.pid == '2')
        this.stuCount = this.students.length
        this.teaCount = this.teachers.length
        // 初始化
        this.checkStatus = this.students.map(e => {
          return false
        })
      })).catch(err => {console.log(err)})

    },
    methods: {
      change(num) {
        this.isActive = num
      },

      //单删
      delHandle(index) {
        this.delVisible = true
        this.delarr.push(this.students[index])
      },
      //多删
      delAll() {
        this.delVisible = true
        this.delarr = this.multipleSelection
        this.multipleSelection = []
      },
      //多选
      mutiplySelectionMethod(item, index) {
        for (let item of this.checkStatus) {
          if (item == false) {
            this.checked = false
            break
          }
          this.checked = true
        }
        if (this.multipleSelection.indexOf(item) == -1)
          this.multipleSelection.push(item)
        else
          this.multipleSelection.remove(item)
      },
      //全选
      selectAll() {
        for (let i in this.checkStatus) {
          this.checkStatus[i] = this.checked
        }
        if(this.checked == true) {
          this.multipleSelection = [...this.students];
        }else {
          this.multipleSelection =[]
        }

      },
      clearSelect() {
        this.checkStatus = this.students.map(e => false)
      },
      deleteRow() {
        // 删除在删除数组中的所有数据
        // 取差集
        request({
          url: '/api/course/deleteMembers?cno=' + this.$route.params.cno,
          method: 'post',
          data: this.delarr
        }).then(res => {
          if (res.data) {
            this.students = this.students.filter(v => this.delarr.indexOf(v) == -1)
            this.delarr = []
            this.stuCount = this.students.length
            this.delVisible = false
            this.clearSelect()
          }
        },
        )

      },
      back() {
        if (this.$store.state.permission == '2') {
          this.$router.push(`/teachertask/${this.$route.params.cno}`)
        } else {
          this.$router.push(`/studenttask/${thios.$route.params.cno}`)
        }
      },

      //取消操作
      cancle(){
        this.delarr = []
        this.multipleSelection = []
        this.delVisible = false
        this.clearSelect()
      }
    },

  };

  Array.prototype.remove = function (val) {
    var index = this.indexOf(val);
    if (index > -1) {
      this.splice(index, 1);
    }
  }
</script>

<style scoped>
  .outer {
    width: 1224px;
    padding-top: 112px;
    padding-bottom: 60px;
    margin-left: auto;
    margin-right: auto;
  }

  ul {
    margin: 0;
    padding: 0;
  }

  .content {
    background: #FFF;
    border: 1px solid rgba(226, 230, 237, 1);

    min-height: 600px;
    border-radius: 8px;
    position: relative;
    zoom: 1;
  }

  .lstyle {
    width: 286px;
    min-height: 300px;
    float: left;
  }

  .lstyle ul li {
    height: 55px;
    line-height: 55px;
    padding: 0 24px;
    cursor: pointer;
    font-weight: 500;
    color: rgba(59, 61, 69, 1);
    font-family: PingFangSC-Medium;
    font-size: 16px;
    border-radius: 8px 0 0;
    list-style: none;
  }

  .lstyle ul li:hover {
    background-color: #e4edfd;
  }

  .lstyle ul li.active {
    background: #32BAF0 !important;
    color: #FFF;
  }

  .rstyle {
    width: 936px;
    border-left: 1px solid rgba(226, 230, 237, 1);
    min-height: 600px;
    margin-left: -1px;
    float: left;
  }

  .members .tittle {
    padding: 0 12px 0 25px;
    height: 55px;
    background: rgba(241, 243, 244, 1);
    border-radius: 0 8px 0 0;
    line-height: 55px;
  }

  .members ul {
    padding-bottom: 60px;
    max-height: 430px;
    overflow-y: scroll;
    overflow-x: hidden;
  }

  .members ul li {
    height: 60px;
    padding: 0 25px;
    list-style: none;
  }

  .members ul li p.tname {
    width: 300px;
  }

  .members ul li p.sname {
    width: 150px;
  }

  .members ul li p {
    float: left;
    margin-right: 20px;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
  }

  .members ul li:hover {
    background: rgba(241, 243, 244, 1);
  }

  .tname img {
    width: 30px;
    height: 30px;
    border-radius: 50%;
    margin-top: 15px;
  }

  .sname img {
    width: 30px;
    height: 30px;
    border-radius: 50%;
    margin-top: 15px;
  }

  .members ul li p.tname span {
    display: inline-block;
    max-width: 200px;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
    vertical-align: top;
    margin-top: 20px;
  }

  .members ul li p.sname span {
    display: inline-block;
    max-width: 200px;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
    vertical-align: top;
    line-height: 20px;
  }

  .mail {
    min-width: 150px;
    max-width: 100px;
    margin-top: 20px;
  }

  .delete {
    cursor: pointer
  }

  li.tname > p.mail {
    width: 300px;
  }

  li.sname > p.mail {
    width: 300px;
  }

  .hide {
    display: none;
  }

  .el-pagination {
    padding: 2px 400px;
  }

  .el-checkbox {
    float: left;
  }

  .stu-list p {
    width: 150px;
  }
  .stu-list p:last-child {
    text-align: right;
  }
</style>
