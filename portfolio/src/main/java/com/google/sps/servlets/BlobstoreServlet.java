package com.google.sps.servlets;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;

import com.google.gson.Gson;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;

/** Servlet to handle blobstore */
@WebServlet("/blobstore")
public class BlobstoreServlet extends HttpServlet {

    BlobstoreService blobstore;

    @Override
    public void init() {
        this.blobstore = BlobstoreServiceFactory.getBlobstoreService();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uploadUrl = this.blobstore.createUploadUrl("/blobstore");

        response.setContentType("application/json;");
        Gson gson = new Gson();
        response.getWriter().println(gson.toJson(uploadUrl));
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String action = request.getParameter("action");
        String imageUrl = getUploadedFileUrl(request, "image");

        RequestDispatcher rd = request.getRequestDispatcher(action);
        request.setAttribute("imageUrl", imageUrl);
        try {
            rd.forward(request, response);
        }
        catch(ServletException se) {
            se.printStackTrace();
        }
    }

    /** Returns a URL that points to the uploaded file, or null if the user didn't upload a file. */
    private String getUploadedFileUrl(HttpServletRequest request, String formInputElementName) {
        Map<String, List<BlobKey>> blobs = this.blobstore.getUploads(request);
        List<BlobKey> blobKeys = blobs.get("image");

        // User submitted form without selecting a file, so we can't get a URL. (dev server)
        if (blobKeys == null || blobKeys.isEmpty()) {
            return null;
        }

        // Our form only contains a single file input, so get the first index.
        BlobKey blobKey = blobKeys.get(0);

        // User submitted form without selecting a file, so we can't get a URL. (live server)
        BlobInfo blobInfo = new BlobInfoFactory().loadBlobInfo(blobKey);
        if (blobInfo.getSize() == 0) {
            this.blobstore.delete(blobKey);
            return null;
        }

        // We could check the validity of the file here, e.g. to make sure it's an image file
        // https://stackoverflow.com/q/10779564/873165

        // Use ImagesService to get a URL that points to the uploaded file.
        ImagesService imagesService = ImagesServiceFactory.getImagesService();
        ServingUrlOptions options = ServingUrlOptions.Builder.withBlobKey(blobKey);
        String url = imagesService.getServingUrl(options);

        // GCS's localhost preview is not actually on localhost,
        // so make the URL relative to the current domain.
        if(url.startsWith("http://localhost:8080/")){
            url = url.replace("http://localhost:8080/", "/");
        }
        return url;
    }
}