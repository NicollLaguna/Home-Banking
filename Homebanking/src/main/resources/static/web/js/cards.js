const {createApp} = Vue
const app = createApp({
    data() {
        return {
            datos: [],
            cards: [],
            type:'',
            fromDate: '',
            thruDate:'',
            cardholder: '',
            cvv:'',
            id: '',
            debit:[],
            credit:[],
        }
    },
    created() {
        this.loadData();
    },
    methods: {
       loadData() {
            try {
                axios.get('http://localhost:8080/api/clients/current')
                    .then(response => {
                        this.datos = response.data;
                        this.cards = this.datos.cards;
                        this.debit = this.cards.filter(card => card.type == "DEBIT");
                        console.log(this.debit)
                        this.credit = this.cards.filter(card => card.type == "CREDIT");
                        
                    })
                    
            } catch { err => console.log(err) };
        },
        exit() {
            axios.post('/api/logout')
            .then(response => window.location.href="/web/index.html")
            .catch(error => console.log(error));
        }
     
    }
}).mount('#app');