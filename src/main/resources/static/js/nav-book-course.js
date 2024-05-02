const memberId = document.body.dataset.memberId;
document.getElementById('navBookCourseBtn').addEventListener('click', function(event) {
    event.preventDefault(); // 阻止默认的链接跳转行为
    if (memberId) {
        fetch(`/members/check-validity/${memberId}`)
            .then(response => {
                if (response.ok) {
                    window.location.href = '/member/order/course';
                } else {
                    return response.text();
                }
            })
            .then(message => {
                if (message) {
                    let title = 'Member Status';
                    if (message === 'Something wrong happens, please connect with the administrator.') {
                        title = 'Error';
                    }
                    Swal.fire({
                        title: title,
                        text: message,
                        icon: 'error',
                        confirmButtonText: 'Close'
                    });
                }
            })
            .catch(error => console.error('Error:', error));
    } else {
        Swal.fire({
            title: 'Error',
            text: 'Member ID is not available.',
            icon: 'error',
            confirmButtonText: 'Close'
        });
    }
});