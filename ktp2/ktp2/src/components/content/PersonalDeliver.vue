<template>
  <div class="personal-deliver">
    <form id="personal_form">
      <el-input v-model="tname" placeholder="作业名称"></el-input>
      <Editor class="editor" :description="description" @description="upDescription"/>
      <el-upload
        class="annex-upload"
        multiple
        action="/api/annex/upload"
        :file-list="annex"
        :on-success="annexUploadSuccess"
        :on-error="annexUploadError"
        :before-remove="annexBeforeDelete">
        <el-button type="primary" class="annex-button"><i class="el-icon-paperclip el-icon--left"/>上传附件</el-button>
      </el-upload>
      <div class="form-line">
        <span class="form-span">截止日期：</span>
        <el-date-picker
          v-model="endDate"
          type="date"
          placeholder="选择日期"
          value-format="yyyy-MM-dd"
          :clearable="false">
        </el-date-picker>
        <el-time-select v-model="end_time" :picker-options="{
              start: '00:00',
              step: '00:15',
              end: '23:45'
            }" placeholder="选择时间" v-if="endDate !== ''" style="margin-left: 20px;">
        </el-time-select>
        <el-switch
          v-model="outdate"
          v-if="endDate !== ''"
          active-text="若超时，禁止提交"
          style="margin-left: 50px">
        </el-switch>
      </div>
      <div class="form-line">
        <span class="form-span">满分值：</span>
        <el-input v-model="max_point" style="width: 60px"></el-input>
      </div>
      <div class="form-line">
        <span class="form-span" style="line-height: 40px;height: 40px">是否查重：</span>
        <el-switch
          v-model="check_dump">
        </el-switch>
        <span v-if="check_dump === true">
          <span style="margin-left: 20px;color: #a0a0a0">查重禁戒值：</span>
          <el-input v-model="alarm_line" style="width: 60px;margin-left: 10px;"></el-input>
          <span style="text-align: right;width: 20px;display: inline-block">%</span>
        </span>
        <div style="margin-top: 15px;" v-if="check_dump === true">
          <span class="form-span"></span>
          <el-checkbox v-model="auto_alarm"></el-checkbox>
          <span v-if="check_dump === true">
            <span style="margin-left: 20px;color: #a0a0a0">查重率高于</span>
            <el-input v-model="alarm_line" style="width: 60px;margin-left: 10px;"></el-input>
            <span style="text-align: right;width: 80px;display: inline-block">%自动打回</span>
          </span>
        </div>
      </div>
      <div class="form-line">
        <el-button type="primary" style="float: left"><i class="el-icon-download el-icon--left"/>导入作业</el-button>
        <span style="float: right;display: flex;justify-content: space-between">
          <el-button type="primary" @click="cancel" style="float: left">取消</el-button>
          <el-button type="primary" @click="submit" style="float: left">{{ edit ? "保存":"发布个人作业" }}</el-button>
        </span>
      </div>
    </form>
  </div>
</template>

<script>
  import axios from "axios";
  import Editor from "@/components/content/Editor";

    export default {
      name: "PersonalDeliver",
      components: {Editor},
      props: {
        edit: {
          type:Boolean,
          default:false
        },
        task: {
          type:Object
        },
        cno: {
          type: String
        }
      },
      data() {
        return {
          tname: '',
          description: this.task === undefined ? '':this.task.description,
          endDate: '',
          end_time: '',
          annex: [],
          annex_map: [],
          outdate: false,
          max_point: '',
          check_dump: false,
          alarm_line: '',
          auto_alarm: false,
        }
      },
      mounted() {
        if (this.edit) {
          this.putUp()
        }
      },
      methods: {
        cancel() {
          this.$emit('deliver',0)
        },
        submit() {
          const data = {
            tno: this.task === undefined ? '' : this.task.tno,
            tname: this.tname,
            description: this.description,
            annex: this.form_annex(),
            avgScore: this.task === undefined ? 0:this.task.avgScore,
            passRate: this.task === undefined ? 0:this.task.passRate,
            highestScore: this.task === undefined ? 0:this.task.highestScore,
            lowestScore: this.task === undefined ? 0:this.task.lowestScore,
            createDate: new Date().format('yyyy-MM-ddThh:mm:ss+0800'),
            endDate: (this.endDate + 'T' + this.end_time + ':00+0800'),
            submitNum: this.task === undefined ? 0:this.task.submitNum,
            judgeNum: this.task === undefined ? 0:this.task.judgeNum,
            cno: this.task === undefined ? this.cno:this.task.cno,
          }

          axios.post(this.edit ? '/api/updateTask':'/api/insertTask', data).then(res => {
            console.log('-------------')
            this.annex_map = []
            this.$emit('send')
            this.$emit('deliver')
          })

        },
        putUp() {
          this.tname = this.task.tname
          this.endDate = new Date(this.task.endDate).format('yyyy-MM-dd')
          this.end_time = new Date(this.task.endDate).format('hh:mm')
          this.annex = this.annex_format(this.task.annex)
        },
        upDescription(description) {
          this.description = description
          console.log(description)
        },
        annex_format(annex) {
          if (annex === null) {
            return []
          }
          var temp = annex.split('?')
          var annex_temp = []
          temp.forEach(item => {
            annex_temp.push({
              oriName: item,
              name: item.substring(10),
            })
          })
          return annex_temp
        },
        annexUploadSuccess(res) {
          this.annex.push({
            oriName: res,
            name: res.substring(10)
          })
          this.$message.success("附件上传成功")
        },
        annexUploadError(res) {
          this.$message.error("附件上传失败")
        },
        annexBeforeDelete(file) {
          this.annex.forEach((item, index) => {
            if (item.name === file.name) {
              this.annex.slice(index, 1)
            }
          })
        },
        form_annex() {
          var annex_temp = ''
          if (this.annex !== []) {
            this.annex.forEach(item => {
              annex_temp = annex_temp.concat(item.oriName + '?')
            })
            annex_temp = annex_temp.substring(0, annex_temp.length - 1)
          } else {
            return null
          }
          return annex_temp
        }
      }
      
    }
</script>

<style scoped>
  .personal-deliver {
    border: 1px solid #E2E6ED;
    margin-top: 20px;
    border-radius: 8px;
    background: #fff;
    padding: 20px 20px 60px;
    text-align: left;
  }
  .annex-button {
    float: left;
  }
  .editor, .form-line, .annex-upload {
    margin-top: 20px;
  }
  .editor {
    position: relative;
    z-index: 40;
  }
  .form-span {
    display: inline-block;
    width: 70px;
    text-align: right;
    margin-right: 15px;
  }
</style>
