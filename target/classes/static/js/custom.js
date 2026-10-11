// Custom JavaScript for ATS
document.addEventListener('DOMContentLoaded', function () {
    // Auto-dismiss alerts after 5 seconds
    const alerts = document.querySelectorAll('.alert-dismissible');
    alerts.forEach(function (alert) {
        setTimeout(function () {
            const bsAlert = bootstrap.Alert.getOrCreateInstance(alert);
            if (bsAlert) {
                bsAlert.close();
            }
        }, 5000);
    });

    // File input label update
    const fileInputs = document.querySelectorAll('input[type="file"]');
    fileInputs.forEach(function (input) {
        input.addEventListener('change', function (e) {
            const fileName = e.target.files[0] ? e.target.files[0].name : 'No file chosen';
            const feedbackElem = document.getElementById(input.id + '-feedback');
            if (feedbackElem) {
                feedbackElem.textContent = 'Selected: ' + fileName;
                feedbackElem.classList.remove('d-none');
            }
        });
    });

    // Mobile sidebar toggle
    const sidebarToggle = document.getElementById('sidebarToggle');
    const sidebar = document.querySelector('.app-sidebar');
    if (sidebarToggle && sidebar) {
        sidebarToggle.addEventListener('click', function () {
            sidebar.classList.toggle('show');
        });
    }
});
