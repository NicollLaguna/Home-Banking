const { createApp } = Vue;

const app = createApp({
    data() {
        return {
            debit: [],
            credit: [],
            cards: [],
            type: '',
            color: '',
            idClient: [],
            value: '',
            valueT: ''
        }
    },
    created() {
        this.loadData()
    },
    methods: {
        loadData() {
            axios.get('http://localhost:8080/api/clients/current')
                .then(response => {
                    this.idClient = response.data;
                    this.cards = this.idClient.cards;
                    this.debit = this.cards.filter(card => card.type == "DEBIT");
                    this.credit = this.cards.filter(card => card.type == "CREDIT");
                })
                .catch(error => console.log(error));
        },
       
        createCard() {
            axios.post('/api/clients/current/cards', `type=${this.type}&color=${this.color}`)
                .then(response => window.location.href = "/web/cards.html")
                .catch(error => {
                    Swal.fire({
                        icon: 'error',
                        title: 'EXISTING CARD',
                        text: error.response.data}
                    )
                })
        },
        typeSelect() {
            if (this.valueT == 2) {
                this.type = "CREDIT"
            }
            else if (this.valueT == 1) {
                this.type = "DEBIT"
            }

        },
        colorSelect() {
            if (this.value == 3) {
                this.color = "TITANIUM"
            }
            else if (this.value == 1) {
                this.color = "GOLD"
            }
            else if (this.value == 2) {
                this.color = "SILVER"
            }
        },
         exit() {
            axios.post('/api/logout')
                .then(response => window.location.href = "/web/index.html")
                .catch(error => console.log(error));
        },
    }
})
app.mount('#app');