const auth_base_url = '/api/v1'
const auth = () => {
    if (!sessionStorage.getItem('token')) {
        axios({
            method: 'post',
            url: `${auth_base_url}/login`,
            data: {
                username: prompt('Enter your username', ''),
                password: prompt('Enter your password', ''),
            }
        })
            .then(
                (response) => {
                    console.log(`${response.data.message} with status code ${response.status}`)
                    sessionStorage.setItem('token', response.data.token)
                }
            )
            .catch(
                (error) => {
                    console.log(`${error.response.data.message} with status code ${error.response.status}`)
                    alert('Login failed. Try again?')
                    window.location.reload()
                }
            )
    }
}