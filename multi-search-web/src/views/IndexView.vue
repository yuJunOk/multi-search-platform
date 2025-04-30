<template>
  <div class="index-view">
    <a-input-search
      v-model:value="searchText"
      placeholder="请输入搜索关键词"
      enter-button="搜索"
      size="large"
      @search="onSearch"
    />
    <MyDivider />
    <a-tabs v-model:activeKey="searchKey" @change="onTabChange">
      <a-tab-pane key="post" tab="文章">
        <PostList :post-list="postList" />
      </a-tab-pane>
      <a-tab-pane key="picture" tab="图片">
        <PictureList :picture-list="pictureList" />
      </a-tab-pane>
      <a-tab-pane key="user" tab="用户">
        <UserList :user-list="userList" />
      </a-tab-pane>
    </a-tabs>
  </div>
</template>

<script setup lang="ts">
import PostList from "@/components/PostList.vue";
import PictureList from "@/components/PictureList.vue";
import UserList from "@/components/UserList.vue";
import { ref, watchEffect } from "vue";
import { useRoute, useRouter } from "vue-router";
import { message } from "ant-design-vue";
import myAxios from "@/plugins/myAxios";

const postList = ref([]);
const userList = ref([]);
const pictureList = ref([]);

const route = useRoute();
const router = useRouter();

const searchKey = route.params.category ?? "post";
const searchText = ref(route.query.text || "");

const initSearchParams = {
  text: "",
  pageSize: 10,
  current: 1,
};
const searchParams = ref(initSearchParams);
const onSearch = (value: string) => {
  router.push({
    query: {
      ...searchParams.value,
      text: value,
    },
  });
};

const onTabChange = (key: string) => {
  router.push({
    path: `/${key}`,
    query: searchParams.value,
  });
};

/**
 * 加载单类数据
 * @param params
 */
const loadData = (params: any) => {
  const { type = "post" } = params;
  if (!type) {
    message.error("类别为空");
    return;
  }
  const query = {
    ...params,
    searchText: params.text,
  };
  myAxios.post("search/all", query).then((res: any) => {
    if (type === "post") {
      postList.value = res.postList;
    } else if (type === "user") {
      userList.value = res.userList;
    } else if (type === "picture") {
      pictureList.value = res.pictureList;
    }
  });
};

watchEffect(() => {
  searchParams.value = {
    ...initSearchParams,
    text: route.query.text,
  } as any;
  loadData({
    ...searchParams.value,
    type: route.params.category ?? "post",
  });
});
</script>
