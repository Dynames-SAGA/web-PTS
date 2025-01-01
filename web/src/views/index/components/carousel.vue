<template>
  <div class="block text-center" style="margin: 56px;">
    <el-carousel height="180px" autoplay>
      <el-carousel-item v-for="(item, index) in images" :key="index">
        <img :src="item" class="image" alt="Banner Image">
      </el-carousel-item>
    </el-carousel>
  </div>
</template>

<script>
export default {
  name: 'Index',
  data() {
    return {
      images: [
        '/src/assets/images/Chris3.png',
        '/src/assets/images/Chris1.png', // 替换为实际的图片路径
        '/src/assets/images/Chris2.png',
        '/src/assets/images/Chris4.png'
      ]
    }
  }
}
</script>

<style scoped>
.demonstration {
  color: var(--el-text-color-secondary);
}

.el-carousel__item {
  display: flex; /* 使图片居中显示 */
  justify-content: center;
  align-items: center;
  height: 100%; /* 确保轮播项高度为100% */
}

.image {
  width: 100%; /* 使图片适应容器宽度 */
  height: 100%; /* 设置高度为容器高度 */
  object-fit: cover; /* 填满容器，可能裁剪部分图片 */
  object-position: center; /* 图片居中显示 */
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n + 1) {
  background-color: #d3dce6;
}
</style>