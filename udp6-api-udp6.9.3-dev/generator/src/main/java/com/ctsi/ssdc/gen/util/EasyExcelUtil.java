package com.ctsi.ssdc.gen.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.ctsi.ssdc.gen.domain.NoModelWriteData;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EasyExcelUtil {

    public void noModelWrite(@RequestBody NoModelWriteData data, HttpServletResponse response) throws IOException {
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码
            String fileName = URLEncoder.encode(data.getFileName(), "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream()).head(head(data.getHeadMap())).sheet(data.getFileName()).doWrite(dataList(data.getDataList(), data.getDataStrMap()));
        } catch (Exception e) {
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = new HashMap<>();
            map.put("status", "failure");
            map.put("message", "下载文件失败" + e.getMessage());
            response.getWriter().println(JSON.toJSONString(map));
        }
    }

    //设置表头
    private List<List<String>> head(List<String> headMap) {
        List<List<String>> list = new ArrayList<List<String>>();

        for (String head : headMap) {
            List<String> headList = new ArrayList<String>();
            headList.add(head);
            list.add(headList);
        }
        return list;
    }

    //设置导出的数据内容
    private List<List<String>> dataList(List<Map<String, Object>> dataList, List<String> dataStrMap) {
        List<List<String>> list = new ArrayList<>();
        for (Map<String, Object> map : dataList) {
            List<String> data = new ArrayList<>();
            for (int i = 0; i < dataStrMap.size(); i++) {
                data.add(map.get(dataStrMap.get(i)).toString());
            }
            list.add(data);
        }
        return list;
    }
}
