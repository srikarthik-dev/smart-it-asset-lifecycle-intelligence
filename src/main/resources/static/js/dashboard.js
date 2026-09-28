
document.addEventListener('DOMContentLoaded', async () => {
    try {
        const summary = await fetchDashboardSummary();
        renderKPIs(summary);
        renderCharts(summary);
    } catch (err) {
        console.error('Error loading dashboard', err);
    }
});

function renderKPIs(data) {
    const grid = document.getElementById('kpiGrid');
    const kpis = [
        { label: 'Total Assets', value: data.totalAssets },
        { label: 'Assigned', value: data.assigned },
        { label: 'Available', value: data.available },
        { label: 'Under Maintenance', value: data.underMaintenance },
        { label: 'At Risk', value: data.atRisk, color: 'var(--warning)' },
        { label: 'Replace Recommended', value: data.replacementRecommended, color: 'var(--danger)' }
    ];
    
    grid.innerHTML = kpis.map(k => `
        <div class="kpi-card">
            <div class="kpi-label">${k.label}</div>
            <div class="kpi-value" style="color: ${k.color || 'white'}">${k.value}</div>
        </div>
    `).join('');
}

function renderCharts(data) {
    const ctx = document.getElementById('healthChart').getContext('2d');
    const healthy = data.totalAssets - data.atRisk - data.replacementRecommended;
    
    new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: ['Healthy / Monitor', 'At Risk', 'Replace Recommended'],
            datasets: [{
                data: [healthy, data.atRisk, data.replacementRecommended],
                backgroundColor: ['#10b981', '#f59e0b', '#ef4444'],
                borderWidth: 0
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { position: 'bottom', labels: { color: 'white' } }
            }
        }
    });
}
