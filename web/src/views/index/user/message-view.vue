<template>
  <div class="chat-container">
    <!-- 用户列表 -->
    <div class="user-list">
      <div class="list-title">User List</div>
      <div class="user-item" v-for="user in userList" :key="user.id" @click="selectUser(user)" :class="{'selected': selectedUser?.id === user.id}">
        <img :src="user.avatarUrl || defaultAvatar" alt="avatar" class="user-avatar">
        <p class="user-name">{{ user.name }}</p>
      </div>
    </div>

    <!-- 对话界面 -->
    <div class="chat-box" v-if="selectedUser">
      <div class="chat-header">
        <h3>Chat with {{ selectedUser.name }}</h3>
      </div>
      <div class="chat-messages">
        <div class="message" v-for="message in messages" :key="message.id">
          <p><strong>{{ message.sender }}:</strong> {{ message.content }}</p>
        </div>
      </div>
      <div class="chat-input">
        <textarea v-model="newMessage" placeholder="Type your message..."></textarea>
        <button @click="sendMessage">Send</button>
      </div>
    </div>

    <div v-else class="no-user-selected">
      <p>Please select a user to start a conversation.</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { listUserDialogsApi, createDialogApi, listMessagesApi, sendMessageApi } from '/@/api/dialog';
import { useUserStore } from "/@/store";

const userStore = useUserStore();

const userList = ref([]);
const selectedUser = ref(null);
const messages = ref([]);
const newMessage = ref("");
const defaultAvatar = '/path/to/default/avatar.png'; // 设置默认头像路径

// 获取用户列表
const fetchUserList = async () => {
  const response = await listUserDialogsApi(userStore.user_id);
  if (response && response.data) {
    const userDialogs = response.data;

    const usersInDialogs = userDialogs.map(item => {
      let otherUserId = null;
      let otherUserName = null;
      console.log(item);  // 调试输出每一项的内容
      if (item.user1Id.toString() === userStore.user_id.toString()) {
        otherUserId = item.user2Id;
        otherUserName = item.username2;
      } else if (item.user2Id.toString() === userStore.user_id.toString()) {
        otherUserId = item.user1Id;
        otherUserName = item.username1;
      }

      if (otherUserId) {
        return {dialogId:item.dialogId, id: otherUserId, name: otherUserName, avatarUrl: `/avatars/${otherUserId}.png` }; // 假设头像存储在指定路径
      }
    }).filter(user => user); // 过滤掉无效的值

    userList.value = usersInDialogs; // 更新用户列表
  }
};

// 获取对话消息
const fetchMessages = async (dialogId) => {
  const response = await listMessagesApi(dialogId);
  if (response && response.data) {
    messages.value = response.data;
  }
};

// 选择用户并加载对话
const selectUser = (user) => {
  selectedUser.value = user;
  const dialogId = findDialogId(user.id);
  fetchMessages(dialogId);
};

// 查找对话ID
const findDialogId = (otherUserId) => {
  let dialogId = '';
  const userDialogs = userList.value;

  userDialogs.forEach(item => {
    console.log(item);  // 调试输出每一项的内容// 确保 item 中有 user1Id 和 user2Id
    if ((item.id === otherUserId)) {
      dialogId = item.dialogId;
    }

  });

  return dialogId;
};


// 发送消息
const sendMessage = async () => {
  if (!newMessage.value.trim()) return;

  const dialogId = findDialogId(selectedUser.value.id);
  const senderId = userStore.user_id;
  const content = newMessage.value;
  console.log("dialogId:" + dialogId);
  const response = await sendMessageApi(dialogId, senderId, content);
  if (response) {
    messages.value.push({
      sender: userStore.user_name,
      content: newMessage.value,
      id: Date.now(),
    });
    newMessage.value = "";
  }
};

onMounted(() => {
  fetchUserList();
});
</script>

<style scoped>
.chat-container {
  display: flex;
  justify-content: space-between;
  margin: 20px;
}

.user-list {
  width: 25%;
  background-color: #f4f4f4;
  padding: 10px;
  border-radius: 8px;
}

.list-title {
  color: #152844;
  font-weight: 600;
  font-size: 18px;
  margin-bottom: 10px;
  border-bottom: 1px solid #cedce4;
}

.user-item {
  display: flex;
  align-items: center;
  padding: 10px;
  cursor: pointer;
  border-bottom: 1px solid #ddd;
}

.user-item:hover {
  background-color: #e0e0e0;
}

.user-item.selected {
  background-color: #d0e7f9; /* 高亮显示选中的用户 */
}

.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 10px;
}

.user-name {
  font-size: 16px;
  color: #315c9e;
}

.chat-box {
  width: 70%;
  padding: 20px;
  border-radius: 8px;
  border: 1px solid #ddd;
}

.chat-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.chat-messages {
  height: 300px;
  overflow-y: auto;
  margin-bottom: 10px;
}

.message {
  padding: 5px;
  border-bottom: 1px solid #f0f0f0;
}

.chat-input {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chat-input textarea {
  width: 80%;
  height: 50px;
  padding: 10px;
  border-radius: 5px;
  border: 1px solid #ccc;
}

.chat-input button {
  width: 15%;
  padding: 10px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.chat-input button:hover {
  background-color: #45a049;
}

.no-user-selected {
  padding: 20px;
  text-align: center;
  color: #aaa;
}
</style>
