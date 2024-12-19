<template>
  <div class="container">
    <div class="tel-regist-page pc-style">
      <div class="regist-title">
        <span @click="router.push({name: 'login'})" class="toWxLogin">Login</span>
      </div>

      <div class="regist-padding">
        <div class="common-input">
          <div class="input-view">
            <input placeholder="Please enter your username" v-model="tData.resetForm.username" type="text" class="input">
            <p class="err-view">
            </p>
          </div>
        </div>
      </div>
      <div class="regist-padding">
        <div class="common-input">
          <div class="input-view">
            <input placeholder="Please enter your email" v-model="tData.resetForm.email" type="text" class="input">
            <p class="err-view">
            </p>
          </div>
        </div>
      </div>

      <div class="tel-login">
        <div class="next-btn-view">
          <button class="next-btn" @click="handleSendEmail">Send Reset Email</button>
        </div>
      </div>

      <!-- 添加20px的空行 -->
      <div style="height: 20px;"></div>

      <div class="regist-padding">
        <div class="common-input">
          <div class="input-view">
            <input placeholder="Please enter verification code" v-model="tData.resetForm.code" type="text" class="input">
            <p class="err-view">
            </p>
          </div>
        </div>
      </div>

      <div class="regist-padding">
        <div class="common-input">
          <div class="input-view">
            <input placeholder="Please enter your passwd" v-model="tData.resetForm.password" type="password" class="input">
            <p class="err-view">
            </p>
          </div>
        </div>
      </div>
      <div class="regist-padding">
        <div class="common-input">
          <div class="input-view">
            <input placeholder="Please enter your passwd again" v-model="tData.resetForm.repassword" type="password" class="input">
            <p class="err-view">
            </p>
          </div>
        </div>
      </div>

      <div class="tel-login">
        <div class="next-btn-view">
          <button class="next-btn" @click="handleResetPassword">Reset Passwd</button>
        </div>
      </div>
    </div>
  </div>
</template>


<script setup lang="ts">
import {sendResetPasswordEmailApi} from '/@/api/user'
import {resetPwdApi} from '/@/api/user'
import {message} from "ant-design-vue";
import MailIcon from '/@/assets/images/mail-icon.svg';
import PwdIcon from '/@/assets/images/pwd-icon.svg';

const router = useRouter();

const tData = reactive({
  resetForm: {
    username: '',
    email: '',
    code: '',
    password: '',
    repassword: ''
  }
})

const handleSendEmail = () => {
  const { username, email } = tData.resetForm;

  // 输入验证
  if (!username || !email) {
    message.warn('Username and email cannot be empty！');
    return;
  }

  // 邮箱格式验证
  const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (!emailPattern.test(email)) {
    message.warn('Please enter a valid email address！');
    return;
  }

  // 发送重置密码邮件请求
  sendResetPasswordEmailApi({ username, email })
    .then(res => {
      message.success('The reset email has been sent. Please check your email！');
    })
    .catch(err => {
      message.error(err.msg || 'Sending failed, please try again');
    });
}

const handleResetPassword = () => {
  console.log('login')
    if(tData.resetForm.username === ''
      || tData.resetForm.email === ''
      || tData.resetForm.code === ''
      || tData.resetForm.password === ''
      || tData.resetForm.repassword === ''){
      message.warn('Cannot be Null！')
      return;
    }

    resetPwdApi({
      username: tData.resetForm.username,
      email: tData.resetForm.email,
      code: tData.resetForm.code,
      password: tData.resetForm.password,
      rePassword: tData.resetForm.repassword
    }).then(res => {
      message.success('Modified successfully！')
      router.push({name: 'login'})
    }).catch(err => {
      message.error(err.msg || 'Modification failed')
    })
}

</script>

<style scoped lang="less">
div {
  display: block;
}

*, :after, :before, img {
  border-style: none;
}

*, :after, :before {
  border-width: 0;
  border-color: #dae1e7;
}

.container {
  // background-color: #f1f1f1; // 可以保留或删除
  background-image: url('../images/admin-login-bg.jpg');
  background-size: cover;
  object-fit: cover;
  height: 100%;
  max-width: 100%;
  display: flex;
  justify-content: flex-end; // 将内容推到右边
  align-items: center;
}

.pc-style {
  position: relative;
  width: 400px;
  height: 580px;
  background: #fff;
  border-radius: 4px;
  margin-right: 200px; // 可选，增加右边距
  -webkit-box-shadow: 2px 2px 6px #aaa;
  box-shadow: 2px 2px 6px #aaa;
}

.tel-regist-page {
  overflow: hidden;

  .regist-title {
    font-size: 14px;
    color: #1e1e1e;
    font-weight: 500;
    height: 24px;
    line-height: 24px;
    margin: 40px 0;
    padding: 0 28px;

    .toWxLogin {
      color: #3d5b96;
      float: right;
      cursor: pointer;
    }
  }

  .regist-padding {
    padding: 0 28px;
    margin-bottom: 8px;
  }
}

.common-input {
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-align: start;
  -ms-flex-align: start;
  align-items: flex-start;

  .left-icon {
    margin-right: 12px;
    width: 24px;
  }

  .input-view {
    -webkit-box-flex: 1;
    -ms-flex: 1;
    flex: 1;

    .input {
      font-weight: 500;
      font-size: 14px;
      color: #1e1e1e;
      height: 26px;
      line-height: 26px;
      padding: 0;
      display: block;
      width: 100%;
      letter-spacing: 1.5px;
      outline: none; // 去掉边框线
    }

    .err-view {
      margin-top: 4px;
      height: 16px;
      line-height: 16px;
      font-size: 12px;
      color: #f62a2a;
    }
  }
}

.tel-login {
  padding: 0 28px;
}

.next-btn {
  background: linear-gradient(90deg, #4e73df, #1cc88a);
  border-radius: 4px;
  color: #fff;
  font-size: 14px;
  font-weight: 500;
  height: 40px;
  line-height: 40px;
  text-align: center;
  width: 100%;
  outline: none;
  cursor: pointer;
}

</style>
