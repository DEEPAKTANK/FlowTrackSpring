// Initialize balances
let totalBalance = 0;
let cashBalance = 0;
let bank1Balance = 0;
let bank2Balance = 0;
let totalCredit = 0;
let totalDebit = 0;
let transactions = [];

let emailOrUsername = localStorage.getItem("userName1");
// API Base URL
const API_URL = "http://localhost:5000/api";



// Fetch initial data from the server
async function fetchInitialData() {
  try {
    const response = await fetch(`${API_URL}/data`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ emailOrUsername }),
    });

    const data = await response.json();

    updateBalances(data.balances);
    // updateTotalBal(data.balances)
    populateBankNames(data.bankNames);
    updateTotalBal(data.totalbal)

    console.log('data.bankNames:', data.bankNames);
    console.log('data.data.balances:', data.balances);
  } catch (error) {
    console.error("Error fetching initial data:", error);
  }
}

// Fetch total credit and debit for a specific date
async function fetchTotalsForDate(date) {
  try {
    const formattedDate = date || formatDate(new Date());
    const response = await fetch(`${API_URL}/total/${formattedDate}`);
    const totals = await response.json();
    console.log('totals', totals);

    totalCredit = totals.totalCredit || 0;
    totalDebit = totals.totalDebit || 0;

    document.getElementById("total-Credit").textContent = totalCredit;
    document.getElementById("total-Debit").textContent = totalDebit;
  } catch (error) {
    console.error("Error fetching totals:", error);
  }
}


// Add a new transaction
async function addTransaction() {
  // Get input values
  const description = document.getElementById("description").value;
  const amount = parseFloat(document.getElementById("amount").value);
  const type = document.querySelector('input[name="type"]:checked');
  const source = document.getElementById("source").value;
  const transactionDate = document.getElementById("transaction-date").value || formatDate(new Date()); // Use current date if none selected

  // Clear all previous error messages
  clearErrorMessages();

  // Validate inputs
  let isValid = true;

  if (description === "") {
    showError("description-error", "Please enter a valid description.");
    isValid = false;
  }

  if (isNaN(amount) || amount <= 0) {
    showError("amount-error", "Please enter a valid amount greater than 0.");
    isValid = false;
  }

  if (!type) {
    showError("type-error", "Please select either Credit or Debit.");
    isValid = false;
  }

  if (!isValid) {
    return; // Stop the function if there are validation errors
  }

  // Prepare the transaction object
  const transaction = {
    description,
    amount,
    type: type.value,
    source,
    transactionDate,
    emailOrUsername,
  };

  // Push the transaction object into the transactions array
  transactions.push(transaction);

  // Send the transaction to the server
  try {
    await fetch(`${API_URL}/transaction`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(transaction),
    });

    // Fetch updated data and totals
    fetchInitialData();
    fetchTotalsForDate(transactionDate);

    // Update the transaction table with the latest transactions
    updateTransactionTable(transactions);

  } catch (error) {
    console.error("Error adding transaction:", error);
  }

  // Reset form after submission
  resetForm();
}

// Function to update the transaction table
function updateTransactionTable(transactions) {
  console.log("inside:");
  console.log("transactions---:", transactions.length);
  console.log("inside2:");

  const table = document.getElementById("transaction-table");
  table.innerHTML = ""; // Clear existing rows

  if (transactions.length === 0) {
    table.innerHTML = "<tr><td colspan='5'>No transactions available</td></tr>";
    return;
  }

  transactions.forEach((transaction) => {
    const row = table.insertRow();

    const dateCell = row.insertCell(0);
    dateCell.textContent = transaction.transactionDate;

    const descriptionCell = row.insertCell(1);
    descriptionCell.textContent = transaction.description;

    const typeCell = row.insertCell(2);
    typeCell.textContent = transaction.type;

    const sourceCell = row.insertCell(3);
    sourceCell.textContent = transaction.source;

    const amountCell = row.insertCell(4);
    amountCell.textContent = `₹${transaction.amount.toFixed(2)}`;
    // Apply the class based on the type of transaction
    if (transaction.type === 'Debit') {
      amountCell.classList.add('debit');
    } else if (transaction.type === 'Credit') {
      amountCell.classList.add('credit');
    }
  });
}


// Display current date in the format DD/MM/YYYY
function formatDate(date) {
  const day = date.getDate().toString().padStart(2, "0");
  const month = (date.getMonth() + 1).toString().padStart(2, "0");
  const year = date.getFullYear();
  return `${year}-${month}-${day}`;
}

// Clear error messages
function clearErrorMessages() {
  const errors = document.querySelectorAll(".error-message");
  errors.forEach((error) => {
    error.style.display = "none";
    error.textContent = "";
  });
}

// Show error messages
function showError(fieldId, message) {
  const errorElement = document.getElementById(fieldId);
  errorElement.textContent = message;
  errorElement.style.display = "block";
}

// Reset the form
function resetForm() {
  document.getElementById("description").value = "";
  document.getElementById("amount").value = "";
  document.querySelector('input[name="type"][value="Debit"]').checked = true;
  document.getElementById("source").value = "cash";
  document.getElementById("transaction-date").value = formatDate(new Date());

  clearErrorMessages();
}

// Fetch username and display it
window.addEventListener("DOMContentLoaded", () => {
  const usernameDisplay = document.getElementById("username-display");
  usernameDisplay.textContent = emailOrUsername || "User1";

  // Fetch initial data on page load
  fetchInitialData();
});
function displayCurrentDate() {
  const currentDate = new Date();
  const formattedDate = currentDate.toLocaleDateString();
  document.getElementById('current-date').textContent = formattedDate;
}

// Call function to display current date on page load
displayCurrentDate();
// Logout functionality
function logout() {
  localStorage.removeItem("userLoggedIn");
  sessionStorage.clear();

  window.location.href = "index.html";
  preventBackNavigation();
}

function preventBackNavigation() {
  window.history.pushState(null, document.title, window.location.href);
  window.addEventListener("popstate", () => {
    window.history.pushState(null, document.title, window.location.href);
  });
}

// Dynamically update balances display
function updateBalances(balances) {
  const balanceContainer = document.getElementById("balance-container");
  balanceContainer.innerHTML = ""; // Clear existing elements

  balances.forEach((balance, index) => {
    const balanceElement = document.createElement("p");
    balanceElement.innerHTML = `
      <strong id="_bal${index}">${balance.bank_name.toUpperCase()} Balance : ₹ </strong> 
      <span id="bank${index}-balance">${parseFloat(balance.balance).toFixed(2)}</span>
    `;
    balanceContainer.appendChild(balanceElement);
  });
}

// Populate bank names in the dropdown
function populateBankNames(balances) {
  const sourceSelect = document.getElementById("source");
  sourceSelect.innerHTML = ""; // Clear existing options

  balances.forEach((balance) => {
    const option = document.createElement("option");
    option.value = balance.bank_name;
    option.textContent = balance.bank_name;
    sourceSelect.appendChild(option);
  });
}


// get total balance from dp and display
function updateTotalBal(totalbal) {
  // totalBalance = parseFloat(cashBalance) + parseFloat(bank1Balance) + parseFloat(bank2Balance);
  totalbal.forEach((tbal) => {
    totalBalance = tbal.totalbal;
    totalCredit = tbal.totalcredit;
    totalDebit = tbal.totaldebit;
    document.getElementById("total-balance").textContent = parseFloat(totalBalance).toFixed(2);
    document.getElementById("total-Credit").textContent = parseFloat(totalCredit).toFixed(2);
    document.getElementById("total-Debit").textContent = parseFloat(totalDebit).toFixed(2);
  })
}
