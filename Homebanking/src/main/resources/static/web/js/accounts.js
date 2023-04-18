const { createApp } = Vue

const app = createApp({
    data() {
        return {
            datos: [],
            id:'',
            idClient: [],
            idClient2:[],
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
                    })
                    .catch(err=>console.error(err));
            } 
        },
        formatMoney(balance){
            let options = { style: 'currency', currency: 'USD' };
            let numberFormat = new Intl.NumberFormat('en-US', options);
            return numberFormat.format(balance);
    },
});
app.mount('#app');