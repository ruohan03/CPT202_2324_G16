document.addEventListener('DOMContentLoaded', function() {
    // 如果页面有课程选择的表单或链接，确保每个选择都附带课程ID
    const courseLinks = document.querySelectorAll('.course-link'); // 假设每个课程链接都有类名 'course-link'
    courseLinks.forEach(link => {
        link.addEventListener('click', function() {
            const courseId = this.getAttribute('data-course-id');
            window.location.href = `/member/select-trainer?courseId=${courseId}`;
        });
    });
});