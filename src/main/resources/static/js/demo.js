$(function() {

    function listRule() {
        $("#rule-list .panel-body").html("");
        $.ajax({
            url : "/api/v1/rule/list",
            type : "GET",
            headers : {
            "Accept" : "application/json; charset=utf-8"
            }
        }).done(function(data) {
            for(idx in data){
              displayRule(data[idx].id, data[idx].name, data[idx].content);
            }
        }).fail(function(jqXHR) {
        }).always(function(jqXHR) {
        });
    }

    $("#add-condition").click(function() {
        var candidateInput = '<form>' +
            '<div class="col-sm-6"><input id="candidate-input-k-' + cCnt + '" type="text" class="form-control candidate-input" placeholder="key' + cCnt + '" aria-describedby="sizing-addon2" /></div>' +
            ' <div class="col-sm-6"><input id="candidate-input-v-' + cCnt + '" type="text" class="form-control candidate-input" placeholder="value' + cCnt + '" aria-describedby="sizing-addon2" /></div>' +
            '</form>';
        cCnt++;
        $("#candidate-group").append(candidateInput);
    });

    function displayRule(id, name, content){
        var rule = '<div class="form-group"><label for="rule-' + id + '">' + name +
                 '</label><a id="' + id + '" class="delete-rule"><span class="glyphicon glyphicon-remove" style="float: right;"></span></a><textarea id="rule-' + id + '" rows="8" class="form-control">' + content + '</textarea></div>';
        $("#rule-list .panel-body").prepend(rule);
    }

    $("#submit-condition").click(function() {
        $("#success").css("display", "none");
        $("#fail").css("display", "none");
        var rules = {};
        for(var i=1; i<=cCnt; i++) {
            if($("#candidate-input-k-" + i).val() && $("#candidate-input-v-" + i).val()){
                rules[$("#candidate-input-k-" + i).val()] = $("#candidate-input-v-" + i).val();
            }
        }
        $.ajax({
            url : "/api/v1/validate/demo-rules",
            type : "POST",
            headers : {
            "Accept" : "application/json; charset=utf-8",
            "Content-Type": "application/json; charset=utf-8"
            },
            data : JSON.stringify({ "detail" : rules }),
            dataType:"json"
        }).done(function(data) {
            if(data.answer){
                $("#success").css("display", "block");
                $("#success .panel-body").html("<pre>" + JSON.stringify(data, null, 4) + "</pre>");
            } else {
                $("#fail").css("display", "block");
                $("#fail .panel-body").html("<pre>" + JSON.stringify(data, null, 4) + "</pre>");
            }
        }).fail(function(jqXHR) {
        }).always(function(jqXHR) {
        });
    });

    $("#submit-rule").click(function() {
        if($("#rule-name").val() == "" || $("#rule-content").val() == "") {
          return;
        }
        $.ajax({
            url : "/api/v1/rule",
            type : "POST",
            headers : {
              "Accept" : "application/json; charset=utf-8",
              "Content-Type": "application/json; charset=utf-8"
            },
            data : JSON.stringify({ "name" : $("#rule-name").val(), "content" : $("#rule-content").val() }),
            dataType:"json"
        }).done(function(data) {
            displayRule(data.id, data.name, data.content);
            clearRule();
            refreshRule();
        }).fail(function(jqXHR) {
        }).always(function(jqXHR) {
        });
    });

    $(document).on('click', '.delete-rule', function(){
        $.ajax({
            url : "/api/v1/rule/" + $(this).attr('id'),
            type : "DELETE",
            headers : {
            "Accept" : "application/json; charset=utf-8"
            }
        }).done(function(data) {
            refreshRule();
            listRule();
        }).fail(function(jqXHR) {
        }).always(function(jqXHR) {
        });
    });

    $("#rm-condition").click(function() {
        clear();
    });

    $("#clear-rule").click(function() {
        clearRule();
    });

    function clearRule() {
        $("#rule-name").val("");
        $("#rule-content").val("");
        var p = $("#rule-content").parent();
        $("#rule-content").remove();
        p.append('<textarea id="rule-content" rows="5" class="form-control"></textarea>');
        dropable("rule-content");
    }

    function clear() {
        cCnt = 1;
        $("#candidate-group").html("");
    }

    function refreshRule() {
        $.ajax({
          url : "/api/v1/rule/container/demo-rules/refresh",
          type : "POST",
          headers : {
            "Accept" : "application/json; charset=utf-8"
          }
        }).done(function(data) {
        }).fail(function(jqXHR) {
        }).always(function(jqXHR) {
        });
    }
  
    // init
    var cCnt = 1;
    $("#success").css("display", "none");
    $("#fail").css("display", "none");
    listRule();
    dropable("rule-content");
});