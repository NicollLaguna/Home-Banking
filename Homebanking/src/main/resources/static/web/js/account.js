const { createApp } = Vue

const app = createApp({
    data() {
        return {
            datos: [],
            account: [],
            id: new URLSearchParams(location.search).get('id') ,
            amount: '',
            description: '',
            date: '',
            type: '',
            number: '',
            account2: [],
        }
    },
    created() {
        this.loadData();
    },
    methods: {
       loadData() {
            try {
                axios.get('http://localhost:8080/api/accounts/' + this.id)
                    .then(response => {
                        this.datos = response.data;
                        this.account = this.datos;
                        this.account2= this.account.transactions.sort((x,y)=> y.id-x.id);
                    })
                    
            } catch { err => console.log(err) };
        },
        formatCurrency(amount){
            let options = { style: 'currency', currency: 'USD' };
            let numberFormat = new Intl.NumberFormat('en-US', options);
            return numberFormat.format(amount);
    },
     

    }
}).mount('#app');