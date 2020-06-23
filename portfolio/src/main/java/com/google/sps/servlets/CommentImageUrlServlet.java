package com.google.sps.servlets;

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

import com.google.gson.Gson;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet to get blobstore url for comment image upload **/
@WebServlet("/commentsImageUrl")
public class CommentImageUrlServlet extends HttpServlet {

    BlobstoreService blobstore;

    @Override
    public void init() {
        this.blobstore = BlobstoreServiceFactory.getBlobstoreService();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uploadUrl = this.blobstore.createUploadUrl("/comments");

        response.setContentType("application/json;");
        Gson gson = new Gson();
        response.getWriter().println(gson.toJson(uploadUrl));
    }
}