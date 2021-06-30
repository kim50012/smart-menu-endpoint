package com.basoft.eorder.interfaces.query.ghraphql;

import ch.qos.logback.core.CoreConstants;
import com.basoft.eorder.application.framework.ComponentProvider;
import com.basoft.eorder.interfaces.query.*;
import com.basoft.eorder.interfaces.query.postStoreSet.PostStoreSetDTO;
import com.basoft.eorder.interfaces.query.postStoreSet.PostStoreSetQuery;
import com.basoft.eorder.interfaces.query.retail.cms.*;
import com.basoft.eorder.interfaces.query.retailOrderService.RetailOrderServiceDTO;
import com.basoft.eorder.interfaces.query.retailOrderService.RetailOrderServiceQuery;
import com.google.common.collect.Maps;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.*;
import java.util.stream.Collectors;

public class RetailGraphqlQuery extends BaseGraphql {


    /**
     * 查询订单状态对应的订单数量列表
     *
     * @param componentFactory
     * @param <T>
     * @return
     */
    public static <T> DataFetcher<List<RetailOrderStatusDTO>> initRetailOrderStatusCount(ComponentProvider componentFactory) {
        RetailQuery rq = componentFactory.getComponent(RetailQuery.class);
        return env -> {
            String storeId = getStoreIdStr(env);
            Map<String, Object> param = Maps.newHashMap();

            if (StringUtils.isNotBlank(storeId)) {
                param.put("storeId", storeId);
            }
            String status = env.getArgument("status");
            if (StringUtils.isNotBlank(status)) {
                param.put("status", status);
            }
            String shippingType = env.getArgument("shippingType");
            if (StringUtils.isNotBlank(shippingType)) {
                param.put("shippingType", shippingType);
            }

            List<RetailOrderStatusDTO> statusCountList = rq.getRetailOrderStatusCountList(param);
            for (int i = 4; i < 13; i++) {
                int finalI = i;
                RetailOrderStatusDTO dto = statusCountList.stream().filter(t->t.getStatus()== finalI).findFirst()
                        .orElseGet(() -> null);
                if (dto == null) {
                    RetailOrderStatusDTO entity = new RetailOrderStatusDTO();
                    entity.setCount(0);
                    entity.setStatus(i);
                    statusCountList.add(entity);
                }
            }
             statusCountList.sort(Comparator.comparing(RetailOrderStatusDTO::getStatus));
            return statusCountList;
        };
    }


    /**
     * 查询售后订单状态对应的订单数量列表
     *
     * @param componentFactory
     * @return
     */
    public static DataFetcher<?> initRetailServOrderStatusCount(ComponentProvider componentFactory) {
        RetailQuery rq = componentFactory.getComponent(RetailQuery.class);

        return env -> {
            String storeId = getStoreIdStr(env);
            Map<String, Object> param = Maps.newHashMap();

            if (StringUtils.isNotBlank(storeId)) {
                param.put("storeId", storeId);
            }
            String servStatus = env.getArgument("servStatus");
            if (StringUtils.isNotBlank(servStatus)) {
                param.put("servStatus", servStatus);
            }

            List<RetailOrderStatusDTO> statusCountList = rq.getRetailServOrderStatusCountList(param);
            for (int i = 1; i < 6; i++) {
                int finalI = i;
                RetailOrderStatusDTO dto = statusCountList.stream().filter(t->t.getServStatus()== finalI).findFirst()
                        .orElseGet(() -> null);
                if (dto == null&&i!=3) {
                    RetailOrderStatusDTO entity = new RetailOrderStatusDTO();
                    entity.setCount(0);
                    entity.setServStatus(i);
                    statusCountList.add(entity);
                }
            }

            statusCountList.sort(Comparator.comparing(RetailOrderStatusDTO::getServStatus));
            return statusCountList;
        };
    }


        /**
         * retail订单查询
         *
         * @param componentFactory
         * @param <T>
         * @return
         */
    public static <T>  DataFetcher<PaginationDTO<OrderDTO>> initRetailOrders(ComponentProvider componentFactory) {
        RetailQuery req = componentFactory.getComponent(RetailQuery.class);

        return env -> {
            PaginationDTO<OrderDTO> pageOrder = new PaginationDTO<>();

            Map<String, Object> param = getBaseQueryParam(env);
            param = getOrderQueryParam(param, env);
            int total = req.getRetailOrderCount(param);

            if (total > 0) {
                List<OrderDTO> orderList = req.getRetailOrderList(param);
                pageOrder.setTotal(total);
                pageOrder.setDataList(orderList);
            } else {
                pageOrder.setDataList(new ArrayList<>());
            }

            return pageOrder;
        };
    }

    private static Map<String, Object> getOrderQueryParam(Map<String, Object> param, DataFetchingEnvironment env) {
        String id = env.getArgument("id");
        String status = env.getArgument("status");
        String status8From = env.getArgument("status8From");
        String changeStatus = env.getArgument("changeStatus");
        String openId = env.getArgument("openId");
        String startTime = env.getArgument("startTime");
        String endTime = env.getArgument("endTime");
        String spStartTime = env.getArgument("spStartTime");
        String spEndTime = env.getArgument("spEndTime");
        String mobile = env.getArgument("mobile");
        String shippingAddrCountry = env.getArgument("shippingAddrCountry");
        String shippingType = env.getArgument("shippingType");
        String custNm = env.getArgument("custNm");
        String storeNm = env.getArgument("storeNm");

        if (StringUtils.isNotBlank(id)) {
            param.put("id", id);
        }
        if (StringUtils.isNotBlank(status)) {
            param.put("status", status);
        }
        if (StringUtils.isNotBlank(changeStatus)) {
            param.put("changeStatus", changeStatus);
        }
        if (StringUtils.isNotBlank(openId)) {
            param.put("openId", openId);
        }
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            startTime = DateFormatUtils.format(Long.valueOf(startTime), CoreConstants.ISO8601_PATTERN);
            endTime = DateFormatUtils.format(Long.valueOf(endTime), CoreConstants.ISO8601_PATTERN);
            param.put("startTime", startTime);
            param.put("endTime", endTime);
        }
        if (StringUtils.isNotBlank(spStartTime) && StringUtils.isNotBlank(spEndTime)) {
            spStartTime = DateFormatUtils.format(Long.valueOf(spStartTime), CoreConstants.ISO8601_PATTERN);
            spEndTime = DateFormatUtils.format(Long.valueOf(spEndTime), CoreConstants.ISO8601_PATTERN);
            param.put("spStartTime", spStartTime);
            param.put("spEndTime", spEndTime);
        }
        if (StringUtils.isNotBlank(mobile)) {
            param.put("mobile", mobile);
        }
        if (StringUtils.isNotBlank(shippingAddrCountry)) {
            param.put("shippingAddrCountry", shippingAddrCountry);
        }
        if (StringUtils.isNotBlank(shippingType)) {
            param.put("shippingType", shippingType);
        }
        if (StringUtils.isNotBlank(custNm)) {
            param.put("custNm", custNm);
        }
        if (StringUtils.isNotBlank(storeNm)) {
            param.put("storeNm", storeNm);
        }
        if (StringUtils.isNotBlank(status8From)) {
            param.put("status8From", status8From);
        }
        if (StringUtils.isNotBlank(changeStatus)) {
            param.put("changeStatus", changeStatus);
        }

        return param;
    }


    /**
     * 查询售后记录
     *
     * @Param saveRetailOrderService
     * @author Dong Xifu
     */
    public static <T> DataFetcher<PaginationDTO<RetailOrderServiceDTO>> initRetailOrderServices(ComponentProvider componentFactory) {
        RetailOrderServiceQuery query = componentFactory.getComponent(RetailOrderServiceQuery.class);
        return env -> {
            // 查询参数
            Map<String, Object> param = getBaseQueryParam(env);

            String servId = env.getArgument("servId");
            String orderId = env.getArgument("orderId");
            String servCode = env.getArgument("servCode");
            String servType = env.getArgument("servType");
            String applyLinker = env.getArgument("applyLinker");
            String applyMobile = env.getArgument("applyMobile");
            String startTime = env.getArgument("startTime");
            String endTime = env.getArgument("endTime");
            String servStatus = env.getArgument("servStatus");
            String auditResult = env.getArgument("auditResult");

            if (StringUtils.isNotBlank(servId)) {
                param.put("servId", servId);
            }
            if (StringUtils.isNotBlank(orderId)) {
                param.put("orderId", orderId);
            }
            if (StringUtils.isNotBlank(servCode)) {
                param.put("servCode", servCode);
            }
            if (StringUtils.isNotBlank(servType)) {
                param.put("servType", servType);
            }
            if (StringUtils.isNotBlank(applyLinker)) {
                param.put("applyLinker", applyLinker);
            }
            if (StringUtils.isNotBlank(applyMobile)) {
                param.put("applyMobile", applyMobile);
            }
            if (StringUtils.isNotBlank(servStatus)) {
                param.put("servStatus", servStatus);
            }
            if (StringUtils.isNotBlank(auditResult)) {
                param.put("auditResult", auditResult);
            }

            if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
                startTime = DateFormatUtils.format(Long.valueOf(startTime), CoreConstants.ISO8601_PATTERN);
                endTime = DateFormatUtils.format(Long.valueOf(endTime), CoreConstants.ISO8601_PATTERN);

                param.put("startTime", startTime);
                param.put("endTime", endTime);
            }

            final int totalCount = query.getRetailOrderServiceCount(param);
            final List<RetailOrderServiceDTO> list = query.getRetailOrderServiceListByMap(param);

            return new PaginationDTO(totalCount, list);
        };
    }

    /**
     * 根据订单编号查询售后记录
     *
     * @param componentFactory
     * @return
     */
    public static DataFetcher<?> initRetailAfterSalesApplys(ComponentProvider componentFactory) {
        RetailOrderServiceQuery query = componentFactory.getComponent(RetailOrderServiceQuery.class);
        return env -> {
            // 查询参数
            String orderId = env.getArgument("orderId");

            final List<RetailOrderServiceDTO> list = query.getAfterSalesApplys(0L,0L);

            return list;
        };
    }

    /**
         * retail 商品详情
         *
         * @param componentFactory
         * @param <T>
         * @return
         */
    public static <T> DataFetcher<ProductDTO> initRetail(ComponentProvider componentFactory) {

        RetailQuery req = componentFactory.getComponent(RetailQuery.class);
        ProductQuery pq = componentFactory.getComponent(ProductQuery.class);
        InventoryRetailQuery ir = componentFactory.getComponent(InventoryRetailQuery.class);

        return env -> {
            Long productId = env.getArgument("id");
            Map<String, Object> param = new HashMap<>();
            param.put("id", productId);
            param.put("productId", productId);
            param.put("storeId", getStoreIdStr(env));
            ProductDTO productDTO = pq.getProductById(productId);//商品详情
            if (productDTO == null) {
                return productDTO;
            }

            List<ProductAloneStandardDTO> standardDTOList = req.getProductStandardListByMap(param); //查询产品下的standard
            productDTO.setAloneStandardList(standardDTOList);

            List<ProductSkuDTO> skuDTOList = pq.getProductSkuListByMap(param); //查询产品下的规格

            skuDTOList.sort(Comparator.comparingInt(ProductSkuDTO::getDisOrder));

            List<ProSkuItemNameDTO> skuStandItemNames = req.getSkuStandardList(param); //查询规格下的standardItem

            List<InventoryRetailDTO>  inventoryRetails = ir.getInventoryRetailListByMap(param);//查询剩余库存

            Map<Long, List<ProSkuItemNameDTO>> itemCollect = skuStandItemNames.stream().collect(Collectors.groupingBy(ProSkuItemNameDTO::getProductSkuId));
            skuDTOList.stream().forEach(s-> {
                inventoryRetails.forEach(i -> {
                    if(i.getProdSkuId()!= null) {
                        if (i.getProdSkuId().longValue() == s.getId()) {
                            s.setInvTotal(i.getInvBalance());
                        }
                    }
                });
                s.setStandardItemList(itemCollect.get(s.getId()));//给规格赋值standardItem
            });

            productDTO.setPsdList(skuDTOList);
            return productDTO;
        };
    }


    /**
     * 库存列表
     *
     * @param componentFactory
     * @param <T>
     * @return
     */
    public static <T> DataFetcher<PaginationDTO<InventoryRetailDTO>> initInventoryRetails(ComponentProvider componentFactory) {
        InventoryRetailQuery ir = componentFactory.getComponent(InventoryRetailQuery.class);
        RetailQuery req = componentFactory.getComponent(RetailQuery.class);

        return env -> {
            String productId = env.getArgument("productId");
            String categoryId = env.getArgument("categoryId");
            String status = env.getArgument("status");
            String name = env.getArgument("name");

            Map<String, Object> param = getBaseQueryParam(env);

            if (StringUtils.isNotBlank(productId)) {
                param.put("productId", productId);
            }
            if (StringUtils.isNotBlank(categoryId)) {
                param.put("categoryId", categoryId);
            }
            if (StringUtils.isNotBlank(status)) {
                param.put("status", status);
            }
            if (StringUtils.isNotBlank(name)) {
                param.put("name", name);
            }


            int totalCount = ir.getProductRetailCount(param);
            List<InventoryRetailDTO> inventoryList = ir.getProductRetailListByMap(param);//查询商品
            if(totalCount>0) {
                //获取standar为1的产品id
                List<Long> prodIds = inventoryList.stream().filter(i -> i.getIsStandard() == 1).map(i -> i.getProdId()).collect(Collectors.toList());
                //获取standar为0的产品id
                List<Long> noStandardprodIds = inventoryList.stream().filter(i -> i.getIsStandard() == 0).map(k -> k.getProdId()).collect(Collectors.toList());

                List<StandardAndItemDTO> noStandardAndItems = req.getNoStandardAndItemList(noStandardprodIds.toArray(new Long[noStandardprodIds.size()]));
                Map<Long, List<StandardAndItemDTO>> noStandAndItemCollect = noStandardAndItems.stream().collect(Collectors.groupingBy(StandardAndItemDTO::getProdId));

                //规格和item组合放入库存list
                List<StandardAndItemDTO> standardAndItems = req.getStandardAndItemList(prodIds.toArray(new Long[prodIds.size()]));//获取规格和item组合
                Map<Long, List<StandardAndItemDTO>> standAndItemCollect = standardAndItems.stream().collect(Collectors.groupingBy(StandardAndItemDTO::getProdId));

                inventoryList.forEach(i -> {
                    if (i.getIsStandard() == 0) {
                        i.setStandardAndItems(noStandAndItemCollect.get(i.getProdId()));
                    } else {
                        i.setStandardAndItems(standAndItemCollect.get(i.getProdId()));
                    }
                });
            }
            return new PaginationDTO(totalCount, inventoryList);
        };
    }


    /**
     * 查询模版列表
     *
     * @rern int
     * @author Dong Xifu
     */
    public static  <T> DataFetcher<PaginationDTO<RetailTemplateDTO>> initProductAloneStandardTemplates(ComponentProvider componentFactory) {
        RetailTemplateQuery query = componentFactory.getComponent(RetailTemplateQuery.class);
        return env -> {
            // 查询参数
            String tId = env.getArgument("tId");
            String name = env.getArgument("name");
            String status = env.getArgument("status");

            Map<String, Object> param = getBaseQueryParam(env);

            if (StringUtils.isNotBlank(tId)) {
                param.put("tId", tId);
            }
            if (StringUtils.isNotBlank(name)) {
                param.put("name", name);
            }
            if (StringUtils.isNotBlank(status)) {
                param.put("status", status);
            }

            final int totalCount = query.getProductAloneStandardTemplateCount(param);
            final List<RetailTemplateDTO> list = query.getProductAloneStandardTemplateListByMap(param);

            return new PaginationDTO(totalCount, list);
        };
    }


    /**
     * 查询模版详情
     *
     * @param componentFactory
     * @param <T>
     * @return
     */
    public static <T> DataFetcher<RetailTemplateDTO> initRetailTemplate(ComponentProvider componentFactory) {
        RetailQuery req = componentFactory.getComponent(RetailQuery.class);
        RetailTemplateQuery rq = componentFactory.getComponent(RetailTemplateQuery.class);

        return env -> {
            String tId = env.getArgument("tId");
            String tStatus = env.getArgument("tStatus");

            Map<String, Object> param = getBaseQueryParam(env);
            param.put("tId", tId);
            param.put("tStatus", tStatus);
            param.put("isTemplete", 1);
            RetailTemplateDTO templateDTO = rq.getProductAloneStandardTemplateDto(param);
            List<ProductAloneStandardDTO> standardDTOList = req.getProductStandardListByMap(param); //查询产品下的standard
            if (standardDTOList != null)
                templateDTO.setAloneStandardList(standardDTOList);
            return templateDTO;
        };

    }



    /**
     * 查询商户配送设置列表
     *
     * @Param savePostStoreSet
     * @return int
     * @author Dong Xifu
     */
    public static <T> DataFetcher<PaginationDTO<PostStoreSetDTO>> initPostStoreSets(ComponentProvider componentFactory) {
        PostStoreSetQuery query = componentFactory.getComponent(PostStoreSetQuery.class);
        return env -> {
            // 查询参数
            String name = env.getArgument("name");
            String pssId = env.getArgument("pssId");
            String isFree = env.getArgument("isFree");
            String status = env.getArgument("status");
            String targetCountryCode = env.getArgument("targetCountryCode");
            Map<String, Object> param = getBaseQueryParam(env);
            if (StringUtils.isNotBlank(name)) {
                param.put("name", name);
            }
            if (StringUtils.isNotBlank(pssId)) {
                param.put("pssId", pssId);
            }
            if (StringUtils.isNotBlank(isFree)) {
                param.put("isFree", isFree);
            }
            if (StringUtils.isNotBlank(status)) {
                param.put("status", status);
            }
            if (StringUtils.isNotBlank(targetCountryCode)) {
                param.put("targetCountryCode", targetCountryCode);
            }

            final int totalCount = query.getPostStoreSetCount(param);
            final List<PostStoreSetDTO> list = query.getPostStoreSetListByMap(param);

            return new PaginationDTO(totalCount, list);
        };
    }


    /**
     * 配送设置详情
     *
     * @param componentFactory
     * @param <T>
     * @return
     */
    public static <T> DataFetcher<PostStoreSetDTO> initPostStoreSet(ComponentProvider componentFactory) {
        PostStoreSetQuery query = componentFactory.getComponent(PostStoreSetQuery.class);
        return env -> {
            Map<String, Object> param = getBaseQueryParam(env);
            String pssId = env.getArgument("pssId");
            param.put("pssId", pssId);

            return query.getPostStoreSetDto(param);
        };
    }


}
