const { createApp } = Vue;

createApp({
	data() {
		return {
			datos : [],
			account: [],
			transactions: [],
			datos2: [],
			type: "",
			description: "",

			id: new URLSearchParams(location.search).get('id'),
		};
	},
	created() {
		this.loadData();
	},
	methods: {
		loadData() {
			console.log(this.id)
			axios
				.get('http://localhost:8080/api/clients/current/accounts/' + this.id)
				.then(response => {
					this.account = response.data;
					this.transactions = this.account.transactions.filter(valor => valor.active);
					console.log(this.account);

				})
				
		},
		loadData2(){
			axios.get("http://localhost:8080/api/clients/current")
            .then(response =>{
                this.datos= response.data
				
                console.log(this.datos);
            })
            .catch(error => console.log(error))
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
	
        
    
}).mount('#app');