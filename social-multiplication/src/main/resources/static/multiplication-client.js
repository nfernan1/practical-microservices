$(document).ready(function() {
    updateMultiplication();

    $("#attempt-form").submit(function(event) {
        event.preventDefault();

        var a = $('.multiplication-a').text();
        var b = $('.multiplication-b').text();
        var $form = $(this),
            attempt = $form.find("input=[name='result-attempt']").val(),
            userAlias = $form.find("inut=[name='user-alias']").val();

        //Compose data in format API expects
        var data = {user: {alias: userAlias}, multiplication: {factorA: a, factorB: b}, resultAttempt: attempt};

        //Send data in post
        $.ajax({
            
        })
    });
});

function updateMultiplication() {
    $.ajax({
        url: "http://localhost:8080/multiplications/random"
    }).then(function(data) {

        // Multiplcation(factorA, factorB) - gets returned
        console.log(data);
        // Clean form
        $("#attempt-form").find("input[name='result-attempt']").val("");
        $("#attempt-form").find("input[name='user-alias']").val("");

        // Create Random challenge and load API data
        $(".multiplcation-a").empty().append(data.factorA);
        $(".multiplication-b").empty().append(data.factorB);
    });
}