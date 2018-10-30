$(document).ready(function() {
    postResultAttempt();
});

function updateMultiplication() {
    $.ajax({
        url: "http://localhost:8080/multiplications/random"
    }).then(function(data) {

        // Multiplcation(factorA, factorB) - gets returned

        // Clean form
        $("#attempt-form").find("input[name='result-attempt']").val("");
        $("#attempt-form").find("input[name='user-alias']").val("");

        // Create Random challenge and load API data
        $(".multiplication-a").empty().append(data.factorA);
        $(".multiplication-b").empty().append(data.factorB);
    });
}

function postResultAttempt() {
    updateMultiplication();

    $("#attempt-form").submit(function(event) {
        event.preventDefault();

        var a = $('.multiplication-a').text();
        var b = $('.multiplication-b').text();
        var $form = $(this),
            attempt = $form.find("input[name='result-attempt']").val(),
            userAlias = $form.find("input[name='user-alias']").val();

        //Compose data in format API expects
        var data = {user: {alias: userAlias}, multiplication: {factorA: a, factorB: b}, resultAttempt: attempt};

        //Send data in post
        $.ajax({
            url: '/results',
            type: 'POST',
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            async: false,
            success: function(result) {
                if (result.correct) {
                    $(".result-message").empty().append("The result is Correct!");
                } else {
                    $(".result-message").empty().append("You ain't right! Try again!");
                }
            }
        });
        updateMultiplication();
        updateStats(userAlias);
    });
}

function updateStats(alias) {
    $.ajax({
        url: "http://localhost:8080/results?alias=" + alias,
    }).then(function(data) {
        $("#stats-body").empty();
        data.forEach(function(row) {
           $("#stats-body").append(
               "<tr><td>" + row.id + "</td>" +
               "<td>" + row.multiplication.factorA + 'x' + row.multiplication.factorB + "</td>" +
               "<td>" + row.resultAttempt + "</td>" +
               "<td>" + (row.correct === true ? 'YES' : 'NO') + "</td></tr>");
        });
    });
}