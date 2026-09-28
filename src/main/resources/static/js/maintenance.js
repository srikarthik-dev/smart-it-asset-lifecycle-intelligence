
document.getElementById('maintenanceForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const id = document.getElementById('assetId').value;
    const msg = document.getElementById('formMessage');
    
    try {
        const payload = {
            type: document.getElementById('type').value,
            description: document.getElementById('description').value,
            reportedDate: document.getElementById('reportedDate').value,
            cost: parseFloat(document.getElementById('cost').value),
            severity: document.getElementById('severity').value,
            technician: document.getElementById('technician').value,
            status: document.getElementById('status').value
        };
        await fetchAPI(`/assets/${id}/maintenance`, { method: 'POST', body: JSON.stringify(payload) });
        msg.innerHTML = '<span style="color:var(--success)">Maintenance logged successfully!</span>';
        e.target.reset();
    } catch (err) {
        msg.innerHTML = `<span style="color:var(--danger)">${err.message}</span>`;
    }
});
