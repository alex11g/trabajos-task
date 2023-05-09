const { createApp } = Vue

const app = createApp({

    data() {

        return {
            datos: [],
            datos_Debit: [],
            datos_Credit: [],

        }
    },
    created() {
        axios.get("http://localhost:8080/api/clients/current")
            .then(response => {
                this.datos = response.data
                this.datos_Debit = this.datos.cardDTOS.filter(valor => valor.type == "DEBIT")
                this.datos_Credit = this.datos.cardDTOS.filter(valor => valor.type == "CREDIT")
                console.log(this.datos_Debit)
                console.log(this.datos_Credit)
                console.log(this.datos)

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
        }
    },
})
app.mount("#app")