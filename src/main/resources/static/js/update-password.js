document.addEventListener("DOMContentLoaded", function() {
    const errorMessage = document.querySelector('.alert-danger');
    if (errorMessage) {
        setTimeout(() => {
            errorMessage.style.display = 'none'; // 或者 successMessage.remove(); 若希望从 DOM 中完全移除元素
        }, 3000); // 设置延时为 5000 毫秒（5 秒）
    }
    document.addEventListener("DOMContentLoaded", function() {
        const successMessage = document.querySelector('.alert-success');
        if (successMessage) {
            setTimeout(() => {
                successMessage.style.display = 'none'; // 或者 successMessage.remove(); 若希望从 DOM 中完全移除元素
            }, 3000); // 设置延时为 5000 毫秒（5 秒）
        }
    });
});
function cancelEdit() {
    window.location.href = '/home_page'; // Redirect to home page on cancel
}