package com.kss.stockDiscussion.service.newsFeedService;

import com.kss.stockDiscussion.domain.newsFeed.NewsFeed;
import com.kss.stockDiscussion.repository.newsFeedRepository.NewsFeedRepository;
import com.kss.stockDiscussion.web.dto.request.newsFeed.GetMyNewsFeedRequestDto;
import com.kss.stockDiscussion.web.dto.response.ResponseDto;
import com.kss.stockDiscussion.web.dto.response.newsFeed.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class NewsFeedServiceImpl implements NewsFeedService {

    private final NewsFeedMapper newsFeedMapper;
    private final NewsFeedRepository newsFeedRepository;

    @Override
    public ResponseEntity<? super GetMyNewsFeedResponseDto> getMyNewsFeeds(GetMyNewsFeedRequestDto dto) {
        Page<GetMyNewsFeedDto> newsFeedDtoPage;
        try {
            Long userId = dto.getUserId();
            int page = dto.getPage();
            int size = dto.getSize();
            Pageable pageable = PageRequest.of(page, size);
            Page<NewsFeed> newsFeedPage = newsFeedRepository.findByUserId(userId, pageable);
            newsFeedDtoPage = newsFeedPage.map(newsFeedMapper::toGetMyNewsFeedDto);
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return GetMyNewsFeedResponseDto.success(newsFeedDtoPage);
    }
}
