const { createApp } = Vue

const app = createApp({

    data() {

        return {
            loans: [],
            accounts: [],
            interest: "",
            account: "",
            filter: "",
            loanType: '',
            Payments2: "",
            amount: 0,
            quotas: 0,
            name: "",
            monto: "",
            interest: [],
            payments: [],
            

        }
    },
    created() {
        axios.get("/api/clients/current")
            .then(response => {
                this.accounts = response.data;
                console.log(this.accounts)
            })
            .catch(error => console.log(error))
        axios.get('/api/loans')
            .then(response => {
                this.loans = response.data;
                console.log(this.loans)
            })
            .catch(error => console.log(error));
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
        loanCreate2() {
            this.loanID = this.filter.id;
            console.log(this.name)
            console.log(this.amount)
            console.log(this.payments)
            console.log(this.interest)
            Swal.fire({
                title: 'Are you sure you want to apply for this loan?',
                inputAttributes: {
                    autocapitalize: 'off',
                },
                showCancelButton: true,
                confirmButtonText: 'Sure',
                confirmButtonColor: '#7c601893',
                preConfirm: () => {
                    return axios
                        .post('/api/loans/manager', {
                            name: this.name,
                            maxAmount: this.amount,
                            payments: this.payments,
                            interest : this.interest,
                        })
                        .then(response => {
                            Swal.fire({
                                icon: 'success',
                                text: 'Your loan was approved',
                                showConfirmButton: false,
                                timer: 2000,
                            }).then(() => (window.location.href = '/acconts.html'));
                        })
                        .catch(error => {
                            Swal.fire({
                                icon: 'error',
                                text: error.response.data,
                                confirmButtonColor: '#7c601893',
                            });
                            console.log(error);
                        });
                },
            });
        },
    }








})
app.mount("#app")