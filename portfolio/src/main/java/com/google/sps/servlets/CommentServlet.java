// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

import com.google.sps.data.Comment;
import com.google.gson.Gson;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet to handle comments data */
@WebServlet("/comments")
public class CommentServlet extends HttpServlet {

    DatastoreService datastore;
    
    @Override
    public void init() {
        this.datastore = DatastoreServiceFactory.getDatastoreService();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);

        PreparedQuery results = this.datastore.prepare(query);

        List<Comment> comments = new ArrayList<>();
        for (Entity entity : results.asIterable()) {
            long id = entity.getKey().getId();
            String name = (String) entity.getProperty("name");
            Date timestamp = (Date) entity.getProperty("timestamp");
            String text = (String) entity.getProperty("text");
            String imageUrl = (String) entity.getProperty("imageUrl");

            Comment newComment = new Comment(id, name, timestamp, text, imageUrl);
            comments.add(newComment);
        }

        response.setContentType("application/json;");
        Gson gson = new Gson();
        response.getWriter().println(gson.toJson(comments));
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        String text = request.getParameter("text");
        String imageUrl = (String) request.getAttribute("imageUrl");

        Entity commentEntity = new Entity("Comment");
        commentEntity.setProperty("name", name);
        commentEntity.setProperty("timestamp", new Date());
        commentEntity.setProperty("text", text);
        commentEntity.setProperty("imageUrl", imageUrl);
        this.datastore.put(commentEntity);
        response.sendRedirect("/");
    }
}
