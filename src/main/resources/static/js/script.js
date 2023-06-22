let jwtToken_; // объявляем переменную глобально

async function login() {
    const response = await fetch('http://localhost:8080/auth/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            username: document.getElementById('username').value,
            password: document.getElementById('password').value
        })
    });

    const data = await response.json();
    jwtToken_ = data.jwtToken; // сохраняем значение jwtToken в переменную
    console.log(jwtToken_);
}
async function sendData() {
    const data = {
        value: document.getElementById('value').value,
        raining: document.getElementById('raining').checked,
        sensor: {
            "name": document.getElementById('sensorName').value
            }

        // jwtToken: jwtToken // добавляем jwtToken в данные
    };

    const response = await fetch('http://localhost:8080/measurements/add', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + jwtToken_
        },
        body: JSON.stringify(data)
    });

    const result = await response.json();
    console.log(result);
}
