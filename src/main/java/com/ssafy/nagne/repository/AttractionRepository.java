package com.ssafy.nagne.repository;

import com.ssafy.nagne.domain.Attraction;
import com.ssafy.nagne.page.Pageable;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AttractionRepository {

    Optional<Attraction> findById(@Param("id") Long id);

    List<Attraction> findAttractionsByKeyword(@Param("keyword") String keyword,
                                              Pageable pageable);
}
