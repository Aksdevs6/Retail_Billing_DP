/**
 * SHARED UTILITY: app.js
 * -----------------------
 * Common JavaScript functions shared across all pages.
 * Included in every HTML page via <script src="js/app.js">
 */

/**
 * SHOW TOAST NOTIFICATION
 * Displays a temporary message at the bottom-right of screen.
 *
 * @param {string} message - Text to display
 * @param {string} type    - 'success' | 'error' | 'warning'
 */
function showToast(message, type = 'success') {
    const toast = document.getElementById('toast');
    toast.textContent = message;
    toast.className = `toast ${type} show`;

    // Auto-hide after 3 seconds
    setTimeout(() => {
        toast.className = 'toast';
    }, 3000);
}

/**
 * FORMAT CURRENCY
 * Formats a number as Indian Rupee string
 *
 * @param {number} amount
 * @returns {string} e.g. "₹1,250.00"
 */
function formatCurrency(amount) {
    return new Intl.NumberFormat('en-IN', {
        style: 'currency',
        currency: 'INR'
    }).format(amount);
}

/**
 * FORMAT DATE
 * Formats a date string in readable Indian format
 *
 * @param {string} dateStr - ISO date string from backend
 * @returns {string} e.g. "15 Apr 2026, 3:30 PM"
 */
function formatDate(dateStr) {
    return new Date(dateStr).toLocaleString('en-IN', {
        day: '2-digit', month: 'short', year: 'numeric',
        hour: '2-digit', minute: '2-digit'
    });
}
