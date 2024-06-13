document.addEventListener("DOMContentLoaded", function () {
    let buttons = document.querySelectorAll(".side-container-button");

    buttons.forEach((btn) => {
        btn.addEventListener("click", function () {
            buttons.forEach(function (btn) {
                btn.classList.remove("active");
                btn.classList.add("inactive");
            });

            this.classList.remove("inactive");
            this.classList.add("active");
        });
    });
});
