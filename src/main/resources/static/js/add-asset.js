
document.getElementById('addAssetForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const btn = e.target.querySelector('button');
    const msg = document.getElementById('formMessage');
    
    btn.disabled = true;
    btn.textContent = 'Saving...';
    
    try {
        const payload = {
            assetCode: document.getElementById('assetCode').value,
            assetType: document.getElementById('assetType').value,
            brand: document.getElementById('brand').value,
            model: document.getElementById('model').value,
            serialNumber: document.getElementById('serialNumber').value,
            purchaseDate: document.getElementById('purchaseDate').value,
            purchaseCost: parseFloat(document.getElementById('purchaseCost').value),
            warrantyEndDate: document.getElementById('warrantyEndDate').value,
        };
        
        await api.createAsset(payload);
        msg.innerHTML = '<span style="color:var(--success)">Asset created successfully!</span>';
        setTimeout(() => window.location.href = 'assets.html', 1500);
    } catch (err) {
        msg.innerHTML = `<span style="color:var(--danger)">${err.message}</span>`;
        btn.disabled = false;
        btn.textContent = 'Save Asset';
    }
});
