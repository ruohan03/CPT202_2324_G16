document.addEventListener("DOMContentLoaded", function() {
    const today = new Date();
    const oneWeekAgo = new Date(today.getFullYear(), today.getMonth(), today.getDate() - 7);
    const oneMonthAgo = new Date(today.getFullYear(), today.getMonth() - 1, today.getDate());
    const oneYearAgo = new Date(today.getFullYear() - 1, today.getMonth(), today.getDate());

    document.getElementById("oneWeekBtn").addEventListener("click", function() {
        document.getElementById("startDate").value = oneWeekAgo.toISOString().split('T')[0];
        document.getElementById("endDate").value = today.toISOString().split('T')[0];
    });

    document.getElementById("oneMonthBtn").addEventListener("click", function() {
        document.getElementById("startDate").value = oneMonthAgo.toISOString().split('T')[0];
        document.getElementById("endDate").value = today.toISOString().split('T')[0];
    });

    document.getElementById("oneYearBtn").addEventListener("click", function() {
        document.getElementById("startDate").value = oneYearAgo.toISOString().split('T')[0];
        document.getElementById("endDate").value = today.toISOString().split('T')[0];
    });
});