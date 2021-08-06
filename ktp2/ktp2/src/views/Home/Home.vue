<template>
  <div class="outer">
    <home-nav 
      @clear-style="clearStyle"/>
    <!-- 首页分导航部分 -->
    <div class="course-nav">
      <div>
        <div @click="addCourseDialog = true"><span>+ 加入课程</span></div>
        <div @click="archiveDialog = true"><span>归档管理</span></div>
        <div v-if="permission == '2'" @click="cc('创建课程')"><span>+ 创建课程</span></div>
      </div>
      
    </div>
    <!-- 卡片部分 -->
    <div class="content">
      <div v-for="(course, index) in courses" 
          :key="course.cno">
        <home-card 
          :ref="'c' + course.cno"
          :course="course" 
          :index="index"
          @edit-course="editCourse"
          @delete-course="deleteCourse"
          @archive-course="archiveCourse"
          @drop-out="dropOut"
          @goto-task="gotoTask" />
      </div>
      
    </div>

    <!-- 弹框 -->
    <!-- 归档 -->
    <el-dialog title="归档管理" :visible.sync="archiveDialog">
      <div class="archivePage">
        <div v-for="item in archiveCourses" :key="item.cno">
          <div>
            <div style="font-size: 20px">{{item.name}}</div>
            <div style="font-size: 12px">角色: {{showPermission}}</div>
          </div>
          <div>
            <span @click="recover(item)">恢复</span>
            <span @click="deleteCourse(item, true)" v-if="permission === '2'">删除</span>
            <span @click="dropOut(item, true)" v-else>退课</span>
          </div>
        </div>
      </div>
    </el-dialog>

    <!-- 创建/加入 -->
    <el-dialog title="加入课程" :visible.sync="addCourseDialog" :width="'250px'">
      <el-input v-model="cno" placeholder="请输入课程号" class="input-cno" @input="changeAddDisable"></el-input>
      <div>
        <div><el-button round @click="cancel">取消</el-button></div>
        <div><el-button type="primary" round :disabled="inputNotNull" @click="addToACourse">加入</el-button></div>
      </div>
    </el-dialog>
    <el-dialog :title="course_select" :visible.sync="createCourseDialog">
      <div class="create">
        <div>
          <span>课程名称: </span><input v-model="course.cname" @input="changeCreateDisable" type="text" placeholder="请输入课程名称" />
        </div>
        <div>
          <span>选择学期: </span>
          <el-select @change="changeCreateDisable" v-model="course.year" placeholder="请选择">
            <el-option
              v-for="item in years"
              :key="item"
              :label="item"
              :value="item">
            </el-option>
          </el-select>
          <el-select @change="changeCreateDisable" v-model="course.semester" placeholder="请选择">
            <el-option
              v-for="item in semesters"
              :key="item.id"
              :label="item.label"
              :value="item.id">
            </el-option>
          </el-select>
        </div>
        <div>
          <el-button plain @click="cancel">取消</el-button>
          <el-button type="primary" plain :disabled="createNotNull" @click="addCourse">确定</el-button>
        </div>
      </div>
    </el-dialog>

  </div>
</template>

<script>
  import HomeNav from 'components/content/HomeNav'
  import HomeCard from 'views/Home/HomeCard'
  import {request} from '../../network/request'
  import axios from 'axios'

  export default {
    name: 'Home',
    data() {
      return {
        courses: [],
        permission: '',
        archiveDialog: false,
        addCourseDialog: false,
        createCourseDialog: false,
        cno: '',
        inputNotNull: true,
        createNotNull: true,
        years: ['2018-2019', '2019-2020', '2020-2021'],
        semesters: [{id: '1', label: '第一学期'}, {id: '2', label: '第二学期'}],
        course: {
          year: '',
          cnmae: '',
          semester: '',
          members: 0,
          admin: {userId: this.$store.state.uid, name: this.$store.state.name}
        },
        course_select: '',
        // 归档数组
        archiveCourses: [],
      }
    },
    computed: {
      showPermission() {
        return this.$store.permission === 1 ? '学生' : '教师'
      }
    },
    components: {
      HomeNav,
      HomeCard
    },
    mounted() {
      this.permission = this.$store.state.permission
      this.getCourses()
    },
    methods: {
      getCourses() {
        // 教师端和学生端请求课程
        const stCourse = request({
          url: '/api/course/getCourse',
          method: 'get',
          params: {
            uid: this.$store.state.uid
          }
        })
        // 教师端请求课程
        const tCourse = request({
          url: '/api/course/getCourses',
          method: 'get',
          params: {
            uid: this.$store.state.uid
          }
        })

        axios.all([stCourse, tCourse])
        .then(axios.spread((res1, res2) => {
          this.courses = [...res1.data, ...res2.data]
          this.archiveCourses = this.courses.filter(e => e.archiveStatus === 1)
          this.courses = this.courses.filter(e => e.archiveStatus === 0)
        }))
      },
      cc(title) {
        if (title === '创建课程') {
          this.cno = ''
          this.course.semester = ''
          this.course.cname = ''
          this.course.year = ''
        }
        this.course_select = title
        this.createCourseDialog = true
      },
      changeAddDisable() {
        this.inputNotNull = this.cno === ''
      },
      changeCreateDisable() {
        if (this.course.cname !== '' && this.course.year !== '' && this.course.semester !== '') {
          this.createNotNull = false
        } else {
          this.createNotNull = true
        }
      },
      cancel() {
        this.cno = ''
        this.addCourseDialog = false
        this.createCourseDialog = false
        this.course.semester = ''
        this.course.cname = ''
        this.course.year = ''
        this.archiveDialog = false
        this.changeAddDisable()
        this.changeCreateDisable()
      },
      addCourse() {
        if (this.course_select === '创建课程') {
          // 请求
          request({
            url: '/api/course/addCourse',
            method: 'post', 
            data: this.course
          }).then(res => {
            if (res.data === true) { 
              this.$message.success('添加成功')
              this.getCourses()
            } else {
              this.$message.error('添加失败')
            }

            this.cancel()
          }).catch(err => {
            console.log(err)
            this.cancel()
          })
        } else {
          request({
            url: '/api/course/updateCourse',
            method: 'put',
            params: {
              name: this.course.cname,
              year: this.course.year,
              semester: this.course.semester,
              cno: this.course.cno
            }
          }).then(res => {
            if (res.data === true) {
              this.$message.success('修改成功')
              this.getCourses()
            } else {
              this.$message.error('编辑失败')
            }

            this.cancel()
          }).catch(err => {
            console.log(err)
            this.cancel()
          })
        }
        
      },
      editCourse(course) {
        // 拷贝课程
        this.course = JSON.parse(JSON.stringify(course))
        this.course.cname = this.course.name
        delete this.course.name
        // create course
        this.cc('编辑课程')
      },
      deleteCourse(course, ext) {
        // 弹出弹框
        this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          // 通过异步删除
          request({
            url: '/api/course/updateCourse',
            method: 'put',
            params: {
              cno: course.cno,
              deleteStatus: 1
            }
          }).then(res => {
            // 确定操作
            if (res.data === true) {
              if (ext) {
              } else {
                // 清除事件，避免报错
                const xx = 'c' + course.cno
                this.$refs[xx][0].recoverStyle()
              }

              this.getCourses()

              this.$message({
                type: 'success',
                message: '删除成功!'
              });
            } else {
              this.$message({
                type: 'error',
                message: '删除出错'
              })
            }
          })
          
        }).catch(() => {
          // 取消操作
          this.$message({
            type: 'info',
            message: '已取消删除'
          })          
        })
        // end
      },
      archiveCourse(course) {
        this.$confirm('此操作将归档该课程, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          // 通过异步归档
          request({
            url: '/api/course/updateCourse',
            method: 'put',
            params: {
              cno: course.cno,
              archive: 1
            }
          }).then(res => {
            // 确定操作
            if (res.data === true) {
              // 清除事件，避免报错
              const xx = 'c' + course.cno
              this.$refs[xx][0].recoverStyle()

              this.getCourses()
            } else {
              this.$message({
                type: 'error',
                message: '归档出错'
              })
            }
          })
          
        }).catch(() => {
          // 取消操作
          this.$message({
            type: 'info',
            message: '已取消归档'
          })          
        })
        // end
      },
      recover(course) {
        let requestBody = {}
        if (this.permission === '2') {
          requestBody = {
            url: '/api/course/updateCourse',
            method: 'put',
            params: {
              cno: course.cno,
              archive: 0
            }
          }
        } else {
          requestBody = {
            url: '/api/course/changeStatus',
            method: 'put',
            params: {
              cno: course.cno,
              uid: this.$store.state.uid,
              status: '0'
            }
          }
        }

        this.$confirm('此操作该课程恢复, 是否继续?', '要恢复此课程么?', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          // 通过异步恢复
          request(requestBody).then(res => {
            // 确定操作
            if (res.data === true) {
              this.getCourses()
            } else {
              this.$message({
                type: 'error',
                message: '恢复出错'
              })
            }
          })
        })
        // end
        this.cancel()
      },
      dropOut(course, ext) {
        // 退课
        this.$confirm('此操作会将本课程退了, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          // 通过退课归档
          request({
            url: '/api/course/changeStatus',
            method: 'put',
            params: {
              cno: course.cno,
              uid: this.$store.state.uid,
              status: '2'
            }
          }).then(res => {
            // 确定操作
            if (res.data === true) {
              if (ext) {
              } else {
                // 清除事件，避免报错
                const xx = 'c' + course.cno
                this.$refs[xx][0].recoverStyle()

                this.getCourses()
              }
              

            } else {
              this.$message({
                type: 'error',
                message: '退课出错'
              })
            }
          })
          
        }).catch(() => {
          // 取消操作
          this.$message({
            type: 'info',
            message: '已取消退课'
          })          
        })
        // end
      },
      addToACourse() {
        console.log(this.$store.state.uid)

        request({
          url: '/api/course/addMember',
          method: 'put',
          params: {
            cno: this.cno,
            uid: this.$store.state.uid
          }
        }).then(res => {
          if (res.data) {
            this.getCourses()
          } else {
            this.$message.error('未找到相应的课程')
          }
        }).catch(err => {
          console.log(err)
        })

        this.cancel()
      },
      clearStyle() {
        for (let course of this.courses) {
          // 清除事件，避免报错
          const xx = 'c' + course.cno
          this.$refs[xx][0].recoverStyle()
        }
      },
      gotoTask(course) {
        this.clearStyle()
        if (this.permission == '2') {
          this.$router.push({path: `/teachertask/${course.cno}`})
        } else {
          this.$router.push({path: `/studenttask/${course.cno}`})
        }
        
      }
    }
  }
</script>

<style scoped>
  .outer {
    height: 100%;
  }

  .course-nav {
    padding-top: 100px;
    position: relative;
  }
  .course-nav>div {
    width: 90%;
    margin: 0 auto;
    height: 40px;
    line-height: 40px;
    padding-bottom: 12px;
    box-shadow: 0 1px 0 0 rgba(226,230,237,1);
    margin-bottom: 20px;
  }
  .course-nav>div>div:first-child {
    float: right;
    height: 26px;
    line-height: 26px;
    cursor: pointer;
    border-radius: 4px;
    background: rgba(50,186,240,1);
    text-align: center;
    font-size: 14px;
    color: rgba(255,255,255,1);
    padding: 5px 15px;
  }
  .course-nav>div>div:nth-child(2) {
    float: right;
    height: 26px;
    line-height: 26px;
    cursor: pointer;
    border-radius: 4px;
    background: rgba(50,186,240,1);
    text-align: center;
    font-size: 14px;
    color: rgba(255,255,255,1);
    padding: 5px 15px;
    margin-right: 10px;
  }
  .course-nav>div>div:nth-child(3) {
    float: right;
    height: 26px;
    line-height: 26px;
    cursor: pointer;
    border-radius: 4px;
    background: rgba(50,186,240,1);
    text-align: center;
    font-size: 14px;
    color: rgba(255,255,255,1);
    padding: 5px 15px;
    margin-right: 10px;
  }

  .content {
    width: 90%; 
    margin: 0 auto;
  }

  .archivePage {
    max-height: 300px;
    overflow-y: scroll;
    overflow-x: hidden;
  }
  /* 滑动槽 */
  .archivePage::-webkit-scrollbar {
    width: 6px;
    height: 6px;
  }
  /* 滑动槽背景 */
  .archivePage::-webkit-scrollbar-track {
    border-radius: 3px;
    background: rgba(255, 255, 255, .6);
  }
  /* 滚动条 */
  .archivePage::-webkit-scrollbar-thumb {
    border-radius: 3px;
    background: rgba(126, 214, 223, .6);
    box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.08);
  }

  .archivePage>div>div:first-child {
    display: flex;
    flex-direction: column;
    color: #fff;
  }
  .archivePage>div {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    background: #feca57;
    height: 72px;
    padding: 24px 16px 14px 16px;
  }
  .archivePage>div>div:first-child>div:first-child {
    margin-bottom: 10px;
    margin-top: 5px;
  }
  .archivePage>div:not(:last-child) {
    margin-bottom: 10px;
  }
  .archivePage>div>div:last-child>span {
    margin-right: 10px;
    cursor: pointer;
    color: #ff6b6b;
    line-height: 72px;
  }
  .input-cno {
    margin-bottom: 10px;
  }
  .input-cno~div {
    width: 80%;
    margin: 0 auto;
    display: flex;
    flex-direction: row;
    justify-content: space-between;
  }

  .create>div:first-child>input {
    display: inline-block;
    vertical-align: middle;
    height: 38px;
    width: 260px;
    font-size: 14px;
    outline: 0;
    border: none;
    background: 0 0;
    cursor: text;
    margin-left: 10px;
  }
  .create>div:nth-child(2)>.el-select {
    margin-right: 10px;
    margin-left: 10px;
  }
  .create>div:nth-child(3) {
    margin-top: 20px;
    margin-right: 40px;
    text-align: right;
  }

  .content>div {
    float: left;
    width: 300px;
    padding-bottom: 30px;
  }
  .content>div:after {
    content: '';
    clear: both;
  }
</style>