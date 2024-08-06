$(document).ready(function() {
    var socket = new SockJS('/chat');
    var stompClient = Stomp.over(socket);

    stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/public', function(messageOutput) {
            showMessage(JSON.parse(messageOutput.body));
        });
    });

    function showMessage(messageOutput) {
        var chatBox = $('#chatBox');
        chatBox.append('<div><strong>' + messageOutput.sender + ':</strong> ' + messageOutput.content + '</div>');
        chatBox.scrollTop(chatBox[0].scrollHeight);
    }

    $('#sendButton').click(function() {
        var messageInput = $('#messageInput').val();
        var message = {
            sender: 'Kullanıcı', // Bu kısmı dinamik hale getirebilirsiniz
            content: messageInput
        };
        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(message));
        $('#messageInput').val('');
    });

    $('#messageInput').on('keypress', function(e) {
        if (e.which == 13) {
            $('#sendButton').click();
        }
    });
});
