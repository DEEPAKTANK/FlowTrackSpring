document.getElementById('set-password-form').addEventListener('submit', async function(event) {
  event.preventDefault(); // Prevent form submission

  // Get input values from the form
  const email = document.getElementById('email').value.trim();
  const username = document.getElementById('username').value.trim();
  const password = document.getElementById('password').value.trim();
  const confirmPassword = document.getElementById('confirm-password').value.trim();

  // Clear all previous error messages
  clearErrorMessages();

  // Input validation
  let isValid = true;

  if (!email) {
    showError('email-error', 'Please enter a valid email.');
    isValid = false;
  }

  if (!username) {
    showError('username-error', 'Please enter a valid username.');
    isValid = false;
  }

  if (!password) {
    showError('password-error', 'Please enter a password.');
    isValid = false;
  }

  if (confirmPassword !== password) {
    showError('confirm-password-error', 'Passwords do not match.');
    isValid = false;
  }

  // If validation fails, stop execution
  if (!isValid) return;
  localStorage.setItem('uname', username);

  // Proceed to API call if inputs are valid
  try {
    const response = await fetch('http://localhost:5000/api/setpassword', {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        email,
        username,
        password,
      }),
    });

    // Handle API response
    if (response.ok) {
      const data = await response.json();
      console.log('User registered successfully:', data);

      // Show success message
      document.getElementById('set-password-success').style.display = 'block';

      // Optionally redirect the user after a delay
      setTimeout(() => {
        window.location.href = 'registration.html'; // Redirect to login page
      }, 2000);
    } else {
      // Handle server errors
      const errorText = await response.text();
      console.error('Error while registering user:', response.status, errorText);
      showError('form-error', 'Registration failed. Please try again.');
    }
  } catch (error) {
    // Handle network or unexpected errors
    console.error('Error:', error);
    showError('form-error', 'An error occurred. Please try again later.');
  }
  // Reset the form after submission
  resetForm();
});

// Function to display error messages
function showError(fieldId, message) {
  const errorElement = document.getElementById(fieldId);
  if (errorElement) {
    errorElement.textContent = message;
    errorElement.style.display = 'block';
  }
}

// Function to clear all error messages
function clearErrorMessages() {
  const errors = document.querySelectorAll('.error-message');
  errors.forEach(error => {
    error.style.display = 'none';
    error.textContent = '';
  });
}

// Function to reset the form
function resetForm() {
  document.getElementById('set-password-form').reset();

  // Hide success message after a few seconds
  setTimeout(() => {
    document.getElementById('set-password-success').style.display = 'none';
  }, 3000);
}
