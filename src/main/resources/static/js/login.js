const loginForm = document.getElementById('login-form');
let jwtToken = '';

loginForm.addEventListener('submit', function(event) {
    event.preventDefault();
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    const data = { username: username, password: password };

    // отправляем данные формы на сервер
    fetch('http://localhost:8080/auth/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => response.json())
        .then(data => {
            if (data.jwtToken) {
                jwtToken = data.jwtToken; // сохраняем jwtToken в переменную

                // отправляем GET запрос на сервер с параметром jwtToken
                fetch(`http://localhost:8080/test?jwtToken=${jwtToken}`, {
                    method: 'GET'
                })
                    .then(response => response.text())
                    .then(data => {
                        console.log(data);
                    })
                    .catch(error => {
                        console.error('Error:', error);
                    });

            } else {
                alert('Authentication failed.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
});