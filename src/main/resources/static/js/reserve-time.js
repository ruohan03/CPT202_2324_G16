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
    document.querySelectorAll('.reservation-steps .step.completed').forEach(step => {
        step.addEventListener('click', function() {
            // Retrieve parameters from hidden fields
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
    });
});