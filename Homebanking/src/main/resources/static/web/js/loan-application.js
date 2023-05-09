const {createApp} = Vue 

const app = createApp({
    data(){
        return{
            idClient:[],
            accounts: [],
            loans:[],
			accountNumber: '',
			payments: '',
            paymentSelect:'',
			amount:'',
			loanType: '',
			loadId: '',
        }
    },
    created(){
        this.loanData()
        this.loanData2()
    },
    methods:{
        loanData(){
            axios.get('http://localhost:8080/api/loans')
            .then(response => {
                this.loans = response.data;
                console.log(this.loans)
            })
            .catch(error => console.log(error));
        },
        loanData2(){
            axios.get('http://localhost:8080/api/clients/current')
				.then(response => {
					this.idClient = response.data;
                    this.accounts= this.idClient.accounts
				})
				.catch(error => console.log(error));
        },
        exit() {
            axios.post('/api/logout')
                .then(response => window.location.href = "/web/index.html")
                .catch(error => console.log(error));
        },
        selectPayment(){
            this.paymentSelect = this.loans.filter(loan =>{
                return this.loanType.includes(loan.name); 
            })[0];
            console.log(this.paymentSelect)
           
        },
        newLoan(){
            const swalWithBootstrapButtons = Swal.mixin({
                customClass: {
                  confirmButton: 'btn btn-success',
                  cancelButton: 'btn btn-danger'
                },
                buttonsStyling: false
              })
              
              swalWithBootstrapButtons.fire({
                title: 'Do you want to apply the loan?',
                text: "You can't reverse this action.!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonText: 'Yes, I want!',
                cancelButtonText: 'No, cancel!',
                reverseButtons: true
              }).then((result) => {
                if (result.isConfirmed) {
                    axios.post('/api/loans',{id:this.paymentSelect.id,amount:this.amount, payments: this.payments, accountNumber:this.accountNumber} )
                  .then((result) => window.location.href = "/web/accounts.html")
                  .catch(error => {
                    Swal.fire({
                        icon: 'error',
                        text: error.response.data}
                    )
                    
                })
                swalWithBootstrapButtons.fire(
                    'Succesful Apply',
                    'Your apply was made.',
                    'success'
                  )
                  
                } else if (
                  result.dismiss === Swal.DismissReason.cancel
                ) {
                  swalWithBootstrapButtons.fire(
                    'Cancelled',
                    "Your application didn't go through",
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
    }
})
app.mount('#app');