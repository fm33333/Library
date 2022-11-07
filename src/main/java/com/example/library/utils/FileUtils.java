package com.example.library.utils;

import com.example.library.domain.ApiCode;
import com.example.library.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Base64Utils;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 文件工具类
 *
 * @author 冯名豪
 * @date 2022-09-28
 * @since 1.0
 */
@Slf4j
public final class FileUtils {

    private FileUtils() {

    }

    /**
     * 写入并保存文件，返回文件路径
     *
     * @param fileBytes 文件字节流
     * @param parentDir 父目录路径
     * @param fileExt 文件后缀
     * @return 文件路径
     */
    public static String saveFile(byte[] fileBytes, String parentDir, String fileExt) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String dateDir = DateUtils.dateToString(LocalDateTime.now(), DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        StringBuilder sb = new StringBuilder(parentDir);
        sb.append("/").append(dateDir);
        sb.append("/").append(uuid).append(".").append(fileExt);
        try {
            File file = new File(sb.toString());
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            FileCopyUtils.copy(fileBytes, file);
            return file.getPath();
        } catch (IOException e) {
            log.error(e.toString(), e);
            throw new BusinessException(ApiCode.SYSTEM_ERROR);
        }
    }

    /**
     * 根据文件路径获得文件字节流
     *
     * @param filePath 文件路径
     * @return 文件字节流
     */
    public static byte[] getFile(String filePath) {
        try {
            return Files.readAllBytes(Path.of(filePath));
        } catch (Exception e) {
            log.error(e.toString(), e);
            throw new BusinessException(ApiCode.SYSTEM_ERROR);
        }
    }

    public static void main(String[] args) throws IOException {
        String str = "CiA5N2IwMTczNWVhYjI0ODA5NDMyMDkyYjU2MjhlNTA3YRI4TkRkZk56ZzRNVE13TURRMU9EazRPVFkzTWw4eE16RXpNekV6TmpRM1h6RTJOVGsyTnpBeE9UVT0aIDM2ZDNlNjYyNzcxZDU0YWU4ZGZkNmNhYWM3ZDU3MzFi";
        byte[] decode = Base64Utils.decode(str.getBytes());
        Files.write(new File("F:\\会话存档项目\\test.jpg").toPath(), decode);


    }

}
