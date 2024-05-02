document.addEventListener('DOMContentLoaded', function() {
    // 如果页面有课程选择的表单或链接，确保每个选择都附带课程ID
    const courseLinks = document.querySelectorAll('.course-link'); // 假设每个课程链接都有类名 'course-link'
    courseLinks.forEach(link => {
        link.addEventListener('click', function() {
            const courseId = this.getAttribute('data-course-id');
            window.location.href = `/member/select-trainer?courseId=${courseId}`;
        });
    });
    // 检查每一步是否应该激活
    const steps = document.querySelectorAll('.reservation-steps .step');
    let isPreviousCompleted = true; // 前一步是否完成

    steps.forEach((step, index) => {
        // 如果步骤已完成，激活点击事件，否则禁用
        if (step.classList.contains('completed') || isPreviousCompleted) {
            step.addEventListener('click', function() {
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

});