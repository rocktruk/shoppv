package com.online.mall.shoppv.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import com.online.mall.shoppv.entity.Trans;

public interface TransRepository extends IExpandJpaRepository<Trans, String> {

	
	Optional<Trans> findTransByBackChannelAndBackChnlTraceNo(String backChannel,String backChnlTraceNo);
	
	
	@Query("select t from Trans t where t.backChnlTraceNo = ?1 and t.trxCode = '110111'")
	Optional<Trans> findTraceNoByBackTraceNo(String traceNo);
}
