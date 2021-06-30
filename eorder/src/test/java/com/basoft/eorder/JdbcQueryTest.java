package com.basoft.eorder;

import com.basoft.eorder.foundation.jdbc.query.JdbcProductQueryImpl;
import com.basoft.eorder.foundation.jdbc.query.inventory.hotel.JdbcInventoryHotelQueryImpl;
import com.basoft.eorder.interfaces.query.ProductSkuDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JdbcQueryTest {


    static ObjectMapper om =new ObjectMapper();

    public static void main(String...args){


        DataSource ds = new JdbcRepoTest().getDataSource();





    }

    public static void productQueryTest(DataSource ds){
        JdbcProductQueryImpl jpq = new JdbcProductQueryImpl(ds);
        Long prdId = new Long("535169855827284996");
        Map<String,Object> param = new HashMap<>();
        param.put("productId",prdId);
        final List<ProductSkuDTO> productSkuListByMap = jpq.getProductSkuListByMap(param);

        System.out.println("SKU of Product->"+prdId+" size:"+productSkuListByMap.size());

        try {
            final String s = om.writeValueAsString(productSkuListByMap);
            System.out.println(s);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }


    public static void inventHotelQueryTest(DataSource ds){
        JdbcInventoryHotelQueryImpl jhq = new JdbcInventoryHotelQueryImpl();
        Map<String,Object> param = new HashMap<>();
        String storeId = "635790301684634630";
        param.put("storeId", "635790301684634630");
        param.put("startTime", "");
        jhq.getIventHotelGroupList(param);

    }
}
