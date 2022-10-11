package edu.controller;

import edu.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {
    @Value("${reggie.path}")
    private String basePath;
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        /*参数file是和html页面中文件上传的组件名称相同的，不能乱编写*/
        /*new File对象只是存在内存中，通过createNewFile方法才能将文件创建到磁盘上*/
        /*使用transferTo转存的原因是图片一开始只存在于内存的临时文件中，必须存到服务器磁盘才行*/
        String originalFilename = file.getOriginalFilename();
        String appendName = originalFilename.substring(originalFilename.lastIndexOf('.'));
        String fileName = UUID.randomUUID() + appendName;
        File parentFile = new File(basePath);
        if(!parentFile.exists()){
            parentFile.mkdir();
        }

        try {
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return R.success(fileName);
    }
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));
            int len = 0;
            byte[] buff = new byte[1024];
            ServletOutputStream outputStream = response.getOutputStream();
            while((len = fileInputStream.read(buff)) != -1){
                outputStream.write(buff,0,len);
                outputStream.flush();
            }
            fileInputStream.close();
            outputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
