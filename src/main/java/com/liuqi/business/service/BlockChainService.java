package com.liuqi.business.service;

import com.alibaba.fastjson.JSONObject;
import com.liuqi.business.dto.ContractTriggerDto;
import com.liuqi.business.dto.QueryBalanceDto;
import com.liuqi.business.dto.SendTransactionResultDto;
import com.liuqi.response.ReturnResponse;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public interface BlockChainService {

    String getTriggerSmartContractData(Long chainId, BigInteger gasLimit, BigInteger gasPrice, String privateKey,
                                       String contractAddress, String method, String params, String tHash);

    String getTriggerConstantContractData(String address, String contractAddress, String method, String params, String dataHash);

    ContractTriggerDto parseInput(String input);

    ReturnResponse refreshNonce(String chainName);


    /** 获取地址及私钥 **/
    String[] getNewAddress(List<String> mnemonicCode, Integer index);

    /** 执行交易 **/
    SendTransactionResultDto doTransaction(JSONObject jsonObject);

    /** 交易失败处理 **/
    boolean processTxFail(String errorMsg, String params);

    /** 处理总出币地址和空投地址的NONCE **/
    void processAddressNonce(String airdropAddress, String extractAddress);

    /** 交易前NONCE处理【FIL和ETH需要添加nonce】 **/
    void processBeforeTx(JSONObject jsonObject);

    /** 交易后NONCE处理【FIL和ETH需要递增nonce】 **/
    void processAfterTx(SendTransactionResultDto s, JSONObject params);

    /** 获取余额 **/
    QueryBalanceDto getBalance(JSONObject jsonObject);

    /** 获取地址及私钥 **/
    String getAddressFromPrivateKey(String privateKey);

    /** 处理私钥 **/
    String processPrivateKey(String privateKey);
}
