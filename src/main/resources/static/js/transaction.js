const { createApp } = Vue

createApp({
    data() {
        return {
            ENVIAR: "",
            RECIBIR:"",
            MONTO:"",
            DESCRIPTION:"",
            N_CUENTA: "",
            data:[],
            datos2: [],
            mostrarImagen: 1,
        };
    },
    created() {
        axios.get('http://localhost:8080/api/clients/current')
        .then(response => {
            this.data = response.data
            this.datos2 = this.data.accountDTOS.filter(valor => valor.active)
            console.log(this.data);

        })
        .catch(error => console.log(error));
    },
    methods: {
        addTransfer() {
            Swal.fire({
                title: 'Create a transfers',
                inputAttributes: {
                    autocapitalize: 'off'
                },
                showCancelButton: true,
                confirmButtonText: 'Ok',
                showLoaderOnConfirm: true,
                preConfirm: () => {
                    return axios.post('/api/transaction',"amount="+this.MONTO+"&description="+this.DESCRIPTION +"&account1="+this.ENVIAR+"&account2="+this.RECIBIR)
                     
                        .then(response => window.location.href = "/acconts.html")
                        .catch(error => {
                            Swal.fire({
                                title: "There is a problem!",
                                text: error.response.data,
                                icon: "error",
                                confirmButtonColor: "#009269",
                            })
                            console.log(error)
                        })

                },
                allowOutsideClick: () => !Swal.isLoading()
            })
        },
        CUENTA_RECIBE() {
            Swal.fire({
                title: 'Create a transfers?',
                inputAttributes: {
                    autocapitalize: 'off'
                },
                showCancelButton: true,
                confirmButtonText: 'Ok',
                showLoaderOnConfirm: true,
                preConfirm: () => {
                    return axios.post('/api/transaction',"amount="+this.MONTO+"&description="+this.DESCRIPTION +"&account1="+this.ENVIAR+"&account2="+this.N_CUENTA)
                     
                        .then(response => window.location.href = "/acconts.html")
                        .catch(error => {
                            Swal.fire({
                                title: "There is a problem!",
                                text: error.response.data,
                                icon: "error",
                                confirmButtonColor: "#009269",
                            })
                            console.log(error)
                        })

                },
                allowOutsideClick: () => !Swal.isLoading()
            })
        },
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
        mostrar1() {
            this.mostrarImagen = 1;
          },
          mostrar2() {
            this.mostrarImagen = 2;
          }
    }

}).mount('#app')