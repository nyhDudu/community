package nyh.example.community.provider;

import com.alibaba.fastjson.JSON;
import nyh.example.community.dto.AccessTokenDto;
import nyh.example.community.dto.GithubUser;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.RequestBody;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GithubProvider {
    //    接收返回的accesstoken
    public String getAccessToken(AccessTokenDto accessTokenDto){
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDto));

        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string =response.body().string();
            System.out.println("String"+string);
//            access_token=64718ae74c92c65e846defa6ecc45091f2b934b2&scope=user&token_type=bearer
//            取出64718ae74c92c65e846defa6ecc45091f2b934b2
            String[] split = string.split("&");
            String tokenstr=split[0];
            String token = string.split("&")[0].split("=")[1];
            System.out.println(token);
            return token;
        } catch (IOException e) {
            e.printStackTrace();

        }
        return null;


    }

    //    通过accestoken获取用户信息
//    GithubUser是provider的一个返回值
    public GithubUser getUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token="+accessToken)
                .build();
        try{
            Response response = client.newCall(request).execute();
            String string =response.body().string();
            System.out.println(string);
//            把String的一个json对象自动转化解析成java的类对象
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
            System.out.println(githubUser);
            return githubUser;
        } catch(IOException e){

        }
        return null;

    }

}

