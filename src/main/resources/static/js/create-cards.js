const { createApp } = Vue

createApp({
    data() {
        return {
            TYPE: "",
            COLOR: "",




        };
    },
    created() {

    },
    methods: {
        addCards() {
            Swal.fire({
                title: 'Create card?',
                inputAttributes: {
                    autocapitalize: 'off'
                },
                showCancelButton: true,
                confirmButtonText: 'Ok',
                showLoaderOnConfirm: true,
                preConfirm: () => {
                    return axios.post('/api/clients/current/cards', "type=" + this.TYPE + "&color=" + this.COLOR)
                        .then(response => window.location.href = "/cards.html")
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
        }
    }

}).mount('#app')