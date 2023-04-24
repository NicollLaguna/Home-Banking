const {createApp} = Vue;

const app = createApp({
  data() {
    return {
      email: '',
      password: '',
      name: '',
      lastName: '',
      email2: '',
      password2: ''
    };
  },
  
  methods: {
    Login() {
      axios
      .post('/api/login', `email=${this.email}&password=${this.password}`)
        .then(response => window.location.href = '/web/accounts.html')
        .catch(error => console.log(error));         
    },
    Register() {
      axios
      .post('/api/clients', `firstName=${this.name}&lastName=${this.lastName}&email=${this.email2}&password=${this.password2}`)
        .then(response => axios.post('/api/login', `email=${this.email2}&password=${this.password2}`)
          .then(response => window.location.href = '/web/accounts.html')
          .catch(error => console.log(error)))
        .catch(error => console.log(error));
    }
  }
});

app.mount('#app');

const wrapper = document.querySelector('.wrapper');
const loginLink = document.querySelector('.login-link');
const registerLink = document.querySelector('.register-link');
const btnPopup = document.querySelector('.btnLogin-popup');
const iconClose = document.querySelector('.icon-close');
registerLink.addEventListener('click', ()=> {
    wrapper.classList.add('active');
});

loginLink.addEventListener('click', ()=> {
    wrapper.classList.remove('active');
});

btnPopup.addEventListener('click', ()=> {
    wrapper.classList.add('active-popup');
});


iconClose.addEventListener('click', ()=> {
    wrapper.classList.remove('active-popup');
});