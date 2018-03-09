$(document).ready(function () {
    getAllTickets();
});

openTicket = () => {
    console.log($(event.target).text());
    $.ajax({
        url: "/api/ticket/"
    });
};

getAllTickets = () => {
    $.ajax({
        url: "/api/tickets",
        success: function (data) {
            for (var i = 0; i < data.length; i++) {
                date = new Date(data[i].dueAt);
                var html = '<p class="ticketSubject">';
                html += '<span>' + data[i].creator + ' </span><br />';
                html += '<span>' + date.getDate() + "/" + (date.getMonth() + 1) + "/" + date.getFullYear() + '</span>';
                html += '</p>';

                $('#ticketsList').append(html);
            }

            $('.ticketSubject').on('click', openTicket);
        }
    });
};




