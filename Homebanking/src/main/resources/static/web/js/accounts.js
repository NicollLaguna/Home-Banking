const { createApp } = Vue

const app = createApp({
    data() {
        return {
            datos: [],
            params: '',
            idClient: [],
            id: '',
            number: '',
            creationDate: '',
            balance: '',
            firstName: '',
        }
    },
    created() {
        this.loadData();
    },
    methods: {
        async loadData() {
            try {
                axios.get('http://localhost:8080/api/clients'+ this.id)
                    .then(response => {
                        this.datos = response.data;
                        this.params = new URLSearchParams(location.search);
                        this.id = this.params.get('id');
                        this.idClient = this.datos.find(client => client.id == this.id);
                    })
            } catch { err => console.log(err) };
        },
        formatMoney(balance){
            return balance.toLocaleString('es-US', {style: 'currency', currency: 'USD'});
          }
        
    }
    
    
}).mount('#main');