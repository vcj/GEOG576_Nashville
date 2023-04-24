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

        // 1. create emergency contact
        int contact_id = 0;
        String contact_fN = request.getParameter("contact_fN");
        String contact_lN = request.getParameter("contact_lN");
        String contact_tel = request.getParameter("contact_tel");
        String contact_email = request.getParameter("contact_email");
        if (contact_fN != null) {contact_fN = "'" + contact_fN + "'";}
        if (contact_lN != null) {contact_lN = "'" + contact_lN + "'";}
        if (contact_tel != null) {contact_tel = "'" + contact_tel + "'";}
        if (contact_email != null) {contact_email = "'" + contact_email + "'";}
        if (contact_fN != null && contact_lN != null) {
            // create the contact
            sql = "insert into person (first_name, last_name, telephone, email) " +
                    "values (" + contact_fN + "," + contact_lN + "," + contact_tel + ","
                    + contact_email + ")";
            dbutil.modifyDB(sql);

            // record the contact id
            ResultSet res_1 = dbutil.queryDB("select last_value from person_id_seq");
            res_1.next();
            contact_id = res_1.getInt(1);

            System.out.println("Success! Contact created.");
        }

        // 2. create user
        int user_id = 0;
        String fN = request.getParameter("fN");
        String lN = request.getParameter("lN");
        String is_male = request.getParameter("is_male");
        String age = request.getParameter("age");
        String blood_type = request.getParameter("blood_type");
        String tel = request.getParameter("tel");
        String email = request.getParameter("email");
        if (fN != null) {fN = "'" + fN + "'";}
        if (lN != null) {lN = "'" + lN + "'";}
        if (is_male != null) {is_male = "'" + is_male + "'";}
        if (age != null) {age = "'" + age + "'";}
        if (blood_type != null) {blood_type = "'" + blood_type + "'";}
        if (tel != null) {tel = "'" + tel + "'";}
        if (email != null) {email = "'" + email + "'";}

        sql = "insert into person (first_name, last_name, is_male, age, " +
                "blood_type, telephone, email, emergency_contact_id) values (" + fN +
                "," + lN + "," + is_male + "," + age + "," + blood_type + "," + tel +
                "," + email;
        if (contact_id > 0) { // check whether has a contact
            sql += "," + contact_id + ")";
        } else {
            sql += ",null)";
        }
        dbutil.modifyDB(sql);

        // record user_id
        ResultSet res_2 = dbutil.queryDB("select last_value from person_id_seq");
        res_2.next();
        user_id = res_2.getInt(1);

        System.out.println("Success! User created.");

        // 3. create report
        int report_id = 0;
        String report_type = request.getParameter("report_type");
        String disaster_type = request.getParameter("disaster_type");
        String lon = request.getParameter("longitude");
        String lat = request.getParameter("latitude");
        String message = request.getParameter("message");
        String add_msg = request.getParameter("additional_message");
        if (report_type != null) {report_type = "'" + report_type + "'";}
        if (disaster_type != null) {disaster_type = "'" + disaster_type + "'";}
        if (message != null) {message = "'" + message + "'";}
        if (add_msg != null) {add_msg = "'" + add_msg + "'";}

        sql = "insert into report (reporter_id, report_type, disaster_type, geom," +
                " message) values (" + user_id + "," + report_type + "," + disaster_type
                + ", ST_GeomFromText('POINT(" + lon + " " + lat + ")', 4326)" + "," +
                message + ")";
        dbutil.modifyDB(sql);

        // record report_id
        ResultSet res_3 = dbutil.queryDB("select last_value from report_id_seq");
        res_3.next();
        report_id = res_3.getInt(1);

        System.out.println("Success! Report created.");

        // 4. create specific report
        if (report_type.equals("'donation'")) {
            sql = "insert into donation_report (report_id, resource_type) values ('"
                    + report_id + "'," + add_msg + ")";
            System.out.println("Success! Donation report created.");
        } else if (report_type.equals("'request'")) {
            sql = "insert into request_report (report_id, resource_type) values ('"
                    + report_id + "'," + add_msg + ")";
            System.out.println("Success! Request report created.");
        } else if (report_type.equals("'damage'")) {
            sql = "insert into damage_report (report_id, damage_type) values ('"
                    + report_id + "'," + add_msg + ")";
            System.out.println("Success! Damage report created.");
        } else {
            return;
        }
        dbutil.modifyDB(sql);

        // response that the report submission is successful
        JSONObject data = new JSONObject();
        try {
            data.put("status", "success");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        response.getWriter().write(data.toString());

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

        // request report
        if (site_type == null) {
            String sql = "select * from pois where id is not null";
            queryReportHelper(sql,list,"art",disaster_type,art_or_war, latitude, longitude);
        }
        // request report
        if (site_type == null || site_type.equalsIgnoreCase("art")) {
            String sql = "select * from pois where site_type = 'art_in_public_places'";
            queryReportHelper(sql,list,"art",disaster_type,art_or_war,latitude, longitude);
        }

        // donation report
        if (site_type == null || site_type.equalsIgnoreCase("historical")) {
            String sql = "select * from pois where site_type = 'historical_marker'";
            queryReportHelper(sql,list,"historical",disaster_type,art_or_war, latitude, longitude);
        }

        response.getWriter().write(list.toString());
    }

    private void queryReportHelper(String sql, JSONArray list, String site_type,
                                   String disaster_type, String art_or_war, String latitude, String longitude) throws SQLException {
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
        }
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
            list.put(m);
        }
    }

    public void main() throws JSONException {
    }
}