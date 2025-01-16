<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
    </head>
    <body>
        <h1>Login page</h1>
        <form id="loginForm">
            <label for="email">Email:</label>
            <input type="email" id="email" name="email" required><br><br>

            <label for="password">Password:</label>
            <input type="password" id="password" name="password" required><br><br>

            <button id="loginButton">Login</button>
        </form>

        <script>
            document.getElementById("loginButton").addEventListener("click", function (event) { event.preventDefault();
            // Отправка запроса к api
            axios.post("${pageContext.request.contextPath}/api/auth/login", {
                email: document.getElementById("email").value,
                password: document.getElementById("password").value
                }, {
                headers: {
                    'Content-Type': 'application/json'
                }
            })
                // Успешная регистрация
                .then(response => {
                    alert('Login successful!', response);
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
