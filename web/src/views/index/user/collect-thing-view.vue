<template>
  <div class="content-list">
    <div class="list-title">My Collections</div>
    <div role="tablist" class="list-tabs-view flex-view">
    </div>
    <div class="list-content">
      <div class="collect-thing-view">
        <div class="thing-list flex-view">
          <div class="thing-item item-column-3" v-for="(item,index) in pageData.collectData" :key="index">
            <div class="remove" @click="handleRemove(item)">Remove</div>
            <div class="img-view" @click="handleClickItem(item)">
              <img :src="item.cover">
            </div>
            <div class="info-view">
              <h3 class="thing-name">{{item.title}}</h3>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {message} from 'ant-design-vue';
import {userCollectListApi, unCollectApi} from '/@/api/thingCollect'
import {BASE_URL} from "/@/store/constants";
import {useUserStore} from "/@/store";

const router = useRouter();
const userStore = useUserStore();

const pageData = reactive({
  collectData: []
})

onMounted(()=>{
  getCollectThingList()
})

const handleClickItem =(record) =>{
  let text = router.resolve({name: 'detail', query: {id: record.thing_id}})
  window.open(text.href, '_blank')
}
const handleRemove =(record)=> {
  let username = userStore.user_name
  unCollectApi({id: record.id}).then(res => {
    message.success('Removed successfully')
    getCollectThingList()
  }).catch(err => {
    console.log(err)
  })
}
const getCollectThingList =()=> {
  let userId = userStore.user_id
  userCollectListApi({userId: userId}).then(res => {
    res.data.forEach(item => {
      item.cover = BASE_URL + '/api/staticfiles/image/' + item.cover
    })
    console.log(res.data)
    pageData.collectData = res.data
  }).catch(err => {
    console.log(err.msg)
  })
}

</script>
<style scoped lang="less">
.flex-view {
  display: flex;
}

.content-list {
  flex: 1;

  .list-title {
    color: #152844;
    font-weight: 600;
    font-size: 18px;
    line-height: 24px;
    height: 24px;
    margin-bottom: 4px;
  }

  .list-tabs-view {
    position: relative;
    border-bottom: 1px solid #cedce4;
    height: 12px;
    line-height: 42px;
  }
}

.thing-list {
  flex-wrap: wrap; /* 支持换行 */
  justify-content: flex-start;

  .thing-item {
    position: relative;
    flex: 1;
    margin-right: 20px;
    min-width: 255px;
    max-width: 255px;
    height: fit-content;
    border-radius: 8px; /* 圆角调整 */
    overflow: hidden;
    margin-top: 16px;
    cursor: pointer;
    background: #ffffff;
    box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1); /* 增强阴影效果 */

    .remove {
      position: absolute;
      right: 12px; /* 改到右侧 */
      top: 12px;
      width: 60px;
      height: 24px;
      text-align: center;
      line-height: 24px;
      color: #fff;
      background: #a1adc5; /* 使用灰色 */
      border-radius: 12px;
      cursor: pointer;
      z-index: 1; /* 确保在其他元素前面 */
      font-size: 12px; /* 字体大小调整 */
      transition: all 0.3s ease-in-out;

      &:hover {
        background: #8595b0; /* 悬停时背景色 */
      }
    }

    .img-view {
      background: #eaf1f5;
      text-align: center;
      height: 180px; /* 调高图片显示区域 */
      padding: 8px 0;

      img {
        max-width: 100%;
        height: 100%;
        display: block;
        margin: 0 auto;
        border-radius: 4px;
      }
    }

    .info-view {
      background: #f6f9fb;
      text-align: center;
      height: 60px; /* 高度调整 */
      overflow: hidden;
      padding: 8px 16px; /* 增加内间距，保持样式美观 */

      h3 {
        color: #1c355a;
        font-weight: 500;
        font-size: 16px;
        line-height: 20px;
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
        margin: 12px 0 8px;
      }

      .authors, .translators {
        color: #6f6f6f;
        font-size: 12px;
        line-height: 14px;
        margin-top: 4px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
    }
  }
}

</style>
