package com.liuqi.business.constant;

import java.math.BigInteger;

public class MethodConstant {

    // transfer(address,uint256)
    public static final String TRANSFER = "a9059cbb";

    public static final String GET_INVITER = "getInviter(address)";

    // swapTokensForExactTokens(uint256 amountOut, uint256 amountInMax, address[] path, address to, uint256 deadline)
    // 换精确的amountOut目标币，最多只支付amountInMax数量的源币
    public static final String PANCAKE_SWAP_BUY1_METHOD = "swapTokensForExactTokens(uint256,uint256,address[],address,uint256)";
    public static final String PANCAKE_SWAP_BUY1 = "8803dbee";

    // swapExactTokensForTokens(uint256 amountIn, uint256 amountOutMin, address[] path, address to, uint256 deadline)
    // 用精确的amountIn源币换至少amountOutMin数量的目标币
    public static final String PANCAKE_SWAP_BUY2_METHOD = "swapExactTokensForTokens(uint256,uint256,address[],address,uint256)";
    public static final String PANCAKE_SWAP_BUY2 = "38ed1739";

    public static void main(String[] args) {
        System.out.println(new BigInteger("13a198cf010af8710", 16));
    }
}
