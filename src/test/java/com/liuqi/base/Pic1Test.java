package com.liuqi.base;

import cn.hutool.core.io.FileUtil;
import cn.hutool.db.SqlRunner;
import cn.hutool.db.ds.simple.SimpleDataSource;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.liuqi.business.enums.ExchangeEnum;
import com.liuqi.business.model.CurrencyConfigModelDto;
import com.liuqi.business.service.CurrencyConfigService;
import jnr.ffi.Struct;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.io.File;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tanyan
 * @create 2020-01=10
 * @description
 */
public class Pic1Test extends BaseTest {

    @Autowired
    private CurrencyConfigService currencyConfigService;

    public static SqlRunner getRunner() {
        DataSource ds = new SimpleDataSource("jdbc:mysql://127.0.0.1:3306/autotrade", "root", "123456");
        return SqlRunner.create(ds);
    }

    public void s() {

    }

    private static Map<String, JSONObject> picMaps = new HashMap<>();

    public static void main(String[] args) {
        SqlRunner runner = getRunner();
        int cur = 0;
        File file2 = new File("E:/content.txt");
        if (file2.exists()) {
            String fileContent = FileUtil.readUtf8String(file2);
            if (StringUtils.isNotEmpty(fileContent)) {
                picMaps = JSONObject.parseObject(fileContent, Map.class);
            }
        }
        int count=picMaps.size();
        for (Map.Entry<String,JSONObject> pic : picMaps.entrySet()) {
            try {
                cur++;
                update(cur,runner,JSONObject.parseObject(pic.getValue().toString(),PicDto.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private static void update(int cur,SqlRunner tec, PicDto pic) {
        try {
            int success = tec.execute("update t_product set pic_main=?,pic2=?,pic3=?,pic4=?,pic5=?,pic_detail=? where id=?",
                    pic.getPicMain(), pic.getPic2(), pic.getPic3(), pic.getPic4(), pic.getPic5(), pic.getPicDetail(), pic.getId());
            System.out.println("------->" + cur + "/" + picMaps.size()+"--->"+success);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
