
const API_BASE = '/api';

async function fetchAPI(endpoint, options = {}) {
    const res = await fetch(`${API_BASE}${endpoint}`, {
        headers: { 'Content-Type': 'application/json' },
        ...options
    });
    if (!res.ok) {
        const errorText = await res.text();
        throw new Error(errorText || 'API Error');
    }
    return res.status === 204 ? null : res.json();
}

const api = {
    getDashboardSummary: () => fetchAPI('/dashboard/summary'),
    getAssets: () => fetchAPI('/assets'),
    getAsset: (id) => fetchAPI(`/assets/${id}`),
    createAsset: (data) => fetchAPI('/assets', { method: 'POST', body: JSON.stringify(data) }),
    getHealth: (id) => fetchAPI(`/assets/${id}/health`),
    getLifecycle: (id) => fetchAPI(`/assets/${id}/lifecycle`),
    getMaintenance: (id) => fetchAPI(`/assets/${id}/maintenance`)
};
