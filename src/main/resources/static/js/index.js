const { createApp } = Vue

const app = createApp({

    data() {

        return {
            email: "",
            password: "",

        }
    },
    created() {


    },
    methods: {
        login() {
            axios.post("/api/login", {
                email: this.email,
                password: this.password,
            }, {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            })
                .then(() => {
                    window.location.href = "/acconts.html"
                })
                .catch(() => {
                    Swal.fire({
                        title: "There is a problem!",
                        text: "Email or password incorrect",
                        icon: "error",
                        confirmButtonColor: "#009269",
                    })
                })
        }
    },
})
app.mount("#app")