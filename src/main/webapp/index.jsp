<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Nashville</title>

    <!-- Custom styles -->
    <link rel="stylesheet" href="css/style.css">

    <!-- jQuery -->
    <script src="//code.jquery.com/jquery-1.11.3.min.js"></script>
    <script src="//code.jquery.com/jquery-migrate-1.2.1.min.js"></script>

    <!-- Bootstrap -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>

    <!-- Google Map JS Libraries -->
    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBF6xl45i9h4u6ytufMmJReQo4k0A7AO5E&libraries=geometry,places">
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
                <li class="active"><a href="#create_report" data-toggle="tab">Find a Site!</a></li>
                <li><a href="#query_report" data-toggle="tab">Leave a Review!</a></li>
            </ul>

            <!-- Tab panes -->
            <div class="tab-content ">
                <!-- Find a Site Tab Panel -->
                <div class="tab-pane active" id="create_report">
                    <form id = "create_report_form">
                        <div><label>Site Name:</label><input placeholder="Site Name" name="fN"></div>
                        <div><label>Site Type:</label>
                            <select name="site_type">
                                <option value="">Choose a site type</option>
                                <option value="SiteType1">Site Type 1</option>
                                <option value="SiteType2">Site Type 2</option>
                                <option value="SiteType3">Site Type 3</option>
                                <option value="SiteType4">Site Type 4</option>
                                <option value="Other">Other</option>
                            </select>
                        </div>
                        <div><label>Nearby Address:</label>
                            <input id="autocomplete" placeholder="Address" >
                        </div>
                        <button type="submit" class="btn btn-default" id="report_submit_btn">
                            <span class="glyphicon glyphicon-star"></span> Submit
                        </button>
                    </form>
                </div>

                <!-- Leave a Review Tab Panel -->
                <div class="tab-pane" id="query_report">
                    <form id = "review_report_form">
                        <div><label>Site Name:</label><input placeholder="Site Name" name="fN"></div>
                        <div><label>Rating:</label>
                            <select name="rating_type">
                                <option value="">Choose a rating</option>
                                <option value="onestar">One star</option>
                                <option value="twostars">Two stars</option>
                                <option value="threestars">Three stars</option>
                                <option value="fourstars">Four stars</option>
                                <option value="fivestars">Five stars!</option>
                            </select>
<%--                            this will be for the comment - not sure how to do that part! --%>
                        <div class="additional_msg_div" style="visibility: hidden"><label class="additional_msg"></label>
                            <select class="additional_msg_select" name="resource_or_damage"></select>
                        </div>
                        <button type="submit" class="btn btn-default">
                            <span class="glyphicon glyphicon-star"></span> Submit Review
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