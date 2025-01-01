<template>
  <div class="user">
    <Header />

    <div class="user-content">
      <div class="user-content-left">
        <MineInfosView />
      </div>
      <div class="user-content-right">
        <router-view />
      </div>
    </div>


  </div>
</template>

<script>
import Header from '/@/views/index/components/header.vue'
import Footer from '/@/views/index/components/footer.vue'
import MineInfosView from '/@/views/index/user/mine-infos-view.vue'

export default {
  components: {
    MineInfosView,
    Footer,
    Header
  },
  data () {
    return {
      collapsed: false
    }
  }
}
</script>

<style scoped lang="less">
.user {
  display: block;
  background-color: #f0f4f8; /* 更明亮的背景色 */
  border-radius: 0; /* 设置为0以适应整个页面 */
  box-shadow: none; /* 取消阴影效果以适应整个页面 */
  width: 100%; /* 占满整个宽度 */
  height: 100vh; /* 占满整个高度 */
  position: relative; /* 相对定位以适应子元素 */
}

.user-content {
  display: flex;
  flex-direction: row;
  width: 100%; /* 占满整个宽度 */
  height: 100vh; /* 占满整个高度 */
  padding: 30px; /* 增加内容区域的内边距 */
  background-color: #ffffff; /* 内容区域的背景色 */
  border-radius: 0; /* 取消圆角 */

  .user-content-left {
    width: 300px; /* 左侧区域宽度 */
    max-height: 1200px;
    background-color: #e0f7fa; /* 左侧区域背景色 */
    border-radius: 8px; /* 左侧区域圆角 */
    padding: 20px; /* 增加左侧区域的内边距 */
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1); /* 左侧区域阴影 */
    margin-right: 20px; /* 在左侧和右侧之间增加间距 */
    margin-top: 20px;
    overflow: auto;
  }

  .user-content-right {
    max-height: 1200px;
    flex: 1;
    padding: 20px; /* 增加右侧的内边距 */
    background-color: #fff3e0; /* 右侧区域背景色 */
    border-radius: 8px; /* 右侧区域圆角 */
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1); /* 右侧区域阴影 */
    margin-top: 20px;
    overflow: auto;
  }
}
</style>

