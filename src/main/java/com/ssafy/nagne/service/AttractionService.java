package com.ssafy.nagne.service;

import static lombok.Lombok.checkNotNull;

import com.ssafy.nagne.domain.Attraction;
import com.ssafy.nagne.error.NotFoundException;
import com.ssafy.nagne.page.Pageable;
import com.ssafy.nagne.repository.AttractionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AttractionService {

    private final AttractionRepository attractionRepository;

    public Attraction findById(Long id) {
        checkNotNull(id, "id must be provided");

        return attractionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Could not found attraction for " + id));
    }

    public List<Attraction> findAttractionsByKeyword(String keyword, Long attractionTypeId, Pageable pageable) {
        checkNotNull(keyword, "keyword must be provided");

        return attractionRepository.findAttractionsByKeyword(keyword, attractionTypeId, pageable);
    }
}
