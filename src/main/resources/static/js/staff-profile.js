function enableEdit() {
    const container = document.querySelector('.profile-container');
    container.classList.add('edit-mode');
    document.querySelectorAll('.profile-table .text-input').forEach(function(input) {
        input.disabled = false;
    });

    // 显示性别的下拉菜单并隐藏文本显示
    const genderSelect = document.querySelector('select[name="gender"]');
    const genderTextDisplay = document.querySelector('.text-display.gender-display');

    if (genderSelect) {
        genderSelect.style.display = 'block';
    }
    if (genderTextDisplay) {
        genderTextDisplay.style.display = 'none';
    }

    document.querySelector('.edit-button').style.display = 'none';
    document.querySelector('.save-button').style.display = 'inline';
    document.querySelector('.cancel-button').style.display = 'inline';
}

function cancelEdit() {
    const container = document.querySelector('.profile-container');
    container.classList.remove('edit-mode');
    document.querySelectorAll('.profile-table .text-input').forEach(function(input) {
        input.disabled = true;
    });

    // 隐藏性别的下拉菜单并显示文本
    const genderSelect = document.querySelector('select[name="gender"]');
    const genderTextDisplay = document.querySelector('.text-display.gender-display');

    if (genderSelect) {
        genderSelect.style.display = 'none';
    }
    if (genderTextDisplay) {
        genderTextDisplay.style.display = 'block';
    }

    document.querySelector('.edit-button').style.display = 'inline';
    document.querySelector('.save-button').style.display = 'none';
    document.querySelector('.cancel-button').style.display = 'none';
}