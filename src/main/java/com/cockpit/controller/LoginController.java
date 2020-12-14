package com.cockpit.controller;


import com.cockpit.commons.config.VerificationCode;
import com.cockpit.commons.model.RestResult;
import com.cockpit.model.UserModel;
import com.cockpit.service.impl.UserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
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
@Api(value = "用户登录", tags = {"LoginController"},description ="用户登录")
public class LoginController {

    @Autowired
    private UserServiceImpl userService;
    @ApiOperation(value = "用户登录接口", notes = "用户登录接口")
    @PostMapping ("/doLogin")
    public RestResult login(@RequestBody UserVo userVo ) {
        RestResult restResult = new RestResult();
//        String verify_code = (String) request.getSession().getAttribute("verify_code");
//
//        if (!verify_code.equals(userVo.getCode())){
//            restResult.setMeta(HttpStatus.OK.value(),"验证码错误");
//        }

        UserModel user = new UserModel();
        user.setUsername(userVo.getUsername());
        user.setPassword(userVo.getPassword());
        UserModel resultUser =  userService.doLogin(user);
        if (resultUser==null){
            //登录失败返回
            restResult.setMeta(HttpStatus.BAD_REQUEST.value(),"用户名密码错误，登录失败！");
        }
        resultUser.setPassword(null);
        restResult.setCode(HttpStatus.OK.value());
        restResult.setMessage("登录成功！");
        restResult.setInfo(resultUser);
        return  restResult;

    }

    @ApiModel
   static class UserVo implements Serializable{
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
