package com.liuqi.base;

import cn.hutool.core.io.FileUtil;
import cn.hutool.db.SqlRunner;
import cn.hutool.db.ds.simple.SimpleDataSource;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.liuqi.business.constant.KeyConstant;
import com.liuqi.business.constant.LockConstant;
import com.liuqi.business.enums.ProtocolEnum;
import com.liuqi.business.model.FwtAirModelDto;
import com.liuqi.business.service.FwtAirService;
import com.liuqi.fastdfs.FastDFSClientUtils;
import com.liuqi.fil.FilFactory;
import com.liuqi.redis.lock.RedissonLockUtil;
import com.liuqi.tron.AddressHelper;
import org.apache.commons.lang3.StringUtils;
import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;
import org.bitcoinj.crypto.MnemonicCode;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.utils.Numeric;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author tanyan
 * @create 2020-01=10
 * @description
 */
public class PicTest extends BaseTest {

    @Autowired
    private FastDFSClientUtils fastDFSClientUtils;

    public static SqlRunner getRunner() {
        DataSource ds = new SimpleDataSource("jdbc:mysql://127.0.0.1:3306/cost-effective", "root", "root");
        return SqlRunner.create(ds);
    }

    private static String base = "http://www.zhssc.one/search/download?path=";
    private static Map<String, String> imgMap = new HashMap<>();
    private static Map<String, PicDto> picMaps = new HashMap<>();
    private static List<String> errorList = new ArrayList<>();

    @Autowired
    private FilFactory filFactory;
    @Autowired
    private FwtAirService fwtAirService;

    private String addZero(String dt, int length) {
        StringBuilder builder = new StringBuilder();
        final int count = length;
        int zeroAmount = count - dt.length();
        for (int i = 0; i < zeroAmount; i++) {
            builder.append("0");
        }
        builder.append(dt);
        return builder.toString();
    }

    public String coinToParam(BigDecimal real, Integer decimal) {
        return real.multiply(BigDecimal.TEN.pow(decimal)).toBigInteger().toString(16);
    }

    private String generateGetGasData() {
        JSONObject postJson = new JSONObject();
        JSONArray array = new JSONArray();
        postJson.put("jsonrpc", "2.0");
        postJson.put("method", "eth_gasPrice");
        postJson.put("id", 1);
        postJson.put("params", array);
        String postData = postJson.toString();
        return postData;
    }

    @Test
    public void test05888() throws Exception {
        Integer decimal = 8;
        Integer count = 300;
        List<FwtAirModelDto> l = fwtAirService.queryListByDto(null, false);
        l = l.subList(0, count);
        StringBuilder s = new StringBuilder();
        s.append("0000000000000000000000000000000000000000000000000000000000000020");
        s.append(addZero(Long.toHexString(l.size() * 2), 64));
        for (FwtAirModelDto m : l) {
            String ss = m.getAddress().replaceAll("\n", "");
            ss = ss.replaceAll(" ", "");
            s.append(addZero(AddressHelper.toHexString(ss).substring(2), 64));
            s.append(addZero(coinToParam(m.getQty(), decimal), 64));
        }

        String privateKey = "f6ae1ff8dc8b1415b0b2a260b9e029c185046811e972f9f4aa6091191893142a";
        String fromAddress = AddressHelper.privateKeyToBase58Address(privateKey);// transfer(address,uint256)
        try {
            JSONObject params2 = new JSONObject();
            params2.put("owner_address", AddressHelper.toHexString(fromAddress));
            params2.put("contract_address", AddressHelper.toHexString("TE4QwXfzNbe6Jo6CtsedyttU5ZYsAS6UkC"));
            params2.put("fee_limit", 2000 * 1000000);
            params2.put("function_selector", "syncBatch(address[])");
            params2.put("parameter", s.toString());
            String createTransaction = HttpUtil.createPost("https://api.shasta.trongrid.io/wallet/triggersmartcontract").
                    body(params2.toJSONString()).timeout(3000).execute().body();
            JSONObject jsonObject2 = JSONObject.parseObject(createTransaction);

            JSONObject params3 = new JSONObject();
            JSONObject transaction = jsonObject2.getJSONObject("transaction");
            String hash = transaction.getString("txID");
            params3.put("transaction", transaction);
            params3.put("privateKey", privateKey);
            String signTransaction = HttpUtil.createPost("https://api.shasta.trongrid.io/wallet/gettransactionsign").
                    body(params3.toJSONString()).timeout(3000).execute().body();
            JSONObject jsonObject3 = JSONObject.parseObject(signTransaction);
            jsonObject3.remove("visible");

            String broadcasttransaction = HttpUtil.createPost("https://api.shasta.trongrid.io/wallet/broadcasttransaction").
                    body(jsonObject3.toJSONString()).timeout(3000).execute().body();
            System.out.println("TRC20广播合约交易，" + broadcasttransaction);
        } catch (Exception e) {
        }
    }

    @Test
    public void test05() throws Exception {
        Long nonce = 61L;
        List<FwtAirModelDto> l = fwtAirService.queryListByDto(null, false);
        l = l.subList(0, 13);
        StringBuilder s = new StringBuilder();
        s.append("0000000000000000000000000000000000000000000000000000000000000020");
        s.append(addZero(Long.toHexString(l.size()), 64));
        for (FwtAirModelDto m : l) {
            String ss = m.getAddress().replaceAll("\n", "");
            ss = ss.replaceAll(" ", "");
            s.append(addZero(AddressHelper.toHexString(ss).substring(2), 64));
            s.append(addZero(coinToParam(m.getQty(), 6), 64));
        }
//        if (!l.isEmpty()) {
//            return;
//        }

        String privateKey = "0x0917f87728873b8f1968430603e97a69097646447d70b929a4d52197d2220fd2";
        try {
            String url = "https://bsc-dataseed.binance.org";
            String r = HttpUtil.createPost(url).body(generateGetGasData()).timeout(3000).execute().body();
            String gasPriceStr = JSONObject.parseObject(r).getString("result");

            BigDecimal gasPrice = new BigDecimal(Numeric.decodeQuantity(gasPriceStr));

            BigDecimal maxGas = BigDecimal.valueOf(0.1).multiply(BigDecimal.TEN.pow(18));
            long gasLimit = maxGas.divide(gasPrice, 1, BigDecimal.ROUND_DOWN).longValue();

            System.out.println("GasPrice = {}, GAS Limit = {}" + gasPrice + "-" + gasLimit);
//            if (gasLimit > 50000000) gasLimit = 50000000;



            String rr = HttpUtil.createPost(url).body(processRequest(privateKey,
                    "0xcc321df17466a8fcda86be2ec015f3e25bdc4d48", nonce, gasPriceStr,
                    gasLimit, "getStatus(address[])",
                    s.toString())).timeout(3000).execute().body();
            System.out.println("交易结果 = {}" + rr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getInput(String method, String params) {
        return "0x" + getMethodByte(method) + params;
    }

    private String processRequest(String privateKey, String contractAddress, Long nonce,
                                  String gasPrice, Long gasLimit, String method, String params) {
        Credentials credentials = Credentials.create(privateKey);
        RawTransaction rawTransaction = RawTransaction.createTransaction(
                BigInteger.valueOf(nonce),
                Numeric.decodeQuantity(gasPrice), BigInteger.valueOf(gasLimit),
                contractAddress, getInput(method, params));
        byte[] transactionSign = TransactionEncoder.signMessage(rawTransaction, 56L, credentials);
        String transactionData = Numeric.toHexString(transactionSign);

        JSONObject postJson = new JSONObject();
        JSONArray array = new JSONArray();
        array.add(transactionData);
        postJson.put("jsonrpc", "2.0");
        postJson.put("method", "eth_sendRawTransaction");
        postJson.put("id", 1);
        postJson.put("params", array);
        String postData = postJson.toString();
        return postData;
    }

    @Test
    public void test03() {
        List<FwtAirModelDto> l = fwtAirService.queryListByDto(null, false);

        System.out.println(l.size());

        String privateKey = "f6ae1ff8dc8b1415b0b2a260b9e029c185046811e972f9f4aa6091191893142a";
        String fromAddress = AddressHelper.privateKeyToBase58Address(privateKey);
        String contractAddress = "TAcfhsJ26fU5u2rR9FVdnJAQVmfMV9xLhw";

        for (FwtAirModelDto f : l) {
            String address = f.getAddress();

            try {
                JSONObject params2 = new JSONObject();
                params2.put("owner_address", AddressHelper.toHexString(fromAddress));
                params2.put("contract_address", AddressHelper.toHexString(contractAddress));
                params2.put("fee_limit", 20 * 1000000);
                params2.put("function_selector", "transfer(address,uint256)");
                String amountParam1 = addZero(AddressHelper.toHexString(address).substring(2), 64);
                String amountParam2 = addZero(coinToParam(BigDecimal.valueOf(8.888), 16), 64);
                params2.put("parameter", amountParam1 + amountParam2);
                String createTransaction = HttpUtil.createPost("https://tron-mainnet.token.im/wallet/triggersmartcontract").
                        body(params2.toJSONString()).execute().body();
                System.out.println("TRC20创建合约交易，{}" + createTransaction);
                JSONObject jsonObject2 = JSONObject.parseObject(createTransaction);


                JSONObject params3 = new JSONObject();
                JSONObject transaction = jsonObject2.getJSONObject("transaction");
                String hash = transaction.getString("txID");
                params3.put("transaction", transaction);
                params3.put("privateKey", privateKey);
                String signTransaction = HttpUtil.createPost("https://tron-mainnet.token.im/wallet/gettransactionsign").
                        body(params3.toJSONString()).execute().body();
                System.out.println("TRC20签名合约交易，{}" + signTransaction);
                JSONObject jsonObject3 = JSONObject.parseObject(signTransaction);
                jsonObject3.remove("visible");

                String broadcasttransaction = HttpUtil.createPost("https://tron-mainnet.token.im/wallet/broadcasttransaction").
                        body(jsonObject3.toJSONString()).execute().body();
                System.out.println("TRC20广播合约交易，{}" + broadcasttransaction);

                Thread.sleep(300);
            } catch (Exception e) {
            }

        }
    }

    public static String getMethodByte(String method) {
        byte[] s = Hash.sha3(method.getBytes(StandardCharsets.UTF_8));
        String s1 = process(Integer.toHexString(s[0] & 0xFF));
        String s2 = process(Integer.toHexString(s[1] & 0xFF));
        String s3 = process(Integer.toHexString(s[2] & 0xFF));
        String s4 = process(Integer.toHexString(s[3] & 0xFF));
        return s1 + s2 + s3 + s4;
    }

    private static String process(String s) {
        if (s.length() < 2) return "0" + s;
        return s;
    }

    @Test
    public void teso02() {
    }

    @Test
    public void teso01() {
        SqlRunner runner = getRunner();
        List<PicDto> list = query(runner);
        int count = list.size();
        int cur = 0;
        File file = new File("E:/pic.txt");
        if (file.exists()) {
            String fileContent = FileUtil.readUtf8String(file);
            if (StringUtils.isNotEmpty(fileContent)) {
                imgMap = JSONObject.parseObject(fileContent, Map.class);
            }
        }

        File file2 = new File("E:/content.txt");

        Long id = 0L;
        for (PicDto pic : list) {
            try {
                cur++;
                System.out.println("------->" + cur + "/" + count);
                transferContent(pic);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        FileUtil.writeString(JSONObject.toJSONString(imgMap), file, "UTF-8");
        FileUtil.writeString(JSONObject.toJSONString(picMaps), file2, "UTF-8");
        FileUtil.writeString(JSONObject.toJSONString(errorList), new File("E:/error.txt"), "UTF-8");
    }


    private void transferContent(PicDto picDto) {
        //获取所有需要修改的图片连接
        String content = picDto.getPicDetail();
        Long id = picDto.getId();
        List<String> list = new ArrayList<>();
        String img = "";
        Pattern p_image;
        Matcher m_image;
        //     String regEx_img = "<img.*src=(.*?)[^>]*?>"; //图片链接地址
        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
        m_image = p_image.matcher(content);
        while (m_image.find()) {
            // 得到<img />数据
            img = m_image.group();
            // 匹配<img>中的src数据
            Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
            while (m.find()) {
                String pic = m.group(1);
                list.add(pic);
                transferPic(id, pic);
            }
        }
        for (String str : list) {
            content = content.replace(str, imgMap.get(str));
        }
        picDto.setPicDetail(content);

        picDto.setPicMain(transferPic(id, base + picDto.getPicMain()));
        if (StringUtils.isNotEmpty(picDto.getPic2())) {
            picDto.setPic2(transferPic(id, base + picDto.getPic2()));
        }
        if (StringUtils.isNotEmpty(picDto.getPic3())) {
            picDto.setPic3(transferPic(id, base + picDto.getPic3()));
        }
        if (StringUtils.isNotEmpty(picDto.getPic4())) {
            picDto.setPic4(transferPic(id, base + picDto.getPic4()));
        }
        if (StringUtils.isNotEmpty(picDto.getPic5())) {
            picDto.setPic5(transferPic(id, base + picDto.getPic5()));
        }
        picMaps.put(picDto.getId() + "", picDto);
    }

    private String transferPic(Long id, String oldPic) {
        String pic = imgMap.get(oldPic);
        if (StringUtils.isNotEmpty(oldPic) && !imgMap.containsKey(oldPic)) {
            try {
                File file = download(oldPic, oldPic.substring(oldPic.lastIndexOf("/") + 1), "E://pic/" + id + "/");
                pic = fastDFSClientUtils.uploadFile(file);
                imgMap.put(oldPic, pic);
            } catch (Exception e) {
                e.printStackTrace();
                errorList.add(oldPic);
            }
        }
        return pic;
    }

    public static File download(String urlString, String filename, String savePath) throws Exception {
        System.out.println("---->" + urlString);
        // 构造URL
        URL url = new URL(urlString);
        URLConnection con = url.openConnection();
        con.setConnectTimeout(5 * 1000);
        InputStream is = con.getInputStream();
        // 1K的数据缓冲
        byte[] bs = new byte[1024];
        int len;
        // 输出的文件流
        File sf = new File(savePath);
        if (!sf.exists()) {
            sf.mkdirs();
        }
        File file = new File(sf.getPath() + "\\" + filename);
        OutputStream os = new FileOutputStream(file);
        // 开始读取
        while ((len = is.read(bs)) != -1) {
            os.write(bs, 0, len);
        }
        // 完毕，关闭所有链接
        os.close();
        is.close();
        return file;
    }


    private List<PicDto> query(SqlRunner tec) {
        List<PicDto> list = null;
        try {
            list = tec.query("select id,pic_main as picMain,pic2,pic3,pic4,pic5,pic_detail as picDetail  from t_product where id>=30", PicDto.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static void update(SqlRunner tec, PicDto pic) {
        try {
            int success = tec.execute("update t_product set pic_main=?,pic2=?,pic3=?,pic4=?,pic5=?,pic_detail=? where id=?",
                    pic.getPicMain(), pic.getPic2(), pic.getPic3(), pic.getPic4(), pic.getPic5(), pic.getPicDetail(), pic.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
