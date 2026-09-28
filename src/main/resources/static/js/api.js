
const API_BASE = '/api';

async function fetchDashboardSummary() {
    const res = await fetch(`${API_BASE}/dashboard/summary`);
    if (!res.ok) throw new Error('Failed to fetch summary');
    return res.json();
}
