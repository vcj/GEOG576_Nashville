package com.example.nashville;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet implementation class HttpServlet
 */
@WebServlet("/HttpServlet")
public class HttpServlet extends jakarta.servlet.http.HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    public HttpServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }


    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse
            response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String tab_id = request.getParameter("tab_id");

        // create a report
        if (tab_id.equals("0")) {
            System.out.println("A report is submitted!");
            try {
                createReport(request, response);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // query reports
        else if (tab_id.equals("1")) {
            try {
                queryReport(request, response);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void createReport(HttpServletRequest request, HttpServletResponse
            response) throws SQLException, IOException {
        DBUtility dbutil = new DBUtility();
        String sql;

        // 1. create report
        String site_name = request.getParameter("sN");
        site_name = site_name.replace("'", "''");
        String first_name = request.getParameter("fN");
        String last_name = request.getParameter("lN");
        String stars = request.getParameter("stars");
        String comment = request.getParameter("message");
        comment = comment.replace("'", "''");
        String age = request.getParameter("age");
        sql = "insert into reviews (fN, lN, site_name, comment, age, stars_text, review_time) " +
                    "values ('" + first_name + "','" + last_name + "','" + site_name + "','"
                    + comment + "'," + age +",'" + stars +"', CURRENT_TIMESTAMP)";
        System.out.println(sql);
        dbutil.modifyDB(sql);

            System.out.println("Success! review created.");
        }

    private void queryReport(HttpServletRequest request, HttpServletResponse
            response) throws JSONException, SQLException, IOException {
        JSONArray list = new JSONArray();

        String disaster_type = request.getParameter("disaster_type");
        String site_type = request.getParameter("site_type");
        // resource_or_damage will be null if report_type is null
        String art_or_war = request.getParameter("art_or_war");
        String latitude = request.getParameter("latitude");
        String longitude  = request.getParameter("longitude");
        String lon2  = request.getParameter("lon2");
        String lat2  = request.getParameter("lat2");
        String title  = request.getParameter("title");
        String coords  = request.getParameter("coords");

        // request report
        if (site_type == null) {
            String sql = "select * from pois where latitude is not null";
            queryReportHelper(sql,list,"art",disaster_type,art_or_war, latitude, longitude, title, lon2, lat2, coords);
        }
        // request report
        if (site_type == null || site_type.equalsIgnoreCase("art")) {
            String sql = "select * from pois where site_type = 'art_in_public_places'";
            queryReportHelper(sql,list,"art",disaster_type,art_or_war,latitude, longitude , title, lon2, lat2, coords);
        }

        // donation report
        if (site_type == null || site_type.equalsIgnoreCase("historical")) {
            String sql = "select * from pois where site_type = 'historical_marker'";
            queryReportHelper(sql,list,"historical",disaster_type,art_or_war, latitude, longitude, title, lon2, lat2, coords);
        }

        response.getWriter().write(list.toString());
    }

    private void queryReportHelper(String sql, JSONArray list, String site_type,
                                   String disaster_type, String art_or_war, String latitude, String longitude,
                                    String title, String lon2, String lat2, String coords) throws SQLException {
        DBUtility dbutil = new DBUtility();
        if (art_or_war != null) {
            if (site_type.equalsIgnoreCase("historical")) {
                if (art_or_war.equalsIgnoreCase("true")) {
                    sql += " and civil_war = 'X'";}
            } else {
                sql += " and art_type = '" + art_or_war + "'";
            }
        }
        if (longitude != null) {
            sql += " and ST_DWithin(st_setsrid(st_makepoint(" + longitude + "," + latitude +"),4326), st_setsrid(st_makepoint("
        + "longitude, latitude),4326), 804.672, true)";
        } else if ( coords != null ) {
            sql += " and ST_DWithin(st_setsrid(st_makepoint(" + lon2 + "," + lat2 +"),4326), st_setsrid(st_makepoint("
                    + "longitude, latitude),4326), 804.672, true)";
        }
        if (title != null) {
            sql += " and lower(title) LIKE '%" + title.toLowerCase() + "%'";
        }
        // add join to reviews
        sql = "SELECT * from ("+ sql+ ") pois2 LEFT join site_reviews on pois2.title = site_reviews.site_name";
        System.out.println(sql);
        ResultSet res = dbutil.queryDB(sql);
        while (res.next()) {
            // add to response
            HashMap<String, String> m = new HashMap<String,String>();
            m.put("id", res.getString("id"));
            m.put("site_type", res.getString("site_type"));
            m.put("title", res.getString("title"));
            m.put("artist", res.getString("artist"));
            m.put("description", res.getString("description"));
            m.put("year", res.getString("year"));
            m.put("longitude", res.getString("longitude"));
            m.put("latitude", res.getString("latitude"));
            m.put("art_type", res.getString("art_type"));
            m.put("location", res.getString("location"));
            m.put("total_reviews", res.getString("total_reviews"));
            m.put("average_review", res.getString("average_review"));
            m.put("most_recent_comment", res.getString("most_recent_comment"));
            list.put(m);
        }
    }

    public void main() throws JSONException {
    }
}