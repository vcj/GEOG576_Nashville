function onSelectReportType(ele){
    var form = $(ele).parent().parent();
    var label = $(form).find(".additional_msg");
    var select = $(form).find(".additional_msg_select");

    switch (ele.value) {
        case "art":
            label.text("Art Type");
            select.find('option').remove();
            select.append($("<option></option>")
                .attr("value","")
                .text("Art Type"));
            selectValues = ['Mural','Sculpture', 'Mosaic','Mobile','Frieze'];
            $.each(selectValues, function(index,value) {
                select.append($("<option></option>")
                    .attr("value",value)
                    .text(value));
            });
            break;
        case "historical":
            label.text("Civil War");
            select.find('option').remove();
            select.append($("<option></option>")
                .attr("value","")
                .text("Civil War Site?"));
            selectValues = ['true','false'];
            $.each(selectValues, function(index,value) {
                select.append($("<option></option>")
                    .attr("value",value)
                    .text(value));
            });
            break;
        default:
            $(form).find(".additional_msg_div").css("visibility", "hidden");
            return;
    }
    $(form).find(".additional_msg_div").css("visibility", "visible");
}

function queryReport(event) {
    event.preventDefault(); // stop form from submitting normally

    var a = $("#query_report_form").serializeArray();
    a.push({ name: "tab_id", value: "1" });
    try {
        a.push({ name: "longitude", value: place.geometry.location.lng() });
    } catch (error) {
        a.push({ name: "longitude", value: "" });
    };
    try {
        a.push({ name: "latitude", value: place.geometry.location.lat() });
    } catch (error) {
        a.push({ name: "latitude", value: "" });
    };
    a = a.filter(function(item){return item.value != '';});
    $.ajax({
        url: 'HttpServlet',
        type: 'POST',
        data: a,
        success: function(reports) {
            mapInitialization(reports);
        },
        error: function(xhr, status, error) {
            alert("Status: " + status + "\nError: " + error);
        }
    });
}

$("#query_report_form").on("submit",queryReport);



///question 4 working
function resetForm(event) {
    document.getElementById("create_report_form").reset();
};


///question 4 part 1 runs create report
function createReport(event) {
    event.preventDefault(); // stop form from submitting normally

    var a = $("#create_report_form").serializeArray();
    //a["longitude"] = place.geometry.location.lng();
    //a["latitude"] = place.geometry.location.lat();
    a.push({ name: "tab_id", value: "0" });
    //question 4 part 2 adds the latitude and longitude
    //a.push({ name: "longitude", value: place.geometry.location.lng() });
    //a.push({ name: "latitude", value:  place.geometry.location.lat() });
    a = a.filter(function(item){return item.value != '';});

    //console.log(a);
    $.ajax({
        url: 'HttpServlet',
        type: 'POST',
        data: a,
        success: function(reports) {
            $.ajax({
                url: 'HttpServlet',
                type: 'POST',
                data: { "tab_id": "1"},
                success: function(reports) {
                    //question 4 part 3 adds the alert box
                    alert("The report was successfully submitted!");
                    //question 4 part 5 re-runs map initialization
                    mapInitialization(reports);
                },
                error: function(xhr, status, error) {
                    alert("An AJAX error occured: " + status + "\nError: " + error);
                }
            });
        },
        error: function(xhr, status, error) {
            alert("Status: " + status + "\nError: " + error);
        }
    });
    //question 4 part 4 resets the form, i used a function called resetForm to do this
    resetForm();

}


$("#create_report_form").on("submit",createReport);
