package nyh.example.community.controller;

import nyh.example.community.dto.AccessTokenDto;
import nyh.example.community.dto.GithubUser;
import nyh.example.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

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

    @GetMapping("/callback")
//   @RequestParam参数的接收
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request){
        AccessTokenDto accessTokenDto = new AccessTokenDto();
        accessTokenDto.setClient_id(clientId);
        accessTokenDto.setClient_secret(clientSecret);
        accessTokenDto.setCode(code);
        accessTokenDto.setRedirect_uri(redirectUri);
        accessTokenDto.setState(state);
//        拿到accesstoken
        String accessToken = githubProvider.getAccessToken(accessTokenDto);
//        传入accesstoken后可以拿到user
        GithubUser user = githubProvider.getUser(accessToken);
        if (user !=null){
            //登录成功，写cookie和session 记住当前的登录态 
            //把user对象放到session里面
            request.getSession().setAttribute("user",user);
            return "redirect:/";

        }else{
            //登录失败，重新登录
            return "redirect:/";
        }


    }
}

