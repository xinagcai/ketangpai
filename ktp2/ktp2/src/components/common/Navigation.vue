<template>
  <div class="nav">
    <div class="content">
      <div><slot name="left"></slot></div>
      <div><slot name="center"></slot></div>
      <div><slot name="right"></slot></div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Navigation',
}
</script>

<style scoped>
  .nav {
    width: 100%;
    height: 72px;
    position: fixed;
    z-index: inherit;
    top: 0;
    left: 0;
  }
  .content {
    width: 90%;
    margin: 0 auto;
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    height: 100%;
  }
</style>