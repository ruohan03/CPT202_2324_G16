window.onload = function() {
    const steps = document.querySelectorAll('.reservation-steps .step');
    let isPreviousCompleted = true; // 前一步是否完成

    steps.forEach((step, index) => {
        // 如果步骤已完成，激活点击事件，否则禁用
        if (step.classList.contains('completed') || isPreviousCompleted) {
            step.addEventListener('click', function() {
                if (step.classList.contains('active')) {
                    return;
                }
                const url = this.getAttribute('data-href');
                if (url) {
                    window.location.href = url;
                }
            });
        } else {
            // 禁用未完成的步骤的点击事件
            step.style.pointerEvents = 'none';
            step.style.opacity = '0.5'; // 给未激活的步骤一个明显的视觉差异
        }

        // 更新是否所有前步都已完成的状态
        isPreviousCompleted = step.classList.contains('completed');
    });
};