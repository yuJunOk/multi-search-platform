import { createRouter, createWebHashHistory, RouteRecordRaw } from "vue-router";
import IndexView from "../views/IndexView.vue";

const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    name: "index",
    component: IndexView,
  },
  // 动态路由
  {
    path: "/:category",
    component: IndexView,
  },
];

const router = createRouter({
  history: createWebHashHistory(),
  routes,
});

export default router;
