package com.webank.oracle.test.bac.auction;

import com.webank.oracle.test.base.BaseTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class AuctionUnFixedPriceTest extends BaseTest {

    @Test
    public void testAuctionFixedPrice() throws Exception {
        //------------------------------------
        //步骤一：部署相关合约-----------------
        //------------------------------------
        //部署bac001合约，用于生成FT
        // TODO 实现
        //部署bac002合约，用于生成NFT
        // TODO 实现
        //部署AuctionFixedPrice合约，用于支持使用FT来拍卖NFT
        // TODO 实现



        //-------------------------------------
        //步骤二：商家发布一个定价拍卖消息-------
        //-------------------------------------
        //商家发行一个NFT,并设置允许AuctionFixedPrice合约从自己账户下转走这个NFT
        // TODO 实现
        //商家发布一个拍卖信息
        // TODO 实现



        //-----------------------------------
        //步骤三：客户参与拍卖活动-------------
        //-----------------------------------
        //客户1发行一定量的FT,并设置允许AuctionFixedPrice合约从自己账户下转走这一些FT（可以根据商家设置的拍卖价格设置）
        // TODO 实现
        //客户1参与拍卖
        // TODO 实现

        //客户2发行一定量的FT,并设置允许AuctionFixedPrice合约从自己账户下转走这一些FT（可以根据商家设置的拍卖价格设置）
        // TODO 实现
        //客户2参与拍卖（出高价）
        // TODO 实现

        //客户1继续参与拍卖（出更高价）
        // TODO 实现


        //--------------------------------
        //步骤四：商家截止拍卖-------------
        //--------------------------------
        //商家截止本轮活动，合约自动与胜出者达成交易，并归还失败者ft
        // TODO 实现


        //---------------------------------
        //步骤五：验证----------------------
        //---------------------------------
        //验证商家剩余的ft
        // TODO 实现
        //验证竞争获胜的客户剩余的ft
        // TODO 实现
        //验证竞争失败的客户剩余的ft
        // TODO 实现
        //验证NFT归属
        // TODO 实现
    }
}
