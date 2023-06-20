function sendData() {
    var value = document.getElementById("value").value;
    var raining = document.getElementById("raining").checked;
    var sensorName = document.getElementById("sensorName").value;

    var jsonData = {
        "value": value,
        "raining": raining,
        "sensor": {
            "name": sensorName
        }
    };

    var xhr = new XMLHttpRequest();
    xhr.open("POST", "http://localhost:8080/measurements/add", true);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.setRequestHeader('Authorization', 'Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJVc2VyIGRldGFpbHMiLCJpc3MiOiJtaXRyeWFzb3YiLCJleHAiOjE2ODcyOTM4MjEsImlhdCI6MTY4NzI5MDIyMSwidXNlcm5hbWUiOiJhZG1pbiJ9.urfGeYLTCZSIj_qGiW7j73X0rZCl19uAIle9mk_qWls');
    xhr.send(JSON.stringify(jsonData));

    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4 && xhr.status == 200) {
            alert(xhr.responseText);
        }
    };
}