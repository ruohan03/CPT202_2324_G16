$(document).ready(function() {
    // 提交预约表单
    $("form").submit(function(e) {
        e.preventDefault();

        $.post("/member/finalize-reservation", $(this).serialize(), function(response) {
            $('#modalMessage').text(response.message);
            if (response.result === "success") {
                // 显示预约成功的模态窗口
                $('#modalTitle').text("The appointment was successful");
                $('#messageModal').modal('show');
                console.log(response);
            } else {
                // 显示预约失败的模态窗口
                $('#modalTitle').text("The appointment failed");
                $('#messageModal').modal('show');
                console.log(response);
            }
        });
    });

    // 预约成功，点击确认后回到主页
    $('#confirmButton').click(function() {
        $('#messageModal').modal('hide');
        if ($('#modalTitle').text() === "The appointment was successful") {
            window.location.href = '/home_page';
        } else {
            window.location.href = '/member/order/course';
        }
    });

    document.querySelectorAll('.reservation-steps .step.completed').forEach(step => {
        step.addEventListener('click', function() {
            // 从隐藏字段获取参数
            const courseId = $('input[name="courseId"]').val();
            const staffId = $('input[name="staffId"]').val();

            // 构建URL并跳转
            let url = this.getAttribute('data-href');
            url += `?courseId=${courseId}`;
            if (url.includes('select-time')) {
                url += `&staffId=${staffId}`;
            }
            window.location.href = url;
        });
    });
});
document.addEventListener('DOMContentLoaded', function() {
    // 检查每一步是否应该激活
    const steps = document.querySelectorAll('.reservation-steps .step');
    let isPreviousCompleted = true; // 前一步是否完成

    steps.forEach((step, index) => {
        // 如果步骤已完成，激活点击事件，否则禁用
        if (step.classList.contains('completed') || isPreviousCompleted) {
            step.addEventListener('click', function () {
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