
document.addEventListener('DOMContentLoaded', async () => {
    const urlParams = new URLSearchParams(window.location.search);
    const id = urlParams.get('id');
    if (!id) return;

    try {
        const asset = await api.getAsset(id);
        const health = await api.getHealth(id);
        const lifecycle = await api.getLifecycle(id);
        const maintenance = await api.getMaintenance(id);
        
        document.getElementById('assetCodeTitle').textContent = `- ${asset.assetCode}`;
        
        document.getElementById('identityDetails').innerHTML = `
            <div class="details-row"><span>Type</span> <span>${asset.assetType}</span></div>
            <div class="details-row"><span>Brand</span> <span>${asset.brand}</span></div>
            <div class="details-row"><span>Model</span> <span>${asset.model}</span></div>
            <div class="details-row"><span>Serial</span> <span>${asset.serialNumber}</span></div>
            <div class="details-row"><span>Status</span> <span class="badge ${getBadgeClass(asset.status)}">${asset.status}</span></div>
            <div class="details-row"><span>Condition</span> <span class="badge ${getBadgeClass(asset.conditionState)}">${asset.conditionState}</span></div>
            <div class="details-row"><span>Assigned</span> <span>${asset.assignedTo || 'N/A'}</span></div>
            <div class="details-row"><span>Department</span> <span>${asset.department || 'N/A'}</span></div>
            <div class="details-row"><span>Usage Hours</span> <span>${asset.usageHours}</span></div>
        `;
        
        const maintTotal = maintenance.reduce((sum, m) => sum + (m.cost || 0), 0);
        const tco = asset.purchaseCost + maintTotal;
        
        document.getElementById('financialDetails').innerHTML = `
            <div class="details-row"><span>Purchase Date</span> <span>${asset.purchaseDate || 'N/A'}</span></div>
            <div class="details-row"><span>Purchase Cost</span> <span>$${asset.purchaseCost?.toFixed(2) || '0.00'}</span></div>
            <div class="details-row"><span>Total Maintenance Cost</span> <span>$${maintTotal.toFixed(2)}</span></div>
            <div class="details-row"><span><strong>Total Cost of Ownership</strong></span> <span><strong>$${tco.toFixed(2)}</strong></span></div>
            <div class="details-row" style="margin-top: 10px;"><span>Warranty End</span> <span>${asset.warrantyEndDate || 'N/A'}</span></div>
        `;

        const scoreDiv = document.getElementById('healthScoreDisplay');
        scoreDiv.textContent = `${health.score}/100`;
        scoreDiv.style.color = getBadgeClass(health.category).replace('badge-', 'var(--');
        
        document.getElementById('riskFactorsList').innerHTML = health.factors.length > 0 ? 
            health.factors.map(f => `<li>${f}</li>`).join('') : '<li style="color:var(--success)">No major risk factors detected.</li>';
            
        document.getElementById('lifecycleRecommendation').innerHTML = `
            <div style="font-size: 1.5rem; font-weight: bold; margin-bottom: 10px;" class="badge ${getBadgeClass(lifecycle.recommendation)}">${lifecycle.recommendation}</div>
            <p>${lifecycle.justification}</p>
        `;
        
        document.getElementById('maintenanceTableBody').innerHTML = maintenance.map(m => `
            <tr>
                <td>${m.reportedDate}</td>
                <td>${m.type}</td>
                <td>${m.description}</td>
                <td>$${m.cost?.toFixed(2) || '0.00'}</td>
                <td>${m.status}</td>
            </tr>
        `).join('') || '<tr><td colspan="5">No maintenance records.</td></tr>';

    } catch (err) {
        console.error(err);
        alert('Failed to load asset details');
    }
});
