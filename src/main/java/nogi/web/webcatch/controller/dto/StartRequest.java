package nogi.web.webcatch.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class StartRequest {
    private List<String> contents;

    private List<Map<String, String>> steps;
}
