const { createApp } = Vue

const app = createApp({
    data() {
        return {
            datos: [],
            params: '',
            account: [],
            id: '',
            amount: '',
            description: '',
            date: '',
            type: '',
            number: '',
        }
    },
    created() {
        this.loadData();
    },
    methods: {
        async loadData() {
            try {
                axios.get('http://localhost:8080/api/accounts')
                    .then(response => {
                        this.datos = response.data;
                        this.params = new URLSearchParams(location.search);
                        this.id = this.params.get('id');
                        this.account = this.datos.find(account => account.id == this.id);
                    })
                    
            } catch { err => console.log(err) };
        },
        formatCurrency(amount){
            let options = { style: 'currency', currency: 'USD' };
            let numberFormat = new Intl.NumberFormat('en-US', options);
            return numberFormat.format(amount);
    },

    }
}).mount('#main');