document.addEventListener("DOMContentLoaded", function () {
    // === Bar Chart ===
    const barCtx = document.getElementById('incomeExpenseBarChart').getContext('2d');
    new Chart(barCtx, {
        type: 'bar',
        data: {
            labels: chartLabels,
            datasets: [
                {
                    label: 'Income',
                    data: incomeData,
                    backgroundColor: 'rgba(25, 135, 84, 0.7)'
                },
                {
                    label: 'Expense',
                    data: expenseData,
                    backgroundColor: 'rgba(220, 53, 69, 0.7)'
                }
            ]
        },
        options: {
            responsive: true,
            scales: {
                y: { beginAtZero: true }
            }
        }
    });

    // === Donut Chart ===
    const donutCtx = document.getElementById('currentMonthDonutChart').getContext('2d');
    new Chart(donutCtx, {
        type: 'doughnut',
        data: {
            labels: donutLabels,
            datasets: [{
                data: donutData,
                backgroundColor: donutColors
            }]
        },
        options: {
            responsive: true,
            cutout: '65%',
            plugins: {
                legend: { position: 'bottom' }
            }
        },
        plugins: [{
            id: 'centerText',
            afterDraw(chart) {
                const { ctx, chartArea: { width, height } } = chart;
                ctx.save();
                ctx.font = 'bold 16px sans-serif';
                ctx.fillStyle = '#000';
                ctx.textAlign = 'center';
                ctx.textBaseline = 'middle';
                ctx.fillText(`₹${currentMonthBalance} left`, width / 2, height / 2);
                ctx.restore();
            }
        }]
    });

    // === Transaction Search and Sort ===
    const searchInput = document.getElementById('transactionSearch');
    if (searchInput) {
        searchInput.addEventListener('keyup', filterTransactions);
    }

    function filterTransactions() {
        const filter = searchInput.value.toLowerCase();
        const rows = document.querySelectorAll('table tbody tr');
        rows.forEach(row => {
            const type = row.children[1].textContent.toLowerCase();
            const amount = row.children[2].textContent.toLowerCase();
            const desc = row.children[3].textContent.toLowerCase();
            const visible = type.includes(filter) || amount.includes(filter) || desc.includes(filter);
            row.style.display = visible ? '' : 'none';
        });
    }

    window.sortDirection = {
        date: 'asc',
        amount: 'asc'
    };

    window.sortTransactions = function (by) {
        const tbody = document.querySelector('table tbody');
        const rows = Array.from(tbody.querySelectorAll('tr'))
            .filter(row => row.children.length >= 4);

        const dir = sortDirection[by] === 'asc' ? 1 : -1;

        rows.sort((a, b) => {
            let valA, valB;
            if (by === 'date') {
                valA = new Date(a.children[0].textContent);
                valB = new Date(b.children[0].textContent);
            } else if (by === 'amount') {
                valA = parseFloat(a.children[2].textContent.replace(/[₹,]/g, '')) || 0;
                valB = parseFloat(b.children[2].textContent.replace(/[₹,]/g, '')) || 0;
            }
            return valA > valB ? dir : valA < valB ? -dir : 0;
        });

        sortDirection[by] = sortDirection[by] === 'asc' ? 'desc' : 'asc';
        rows.forEach(row => tbody.appendChild(row));
    };
});
