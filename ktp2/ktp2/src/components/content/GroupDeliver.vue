<template>
  <div class="personal-deliver">
    <form id="personal-form">
      <el-input v-model="tname" placeholder="作业名称"></el-input>
      <Editor class="editor"/>
      <div class="form-line">
        <span class="form-span" style="width: 98px">选择分组批次：</span>
        <el-select v-model="group" placeholder="请选择">
          <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </div>
      <div class="form-line">
        <span class="form-span">截止日期：</span>
        <el-date-picker
          v-model="end_date"
          type="date"
          placeholder="选择日期"
          :clearable=false>
        </el-date-picker>
        <el-time-select v-model="end_time" :picker-options="{
              start: '00:00',
              step: '00:15',
              end: '23:45'
            }" placeholder="选择时间" v-if="end_date !== ''" style="margin-left: 20px;">
        </el-time-select>
        <el-switch
          v-model="outdate"
          v-if="end_date !== ''"
          active-text="若超时，禁止提交"
          style="margin-left: 50px">
        </el-switch>
      </div>
      <div class="form-line">
        <span class="form-span">满分值：</span>
        <el-input v-model="max_point" style="width: 60px"></el-input>
        <span style="color: red;padding-left: 10px">注：</span><span>同一分组内，任一学生所交作业都将记为小组作业，组内成员作业共享。</span>
      </div>
      <div class="form-line">
        <el-button type="primary" style="float: left"><i class="el-icon-download el-icon--left"/>导入作业</el-button>
        <span style="float: right;display: flex;justify-content: space-between">
            <el-button type="primary" v-on:click="cancel" style="float: left">取消</el-button>
            <el-button type="primary" style="float: left">发布小组作业</el-button>
          </span>
      </div>
    </form>
  </div>
</template>

<script>
import Editor from '@/components/content/Editor'

export default {
  name: 'GroupDeliver',
  components: {
      Editor
    },
    data() {
      return {
        tname: '',
        end_date: '',
        end_time: '08:30',
        outdate: false,
        max_point: '',
        group: '',
        options: [{
          value: 'g1',
          label: '小组1'
        },{
          value: 'g2',
          label: '小组2'
        },{
          value: 'g3',
          label: '小组3'
        },{
          value: 'g4',
          label: '小组4'
        }],
        showDeliver: 2,
      }
    },
    methods: {
      cancel: function () {
        this.$emit('deliver',0)
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
  .editor, .form-line {
    margin-top: 20px;
  }
  .editor {
    position: relative;
    z-index: 40;
  }
  .form-span {
    display: inline-block;
    width: 70px;
    margin-right: 15px;
    text-align: right;
  }
</style>