<template>
  <div class="wrapper">
    <task-commit-nav  
      :cname="cname" 
      :cno="$route.params.cno" 
      :tno="$route.params.tno"
      @back="back" />
    <el-table
      :data="gradeData"
      style="width: 100%; padding-top: 100px"
      @selection-change="handleSelectionChange">
      <el-table-column
        type="selection"
        width="55"
        align="center">
      </el-table-column>
      <el-table-column
        type="expand"
        width="100px">
        <template slot="header" slot-scope="scope">
          <el-dropdown
            trigger="click"
            @command="give_mark_command">
            <span class="el-dropdown-link">
              批量给分<i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="same">统一给分</el-dropdown-item>
              <el-dropdown-item command="area">区间给分</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
        <template slot-scope="props">
          <el-form label-position="left" inline class="demo-table-expand">
            <el-form-item label="学生答案">
              <span>{{ props.row.answer }}</span>
            </el-form-item>
          </el-form>
        </template>
      </el-table-column>
      <el-table-column
        label="学号"
        prop="user.identifyNo">
      </el-table-column>
      <el-table-column
        label="姓名"
        prop="user.name">
      </el-table-column>
      <el-table-column
        label="成绩">
        <template v-slot="scope">
          <el-input
            class="score-input"
            v-show="scope.row.isShow"
            v-model="scope.row.score"
            size="mini"
            @blur="score_in_handler(scope.row)" >
          </el-input>
          <span v-show="!scope.row.isShow" class="score-in" @click="showInput(scope.row)">{{ scope.row.score }}</span>
          /100
        </template>
      </el-table-column>
      <el-table-column
        label="批改状态">
        <template v-slot="scope">
          <span>{{ scope.row.status === 0 ? "未批改":"已批改" }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="操作">
        <template v-slot="scope">
          <el-link type="primary" @click="into_judge(scope.row.user.identifyNo)">进入批阅</el-link>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog
      title="学生答案"
      :visible.sync="markModalVisible">
      <div class="mark-modal">
        <el-carousel ref="markCarousel" :autoplay="false" trigger="click" height="300px">
          <el-carousel-item v-for="item in gradeData" :key="item.user.userId" :name="item.user.identifyNo">
            <div class="modal-answer">
              <div class="score-modal-wrapper">
                <span>成绩：</span>
                <el-input
                  class="score-modal"
                  v-model="item.score"
                  :id="item.user.userId"
                  size="mini"
                  @blur="score_in_handler(item)">
                </el-input>
                <span>/100</span>
              </div>
              <div>学生{{ item.user.name }}的答案：</div>
              <div class="answer-box">{{ item.answer }}</div>
            </div>
          </el-carousel-item>
        </el-carousel>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="markModalVisible = false">取 消</el-button>
        <el-button type="primary" @click="markModalVisible = false">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog
      title="统一给分"
      :visible.sync="sameMarkVisible"
      width="30%">
      <span>统一给分值：</span>
      <el-input
        class="mark-input"
        v-model="sameMark"
        size="mini">
      </el-input>
      <span slot="footer" class="dialog-footer">
        <el-button @click="sameMarkVisible = false">取 消</el-button>
        <el-button type="primary" @click="sameMarkConfirm">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog
      title="区间随机给分"
      :visible.sync="areaMarkVisible"
      width="30%">
      <span>区间随机给分值：</span>
      <el-input
        class="mark-input"
        v-model="areaMarkForward"
        size="mini">
      </el-input>
      <span>到</span>
      <el-input
        class="mark-input"
        v-model="areaMarkBackward"
        size="mini">
      </el-input>
      <span slot="footer" class="dialog-footer">
        <el-button @click="areaMarkVisible = false">取 消</el-button>
        <el-button type="primary" @click="areaMarkConfirm">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>
 
<script>
import axios from 'axios'
import {request} from '../../network/request'
import TaskCommitNav from 'components/content/TaskCommitNav'

export default {
  name: 'TaskMark',
  components: {
    TaskCommitNav
  },
  data() {
    return {
      markModalVisible: false,
      sameMarkVisible:false,
      sameMark:0,
      areaMarkVisible:false,
      areaMarkForward:0,
      areaMarkBackward:100,
      multipleSelection: [],
      gradeData: [],
      cname: ''
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

    const gradeConfig = request({
      url: '/api/grade/getGradeList',
      method: 'get',
      params: {
        tno: this.$route.params.tno
      }
    })

    axios.all([courseConfig, gradeConfig])
    .then(axios.spread((res1, res2) => {
      this.cname = res1.data.name
      this.gradeData = res2.data
    })).catch(err => {
      console.log(err)
    })
  },
  methods: {
    showInput:function(grade_row) {
      this.$set(grade_row, 'isShow', true)
    },
    score_in_handler:function(row) {
      request({
        url: '/api/grade/revise',
        method: 'put',
        params: {
          tno: row.task.tno,
          score: row.score,
          uid: row.user.userId
        }
      }).then(res => {
        this.$set(row, 'isShow', false)
        row.status = 1
        this.$message({
          message: "成绩已成功上传",
          type: 'success'
        })
      }).catch(err => {
        console.log(err)
        this.$set(row, 'isShow', false)
        this.$message({
          message: "成绩上传失败，请检查网络或联系管理员",
          type: 'error'
        })
      })
    },
    into_judge:function (i_no) {
      this.markModalVisible = true
      setTimeout( () => {
        this.$refs.markCarousel.setActiveItem(i_no)
      },0)
    },
    give_mark_command: function (command) {
      if (this.multipleSelection.length === 0) {
        this.$message({
          message: '请批量选择学生',
          type: 'warning'
        })
        this.sameMarkVisible = false
        return
      }
      switch (command) {
        case 'same':
          this.sameMarkVisible = true
          break
        case 'area':
          this.areaMarkVisible = true
          break
        default:
      }
    },
    sameMarkConfirm: function () {
      const data = {
        tno: this.multipleSelection[0].task.tno,
        member: [],
        status:1,
        score: this.sameMark
      }
      this.multipleSelection.forEach(item => {
        data.member[data.member.length] = item.user.userId
      })
      request({
        url: `/api/grade/reviseMany?tno=${data.tno}&score=${data.score}`,
        method: 'post',
        data: data.member
      }).then(res => {
        if (res.data) {
          this.multipleSelection.forEach(item => {
            item.score = this.sameMark
            item.status = 1
          })
          this.$message({
            message:'成绩上传成功',
            type: 'success'
          })
        } else {
          this.$message({
            message:'成绩上传失败，请检查网络或联系管理员',
            type: 'error'
          })
        }
      }).catch(err => {
        this.$message({
          message:'成绩上传失败，请检查网络或联系管理员',
          type: 'error'
        })
      })
      this.sameMarkVisible = false
    },
    areaMarkConfirm: function() {
      this.areaMarkForward = eval(this.areaMarkForward)
      this.areaMarkBackward= eval(this.areaMarkBackward)
      var data = {
        tno:this.multipleSelection[0].task.tno,
        member:[],
        status:1,
        score:[]
      }
      if (this.areaMarkForward > this.areaMarkBackward) {
        this.$message({
          message: '随机分数上限不能小于下限',
          type: 'warning'
        })
        return
      }
      if (this.areaMarkBackward > 100) {
        this.$message({
          message: '随机分数上限不能大于满分100',
          type: 'warning'
        })
        return
      }
      this.multipleSelection.forEach(item => {
        data.member[data.member.length] = item.user.userId
        data.score[data.score.length] = Math.floor(Math.random()*(this.areaMarkBackward - this.areaMarkForward + 1)) + this.areaMarkForward
      })
      axios.post('/api/grade/reviseMulti', data).then(res => {
        if (res.data) {
          for (let i = 0; i < this.multipleSelection.length; i++) {
            this.multipleSelection[i].score = data.score[i]
          }
          this.$message({
            message:'成绩上传成功',
            type: 'success'
          })
        } else {
          this.$message({
            message:'成绩上传失败，请检查网络或联系管理员',
            type: 'error'
          })
        }
        
      }).catch(err => {
        this.$message({
          message:'成绩上传失败，请检查网络或联系管理员',
          type: 'error'
        })
      })
      this.areaMarkVisible = false
    },
    handleSelectionChange: function (val) {
      this.multipleSelection = val
      console.log(val)
    },
    back() {
      this.$router.push(`/teachertask/${this.$route.params.cno}`)
    }
  }
}
</script>

<style scoped>
  .wrapper {
    position: relative;
    width: 83%;
    margin: auto;
    font-size: 14px;
    font-family: 'PingFang SC',tahoma,arial,'helvetica neue','hiragino sans gb','microsoft yahei ui','microsoft yahei',simsun,sans-serif;
  }

  .demo-table-expand {
    font-size: 0;
  }
  .demo-table-expand label {
    width: 90px;
    color: #99a9bf;
  }
  .demo-table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 50%;
  }
  .el-dropdown-link {
    cursor: pointer;
    color: #409EFF;
  }
  .el-dropdown-link:hover {
    color: #4d90fe;
  }
  .el-icon-arrow-down {
    font-size: 12px;
  }
  .score-input {
    width: 60px;
  }
  .score-in {
    display: inline-block;
    border-bottom: 1px #818181 solid;
    width: 60px;
    text-align: center;
    font-size: 18px;
  }
  .mark-modal {
    height: 300px;
  }
  .score-modal-wrapper {
    float: right;
    width: 131px;
  }
  .score-modal, .mark-input {
    width: 60px;
  }
  .modal-answer {
    padding: 20px 60px;
  }
  .answer-box {
    padding: 20px;
    text-indent: 20px;
    border: 1px solid gold;
    border-radius: 8px;
    margin-top: 20px;
  }
  .el-carousel__item {
    background-color: #99a9bf;
  }
</style>