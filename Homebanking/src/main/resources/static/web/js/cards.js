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
            id: new URLSearchParams(location.search).get('id') ,
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
                axios.get('http://localhost:8080/api/clients/current'+ this.id)
                    .then(response => {
                        this.datos = response.data;
                        this.cards = this.datos.cards;
                        this.debit = this.cards.filter(card => card.type == "DEBIT");
                        console.log(this.debit)
                        this.credit = this.cards.filter(card => card.type == "CREDIT");
                        
                    })
                    
            } catch { err => console.log(err) };
        },
       
     
    }
}).mount('#app');