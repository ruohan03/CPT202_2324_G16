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