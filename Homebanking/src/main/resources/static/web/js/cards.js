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
            actualDate:'',//vencimiento de la tarjeta
        
        }
    },
    created() {
        this.loadData();
    },
    methods: {
       loadData() {
         
                axios.get('http://localhost:8585/api/clients/current')
                    .then(response => {
                        this.datos = response.data;
                        this.cards = this.datos.cards;
                        this.debit = this.cards.filter(card => card.type == "DEBIT" && card.active);
                        console.log(this.debit)
                        this.credit = this.cards.filter(card => card.type == "CREDIT" && card.active);
                        console.log(this.credit)
                        this.actualDate= new Date().toLocaleDateString().split(",")[0].split("/").reverse().join("-");
                        
                    })
                    .catch (err => console.log(err)) ;
                    
            }, 
            exit() {
                axios.post('/api/logout')
                .then(response => window.location.href="/web/index.html")
                .catch(error => console.log(error));
            },
            //Eliminar tarjeta
            cardDelete(id){
                const swalWithBootstrapButtons = Swal.mixin({
                    customClass: {
                      confirmButton: 'btn btn-success',
                      cancelButton: 'btn btn-danger'
                    },
                    buttonsStyling: false
                  })
                  
                  swalWithBootstrapButtons.fire({
                    title: 'Do you want to delete this card?',
                    text: "You can't reverse this action.!",
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonText: 'Yes, I want!',
                    cancelButtonText: 'No, cancel!',
                    reverseButtons: true
                  }).then((result) => {
                    if (result.isConfirmed) {
                        axios.put(`http://localhost:8585/api/clients/current/cards/${id}`)
                      .then((result) => window.location.href = "/web/cards.html")
                      .catch(error => {
                        Swal.fire({
                            icon: 'error',
                            text: error.response.data}
                        )
                        
                    })
                    swalWithBootstrapButtons.fire(
                        'Deleted Successful',
                        'Your card was deleted.',
                        'success'
                      )
                      
                    } else if (
                      result.dismiss === Swal.DismissReason.cancel
                    ) {
                      swalWithBootstrapButtons.fire(
                        'Cancelled',
                        "Your card didn't delete",
                        'error'
                      )
                    }
                  }).catch(error => {
                    Swal.fire({
                        icon: 'error',
                        text: error.response.data}
                    )
                })
            }
        },

     
    }
).mount('#app');