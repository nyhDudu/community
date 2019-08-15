package nyh.example.community.controller;

import nyh.example.community.dto.AccessTokenDto;
import nyh.example.community.dto.GithubUser;
import nyh.example.community.mapper.UserMapper;
import nyh.example.community.model.User;
import nyh.example.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
//认证的Controller
public class AuthorizeController {
    //自动将写好的实例AccessTokenDTO加载到Spring(githubProvider)的上下文
    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect_uri}")
    private String redirectUri;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/callback")
//   @RequestParam参数的接收
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           //HttpServletRequest request,
                           HttpServletResponse response){
        AccessTokenDto accessTokenDto = new AccessTokenDto();
        accessTokenDto.setClient_id(clientId);
        accessTokenDto.setClient_secret(clientSecret);
        accessTokenDto.setCode(code);
        accessTokenDto.setRedirect_uri(redirectUri);
        accessTokenDto.setState(state);
//        拿到accesstoken
        String accessToken = githubProvider.getAccessToken(accessTokenDto);
//        传入accesstoken后可以拿到user
        GithubUser githubUser = githubProvider.getUser(accessToken);
        if (githubUser !=null){
            User user = new User();
            String token=UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);//存储在数据库相当于写进session
            //登录成功，写cookie和session 记住当前的登录态
            //把token放到cookie中
            response.addCookie(new Cookie("token",token));

            //把user对象放到session里面（手动）
//            request.getSession().setAttribute("user",user);
            return "redirect:/";

        }else{
            //登录失败，重新登录
            return "redirect:/";
        }


    }
}

