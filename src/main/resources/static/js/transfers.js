
document.getElementById('transferForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const id = document.getElementById('assetId').value;
    const msg = document.getElementById('formMessage');
    
    try {
        const payload = {
            newEmployeeId: document.getElementById('newEmployeeId').value,
            newLocation: document.getElementById('newLocation').value,
            reason: document.getElementById('reason').value
        };
        await fetchAPI(`/assets/${id}/transfer`, { method: 'PUT', body: JSON.stringify(payload) });
        msg.innerHTML = '<span style="color:var(--success)">Asset transferred successfully!</span>';
        e.target.reset();
    } catch (err) {
        msg.innerHTML = `<span style="color:var(--danger)">${err.message}</span>`;
    }
});
