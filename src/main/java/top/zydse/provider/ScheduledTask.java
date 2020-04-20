package top.zydse.provider;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.zydse.cache.HotTagCache;
import top.zydse.dto.HotTagDTO;
import top.zydse.mapper.CommonExtensionMapper;
import top.zydse.mapper.QuestionMapper;
import top.zydse.model.QuestionExample;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * CreateBy: zydse
 * ClassName: HotTagScheduled
 * Description:
 *
 * @Date: 2020/4/6
 */
@Component
@Slf4j
public class ScheduledTask {
    @Autowired
    private HotTagCache hotTagCache;
    @Autowired
    private CommonExtensionMapper extensionMapper;
    @Autowired
    private QuestionMapper questionMapper;

    @Scheduled(fixedRate = 1000 * 30)
    public void updateHotTag() {
        List<HotTagDTO> hotTagDTOList = extensionMapper.getTagPriority();
        hotTagCache.setHots(hotTagDTOList);
    }

    @Scheduled(fixedRate = 1000 * 30)
    public void updateLatestQuestion() {
        QuestionExample example = new QuestionExample();
        example.setOrderByClause("gmt_modified");
        questionMapper.selectByExample(example);
//        hotTagCache.setHots(hotTagDTOList);
//        log.info("now you see me at {}",new Date());
//        log.info("update hotTagDTO list is : {}", hotTagDTOList);
    }

    //@Scheduled(fixedRate = 1000 * 60 * 30)
    public void updateBg() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectionSpecs(Arrays.asList(ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS))
                .build();
        String bing = "https://cn.bing.com/";
        String url = "HPImageArchive.aspx?"
                + "format=js&idx=0&n=1&nc=" + System.currentTimeMillis() + "&pid=hp";
        Request request = new Request.Builder()
                .url(bing + url)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String result = response.body().string();
            result = result.substring(result.indexOf("url\":") + 7);
            result = result.substring(0, result.indexOf("\""));
            log.info("result last : {}", result);
            Request download = new Request.Builder()
                    .url(bing + result)
                    .build();
            client.newCall(download).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    log.info("download failed");
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    InputStream is = null;
                    byte[] buf = new byte[2048];
                    int len = 0;
                    FileOutputStream fos = null;
                    // 储存下载文件的目录
                    String path = "/posts/";
                    File directory = new File(path);
                    if(!directory.exists()){
                        directory.mkdir();
                    }
                    try {
                        is = response.body().byteStream();
                        long total = response.body().contentLength();
                        File file = new File(directory, "bing.jpg");
                        fos = new FileOutputStream(file);
                        long sum = 0;
                        while ((len = is.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                            sum += len;
                            int progress = (int) (sum * 1.0f / total * 100);
                            log.info("download progress : " + progress);
                        }
                        fos.flush();
                        log.info("download success");
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.info("download failed : "+e.getMessage());
                    } finally {
                        try {
                            if (is != null)
                                is.close();
                        } catch (IOException e) {
                        }
                        try {
                            if (fos != null)
                                fos.close();
                        } catch (IOException e) {
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
