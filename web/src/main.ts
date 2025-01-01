import { createApp } from 'vue';
import ElementPlus from 'element-plus'; // 引入 Element Plus 库
import 'element-plus/dist/index.css'; // 引入 Element Plus 的样式文件
import App from './App.vue';
import router from './router';
import piniaStore from './store';

import bootstrap from './core/bootstrap';
import '/@/styles/reset.less';
import '/@/styles/index.less';
import Antd from 'ant-design-vue';

const app = createApp(App);

app.use(ElementPlus)

app.use(ElementPlus);
app.use(Antd);
app.use(router);
app.use(piniaStore);
app.use(bootstrap)
app.mount('#app');
