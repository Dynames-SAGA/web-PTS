<template>
  <div>
    <!--页面区域-->
    <div class="page-view">
      <div class="table-operations">
        <a-space>
          <a-button type="primary" @click="handleAdd">Add</a-button>
          <a-button @click="handleBatchDelete">Delete</a-button>
          <a-input-search addon-before="Name" enter-button @search="onSearch" @change="onSearchChange" />
        </a-space>
      </div>
      <a-table
        size="middle"
        rowKey="id"
        :loading="data.loading"
        :columns="columns"
        :data-source="data.dataList"
        :scroll="{ x: 'max-content' }"
        :row-selection="rowSelection"
        :pagination="{
          size: 'default',
          current: data.page,
          pageSize: data.pageSize,
          onChange: (current) => (data.page = current),
          showSizeChanger: false,
          showTotal: (total) => `Total ${total} datas`,
        }"
      >
        <template #bodyCell="{ text, record, index, column }">
          <template v-if="column.key === 'operation'">
            <span>
              <a @click="handleEdit(record)">Edit</a>
              <a-divider type="vertical" />
              <a-popconfirm title="Confirm" ok-text="Yes" cancel-text="No" @confirm="confirmDelete(record)">
                <a href="#">Delete</a>
              </a-popconfirm>
            </span>
          </template>
        </template>
      </a-table>
    </div>

    <!--弹窗区域-->
    <div>
      <a-modal
        :visible="modal.visile"
        :forceRender="true"
        :title="modal.title"
        width="880px"
        ok-text="Yes"
        cancel-text="No"
        @cancel="handleCancel"
        @ok="handleOk"
      >
        <div>
          <a-form ref="myform" :label-col="{ style: { width: '80px' } }" :model="modal.form" :rules="modal.rules">
            <a-row :gutter="24">
              <a-col span="24">
                <a-form-item label="Name" name="title">
                  <a-input placeholder="Please enter" v-model:value="modal.form.title" />
                </a-form-item>
              </a-col>
              <a-col span="12">
                <a-form-item label="Subject" name="classificationId">
                  <a-select
                    placeholder="Please choose"
                    allowClear
                    :options="modal.cData"
                    :field-names="{ label: 'title', value: 'id' }"
                    v-model:value="modal.form.classificationId"
                  />
                </a-form-item>
              </a-col>
<!--              <a-col span="12">-->
<!--                <a-form-item label="标签">-->
<!--                  <a-select mode="multiple" placeholder="请选择" allowClear v-model:value="modal.form.tags">-->
<!--                    <template v-for="item in modal.tagData">-->
<!--                      <a-select-option :value="item.id">{{ item.title }}</a-select-option>-->
<!--                    </template>-->
<!--                  </a-select>-->
<!--                </a-form-item>-->
<!--              </a-col>-->
              <a-col span="24">
                <a-form-item label="Avatar">
                  <a-upload-dragger
                    name="file"
                    accept="image/*"
                    :multiple="false"
                    :before-upload="beforeUpload"
                    v-model:file-list="fileList"
                  >
                    <p class="ant-upload-drag-icon">
                      <template v-if="modal.form.coverUrl">
                        <img :src="modal.form.coverUrl" style="width: 60px; height: 80px" />
                      </template>
                      <template v-else>
                        <file-image-outlined />
                      </template>
                    </p>
                    <p class="ant-upload-text"> Please choose your avatar file </p>
                  </a-upload-dragger>
                </a-form-item>
              </a-col>

              <a-col span="24">
                <a-form-item label="Introduction">
                  <a-textarea placeholder="Please enter" v-model:value="modal.form.description" />
                </a-form-item>
              </a-col>
              <a-col span="12">
                <a-form-item label="Price" name="price">
                  <a-input-number placeholder="Please enter" :min="0" v-model:value="modal.form.price" style="width: 100%" />
                </a-form-item>
              </a-col>
              <a-col span="12">
                <a-form-item label="TelNumber">
                  <a-input-number placeholder="Please enter" :min="0" v-model:value="modal.form.mobile" style="width: 100%" />
                </a-form-item>
              </a-col>
              <a-col span="12">
                <a-form-item label="Age">
                  <a-input-number placeholder="Please enter" :min="0" v-model:value="modal.form.age" style="width: 100%" />
                </a-form-item>
              </a-col>
              <a-col span="12">
                <a-form-item label="Gender">
                  <a-input placeholder="Please enter" v-model:value="modal.form.sex" style="width: 100%" />
                </a-form-item>
              </a-col>
              <a-col span="12">
                <a-form-item label="District">
                  <a-input placeholder="Please enter" v-model:value="modal.form.location" style="width: 100%" />
                </a-form-item>
              </a-col>
              <a-col span="12">
                <a-form-item label="Status" name="status">
                  <a-select placeholder="Please choose" allowClear v-model:value="modal.form.status">
                    <a-select-option key="0" value="0">Agree</a-select-option>
                    <a-select-option key="1" value="1">Disagree</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>
        </div>
      </a-modal>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { FormInstance, message, SelectProps } from 'ant-design-vue';
  import { createApi, listApi, updateApi, deleteApi } from '/@/api/thing';
  import { listApi as listClassificationApi } from '/@/api/classification';
  import { listApi as listTagApi } from '/@/api/tag';
  import { BASE_URL } from '/@/store/constants';
  import { FileImageOutlined } from '@ant-design/icons-vue';

  const columns = reactive([
    {
      title: 'No',
      dataIndex: 'index',
      key: 'index',
      width: 60,
    },
    {
      title: 'Name',
      dataIndex: 'title',
      key: 'title',
    },
    {
      title: 'Price(￥)',
      dataIndex: 'price',
      key: 'price',
    },
    {
      title: 'Gender',
      dataIndex: 'sex',
      key: 'sex',
    },
    {
      title: 'Age',
      dataIndex: 'age',
      key: 'age',
    },
    {
      title: 'District',
      dataIndex: 'location',
      key: 'location',
    },
    {
      title: 'Introduction',
      dataIndex: 'description',
      key: 'description',
      customRender: ({ text, record, index, column }) => (text ? text.substring(0, 10) + '...' : '--'),
    },
    {
      title: 'Status',
      dataIndex: 'status',
      key: 'status',
      customRender: ({ text, record, index, column }) => (text === '0' ? 'Agree' : 'Disagree'),
    },
    {
      title: 'Opt',
      dataIndex: 'action',
      key: 'operation',
      align: 'center',
      fixed: 'right',
      width: 140,
    },
  ]);

  const beforeUpload = (file: File) => {
    // 改文件名
    const fileName = new Date().getTime().toString() + '.' + file.type.substring(6);
    const copyFile = new File([file], fileName);
    console.log(copyFile);
    modal.form.imageFile = copyFile;
    return false;
  };

  // 文件列表
  const fileList = ref<any[]>([]);

  // 页面数据
  const data = reactive({
    dataList: [],
    loading: false,
    keyword: '',
    selectedRowKeys: [] as any[],
    pageSize: 10,
    page: 1,
  });

  // 弹窗数据源
  const modal = reactive({
    visile: false,
    editFlag: false,
    title: '',
    cData: [],
    tagData: [{}],
    form: {
      id: undefined,
      title: undefined,
      classificationId: undefined,
      tags: [],
      repertory: undefined,
      price: undefined,
      mobile: undefined,
      age: undefined,
      sex: undefined,
      location: undefined,
      status: undefined,
      cover: undefined,
      coverUrl: undefined,
      imageFile: undefined,
    },
    rules: {
      title: [{ required: true, message: 'Please enter name', trigger: 'change' }],
      classificationId: [{ required: true, message: 'Please choose subject', trigger: 'change' }],
      price: [{ required: true, message: 'Please enter price', trigger: 'change' }],
      status: [{ required: true, message: 'Please choose status ', trigger: 'change' }],
    },
  });

  const myform = ref<FormInstance>();

  onMounted(() => {
    getDataList();
    getCDataList();
    getTagDataList();
  });

  const getDataList = () => {
    data.loading = true;
    listApi({
      keyword: data.keyword,
    })
      .then((res) => {
        data.loading = false;
        console.log(res);
        res.data.forEach((item: any, index: any) => {
          item.index = index + 1;
        });
        data.dataList = res.data;
      })
      .catch((err) => {
        data.loading = false;
        console.log(err);
      });
  };

  const getCDataList = () => {
    listClassificationApi({}).then((res) => {
      modal.cData = res.data;
    });
  };
  const getTagDataList = () => {
    listTagApi({}).then((res) => {
      res.data.forEach((item, index) => {
        item.index = index + 1;
      });
      modal.tagData = res.data;
    });
  };

  const onSearchChange = (e: Event) => {
    data.keyword = e?.target?.value;
    console.log(data.keyword);
  };

  const onSearch = () => {
    getDataList();
  };

  const rowSelection = ref({
    onChange: (selectedRowKeys: (string | number)[], selectedRows: DataItem[]) => {
      console.log(`selectedRowKeys: ${selectedRowKeys}`, 'selectedRows: ', selectedRows);
      data.selectedRowKeys = selectedRowKeys;
    },
  });

  const handleAdd = () => {
    resetModal();
    modal.visile = true;
    modal.editFlag = false;
    modal.title = 'Add';
    // 重置
    for (const key in modal.form) {
      modal.form[key] = undefined;
    }
    modal.form.cover = undefined;
  };
  const handleEdit = (record: any) => {
    resetModal();
    modal.visile = true;
    modal.editFlag = true;
    modal.title = 'Edit';
    // 重置
    for (const key in modal.form) {
      modal.form[key] = undefined;
    }
    for (const key in record) {
      if (record[key]) {
        modal.form[key] = record[key];
      }
    }
    if (modal.form.cover) {
      modal.form.coverUrl = BASE_URL + '/api/staticfiles/image/' + modal.form.cover;
      modal.form.cover = undefined;
    }
  };

  const confirmDelete = (record: any) => {
    console.log('delete', record);
    deleteApi({ ids: record.id })
      .then((res) => {
        getDataList();
      })
      .catch((err) => {
        message.error(err.msg || 'Failed');
      });
  };

  const handleBatchDelete = () => {
    console.log(data.selectedRowKeys);
    if (data.selectedRowKeys.length <= 0) {
      console.log('hello');
      message.warn('Please select delete items');
      return;
    }
    deleteApi({ ids: data.selectedRowKeys.join(',') })
      .then((res) => {
        message.success('delete successfully');
        data.selectedRowKeys = [];
        getDataList();
      })
      .catch((err) => {
        message.error(err.msg || 'failed');
      });
  };

  const handleOk = () => {
    myform.value
      ?.validate()
      .then(() => {
        const formData = new FormData();
        if (modal.editFlag) {
          formData.append('id', modal.form.id);
        }
        formData.append('title', modal.form.title);
        if (modal.form.classificationId) {
          formData.append('classificationId', modal.form.classificationId);
        }
        if (modal.form.tags) {
          modal.form.tags.forEach(function (value) {
            if (value) {
              formData.append('tags[]', value);
            }
          });
        }
        if (modal.form.imageFile) {
          formData.append('imageFile', modal.form.imageFile);
        }
        // formData.append('description', modal.form.description || '');
        formData.append('price', modal.form.price || '');
        if (modal.form.mobile) {
          formData.append('mobile', modal.form.mobile);
        }
        if (modal.form.age) {
          formData.append('age', modal.form.age);
        }
        if (modal.form.sex) {
          formData.append('sex', modal.form.sex);
        }
        if (modal.form.location) {
          formData.append('location', modal.form.location);
        }
        if (modal.form.description) {
          formData.append('description', modal.form.description);
        }
        if (modal.form.status) {
          formData.append('status', modal.form.status);
        }
        if (modal.editFlag) {
          updateApi(formData)
            .then((res) => {
              hideModal();
              getDataList();
            })
            .catch((err) => {
              console.log(err);
              message.error(err.msg || 'Failed');
            });
        } else {
          createApi(formData)
            .then((res) => {
              hideModal();
              getDataList();
            })
            .catch((err) => {
              console.log(err);
              message.error(err.msg || 'Failed');
            });
        }
      })
      .catch((err) => {
        console.log('Not null');
      });
  };

  const handleCancel = () => {
    hideModal();
  };

  // 恢复表单初始状态
  const resetModal = () => {
    myform.value?.resetFields();
    fileList.value = [];
  };

  // 关闭弹窗
  const hideModal = () => {
    modal.visile = false;
  };
</script>

<style scoped lang="less">
  .page-view {
    min-height: 100%;
    background: #fff;
    padding: 24px;
    display: flex;
    flex-direction: column;
  }

  .table-operations {
    margin-bottom: 16px;
    text-align: right;
  }

  .table-operations > button {
    margin-right: 8px;
  }
</style>
