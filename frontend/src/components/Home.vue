<template>
  <div>
    <h1>大模型测试平台</h1>
    <button @click="goToQuestions">查询题目</button>
  </div>
</template>

<script>
export default {
  name: 'Home',
  methods: {
    goToQuestions() {
      this.$router.push('/questions')
    }
  }
}
</script>

<style scoped>
button {
  padding: 10px 20px;
  background-color: #42b983;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
}

button:hover {
  background-color: #3aa876;
}
</style>