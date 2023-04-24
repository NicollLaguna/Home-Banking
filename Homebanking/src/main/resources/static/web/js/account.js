const { createApp } = Vue

const app = createApp({
    data() {
        return {
            datos: [],
            account: [],
            idClient:'', 
            id: new URLSearchParams(location.search).get('id') ,
            amount: '',
            description: '',
            date: '',
            type: '',
            number: '',
            account2: [],
            Client: [],
        }
    },
    created() {
        this.loadData();
        this.Data2();
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
        Data2(){
            try{
                axios.get('http://localhost:8080/api/clients/current')
                    .then(response => {
                        this.datos = response.data;
                        this.Client = this.datos;
                        console.log(this.Client);
                    })
            } catch { err => console.log(err) };
            },
        
        formatCurrency(amount){
            let options = { style: 'currency', currency: 'USD' };
            let numberFormat = new Intl.NumberFormat('en-US', options);
            return numberFormat.format(amount);
    }, 
    exit() {
        axios.post('/api/logout')
        .then(response => window.location.href="/web/index.html")
        .catch(error => console.log(error));
    }
        },

        
     
    }
).mount('#app');