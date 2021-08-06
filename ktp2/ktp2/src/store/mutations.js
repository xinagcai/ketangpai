import * as mt from "./mutation-type"

const mutations = {
  [mt.IS_LOGIN](state, account) {
    state.isLogin = account != ""
    window.sessionStorage.isLogin = state.isLogin
  },
  [mt.CHANGE_UID](state, uid) {
    state.uid = uid
    window.sessionStorage.uid = state.uid
  },
  [mt.USER_AVATAR](state, avatar) {
    state.avatar = avatar
    window.sessionStorage.avatar = state.avatar
  },
  [mt.PERMISSION](state, permission) {
    state.permission = permission
    window.sessionStorage.permission = state.permission
  },
  [mt.CHANGE_NAME](state, name) {
    state.name = name
    window.sessionStorage.name = state.name
  }
}

export default mutations