function showAddCourseForm() {
    var addCourseForm = document.getElementById('add-course-form');
    if (addCourseForm.style.display === 'none') {
        addCourseForm.style.display = 'block';
    } else {
        addCourseForm.style.display = 'none';
    }
}

$(document).ready(function() {
    $('#courseTable').DataTable();
});

document.addEventListener("DOMContentLoaded", function() {
    setTimeout(function() {
        // 获取打开模态窗口的按钮
        var addButton = document.getElementById('add-course-button');
        // 获取模态窗口元素
        var modal = document.getElementById('add-course-modal');

        // 打开模态窗口的函数
        // 打开模态窗口的函数
        function openModal() {
            console.log('openModal function called');
            modal.style.display = 'flex'; // 使用flex布局使内容居中
        }

        // 关闭模态窗口的函数
        // 关闭模态窗口的函数
        window.closeModal = function () {
            console.log('closeModal function called');
            modal.style.display = 'none';
        }

        // 点击添加教练按钮时打开模态窗口
        addButton.addEventListener('click', openModal);
        console.log('Event listener added to addButton');

        // 点击模态窗口外部区域也应关闭模态窗口
        window.addEventListener('click', function(event) {
            if (event.target == modal) {
                closeModal();
            }
        });
        console.log('Event listener added to window');
    }, 1000);
});