const loginForm = document.getElementById("loginForm");
const loginMessage = document.getElementById("loginMessage");


loginForm.addEventListener("submit", async function (event) {

    event.preventDefault();


    // Get values from form
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;


    try {

        const API_BASE = window.SMARTTRAVEL_API_BASE || "http://localhost:8081";

        // Send login request to Spring Boot
        const response = await fetch(
            `${API_BASE}/api/users/login`,
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify({
                    email: email,
                    password: password
                })
            }
        );


        // Login successful
        if (response.ok) {

            const data = await response.json();


            // Save JWT token
            localStorage.setItem(
                "token",
                data.token
            );


            // Save user information
            localStorage.setItem(
                "userName",
                data.name
            );

            localStorage.setItem(
                "userEmail",
                data.email
            );


            // Show success message
            loginMessage.textContent =
                "Login successful!";

            loginMessage.style.color = "green";


            // Open dashboard
            setTimeout(function () {

                window.location.href =
                    "dashboard.html";

            }, 1000);

        }


        // Login failed
        else {

            loginMessage.textContent =
                "Invalid email or password.";

            loginMessage.style.color = "red";
        }


    } catch (error) {

        console.error(error);

        loginMessage.textContent =
            "Unable to connect to server.";

        loginMessage.style.color = "red";
    }

});