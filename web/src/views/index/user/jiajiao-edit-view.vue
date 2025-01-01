<template>
  <div class="content-list">
    <div class="list-title">Tutoring Registration</div>
    <a-spin :spinning="loading" style="min-height: 200px;">
      <div class="list-content">
        <div class="edit-view">
          <div class="item flex-view">
            <div class="label">Avatar</div>
            <div class="right-box avatar-box flex-view">
              <img v-if="tData.form && tData.form.avatar" :src="tData.form.avatar" class="avatar">
              <img v-else :src="AvatarIcon" class="avatar">
              <div class="change-tips flex-view">
                <a-upload
                    name="file"
                    accept="image/*"
                    :multiple="false"
                    :before-upload="beforeUpload"
                >
                  <span class="avatar-upload-button">change avatar</span>
                </a-upload>
                <p class="tip">Image formats supported: GIF, PNG, JPEG; size not less than 200 PX and less than 4 MB</p>
              </div>

            </div>
          </div>
          <div class="item flex-view">
            <div class="label">Name</div>
            <div class="right-box">
              <input type="text" v-model="tData.form.title" placeholder="Please enter name" maxlength="20" class="input-dom">
              <p class="tip">Supports Chinese and English, length cannot exceed 20 characters</p>
            </div>
          </div>
          <div class="item flex-view">
            <div class="label">Gender</div>
            <div class="right-box">
              <input type="text" v-model="tData.form.sex" placeholder="Please enter gender" maxlength="100" class="input-dom web-input">
            </div>
          </div>
          <div class="item flex-view">
            <div class="label">Age</div>
            <div class="right-box">
              <input type="number" v-model="tData.form.age" placeholder="Please enter age" maxlength="100" class="input-dom web-input">
            </div>
          </div>
          <div class="item flex-view">
            <div class="label">Location</div>
            <div class="right-box">
              <input type="text" v-model="tData.form.location" placeholder="Please enter location" maxlength="100" class="input-dom web-input">
            </div>
          </div>
          <div class="item flex-view">
            <div class="label">Hourly Price</div>
            <div class="right-box">
              <input type="number" v-model="tData.form.price" placeholder="Please enter price" maxlength="100" class="input-dom web-input">
            </div>
          </div>
          <div class="item flex-view">
            <div class="label">Phone Number</div>
            <div class="right-box">
              <input type="number" v-model="tData.form.mobile" placeholder="Please enter phone number" maxlength="100" class="input-dom web-input">
            </div>
          </div>
          <div class="item flex-view">
            <div class="label">Subject</div>
            <div class="right-box">
              <a-select placeholder="Please select"
                        allowClear
                        :options="cData"
                        style="width: 200px;"
                        :field-names="{ label: 'title', value: 'id',}"
                        v-model:value="tData.form.classificationId">
              </a-select>
            </div>
          </div>
          <div class="item flex-view">
            <div class="label">Personal Introduction</div>
            <div class="right-box">
          <textarea v-model="tData.form.description" placeholder="Please enter introduction" maxlength="200" class="intro">
          </textarea>
              <p class="tip">Limit to 200 characters</p>
            </div>
          </div>
          <button class="save mg" @click="submit()">Save</button>
        </div>
      </div>
    </a-spin>
  </div>
</template>

<script setup>
import {message} from "ant-design-vue";
import {listUserThingApi, updateApi, createApi} from '/@/api/thing'
import {listApi as listClassificationApi} from '/@/api/classification'
import {BASE_URL} from "/@/store/constants";
import {useUserStore} from "/@/store";
import AvatarIcon from '/@/assets/images/avatar.jpg'

const router = useRouter();
const userStore = useUserStore();

let loading = ref(false)
let tData = reactive({
  form:{
    avatar: undefined,
    avatarFile: undefined,
    title: undefined,
    age: undefined,
    sex: undefined,
    price: undefined,
    mobile: undefined,
    location: undefined,
    description: undefined,
    classificationId: undefined,
  }
})

let cData = ref([])

onMounted(()=>{
  getCDataList()
  getUserThing()
})

const beforeUpload =(file)=> {
  // 改文件名
  const fileName = new Date().getTime().toString() + '.' + file.type.substring(6)
  const copyFile = new File([file], fileName)
  console.log(copyFile)
  tData.form.avatarFile = copyFile
  return false
}

const getCDataList = () => {
  listClassificationApi({}).then(res => {
    cData.value = res.data
  })
}

const getUserThing =()=> {
  loading.value = true
  let userId = userStore.user_id
  listUserThingApi({userId: userId}).then(res => {
    console.log(res)
    if(res.data && res.data.length > 0){
      tData.form = res.data[0]
    }
    if (tData.form.cover) {
      tData.form.avatar = BASE_URL + '/api/staticfiles/image/'  + tData.form.cover
    }
    loading.value = false
  }).catch(err => {
    console.log(err)
    loading.value = false
  })
}
const submit =()=> {
  let formData = new FormData()
  let userId = userStore.user_id
  if (tData.form.avatarFile) {
    formData.append('imageFile', tData.form.avatarFile)
  }
  if (tData.form.title) {
    formData.append('title', tData.form.title)
  }else {
    message.warn("Name cannot be empty")
    return
  }
  if (tData.form.id) {
    formData.append('id', tData.form.id)
  }
  if (tData.form.classificationId) {
    formData.append('classificationId', tData.form.classificationId)
  }
  if (tData.form.sex) {
    formData.append('sex', tData.form.sex)
  }else {
    message.warn("Gender cannot be empty")
    return
  }

  if (tData.form.age) {
    formData.append('age', tData.form.age)
  }else {
    message.warn("Age cannot be empty")
    return
  }
  if (tData.form.mobile) {
    formData.append('mobile', tData.form.mobile)
  }else {
    message.warn("Phone number cannot be empty")
    return
  }
  if (tData.form.location) {
    formData.append('location', tData.form.location)
  }else {
    message.warn("Location cannot be empty")
    return
  }
  if (tData.form.price) {
    formData.append('price', tData.form.price)
  }else {
    message.warn("Price cannot be empty")
    return
  }
  if (tData.form.description) {
    formData.append('description', tData.form.description)
  }else {
    message.warn("Introduction cannot be empty")
    return
  }
  formData.append('userId', userId)
  formData.append('status', '1')

  if(tData.form.id) {
    updateApi(formData).then(res => {
      message.success('Saved successfully, pending review')
      getUserThing()
    }).catch(err => {
      console.log(err)
    })
  }else {
    createApi(formData).then(res => {
      message.success('Saved successfully, pending review')
      getUserThing()
    }).catch(err => {
      console.log(err)
    })
  }

}

</script>

<style scoped lang="less">
input, textarea {
  border-style: none;
  outline: none;
  margin: 0;
  padding: 0;
}

.flex-view {
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
}

.content-list {
  -webkit-box-flex: 1;
  -ms-flex: 1;
  flex: 1;

  .list-title {
    color: #152844;
    font-weight: 600;
    font-size: 18px;
    line-height: 48px;
    height: 48px;
    margin-bottom: 4px;
    border-bottom: 1px solid #cedce4;
  }

  .edit-view {
    .item {
      -webkit-box-align: center;
      -ms-flex-align: center;
      align-items: center;
      margin: 24px 0;

      .label {
        width: 100px;
        color: #152844;
        font-weight: 600;
        font-size: 14px;
      }

      .right-box {
        -webkit-box-flex: 1;
        -ms-flex: 1;
        flex: 1;
      }

      .avatar {
        width: 64px;
        height: 64px;
        border-radius: 50%;
        margin-right: 16px;
      }

      .change-tips {
        -webkit-box-align: center;
        -ms-flex-align: center;
        align-items: center;
        -ms-flex-wrap: wrap;
        flex-wrap: wrap;
      }

      label {
        color: #4684e2;
        font-size: 14px;
        line-height: 22px;
        height: 22px;
        cursor: pointer;
        width: 100px;
        display: block;
      }

      .tip {
        color: #6f6f6f;
        font-size: 14px;
        height: 22px;
        line-height: 22px;
        margin: 0;
        width: 100%;
      }

      .right-box {
        -webkit-box-flex: 1;
        -ms-flex: 1;
        flex: 1;
      }

      .input-dom {
        width: 400px;
      }

      .input-dom {
        background: #f8fafb;
        border-radius: 4px;
        height: 40px;
        line-height: 40px;
        font-size: 14px;
        color: #152844;
        padding: 0 12px;
      }

      .tip {
        font-size: 12px;
        line-height: 16px;
        color: #6f6f6f;
        height: 16px;
        margin-top: 4px;
      }

      .intro {
        resize: none;
        background: #f8fafb;
        width: 100%;
        padding: 8px 12px;
        height: 82px;
        line-height: 22px;
        font-size: 14px;
        color: #152844;
      }
    }

    .save {
      background: #4684e2;
      border-radius: 32px;
      width: 96px;
      height: 32px;
      line-height: 32px;
      font-size: 14px;
      color: #fff;
      border: none;
      outline: none;
      cursor: pointer;
    }

    .mg {
      margin-left: 100px;
    }
  }
}

.avatar-upload-button {
  background-color: #4684e2; /* 主背景色 */
  color: white; /* 字体颜色 */
  padding: 8px 16px; /* 内边距 */
  border-radius: 4px; /* 圆角 */
  cursor: pointer; /* 鼠标效果 */
  transition: background-color 0.3s; /* 动效 */
}

.avatar-upload-button:hover {
  background-color: #357ae8; /* hover 时的背景色 */
}

.right-box {
  margin-left: 20px; /* 增加左边距，向右移动 */
}

</style>
