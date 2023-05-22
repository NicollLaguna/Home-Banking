const { createApp } = Vue

const app = createApp({
    data() {
        return {
            datos: [],
            id:'',
            idClient: [],
            idClient2:[],
            loans:  [],
            transactions:[],
            finalBalance:0,
            accountType:'',
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
                        this.idClient = this.datos;
                        this.idClient2 = this.idClient.accounts.sort((x,y)=> x.id-y.id).filter(account => account.active);
                        this.loans = this.idClient.loans;
                        console.log(this.idClient2)
                        console.log(this.idClient)
                    })
                    .catch(err=>console.error(err));
            }, 
            format(balance){
                let options = { style: 'currency', currency: 'USD' };
                let numberFormat = new Intl.NumberFormat('en-US', options);
                return numberFormat.format(balance);
        },
        exit() {
            axios.post('/api/logout')
            .then(response => window.location.href="/web/index.html")
            .catch(error => console.log(error));
        },
        newAccount(){
          const swalWithBootstrapButtons = Swal.mixin({
              customClass: {
                confirmButton: 'btn btn-success',
                cancelButton: 'btn btn-danger'
              },
              buttonsStyling: false
            })
            
            swalWithBootstrapButtons.fire({
              title: 'Do you want to create an account?',
              text: "You can't reverse this action.!",
              icon: 'warning',
              showCancelButton: true,
              confirmButtonText: 'Yes, I want!',
              cancelButtonText: 'No, cancel!',
              reverseButtons: true
            }).then((result) => {
              if (result.isConfirmed) {
                axios.post('http://localhost:8585/api/clients/current/accounts',`accountType=${this.accountType}`)
                .then((result) => window.location.href = "/web/accounts.html")
                .catch(error => {
                  Swal.fire({
                      icon: 'error',
                      text: error.response.data}
                  )
                  
              })
              swalWithBootstrapButtons.fire(
                  'Successful creation',
                  'Your account was created.',
                  'success'
                )
                
              } else if (
                result.dismiss === Swal.DismissReason.cancel
              ) {
                swalWithBootstrapButtons.fire(
                  'Cancelled',
                  "The account will not be created",
                  'error'
                )
              }
            }).catch(error => {
              Swal.fire({
                  icon: 'error',
                  text: error.response.data}
              )
          })
      },
       
        //Eliminar cuenta
        accountDelete(id){
            const swalWithBootstrapButtons =Swal.mixin({
                customClass: {
                  confirmButton: 'btn btn-success',
                  cancelButton: 'btn btn-danger'
                },
                buttonsStyling: false
              })
              
              swalWithBootstrapButtons.fire({
                title: 'Do you want to delete this account?',
                text: "You can't reverse this action.!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonText: 'Yes, I want!',
                cancelButtonText: 'No, cancel!',
                reverseButtons: true
              }).then((result) => {
                if (result.isConfirmed) {
                    axios.put(`http://localhost:8585/api/clients/current/accounts/${id}`)
                  .then((result) => window.location.href = "/web/accounts.html")
                  .catch(error => {
                    Swal.fire({
                        icon: 'error',
                        text: error.response.data}
                    )
                    
                })
                swalWithBootstrapButtons.fire(
                    'Deleted Successful',
                    'Your account was deleted.',
                    'success'
                  )
                  
                } else if (
                  result.dismiss === Swal.DismissReason.cancel
                ) {
                  swalWithBootstrapButtons.fire(
                    'Cancelled',
                    "Your account didn't delete",
                    'error'
                  )
                }
              }).catch(error => {
                Swal.fire({
                    icon: 'error',
                    text: error.response.data}
                )
            })
        },
        //eliminar la cuenta
      
        sumbalance(){
          for (let transaction of this.idClient2.transactions){
              if(transaction.type === "CREDIT"){
                  this.finalBalance = this.finalBalance + transaction.amount
              }else{
                  this.finalBalance=this.finalBalance-transaction.amount
              }
          }
          this.finalBalance = this.finalBalance.toLocaleString('en-US', { style: 'currency', currency: 'USD' })
      }//balance exacto
        },
       
});
app.mount('#app');