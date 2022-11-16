const app = new Vue({
    el: '#app',
    delimiters: ['[[', ']]'],
    created() {
        auth()
    }
})