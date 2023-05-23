const { createApp } = Vue

const app = createApp({

    data() {

        return {
            loans: [],
            accounts: [],
            datos2: [],
            interest: "",
            account: "",
            filter: "",
            payments: "",
            loanType: '',
            Payments2: "",
            amount: 0,
            quotas: 0,
            loanID: '',
            monto: "",
           

        }
    },
    created() {
        axios.get("http://localhost:8080/api/clients/current")
            .then(response => {
                this.accounts = response.data;
                this.datos2 = this.accounts.accountDTOS.filter(valor => valor.active)
                console.log(this.datos2)
            })
            .catch(error => console.log(error))



        axios.get('http://localhost:8080/api/loans')
            .then(response => {
                this.loans = response.data;
                console.log(this.loans)



            })
            .catch(error => console.log(error));


    },
    methods: {
        loanCreate() {
            this.loanID = this.filter.id;
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
                        .post('/api/loans', {
                            id: this.loanType,
                            amount: this.amount,
                            payments: this.payments,
                            accountDestiny: this.account,
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
        },
        computed: {
            FilterPayments() {
                this.filter = this.loans.filter(loan => loan.id == this.loanType)
                this.Payments2 = this.filter.map(valor => valor.payments)[0]
                this.monto = this.filter.map(valor => valor.maxAmount)[0]
                this.interest = this.filter.map(valor2 => valor2.interest)[0]





            },

        }








})
app.mount("#app")