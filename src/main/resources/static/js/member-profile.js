window.onload = function() {
    const genderSelect = document.getElementById('genderSelect');
    const genderDisplay = document.getElementById('genderDisplay');

    // 初始化显示的性别
    const initialGender = genderSelect.options[genderSelect.selectedIndex].text;
    genderDisplay.textContent = initialGender;

    // 处理select元素的change事件
    genderSelect.addEventListener('change', function() {
        // 更新member.gender的值
        member.gender = genderSelect.value;
        // 更新genderDisplay的文本内容
        genderDisplay.textContent = genderSelect.options[genderSelect.selectedIndex].text;
    });

    document.getElementById('editButton').addEventListener('click', function() {
        // 进入编辑模式
        genderSelect.disabled = false;
        genderDisplay.style.display = 'none';
        genderSelect.style.display = 'block';
    });

    document.getElementById('cancelButton').addEventListener('click', function() {
        // 退出编辑模式
        genderSelect.disabled = true;
        genderDisplay.style.display = 'block';
        genderSelect.style.display = 'none';
        // 重置select元素的值
        genderSelect.value = initialGender;
        // 更新genderDisplay的文本内容
        genderDisplay.textContent = genderSelect.options[genderSelect.selectedIndex].text;
    });

    enableEdit();
    cancelEdit();
};

function enableEdit() {
    // Enable edit mode and make editable fields active
    const container = document.querySelector('.profile-container');
    container.classList.add('edit-mode');
    document.querySelectorAll('.profile-table .text-input, .profile-table select.text-input').forEach(function(input) {
        if (input.hasAttribute('data-original-value')) {
            input.disabled = false; // Enable only those inputs which have original values set
            input.style.display = 'block'; // Show input
            input.nextElementSibling.style.display = 'none'; // Hide span
            input.value = input.getAttribute('data-value'); // Display the actual value instead of the Thymeleaf expression
        }
    });
    document.querySelector('.edit-button').style.display = 'none';
    document.querySelector('.save-button').style.display = 'inline';
    document.querySelector('.cancel-button').style.display = 'inline';

    // Set the value of the genderSelect element to the current gender
    const genderSelect = document.getElementById('genderSelect');
    const genderDisplay = document.getElementById('genderDisplay');
    genderSelect.value = genderDisplay.textContent;
}

function cancelEdit() {
    const container = document.querySelector('.profile-container');
    container.classList.remove('edit-mode');
    document.querySelectorAll('.profile-table .text-input, .profile-table select.text-input').forEach(function(input) {
        input.disabled = true;
        input.style.display = 'none'; // Hide input
        var span = input.nextElementSibling;
        span.style.display = 'block'; // Show span
        span.textContent = span.getAttribute('data-value'); // Display the actual value instead of the Thymeleaf expression
        // 这一行会将输入框的值重置为其在 data-original-value 属性中的原始值
        input.value = input.getAttribute('data-original-value');
    });
    document.querySelector('.edit-button').style.display = 'inline';
    document.querySelector('.save-button').style.display = 'none';
    document.querySelector('.cancel-button').style.display = 'none';
}
document.addEventListener("DOMContentLoaded", function() {
    const errorMessage = document.querySelector('.alert-danger');
    if (errorMessage) {
        setTimeout(() => {
            errorMessage.style.display = 'none'; // 或者 successMessage.remove(); 若希望从 DOM 中完全移除元素
        }, 3000); // 设置延时为 5000 毫秒（5 秒）
    }    document.addEventListener("DOMContentLoaded", function() {
        const successMessage = document.querySelector('.alert-success');
        if (successMessage) {
            setTimeout(() => {
                successMessage.style.display = 'none'; // 或者 successMessage.remove(); 若希望从 DOM 中完全移除元素
            }, 3000); // 设置延时为 5000 毫秒（5 秒）
        }
    });
});