
    // 检查用户登录状态
    var isLoggedIn = false; // 假设用户未登录

    // 为所有按钮添加事件监听
    document.querySelectorAll('button').forEach(button => {
    button.addEventListener('click', function() {
        if (!isLoggedIn) {
            window.location.href = '/login'; // 未登录则跳转到登录页面
        } else {
            // 登录状态下的其他逻辑
            console.log('执行登录后的操作');
        }
    });
});
