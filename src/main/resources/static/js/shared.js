
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

document.addEventListener('DOMContentLoaded', checkAuth);
