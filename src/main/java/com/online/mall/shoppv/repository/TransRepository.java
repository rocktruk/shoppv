package com.online.mall.shoppv.repository;

import java.util.Optional;

import com.online.mall.shoppv.entity.Trans;

public interface TransRepository extends IExpandJpaRepository<Trans, String> {

	
	Optional<Trans> findTransByBackChannelAndBackChnlTraceNo(String backChannel,String backChnlTraceNo);
	
	
}
