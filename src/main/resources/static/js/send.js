async function sendData() {
    const data = {
        value: document.getElementById('value').value,
        raining: document.getElementById('raining').checked,
        sensor: {
            "name": document.getElementById('sensorName').value
            }

        // jwtToken: jwtToken // добавляем jwtToken в данные
    };

    const response = await fetch('/measurements/add', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + document.getElementById("jwtToken").value
        },
        body: JSON.stringify(data)
    });

    const result = await response.json();
    console.log(result);
}
