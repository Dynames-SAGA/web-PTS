<template>
  <div class="content-list">
    <div class="list-title">My Tutors</div>
    <div role="tablist" class="list-tabs-view flex-view"></div>
    <div class="list-content">
      <div class="collect-thing-view">
        <div class="thing-list flex-view">
          <div class="thing-item item-column-3" v-for="(item, index) in wishData" :key="index">
            <div class="remove" @click="handleRemove(item)">Remove</div>
            <div class="img-view" @click="handleClickItem(item)">
              <img :src="item.cover" alt="Item Cover">
            </div>
            <div class="info-view">
              <h3 class="thing-name">{{ item.title }}</h3>
              <p class="authors" v-if="item.author">{{ item.author }} (Author)</p>
              <p class="translators" v-if="item.translator">{{ item.translator }} (Translator)</p>
              <div class="action-buttons">
                <!-- 打开二维码弹窗 -->
                <button class="pay-button" @click="openQrcodeModal(item)">Generate QR Code</button>
                <button class="chat-button" @click="openChat(item)">Chat</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 二维码弹窗 -->
    <el-dialog v-model="qrcodeVisible" title="Payment QR Code" width="300px" :before-close="closeQrcodeModal">
      <div id="qrcode-container" style="text-align: center;"></div>
      <p v-if="paymentStatus === 'non-payment'" style="color: orange; text-align: center;">User has scanned the QR code but not paid yet.</p>
      <p v-if="paymentStatus === 'yes-payment'" style="color: green; text-align: center;">Payment completed successfully!</p>
    </el-dialog>

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
import { ref, onMounted, onBeforeUnmount } from 'vue';
import $ from 'jquery'; // 确保安装了 jQuery
import 'jquery-qrcode'; // 确保安装了 jquery-qrcode
import { message } from 'ant-design-vue';
import { userWishListApi, unWishApi } from '/@/api/thingWish';
import { BASE_URL } from "/@/store/constants";
import { useUserStore } from "/@/store";

const router = useRouter();
const userStore = useUserStore();

const wishData = ref([]);


// 聊天窗口相关状态
const chatVisible = ref(false); // 控制聊天窗口显示/隐藏
const chatMessages = ref([]); // 聊天消息列表
const newMessage = ref(''); // 当前输入的消息

// 二维码相关状态
const qrcodeVisible = ref(false); // 控制二维码弹窗显示/隐藏
const currentItem = ref(null); // 当前选中的条目（用于生成二维码）
const outTradeNo = ref(null); // 商户订单编号
const paymentStatus = ref(null); // 支付状态（non-payment, yes-payment）

let tradeInterval = null; // 定时任务句柄
let findNumber = true; // 记录是否通知页面“用户已扫码”

// 获取愿望列表
const getWishThingList = () => {
  const userId = userStore.user_id;
  userWishListApi({ userId }).then(res => {
    res.data.forEach(item => {
      item.cover = BASE_URL + '/api/staticfiles/image/' + item.cover;
    });
    wishData.value = res.data;
  }).catch(err => {
    console.error(err.msg);
  });
};

// 打开二维码弹窗并生成二维码
const openQrcodeModal = (item) => {
  currentItem.value = item;
  qrcodeVisible.value = true;

  // 模拟请求二维码数据
  $.ajax({
    url: `http://localhost:8080/api/AlipayFaceToFaceController/ZFBPreorderAction`,
    method: 'POST',
    dataType: 'JSON',
    success: function (res) {
      outTradeNo.value = res.outTradeNo; // 保存订单编号
      paymentStatus.value = null; // 重置支付状态
      createQrcode(res.qrcode); // 调用生成二维码方法
      startFindZFBTrade(); // 启动定时任务
    },
    error: function () {
      message.error('Failed to generate QR code.');
    },
  });
};

// 生成二维码
const createQrcode = (url) => {
  $('#qrcode-container').empty(); // 清空之前的二维码
  $('#qrcode-container').qrcode({
    width: 150,
    height: 150,
    text: url,
  });
};

// 开始定时查询交易状态
const startFindZFBTrade = () => {
  tradeInterval = setInterval(function () {
    console.log("Checking transaction status...");
    $.ajax({
      url: `http://localhost:8080/api/AlipayFaceToFaceController/findZFB_tradeAction`,
      method: 'POST',
      data: {
        outTradeNo: outTradeNo.value,
      },
      dataType: 'JSON',
      success: function (res) {
        if (res.code === '10000' && res.data === 'non-payment') {
          // 用户扫码未支付
          if (findNumber) {
            console.log("User has scanned the QR code but not paid.");
            paymentStatus.value = 'non-payment';
            findNumber = false;
          }
        } else if (res.code === '10000' && res.data === 'yes-payment') {
          // 用户已支付
          clearInterval(tradeInterval); // 停止定时任务
          paymentStatus.value = 'yes-payment';
          message.success('Payment completed successfully!');
        }
      },
    });
  }, 3000); // 每 3 秒查询一次
};

// 关闭二维码弹窗
const closeQrcodeModal = () => {
  qrcodeVisible.value = false;
  clearInterval(tradeInterval); // 停止查询任务
  findNumber = true; // 重置扫码标志
};

// 打开聊天窗口
const openChat = (item) => {
  chatVisible.value = true;
  chatMessages.value = [{ sender: 'Tutor', text: 'Hello! How can I help you?' }];
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
  chatMessages.value = [];
};

// 点击条目，跳转详情页
const handleClickItem = (record) => {
  const text = router.resolve({ name: 'detail', query: { id: record.thing_id } });
  window.open(text.href, '_blank');
};

// 移除条目
const handleRemove = (record) => {
  unWishApi({ id: record.id }).then(() => {
    message.success('Removed successfully');
    getWishThingList();
  }).catch(err => {
    console.error(err);
  });
};

// 获取愿望列表
onMounted(() => {
  getWishThingList();
});

// 清理定时任务（防止内存泄漏）
onBeforeUnmount(() => {
  clearInterval(tradeInterval);
});
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
