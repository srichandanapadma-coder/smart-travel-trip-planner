const registerForm = document.getElementById("registerForm");
const message = document.getElementById("message");

registerForm.addEventListener("submit", async function (event) {

    event.preventDefault();

    const name = document.getElementById("name").value;
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    try {

        const API_BASE = window.SMARTTRAVEL_API_BASE || "http://localhost:8081";

        const response = await fetch(
            `${API_BASE}/api/users/register`,
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify({
                    name: name,
                    email: email,
                    password: password
                })
            }
        );

        if (response.ok) {

            message.textContent =
                "Registration successful!";

            message.style.color = "green";

            registerForm.reset();

        } else {

            const errorText = await response.text();

            message.textContent =
                errorText || "Registration failed.";

            message.style.color = "red";
        }

    } catch (error) {

        console.error(error);

        message.textContent =
            "Unable to connect to server.";

        message.style.color = "red";
    }

});