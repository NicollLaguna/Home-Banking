const { createApp } = Vue

const app = createApp({
    data() {
        return {
            datos: [],
            id:'',
            idClient: [],
            idClient2:[],
            loans:  [],
        }
    },
    created() {
        this.loadData();
    },
    methods: {
         loadData() {
            
                axios.get('http://localhost:8080/api/clients/current')
                    .then(response => {
                        this.datos = response.data;
                        this.idClient = this.datos;
                        this.idClient2 = this.idClient.accounts.sort((x,y)=> x.id-y.id);
                        this.loans = this.idClient.loans;
                    })
                    .catch(err=>console.error(err));
            }, 
            format(balance){
                let options = { style: 'currency', currency: 'USD' };
                let numberFormat = new Intl.NumberFormat('en-US', options);
                return numberFormat.format(balance);
        },
        exit() {
            axios.post('/api/logout')
            .then(response => window.location.href="/web/index.html")
            .catch(error => console.log(error));
        },
        newAccount(){
            axios.post('http://localhost:8080/api/clients/current/accounts')
            .then(response=>window.location.href="/web/accounts.html")
            .catch(error => console.log(error));
        }
        },
       
});
app.mount('#app');