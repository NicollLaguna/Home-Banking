const { createApp } = Vue

const app = createApp({
    data() {
        return {
            datos: [],
            id:'',
            idClient: [],
            idClient2:[],
            loans:  [],
            
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
            axios.post('http://localhost:8585/api/clients/current/accounts')
            .then(response=>window.location.href="/web/accounts.html")
            .catch(error => console.log(error));
        },
        //Eliminar cuenta
        accountDelete(id){
            const swalWithBootstrapButtons = Swal.mixin({
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
        }//eliminar la cuenta
        },
       
});
app.mount('#app');