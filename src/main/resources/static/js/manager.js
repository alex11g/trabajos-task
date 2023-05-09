const { createApp } = Vue

const app = createApp({

    data() {

        return {
            datos: [],
            nombre: "",
            apellido: "",
            correo: "",

        }
    },
    created() {
        this.loadData()
        
    },
    methods: {
        loadData() {
            axios.get("http://localhost:8080/api/clients" )
                .then(response => {
                    this.datos = response.data
                    console.log(response);
                   
                })
                .catch(error => console.log(error))
               
        },
        addClient(){
            this.postClient()
        },
        postClient(){
            axios.post("http://localhost:8080/api/clients",{
                nombre: this.nombre,
                correo: this.correo,
                apellido: this.apellido
            } )
            .then(() => {
                this.loadData()
            })
            .catch(error => console.log(error))
        },
        
    }
})
app.mount("#app")