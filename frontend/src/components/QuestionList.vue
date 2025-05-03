<template>
  <div>
    <h1>题目列表</h1>
    <table>
      <thead>
      <tr>
        <th>ID</th>
        <th>题目</th>
        <th>答案</th>
        <th>数据来源</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="question in questions" :key="question.dataId">
        <td>{{ question.dataId }}</td>
        <td>{{ question.question }}</td>
        <td>{{ question.answer }}</td>
        <td>{{ question.dataSource }}</td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'QuestionList',
  data() {
    return {
      questions: []
    }
  },
  created() {
    this.fetchQuestions()
  },
  methods: {
    fetchQuestions() {
      axios.get('/api/dataInfo/select')
        .then(response => {
          console.log('后端返回数据:', response.data)
          this.questions = response.data
        })
        .catch(error => {
          console.error('获取题目失败:', error)
        })
    }
  }
}
</script>

<style scoped>
table {
  width: 80%;
  margin: 20px auto;
  border-collapse: collapse;
}

th, td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: left;
}

th {
  background-color: #f2f2f2;
}

tr:nth-child(even) {
  background-color: #f9f9f9;
}
</style>