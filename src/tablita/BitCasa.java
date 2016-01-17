package tablita;
//
//
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.FileEntity;
//import org.apache.http.impl.client.HttpClientBuilder;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.util.EntityUtils;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Creado por akino on 12-17-15.
// */
public class BitCasa {
//    String URI = "https://www.googleapis.com/upload/drive/v2/files?uploadType=media";
//
//    void init(File file){
//        HttpClient httpClient = HttpClientBuilder.create().build();
//
//        String fileLengh = String.valueOf(file.getTotalSpace());
////        HttpPost post = new HttpPost(java.net.URI.create(URI));
//
//        HttpEntity entity = new FileEntity(file, "application,json");
//
//        HttpPost post = new HttpPost(URI);
//
//
//        List<NameValuePair> urlParameters = new ArrayList<>();
//
//        urlParameters.add(new BasicNameValuePair("Host", "www.googleapis.com"));
////        urlParameters.add(new BasicNameValuePair("ContentType", "application/json"));
//        urlParameters.add(new BasicNameValuePair("Content-Length", fileLengh));
//        urlParameters.add(new BasicNameValuePair("Authorization", "Bearer cPzgRj0S_8fMvmGk0hKW5ahz"));
//
//        try {
//            post.setEntity(entity);
//            HttpResponse response = httpClient.execute(post);
//            String result = EntityUtils.toString(response.getEntity());
//            System.out.println(result);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (ClientProtocolException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//
//    }
}
