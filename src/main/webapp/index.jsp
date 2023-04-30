<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Web Project</title>

    <!-- Custom styles -->
    <link rel="stylesheet" href="css/style.css">

    <!-- jQuery -->
    <script src="//code.jquery.com/jquery-1.11.3.min.js"></script>
    <script src="//code.jquery.com/jquery-migrate-1.2.1.min.js"></script>

    <!-- Bootstrap -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>

    <! GOogle Map JS Libraries -->
    <script src="https://maps.googleapis.com/maps/api/js?key=REPLACEME&libraries=geometry,places">
    </script>

</head>

<body>
<nav class="navbar navbar-inverse navbar-fixed-top">
    <a class="navbar-brand">Explore Nashville by Foot</a>
</nav>

<div class="container-fluid">
    <div class="row">
        <div class="sidebar col-xs-3">

            <!-- Tab Navis-->
            <ul class="nav nav-tabs">
                <li class="active"><a href="#query_site" data-toggle="tab">Find a Site!</a></li>
                <li><a href="#create_review" data-toggle="tab">Review a Site</a></li>
            </ul>

            <!-- Tab panes -->
            <div class="tab-content ">
                <!-- Create Report Tab Panel -->
                <div class="tab-pane" id="create_review">
                    <form id = "create_report_form">
                        <div><label>Site Name:&nbsp</label><input placeholder="Site name" name="sN" id="sN"></div>
                        <div><label>Stars:</label>
                            <select name="stars">
                                <option value="">Rate the site</option>
                                <option value="One Star">One Star</option>
                                <option value="Two Stars">Two Stars</option>
                                <option value="Three Stars">Three Stars</option>
                                <option value="Four Stars">Four Stars</option>
                                <option value="Five Stars">Five Stars!</option>
                            </select>
                        </div>
                        <div><label>Comment:&nbsp</label><input placeholder="Additional message" name="message"></div>
                        <div><label>First Name:&nbsp</label><input placeholder="Your first name" name="fN"></div>
                        <div><label>Last Name:&nbsp</label><input placeholder="Your last name" name="lN"></div>
                        <div><label>Age:&nbsp</label><input placeholder="Your age" name="age"></div>
                        <button type="submit" class="btn btn-default" id="report_submit_btn">
                            Submit the review <span class="glyphicon glyphicon-pencil"></span>
                        </button>
                    </form>
                </div>

                <!-- Query Report Tab Panel -->
                <div class="tab-pane active" id="query_site">
                    <form id = "query_report_form">
                        <div><label>Site Type:</label>
                            <select onchange="onSelectReportType(this)" name="site_type">
                                <option value="">Choose the site type</option>
                                <option value="art">Public Art</option>
                                <option value="historical">Historical Site</option>
                            </select>
                        </div>
                        <div class="additional_msg_div" style="visibility: hidden"><label class="additional_msg"></label>
                            <select class="additional_msg_select" name="art_or_war"></select>
                        </div>
                        <div><label>Search Near Address:</label>
                            <input id="autocomplete" placeholder="Address" >
                        </div>
                        <div><label>Site Name:&nbsp</label><input placeholder="Optional" name="title"></div>
                        <div>
                            <label><input type="radio" name="coords" value="t">&nbspSearch Near Clicked Coordinates</label>
                        </div>
                        <div><label>Latitude&nbsp</label><input placeholder="Optional" name="lat2" id="lat"></div>
                        <div><label>Longitude&nbsp</label><input placeholder="Optional" name="lon2" id="lon"></div>
                        <button type="submit" class="btn btn-default">
                           Submit the query <span class="glyphicon glyphicon-search"></span>
                        </button>
                    </form>
                </div>
            </div>
        </div>

        <div id="map-canvas" class="col-xs-9"></div>

    </div>
</div>

<script src="js/loadform.js"></script>
<script src="js/loadmap.js"></script>
</body> </html>
