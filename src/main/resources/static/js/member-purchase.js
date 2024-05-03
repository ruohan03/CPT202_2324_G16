function updatePrice(plan) {
    const prices = {
        "Diamond Super VIP": 8888,
        "Gold Super VIP": 6888,
        "Silver Super VIP": 3888,
        "Diamond VIP": 5666,
        "Gold VIP": 3666,
        "Silver VIP": 1666
    };
    document.getElementById('planPrice').textContent = prices[plan] || "Please select a plan";
}

document.querySelector('select[name="plan"]').addEventListener('change', function() {
    updatePrice(this.value);
    const durationField = document.getElementById('durationField');
    if (this.value.includes('Super VIP')) {
        durationField.value = 12;  // 12 months for VIP plans
    } else if (this.value.includes('VIP')) {
        durationField.value = 6;   // 6 months for Member plans
    } else {
        durationField.value = '';  // clear if nothing selected or recognized
    }
});

function cancelEdit() {
    window.location.href = '/home_page'; // Redirect to home page on cancel
}