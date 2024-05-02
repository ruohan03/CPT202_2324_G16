document.addEventListener('DOMContentLoaded', function() {
    var memberId = sessionStorage.getItem('memberId'); // 假设在登录时已存储
    var profileLink = document.getElementById('profile-link');
    if (profileLink && memberId) {
        profileLink.href = `/profile/${memberId}`;
    }
});

document.getElementById('bookCourseBtn').addEventListener('click', function() {
    const memberId = document.body.dataset.memberId;
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