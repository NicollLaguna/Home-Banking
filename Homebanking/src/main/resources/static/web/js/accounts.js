const { createApp } = Vue

const app = createApp({
    data() {
        return {
            datos: [],
            idClient: [],
            id: new URLSearchParams(location.search).get('id') ,
            number: '',
            creationDate: '',
            balance: '',
            firstName: '',
            idClient2:[],
        }
    },
    created() {
        this.loadData();
    },
    methods: {
         loadData() {
            try {
                axios.get('http://localhost:8080/api/clients/'+ this.id)
                    .then(response => {
                        this.datos = response.data;
                        this.idClient = this.datos;
                        this.idClient2 = this.idClient.accounts.sort((x,y)=> x.id-y.id);
                    })
            } catch { err => console.log(err) };
        },
        formatMoney(balance){
            let options = { style: 'currency', currency: 'USD' };
            let numberFormat = new Intl.NumberFormat('en-US', options);
            return numberFormat.format(balance);
    },
}   
}).mount('#app');