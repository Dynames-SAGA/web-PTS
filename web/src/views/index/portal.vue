<template>
  <div class="portal">
    <Header />
    <Carousel />
    <Content />
    <Footer />
  </div>
</template>
<script>
import Header from '/@/views/index/components/header.vue'
import Footer from '/@/views/index/components/footer.vue'
import Content from '/@/views/index/components/content.vue'
import Carousel from '/@/views/index/components/carousel.vue'

export default {
  components: {
    Footer,
    Header,
    Content,
    Carousel
  },
  data () {
    return {
    }
  }
}
</script>
<style scoped lang="less">

</style>
