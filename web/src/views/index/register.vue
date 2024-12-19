<template>
  <div class="container">
    <div class="tel-regist-page pc-style">
      <div class="regist-title">
        <span>Register</span>
        <span @click="router.push({name:'login'})" class="toWxLogin">Login</span>
      </div>

      <div class="regist-padding">
        <div class="common-input">
          <div class="input-view">
            <input placeholder="Username: at least 8 characters." v-model="tData.loginForm.username" type="text" class="input">
            <p class="err-view">{{ usernameError }}</p>
          </div>
        </div>
      </div>
      <div class="regist-padding">
        <div class="common-input">
          <div class="input-view">
            <input placeholder="Please enter a valid email address." v-model="tData.loginForm.email" type="text" class="input">
            <p class="err-view">{{ emailError }}</p>
          </div>
        </div>
      </div>
      <div class="regist-padding">
        <div class="common-input">
          <div class="input-view">
            <input placeholder="Please enter your phone number" v-model="tData.loginForm.mobile" type="text" class="input">
            <p class="err-view">{{ mobileError }}</p>
          </div>
        </div>
      </div>
      <div class="regist-padding">
        <div class="common-input">
          <div class="input-view">
            <input placeholder="Please enter your passwd" v-model="tData.loginForm.password" type="password" class="input">
            <p class="err-view">{{ passwordError }}</p>
          </div>
        </div>
      </div>
      <div class="regist-padding">
        <div class="common-input">
          <div class="input-view">
            <input placeholder="Please enter your passwd again" v-model="tData.loginForm.repassword" type="password" class="input">
            <p class="err-view">{{ repasswordError }}</p>
          </div>
        </div>
      </div>
      <div class="tel-login">
        <div class="next-btn-view">
          <button class="next-btn" @click="handleRegister">Register</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { userRegisterApi } from '/@/api/user';
import { message } from "ant-design-vue";
import { reactive, ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

const tData = reactive({
  loginForm: {
    username: '',
    email: '',
    mobile: '',
    password: '',
    repassword: ''
  }
});

// Error messages
const usernameError = ref('');
const emailError = ref('');
const mobileError = ref('');
const passwordError = ref('');
const repasswordError = ref('');

const validateForm = () => {
  let valid = true;
  usernameError.value = '';
  emailError.value = '';
  mobileError.value = '';
  passwordError.value = '';
  repasswordError.value = '';

  if (tData.loginForm.username.length < 8) {
    usernameError.value = 'Username must be at least 8 characters.';
    valid = false;
  }

  if (!/\S+@\S+\.\S+/.test(tData.loginForm.email)) {
    emailError.value = 'Please enter a valid email address.';
    valid = false;
  }

  if (tData.loginForm.mobile === '') {
    mobileError.value = 'Phone number cannot be empty.';
    valid = false;
  }

  if (tData.loginForm.password.length < 6 || !/[A-Z]/.test(tData.loginForm.password) || !/\d/.test(tData.loginForm.password)) {
    passwordError.value = 'Password must be at least 6 characters and contain at least one uppercase letter and one number.';
    valid = false;
  }

  if (tData.loginForm.password !== tData.loginForm.repassword) {
    repasswordError.value = 'Passwords do not match.';
    valid = false;
  }

  return valid;
};

const handleRegister = () => {
  if (!validateForm()) {
    message.warn('Please correct the errors before submitting.');
    return;
  }

  userRegisterApi({
    username: tData.loginForm.username,
    email: tData.loginForm.email,
    mobile: tData.loginForm.mobile,
    password: tData.loginForm.password,
    rePassword: tData.loginForm.repassword
  }).then(res => {
    message.success('Register Successfully！');
    router.push({ name: 'login' });
  }).catch(err => {
    message.error(err.msg || 'Register Failed');
  });
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
  height: 540px;
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
  background: linear-gradient(90deg, #4e73df, #1cc88a);;
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
