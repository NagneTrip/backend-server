package com.ssafy.nagne.service;

import com.ssafy.nagne.domain.Attraction;
import com.ssafy.nagne.error.NotFoundException;
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
        return attractionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Could not found article for " + id));
    }

    public List<Attraction> findAttractionsByKeyword(String keyword) {
        return attractionRepository.findAttractionsByKeyword(keyword);
    }
}
