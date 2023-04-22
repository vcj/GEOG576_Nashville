function onSelectReportType(ele){
    var form = $(ele).parent().parent();
    var label = $(form).find(".additional_msg");
    var select = $(form).find(".additional_msg_select");

    switch (ele.value) {
        case "donation":
        case "request":
            label.text("Resource Type:");
            select.find('option').remove();
            select.append($("<option></option>")
                .attr("value","")
                .text("Choose the resource type"));
            selectValues = ['water', 'food', 'money', 'medicine', 'cloth',
                'rescue/volunteer'];
            $.each(selectValues, function(index,value) {
                select.append($("<option></option>")
                    .attr("value",value)
                    .text(value));
            });
            break;
        case "damage":
            label.text("Damage Type:");
            select.find('option').remove();
            select.append($("<option></option>")
                .attr("value","")
                .text("Choose the damage type"));
            selectValues = ['pollution', 'building damage', 'road damage', 'casualty',
                'other'];
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
// Q4-1 & 4-2 Create report, set tab-id 0 and lat/long
function createReport(event) {
    event.preventDefault(); // stop form from submitting normally

    var a = $("#create_report_form").serializeArray()
    var latitude = place.geometry.location.lat()
    var longitude = place.geometry.location.lng()
    console.log("loadform.js createReport, testing lat and long" + latitude + " " + longitude);
    a.push({ name: "tab_id", value: "0"});
    a.push({ name: "longitude", value: longitude});
    a.push({ name: "latitude", value: latitude});
    a = a.filter(function(item){return item.value != '';});
    $.ajax({
        url: 'HttpServlet',
        type: 'POST',
        data: a,
        // Q 4-3 alert box for successful submission
        success: function(reports) {
            mapInitialization(reports);
            window.alert("The report is successfully submitted!");
            console.log("loadmap.js successful report submission...will it reset???");
            // Q4-4 reset form
            document.getElementById("create_report_form").reset();
            console.log("loadmap.js if you're seeing this, the form reset!");
            // Q4-5 another Ajax query to query all reports in the server and call mapInitialization function in the success callback
            // with the new marker for the report just submitted
            $.ajax({
                url: 'HttpServlet',
                type: 'POST',
                data: { "tab_id": "1"},
                success: function(reports) {
                    mapInitialization(reports);
                    // Extra Credit - zoom map to new marker after form submission
                    onPlaceChanged();
                    console.log("loadform.js, reload map with new marker. if you are seeing this message, everything apparently worked!!!")
                },
                error: function(xhr, status, error) {
                    alert("An AJAX error occurred: " + status + "\nError: " + error);
                }
            });
        },
        error: function(xhr, status, error) {
            window.alert("Status: " + status + "\nError: " + error);
        }
    });
}

$("#query_report_form").on("submit",queryReport);

$("#create_report_form").on("submit",createReport);



