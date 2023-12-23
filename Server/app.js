
let userName = document.querySelectorAll('.username');

for (let i = 0; i < userName.length; i++) {
  fetch('127.0.0.1:8989/users')
  .then(response => response.text())
  .then(data =>  userName[i].innerText = data.users[i])
 

}