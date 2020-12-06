package com.cockpit.controller;


import com.cockpit.commons.config.VerificationCode;
import com.cockpit.commons.model.RestResult;
import com.cockpit.model.UserModel;
import com.cockpit.service.impl.UserServiceImpl;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;

@RestController
public class LoginController {

    @Autowired
    private UserServiceImpl userService;

    @PostMapping ("/doLogin")
    public RestResult login( ) {
        RestResult restResult = new RestResult();
//        String verify_code = (String) request.getSession().getAttribute("verify_code");
//
//        if (!verify_code.equals(userVo.getCode())){
//            restResult.setMeta(HttpStatus.OK.value(),"验证码错误");
//        }
//        UserModel userModel = new UserModel();
//        userModel.setUsername(userVo.getUsername());
//        userModel.setPassword(userVo.getPassword());
//        boolean flag =   userService.doLogin(userModel);
//        if (!flag){
//            restResult.setMeta(HttpStatus.OK.value(),"用户名密码错误，登录失败！");
//        }
        UserModel user = new UserModel();
        user.setUsername("张三");
        user.setId(1);
        user.setTelephone("18568887789");
        restResult.setCode(HttpStatus.OK.value());
        restResult.setMessage("登录成功！");
        restResult.setInfo(user);
        return  restResult;

    }

    @ApiModel
    class UserVo implements Serializable{
        private static final long serialVersionUID = 4790924204646750015L;
        @ApiModelProperty(value = "用户名")
        private String username;

        @ApiModelProperty(value = "密码")
        private String password;

        @ApiModelProperty(value = "验证码")
        private String code;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

    // 用户登录验证码
    @GetMapping("/verifyCode")
    public void verifyCode(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        VerificationCode code = new VerificationCode();
        BufferedImage image = code.getImage();
        String text = code.getText();
        HttpSession session = request.getSession(true);
        session.setAttribute("verify_code", text);
        System.out.println(session.getId());
        VerificationCode.output(image,resp.getOutputStream());
    }


    @GetMapping("/menu")
    public RestResult  doMenu(){
        RestResult restResult = new RestResult();
        restResult.setCode(HttpStatus.OK.value());
        restResult.setMessage("登录成功！");
        return  restResult;

    }
}
