<template>
  <div class="mine-infos-view">
    <div class="info-box flex-view">
      <img :src="AvatarImg" class="avatar-img">
      <div class="name-box">
        <h2 class="nick">{{ userStore.user_name }}</h2>
        <div class="age">
          <span> </span>
          <span class="give-point"></span>
        </div>
      </div>
    </div>
    <div class="counts-view">
      <div class="counts flex-view">
        <div class="fans-box flex-item" @click="clickMenu('collectThingView')">
          <div class="text">My Collections</div>
          <div class="num">{{collectCount}}</div>
        </div>
        <div class="split-line"></div>
        <div class="follow-box flex-item" @click="clickMenu('wishThingView')">
          <div class="text">My tutors</div>
          <div class="num">{{wishCount}}</div>
        </div>
      </div>
    </div>
    <div class="setting-box">
      <div class="title">Personal Settings</div>
      <div class="list">
        <div class="mine-item flex-view" @click="clickMenu('jiajiaoEditView')">
          <img :src="SettingIconImage" alt="Tutor Info">
          <span>Tutor Info</span>
        </div>
        <div class="mine-item flex-view" @click="clickMenu('userInfoEditView')">
          <img :src="SettingIconImage" alt="Edit Profile">
          <span>Edit Profile</span>
        </div>
        <div class="mine-item flex-view" @click="clickMenu('securityView')">
          <img :src="SafeIconImage" alt="Account Security">
          <span>Account Security</span>
        </div>
        <div class="mine-item flex-view" @click="clickMenu('messageView')">
          <img :src="MessageIconImage" alt="Message Management">
          <span>Message Management</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import AvatarImg from '/@/assets/images/avatar.jpg'
import SettingIconImage from '/@/assets/images/setting-icon.svg'
import SafeIconImage from '/@/assets/images/setting-safe-icon.svg'
import PushIconImage from '/@/assets/images/setting-push-icon.svg'
import MessageIconImage from '/@/assets/images/setting-msg-icon.svg'

import { userCollectListApi } from '/@/api/thingCollect'
import { userWishListApi } from '/@/api/thingWish'
import { useUserStore } from '/@/store'
const userStore = useUserStore()
const router = useRouter()

let collectCount = ref(0)
let wishCount = ref(0)

onMounted(() => {
  getCollectThingList()
  getWishThingList()
})

const clickMenu = (name) => {
  router.push({ name: name })
}

const getCollectThingList = () => {
  let userId = userStore.user_id
  userCollectListApi({ userId: userId })
      .then((res) => {
        collectCount.value = res.data.length
      })
      .catch((err) => {
        console.log(err.msg)
      })
}

const getWishThingList = () => {
  let userId = userStore.user_id
  userWishListApi({ userId: userId })
      .then((res) => {
        wishCount.value = res.data.length
      })
      .catch((err) => {
        console.log(err.msg)
      })
}
</script>

<style scoped lang="less">
.flex-view {
  display: flex;
}

.mine-infos-view {
  width: 250px;
  margin-right: 20px;
  border: 1px solid #e6e6e6;
  border-radius: 8px;
  background-color: #f9f9f9;
  overflow: hidden;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);

  .info-box {
    display: flex;
    align-items: center;
    padding: 20px;
    background: #ffffff;

    .avatar-img {
      width: 60px;
      height: 60px;
      border-radius: 50%;
      margin-right: 16px;
      border: 2px solid #e6e6e6;
    }

    .name-box {
      flex: 1;

      .nick {
        color: #333333;
        font-weight: 700;
        font-size: 18px;
        margin: 0;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }

      .age {
        font-size: 12px;
        color: #999999;
        margin-top: 4px;

        .give-point {
          color: #4a90e2;
          cursor: pointer;
          margin-left: 10px;
        }
      }
    }
  }

  .counts-view {
    padding: 16px;
    background-color: #ffffff;

    .counts {
      display: flex;
      text-align: center;

      .flex-item {
        flex: 1;
        cursor: pointer;
      }

      .text {
        font-size: 14px;
        color: #666666;
      }

      .num {
        font-size: 16px;
        color: #333333;
        font-weight: 700;
        margin-top: 4px;
      }

      .split-line {
        width: 1px;
        background: #e6e6e6;
      }
    }
  }

  .setting-box {
    padding: 16px;
    background: #ffffff;
    margin-top: 8px;

    .title {
      color: #333333;
      font-weight: 700;
      font-size: 16px;
      margin-bottom: 12px;
    }

    .list {
      .mine-item {
        display: flex;
        align-items: center;
        padding: 12px 0;
        border-top: 1px solid #e6e6e6;
        cursor: pointer;

        img {
          width: 24px;
          height: 24px;
          margin-right: 12px;
        }

        span {
          font-size: 14px;
          color: #333333;
        }
      }

      .mine-item:first-child {
        border-top: none;
      }
    }
  }
}
</style>
