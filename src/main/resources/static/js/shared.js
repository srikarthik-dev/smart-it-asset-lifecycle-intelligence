
function checkAuth() {
    const role = localStorage.getItem('userRole');
    if (!role && !window.location.pathname.endsWith('index.html')) {
        window.location.href = 'index.html';
    }
    const userInfo = document.getElementById('userInfo');
    if (userInfo) userInfo.textContent = `Role: ${role}`;
}

function logout() {
    localStorage.removeItem('userRole');
    window.location.href = 'index.html';
}

function getBadgeClass(category) {
    if (['HEALTHY', 'EXCELLENT', 'GOOD', 'AVAILABLE', 'KEEP_IN_SERVICE'].includes(category)) return 'badge-success';
    if (['MONITOR', 'FAIR', 'ASSIGNED', 'MAINTENANCE_REQUIRED'].includes(category)) return 'badge-info';
    if (['AT_RISK', 'POOR', 'UNDER_MAINTENANCE'].includes(category)) return 'badge-warning';
    if (['REPLACEMENT_RECOMMENDED', 'CRITICAL', 'LOST', 'RETIRE'].includes(category)) return 'badge-danger';
    return 'badge-secondary';
}

document.addEventListener('DOMContentLoaded', checkAuth);
