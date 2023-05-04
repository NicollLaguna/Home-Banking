const {createApp} = Vue 

const app = createApp({
    data(){
        return{
            transfer:[],
            type:'',
            valueType:'',
            amount:'',
            account1:'',
            account2:'',
            description:'',
            accounts:[],
            idClient:[],
        }
    },
    created(){
        this.election()
    },
    methods: {
        election(){
            axios.get('http://localhost:8080/api/clients/current')
            .then(response => {
                this.idClient = response.data;
                this.accounts = this.idClient.accounts;
                console.log(this.accounts)
            })
        },
        newTransaction(){
            const swalWithBootstrapButtons = Swal.mixin({
                customClass: {
                  confirmButton: 'btn btn-success',
                  cancelButton: 'btn btn-danger'
                },
                buttonsStyling: false
              })
              
              swalWithBootstrapButtons.fire({
                title: 'Do you want to make the transaction?',
                text: "You can't reverse this action.!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonText: 'Yes, I want!',
                cancelButtonText: 'No, cancel!',
                reverseButtons: true
              }).then((result) => {
                if (result.isConfirmed) {
                    axios.post('/api/transactions','amount=' + this.amount + '&description=' + this.description + '&account1=' + this.account1 + '&account2=' + this.account2 )
                  .then((result) => window.location.href = "/web/accounts.html")
                  .catch(error => {
                    Swal.fire({
                        icon: 'error',
                        text: error.response.data}
                    )
                    
                })
                swalWithBootstrapButtons.fire(
                    'Succesful transaction',
                    'Your money was sent.',
                    'success'
                  )
                  
                } else if (
                  result.dismiss === Swal.DismissReason.cancel
                ) {
                  swalWithBootstrapButtons.fire(
                    'Cancelled',
                    "Your transaction didn't go through",
                    'error'
                  )
                }
              }).catch(error => {
                Swal.fire({
                    icon: 'error',
                    text: error.response.data}
                )
            })
               
            /* axios.post('/api/transactions','amount=' + this.amount + '&description=' + this.description + '&account1=' + this.account1 + '&account2=' + this.account2 )
            .then(response => Swal.fire({
                title: 'Are you sure?',
                text: "You won't be able to revert this!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes, delete it!'
              }).then((result) => {
                if (result.isConfirmed) {
                  Swal.fire(
                    'Deleted!',
                    'Your file has been deleted.',
                    'success'
                  )
                }else if (
                    
                    result.dismiss === Swal.DismissReason.cancel
                  ) {
                    swalWithBootstrapButtons.fire(
                      'Cancelled',
                      'Your imaginary file is safe :)',
                      'error'
                    )
                  }
              }))
            */
        },
        typeSelect() {
            if (this.valueType == 1) {
                this.type = "Own account"
            }
            else if (this.valueType == 2) {
                this.type = "Someone else's account"
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