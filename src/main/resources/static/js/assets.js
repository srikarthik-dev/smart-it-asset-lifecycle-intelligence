
document.addEventListener('DOMContentLoaded', async () => {
    try {
        const assets = await api.getAssets();
        const tbody = document.getElementById('assetsTableBody');
        
        tbody.innerHTML = assets.map(a => `
            <tr>
                <td>${a.assetCode}</td>
                <td>${a.assetType}</td>
                <td>${a.brand} ${a.model}</td>
                <td><span class="badge ${getBadgeClass(a.status)}">${a.status}</span></td>
                <td><span class="badge ${getBadgeClass(a.conditionState)}">${a.conditionState}</span></td>
                <td>${a.assignedTo || 'Unassigned'}</td>
                <td><a href="asset-details.html?id=${a.id}" class="btn btn-secondary" style="padding: 4px 8px; font-size: 0.8rem;">View</a></td>
            </tr>
        `).join('');
    } catch (err) {
        console.error(err);
        document.getElementById('assetsTableBody').innerHTML = '<tr><td colspan="7">Failed to load assets</td></tr>';
    }
});

function filterTable() {
    const filter = document.getElementById('searchInput').value.toUpperCase();
    const rows = document.getElementById('assetsTableBody').getElementsByTagName('tr');
    for (let i = 0; i < rows.length; i++) {
        const txtValue = rows[i].textContent || rows[i].innerText;
        rows[i].style.display = txtValue.toUpperCase().indexOf(filter) > -1 ? "" : "none";
    }
}
