// Navbar toggle
const toggle_btn = document.querySelector('.dropdown-toggle');
const navigation = document.querySelector('header .navigation');

toggle_btn.addEventListener('click', () => {
    if (navigation.classList.contains('active')) {
        navigation.classList.remove('active');
    } else {
        navigation.classList.add('active');
    }
});

// Set window width in .height element
document.querySelector('.height').innerHTML = window.innerWidth;

// Fetch and display stock data
$(document).ready(function() {
    $.ajax({
        url: 'https://v6.exchangerate-api.com/v6/17157c640502f3ca70087c83/latest/USD',
        method: 'GET',
        success: function(response) {
            console.log(response); // Yanıtı konsola yazdır
            var stockDataBody = $('#stockDataBody');
            stockDataBody.empty(); // Önceki verileri temizle
            var timeSeries = response.conversion_rates;
            $.each(timeSeries, function(currency, rate) {
                var row = '<tr>' +
                    '<td>' + currency + '</td>' +
                    '<td>' + rate + '</td>' +
                    '</tr>';
                stockDataBody.append(row);
            });
        },
        error: function(error) {
            console.error('API erişim hatası:', error);
            $('#stockDataContainer').html('<div class="alert alert-danger" role="alert">API erişim hatası</div>');
        }
    });
});
