document.addEventListener('DOMContentLoaded', (event) => {
    const genreCheckboxes = document.querySelectorAll('input[name="genres"]');
    const maxSelection = 5;

    genreCheckboxes.forEach(checkbox => {
        checkbox.addEventListener('change', () => {
            const selectedCheckboxes = Array.from(genreCheckboxes).filter(checkbox => checkbox.checked);

            if (selectedCheckboxes.length > maxSelection) {
                checkbox.checked = false;
                alert('최대 5개만 선택할 수 있습니다.');
            }
        });
    });
});