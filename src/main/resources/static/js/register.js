const { createApp } = Vue

createApp({
    data() {
        return {
            firstname: "",
            lastname: "",
            password: "",
            email: "",



        };
    },
    created() {

    },
    methods: {
        postClient() {

            Swal.fire({
                title: 'register with these data?',
                inputAttributes: {
                    autocapitalize: 'off'
                },
                showCancelButton: true,
                confirmButtonText: 'Ok',
                showLoaderOnConfirm: true,
                preConfirm: (login) => {
                    return axios.post('/api/clients',
                        "firstName=" + this.firstname +
                        "&lastName=" + this.lastname +
                        "&email=" + this.email +
                        "&password=" + this.password,
                        { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                        .then(() => {
                            window.location.href = "index.html"
                        })
                        .catch(error => {
                            {
                                Swal.fire({
                                    icon: 'error',
                                    text: 'the mail already exists!',
                                })
                            }
                        })
                },
                allowOutsideClick: () => !Swal.isLoading()
            })


                


        },
    }

}).mount('#app')