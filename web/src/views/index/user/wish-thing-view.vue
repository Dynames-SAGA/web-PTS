<template>
  <div class="content-list">
    <div class="list-title">My Tutors</div>
    <div role="tablist" class="list-tabs-view flex-view">
    </div>
    <div class="list-content">
      <div class="collect-thing-view">
        <div class="thing-list flex-view">
          <div class="thing-item item-column-3" v-for="(item,index) in wishData" :key="index">
            <div class="remove" @click="handleRemove(item)">Remove</div>
            <div class="img-view" @click="handleClickItem(item)">
              <img :src="item.cover" alt="Item Cover">
            </div>
            <div class="info-view">
              <h3 class="thing-name">{{item.title}}</h3>
              <p class="authors" v-if="item.author">{{item.author}} (Author)</p>
              <p class="translators" v-if="item.translator">{{item.translator}} (Translator)</p>
              <div class="action-buttons">
                <button class="pay-button">Pay</button>
                <button class="chat-button" @click="openChat(item)">Chat</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- 聊天窗口 -->
    <el-dialog v-model="chatVisible" title="Chat" width="400px" :before-close="handleCloseChat">
      <div class="chat-window">
        <div class="chat-messages">
          <div v-for="(message, index) in chatMessages" :key="index" class="message">
            <strong>{{ message.sender }}:</strong> {{ message.text }}
          </div>
        </div>
        <el-input
            v-model="newMessage"
            placeholder="Type a message..."
            @keyup.enter="sendMessage"
        >
          <template #append>
            <el-button @click="sendMessage">Send</el-button>
          </template>
        </el-input>
      </div>
    </el-dialog>





  </div>
</template>

<script setup>
import {message} from 'ant-design-vue';
import {userWishListApi, unWishApi} from '/@/api/thingWish'
import {BASE_URL} from "/@/store/constants";
import {useUserStore} from "/@/store";

const router = useRouter();
const route = useRoute();
const userStore = useUserStore();

// 聊天窗口相关状态
const chatVisible = ref(false); // 控制聊天窗口显示/隐藏
const chatMessages = ref([]); // 聊天消息列表
const newMessage = ref(''); // 当前输入的消息

// 打开聊天窗口
const openChat = (item) => {
  chatVisible.value = true;
  // 这里可以根据 item 初始化聊天记录
  chatMessages.value = [
    { sender: 'Tutor', text: 'Hello! How can I help you?' },
  ];
};

// 发送消息
const sendMessage = () => {
  if (newMessage.value.trim()) {
    chatMessages.value.push({ sender: 'You', text: newMessage.value.trim() });
    newMessage.value = ''; // 清空输入框
  }
};

// 关闭聊天窗口
const handleCloseChat = () => {
  chatVisible.value = false;
  chatMessages.value = []; // 清空聊天记录
};

let wishData = ref([])

onMounted(()=>{
  getWishThingList()
})

const handleClickItem =(record)=> {
  let text = router.resolve({name: 'detail', query: {id: record.thing_id}})
  window.open(text.href, '_blank')
}

const handleRemove =(record)=> {
  unWishApi({id:record.id}).then(res => {
    message.success('Removed successfully')
    getWishThingList()
  }).catch(err => {
    console.log(err)
  })
}

const getWishThingList =()=> {
  let userId = userStore.user_id
  userWishListApi({userId: userId}).then(res => {
    res.data.forEach(item => {
      item.cover = BASE_URL + '/api/staticfiles/image/' + item.cover
    })
    wishData.value = res.data
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

.chat-window {
  .chat-messages {
    height: 200px;
    overflow-y: auto;
    border: 1px solid #ddd;
    padding: 10px;
    margin-bottom: 10px;
    border-radius: 4px;

    .message {
      margin-bottom: 10px;
    }
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
    border-radius: 4px;
    overflow: hidden;
    margin-top: 16px;
    cursor: pointer;
    background: #ffffff;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* 添加阴影 */

    .remove {
      position: absolute;
      right: 8px; /* 改到右侧 */
      top: 8px;
      width: 60px;
      height: 20px;
      text-align: center;
      line-height: 20px;
      color: #fff;
      background: #a1adc5; /* 将颜色改为灰色 */
      border-radius: 12px;
      cursor: pointer;
      z-index: 1; /* 确保在其他元素前面 */
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
      height: 80px; /* 减少上部空白，调整高度 */
      overflow: hidden;
      padding: 8px 16px; /* 增加内间距，保持样式美观 */

      h3 {
        color: #1c355a;
        font-weight: 500;
        font-size: 16px;
        line-height: 20px;
      }

      .authors, .translators {
        color: #6f6f6f;
        font-size: 12px;
        line-height: 14px;
        margin-top: 4px;
      }

      .action-buttons {
        margin-top: 8px; /* 增加与标题的间距 */

        .pay-button, .chat-button {
          background-color: #4684e2; /* 按钮主色调 */
          color: #fff;
          border: none;
          border-radius: 4px; /* 圆角边框 */
          padding: 6px 12px; /* 内边距，增加按钮舒适度 */
          cursor: pointer;
          margin-right: 8px; /* 按钮之间的间距 */
          font-size: 14px; /* 按钮文字大小 */
          font-weight: 500; /* 字体加粗 */
          transition: all 0.3s ease-in-out; /* 添加过渡效果 */

          &:hover {
            background-color: #3568b8; /* 悬停时变为更深的蓝色 */
            transform: translateY(-2px); /* 悬停时微微上移 */
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15); /* 增加悬停阴影 */
          }

          &:active {
            background-color: #2d5292; /* 按下时颜色更深 */
            transform: translateY(0); /* 按下时还原位置 */
            box-shadow: none; /* 按下时移除阴影 */
          }
        }

        /* 单独优化 Chat 按钮为绿色 */
        .chat-button {
          background-color: #5cb85c;

          &:hover {
            background-color: #4cae4c;
          }

          &:active {
            background-color: #3d8b3d;
          }
        }
      }

    }
  }
}
</style>
