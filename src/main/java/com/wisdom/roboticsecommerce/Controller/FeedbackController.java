package com.wisdom.roboticsecommerce.Controller;

import com.wisdom.roboticsecommerce.Dto.FeedbackDto;
import com.wisdom.roboticsecommerce.Dto.FeedbackProductResponse;
import com.wisdom.roboticsecommerce.Dto.PageableDto;
import com.wisdom.roboticsecommerce.Entities.Feedback;
import com.wisdom.roboticsecommerce.Mapper.PageMapper;
import com.wisdom.roboticsecommerce.Service.FeedbackService;
import com.wisdom.roboticsecommerce.Utils.ResponseUtil;
import com.wisdom.roboticsecommerce.Utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/feedback")
public class FeedbackController {
    @Autowired
    private FeedbackService feedbackService;
    @Autowired
    private PageMapper pageMapper;

    @PostMapping("create")
    private ResponseEntity<?> create(@RequestBody FeedbackDto feedbackDto) {
        Feedback feedback = feedbackService.create(feedbackDto);

        return ResponseUtil.success(feedback);
    }

    @GetMapping("detail")
    private ResponseEntity<?> getDetail(@RequestParam Long feedbackId) {
        Feedback feedback = feedbackService.getDetail(feedbackId);

        return ResponseUtil.success(feedback);
    }

    @PutMapping("reply/create")
    private ResponseEntity<?> createReply(@RequestBody FeedbackDto feedbackDto) {
        Feedback feedback = feedbackService.createReply(feedbackDto.getId(), feedbackDto.getReply());

        return ResponseUtil.success(feedback);
    }

    @PutMapping("reply/update")
    private ResponseEntity<?> updateReply(@RequestBody FeedbackDto feedbackDto) {
        Feedback feedback = feedbackService.updateReply(feedbackDto.getId(), feedbackDto.getReply());

        return ResponseUtil.success(feedback);
    }

    @DeleteMapping("reply/delete")
    private ResponseEntity<?> deleteReply(@RequestParam Long feedbackId) {
        feedbackService.deleteReply(feedbackId);

        return ResponseUtil.success("Xóa trả lời phản hồi thành công");
    }

    @PutMapping("status/update")
    private ResponseEntity<?> updateStatus(@RequestBody FeedbackDto feedbackDto) {
        Feedback feedback = feedbackService.updateStatus(feedbackDto.getId(), feedbackDto.getStatus());

        return ResponseUtil.success(feedback);
    }

    @GetMapping("all")
    private ResponseEntity<?> getAll(
            @RequestParam(required = false) Integer status,
            @ModelAttribute PageableDto pageableDto) {
        PageableDto pageableDtoReal = new PageableDto(0, 10, "create_at", "asc");
        Utils.modelMapper(true, true).map(pageableDto, pageableDtoReal);
        Pageable pageable = pageMapper.customPage(
                pageableDtoReal.getPageNo(),
                pageableDtoReal.getPageSize(),
                pageableDtoReal.getSortBy(),
                pageableDtoReal.getSortType()
        );
        Page<Feedback> feedbackList = feedbackService.getAll(status, pageable);

        return ResponseUtil.success(feedbackList);
    }

    @GetMapping("product/all")
    private ResponseEntity<?> getAllByProduct(@RequestParam Long productId, @ModelAttribute PageableDto pageableDto) {
        Pageable pageable = pageMapper.customPage(
                pageableDto.getPageNo(),
                pageableDto.getPageSize(),
                pageableDto.getSortBy().isEmpty() ? "id" : pageableDto.getSortBy(),
                pageableDto.getSortType().isEmpty() ? "desc" : pageableDto.getSortType());
        Page<FeedbackProductResponse> feedbackList = feedbackService.getAllByProduct(productId, pageable);

        return ResponseUtil.success(feedbackList);
    }
}
