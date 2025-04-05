document.getElementById('login-form').addEventListener('submit', async function (event) {
  event.preventDefault(); // Prevent form from submitting

  // Get input values
  const emailOrUsername = document.getElementById('email-or-username').value;
  localStorage.setItem("userName1", emailOrUsername); // Save the username in local storage
  const username3 =localStorage.getItem("userName1");
  console.log("username3"+username3);

  const password = document.getElementById('password').value;

  // Clear all previous error messages
  clearErrorMessages();

  // Validate inputs
  let isValid = true;

  if (emailOrUsername === "") {
    showError("email-or-username-error", "Please enter a valid email or username.");
    isValid = false;
  }

  if (password === "") {
    showError("password-error", "Please enter your password.");
    isValid = false;
  }

  // If inputs are valid, proceed with backend call
  if (isValid) {
    try {
      // Send login details to the backend
          const response = await fetch("http://localhost:5000/api/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ emailOrUsername, password }),
      });

      const result = await response.json();

      if (response.ok) {
        // Successful login: redirect to homepage
        window.location.href = "homepage.html";
      } else {
        // Display error message from backend
        showError("email-or-username-error", result.message || "Invalid credentials. Please try again.");
      }
    } catch (error) {
      console.error("Error connecting to the server:", error);
      showError("email-or-username-error", "An error occurred. Please try again.");
    }
  }
});

function showError(fieldId, message) {
  const errorElement = document.getElementById(fieldId);
  errorElement.textContent = message;
  errorElement.style.display = 'block';
}

function clearErrorMessages() {
  const errors = document.querySelectorAll('.error-message');
  errors.forEach((error) => {
    error.style.display = 'none';
    error.textContent = '';
  });
}

function resetForm() {
  // Reset all input fields
  document.getElementById('login-form').reset();

  // Hide success message after a few seconds
  setTimeout(() => {
    document.getElementById('login-success').style.display = 'none';
  }, 3000);
}
