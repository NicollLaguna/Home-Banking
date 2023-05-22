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
            dateStart: '',
            dateEnd:'',
            accNumber: '',
            
        }
    },
    created() {
        this.loadData();
        this.Data2();
    },
    methods: {
       loadData() {
            try {
                axios.get('http://localhost:8585/api/clients/current/accounts/' + this.id)
                    .then(response => {
                        this.account = response.data;
                        this.account2= this.account.transactions.sort((x,y)=> x.id-y.id);
                        console.log(this.account)

                        /* this.sumbalance() */
                    })
                    
            } catch { err => console.log(err) };
        },
        Data2(){
            try{
                axios.get('http://localhost:8585/api/clients/current' )
                    .then(response => {
                        this.datos = response.data;
                        this.Client = this.datos;
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
    },
    
    //PDF
    download(accNumber,dateStart,dateEnd){
        if(this.dateStart !== "" && this.dateEnd !== ""){
            axios.post('/api/client/current/account_status',`accNumber=${this.account.number}&dateStart=${this.dateStart} 00:00&&dateEnd=${this.dateEnd} 23:59`,{
                responseType: 'blob'
            })
            .then((response) => {
                const url = window.URL.createObjectURL(new Blob([response.data], {
                    type: "application/pdf"
                }));
                const link = document.createElement('a');
                link.href = url;
                link.setAttribute('download',`Transaction${this.account.number}.pdf`);
                document.body.appendChild(link);
                link.click();
            })
            .catch(error => {
                Swal.fire({
                    icon: 'error',
                    text: error.response.data}
                )
            });
        }else{
            Swal.fire(
                'Cancelled',
                "Select two dates to filter!",
                'error'
            );
        }},//PDF

        
         
    }, 
}
).mount('#app');