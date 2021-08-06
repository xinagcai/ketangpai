import Vue from 'vue'
import VueRouter from 'vue-router'
import * as mt from '../store/mutation-type'
import store from '../store'

Vue.use(VueRouter)

const routes = [
  {
    // 登录
    path: '',
    name: 'Login',
    component: () => import('views/Login/Login')
  },
  {
    // 注册
    path: '/register',
    name: 'Register',
    component: () => import('views/Login/Register')
  },
  {
    // 首页
    path: '/home',
    name: 'Home',
    component: () => import('views/Home/Home'),
    meta: {
      requireAuth: true
    }
  },
  {
    // 个人信息管理页面
    path: '/profile',
    name: 'Profile',
    component: () => import('views/Profile/Profile'),
    meta: {
      requireAuth: true
    }
  },
  {
    // 消息查看页面
    path: '/message',
    name: 'Message',
    component: () => import('views/Message/Message'),
    meta: {
      requireAuth: true
    }
  },
  {
    // 教师发布查看自己发布的作业和发布作业的页面
    path: '/teachertask/:cno',
    name: 'TeacherTask',
    component: () => import('views/Task/TeacherTask'),
    meta: {
      requireAuth: true,
      permissionIs: 2
    }
  },
  {
    // 学生进入某个课程的页面
    path: '/studenttask/:cno',
    name: 'StudentTask',
    component: () => import('views/Task/StudentTask'),
    meta: {
      requireAuth: true,
      permissionIs: 1
    }
  },
  {
    // 学生提交作业的页面
    path: '/studenttaskcommit/:cno/:tno',
    name: 'StudentTaskCommit',
    component: () => import('views/Task/StudentTaskCommit'),
    meta: {
      requireAuth: true,
      permissionIs: 1
    }
  },
  {
    // 教师批改作业打分的页面
    path: '/taskmark/:cno/:tno',
    name: 'TaskMark',
    component: () => import('views/Task/TaskMark'),
    meta: {
      requireAuth: true,
      permissionIs: 2
    }
  },
  {
    // 某门课程的成员展示
    path: '/coursemember/:cno',
    name: 'CourseMember',
    component: () => import('views/CourseMember/CourseMember'),
    meta: {
      requireAuth: true
    }
  },
  {
    path: '/discuss/:cno/:tno',
    name: 'Discuss',
    component: () => import('views/Discuss/Discuss'),
    meta: {
      requireAuth: true
    }
  }

]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

// 路由拦截器
router.beforeEach((to, from, next) => {
  if (to.meta.requireAuth) {
    if (window.sessionStorage.isLogin !== undefined && window.sessionStorage.isLogin) {
      // 验证是否通过权限验证
      if (to.meta.permissionIs == undefined || to.meta.permissionIs == window.sessionStorage.permission) {
        next()
      } else {
        next({
          path: '/home'
        })
      }
    } else {
      // 未登录状态
      next({
        path: '/',
      })
    }
  } else {
    // 判断是否存有cookie，有则自动登录
    if ($cookies.get('user') != undefined) {
      store.commit(mt.IS_LOGIN, $cookies.get('user').account)
      store.commit(mt.CHANGE_UID, $cookies.get('user').uid)
      store.commit(mt.USER_AVATAR, $cookies.get('user').avatar)
      store.commit(mt.PERMISSION, $cookies.get('user').permission)
      store.commit(mt.CHANGE_NAME, $cookies.get('user').name)

      next('/home')
    } else {
      // 不需要通过登录就可以获取到的资源
      next()
    }
  }
})

export default router
