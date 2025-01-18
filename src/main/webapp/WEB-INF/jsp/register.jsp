<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Registrate</title>
        <script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
    </head>
    <body>
        <p>1231</p>
        <h1>Registration page</h1>
        <form method="post" id="registerForm">
            <label for="name">Name:</label>
            <input type="text" id="name" name="name" required><br><br>

            <label for="email">Email:</label>
            <input type="email" id="email" name="email" required><br><br>

            <label for="password">Password:</label>
            <input type="password" id="password" name="password" required><br><br>

            <button id="registerButton">Register</button>
        </form>
        <script>
            document.getElementById("registerButton").addEventListener("click", function (event) { event.preventDefault();
            // Отправка запроса к api
            axios.post("${pageContext.request.contextPath}/api/auth/register", {
                name: document.getElementById("name").value,
                email: document.getElementById("email").value,
                password: document.getElementById("password").value
                }, {
                headers: {
                    'Content-Type': 'application/json'
                }
            })
                // Успешная регистрация
                .then(response => {
                    alert('Registration successful!', response);
                    const token = response.headers['authorization'];
                    if(token){
                        localStorage.setItem('jwt', token.split(' ')[1]);
                    }
                })
                // Отображение ошибки
                .catch(error => {
                    if (error.response) {
                        alert('Error: ' + error.response.data);
                    } else {
                        alert('Unexpected error:', error);
                    }
                });
            });
            
        </script>
    </body>
</html>
