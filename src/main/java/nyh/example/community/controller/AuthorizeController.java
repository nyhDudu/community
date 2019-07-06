package nyh.example.community.controller;

import nyh.example.community.dto.AccessTokenDto;
import nyh.example.community.dto.GithubUser;
import nyh.example.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
                           @RequestParam(name = "state") String state) {
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
        System.out.println(user.getName());


        return "index";

    }
}

