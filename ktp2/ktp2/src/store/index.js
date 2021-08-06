import Vue from 'vue'
import Vuex from 'vuex'
import mutations from '@/store/mutations'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    isLogin: window.sessionStorage.isLogin === undefined ? false : window.sessionStorage.isLogin,
    // 全局的账号信息
    uid: window.sessionStorage.uid === undefined ? '' : window.sessionStorage.uid,
    // 用户头像
    avatar: window.sessionStorage.avatar === undefined ? '' : window.sessionStorage.avatar,
    // 用户的身份
    permission: window.sessionStorage.permission === undefined ? '' : window.sessionStorage.permission,
    // 用户姓名
    name: window.sessionStorage.name === undefined ? '' : window.sessionStorage.name
  },
  mutations,
  actions: {},
  modules: {}
})
