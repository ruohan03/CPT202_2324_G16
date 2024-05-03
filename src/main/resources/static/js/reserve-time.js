document.addEventListener('DOMContentLoaded', function() {
    const today = new Date();
    today.setDate(today.getDate() + 1);
    const nextWeek = new Date(today.getFullYear(), today.getMonth(), today.getDate() + 7);
    const datePicker = document.getElementById('date');
    datePicker.min = today.toISOString().split('T')[0];
    datePicker.max = nextWeek.toISOString().split('T')[0];

    datePicker.onchange = function() {
        fetchAvailability();
    };

    function fetchAvailability() {
        const staffId = document.querySelector('input[name="staffId"]').value;
        const date = datePicker.value;
        if (date) {
            $.ajax({
                url: '/api/check-time/' + staffId + '?date=' + date,
                type: 'GET',
                success: function(data) {
                    const results = document.getElementById('availabilityResults');
                    results.innerHTML = '';
                    const timeSlots = ['09:00-11:00', '11:00-13:00', '13:00-15:00', '15:00-17:00', '17:00-19:00', '19:00-21:00'];
                    timeSlots.forEach((slot, i) => {
                        const button = document.createElement('button');
                        button.type = 'button';
                        button.className = `time-slot ${data[i] ? 'available' : 'unavailable'}`;
                        button.textContent = slot;
                        button.disabled = !data[i];
                        button.onclick = function() {
                            checkForExistingReservation(slot);
                        };
                        results.appendChild(button);
                    });
                }
            });
        }
    }

    function checkForExistingReservation(time) {
        const [startTime, endTime] = time.split('-');
        const memberId = document.querySelector('input[name="memberId"]').value;
        const selectedDate = datePicker.value;
        $.ajax({
            url: `/api/has-reservation/${memberId}`,
            data: { date: selectedDate, startTime: startTime, endTime: endTime },
            type: 'GET',
            success: function(hasReservation) {
                if (hasReservation) {
                    $('#reservationModal').modal('show');
                } else {
                    document.querySelector('input[name="time"]').value = time;
                    document.querySelector('form').submit();
                }
            },
            error: function() {
                alert('An error occurred while checking an existing appointment.');
            }
        });
    }
    window.onload = function() {
        const steps = document.querySelectorAll('.reservation-steps .step');
        let isPreviousCompleted = true; // 前一步是否完成

        steps.forEach((step, index) => {
            // 更新是否所有前步都已完成的状态
            isPreviousCompleted = step.classList.contains('completed');

            // 如果步骤已完成，激活点击事件，否则禁用
            if (isPreviousCompleted || step.classList.contains('active')) {
                step.addEventListener('click', function() {
                    if (step.classList.contains('active')) {
                        // 如果这个步骤是当前步骤，那么什么也不做
                        return;
                    }
                    const courseId = document.querySelector('input[name="courseId"]').value;
                    const staffId = document.querySelector('input[name="staffId"]').value;

                    // Build URL and redirect
                    let url = this.getAttribute('data-href');
                    url += `?courseId=${courseId}`;
                    if (url.includes('select-time')) {
                        url += `&staffId=${staffId}`;
                    } else if (url.includes('confirm-reservation')) {
                        url += `&staffId=${staffId}`;
                    }
                    window.location.href = url;
                });
            } else {
                // 禁用未完成的步骤的点击事件
                step.style.pointerEvents = 'none';
                step.style.opacity = '0.5'; // 给未激活的步骤一个明显的视觉差异
            }
        });
    };
});