<template>
  <div class="content-list">
    <div class="list-title">Account Security</div>
    <div class="list-content">
      <div class="safe-view">
        <div class="safe-info-box">
          <div class="item flex-view">
            <div class="label">Account Security Level</div>
            <div class="right-box flex-center flex-view">
              <div class="safe-text">Low Risk</div>
              <progress max="3" class="safe-line" value="2">
              </progress>
            </div>
          </div>
        </div>
        <div class="edit-pwd-box" style="display;">
          <div class="pwd-edit">
            <div class="item flex-view">
              <div class="label">Current Password</div>
              <div class="right-box">
                <a-input-password placeholder="Enter Current Password" v-model:value="password"/>
              </div>
            </div>
            <div class="item flex-view">
              <div class="label">New Password</div>
              <div class="right-box">
                <a-input-password placeholder="Enter New Password" v-model:value="newPassword1"/>
              </div>
            </div>
            <div class="item flex-view">
              <div class="label">Confirm New Password</div>
              <div class="right-box">
                <a-input-password placeholder="Re-enter Password" v-model:value="newPassword2"/>
              </div>
            </div>
            <div class="item flex-view">
              <div class="label">
              </div>
              <div class="right-box">
                <a-button type="primary" @click="handleUpdatePwd()">Change Password</a-button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {message} from "ant-design-vue";
import {updateUserPwdApi} from '/@/api/user'
import {useUserStore} from "/@/store";

const router = useRouter();
const userStore = useUserStore();

let password = ref('')
let newPassword1 = ref('')
let newPassword2 = ref('')

const handleBindMobile = () => {
  message.info('Function under development')
}

const handleUpdatePwd = () => {
  if (!password.value || !newPassword1.value || !newPassword2.value) {
    message.warn('Cannot be empty')
    return
  }
  if (newPassword1.value !== newPassword2.value) {
    message.warn('Passwords do not match')
    return
  }

  let userId = userStore.user_id
  updateUserPwdApi({
    userId:  userId,
    password: password.value,
    newPassword: newPassword1.value,
  }).then(res => {
    message.success('Changed successfully')
  }).catch(err => {
    message.error(err.msg)
  })
}

</script>

<style scoped lang="less">
progress {
  vertical-align: baseline;
}

.flex-view {
  display: flex;
}

input, textarea {
  outline: none;
  border-style: none;
}

.content-list {
  flex: 1;

  .list-title {
    color: #152844;
    font-weight: 600;
    font-size: 18px;
    height: 48px;
    margin-bottom: 4px;
    border-bottom: 1px solid #cedce4;
  }
}

.safe-view {
  .item {
    align-items: center;
    margin: 24px 0;

    .label {
      width: 100px;
      color: #152844;
      font-weight: 600;
      font-size: 14px;
    }

    .flex-center {
      align-items: center;
    }

    .safe-text {
      color: #f62a2a;
      font-weight: 600;
      font-size: 14px;
      margin-right: 18px;
    }

    .safe-line {
      background: #d3dce6;
      border-radius: 8px;
      width: 280px;
      height: 8px;
      overflow: hidden;
      color: #f6982a;
    }

    .input-dom {
      background: #f8fafb;
      border-radius: 4px;
      width: 240px;
      height: 40px;
      line-height: 40px;
      font-size: 14px;
      color: #5f77a6;
      padding: 0 12px;
      margin-right: 16px;
    }

    .change-btn {
      color: #4684e2;
      font-size: 14px;
      border: none;
      outline: none;
      cursor: pointer;
    }

    .wx-text {
      color: #5f77a6;
      font-size: 14px;
      margin-right: 16px;
    }

    .edit-pwd-btn {
      color: #4684e2;
      font-size: 14px;
      cursor: pointer;
    }
  }
}

.right-box {
  margin-left: 20px; /* 增加左边距，向右移动 */
}
</style>
