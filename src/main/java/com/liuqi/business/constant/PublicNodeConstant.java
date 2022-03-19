package com.liuqi.business.constant;

/**
 * @program: vmc
 * @description: 外部接口地址
 * @author: chenxi
 * @create: 2019-12-25 14:16
 **/
public class PublicNodeConstant {

    private static final String IMTOKEN_LTC = "https://ltc-mainnet.token.im";// IMTOKEN -LTC节点

    private static final String IMTOKEN_BTC = "https://btc-mainnet.token.im";// IMTOKEN -BTC节点



    private static final String IMTOKEN_ETH = "https://eth-mainnet.token.im";// IMTOKEN -ETH节点

    private static final String TOKEN_POCKET_ETH1 = "https://web3.mytokenpocket.vip";// TOKENPOCKET -ETH节点1

    private static final String TOKEN_POCKET_ETH2 = "https://geth.mytokenpocket.vip";// TOKENPOCKET -ETH节点2

    private static final String MATH_ETH = "https://jsonrpc.maiziqianbao.net";// 麦子钱包 -ETH节点

    public static final String[] ETH_NODE_ADDRESS = {IMTOKEN_ETH, TOKEN_POCKET_ETH1, TOKEN_POCKET_ETH2, MATH_ETH};


    private static final String OFFICAIL_GCT = "http://120.79.6.248:1280";// GCT节点

    public static final String[] GCT_NODE_ADDRESS = {OFFICAIL_GCT};



    private static final String IMTOKEN_TRX = "https://tron-mainnet.token.im";// IMTOKEN -TRX节点

    public static final String TRON_LINK_TRX1 = "http://3.225.171.164:8090";// 波场官方节点

    public static final String TRON_LINK_TRX2 = "http://52.53.189.99:8090";// 波场官方节点

    private static final String TRON_TOKEN_POCKET = "https://trx.mytokenpocket.vip";// TokenPocket钱包节点

    private static final String OFFICIAL_NODE1 = "http://13.124.62.58:8090";// 波场官方节点 - 韩国首尔

    public static final String[] TRX_NODE_ADDRESS = {IMTOKEN_TRX, TRON_LINK_TRX1, TRON_LINK_TRX2, TRON_TOKEN_POCKET,
            OFFICIAL_NODE1};// TRON-LINK - TRX节点1



    private static final String FOX_FIL = "https://filfox.info/rpc/v0";// Filecoin - 飞狐

    private static final String MATH_FIL = "https://filecoin.maiziqianbao.net/rpc/v0";// Filecoin - 麦子节点

    public static final String[] FIL_NODE_ADDRESS = {FOX_FIL};// Filecoin节点

}
