const loginForm = document.getElementById('login-form');

loginForm.addEventListener('submit', function(event) {
    event.preventDefault();
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    const data = { username: username, password: password };

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
                alert(`JWT token: ${data.jwtToken}`);
            } else {
                alert('Authentication failed.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
});