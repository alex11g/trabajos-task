const { createApp } = Vue

const app = createApp({

    data() {

        return {
            datos: [],
            loans:[],
            loanID: [],
            datos2: [],
            monto: "",
            account:[],
            loan2:[],
            quotas: 0,
            totalPay: 0,
            type1: "",

        }
    },
    created() {
        axios.get("http://localhost:8080/api/clients/current")
            .then(response =>{
                this.datos= response.data
                this.datos2 = this.datos.accountDTOS.filter(valor => valor.active)
                this.loans2 = this.datos.clientLoanDTOS.filter(loan => loan.amount > 0);
               

                console.log(this.loans2);
            })
            .catch(error => console.log(error))


            axios.get('http://localhost:8080/api/loans')
            .then(response => {
                this.loans = response.data;
            })
            .catch(error => console.log(error));
        
    },
    methods: {
        filterLoan(id) {
            this.loanID = this.loans2.filter(loan =>  loan.id == id)[0]
            console.log(this.loanID)
            this.quotas = this.loanID.amount / this.loanID.payments;
            console.log(this.quotas)
            this.totalPay = this.loanID.amount;
            console.log(this.totalPay)
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
        add() {
            Swal.fire({
                title: 'Are you sure you want to add another account?',
                showCancelButton: true,
                confirmButtonText: 'Yes',
                showLoaderOnConfirm: true,
                preConfirm: (login) => {
                    return axios.post("/api/clients/current/accounts","type="+this.type1)
                        .then(response => {
                            window.location.href = "/acconts.html"
                        })
                        .catch(error => {
                            Swal.showValidationMessage(
                                'Request failed: ${error}'
                            )
                            console.log(error)
                        }) 
                       
                },
                allowOutsideClick: () => !Swal.isLoading()
            })
        },
        EliminarAccount(id){
            Swal.fire({
                title: 'Are you sure that you want to delete this card?',
                inputAttributes: {
                    autocapitalize: 'off'
                },
                showCancelButton: true,
                confirmButtonText: 'Sure',
                confirmButtonColor: "#7c601893",
                preConfirm: () => {
                    return axios.put('/api/clients/current/accounts' + `?id=${id}`)
                        .then(response => {
                            window.location.href="/acconts.html"
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
        PAGAR() {
            Swal.fire({
                title: 'Are you sure you want to pay??',
                inputAttributes: {
                    autocapitalize: 'off',
                },
                showCancelButton: true,
                confirmButtonText: 'Sure',
                confirmButtonColor: '#7c601893',
                preConfirm: () => {
                    return axios
                        .post('/api/current/loans', `id=${this.loanID.id}&account=${this.account}&amountEntered=${this.amount}`)
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
        
    },
    
   
	
})
app.mount("#app")