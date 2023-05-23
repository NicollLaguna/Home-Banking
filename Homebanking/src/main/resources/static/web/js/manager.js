const {createApp} = Vue

const app = createApp({
    data(){
        return{
            dataClients: {},
            firstName: '',
            lastName: '',
            email: '',
            name:'',
            amount:'',
            checked:[],
            interest:'',
        };
    },
    created(){
    this.loadData();
    },
    methods: {
        async loadData(){
            try{
                axios
                .get('http://localhost:8585/api/clients')
                .then(response => {
                this.dataClients = response.data._embedded.clients;
            })}catch{err => console.log(err)};
        },
            async postClient(){
                try{
                axios
                .post('http://localhost:8585/api/clients',{
                    firstName: this.firstName,
                    lastName: this.lastName,
                    email: this.email,
                })
                .then(response => {
                    this.loadData();
                })}catch{err => console.log(err)};
            },
            async addClient(){
                this.postClient();
            },
            exit() {
                axios.post('/api/logout')
                .then(response => window.location.href="/web/index.html")
                .catch(error => console.log(error));
            },
            createLoan(){
                Swal.fire({
                    title: 'Are you sure that you want to create this loan?',
                    inputAttributes: {
                        autocapitalize: 'off'
                    },
                    showCancelButton: true,
                    confirmButtonText: 'Sure',
                    confirmButtonColor: "#7c601893",
                    preConfirm: () => {
                        return axios.post('/api/loans/admin-loan' , {
                            "name" : this.name,
                            "maxAmount" : this.amount,
                            "payments" : this.checked,
                            "descriptionLoan" : this.description,
                            "interest" : this.interest
                            })
                            .then(response => {
                                Swal.fire({
                                    icon: 'success',
                                    text: 'You create a new Loan',
                                    showConfirmButton: false,
                                    timer: 2000,
                                })
                            .then( () => window.location.href= "/web/cards.html")
                            })
                            .catch(error => {
                                Swal.fire({
                                    icon: 'error',
                                    text: error.response.data,
                                    confirmButtonColor: "#7c601893",
                                })
                            })
                    },
                    allowOutsideClick: () => !Swal.isLoading()
                })
    
            }
            

},
}).mount('#app');