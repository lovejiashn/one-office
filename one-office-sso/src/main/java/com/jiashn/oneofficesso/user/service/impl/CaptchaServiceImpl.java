package com.jiashn.oneofficesso.user.service.impl;

import com.google.code.kaptcha.Producer;
import com.jiashn.oneofficesso.user.service.CaptchaService;
import com.jiashn.oneofficesso.utils.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

/**
 * @author jiangjs
 * @date 2022-05-23 20:44
 */
@Service
public class CaptchaServiceImpl implements CaptchaService {
    @Autowired
    private Producer producer;

    @Override
    public void getCaptchaCode(HttpServletResponse response, HttpSession session) {
        response.setDateHeader("Expires", 0);

        // Set standard HTTP/1.1 no-cache headers.
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        // Set standard HTTP/1.0 no-cache header.
        response.setHeader("Pragma", "no-cache");
        // 返回图片
        response.setContentType("image/jpeg");
        //获取验证码信息
        String capText = producer.createText();
        //将验证码信息放入session中
        session.setAttribute("captcha",capText);
        //创建图片流
        BufferedImage image = producer.createImage(capText);
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            ImageIO.write(image,"jpg",out);
            image.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (Objects.nonNull(out)){
                    out.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
