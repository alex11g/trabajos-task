const { createApp } = Vue

const app = createApp({

    data() {

        return {
            datos: [],
            datos_Debit: [],
            datos_Credit: [],
            datos_active : []

        }
    },
    created() {
        axios.get("http://localhost:8080/api/clients/current")
            .then(response => {
                this.datos = response.data
                this.datos_Debit = this.datos.cardDTOS.filter(valor => valor.type == "DEBIT" && valor.active)
                this.datos_Credit = this.datos.cardDTOS.filter(valor => valor.type == "CREDIT" && valor.active)
                this.datos_active = this.datos.cardDTOS.filter(valor => valor.active)
                console.log(this.datos_Debit)
                console.log(this.datos_Credit)
             

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
        EliminarCards(id){
            Swal.fire({
                title: 'Are you sure that you want to delete this card?',
                inputAttributes: {
                    autocapitalize: 'off'
                },
                showCancelButton: true,
                confirmButtonText: 'Sure',
                confirmButtonColor: "#7c601893",
                preConfirm: () => {
                    return axios.put('/api/clients/current/cards' + `?id=${id}`)
                        .then(response => {
                            window.location.href="/cards.html"
                        })
                        .catch(error => {
                            Swal.showValidationMessage(
                                `Request failed: ${error.response.data}`
                            )
                        })
                },
                allowOutsideClick: () => !Swal.isLoading()
            })
        },
    }
   
})
app.mount("#app")