package com.webank.oracle.test.transaction.bac.auction.auctionFixedPrice;

import com.webank.oracle.test.transaction.bac.BAC001.BAC001;
import com.webank.oracle.test.transaction.bac.BAC002.BAC002;
import com.webank.oracle.test.base.BaseTest;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.math.BigInteger;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class AuctionFixedPriceTest extends BaseTest {

    @Test
    public void testAuctionFixedPrice() throws Exception {
        //Web3j
        int chainId = eventRegisterProperties.getEventRegisters().get(0).getChainId();
        int groupId = eventRegisterProperties.getEventRegisters().get(0).getGroup();
        Web3j web3j = getWeb3j(chainId, groupId);


        //--------------------------
        //部分公共参-----------------
        //--------------------------
        String bac001Address;//本案例用到的ft合约地址
        String bac002Address;//本案例用到的nft合约地址
        String auctionFixedPriceAddress;//拍卖合约的地址
        BigInteger bac002nftId = BigInteger.valueOf(1000L);//本案例用到的nft编号
        BigInteger auctionPrice = BigInteger.valueOf(100L);//拍卖定价
        BigInteger duration = BigInteger.valueOf(Instant.now().toEpochMilli() + 60 * 1000);//拍卖活动到期时间
        BigInteger initBalance = BigInteger.valueOf(5000L);//普通用户初始拥有的ft量


        //------------------------------------
        //步骤一：部署相关合约-----------------
        //------------------------------------
        //部署bac001合约，用于生成FT
        BAC001 bac001 = BAC001.deploy(web3j, credentials, contractGasProvider, "GDX car asset", "TTT", BigInteger.valueOf(1), BigInteger.valueOf(1000000)).send();
        bac001Address = bac001.getContractAddress();
        //部署bac002合约，用于生成NFT
        BAC002 bac002 = BAC002.deploy(web3j, credentials, contractGasProvider, "nft", "nft").send();
        bac002Address = bac002.getContractAddress();
        //部署AuctionFixedPrice合约，用于支持使用FT来拍卖NFT
        AuctionFixedPrice auctionFixedPrice = AuctionFixedPrice.deploy(web3j, credentials, contractGasProvider).send();
        auctionFixedPriceAddress = auctionFixedPrice.getContractAddress();


        //-------------------------------------
        //步骤二：商家发布一个定价拍卖消息-------
        //-------------------------------------
        //给商家(DaMing)发行一个NFT
        bac002.issueWithAssetURI(DaMing, bac002nftId, "http://wwww.tom.com", "tom Nft".getBytes()).send();
        //商家设置允许AuctionFixedPrice合约从自己账户下转走这个NFT
        BAC002 bac002DaMing = BAC002.load(bac002Address, web3j, credentialsDaMing, contractGasProvider);
        bac002DaMing.approve(auctionFixedPriceAddress, bac002nftId).send();

        //商家(DaMing)发布一个拍卖信息
        AuctionFixedPrice auctionFixedPriceDaMing = AuctionFixedPrice.load(auctionFixedPriceAddress, web3j, credentialsDaMing, contractGasProvider);
        auctionFixedPriceDaMing.createNFTAssetAuction(bac002Address, bac002nftId, bac001Address, auctionPrice, duration).send();


        //-----------------------------------
        //步骤三：客户准备阶段----------------
        //-----------------------------------
        //转给客户(alice)一定量的FT
        bac001.send(Alice, initBalance, "init balance".getBytes()).send();
        //客户(alice)设置允许AuctionFixedPrice合约从自己账户下转走这一些FT（可以根据商家设置的拍卖价格设置）
        BAC001 bac001Alice = BAC001.load(bac001Address, web3j, credentialsAlice, contractGasProvider);
        bac001Alice.approve(auctionFixedPriceAddress, auctionPrice).send();


        //-----------------------------------
        //步骤四：客户参与拍卖活动-------------
        //-----------------------------------
        //客户(alice)参与拍卖
        AuctionFixedPrice auctionFixedPriceAlice = AuctionFixedPrice.load(auctionFixedPriceAddress, web3j, credentialsAlice, contractGasProvider);
        auctionFixedPriceAlice.purchaseNFTAsset(bac002Address, bac002nftId).send();


        //---------------------------------
        //步骤四：验证----------------------
        //---------------------------------
        //验证商家（DaMing）剩余的ft
        BigInteger balanceDaMing = bac001.balance(DaMing).send();
        Assert.isTrue(auctionPrice.compareTo(balanceDaMing) == 0, "da balance error");
        //验证客户(alice)剩余的ft
        BigInteger balanceDaAlice = bac001.balance(Alice).send();
        Assert.isTrue(initBalance.subtract(auctionPrice).compareTo(balanceDaAlice) == 0, "alice balance error");
        //验证NFT归属
        String nftOwner = bac002.ownerOf(bac002nftId).send();
        assertEquals(nftOwner, Alice);
        System.out.println("test success");
    }
}
