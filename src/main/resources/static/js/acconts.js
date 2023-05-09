const { createApp } = Vue

const app = createApp({

    data() {

        return {
            datos: [],

        }
    },
    created() {
        axios.get("http://localhost:8080/api/clients/current" )
            .then(response =>{
                this.datos= response.data
                console.log(this.datos);
            })
            .catch(error => console.log(error))
        
    },
    methods: {
        logout() {
            Swal.fire({
                title: 'Are you sure you want to log out?',
                showCancelButton: true,
                confirmButtonText: 'Yes',
                showLoaderOnConfirm: true,
                preConfirm: (login) => {
                    return axios.post('/api/logout')
                        .then(response => {
                            window.location.href = "/index.html"
                        })
                        .catch(error => {
                            Swal.showValidationMessage(
                                'Request failed: ${error}'
                            )
                        })
                },
                allowOutsideClick: () => !Swal.isLoading()
            })
        },
        add() {
            Swal.fire({
                title: 'Are you sure you want to add another account?',
                showCancelButton: true,
                confirmButtonText: 'Yes',
                showLoaderOnConfirm: true,
                preConfirm: (login) => {
                    return axios.post('/api/clients/current/accounts')
                        .then(response => {
                            window.location.href = "/acconts.html"
                        })
                        .catch(error => {
                            Swal.showValidationMessage(
                                'Request failed: ${error}'
                            )
                        })
                },
                allowOutsideClick: () => !Swal.isLoading()
            })
        }
    },
   
	
})
app.mount("#app")