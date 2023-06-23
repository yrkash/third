const loginForm = document.getElementById('login-form');

loginForm.addEventListener('submit', function(event) {
    event.preventDefault();
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    const data = { username: username, password: password };

    // отправляем данные формы на сервер
    fetch('/auth/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => response.json())
        .then(data => {
            if (data.jwtToken) {
                const jwtToken = data.jwtToken; // сохраняем jwtToken в переменную

                // редирект на страницу test с параметром jwtToken
                window.location.href = `/ui?jwtToken=${jwtToken}`;
            } else {
                alert('Authentication failed.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
});