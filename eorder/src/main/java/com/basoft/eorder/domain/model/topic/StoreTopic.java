package com.basoft.eorder.domain.model.topic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreTopic {
	private Long storeId;  //商户ID

	private Long tpId;  //主题ID

	private Date createTime;  //创建时间

	private String createUser;  //创建人

}

